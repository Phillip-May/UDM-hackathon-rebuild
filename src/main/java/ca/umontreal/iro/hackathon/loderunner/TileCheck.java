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
//GetTileProperties
//Class to get check things about tiles such as what it's next to and
//if you walk on it/break it etc
public class TileCheck {
    
    //Class constructor
    public TileCheck(){
    }    

    //Given A Map tile returns an arraylist of all adjacent walkable tiles
    //Takes destination x an y in order to estimate h value
    public static ArrayList<MapTile> GetAdjacentTiles(String[] inputMap,MapTile inp_CurrentTile, int destX, int destY){
        ArrayList<MapTile> pts_AdjacentTiles = new ArrayList<>();
        boolean fTemp = false;
        int pnt_CurX = inp_CurrentTile.xposition;
        int pnt_CurY = inp_CurrentTile.yposition;
        int pnt_CurG = inp_CurrentTile.gValue;
        MapTile pts_curTile = new MapTile();
        int curHValue = -1;
        curHValue = hValueEstimation(inputMap, 1,1,6,6);
        char curChar;
        char curChar2;
        
        //Up
        fTemp = fTileUpWalkable(inputMap,pnt_CurX,pnt_CurY,destX,destY);
        if ( (fTemp == true) ){
            pts_curTile = new MapTile();
            pts_curTile.ChildXPosition = pnt_CurX;
            pts_curTile.ChildYPosition = pnt_CurY;        
            pts_curTile.xposition = (pnt_CurX);
            pts_curTile.yposition = (pnt_CurY-1);
            pts_curTile.gValue = (pnt_CurG+1);
            //hValue estimation (a^2+b^2=c^2)
            curHValue = hValueEstimation(inputMap, pnt_CurX,pnt_CurY+1,destX,destY);
            //Save HValue
            pts_curTile.hValue = curHValue;
            //Add it to the array list
            pts_AdjacentTiles.add(pts_curTile);
        }      
        //Down
        fTemp = fTileDownWalkable(inputMap, pnt_CurX,pnt_CurY,destX,destY);
        if ( fTemp == true ){
            pts_curTile = new MapTile();
            pts_curTile.ChildXPosition = pnt_CurX;
            pts_curTile.ChildYPosition = pnt_CurY;        
            pts_curTile.xposition = (pnt_CurX);
            pts_curTile.yposition = (pnt_CurY+1);
            pts_curTile.gValue = (pnt_CurG+1);
            //hValue estimation (a^2+b^2=c^2)
            curHValue = hValueEstimation(inputMap, pnt_CurX,pnt_CurY+1,destX,destY);
            //Save HValue
            pts_curTile.hValue = curHValue;
            //Add it to the array list
            pts_AdjacentTiles.add(pts_curTile);
        }        
        //Left (-x)
        //Check if adjacent tile is moveable
        fTemp = fTileLeftWalkable(inputMap, pnt_CurX,pnt_CurY,destX,destY);
        if ( fTemp == true ){
            pts_curTile = new MapTile();
            pts_curTile.ChildXPosition = pnt_CurX;
            pts_curTile.ChildYPosition = pnt_CurY;
            pts_curTile.xposition = (pnt_CurX-1);
            pts_curTile.yposition = (pnt_CurY);
            pts_curTile.gValue = (pnt_CurG+1);
            //hValue estimation (a^2+b^2=c^2)
            curHValue = hValueEstimation(inputMap, pnt_CurX-1,pnt_CurY,destX,destY);
            //Save HValue
            pts_curTile.hValue = curHValue;
            //Add it to the array list
            pts_AdjacentTiles.add(pts_curTile);
        }
        //Right (+x)
        fTemp = fTileRightWalkable(inputMap,pnt_CurX,pnt_CurY,destX,destY);
        if ( fTemp == true ){
            pts_curTile = new MapTile();
            pts_curTile.ChildXPosition = pnt_CurX;
            pts_curTile.ChildYPosition = pnt_CurY;            
            pts_curTile.xposition = (pnt_CurX+1);
            pts_curTile.yposition = (pnt_CurY);
            pts_curTile.gValue = (pnt_CurG+1);
            //hValue estimation (a^2+b^2=c^2)
            curHValue = hValueEstimation(inputMap,pnt_CurX+1,pnt_CurY,destX,destY);            
            //Save HValue
            pts_curTile.hValue = curHValue;            
            //Add it to the array list
            pts_AdjacentTiles.add(pts_curTile);
        }
        //Diagonal left
        fTemp = fTileDownDigonaleLeft(inputMap,pnt_CurX,pnt_CurY,destX,destY);
        if ( fTemp == true ){
            //This may require adding two tiles to the adjcent list
            pts_curTile = new MapTile();
            pts_curTile.fDigSquareLeft = true;
            pts_curTile.ChildXPosition = pnt_CurX;
            pts_curTile.ChildYPosition = pnt_CurY;            
            pts_curTile.xposition = (pnt_CurX-1);
            pts_curTile.yposition = (pnt_CurY+1);
            pts_curTile.gValue = (pnt_CurG+3);
            //hValue estimation (a^2+b^2=c^2)
            curHValue = hValueEstimation(inputMap,pnt_CurX-1,pnt_CurY+1,destX,destY);            
            //Save HValue
            pts_curTile.hValue = curHValue;
            //Add it to the array list
            pts_AdjacentTiles.add(pts_curTile);
        }
        //Diagonal right
        fTemp = fTileDownDigonaleRight(inputMap,pnt_CurX,pnt_CurY,destX,destY);
        if ( fTemp == true ){
            //This may require adding two tiles to the adjcent list
            pts_curTile = new MapTile();
            pts_curTile.fDigSquareRight = true;
            pts_curTile.ChildXPosition = pnt_CurX;
            pts_curTile.ChildYPosition = pnt_CurY;            
            pts_curTile.xposition = (pnt_CurX+1);
            pts_curTile.yposition = (pnt_CurY+1);
            pts_curTile.gValue = (pnt_CurG+3);
            //hValue estimation (a^2+b^2=c^2)
            curHValue = hValueEstimation(inputMap,pnt_CurX+1,pnt_CurY+1,destX,destY);            
            //Save HValue
            pts_curTile.hValue = curHValue;
            //Add it to the array list
            pts_AdjacentTiles.add(pts_curTile);
        }
        //Diagonal left
        fTemp = fTile2Down2DigonaleLeft(inputMap,pnt_CurX,pnt_CurY,destX,destY);
        if ( fTemp == true ){
            //This may require adding two tiles to the adjcent list
            pts_curTile = new MapTile();
            pts_curTile.fDoubleDigSquareLeft = true;
            pts_curTile.ChildXPosition = pnt_CurX;
            pts_curTile.ChildYPosition = pnt_CurY;            
            pts_curTile.xposition = (pnt_CurX-2);
            pts_curTile.yposition = (pnt_CurY+2);
            pts_curTile.gValue = (pnt_CurG+9);
            //hValue estimation (a^2+b^2=c^2)
            curHValue = hValueEstimation(inputMap,pnt_CurX-2,pnt_CurY+2,destX,destY);            
            //Save HValue
            pts_curTile.hValue = curHValue;
            //Add it to the array list
            pts_AdjacentTiles.add(pts_curTile);
        }
        //Dig straight left
        fTemp = fTileDigLeft(inputMap,pnt_CurX,pnt_CurY,destX,destY);
        if ( fTemp == true ){
            //This may require adding two tiles to the adjcent list
            pts_curTile = new MapTile();
            pts_curTile.fDigStraightLeft = true;
            pts_curTile.ChildXPosition = pnt_CurX;
            pts_curTile.ChildYPosition = pnt_CurY;            
            pts_curTile.xposition = (pnt_CurX-1);
            pts_curTile.yposition = (pnt_CurY);
            pts_curTile.gValue = (pnt_CurG+10);
            //hValue estimation (a^2+b^2=c^2)
            curHValue = hValueEstimation(inputMap,pnt_CurX-1,pnt_CurY,destX,destY);            
            //Save HValue
            pts_curTile.hValue = curHValue;
            //Add it to the array list
            pts_AdjacentTiles.add(pts_curTile);
        }
        //Dig straight right
        fTemp = fTileDigRight(inputMap,pnt_CurX,pnt_CurY,destX,destY);
        if ( fTemp == true ){
            //This may require adding two tiles to the adjcent list
            pts_curTile = new MapTile();
            pts_curTile.fDigStraightRight = true;
            pts_curTile.ChildXPosition = pnt_CurX;
            pts_curTile.ChildYPosition = pnt_CurY;            
            pts_curTile.xposition = (pnt_CurX+1);
            pts_curTile.yposition = (pnt_CurY);
            pts_curTile.gValue = (pnt_CurG+10);
            //hValue estimation (a^2+b^2=c^2)
            curHValue = hValueEstimation(inputMap,pnt_CurX+1,pnt_CurY,destX,destY);            
            //Save HValue
            pts_curTile.hValue = curHValue;
            //Add it to the array list
            pts_AdjacentTiles.add(pts_curTile);
        }        
        //Weird edge cases
        fTemp = fCoin2FromWallLeft(inputMap,pnt_CurX,pnt_CurY,destX,destY);
        if ( fTemp == true ){
            //This may require adding two tiles to the adjcent list
            pts_curTile = new MapTile();
            pts_curTile.fWallDigLeftCoinOffset = 2;
            pts_curTile.ChildXPosition = pnt_CurX;
            pts_curTile.ChildYPosition = pnt_CurY;            
            pts_curTile.xposition = (pnt_CurX-3);
            pts_curTile.yposition = (pnt_CurY+1);
            pts_curTile.gValue = (pnt_CurG+4);
            //hValue estimation (a^2+b^2=c^2)
            curHValue = hValueEstimation(inputMap,pnt_CurX,pnt_CurY+1,destX,destY);            
            //Save HValue
            pts_curTile.hValue = curHValue;
            //Add it to the array list
            pts_AdjacentTiles.add(pts_curTile);
        }
        fTemp = fCoin2FromWallRight(inputMap,pnt_CurX,pnt_CurY,destX,destY);
        if ( fTemp == true ){
            //This may require adding two tiles to the adjcent list
            pts_curTile = new MapTile();
            pts_curTile.fWallDigRightCoinOffset = 2;
            pts_curTile.ChildXPosition = pnt_CurX;
            pts_curTile.ChildYPosition = pnt_CurY;            
            pts_curTile.xposition = (pnt_CurX+3);
            pts_curTile.yposition = (pnt_CurY+1);
            pts_curTile.gValue = (pnt_CurG+4);
            //hValue estimation (a^2+b^2=c^2)
            curHValue = hValueEstimation(inputMap,pnt_CurX,pnt_CurY+1,destX,destY);            
            //Save HValue
            pts_curTile.hValue = curHValue;
            //Add it to the array list
            pts_AdjacentTiles.add(pts_curTile);
        }      
        fTemp = fCoin1FromWallLeft(inputMap,pnt_CurX,pnt_CurY,destX,destY);
        if ( fTemp == true ){
            //This may require adding two tiles to the adjcent list
            pts_curTile = new MapTile();
            pts_curTile.fWallDigLeftCoinOffset = 1;
            pts_curTile.ChildXPosition = pnt_CurX;
            pts_curTile.ChildYPosition = pnt_CurY;            
            pts_curTile.xposition = (pnt_CurX-2);
            pts_curTile.yposition = (pnt_CurY+1);
            pts_curTile.gValue = (pnt_CurG+1);
            //hValue estimation (a^2+b^2=c^2)
            curHValue = hValueEstimation(inputMap,pnt_CurX,pnt_CurY+1,destX,destY);            
            //Save HValue
            pts_curTile.hValue = curHValue;
            //Add it to the array list
            pts_AdjacentTiles.add(pts_curTile);
        }
        fTemp = fCoin1FromWallRight(inputMap,pnt_CurX,pnt_CurY,destX,destY);
        if ( fTemp == true ){
            //This may require adding two tiles to the adjcent list
            pts_curTile = new MapTile();
            pts_curTile.fWallDigRightCoinOffset = 1;
            pts_curTile.ChildXPosition = pnt_CurX;
            pts_curTile.ChildYPosition = pnt_CurY;            
            pts_curTile.xposition = (pnt_CurX+2);
            pts_curTile.yposition = (pnt_CurY+1);
            pts_curTile.gValue = (pnt_CurG+1);
            //hValue estimation (a^2+b^2=c^2)
            curHValue = hValueEstimation(inputMap,pnt_CurX,pnt_CurY+1,destX,destY);            
            //Save HValue
            pts_curTile.hValue = curHValue;
            //Add it to the array list
            pts_AdjacentTiles.add(pts_curTile);
        }
        
        return pts_AdjacentTiles;
    }    

    
    //Destination x and y are for accurate hValue estimations
    public static boolean fTileUpWalkable(String[] inputMap, int inp_x1,int inp_y1, 
                                                      int destX, int destY) {
        boolean fReturnValue;
        fReturnValue = false;        
        char chCur;
        char ch1Up;
        
        
        chCur = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1) );
        ch1Up = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1-1) );
        //System.out.println("chCur:" +chCur);
        //System.out.println("ch1Up:" +ch1Up);
        
        //Can walk up if on a ladder
        if ( (ch1Up == 'H') && (chCur == 'H') ){
            fReturnValue = true;
        }
        //Can also stand on top of ladder or rope
        else if ( ((chCur == 'H') ) && (((ch1Up == ' ') || (ch1Up == 'S') || (ch1Up == '$') || (ch1Up == '&')|| (ch1Up == '-'))) ){
            fReturnValue = true;
        }
        
        //System.out.println("Up sort done, is walkable="+fReturnValue+ch1Up);
        return fReturnValue;
    }

    //Destination x and y are for accurate hValue estimations
    public static boolean fTileDownWalkable(String[] inputMap, int inp_x1,int inp_y1, 
                                                        int destX, int destY) {
        boolean fReturnValue;
        fReturnValue = false;     
        char chCur;
        char ch1Down;
        
        ch1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1+1) );
        chCur = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1) );
        //System.out.println("chCur:" +chCur);
        //System.out.println("ch1Down:" +ch1Down);
        //Can fall on air like characters ropes or ladders
        if ( ((ch1Down == ' ') || (ch1Down == 'S') || (ch1Down == '$') || (ch1Down == '&') || (ch1Down == 'H') || (ch1Down == '-'))){
            fReturnValue = true;
        }
        
        //System.out.println("Down sort done, is walkable="+fReturnValue+ch1Down);
        return fReturnValue;
    }    
    
    //Destination x and y are for accurate hValue estimations
    public static boolean fTileLeftWalkable(String[] inputMap, int inp_x1,int inp_y1, 
                                                        int destX, int destY) {
        boolean fReturnValue;
        fReturnValue = false;
        char chCur;
        char ch1Left;
        char ch1Left1Down;
        char ch1Down;
        
        //Character of players location
        chCur = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1) );
        //Next space needs to be non solid
        ch1Left = MapIO.GetCharFromPosition(inputMap, (inp_x1 - 1),(inp_y1) );
        //If it's not a rope or ladder the space below also needs to be valid
        //ch1Left1Down = GetCharFromPosition( (inp_x1 - 1),(inp_y1+1) );
        //Character below you to see if you are falling
        ch1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1+1) );
        
        //System.out.println("Position du runner : (" + x + ", " + y + ")")
        //System.out.println("chCur:" +chCur);
        //System.out.println("ch1Left:" +ch1Left);
        //System.out.println("ch1Left1Down:" +ch1Left1Down);
        //System.out.println("ch1Down:" +ch1Down);
        
        
        
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
        //System.out.println("Left sort done, is walkable="+fReturnValue+ch1Left);
        return fReturnValue;
    }

    public static boolean fTileRightWalkable(String[] inputMap, int inp_x1,int inp_y1, 
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
        chCur = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1) );                
        //Next space needs to be non solid
        ch1Right = MapIO.GetCharFromPosition(inputMap, (inp_x1 + 1),(inp_y1) );
        //If it's not a rope or ladder the space below also needs to be valid
        //ch1Right1Down = GetCharFromPosition( (inp_x1 + 1),(inp_y1+1) );
        //Character below you to see if you are falling
        ch1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1+1) );
        
        //System.out.println("Position du runner : (" + x + ", " + y + ")")
        //System.out.println("chCur:" +chCur);
        //System.out.println("ch1Right:" +ch1Right);
        //System.out.println("ch1Right1Down:" +ch1Right1Down);
        //System.out.println("ch1Right:" +ch1Down);
        
        
        
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
        //System.out.println("Right sort done, is walkable="+fReturnValue+ch1Right);
        
        return fReturnValue;
    }
    
    public static boolean fTileDownDigonaleLeft(String[] inputMap, int inp_x1,int inp_y1, 
                                                            int destX, int destY) {
        boolean fReturnValue;
        fReturnValue = false;
        char chCur;
        char ch1Down;
        char ch1Left;
        char ch1Left1Down;
        
        chCur = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1) );
        ch1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1+1) );
        ch1Left = MapIO.GetCharFromPosition(inputMap, (inp_x1-1),(inp_y1) ); 
        ch1Left1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1-1),(inp_y1+1) );
        
        //System.out.println("chCur:" +chCur);
        //System.out.println("ch1Down:" +ch1Down);
        //System.out.println("ch1Left:" +ch1Left);
        //System.out.println("ch1Left1Down:" +ch1Left1Down);
        
        //g cost needs to be 3 to account for breaking
        //Pretty obvious tile has to be diggable
        if (ch1Left1Down =='#') {
        //Character above has to be walkable
            if ( ((ch1Left == ' ') || (ch1Left == 'S') || (ch1Left == '$') || (ch1Left == '&') || (ch1Left == 'H') || (ch1Left == '-'))){
                //Character below you has to be standable
                if ( ((ch1Down == '@') || (ch1Down == '#') || (ch1Down == 'H') || (ch1Down == '-'))){
                    //Character where you are is walkable by nature of being checked
                    fReturnValue = true;
                }
            }
        }
       
        
        return fReturnValue;
    }    
    
    public static boolean fTileDownDigonaleRight(String[] inputMap, int inp_x1,int inp_y1, 
                                                            int destX, int destY) {
        boolean fReturnValue;
        fReturnValue = false;
        char chCur;
        char ch1Down;
        char ch1Right;
        char ch1Right1Down;
        
        chCur = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1) );
        ch1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1+1) );
        ch1Right = MapIO.GetCharFromPosition(inputMap, (inp_x1+1),(inp_y1) ); 
        ch1Right1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1+1),(inp_y1+1) );
             
        //g cost needs to be 3 to account for breaking
        //Pretty obvious tile has to be diggable
        if (ch1Right1Down =='#') {
        //Character above has to be walkable
            if ( ((ch1Right == ' ') || (ch1Right == 'S') || (ch1Right == '$') || (ch1Right == '&') || (ch1Right == 'H') || (ch1Right == '-'))){
                //Character below you has to be standable
                if ( ((ch1Down == '@') || (ch1Down == '#') || (ch1Down == 'H') || (ch1Down == '-'))){
                    //Character where you are is walkable by nature of being checked
                    fReturnValue = true;
                }
            }
        }
        return fReturnValue;
    }    
    
    public static boolean fTileDigLeft(String[] inputMap, int inp_x1,int inp_y1, 
                                                            int destX, int destY) {
        boolean fReturnValue;
        fReturnValue = false;
        char chCur;
        char ch1Down;
        char ch1Up;
        char ch1Left;
        char ch1Left1Down;
        
        chCur = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1) );
        ch1Left = MapIO.GetCharFromPosition(inputMap, (inp_x1-1),(inp_y1) ); 
        ch1Left1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1-1),(inp_y1+1) );
        ch1Up = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1-1) );
        ch1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1+1) );
       
        //g cost needs to be 3 to account for breaking
        //Pretty obvious tile has to be diggable
        if (ch1Left == '#') {
            //Character where you are has to walkable
            if ( ((chCur == ' ') || (chCur == 'S') || (chCur == '$') || (chCur == '&') || (chCur == 'H') || (chCur == '-'))) {
                //Char above needs to walkable
                if ( ( (chCur == 'H') || (chCur == '-') && ((ch1Up == ' ') || (ch1Up == 'S') || (ch1Up == '$') || (ch1Up == '&') || (ch1Up == 'H') || (ch1Up == '-')) )){
                    //Char below needs to be walkable
                    if ( ((ch1Down == '@') || (ch1Down == '#') || (ch1Down == 'H') || (ch1Down == '-'))){
                        //Char below break needs to walkable
                        if ( ((ch1Left1Down == '@') || (ch1Left1Down == '#') || (ch1Left1Down == 'H') || (ch1Left1Down == '-'))){
                            fReturnValue = true;   
                        }
                    }
                }
            
            }
        }
        
        return fReturnValue;
    }    

    public static boolean fTileDigRight(String[] inputMap, int inp_x1,int inp_y1, 
                                                            int destX, int destY) {
        boolean fReturnValue;
        fReturnValue = false;
        char chCur;
        char ch1Down;
        char ch1Up;
        char ch1Right;
        char chRight1Down;
        
        chCur = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1) );
        ch1Right = MapIO.GetCharFromPosition(inputMap, (inp_x1+1),(inp_y1) ); 
        chRight1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1+1),(inp_y1+1) );
        ch1Up = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1-1) );
        ch1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1+1) );

        //g cost needs to be 3 to account for breaking
        //Pretty obvious tile has to be diggable
        if (ch1Right == '#') {
            //Character where you are has to walkable
            if ( ((chCur == ' ') || (chCur == 'S') || (chCur == '$') || (chCur == '&') || (chCur == 'H') || (chCur == '-'))) {
                //Char above needs to walkable
                if ( ( (chCur == 'H') || (chCur == '-') && ((ch1Up == ' ') || (ch1Up == 'S') || (ch1Up == '$') || (ch1Up == '&') || (ch1Up == 'H') || (ch1Up == '-')) )){
                    //Char below needs to be walkable
                    if ( ((ch1Down == '@') || (ch1Down == '#') || (ch1Down == 'H') || (ch1Down == '-'))){
                        //Char below break needs to walkable
                        if ( ((chRight1Down == '@') || (chRight1Down == '#') || (chRight1Down == 'H') || (chRight1Down == '-'))){
                            fReturnValue = true;   
                        }
                    }
                }
            
            }
        }
        
        return fReturnValue;
    }    
    

    public static boolean fTile2Down2DigonaleLeft(String[] inputMap, int inp_x1,int inp_y1, 
                                                            int destX, int destY) {
        boolean fReturnValue;
        fReturnValue = false;
        char chCur;
        char ch1Down;
        char ch1Left;
        char ch2Left;
        char ch1Left1Down;
        char ch2Left1Down;
        char ch2Left2Down;

        
        chCur = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1) );
        ch1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1+1) );
        ch1Left = MapIO.GetCharFromPosition(inputMap, (inp_x1-1),(inp_y1) ); 
        ch2Left = MapIO.GetCharFromPosition(inputMap, (inp_x1-2),(inp_y1) );
        ch1Left1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1-1),(inp_y1+1) );
        ch2Left1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1-2),(inp_y1+1) );
        ch2Left2Down = MapIO.GetCharFromPosition(inputMap, (inp_x1-2),(inp_y1+2) );
        
        //g cost needs to be 9 to account for breaking
        //The current char must not be a brick
        if (chCur != '#') {
            //Everything in the path must be digable
            if ( (ch1Left1Down == '#') && (ch2Left1Down == '#') && (ch2Left2Down == '#') ) {
                //The two to left must be walkable
                if ( ((ch1Left == ' ') || (ch1Left == 'S') || (ch1Left == '$') || (ch1Left == '&') || (ch1Left == 'H') || (ch1Left == '-'))){
                    if ( ((ch2Left == ' ') || (ch2Left == 'S') || (ch2Left == '$') || (ch2Left == '&') || (ch2Left == 'H') || (ch2Left == '-'))){
                        //Character below you has to be standable
                        if ( ((ch1Down == '@') || (ch1Down == '#') || (ch1Down == 'H') || (ch1Down == '-'))){
                            fReturnValue = true;
                        }
                    }
                }
            }
        }
        
        return fReturnValue;
    }    

    
    //Wall edgecases
    //Where you need to in an then out or you get stuck and cant solve
    //Really this should be solved by keeping track of map modifications within
    //Part of the tree branch solution
    //But that's complicated and this works
    public static boolean fCoin2FromWallLeft(String[] inputMap, int inp_x1,int inp_y1, 
                                                            int destX, int destY) {
        boolean fReturnValue;
        fReturnValue = false;
        char chCur;
        char ch1Down;
        char ch1Left1Down;
        char ch2Left1Down;
        char ch3Left1Down;
        char ch1Left2Down;
        char ch2Left2Down;
        char ch3Left2Down;

        
        chCur = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1) );
        ch1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1+1) );
        ch1Left1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1-1),(inp_y1+1) );
        ch2Left1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1-2),(inp_y1+1) );
        ch3Left1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1-3),(inp_y1+1) );
        ch1Left2Down = MapIO.GetCharFromPosition(inputMap, (inp_x1-1),(inp_y1+2) );
        ch2Left2Down = MapIO.GetCharFromPosition(inputMap, (inp_x1-2),(inp_y1+2) );
        ch3Left2Down = MapIO.GetCharFromPosition(inputMap, (inp_x1-3),(inp_y1+2) );

        
        //g cost needs to be 9 to account for breaking
        //The current char and one below must be a ladder
        if ( (chCur == 'H') || (ch1Down == 'H') ) {
            //The one diagonal must be diggable
            if (ch1Left1Down == '#') {
                //A passageway of 2 tiles
                if( (ch2Left1Down == ' ') && (ch3Left1Down == '$') ) {
                    //You can walk on those 2 tiles
                    if ( ((ch1Left2Down == '@') || (ch1Left2Down == '#') || (ch1Left2Down == 'H') || (ch1Left2Down == '-'))){
                        if ( ((ch2Left2Down == '@') || (ch2Left2Down == '#') || (ch2Left2Down == 'H') || (ch2Left2Down == '-'))){
                            if ( ((ch3Left2Down == '@') || (ch3Left2Down == '#') || (ch3Left2Down == 'H') || (ch3Left2Down == '-'))){
                                fReturnValue = true;
                            }
                        }
                    }
                
                }
            }
        }
        return fReturnValue;
    }    
    
    public static boolean fCoin2FromWallRight(String[] inputMap, int inp_x1,int inp_y1, 
                                                            int destX, int destY) {
        boolean fReturnValue;
        fReturnValue = false;
        char chCur;
        char ch1Down;
        char ch1Right1Down;
        char ch2Right1Down;
        char ch3Right1Down;
        char ch1Right2Down;
        char ch2Right2Down;
        char ch3Right2Down;

        
        chCur = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1) );
        ch1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1+1) );
        ch1Right1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1+1),(inp_y1+1) );
        ch2Right1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1+2),(inp_y1+1) );
        ch3Right1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1+3),(inp_y1+1) );
        ch1Right2Down = MapIO.GetCharFromPosition(inputMap, (inp_x1+1),(inp_y1+2) );
        ch2Right2Down = MapIO.GetCharFromPosition(inputMap, (inp_x1+2),(inp_y1+2) );
        ch3Right2Down = MapIO.GetCharFromPosition(inputMap, (inp_x1+3),(inp_y1+2) );

        
        //g cost needs to be 9 to account for breaking
        //The current char and one below must be a ladder
        if ( (chCur == 'H') || (ch1Down == 'H') ) {
            //The one diagonal must be diggable
            if (ch1Right1Down == '#') {
                //A passageway of 2 tiles
                if( (ch2Right1Down == ' ') && (ch3Right1Down == '$') ) {
                    //You can walk on those 2 tiles
                    if ( ((ch1Right2Down == '@') || (ch1Right2Down == '#') || (ch1Right2Down == 'H') || (ch1Right2Down == '-'))){
                        if ( ((ch2Right2Down == '@') || (ch2Right2Down == '#') || (ch2Right2Down == 'H') || (ch2Right2Down == '-'))){
                            if ( ((ch3Right2Down == '@') || (ch3Right2Down == '#') || (ch3Right2Down == 'H') || (ch3Right2Down == '-'))){
                                fReturnValue = true;
                            }
                        }
                    }
                
                }
            }
        }
        return fReturnValue;
    }    
    
    public static boolean fCoin1FromWallLeft(String[] inputMap, int inp_x1,int inp_y1, 
                                                            int destX, int destY) {
        boolean fReturnValue;
        fReturnValue = false;
        char chCur;
        char ch1Down;
        char ch1Left1Down;
        char ch2Left1Down;
        char ch1Left2Down;
        char ch2Left2Down;
        
        chCur = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1) );
        ch1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1+1) );
        ch1Left1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1-1),(inp_y1+1) );
        ch2Left1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1-2),(inp_y1+1) );
        ch1Left2Down = MapIO.GetCharFromPosition(inputMap, (inp_x1-1),(inp_y1+2) );
        ch2Left2Down = MapIO.GetCharFromPosition(inputMap, (inp_x1-2),(inp_y1+2) );
        
        if ( (chCur == 'H') || (ch1Down == 'H') ) {
            //The one diagonal must be diggable
            if (ch1Left1Down == '#') {
                //A passageway of 2 tiles
                if( (ch2Left1Down == '$') ) {
                    //You can walk on those 2 tiles
                    if ( ((ch1Left2Down == '@') || (ch1Left2Down == '#') || (ch1Left2Down == 'H') || (ch1Left2Down == '-'))){
                        if ( ((ch2Left2Down == '@') || (ch2Left2Down == '#') || (ch2Left2Down == 'H') || (ch2Left2Down == '-'))){
                            fReturnValue = true;
                        }
                    }
                
                }
            }
        }
        return fReturnValue;
    }    
    
    
    public static boolean fCoin1FromWallRight(String[] inputMap, int inp_x1,int inp_y1, 
                                                            int destX, int destY) {
        boolean fReturnValue;
        fReturnValue = false;
        char chCur;
        char ch1Down;
        char ch1Right1Down;
        char ch2Right1Down;
        char ch1Right2Down;
        char ch2Right2Down;

        
        chCur = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1) );
        ch1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1),(inp_y1+1) );
        ch1Right1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1+1),(inp_y1+1) );
        ch2Right1Down = MapIO.GetCharFromPosition(inputMap, (inp_x1+2),(inp_y1+1) );
        ch1Right2Down = MapIO.GetCharFromPosition(inputMap, (inp_x1+1),(inp_y1+2) );
        ch2Right2Down = MapIO.GetCharFromPosition(inputMap, (inp_x1+2),(inp_y1+2) );

        
        //g cost needs to be 9 to account for breaking
        //The current char and one below must be a ladder
        if ( (chCur == 'H') || (ch1Down == 'H') ) {
            //The one diagonal must be diggable
            if (ch1Right1Down == '#') {
                //A passageway of 2 tiles
                if( (ch2Right1Down == '$') ) {
                    //You can walk on those 2 tiles
                    if ( ((ch1Right2Down == '@') || (ch1Right2Down == '#') || (ch1Right2Down == 'H') || (ch1Right2Down == '-'))){
                        if ( ((ch2Right2Down == '@') || (ch2Right2Down == '#') || (ch2Right2Down == 'H') || (ch2Right2Down == '-'))){
                            fReturnValue = true;
                        }
                    }
                
                }
            }
        }
        return fReturnValue;
    }    
    
        
    public static int hValueEstimation(String[] inputMap, int inp_x1,int inp_y1,int inp_x2,int inp_y2){
        int absoluteX;
        int absoluteY;
        int returnHValue;
        
        absoluteX = Math.abs(inp_x1-inp_x2);
        absoluteY = Math.abs(inp_y1-inp_y2);
        returnHValue = absoluteX+absoluteY;
        
        MapIO.yvDoorFromMap(inputMap);
        
        return returnHValue;
    }      
    
}

