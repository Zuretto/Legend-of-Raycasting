package dungeoncrawler;


import javax.swing.*;

public class Launcher{

    public static void main(String[] args){
        //GameWindow display = new GameWindow(800, 480);
        new Thread(new GameLoop(new Game(800,400))).start();
    }

}
