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
public class RunnerPather extends RunnerWalkableTiles {
    //Map data is stored in Runner class
    private ArrayList<MapTile> pts_OpenList;
    private ArrayList<MapTile> pts_ClosedList;
    
    //Class constructor
    public RunnerPather(){
        
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
        //Debug char
        char chDebug;
        
        //Start
        pnt_CurrentTile.xposition = inp_x1;
        pnt_CurrentTile.yposition = inp_y1;
        pnt_CurrentTile.gValue = 0;
        boolean currentComparison = false;
        int compX = -1;
        int compY = -1;
        
        //Instantitate class for finding adjacent walkable tiles
        //Debug call

        //Add original to open list
        pts_OpenList.add(pnt_CurrentTile);
        
        do {
            pnt_CurrentBestTile = new MapTile();
            pnt_CurrentBestTile = GetLowestFScore(pts_OpenList);
            //Add it to the closed list
            pts_ClosedList.add(pnt_CurrentBestTile);
            //Remove it from the open list
            pts_OpenList.remove(pnt_CurrentBestTile);
            
            //Debug breakpoint checks
            //if these trigger shit went wrong
            if (pnt_CurrentBestTile.xposition == 0){
                chDebug = 'a';
            }
            if (pnt_CurrentBestTile.yposition == 0){
                chDebug = 'b';
            }            
            
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
            System.out.println("Failed to find path with parameters:.");
            System.out.println("Path find start x and y");
            System.out.println(inp_x1);
            System.out.println(inp_y1);
            System.out.println("Path find end x and y");
            System.out.println(inp_x2);
            System.out.println(inp_y2);
            return -1;
        }
        return 0;   
    }
    public int GetTurnsTwoPoints(int inp_x1, int inp_y1,int inp_x2,
                                    int inp_y2){
        //Pathfind between two points reduced to the number of moves
        //First make sure that path is possible
        int ind_last;
        int temperror;
        temperror = FindTurnsTwoPoints(inp_x1,inp_y1,inp_x2,inp_y2);
        if (temperror == 0){
            ArrayList<Integer> temp;
            temp = GetMovesArrayTwoPoints(inp_x1,inp_y1,inp_x2,inp_y2);
            ind_last = temp.size();            
        }
        else {
            ind_last = 1000;
        }
        
        return ind_last;
    }
    
    public ArrayList<Integer> GetMovesArrayTwoPoints(int inp_x1, int inp_y1,int inp_x2,
                                    int inp_y2){
        FindTurnsTwoPoints(inp_x1,inp_y1,inp_x2,inp_y2);
        ArrayList<Integer> ReturnedList = new ArrayList<Integer>();
        ArrayList<MapTile> SolvedListSimple = new ArrayList<MapTile>();
        int lastx,lasty,nextx,nexty;
        boolean fTileDig;
        //First simplify the list by going from last point to first pont        
        int closedsize = pts_ClosedList.size();
        int j = pts_ClosedList.size();
        int ind_nextTile = -1;
        
        //Add the first tile
        ind_nextTile = TileListContainsXYAT(pts_ClosedList,inp_x2,inp_y2);
        SolvedListSimple.add( pts_ClosedList.get(ind_nextTile) );
        
        //Check if the array was of size 0 (start and end where the same)
        if (ind_nextTile == 0){
            return ReturnedList;
        }
        
        int cur_gvalue = pts_ClosedList.get(closedsize-1).gValue;
        lastx = pts_ClosedList.get(closedsize-1).ChildXPosition;      
        lasty = pts_ClosedList.get(closedsize-1).ChildYPosition;        
        nextx = pts_ClosedList.get(closedsize-1).xposition;      
        nexty = pts_ClosedList.get(closedsize-1).yposition;           
        ind_nextTile = TileListContainsXYAT(pts_ClosedList,lastx,lasty);
        int tempg1;
        int tempg2;
        while ( (nextx != inp_x1) || (nexty != inp_y1) ){
            //G check is no longer applicable, closed list should only have
            //one instance of each x y anyway.
            SolvedListSimple.add( pts_ClosedList.get(ind_nextTile) );
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
            fTileDig = SolvedListSimple.get(i-1).fDigSquareLeft;
            //Check if this a dig step
            if (fTileDig == true){
                //Breakpoint
                fTileDig = fTileDig;
                //Add a dig left, a left and a down
                ReturnedList.add(DIGLEFT);
                ReturnedList.add(LEFT);
                ReturnedList.add(DOWN);
            }
            //Up
            else if (nexty < lasty){
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
    
}
