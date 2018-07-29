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
public class RunnerPather extends RunnerPathFind {
    
    //Stores the map data
    String[] map_maplocalASCII;
    
    //Class constructor
    public RunnerPather(){
        
    }
    
    public int SetASCIIMapArray(String[] inp_StartGrid){
        map_maplocalASCII = inp_StartGrid.clone();
        return 0;
    }
    
    
    //A star algorythm based off of this page
    //https://www.raywenderlich.com/4946/introduction-to-a-pathfinding
    public int GetTurnsTwoPoints(int inp_x1, int inp_y1,int inp_x2,
                                    int inp_y2){
        //Two lists of x and y coordinates in order
        //One list for tiles being considered
        ArrayList<Integer> pts_OpenList = new ArrayList<Integer>();
        //One list for tiles already considered
        ArrayList<Integer> pts_ClosedList = new ArrayList<Integer>();
        
        
        //Initialise starting data for pathfinding loop
        
        //Main loop
        
        
        int result;
        result = java.lang.Math.abs(inp_x1 - inp_x2);
        return result;
    }    
    
}
