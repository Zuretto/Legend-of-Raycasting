package dungeoncrawler;

import java.awt.*;

public class Game {

    private GameWindow gameWindow;
    private Rectangle rectangle;

    public Game(int width, int height){
        gameWindow = new GameWindow(width, height);
        rectangle = new Rectangle(0, 0, 50, 50);
    }

    public void update(){

    }

    public void render(){
        gameWindow.render(this);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}
