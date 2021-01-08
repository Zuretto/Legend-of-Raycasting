package dungeoncrawler;


public class Launcher{

    public static void main(String[] args){
        //GameWindow display = new GameWindow(800, 480);
        new Thread(new GameLoop(new Game(Constants.SCR_WIDTH, Constants.SCR_HEIGHT))).start();
    }
}
