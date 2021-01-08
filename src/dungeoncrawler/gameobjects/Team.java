package dungeoncrawler.gameobjects;

import dungeoncrawler.Map;

import java.awt.event.KeyEvent;

public class Team extends GameObject {

    private final long keyInputCooldownInMiliseconds = 500;
    private long lastUpdateTime;
    private long nextKeyPressedTime;
    private int lastKeyPressed;

    private double dirX;
    private double dirY; //direction vector
    private double planeX;
    private double planeY; //FOV

    private final Controller controller;

    public Team(double posX, double posY, double dirX, double dirY, double viewAngle, Controller controller) {
        this.posX = (double)Math.round(posX - 0.5) + 0.5;
        this.posY = (double)Math.round(posY - 0.5) + 0.5;
        this.dirX = dirX;
        this.dirY = dirY;
        this.planeX = dirX * Math.cos(-viewAngle) - dirY * Math.sin(-viewAngle);
        this.planeY = dirX * Math.sin(-viewAngle) + dirY * Math.cos(-viewAngle);
        this.controller = controller;
        lastUpdateTime = System.currentTimeMillis();
        nextKeyPressedTime = System.currentTimeMillis();
    }

    public double getDirX(){
        return dirX;
    }
    public double getDirY(){
        return dirY;
    }
    public double getPlaneX() {
        return planeX;
    }
    public double getPlaneY() {
        return planeY;
    }

    @Override
    public void update(Map map) {
        //fraction - delta time over keyCooldown, for 60UPS it's 16 or 17.
        double deltaTOverKeyCooldown = (double)(System.currentTimeMillis() - lastUpdateTime) / (double)(keyInputCooldownInMiliseconds);
        lastUpdateTime = System.currentTimeMillis();
        if(System.currentTimeMillis() > nextKeyPressedTime) {
            lastKeyPressed = 0;
            posX = (double)Math.round(posX - 0.5) + 0.5;
            posY = (double)Math.round(posY - 0.5) + 0.5;
            dirX = (double)Math.round(dirX);
            dirY = (double)Math.round(dirY);
            if (controller.isRequestingUp()) {
                if (dirY == 0 && map.getTileWall((int) (this.posX + dirX), (int) posY) == 0) lastKeyPressed = KeyEvent.VK_UP;
                else if (dirX == 0 && map.getTileWall((int) posX, (int) (posY + dirY)) == 0) lastKeyPressed = KeyEvent.VK_UP;
                nextKeyPressedTime = System.currentTimeMillis() + keyInputCooldownInMiliseconds;
            }
            else if (controller.isRequestingDown()) {
                if (dirY == 0 && map.getTileWall((int) (this.posX - dirX), (int) posY) == 0) lastKeyPressed = KeyEvent.VK_DOWN;
                else if (dirX == 0 && map.getTileWall((int) posX, (int) (posY - dirY)) == 0) lastKeyPressed = KeyEvent.VK_DOWN;
                nextKeyPressedTime = System.currentTimeMillis() + keyInputCooldownInMiliseconds;
            }
            else if (controller.isRequestingLeft()) {
                lastKeyPressed = KeyEvent.VK_LEFT;
                nextKeyPressedTime = System.currentTimeMillis() + keyInputCooldownInMiliseconds;
            }
            else if (controller.isRequestingRight()) {
                lastKeyPressed = KeyEvent.VK_RIGHT;
                nextKeyPressedTime = System.currentTimeMillis() + keyInputCooldownInMiliseconds;
            }
        }
        else{
            if(lastKeyPressed == KeyEvent.VK_UP) {
                this.posX += dirX * deltaTOverKeyCooldown;
                this.posY += dirY * deltaTOverKeyCooldown;
            }
            else if(lastKeyPressed == KeyEvent.VK_DOWN) {
                this.posX -= dirX * deltaTOverKeyCooldown;
                this.posY -= dirY * deltaTOverKeyCooldown;
            }
            else if(lastKeyPressed == KeyEvent.VK_LEFT) {
                double oldDirX = dirX;
                dirX = dirX * Math.cos(Math.PI/2 * deltaTOverKeyCooldown) - dirY * Math.sin(Math.PI/2 * deltaTOverKeyCooldown);
                dirY = oldDirX * Math.sin(Math.PI/2 * deltaTOverKeyCooldown) + dirY * Math.cos(Math.PI/2 * deltaTOverKeyCooldown);
                double oldPlaneX = planeX;
                planeX = planeX * Math.cos(Math.PI/2 * deltaTOverKeyCooldown) - planeY * Math.sin(Math.PI/2 * deltaTOverKeyCooldown);
                planeY = oldPlaneX * Math.sin(Math.PI/2 * deltaTOverKeyCooldown) + planeY * Math.cos(Math.PI/2 * deltaTOverKeyCooldown);
            }
            else if(lastKeyPressed == KeyEvent.VK_RIGHT) {
                double oldDirX = dirX;
                dirX = dirX * Math.cos(-Math.PI/2 * deltaTOverKeyCooldown) - dirY * Math.sin(-Math.PI/2 * deltaTOverKeyCooldown);
                dirY = oldDirX * Math.sin(-Math.PI/2 * deltaTOverKeyCooldown) + dirY * Math.cos(-Math.PI/2 * deltaTOverKeyCooldown);
                double oldPlaneX = planeX;
                planeX = planeX * Math.cos(-Math.PI/2 * deltaTOverKeyCooldown) - planeY * Math.sin(-Math.PI/2 * deltaTOverKeyCooldown);
                planeY = oldPlaneX * Math.sin(-Math.PI/2 * deltaTOverKeyCooldown) + planeY * Math.cos(-Math.PI/2 * deltaTOverKeyCooldown);
            }
        }

    }
}
