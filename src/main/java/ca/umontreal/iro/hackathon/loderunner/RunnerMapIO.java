package ca.umontreal.iro.hackathon.loderunner;

/**
    $ is the ascii map character for the coins that need to be found.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.lang.model.element.Element;

public class RunnerMapIO {
    //Symbolic constants
    private static final int UP = 1;
    private static final int LEFT = 2;
    private static final int DOWN = 3;
    private static final int RIGHT = 4;    

    //Variables
    //Local version of the map
    String[] map_maplocal;      
    //Array of moves to win
    int[] num_listofmoves = new int[500];
    //Position in array of moves, can be reset if moves are recalculated
    int num_movespos = 0;
    //Arrays of coordinates
    //Contains a list of x,y coordinates of where the coins are located.
    //The number of coins depends on the level.
    int num_coincount = 0;
    int[] xpt_CoinXPos = new int[20];
    int[] ypt_CoinYPos = new int[20];
    //Player X and Y coordinates
    int num_PlayerX = 0;
    int num_PlayerY = 0;
    //Door X and Y coordinates
    int num_DoorX = 0;
    int num_DoorY = 0;
    //List for testing combinations
    //Needs to have all coins but one
    //Used in the pathfinding algorythm.
    int lst_CoinOrder[];
    int num_FactPos = 0;
    //Solved list
    int lst_CoinOrderSolved[] = new int[50]; 
    
    //Class constructor
    public RunnerMapIO(){   
        
    }    
    
    //Set the map to use (an array of strings)
    public int SetMapArray(String[] inp_StartGrid){
        map_maplocal = inp_StartGrid.clone();
        return 0;
    }    
    
    //Functions to get data from subclass
    public int GetDoorX(){
        return num_DoorX;
    }
    public int GetDoorY(){
        return num_DoorY;
    }
    
    public int GetPlayerX(){
        return num_PlayerX;
    }
    
    public int GetPlayerY(){
        return num_PlayerY;
    }
    
    public int[] GetCoinX(){
        return xpt_CoinXPos;
    }
    
    public int[] GetCoinY(){
        return ypt_CoinYPos;
    }
    public int GetCoinAmount(){
        return num_coincount;
    }
    
    
    //Function to calculate al the stuff for the getter functions
    public int FindStuff(){
        FindCoinCoords();
        FindPlayerCoords();
        FindDoorCoords();
        
        return 0;
    }

    public int FindCoinCoords() {
        num_coincount = 0;
        //Populates the array called pts_CoinCoords
        //Debug prints
        
        for (int i=0; i<map_maplocal.length; i++) {
            String str_line = map_maplocal[i];
            for (int j = 0; j < str_line.length(); j++){
                int cur_char = str_line.charAt(j);
                if (cur_char == '$') {
                    xpt_CoinXPos[num_coincount] = j;
                    ypt_CoinYPos[num_coincount] = i;
                    num_coincount++;
                }
            }
            System.out.println(str_line);
        }
        //Decrement coint count so it reflects the number of coins found.
        num_coincount--;
        System.out.println("X and y coordinates of coins");
        System.out.println("X coords");
        System.out.println(java.util.Arrays.toString(xpt_CoinXPos));
        System.out.println("Y Coords");
        System.out.println(java.util.Arrays.toString(ypt_CoinYPos));
        
        return 0;
    }    

    public int FindPlayerCoords() {

        for (int i=0; i<map_maplocal.length; i++) {
            String str_line = map_maplocal[i];
            for (int j = 0; j < str_line.length(); j++){
                int cur_char = str_line.charAt(j);
                if (cur_char == '&') {
                    num_PlayerX = j;
                    num_PlayerY = i;
                }
            }
            System.out.println(str_line);
        }         
        System.out.println("X and y coordinates of player");
        System.out.println("X coords");
        System.out.println(num_PlayerX);
        System.out.println("Y Coords");
        System.out.println(num_PlayerY);
        
        return 0;
    }
    
    public int FindDoorCoords() {

        for (int i=0; i<map_maplocal.length; i++) {
            String str_line = map_maplocal[i];
            for (int j = 0; j < str_line.length(); j++){
                int cur_char = str_line.charAt(j);
                if (cur_char == 'S') {
                    num_DoorX = j;
                    num_DoorY = i;
                }
            }
            System.out.println(str_line);
        }         
        System.out.println("X and y coordinates of Door");
        System.out.println("X coords");
        System.out.println(num_DoorX);
        System.out.println("Y Coords");
        System.out.println(num_DoorY);
        
        return 0;
    }
    
    
}
