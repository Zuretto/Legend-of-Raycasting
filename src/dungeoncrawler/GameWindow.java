package dungeoncrawler;

import dungeoncrawler.gameObjects.Input;
import dungeoncrawler.gameObjects.Team;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import javax.swing.JFrame;

public class GameWindow extends JFrame {

    private GameWindowResourceManager resourceManager;
    private Canvas canvas;

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
        addKeyListener((KeyListener) input);
        add(canvas);
        pack();

        canvas.createBufferStrategy(2);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void renderWorld(Team team, Map map, double[] ZBuffer){
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        Graphics graphics = bufferStrategy.getDrawGraphics();
        graphics.setColor(Color.blue);
        graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
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

        graphics.dispose();
        bufferStrategy.show();
    }


}