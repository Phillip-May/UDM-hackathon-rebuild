package ca.umontreal.iro.hackathon.loderunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.lang.model.element.Element;
import java.util.Collections;
/**
 *
 * @author Admin123
 */
public class RunnerPathFind extends Runner {
    //Variables  
    //New list of tiles to go to to win    
    //Class constructor
    public RunnerPathFind(){
        
    }

    public static ArrayList<Integer> GetMoveList(String[] inputMap){
        ArrayList<Integer> solvedListOfMoves;
        solvedListOfMoves = PathFind(inputMap);
        return solvedListOfMoves;
    }    
    
    public static ArrayList<Integer> PathFind(String[] inputMap) {
        ArrayList<Integer> MasterMoveList = new ArrayList<Integer>();    
        //Test path finding
        /*
        int temp1 = xvPlayerFromMap();
        int temp2 = yvPlayerFromMap();
        int temp3 = xvDoorFromMap();
        int temp4 = yvDoorFromMap();
        int temp5 = 0;
        
        temp5 = MapPToP.GetTurnsTwoPoints(temp1,temp2,temp3,temp4);
        */
        ArrayList<Integer> temp2;
        
        ArrayList<Integer> debugArr = new ArrayList<Integer>();
        //MapPtoP.FindTurnsTwoPoints(inputMap,7,13,4,14);
        temp2 = MapPtoP.GetMovesTwoPoints(inputMap,2,1,4,2,debugArr);
        temp2 = MapPtoP.GetMovesTwoPoints(inputMap,2,1,4,2,debugArr);

        //MapPtoP.FindTurnsTwoPoints(inputMap,24,14,27,2);
        //MapPtoP.FindTurnsTwoPoints(inputMap,27,2,25,3);
        
        //Calculate best order to get coins
        
        //Variables for pathfinding
        //Variable for current coin being targetted
        ArrayList<Integer> xviCoinMaster;
        ArrayList<Integer> yviCoinMaster;
        ArrayList<ArrayList<Integer>> solution = SortCoinListV2(inputMap);
        xviCoinMaster = solution.get(0);
        yviCoinMaster = solution.get(1);

        
        //First go from start to coin 0
        int cur_TargetCoin = 0;
        int StartX = MapIO.xvPlayerFromMap(inputMap);
        int StartY = MapIO.yvPlayerFromMap(inputMap);
        int DestX = xviCoinMaster.get(0);
        int DestY = yviCoinMaster.get(0);

        
        ArrayList<Integer> temp;
        temp = new ArrayList<Integer>();
        ArrayList<Integer> returnXY2 = new ArrayList<Integer>();
        temp = MapPtoP.GetMovesTwoPoints(inputMap,StartX, StartY, DestX, DestY,returnXY2);
        
        if (returnXY2.size() != 0) {
            DestX = returnXY2.get(0);
            DestY = returnXY2.get(1);            
        }
        
        if (temp == null) {
            return MasterMoveList;
        }
        
        MasterMoveList.addAll(temp);
        
        ArrayList<Integer> returnXY = new ArrayList<Integer>();
        //Find route to coins
        for (int i = 1; i < (xviCoinMaster.size()); i++){
            StartX = DestX;
            StartY = DestY;
            if (returnXY.size() != 0) {
                StartX = returnXY.get(0);
                StartY = returnXY.get(1);
                returnXY = new ArrayList<Integer>();
            }
            DestX = xviCoinMaster.get(i);
            DestY = yviCoinMaster.get(i);
            
            temp = new ArrayList<Integer>();
            temp = MapPtoP.GetMovesTwoPoints(inputMap,StartX, StartY, DestX, DestY,returnXY);
            if (temp == null) {
               return MasterMoveList;
            }
            MasterMoveList.addAll(temp);            
            
            
            System.out.println("Coin obtained, current x coord");
            System.out.println(StartX);
        }
        //Finally add path to door.
        //Start x and y carry over from last coin
        StartX = DestX;
        StartY = DestY;
        DestX = MapIO.xvDoorFromMap(inputMap);
        DestY = MapIO.yvDoorFromMap(inputMap);
        
        temp = new ArrayList<Integer>();
        temp = MapPtoP.GetMovesTwoPoints(inputMap,StartX, StartY, DestX, DestY,null);        
        MasterMoveList.addAll(temp);
        
        System.out.println("Door reached");
        System.out.println(DestX);
        
        return MasterMoveList;
    }    
    
    //This sorts the coin by the most efficient way to grab them all
    //I kind of forgot to take the door into account whoops.
    //Eventually implement this in pure function programming
    //Returns an arraylist that contains the index of the coins in the correct
    //Order
    public static ArrayList<ArrayList<Integer>> SortCoinListV2(String[] inputMap){
        int ceCoins,xvDoor,yvDoor;
        int cBestTurns;
        int cCurrentTurns;
        int cCurrentTotalTurns;
        int cBestTotalTurns;
        int iBestCoinCombination;
        int iBestCoin;
        int iComparisonCoin;
        int inp_x1,inp_y1,inp_x2,inp_y2;
        int iCurStart,iCurDest;
        ArrayList<Integer> xvpOriginalCoins;
        ArrayList<Integer> yvpOriginalCoins;
        ArrayList<Integer> xvpSolvedCoins;
        ArrayList<Integer> yvpSolvedCoins;
        ArrayList<Integer> rgiCurrentCombination = new ArrayList<Integer>();
        xvpOriginalCoins = MapIO.xypCoinsFromMap(inputMap).get(0);
        yvpOriginalCoins = MapIO.xypCoinsFromMap(inputMap).get(1);
        ceCoins = xvpOriginalCoins.size();
        xvDoor = MapIO.xvDoorFromMap(inputMap);
        yvDoor = MapIO.yvDoorFromMap(inputMap);
        cBestTurns = 1000;
        cCurrentTurns = 0; 
        iBestCoin = -1;
        iBestCoinCombination = -1;
        cCurrentTotalTurns = -1;
        //Coin nearest the door will always be last so that can be found first.
        for (int i = 0; i < (ceCoins); i++) {
            cCurrentTurns = MapPtoP.GetTurnsTwoPoints(inputMap,
                                                      xvpOriginalCoins.get(i),
                                                      yvpOriginalCoins.get(i),
                                                      xvDoor, yvDoor,
                                                      null);
            //Check if coin i is better
            if (cCurrentTurns < cBestTurns) {
                cBestTurns = cCurrentTurns;
                iBestCoin = i;
            }
        }
        System.out.println("V2: Best coin to go last:"+iBestCoin);        
        //Enumerate test
        ArrayList<ArrayList<Integer>> ipEnumeratedIndexes;
        ArrayList<Integer> rgIndices = new ArrayList<> ();
        for (int i =0; i < xvpOriginalCoins.size(); i ++){
            rgIndices.add(i);
        }
        
        ArrayList<Integer> returnXY = new ArrayList<Integer>();
        //This is to enumerate all the ways to gather all the coins in order to
        //Get the best solution
        //When there are too many coins there are too many possibilities so
        if (rgIndices.size() <= 5) {     
            ipEnumeratedIndexes = CombinationFinder.choose(rgIndices, rgIndices.size());
            cBestTotalTurns = 1000;
            for (int k= 0; k < ipEnumeratedIndexes.size(); k++) {
                cCurrentTotalTurns = 0;
                rgiCurrentCombination = ipEnumeratedIndexes.get(k);
                for (int j = 0; j < (rgiCurrentCombination.size() ); j++){
                    iCurStart = rgiCurrentCombination.get(j);
                    //If returnXY is empty its the same
                    if (returnXY.size() == 0) {
                        inp_x1 = xvpOriginalCoins.get(iCurStart);
                        inp_y1 = yvpOriginalCoins.get(iCurStart);
                    } //If it's not that's the players real position after that coin
                    else {
                        inp_x1 = returnXY.get(0);
                        inp_y1 = returnXY.get(1);
                        returnXY = new ArrayList<Integer>();
                    }
                    //Destination is next coin except for last case where it's door
                    if (j < (rgiCurrentCombination.size() - 1)){
                        iCurDest = rgiCurrentCombination.get(j+1);                    
                        inp_x2 = xvpOriginalCoins.get(iCurDest);
                        inp_y2 = yvpOriginalCoins.get(iCurDest);    
                    }
                    else {
                        inp_x2 = MapIO.xvDoorFromMap(inputMap);
                        inp_y2 = MapIO.yvDoorFromMap(inputMap);                    
                    }
                    cCurrentTurns = MapPtoP.GetTurnsTwoPoints(inputMap,
                                                              inp_x1,inp_y1,
                                                              inp_x2,inp_y2,
                                                              returnXY);
                    cCurrentTotalTurns = cCurrentTotalTurns + cCurrentTurns;                
                }
                //Check if combination k is better
                if (cCurrentTotalTurns < cBestTotalTurns) {
                    cBestTotalTurns = cCurrentTotalTurns;
                    iBestCoinCombination = k;
                }            
                System.out.println("V2 consider");
            }


            System.out.println("The best combination of coins was k=:"+iBestCoinCombination);
            System.out.println("That combination being:");
            rgiCurrentCombination = ipEnumeratedIndexes.get(iBestCoinCombination);
            System.out.println(rgiCurrentCombination);
            System.out.println("And it should take this many turns+");
            System.out.println(cCurrentTotalTurns);
        }
        else {
            System.out.println("Too many coins, just trying possibilities until a solution is valid");
            //Instead we just keep on trying random orders until one is possible
            Collections.shuffle(rgIndices);
            cBestTotalTurns = 1000;
            boolean fValidPath = false;
            while (fValidPath == false) {
                //If it's not better try again
                Collections.shuffle(rgIndices);
                cCurrentTotalTurns = 0;
                rgiCurrentCombination = rgIndices;
                for (int j = 0; j < (rgiCurrentCombination.size() ); j++){
                    iCurStart = rgiCurrentCombination.get(j);
                    //If returnXY is empty its the same
                    if (returnXY.size() == 0) {
                        inp_x1 = xvpOriginalCoins.get(iCurStart);
                        inp_y1 = yvpOriginalCoins.get(iCurStart);
                    } //If it's not that's the players real position after that coin
                    else {
                        inp_x1 = returnXY.get(0);
                        inp_y1 = returnXY.get(1);           
                        returnXY = new ArrayList<Integer>();                        
                    }
                    //Destination is next coin except for last case where it's door
                    if (j < (rgiCurrentCombination.size() - 1)){
                        iCurDest = rgiCurrentCombination.get(j+1);                    
                        inp_x2 = xvpOriginalCoins.get(iCurDest);
                        inp_y2 = yvpOriginalCoins.get(iCurDest);    
                    }
                    else {
                        inp_x2 = MapIO.xvDoorFromMap(inputMap);
                        inp_y2 = MapIO.yvDoorFromMap(inputMap);                    
                    }
                    cCurrentTurns = MapPtoP.GetTurnsTwoPoints(inputMap,
                                                              inp_x1,inp_y1,
                                                              inp_x2,inp_y2,
                                                              returnXY);
                    
                    cCurrentTotalTurns = cCurrentTotalTurns + cCurrentTurns;                
                }
                //Check if combination k is better
                if (cCurrentTotalTurns < cBestTotalTurns) {
                    cBestTotalTurns = cCurrentTotalTurns;
                    System.out.println("Random solution found");
                    System.out.println("That combination being:");
                    rgiCurrentCombination = rgIndices;
                    System.out.println(rgiCurrentCombination);
                    System.out.println("And it should take this many turns+");
                    System.out.println(cCurrentTotalTurns);  
                    fValidPath = true;
                }
                System.out.println("V2 consider random");
            }
        }
        //Now pack the new best x and y together and return the packed list
        ArrayList< ArrayList<Integer> > xypCoinsSolved;
        xypCoinsSolved = new ArrayList<>();
        //Pack the x values
        xvpSolvedCoins = new ArrayList<Integer>();
        for (int i = 0; i < ceCoins; i++){
            iComparisonCoin = rgiCurrentCombination.get(i);
            xvpSolvedCoins.add( xvpOriginalCoins.get(iComparisonCoin) );
            
        }
        //Pack the y values
        yvpSolvedCoins = new ArrayList<Integer>();
        for (int i = 0; i < ceCoins; i++){
            iComparisonCoin = rgiCurrentCombination.get(i);
            yvpSolvedCoins.add( yvpOriginalCoins.get(iComparisonCoin) );
            
        }        
        //Pack the two array lists together
        xypCoinsSolved.add(xvpSolvedCoins);
        xypCoinsSolved.add(yvpSolvedCoins);        
        
        
        return xypCoinsSolved;
    }
    
}
