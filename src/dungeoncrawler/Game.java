package dungeoncrawler;

import dungeoncrawler.gameObjects.Input;
import dungeoncrawler.gameObjects.Team;
import dungeoncrawler.gameObjects.TeamController;

import java.awt.*;

public class Game {

    private GameWindow gameWindow;
    private Map map;
    private Team team;
    private double[] ZBuffer;
    private Input input;

    public Game(int width, int height){
        input = new Input();
        gameWindow = new GameWindow(width, height, input);
        ZBuffer = new double[gameWindow.getWidth()];
        map = new Map("data/maps/temp_map.txt");
        team = new Team(2, 2, 1, 0, Constants.CAMERA_ANGLE, new TeamController(input));
    }

    public void update(){
        team.update(map);
    }

    public void render(){
        gameWindow.renderWorld(team, map, ZBuffer);
    }
}
