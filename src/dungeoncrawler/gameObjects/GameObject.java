package dungeoncrawler.gameObjects;

import dungeoncrawler.Map;

public abstract class GameObject {
    protected double posX;
    protected double posY;

    public double calculateEuclideanDistance(GameObject other) {
        return Math.sqrt(Math.pow(this.posX - other.posX, 2) + Math.pow(this.posY - other.posY, 2));
    }

    public abstract void update(Map map);

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }
}
