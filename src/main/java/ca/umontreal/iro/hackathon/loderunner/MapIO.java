/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.umontreal.iro.hackathon.loderunner;

import java.util.ArrayList;

/**
 *
 * @author Admin123
 */
public class MapIO {

    public static int xvPlayerFromMap(String[] inputMap){
        int xvPlayer = xyvPlayerFromMap(inputMap)[0];
        return xvPlayer;
    }

    public static int yvPlayerFromMap(String[] inputMap){
        int yvPlayer = xyvPlayerFromMap(inputMap)[1];
        return yvPlayer;
    }     
    
    //Functions to get data from subclass
    public static int xvDoorFromMap(String[] inputMap){
        int xvDoor = xyvDoorFromMap(inputMap)[0];
        return xvDoor;
    }
    public static int yvDoorFromMap(String[] inputMap){
        int yvDoor = xyvDoorFromMap(inputMap)[1];
        return yvDoor;
    }
    
    public static int[] rgxvCoinsFromMap(String[] inputMap){
        ArrayList<Integer> temp;
        temp =  xypCoinsFromMap(inputMap).get(0);
        //Stupid conversion
        int[] rgxvCoinCount = new int[temp.size()];
        for (int i=0; i < rgxvCoinCount.length; i++)
        {
            rgxvCoinCount[i] = temp.get(i).intValue();
        }
        return rgxvCoinCount;
    }
    public static int[] rgyvCoinsFromMap(String[] inputMap){
        ArrayList<Integer> temp;
        temp =  xypCoinsFromMap(inputMap).get(1);
        //Stupid conversion
        int[] rgyvCoinCount = new int[temp.size()];
        for (int i=0; i < rgyvCoinCount.length; i++)
        {
            rgyvCoinCount[i] = temp.get(i).intValue();
        }
        return rgyvCoinCount;        
    }
    public static int iCoinsFromMap(String[] inputMap){
        int ceCoinCount;
        ceCoinCount = xypCoinsFromMap(inputMap).get(0).size();
        //For some reason my other code relies on this being wrong
        return ceCoinCount-1;
    }      
    
    
    //Returns the character at any given point
    public static char GetCharFromPosition(String[] inputMap,int inp_x, int inp_y){
        
        char chr_ReturnData;
        //Grab the correct string based on the y vlaue
        //Make sure x and y are not out of boundsx
        //If they are then dont return a valid character
        //My invalid character will be lowercase z 'z'
        chr_ReturnData = 'z';
        if ( (inp_x < 0) || (inp_y < 0) ) {
            return chr_ReturnData;
        }
        
        if ( (inp_y < inputMap.length) && (inp_x >= 0)){
            String str_line = inputMap[inp_y];
            //Make sure the x value is valid as well.
            if ( (inp_x < str_line.length() ) && (inp_x >= 0)) {
                chr_ReturnData =str_line.charAt(inp_x);    
            }           
        }
        
        return chr_ReturnData;
    }     

    public static int[] xyvPlayerFromMap(String[] inputMap) {
        int xyvPoint[] = new int[2];
        for (int i=0; i<inputMap.length; i++) {
            String str_line = inputMap[i];
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
    public static int[] xyvDoorFromMap(String[] inputMap) {
        int xyvPoint[] = new int[2];
        for (int i=0; i<inputMap.length; i++) {
            String str_line = inputMap[i];
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
    
    //Takes a map, replaces the player char with a ' ' and puts a new player
    //char at where the player is
    public static int[] UpdatePlayerXY(String[] inputMap, int xin, int yin) {
        int originalX = xvPlayerFromMap(inputMap);
        int originalY = yvPlayerFromMap(inputMap);
        StringBuilder newLine = new StringBuilder(inputMap[originalY]);
        newLine.setCharAt(originalX,' ');
        String newLineReal = new String(newLine.toString());
        inputMap[originalY] = newLineReal;
        
        StringBuilder newLine2 = new StringBuilder(inputMap[yin]);
        newLine2.setCharAt(xin,'&');
        String newLine2Real = new String(newLine2.toString());
        inputMap[yin] = newLine2Real;
        
        System.out.println("New map in function");
        DebugPrinter.PrintMap(inputMap);
        
        return null;
    }  
    
    
    public static ArrayList<ArrayList<Integer>> xypCoinsFromMap(String[] inputMap) {
        //Populates the array called pts_CoinCoords
        ArrayList< ArrayList<Integer> > xypCoins;
        xypCoins = new ArrayList<>();
        ArrayList<Integer> xvpCoins = new ArrayList<Integer>();
        ArrayList<Integer> yvpCoins = new ArrayList<Integer>();
        
        for (int i=0; i<inputMap.length; i++) {
            String str_line = inputMap[i];
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
    
}
