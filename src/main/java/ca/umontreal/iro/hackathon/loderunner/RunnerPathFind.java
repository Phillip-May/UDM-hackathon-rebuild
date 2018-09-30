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

/**
 *
 * @author Admin123
 */
public class RunnerPathFind extends RunnerMapIO {
    //Variables
    //Local version of the map
    String[] map_maplocal;      
    //Array of moves to win
    int[] num_listofmoves = new int[500];
    //New list of tiles to go to to win
    ArrayList<Integer> MasterMoveList = new ArrayList<Integer>();
    //Position in array of moves, can be reset if moves are recalculated
    int num_movespos = 0;
    //Arrays of coordinates
    //Contains a list of x,y coordinates of where the coins are located.
    //The number of coins depends on the level.
    int num_coincount = 0;
    int[] xpt_CoinXPos = new int[20];
    int[] ypt_CoinYPos = new int[20];
    //Player X and Y coordinates
    int num_PlayerX = 0;
    int num_PlayerY = 0;
    //Door X and Y coordinates
    int num_DoorX = 0;
    int num_DoorY = 0;
    //List for testing combinations
    //Needs to have all coins but one
    //Used in the pathfinding algorythm.
    int lst_CoinOrder[];
    int num_FactPos = 0;
    //Solved list
    int lst_CoinOrderSolved[] = new int[50]; 
    //Class for pathfinding
    RunnerPather MapPToP;
    //Class constructor
    public RunnerPathFind(){
        
        
    }

    //Set the map to use (an array of strings)
    public int SetMapArray(String[] inp_StartGrid){
        map_maplocal = inp_StartGrid.clone();
        return 0;
    }

    public ArrayList<Integer> GetMoveList(){
        return MasterMoveList;
    }    
    
    public int PathFind() {
        //Create subclasses
        RunnerMapIO MapGetter = new RunnerMapIO();
        MapPToP = new RunnerPather();

        //Generate data based on map data
        MapGetter.FindStuff();
        //Grab variables from sub class
        num_DoorX = MapGetter.GetDoorX();
        num_DoorY = MapGetter.GetDoorY();
        num_PlayerX = MapGetter.GetPlayerX();
        num_PlayerY = MapGetter.GetPlayerY();
        xpt_CoinXPos = MapGetter.GetCoinX();
        ypt_CoinYPos = MapGetter.GetCoinY();
        num_coincount = MapGetter.GetCoinAmount();
        
        //Test path finding
        //MapPToP.FindTurnsTwoPoints(num_PlayerX,num_PlayerY,num_DoorX,num_DoorY);
        //MapPToP.FindTurnsTwoPoints(15,5,12,5);
        
        
        //Calculate best order to get coins
        //This sorts the array so that the best coin to get first is on top.        
        SortCoinList();
        //This was a thing I moved
        num_movespos = 0;
        
        //Variables for pathfinding
        //Variable for current coin being targetted
        int cur_TargetCoin = 0;
        int StartX = num_PlayerX;
        int StartY = num_PlayerY;
        int DestX = xpt_CoinXPos[lst_CoinOrderSolved[0]];
        int DestY = ypt_CoinYPos[lst_CoinOrderSolved[0]];
        int currentMove = -1;
        
        ArrayList<Integer> temp;
        //First go from start to coin 0
        temp = new ArrayList<Integer>();
        temp = MapPToP.GetMovesArrayTwoPoints(StartX, StartY, DestX, DestY);
        
        MasterMoveList.addAll(temp);
        
        
        //Find route to coins
        for (int i = 1; i < (num_coincount+1); i++){
            cur_TargetCoin = lst_CoinOrderSolved[i];
            
            StartX = DestX;
            StartY = DestY;
            DestX = xpt_CoinXPos[lst_CoinOrderSolved[i]];
            DestY = ypt_CoinYPos[lst_CoinOrderSolved[i]];
            
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
        DestX = num_DoorX;
        DestY = num_DoorY;
        
        temp = new ArrayList<Integer>();
        temp = MapPToP.GetMovesArrayTwoPoints(StartX, StartY, DestX, DestY);        
        MasterMoveList.addAll(temp);
        
        System.out.println("Door reached");
        System.out.println(DestX);
        
        //Reset move position for movement code.
        System.out.println("X coords");
        System.out.println(java.util.Arrays.toString(lst_CoinOrderSolved));
        
        return 0;
    }    
    
    //Calculates a factoraial without recursion
    public static int GetFactorial(int input){  
     int i,fact=1;  
     int number=input;//It is the number to calculate factorial    
     for(i=1;i<=number;i++){    
         fact=fact*i;    
     }    
    return fact;
    }


    
    //This sorts the coin by the most efficient way to grab them all
    //I kind of forgot to take the door into account whoops.
    //Eventually implement this in pure function programming
    public int SortCoinList(){
        //The number of moves to beat.
        //For the inner loops
        int cur_MoveCount = 0;
        int cur_Move = 1000;
        int cur_BestMove = 1000;
        int cur_BestCoin = 0;
        int cur_arrposition = 0;
        int cur_FirstCoin = 0;
        int cur_BestPlayerX = 0;
        int cur_BestPlayerY = 0;
        //List for testing combinations
        //Needs to have all coins but one
        //Array of size will skip one position without adding -1
        
        //Temporary for debugging
        lst_CoinOrder = new int[num_coincount+1];
        //Fill with default order starting at 0 working its way up.
        for (int i = 0; i <= num_coincount; i++) {
            lst_CoinOrder[i] = i;
        }

        
        //Local variable for current position during calculation
        int cur_playerX = num_PlayerX;
        int cur_playerY = num_PlayerY;
        int CoinBeingAnalysed = 0;
        
        //Variables for the outer loop
        //Outer number for move count.
        int bst_movecount = 1000;
        int gbl_GlobalMoveCount = 1000;

        
        
        //The coin nearest the door will always be last so that can be
        //calculated first
        //Set best move to high value so anything calculated will beat it.
        cur_BestMove = 1000;
        cur_Move = 1000;      
        for (int i = 0; i < (num_coincount+1); i++) {
            cur_Move = MapPToP.GetTurnsTwoPoints(xpt_CoinXPos[i], ypt_CoinYPos[i],
                                         num_DoorX, num_DoorY);
            //Check if coin i is better
            if (cur_Move < cur_BestMove) {
                cur_BestMove = cur_Move;
                cur_BestCoin = i;
            }
        }
        System.out.println("Best coin to go last:"+cur_BestCoin);
        //Assign current best coin to last position in the solved array.
        lst_CoinOrderSolved[num_coincount] = cur_BestCoin;
        System.out.println("Last coin assigned to position:"+num_coincount);

        
        //Cast array to arraylist
        //In order to find all permutations
        List<Integer> tempbetween = Arrays.stream(lst_CoinOrder).boxed().collect(Collectors.toList());
        ArrayList<Integer> Input = new ArrayList<Integer>(tempbetween);
        ArrayList<ArrayList<Integer>> allPermutations = new ArrayList<ArrayList<Integer>>();
        //Find all permetutations of the array
        CombinationFinder.enumerate(Input,(num_coincount+1),(num_coincount+1),allPermutations);
        //Cast back to an array of integers
        int[] arrcastback = new int[50];
        List<Integer> ind_Permutations;
        
        for (int k= 0; k < allPermutations.size(); k++) {

            arrcastback = allPermutations.get(k).stream().mapToInt(i -> i).toArray();
            //Make sure last element matches the optimal one
            //IE only test optentially optiaml permutations.
            if (arrcastback[num_coincount] != cur_BestCoin) {
                continue;
            }
        


            //Outer loop to iterate the calculation so current coin checks the next
            //coin in the list.
            for (int j = 0; j < (num_coincount); j++){
                //Reset values for inner for loop
                cur_BestMove = 1000;
                cur_Move = 1000;
                //This finds the number of moves to get to any coin from
                //the current player position. Looks at all coins after the current
                //one. Essential a factorial operation.
                //J is essentially the cur_arrposition
                for (int i = j; i < (num_coincount); i++){
                    CoinBeingAnalysed = arrcastback[i];
                    cur_Move = MapPToP.GetTurnsTwoPoints(cur_playerX,cur_playerY,xpt_CoinXPos[CoinBeingAnalysed],ypt_CoinYPos[CoinBeingAnalysed]);
                    //Check if coin i is better
                    if (cur_Move < cur_BestMove) {
                        cur_BestMove = cur_Move;
                        cur_BestCoin = CoinBeingAnalysed;
                    }
                }
                //Update Player X and Y to take into account last move.
                cur_playerX = xpt_CoinXPos[cur_BestCoin];
                cur_playerY = ypt_CoinYPos[cur_BestCoin];
                //Update best move number.
                System.out.println("Current optimal move:"+cur_BestMove);
                cur_MoveCount = cur_MoveCount + cur_BestMove;
                //Move one number over in the factorial calculation.
                //This is handled by the outer counter j.
            }
            System.out.println("End of series optimal moves:"+cur_MoveCount);
            //Reset player position to start
            cur_playerX = num_PlayerX;
            cur_playerY = num_PlayerY;                
            //See if this iteration beat the last.
            if (cur_MoveCount < bst_movecount){
                bst_movecount = cur_MoveCount;
                lst_CoinOrderSolved = arrcastback;
                System.out.println("New optimal array found, Moves:"+bst_movecount);
                System.out.println(java.util.Arrays.toString(lst_CoinOrderSolved));                 
            }                   
        
        }
        return 0;
    }
    
}
