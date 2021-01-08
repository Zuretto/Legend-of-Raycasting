package dungeoncrawler.gameobjects;

import java.awt.*;

public abstract class Entity extends GameObject{

    protected boolean canDelete;
    protected Image entityImage;

    public Entity(){
        canDelete = false;
    }

    public boolean checkIfCanDelete(){
        return canDelete;
    }

    public abstract Image getEntityImage();


}
