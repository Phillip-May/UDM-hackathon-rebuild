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
    
    //Class constructor
    public RunnerMapIO(){   
        
    }
    
    //Functions to get data from subclass
    public int xvDoorFromMap(){
        int xvDoor = xyvDoorFromMap()[0];
        return xvDoor;
    }
    public int yvDoorFromMap(){
        int yvDoor = xyvDoorFromMap()[1];
        return yvDoor;
    }
    
    public int xvPlayerFromMap(){
        int xvPlayer = xyvPlayerFromMap()[0];
        return xvPlayer;
    }
    
    public int yvPlayerFromMap(){
        int yvPlayer = xyvPlayerFromMap()[1];
        return yvPlayer;
    }
    
    public int[] rgxvCoinsFromMap(){
        ArrayList<Integer> temp;
        temp =  xypCoinsFromMap().get(0);
        //Stupid conversion
        int[] rgxvCoinCount = new int[temp.size()];
        for (int i=0; i < rgxvCoinCount.length; i++)
        {
            rgxvCoinCount[i] = temp.get(i).intValue();
        }
        return rgxvCoinCount;
    }
    
    public int[] rgyvCoinsFromMap(){
        ArrayList<Integer> temp;
        temp =  xypCoinsFromMap().get(1);
        //Stupid conversion
        int[] rgyvCoinCount = new int[temp.size()];
        for (int i=0; i < rgyvCoinCount.length; i++)
        {
            rgyvCoinCount[i] = temp.get(i).intValue();
        }
        return rgyvCoinCount;        
    }
    public int iCoinsFromMap(){
        int ceCoinCount;
        ceCoinCount = xypCoinsFromMap().get(0).size();
        //For some reason my other code relies on this being wrong
        return ceCoinCount-1;
    }
    
    
    //Returns the character at any given point
    public char GetCharFromPosition(int inp_x, int inp_y){
        
        char chr_ReturnData;
        //Grab the correct string based on the y vlaue
        //Make sure x and y are not out of boundsx
        //If they are then dont return a valid character
        //My invalid character will be lowercase z 'z'
        chr_ReturnData = 'z';
        
        if ( (inp_y < super.mpMapCurrent.length) && (inp_x >= 0)){
            String str_line = super.mpMapCurrent[inp_y];
            //Make sure the x value is valid as well.
            if ( (inp_x < str_line.length() ) && (inp_x >= 0)) {
                chr_ReturnData =str_line.charAt(inp_x);    
            }           
        }
        
        return chr_ReturnData;
    } 

    public ArrayList<ArrayList<Integer>> xypCoinsFromMap() {
        //Populates the array called pts_CoinCoords
        //Debug prints
        ArrayList< ArrayList<Integer> > xypCoins;
        xypCoins = new ArrayList<>();
        ArrayList<Integer> xvpCoins = new ArrayList<Integer>();
        ArrayList<Integer> yvpCoins = new ArrayList<Integer>();
        
        for (int i=0; i<super.mpMapCurrent.length; i++) {
            String str_line = super.mpMapCurrent[i];
            for (int j = 0; j < str_line.length(); j++){
                int cur_char = str_line.charAt(j);
                if (cur_char == '$') {
                    xvpCoins.add(j);
                    yvpCoins.add(i);
                }
            }
            System.out.println(str_line);
        }
        xypCoins.add(xvpCoins);
        xypCoins.add(yvpCoins);
        return xypCoins;
    }    

    public int[] xyvPlayerFromMap() {
        int xyvPoint[] = new int[2];
        for (int i=0; i<super.mpMapCurrent.length; i++) {
            String str_line = super.mpMapCurrent[i];
            for (int j = 0; j < str_line.length(); j++){
                int cur_char = str_line.charAt(j);
                if (cur_char == '&') {
                    xyvPoint[0] = j;
                    xyvPoint[1] = i;
                }
            }
            System.out.println(str_line);
        }
        return xyvPoint;
    }
    
    //Returns a an array with the x and y values of the doors position
    public int[] xyvDoorFromMap() {
        int xyvPoint[] = new int[2];
        for (int i=0; i<super.mpMapCurrent.length; i++) {
            String str_line = super.mpMapCurrent[i];
            for (int j = 0; j < str_line.length(); j++){
                int cur_char = str_line.charAt(j);
                if (cur_char == 'S') {
                    xyvPoint[0] = j;
                    xyvPoint[1] = i;
                }
            }
        }
        return xyvPoint;
    }
    
    
}
