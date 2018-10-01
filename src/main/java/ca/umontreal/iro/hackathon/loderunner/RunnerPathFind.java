package ca.umontreal.iro.hackathon.loderunner;

import static ca.umontreal.iro.hackathon.loderunner.CombinationFinder.choose;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.lang.model.element.Element;

/**
 *
 * @author Admin123
 */
public class RunnerPathFind extends RunnerMapIO {
    //Variables  
    //New list of tiles to go to to win
    ArrayList<Integer> MasterMoveList = new ArrayList<Integer>();
    //Class for pathfinding
    RunnerPather MapPToP;
    
    //Class constructor
    public RunnerPathFind(){
        
    }

    public ArrayList<Integer> GetMoveList(){
        return MasterMoveList;
    }    
    
    public int PathFind() {
        MapPToP = new RunnerPather();

        
        //Test path finding
        /*
        int temp1 = xvPlayerFromMap();
        int temp2 = yvPlayerFromMap();
        int temp3 = xvDoorFromMap();
        int temp4 = yvDoorFromMap();
        int temp5 = 0;
        
        temp5 = MapPToP.GetTurnsTwoPoints(temp1,temp2,temp3,temp4);
        */
        //MapPToP.FindTurnsTwoPoints(15,5,12,5);
        
        
        //Calculate best order to get coins
        //Debug call
        SortCoinListV2();
        
        //Variables for pathfinding
        //Variable for current coin being targetted
        ArrayList<Integer> xvpCoinMaster;
        ArrayList<Integer> yvpCoinMaster;
        xvpCoinMaster = SortCoinListV2().get(0);
        yvpCoinMaster = SortCoinListV2().get(1);

        
        //First go from start to coin 0
        int cur_TargetCoin = 0;
        int StartX = xvPlayerFromMap();
        int StartY = yvPlayerFromMap();
        int DestX = xvpCoinMaster.get(0);
        int DestY = yvpCoinMaster.get(0);

        
        ArrayList<Integer> temp;
        temp = new ArrayList<Integer>();
        temp = MapPToP.GetMovesArrayTwoPoints(StartX, StartY, DestX, DestY);
        
        MasterMoveList.addAll(temp);
        
        
        //Find route to coins
        for (int i = 1; i < (xvpCoinMaster.size()); i++){
            StartX = DestX;
            StartY = DestY;
            DestX = xvpCoinMaster.get(i);
            DestY = yvpCoinMaster.get(i);
            
            temp = new ArrayList<Integer>();
            temp = MapPToP.GetMovesArrayTwoPoints(StartX, StartY, DestX, DestY);
            MasterMoveList.addAll(temp);            
            
            
            System.out.println("Coin obtained, current x coord");
            System.out.println(StartX);
        }
        //Finally add path to door.
        //Start x and y carry over from last coin
        StartX = DestX;
        StartY = DestY;
        DestX = xvDoorFromMap();
        DestY = yvDoorFromMap();
        
        temp = new ArrayList<Integer>();
        temp = MapPToP.GetMovesArrayTwoPoints(StartX, StartY, DestX, DestY);        
        MasterMoveList.addAll(temp);
        
        System.out.println("Door reached");
        System.out.println(DestX);
        
        return 0;
    }    
    
    //This sorts the coin by the most efficient way to grab them all
    //I kind of forgot to take the door into account whoops.
    //Eventually implement this in pure function programming
    public ArrayList<ArrayList<Integer>> SortCoinListV2(){
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
        ArrayList<Integer> rgiCurrentCombination;
        xvpOriginalCoins = xypCoinsFromMap().get(0);
        yvpOriginalCoins = xypCoinsFromMap().get(1);
        ceCoins = xvpOriginalCoins.size();
        xvDoor = xvDoorFromMap();
        yvDoor = yvDoorFromMap();
        cBestTurns = 1000;
        cCurrentTurns = 0; 
        iBestCoin = -1;
        iBestCoinCombination = -1;
        cCurrentTotalTurns = -1;
        //Coin nearest the door will always be last so that can be found first.
        for (int i = 0; i < (ceCoins); i++) {
            cCurrentTurns = MapPToP.GetTurnsTwoPoints(xvpOriginalCoins.get(i),
                                                      yvpOriginalCoins.get(i),
                                                      xvDoor, yvDoor);
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
        ipEnumeratedIndexes = choose(rgIndices, rgIndices.size());
        System.out.println(ipEnumeratedIndexes);
        cBestTotalTurns = 1000;
        for (int k= 0; k < ipEnumeratedIndexes.size(); k++) {
            cCurrentTotalTurns = 0;
            rgiCurrentCombination = ipEnumeratedIndexes.get(k);
            for (int j = 0; j < (rgiCurrentCombination.size() ); j++){
                iCurStart = rgiCurrentCombination.get(j);
                inp_x1 = xvpOriginalCoins.get(iCurStart);
                inp_y1 = yvpOriginalCoins.get(iCurStart);
                //Destination is next coin except for last case where it's door
                if (j < (rgiCurrentCombination.size() - 1)){
                    iCurDest = rgiCurrentCombination.get(j+1);                    
                    inp_x2 = xvpOriginalCoins.get(iCurDest);
                    inp_y2 = yvpOriginalCoins.get(iCurDest);    
                }
                else {
                    inp_x2 = xvDoorFromMap();
                    inp_y2 = yvDoorFromMap();                    
                }
                cCurrentTurns = MapPToP.GetTurnsTwoPoints(inp_x1,inp_y1,
                                                          inp_x2,inp_y2);
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
