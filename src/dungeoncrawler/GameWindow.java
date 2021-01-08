package dungeoncrawler;

import dungeoncrawler.gameobjects.Entity;
import dungeoncrawler.gameobjects.GameObject;
import dungeoncrawler.iohandler.Input;
import dungeoncrawler.gameobjects.Team;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.JFrame;

public class GameWindow extends JFrame {



    private GameWindowResourceManager resourceManager;
    private Canvas canvas;

    private double[] ZBuffer;

    public GameWindow(int width, int height, Input input) {

        setTitle("Java project");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(width, height);



        try {
            this.resourceManager = new GameWindowResourceManager();
        } catch (IOException e) {
            e.printStackTrace();
        }
        canvas = new Canvas();
        canvas.setPreferredSize((new Dimension(width,height)));
        canvas.setFocusable(false);
        addKeyListener(input);
        add(canvas);
        pack();

        canvas.createBufferStrategy(2);

        setLocationRelativeTo(null);
        setVisible(true);

        ZBuffer = new double[this.getWidth()];
    }
    public void render(Team team, Map map, ArrayList<Entity> entities){
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        Graphics graphics = bufferStrategy.getDrawGraphics();
        graphics.setColor(Color.blue);

        graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        renderWorld(team, map, graphics);
        renderEntities(team, entities, graphics);

        graphics.dispose();
        bufferStrategy.show();
    }
    private void renderWorld(Team team, Map map, Graphics graphics){
        for(int stripeX = 0; stripeX < this.getWidth(); ++stripeX){
            //calclate ray position and direction
            double cameraX = 2 * stripeX / (double) this.getWidth() - 1; //-1 for left pixel, 1 for right pixel
            double rayDirX = team.getDirX() + team.getPlaneX() * cameraX;
            double rayDirY = team.getDirY() + team.getPlaneY() * cameraX;
            //mapX and mapY - X and Y coordinates of team on the map
            int mapX = (int)team.getPosX();
            int mapY = (int)team.getPosY();
            //length of the ray from one X or Y-side to next X, Y sides
            //double deltaDistX = (rayDirX == 0) ? 0 : ((rayDirY == 0) ? 1 : Math.abs(1.0 / rayDirX));
            //double deltaDistY = (rayDirX == 0) ? 0 : ((rayDirY == 0) ? 1 : Math.abs(1.0 / rayDirY));
            double deltaDistX = Math.abs(1.0 / rayDirX);
            double deltaDistY = Math.abs(1.0 / rayDirY);
            //length of ray from CURRENT position to next x, y sides
            double sideDistX;
            double sideDistY;
            //what direction to step in x, y direction(either +1 or -1)
            int stepX;
            int stepY;
            boolean hit = false; //was there a wall hit?
            int side = 0;        //direction of wall hit - NS or EW
            //calculate step and initial sideDist
            if(rayDirX < 0){
                stepX = -1;
                sideDistX = (team.getPosX() - mapX) * deltaDistX;
            }
            else{
                stepX = 1;
                sideDistX = (mapX + 1.0 - team.getPosX()) * deltaDistX;
            }
            if(rayDirY < 0){
                stepY = -1;
                sideDistY = (team.getPosY() - mapY) * deltaDistY;
            }
            else{
                stepY = 1;
                sideDistY = (mapY + 1.0 - team.getPosY()) * deltaDistY;
            }
            //perform DDA - ray casting algorithm
            while(!hit){
                //jump to next map square in x-dir OR y-dir
                if(sideDistX < sideDistY){
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                }
                else{
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }
                if(map.getTileWall(mapX, mapY) > 0) hit = true;
            }
            //Calculate non-euclidean distance between player and the ray
            double perpWallDist;
            if(side == 0) perpWallDist = (mapX - team.getPosX() + (1.0 - stepX) / 2) / rayDirX;
            else          perpWallDist = (mapY - team.getPosY() + (1.0 - stepY) / 2) / rayDirY;
            //SET THE ZBUFFER FOR THE SPRITE CASTING
            ZBuffer[stripeX] = perpWallDist; //perpendicular distance is used
            //calculate height of line that should be drawn on the screen
            int lineHeight = (int)((double)this.getHeight() / perpWallDist);
            //calculate lowest and highest pixel to fill in current stripe
            int drawStart = this.getHeight() / 2 - lineHeight / 2;
            graphics.drawLine(stripeX, drawStart, stripeX, drawStart + lineHeight);
            //texturing calculations
            int texNum = map.getTileWall(mapX, mapY) - 1;          //texture number
            double wallX;                                          //where exactly the wall was hit
            if (side == 0) wallX = team.getPosY() + perpWallDist * rayDirY;
            else           wallX = team.getPosX() + perpWallDist * rayDirX;
            wallX -= Math.floor(wallX);
            int textureX = (int)(wallX * Constants.WALL_TEXTURE_WIDTH);           //x coordinate on the texture
            Image texImg = resourceManager.wallImages[texNum];
            //image, X1, Y1, X2, Y2, SubX1, SubY1, SubX2, SubY2, observer
            graphics.drawImage(texImg, stripeX, drawStart, stripeX + 1, drawStart + lineHeight,
                    textureX, 0, textureX + 1, Constants.WALL_TEXTURE_HEIGHT, this);
        }

    }

    private void renderEntities(Team team, ArrayList<Entity> entities, Graphics graphics){
        entities.sort((Comparator.<GameObject>
                comparingDouble(character1 -> character1.calculateEuclideanDistance(team))
                .thenComparingDouble(character2 -> character2.calculateEuclideanDistance(team))));

        for(Entity e : entities) {
            //translate sprite position to relative to camera
            double spriteX = e.getPosX() - team.getPosX();
            double spriteY = e.getPosY() - team.getPosY();;
            //transform sprite with the inverse camera matrix
            // [ planeX   dirX ] -1                                       [ dirY      -dirX ]
            // [               ]       =  1/(planeX*dirY-dirX*planeY) *   [                 ]
            // [ planeY   dirY ]                                          [ -planeY  planeX ]
            double invDet = 1.0 / (team.getPlaneX() * team.getDirY() - team.getDirX() * team.getPlaneY()); //required for correct matrix multiplication
            //this is actually the depth inside the screen, that what Z is   in 3D
            double transformX = invDet * (team.getDirY() * spriteX - team.getDirX() * spriteY);
            double transformY = invDet * (- team.getPlaneY() * spriteX + team.getPlaneX() * spriteY);
            int spriteScreenX = (int) ((this.getWidth() / 2) * (1 + transformX / transformY));
            //calculate height of the sprite on screen
            int spriteHeight = Math.abs((int)(this.getHeight() / (transformY))); //using 'transformY' instead of the real distance prevents fisheye
            //calculate lowest and highest pixel to fill in current stripe
            int drawStartY = -spriteHeight / 2 + this.getHeight() / 2;
            //if(drawStartY < 0) drawStartY = 0;
            int drawEndY = spriteHeight / 2 + this.getHeight() / 2;
            //if(drawEndY >= casterHeight) drawEndY = casterHeight - 1;
            //calculate width of the sprite
            int spriteWidth = Math.abs((int) (this.getHeight() / (transformY)));
            int drawStartX = -spriteWidth / 2 + spriteScreenX;
            if(drawStartX < 0) drawStartX = 0;
            int drawEndX = spriteWidth / 2 + spriteScreenX;
            if(drawEndX >= this.getWidth()) drawEndX = this.getWidth() - 1;
            for(int stripe = drawStartX; stripe < drawEndX; stripe++){
                int texX = (int)((256 * (stripe - (-spriteWidth / 2 + spriteScreenX)) * Constants.ENTITY_TEXTURE_WIDTH / spriteWidth) / 256);
                if(transformY > 0 && stripe > 0 && stripe < this.getWidth() && transformY < ZBuffer[stripe]){
                    System.out.println(texX);
                    graphics.drawImage(e.getEntityImage(), stripe, drawStartY, stripe + 1, drawEndY,
                            texX, 0, texX + 1, Constants.ENTITY_TEXTURE_HEIGHT, this);
                    }
                    //sf::Sprite lineSprite(resManager->enemyTexturesPx[enemy->getType()][enemy->calculateState()][texX]);
                    //lineSprite.setScale(1, (double)(drawEndY - drawStartY)/enemyHeight); //stretches line of pixels to the bottom
                    //lineSprite.move(stripe, drawStartY);
                    //target.draw(lineSprite);

                }
            }
        }

    }
