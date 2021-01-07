package dungeoncrawler;

public class Team {
    private double posX;
    private double posY; //x and y positions
    private double dirX;
    private double dirY; //direction vector
    private double planeX;
    private double planeY; //FOV

    public Team(double posX, double posY, double dirX, double dirY, double viewAngle){
        this.posX = posX;
        this.posY = posY;
        this.dirX = dirX;
        this.dirY = dirY;
        this.planeX = dirX * Math.cos(-viewAngle) - dirY * Math.sin(-viewAngle);
        this.planeY = dirX * Math.sin(-viewAngle) + dirY * Math.cos(-viewAngle);
    }

    public double getPosX(){
        return posX;
    }
    public double getPosY(){
        return posY;
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
}
