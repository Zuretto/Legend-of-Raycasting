package dungeoncrawler.gameobjects.enemy;

import dungeoncrawler.Map;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class Skeleton extends Enemy{

    static private Image image;

    public Skeleton(double posX, double posY){
        super(posX, posY);

        if (image == null) {
            try {
                image = ImageIO.read(new File("data/pics/skeleton.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public Image getEntityImage() {
        return image;
    }

    @Override
    public void update(Map map) {

    }
}
