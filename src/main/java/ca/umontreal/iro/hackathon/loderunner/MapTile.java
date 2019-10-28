/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.umontreal.iro.hackathon.loderunner;

/**
 *
 * @author Admin123
 */
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
        //Special flags to specify if a tile needs to be dug
        boolean fDigSquareLeft = false;
        boolean fDigSquareRight = false;
        boolean fDoubleDigSquareLeft = false;
        boolean fDoubleDigSquareRight = false;
        boolean fDigStraightLeft = false;
        boolean fDigStraightRight = false;
        
        int fWallDigLeftCoinOffset = 0;
        int fWallDigRightCoinOffset = 0;
        
        
        
        public int GetfValue(){
            fValue = gValue+hValue;
            return fValue;
        }
        
    }
