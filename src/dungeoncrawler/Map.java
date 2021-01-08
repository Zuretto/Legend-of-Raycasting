package dungeoncrawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Map {
    private Integer[][] mapArray;

    public Map(String fileName){
        try{
            int rows = 0;
            int columns = 0;
            File mapFile = new File(fileName);
            Scanner rowCounter = new Scanner(mapFile);
            while(rowCounter.hasNextLine()){
                String line = rowCounter.nextLine();
                if(line.equals("OBJECTS_LOADING:")) break;
                columns = line.trim().split(" ").length;
                rows++;
            }
            mapArray = new Integer[rows][columns];
            Scanner mapReader = new Scanner(mapFile);
            for(int i = 0; i < rows; ++i){
                String[] data = mapReader.nextLine().trim().split(" ");
                for(int j = 0; j < columns; ++j){
                    mapArray[i][j] = Integer.parseInt(data[j]);
                }
            }
            mapReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        /*
        for(Integer[] i : mapArray){
            for(Integer j : i){
                System.out.print(j);
            }
            System.out.println();
        }
         */
    }



    public int getTileWall(int tileX, int tileY){
        return mapArray[tileX][tileY];
    }

    public static void main(String[] args){
        Map t = new Map("data/maps/temp_map.txt");
    }
}
