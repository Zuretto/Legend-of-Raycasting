package dungeoncrawler.gameobjects.enemy;

import dungeoncrawler.Map;
import dungeoncrawler.gameobjects.Entity;

import java.awt.*;

public abstract class Enemy extends Entity {

    protected int healthPoints;


    public Enemy(double posX, double posY){
        super();

        this.posX = (double)Math.round(posX - 0.5) + 0.5;
        this.posY = (double)Math.round(posY - 0.5) + 0.5;
    }



}
