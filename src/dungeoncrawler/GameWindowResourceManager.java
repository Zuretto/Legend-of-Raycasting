package dungeoncrawler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameWindowResourceManager {

    protected final Image[] wallImages;
    
    public GameWindowResourceManager() throws IOException {
        wallImages = new Image[Constants.WALL_TEXTURES_TYPES];
        wallImages[0] = ImageIO.read(new File("data/pics/wooden plank.png"));
        wallImages[1] = ImageIO.read(new File("data/pics/chiseled stone brick.png"));
        wallImages[2] = ImageIO.read(new File("data/pics/cobblestone.png"));
        wallImages[3] = ImageIO.read(new File("data/pics/cracked stone brick.png"));
        wallImages[4] = ImageIO.read(new File("data/pics/nether brick.png"));
        wallImages[5] = ImageIO.read(new File("data/pics/stone brick.png"));
        wallImages[6] = ImageIO.read(new File("data/pics/stone.png"));
        wallImages[7] = ImageIO.read(new File("data/pics/andesite.png"));
    }

}
