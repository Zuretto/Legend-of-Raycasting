package dungeoncrawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Map {
    private ArrayList<ArrayList<Integer>> mapArray;

    public Map(String fileName){
        try{
            File mapFile = new File(fileName);
            Scanner mapReader = new Scanner(mapFile);
            while(mapReader.hasNextLine()) {
                String data = mapReader.nextLine();
                System.out.println(data);
            }
            mapReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        Map t = new Map("data/maps/temp_map.txt");
    }
}
