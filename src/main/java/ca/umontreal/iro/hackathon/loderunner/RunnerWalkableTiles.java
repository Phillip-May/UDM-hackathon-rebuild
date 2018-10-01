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
public class RunnerWalkableTiles extends RunnerPathFind{
    
    //Class constructor
    public RunnerWalkableTiles(){
    }    

    //Given A Map tile returns an arraylist of all adjacent walkable tiles
    //Takes destination x an y in order to estimate h value
    public ArrayList<MapTile> GetAdjacentTiles(MapTile inp_CurrentTile, int destX, int destY){
        ArrayList<MapTile> pts_AdjacentTiles = new ArrayList<>();
        boolean fTemp = false;
        int pnt_CurX = inp_CurrentTile.xposition;
        int pnt_CurY = inp_CurrentTile.yposition;
        int pnt_CurG = inp_CurrentTile.gValue;
        MapTile pts_curTile = new MapTile();
        int curHValue = -1;
        curHValue = hValueEstimation(1,1,6,6);
        char curChar;
        char curChar2;

        
        //Up
        fTemp = fTileUpWalkable(pnt_CurX,pnt_CurY,destX,destY);
        if ( (fTemp == true) ){
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
        fTemp = fTileDownWalkable(pnt_CurX,pnt_CurY,destX,destY);
        if ( fTemp == true ){
            pts_curTile = new MapTile();
            pts_curTile.ChildXPosition = pnt_CurX;
            pts_curTile.ChildYPosition = pnt_CurY;        
            pts_curTile.xposition = (pnt_CurX);
            pts_curTile.yposition = (pnt_CurY+1);
            pts_curTile.gValue = (pnt_CurG+1);
            //hValue estimation (a^2+b^2=c^2)
            curHValue = hValueEstimation(pnt_CurX,pnt_CurY+1,destX,destY);
            //Save HValue
            pts_curTile.hValue = curHValue;
            //Add it to the array list
            pts_AdjacentTiles.add(pts_curTile);
        }        
        //Left (-x)
        //Check if adjacent tile is moveable
        fTemp = fTileLeftWalkable(pnt_CurX,pnt_CurY,destX,destY);
        if ( fTemp == true ){
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
        fTemp = fTileRightWalkable(pnt_CurX,pnt_CurY,destX,destY);
        if ( fTemp == true ){
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

    
    //Destination x and y are for accurate hValue estimations
    public boolean fTileUpWalkable(int inp_x1,int inp_y1, 
                                   int destX, int destY) {
        boolean fReturnValue;
        fReturnValue = false;        
        char chCur;
        char ch1Up;
        
        chCur = GetCharFromPosition( (inp_x1),(inp_y1) );
        ch1Up = GetCharFromPosition( (inp_x1),(inp_y1-1) );
        System.out.println("chCur:" +chCur);
        System.out.println("ch1Up:" +ch1Up);
        
        //Can walk up if on a ladder
        if ( (ch1Up == 'H') ){
            fReturnValue = true;
        }
        //Can also stand on top of ladder or rope
        else if ( ((chCur == 'H') || (chCur == '-')) && (((ch1Up == ' ') || (ch1Up == 'S') || (ch1Up == '$') || (ch1Up == '&')|| (ch1Up == '-'))) ){
            fReturnValue = true;
        }
        
        System.out.println("Up sort done, is walkable="+fReturnValue+ch1Up);
        return fReturnValue;
    }

    //Destination x and y are for accurate hValue estimations
    public boolean fTileDownWalkable(int inp_x1,int inp_y1, 
                                   int destX, int destY) {
        boolean fReturnValue;
        fReturnValue = false;     
        char chCur;
        char ch1Down;
        
        ch1Down = GetCharFromPosition( (inp_x1),(inp_y1+1) );
        chCur = GetCharFromPosition( (inp_x1),(inp_y1) );
        System.out.println("chCur:" +chCur);
        System.out.println("ch1Down:" +ch1Down);
        //Can fall on air like characters ropes or ladders
        if ( ((ch1Down == ' ') || (ch1Down == 'S') || (ch1Down == '$') || (ch1Down == '&') || (ch1Down == 'H') || (ch1Down == '-'))){
            fReturnValue = true;
        }
        
        System.out.println("Down sort done, is walkable="+fReturnValue+ch1Down);
        return fReturnValue;
    }    
    
    //Destination x and y are for accurate hValue estimations
    public boolean fTileLeftWalkable(int inp_x1,int inp_y1, 
                                          int destX, int destY) {
        boolean fReturnValue;
        fReturnValue = false;
        char chCur;
        char ch1Left;
        char ch1Left1Down;
        char ch1Down;
        
        //Character of players location
        chCur = GetCharFromPosition( (inp_x1),(inp_y1) );
        //Next space needs to be non solid
        ch1Left = GetCharFromPosition( (inp_x1 - 1),(inp_y1) );
        //If it's not a rope or ladder the space below also needs to be valid
        //ch1Left1Down = GetCharFromPosition( (inp_x1 - 1),(inp_y1+1) );
        //Character below you to see if you are falling
        ch1Down = GetCharFromPosition( (inp_x1),(inp_y1+1) );
        
        //System.out.println("Position du runner : (" + x + ", " + y + ")")
        System.out.println("chCur:" +chCur);
        System.out.println("ch1Left:" +ch1Left);
        //System.out.println("ch1Left1Down:" +ch1Left1Down);
        System.out.println("ch1Down:" +ch1Down);
        
        
        
        //If character is 'z' then it attempted to walk off the map
        if ( (ch1Left == 'z') ){
            fReturnValue = false;
        }
        //If your falling you can only fall
        //Current character basically air
        //Character below you, basically air
        else if ( ((chCur == ' ') || (chCur == 'S') || (chCur == '$') || (chCur == '&')) &&  ((ch1Down == ' ') || (ch1Down == 'S') || (ch1Down == '$') || (ch1Down == '&')|| (ch1Down == '-')) ) {
            fReturnValue = false;
        }
        //You can not walk into a wall.
        else if ( (ch1Left =='#' || ch1Left =='@') ){
            fReturnValue = false;
        }
        //Logic to deal with digging TBD
        else if ( (chCur =='#' || chCur =='@') ){
            fReturnValue = false;
        }
        //You can walk onto ladders and rope without a solid block below you
        else if ( (ch1Left =='H') || (ch1Left =='-') ){
            fReturnValue = true;
        }        
        //You can always walk off a ladder
        else if ( (chCur == 'H') || (chCur == '-') ){
            fReturnValue = true;
        }        
        //If the block left is air and I'm not falling
        else if ( (ch1Left == ' ') || (ch1Left == 'S') || (ch1Left == '$') || (ch1Left == '&') ){
            fReturnValue = true;
        }
        else {
            //Shit went wrong
            fReturnValue = false;
        }    
        System.out.println("Left sort done, is walkable="+fReturnValue+ch1Left);
        return fReturnValue;
    }

    public boolean fTileRightWalkable(int inp_x1,int inp_y1, 
                                      int destX, int destY) {
        boolean fReturnValue;
        fReturnValue = false;
        char curChar;
        char curChar2;
        char chCur;
        char ch1Right;
        //char ch1Right1Down;
        char ch1Down;
        //Character of players location
        chCur = GetCharFromPosition( (inp_x1),(inp_y1) );                
        //Next space needs to be non solid
        ch1Right = GetCharFromPosition( (inp_x1 + 1),(inp_y1) );
        //If it's not a rope or ladder the space below also needs to be valid
        //ch1Right1Down = GetCharFromPosition( (inp_x1 + 1),(inp_y1+1) );
        //Character below you to see if you are falling
        ch1Down = GetCharFromPosition( (inp_x1),(inp_y1+1) );
        
        //System.out.println("Position du runner : (" + x + ", " + y + ")")
        System.out.println("chCur:" +chCur);
        System.out.println("ch1Right:" +ch1Right);
        //System.out.println("ch1Right1Down:" +ch1Right1Down);
        System.out.println("ch1Right:" +ch1Down);
        
        
        
        //If character is 'z' then it attempted to walk off the map
        if ( (ch1Right == 'z') ){
            fReturnValue = false;
        }
        //If your falling you can only fall
        //Current character basically air
        //Character below you, basically air
        else if ( ((chCur == ' ') || (chCur == 'S') || (chCur == '$') || (chCur == '&')) &&  ((ch1Down == ' ') || (ch1Down == 'S') || (ch1Down == '$') || (ch1Down == '&')|| (ch1Down == '-')) ) {
            fReturnValue = false;
        }
        //Logic to deal with digging TBD
        //You can not walk into a wall.
        else if ( (ch1Right =='#' || ch1Right =='@') ){
            fReturnValue = false;
        }        
        else if ( (chCur =='#' || chCur =='@') ){
            fReturnValue = false;
        }
        //You can walk onto ladders and rope without a solid block below you
        else if ( (ch1Right =='H') || (ch1Right =='-') ){
            fReturnValue = true;
        }        
        //You can always walk off a ladder
        else if ( (chCur == 'H') || (chCur == '-') ){
            fReturnValue = true;
        }        
        //If the block left is air and I'm not falling
        else if ( (ch1Right == ' ') || (ch1Right == 'S') || (ch1Right == '$') || (ch1Right == '&') ){
            fReturnValue = true;
        }
        else {
            //Shit went wrong
            fReturnValue = false;
        }    
        System.out.println("Right sort done, is walkable="+fReturnValue+ch1Right);
        
        return fReturnValue;
    }
    
    
    public int hValueEstimation(int inp_x1,int inp_y1,int inp_x2,int inp_y2){
        int absoluteX;
        int absoluteY;
        int returnHValue;
        
        absoluteX = Math.abs(inp_x1-inp_x2);
        absoluteY = Math.abs(inp_y1-inp_y2);
        returnHValue = absoluteX+absoluteY;
        
        yvDoorFromMap();
        
        return returnHValue;
    }      
    
}

