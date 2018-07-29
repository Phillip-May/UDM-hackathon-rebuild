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

    //Returns the character at any given point
    public char GetCharPosition(int inp_x, int inp_y){
        
        char chr_ReturnData;
        //Grab the correct string based on the y vlaue
        String str_line = map_maplocalASCII[inp_y];
        chr_ReturnData =str_line.charAt(inp_x);
        System.out.println("Found charater is:");
        System.out.println(chr_ReturnData);
        return chr_ReturnData;
    }    
    
    //Class for storing map tile data
    public class MapTile {
        int xposition;
        int yposition;
        //Number of turns to get to from start position
        int gValue;
        //Estimate of turns from here to destination
        //Estimate ignores terain and simply finds absolute distance
        int fValue;
        //The tile from which this one was accessed
        int ChildXPosition;
        int ChildYPosition;
        

        public MapTile(){
            xposition = 0;
            yposition = 0;
            gValue = 0;
            fValue = 0;
            ChildXPosition = 0;
            ChildYPosition = 0;
        }
    }    
    
    //A star algorythm based off of this page
    //https://www.raywenderlich.com/4946/introduction-to-a-pathfinding
    public int GetTurnsTwoPoints(int inp_x1, int inp_y1,int inp_x2,
                                    int inp_y2){
        //Two lists of x and y and F values in order
        //For the record I would use a structure in c, a class is overkill
        //One list for tiles being considered
        ArrayList<MapTile> pts_OpenList = new ArrayList<>();
        //One list for tiles already considered
        ArrayList<MapTile> pts_ClosedList = new ArrayList<>();
        //List for adjacent tiles
        ArrayList<MapTile> pts_Adjacent = new ArrayList<>();
        //Map Tile of the current x,y and g values
        MapTile pnt_CurrentTile = new MapTile();
        //Map tile of the current adjacent tile being considered
        MapTile pnt_CurrentAdjTile = new MapTile();
        
        //Add starting point to open list
        MapTile StartTile = new MapTile();
        StartTile.xposition = inp_x1;
        StartTile.yposition = inp_y1;
        StartTile.gValue = 0;
        pts_OpenList.add(StartTile);
        //Save Destination X and Y values
        int DestinationX = inp_x2;
        int DestinationY = inp_y2;
        
        //Initialise starting data for pathfinding loop

        //Main loop runs while tiles are still being considered
        while( pts_OpenList.isEmpty() == false ){
            
            MapTile ComparisonMapTile = pts_OpenList.get(0);
            
            //Find openlist square with lowest F score
            for (int i = 0; i < pts_OpenList.size(); i++){
                ComparisonMapTile = pts_OpenList.get(i);
                if (pnt_CurrentTile.gValue < ComparisonMapTile.fValue){
                    pnt_CurrentTile = ComparisonMapTile;
                }
            }
            //Add it to the current closed list
            pts_ClosedList.add(pnt_CurrentTile);
            //Remove it from the open lists
            pts_OpenList.remove(pnt_CurrentTile);
            
            //Check if closed list contains destination tile
            //If it does then break from the loop
            for (int i = 0; i < pts_ClosedList.size(); i++){
                
                if ( (ComparisonMapTile.xposition == DestinationX) &&
                     (ComparisonMapTile.yposition == DestinationY) ){
                    //Leave the main while loop
                    break;
                }                
            }
            
            
            //Retrieve all walkable tiles from currently considered map tiles
            pts_Adjacent = GetAdjacentTiles(pnt_CurrentTile);
            
            
            //Iterate through all adjacent square being considered
            for (int i = 0; i < pts_Adjacent.size(); i++){
                pnt_CurrentAdjTile = pts_Adjacent.get(i);
                //First check if current adjacent tile is in closed list
                //If it is just skip it
                boolean ShouldConsiderCurrentTile = true;
                MapTile CurrentClosedTile = new MapTile();
                for (int j = 0; j < pts_ClosedList.size(); j++){
                    CurrentClosedTile = pts_ClosedList.get(j);
                    if ( (pnt_CurrentAdjTile.xposition == CurrentClosedTile.xposition) &&
                         (pnt_CurrentAdjTile.yposition == CurrentClosedTile.yposition) ){
                        ShouldConsiderCurrentTile = false;
                    }
                }
                if (ShouldConsiderCurrentTile == false){
                    //Skip to next for loop iteration
                    continue;
                }
                boolean CurrentTileInOpenList = false;
                MapTile CurrentOpenTile = new MapTile();
                //Check if current adjacnet tile is in the open list
                //And if is store the index of it
                int ind_CurrentTileInOpen = -1;
                for (int j = 0; j < pts_OpenList.size(); j++){
                    CurrentOpenTile = pts_ClosedList.get(j);
                    if ( (pnt_CurrentAdjTile.xposition == CurrentClosedTile.xposition) &&
                         (pnt_CurrentAdjTile.yposition == CurrentClosedTile.yposition) ){
                        CurrentTileInOpenList = true;
                        ind_CurrentTileInOpen = j;
                    }
                }
                
                //If not in open list then add it
                if (CurrentTileInOpenList == false){
                    pts_OpenList.add(pnt_CurrentAdjTile);
                } //It is already in the Open list
                else if (CurrentTileInOpenList == true){
                    //If current G score is lower then current G score
                    //Then update it using ind_CurrentTileInOpen
                    if ( CurrentOpenTile.gValue < pnt_CurrentAdjTile.gValue){
                        CurrentOpenTile.gValue = pnt_CurrentAdjTile.gValue;
                    }
                }
            //End of for loop of adjacent tiles
            }
        //End of main while loop for checking all adjacent tiles.
        }                 
    //Now recover the best path
        
    int result;
    result = java.lang.Math.abs(inp_x1 - inp_x2);
    return result;   
    }
    
    //Given A Map tiles returns an arraylist of all adjacent tiles
    private ArrayList<MapTile> GetAdjacentTiles(MapTile inp_CurrentTile){
        ArrayList<MapTile> pts_AdjacentTiles = new ArrayList<>();
        int pnt_CurX = inp_CurrentTile.xposition;
        int pnt_CurY = inp_CurrentTile.yposition;
        int pnt_CurG = inp_CurrentTile.gValue;
        MapTile pts_AddedTile =  new MapTile();
        
        //Try to add all the tiles
        
        //Above
        
        //Below
        
        //Left (-x)
        //Tile 1 left and down needs to be an '@'
        if ( GetCharPosition( (pnt_CurX - 1),(pnt_CurY - 1) ) == '@'){
            pts_AddedTile.xposition = pnt_CurX;
            pts_AddedTile.yposition = pnt_CurY;
            //Inherit previous tile and add 1 
            pts_AddedTile.fValue = (pnt_CurG + 1);
            //Add it to the array list
            pts_AdjacentTiles.add(pts_AddedTile);
        }
        
        //Right (+x)
        //Tile 1 Right and down needs to be an '@'
        if ( GetCharPosition( (pnt_CurX + 1),(pnt_CurY - 1) ) == '@'){
            pts_AddedTile.xposition = pnt_CurX;
            pts_AddedTile.yposition = pnt_CurY;
            //Inherit previous tile and add 1 
            pts_AddedTile.fValue = (pnt_CurG + 1);
            pts_AdjacentTiles.add(pts_AddedTile);
        }        
        
        return pts_AdjacentTiles;
    }
}
