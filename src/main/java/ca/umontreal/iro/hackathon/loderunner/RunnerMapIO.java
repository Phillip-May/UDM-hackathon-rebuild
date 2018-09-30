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

public class RunnerMapIO extends Runner{  

    //Variables
    //The number of coins depends on the level.
    int ceCoinCount = 0;
    int[] rgCoinCountxv = new int[20];
    int[] rgCoinCountyv = new int[20];
    //Player X and Y coordinates
    int xvPlayer = 0;
    int yvPlayer = 0;
    //Door X and Y coordinates
    int xvDoor = 0;
    int yvDoor = 0;
    
    //Class constructor
    public RunnerMapIO(){   
        
    }
    
    //Functions to get data from subclass
    public int GetDoorX(){
        return xvDoor;
    }
    public int GetDoorY(){
        return yvDoor;
    }
    
    public int GetPlayerX(){
        return xvPlayer;
    }
    
    public int GetPlayerY(){
        return yvPlayer;
    }
    
    public int[] GetCoinX(){
        return rgCoinCountxv;
    }
    
    public int[] GetCoinY(){
        return rgCoinCountyv;
    }
    public int GetCoinAmount(){
        return ceCoinCount;
    }
    
    
    //Function to calculate al the stuff for the getter functions
    public int FindStuff(){
        FindCoinCoords();
        FindPlayerCoords();
        FindDoorCoords();
        
        return 0;
    }

    public int FindCoinCoords() {
        ceCoinCount = 0;
        //Populates the array called pts_CoinCoords
        //Debug prints
        
        for (int i=0; i<super.mpMapCurrent.length; i++) {
            String str_line = super.mpMapCurrent[i];
            for (int j = 0; j < str_line.length(); j++){
                int cur_char = str_line.charAt(j);
                if (cur_char == '$') {
                    rgCoinCountxv[ceCoinCount] = j;
                    rgCoinCountyv[ceCoinCount] = i;
                    ceCoinCount++;
                }
            }
            System.out.println(str_line);
        }
        //Decrement coint count so it reflects the number of coins found.
        ceCoinCount--;
        System.out.println("X and y coordinates of coins");
        System.out.println("X coords");
        System.out.println(java.util.Arrays.toString(rgCoinCountxv));
        System.out.println("Y Coords");
        System.out.println(java.util.Arrays.toString(rgCoinCountyv));
        
        return 0;
    }    

    public int FindPlayerCoords() {

        for (int i=0; i<super.mpMapCurrent.length; i++) {
            String str_line = super.mpMapCurrent[i];
            for (int j = 0; j < str_line.length(); j++){
                int cur_char = str_line.charAt(j);
                if (cur_char == '&') {
                    xvPlayer = j;
                    yvPlayer = i;
                }
            }
            System.out.println(str_line);
        }         
        System.out.println("X and y coordinates of player");
        System.out.println("X coords");
        System.out.println(xvPlayer);
        System.out.println("Y Coords");
        System.out.println(yvPlayer);
        
        return 0;
    }
    
    public int FindDoorCoords() {

        for (int i=0; i<super.mpMapCurrent.length; i++) {
            String str_line = super.mpMapCurrent[i];
            for (int j = 0; j < str_line.length(); j++){
                int cur_char = str_line.charAt(j);
                if (cur_char == 'S') {
                    xvDoor = j;
                    yvDoor = i;
                }
            }
            System.out.println(str_line);
        }         
        System.out.println("X and y coordinates of Door");
        System.out.println("X coords");
        System.out.println(xvDoor);
        System.out.println("Y Coords");
        System.out.println(yvDoor);
        
        return 0;
    }
    
    
}
