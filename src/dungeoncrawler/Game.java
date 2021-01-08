package dungeoncrawler;

import dungeoncrawler.gameobjects.Entity;
import dungeoncrawler.gameobjects.enemy.Skeleton;
import dungeoncrawler.iohandler.Input;
import dungeoncrawler.gameobjects.Team;
import dungeoncrawler.iohandler.TeamController;

import java.util.ArrayList;

public class Game {

    private GameWindow gameWindow;
    private Map map;
    private Team team;

    private Input input;
    private ArrayList<Entity> entities;

    public Game(int width, int height){
        input = new Input();
        gameWindow = new GameWindow(width, height, input);

        map = new Map("data/maps/temp_map.txt");
        team = new Team(2, 2, 1, 0, Constants.CAMERA_ANGLE, new TeamController(input));

        entities = new ArrayList<>();

        entities.add(new Skeleton(4, 2));
        entities.add(new Skeleton(4, 3));
        entities.add(new Skeleton(4, 4));
        entities.add(new Skeleton(4, 5));
    }

    public void update(){
        team.update(map);
    }

    public void render(){
        gameWindow.render(team, map, entities);
    }
}
