package dungeoncrawler;

import java.awt.*;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

public class GameWindow extends JFrame {

    private Canvas canvas;

    public GameWindow(int width, int height) {

        setTitle("Java project");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(width, height);

        canvas = new Canvas();
        canvas.setPreferredSize((new Dimension(width,height)));
        canvas.setFocusable(false);
        add(canvas);
        pack();

        canvas.createBufferStrategy(2);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void render(Game game){
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        Graphics graphics = bufferStrategy.getDrawGraphics();

        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        Rectangle rectangle = game.getRectangle();
        graphics.setColor(Color.blue);
        graphics.fillRect((int) rectangle.getX(), (int)rectangle.getY(),
                          (int) rectangle.getWidth(), (int) rectangle.getHeight()
        );
        /*
        sf::RectangleShape sky(sf::Vector2f(casterWidth, casterHeight / 2));
        sky.setFillColor(sf::Color(108, 158, 222));
        target.draw(sky);
        sf::RectangleShape floor(sf::Vector2f(casterWidth, casterHeight / 2));
        floor.move(0, casterHeight/2);
        floor.setFillColor(sf::Color(56, 68, 82));
        target.draw(floor);
        auto map_vect = worldMap->getMapVector();
        for(int x = 0; x < casterWidth; x++){
            //calculate ray position and direction
            double cameraX = 2 * x / (double)casterWidth - 1; //-1 for left pixel, 1 for right pixel
            double rayDirX = player->getdirX() + player->getplaneX() * cameraX;
            double rayDirY = player->getdirY() + player->getplaneY() * cameraX;
            //which box of the map we're in
            int mapX = int(player->getPosX());
            int mapY = int(player->getPosY());
            // length of ray from one x or y-side to next x or y-side
            // "1" is for the length of vector [rayDirX, rayDirY].
            double deltaDistX = (rayDirY == 0) ? 0 : ((rayDirX == 0) ? 1 : std::abs(1 / rayDirX));
            double deltaDistY = (rayDirX == 0) ? 0 : ((rayDirY == 0) ? 1 : std::abs(1 / rayDirY));
            //length of ray from current position to next x or y-side
            double sideDistX;
            double sideDistY;
            //what direction to step in x or y-direction (either +1 or -1)
            int stepX;
            int stepY;
            int hit = 0; //was there a wall hit?
            int side; //was a NS or a EW wall hit?
            //calculate step and initial sideDist
            if(rayDirX < 0){
                stepX = -1;
                sideDistX = (player->getPosX() - mapX) * deltaDistX;
            }
            else{
                stepX = 1;
                sideDistX = (mapX + 1.0 - player->getPosX()) * deltaDistX;
            }
            if(rayDirY < 0){
                stepY = -1;
                sideDistY = (player->getPosY() - mapY) * deltaDistY;
            }
            else{
                stepY = 1;
                sideDistY = (mapY + 1.0 - player->getPosY()) * deltaDistY;
            }
            //perform DDA
            while (hit == 0){
                //jump to next map square, OR in x-direction, OR in y-direction
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
                //Check if ray has hit a wall
                if(map_vect[mapX][mapY] > 0) hit = 1;
            }
            //Calculate distance projected on camera direction (Euclidean distance will give fisheye effect!)
            double perpWallDist;
            if(side == 0) perpWallDist = (mapX - player->getPosX() + (1 - stepX) / 2) / rayDirX;
            else          perpWallDist = (mapY - player->getPosY() + (1 - stepY) / 2) / rayDirY;
            //SET THE ZBUFFER FOR THE SPRITE CASTING
            ZBuffer[x] = perpWallDist; //perpendicular distance is used
            //Calculate height of line to draw on screen
            int lineHeight = (int)(casterHeight / perpWallDist);
            //calculate lowest and highest pixel to fill in current stripe
            int drawStart = -lineHeight / 2 + casterHeight / 2;
            //texturing calculations
            int texNum = map_vect[mapX][mapY] - 1;          //texture number
            double wallX;                                   //where exactly the wall was hit
            if (side == 0) wallX = player->getPosY() + perpWallDist * rayDirY;
            else           wallX = player->getPosX() + perpWallDist * rayDirX;
            wallX -= std::floor(wallX);
            int textureX = int(wallX * texWidth);           //x coordinate on the texture
            //Drawing
            sf::Sprite lineSprite(resManager->wallTexturesPx[texNum][textureX]);
            lineSprite.setScale(1, (double)lineHeight/texHeight); //stretches line of pixels to the bottom
            lineSprite.move(x, drawStart);
            target.draw(lineSprite);
        }
         */



        graphics.dispose();
        bufferStrategy.show();
    }
}