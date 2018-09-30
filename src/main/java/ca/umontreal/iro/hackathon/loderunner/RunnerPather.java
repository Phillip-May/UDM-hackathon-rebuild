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
    //Map data is stored in Runner class
    private ArrayList<MapTile> pts_OpenList;
    private ArrayList<MapTile> pts_ClosedList;
    
    //Class constructor
    public RunnerPather(){
        
    }
    
    public int SetASCIIMapArray(String[] inp_StartGrid){
        super.mpMapCurrent = inp_StartGrid.clone();
        return 0;
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
    
    //Class for storing map tile data
    public class MapTile {
        int xposition;
        int yposition;
        //Number of turns to get to from start position
        int gValue;
        //Estimate of turns from here to destination
        int hValue;
        //Estimate ignores terain and simply finds absolute distance
        private int fValue;
        //The tile from which this one was accessed
        int ChildXPosition = -1;
        int ChildYPosition = -1;
        
        public int GetfValue(){
            fValue = gValue+hValue;
            return fValue;
        }
        
    }
    
    //A star algorythm based off of this page
    //https://www.raywenderlich.com/4946/introduction-to-a-pathfinding
    public int FindTurnsTwoPoints(int inp_x1, int inp_y1,
                                  int inp_x2,int inp_y2){
        pts_OpenList = new ArrayList<>();
        pts_ClosedList = new ArrayList<>();
        boolean foundapath = false;
        //Map Tile of the current x,y and g values
        MapTile pnt_CurrentTile = new MapTile();
        //Map tile of the current adjacent tile being considered
        ArrayList<MapTile> pts_AdjacentTiles = new ArrayList<>();
        MapTile pnt_CurrentBestTile = new MapTile();
        MapTile pnt_curTile;
        
        //Start
        pnt_CurrentTile.xposition = inp_x1;
        pnt_CurrentTile.yposition = inp_y1;
        pnt_CurrentTile.gValue = 0;
        boolean currentComparison = false;
        int compX = -1;
        int compY = -1;
        
        //Save destination to global
        //destX = inp_x2;
        //destY = inp_y2;

        //Add original to open list
        pts_OpenList.add(pnt_CurrentTile);
        
        do {
            pnt_CurrentBestTile = new MapTile();
            pnt_CurrentBestTile = GetLowestFScore(pts_OpenList);
            //Add it to the closed list
            pts_ClosedList.add(pnt_CurrentBestTile);
            //Remove it from the open list
            pts_OpenList.remove(pnt_CurrentBestTile);
            
            //Check if destination sqaure is in closed list
            currentComparison = TileListContainsXY(pts_ClosedList,inp_x2,inp_y2);
            if (currentComparison == true){
                System.out.println("Valid path found.");
                foundapath = true;
                break;
            }
            
            pts_AdjacentTiles = GetAdjacentTiles(pnt_CurrentBestTile,inp_x2,inp_y2);
            for (int i = 0; i < pts_AdjacentTiles.size(); i++){
                pnt_curTile = new MapTile();
                pnt_curTile = pts_AdjacentTiles.get(i);
                compX = pnt_curTile.xposition;
                compY = pnt_curTile.yposition;
                currentComparison = TileListContainsXY(pts_ClosedList,compX,compY);
                if (currentComparison == true){
                    continue;
                }
                currentComparison = TileListContainsXY(pts_OpenList,compX,compY);
                if (currentComparison == false){
                    pts_OpenList.add(pnt_curTile);
                }
                else {
                    int curItemGscore;
                    int curListGscore;                
                    int curIndex;
                    MapTile GscoreComparsion = new MapTile();
                    curIndex = TileListContainsXYAT(pts_OpenList,compX,compY);
                    GscoreComparsion = pts_OpenList.get(curIndex);
                    curItemGscore = pnt_curTile.gValue;
                    curListGscore = GscoreComparsion.gValue;
                    if (curItemGscore < curListGscore){
                        pts_OpenList.remove(curIndex);
                        pts_OpenList.add(pnt_curTile);
                    }
                }
            }
        } while(pts_OpenList.isEmpty() == false);
        //Check if it found a path of stop trying
        if (foundapath == false){
            System.out.println("Shit went wrong.");
        }
        
        return 0;   
    }
    public int GetTurnsTwoPoints(int inp_x1, int inp_y1,int inp_x2,
                                    int inp_y2){
        //Pathfind between two points reduced to an array of moves
        ArrayList<Integer> temp;
        temp = GetMovesArrayTwoPoints(inp_x1,inp_y1,inp_x2,inp_y2);
        int ind_last = temp.size();
        
        return ind_last;
    }
    
    public ArrayList<Integer> GetMovesArrayTwoPoints(int inp_x1, int inp_y1,int inp_x2,
                                    int inp_y2){
        FindTurnsTwoPoints(inp_x1,inp_y1,inp_x2,inp_y2);
        ArrayList<Integer> ReturnedList = new ArrayList<Integer>();
        ArrayList<MapTile> SolvedListSimple = new ArrayList<MapTile>();
        int lastx,lasty,nextx,nexty;
        //First simplify the list by going from last point to first pont        
        int closedsize = pts_ClosedList.size();
        int j = pts_ClosedList.size();
        int ind_nextTile = -1;
        
        //Add the first tile
        ind_nextTile = TileListContainsXYAT(pts_ClosedList,inp_x2,inp_y2);
        SolvedListSimple.add( pts_ClosedList.get(ind_nextTile) );
        
        //Check if the array was of size (start and end where the same)
        if (ind_nextTile == 0){
            return ReturnedList;
        }
        
        int cur_gvalue = pts_ClosedList.get(closedsize-1).gValue;
        lastx = pts_ClosedList.get(closedsize-1).ChildXPosition;      
        lasty = pts_ClosedList.get(closedsize-1).ChildYPosition;        
        nextx = pts_ClosedList.get(closedsize-1).xposition;      
        nexty = pts_ClosedList.get(closedsize-1).yposition;           
        ind_nextTile = TileListContainsXYAT(pts_ClosedList,lastx,lasty);
        while ( (nextx != inp_x1) || (nexty != inp_y1) ){
            if (pts_ClosedList.get(ind_nextTile).gValue == (cur_gvalue - 1) ){
                SolvedListSimple.add( pts_ClosedList.get(ind_nextTile) );
            }
            else {
                //Try again if not correct gvalue?
                System.out.println("Shit went wrong after sorting.");
                continue;                
            }
            nextx = pts_ClosedList.get(ind_nextTile).ChildXPosition;      
            nexty = pts_ClosedList.get(ind_nextTile).ChildYPosition;
            cur_gvalue = pts_ClosedList.get(ind_nextTile).gValue;
            ind_nextTile = TileListContainsXYAT(pts_ClosedList,nextx,nexty);
            if ( (nextx == -1) || (nexty == -1) ){
                break;
            }
        }
        //Add the last tile
        if ( (nextx != -1) || (nexty != -1) ){
            SolvedListSimple.add( pts_ClosedList.get(ind_nextTile) );
        }        
      
        //Now get moves from the simplified list
        
        for (int i = (SolvedListSimple.size()-1); i > 0; i--){
            lastx = SolvedListSimple.get(i).xposition;
            lasty = SolvedListSimple.get(i).yposition;
            nextx = SolvedListSimple.get(i-1).xposition;
            nexty = SolvedListSimple.get(i-1).yposition;
            //Up
            if (nexty < lasty){
                ReturnedList.add(UP);
            }
            //Down
            else if (nexty > lasty){
                ReturnedList.add(DOWN);
            }
            //Left
            else if (nextx < lastx){
                ReturnedList.add(LEFT);
            }            
            //Right
            else if (nextx > lastx){
                ReturnedList.add(RIGHT);
            }
            //Dig
            else if (nextx > lasty){
                ReturnedList.add(DIGLEFT);
            }                
            //Error
            else {
                ReturnedList.add(ERROR);
            }
        }
                
        return ReturnedList;
    }
    
    
    
    //Given A Map tile returns an arraylist of all adjacent walkable tiles
    //Takes destination x an y in order to estimate h value
    private ArrayList<MapTile> GetAdjacentTiles(MapTile inp_CurrentTile, int destX, int destY){
        ArrayList<MapTile> pts_AdjacentTiles = new ArrayList<>();
        int pnt_CurX = inp_CurrentTile.xposition;
        int pnt_CurY = inp_CurrentTile.yposition;
        int pnt_CurG = inp_CurrentTile.gValue;
        MapTile pts_curTile = new MapTile();
        int curHValue = -1;
        curHValue = hValueEstimation(1,1,6,6);
        char curChar;
        
        //Up
        curChar = GetCharFromPosition( (pnt_CurX),(pnt_CurY-1) );
        if ( (curChar == 'H') ){
            pts_curTile = new MapTile();
            pts_curTile.ChildXPosition = pnt_CurX;
            pts_curTile.ChildYPosition = pnt_CurY;        
            pts_curTile.xposition = (pnt_CurX);
            pts_curTile.yposition = (pnt_CurY-1);
            pts_curTile.gValue = (pnt_CurG+1);
            //hValue estimation (a^2+b^2=c^2)
            curHValue = hValueEstimation(pnt_CurX,pnt_CurY+1,destX,destY);
            //Save HValue
            pts_curTile.hValue = curHValue;
            //Add it to the array list
            pts_AdjacentTiles.add(pts_curTile);
        }      
        //Down
        
        //Left (-x)
        //Check if adjacent tile is moveable
        curChar = GetCharFromPosition( (pnt_CurX - 1),(pnt_CurY) );
        if ( (curChar == ' ') || (curChar == 'S') || (curChar == '$') || (curChar == '&') || (curChar == 'H')){
            pts_curTile = new MapTile();
            pts_curTile.ChildXPosition = pnt_CurX;
            pts_curTile.ChildYPosition = pnt_CurY;
            pts_curTile.xposition = (pnt_CurX-1);
            pts_curTile.yposition = (pnt_CurY);
            pts_curTile.gValue = (pnt_CurG+1);
            //hValue estimation (a^2+b^2=c^2)
            curHValue = hValueEstimation(pnt_CurX-1,pnt_CurY,destX,destY);
            //Save HValue
            pts_curTile.hValue = curHValue;
            //Add it to the array list
            pts_AdjacentTiles.add(pts_curTile);
        }
        //Right (+x)
        curChar = GetCharFromPosition( (pnt_CurX + 1),(pnt_CurY) );
        if ( (curChar == ' ') || (curChar == 'S') || (curChar == '$') || (curChar == '&' || (curChar == 'H'))){
            pts_curTile = new MapTile();
            pts_curTile.ChildXPosition = pnt_CurX;
            pts_curTile.ChildYPosition = pnt_CurY;            
            pts_curTile.xposition = (pnt_CurX+1);
            pts_curTile.yposition = (pnt_CurY);
            pts_curTile.gValue = (pnt_CurG+1);
            //hValue estimation (a^2+b^2=c^2)
            curHValue = hValueEstimation(pnt_CurX+1,pnt_CurY,destX,destY);            
            //Save HValue
            pts_curTile.hValue = curHValue;            
            //Add it to the array list
            pts_AdjacentTiles.add(pts_curTile);
        }         
        
        return pts_AdjacentTiles;
    }
    
    boolean TileListContainsXY(ArrayList<MapTile> pts_InputList,
                                                  int inp_X,int inp_Y){
        boolean ReturnValue = false;
        int tilelocation = -1;
        tilelocation = TileListContainsXYAT(pts_InputList,inp_X,inp_Y);
        if (tilelocation == -1){
            ReturnValue = false;
        }
        else{
            ReturnValue = true;
        }
        
        return ReturnValue;
    }

    int TileListContainsXYAT(ArrayList<MapTile> pts_InputList,
                                                  int inp_X,int inp_Y){
        int ReturnValue = -1;
        MapTile lcl_MapTile = new MapTile();
        for (int i = 0; i < pts_InputList.size(); i++){
            lcl_MapTile = new MapTile();
            lcl_MapTile = pts_InputList.get(i);
            if( (lcl_MapTile.xposition == inp_X) && (lcl_MapTile.yposition == inp_Y) ){
                ReturnValue = i;
            }
        }
        return ReturnValue;
    }
    
    MapTile GetLowestFScore(ArrayList<MapTile> inputArrayTiles){
        MapTile returnedTile = new MapTile();
        MapTile currentTile = new MapTile();
        int bestFValue = 1000;
        
        for (int i = 0; i < inputArrayTiles.size(); i++){
            currentTile = inputArrayTiles.get(i);
            if (currentTile.GetfValue() < bestFValue){
                bestFValue = currentTile.GetfValue();
                returnedTile = currentTile;
            }
        }
        
        return returnedTile;
    }
    
    int hValueEstimation(int inp_x1,int inp_y1,int inp_x2,int inp_y2){
        int absoluteX;
        int absoluteY;
        int returnHValue;
        
        absoluteX = Math.abs(inp_x1-inp_x2);
        absoluteY = Math.abs(inp_y1-inp_y2);
        returnHValue = absoluteX+absoluteY;
        
        super.GetDoorY();
        
        return returnHValue;
    }
}
