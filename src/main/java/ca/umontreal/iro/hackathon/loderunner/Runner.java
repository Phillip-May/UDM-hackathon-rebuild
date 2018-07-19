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
 */
public class Runner extends BasicRunner {
    //Symbolic constants
    private static final int UP = 1;
    private static final int LEFT = 2;
    private static final int DOWN = 3;
    private static final int RIGHT = 4;
    //Variables
    //Local version of the map
    String[] map_maplocal;      
    //Array of moves to win
    int[] num_listofmoves = new int[500];
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
    //Room name
    public static final String ROOM = "Main60";
    public static final int START_LEVEL = 1;

    
    public Runner() {
        super(ROOM, START_LEVEL);
    }

    @Override
    public void start(String[] grid) {
        System.out.println("Nouveau niveau ! Grille initiale reçue :");
        //First thing is to copy/save array so that it can used and modified
        map_maplocal = grid.clone();
        num_movespos = 0;        
        
        PathFind();
        
        for (int i=0; i<map_maplocal.length; i++) {
            String ligne = map_maplocal[i];

            System.out.println(ligne);
        }
        num_movespos = 0;
    }

    @Override
    public Move next(int x, int y) {
        
        //Server was ignoring inputs when sending them too fast.
        //Given I precalcualte all my moves it seems like the server cant keep
        //up.
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException ex) {
            Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Position du runner : (" + x + ", " + y + ")");
        Direction dir = Direction.fromInt(num_listofmoves[num_movespos]);
        //int direction = (int) (Math.random() * 4 + 1);
        //Direction dir = Direction.fromInt(direction);
        System.out.println("Last move executed");
        System.out.println(num_movespos);
        num_movespos++;
        return new Move(Event.MOVE, dir);
    }
    
    public int PathFind() {
        GetCoinCoords();
        GetPlayerCoords();
        GetDoorCoords();
        //Calculate best order to get coins
        //This sorts the array so that the best coin to get first is on top.        
        SortCoinList();
        
        //Variables for pathfinding
        int lcl_PlayerX = num_PlayerX;
        int lcl_PlayerY = num_PlayerY;
        //Variable for current coin being targetted
        int cur_TargetCoin = 0;
        
        for (int i = 0; i <= (num_coincount); i++){
            cur_TargetCoin = lst_CoinOrderSolved[i];
            while ( (lcl_PlayerX != xpt_CoinXPos[cur_TargetCoin]) || (lcl_PlayerY != ypt_CoinYPos[cur_TargetCoin]) ){
                if (lcl_PlayerX > xpt_CoinXPos[cur_TargetCoin]){
                    lcl_PlayerX--;
                    num_listofmoves[num_movespos] = LEFT;
                }
                else if (lcl_PlayerX < xpt_CoinXPos[cur_TargetCoin]){
                    lcl_PlayerX++;
                    num_listofmoves[num_movespos] = RIGHT;
                }
                num_movespos++;
            }
            System.out.println("Coin obtained, current x coord");
            System.out.println(lcl_PlayerX);            
        }
        //Finally add path to door.
        while ( (lcl_PlayerX != num_DoorX) || (lcl_PlayerY != num_DoorY) ){
            if (lcl_PlayerX > num_DoorX){
                lcl_PlayerX--;
                num_listofmoves[num_movespos] = LEFT;
            }
            else if (lcl_PlayerX < num_DoorX){
                lcl_PlayerX++;
                num_listofmoves[num_movespos] = RIGHT;
            }
            num_movespos++;
        }
        System.out.println("Door reached");
        System.out.println(lcl_PlayerX);
        
        //Reset move position for movement code.
        num_movespos = 0;
        //int a = lst_CoinOrderSolved[1];
        System.out.println("X coords");
        System.out.println(java.util.Arrays.toString(lst_CoinOrderSolved));
        
        return 0;
    }
    
    
    public int GetCoinCoords() {
        num_coincount = 0;
        //Populates teh array called pts_CoinCoords
        //Debug prints
        
        for (int i=0; i<map_maplocal.length; i++) {
            String str_line = map_maplocal[i];
            for (int j = 0; j < str_line.length(); j++){
                int cur_char = str_line.charAt(j);
                if (cur_char == '$') {
                    xpt_CoinXPos[num_coincount] = j;
                    ypt_CoinYPos[num_coincount] = i;
                    num_coincount++;
                }
            }
            System.out.println(str_line);
        }
        //Decrement coint count so it reflects the number of coins found.
        num_coincount--;
        System.out.println("X and y coordinates of coins");
        System.out.println("X coords");
        System.out.println(java.util.Arrays.toString(xpt_CoinXPos));
        System.out.println("Y Coords");
        System.out.println(java.util.Arrays.toString(ypt_CoinYPos));
        
        return 0;
    }    

    public int GetPlayerCoords() {

        for (int i=0; i<map_maplocal.length; i++) {
            String str_line = map_maplocal[i];
            for (int j = 0; j < str_line.length(); j++){
                int cur_char = str_line.charAt(j);
                if (cur_char == '&') {
                    num_PlayerX = j;
                    num_PlayerY = i;
                }
            }
            System.out.println(str_line);
        }         
        System.out.println("X and y coordinates of player");
        System.out.println("X coords");
        System.out.println(num_PlayerX);
        System.out.println("Y Coords");
        System.out.println(num_PlayerY);
        
        return 0;
    }
    
    public int GetDoorCoords() {

        for (int i=0; i<map_maplocal.length; i++) {
            String str_line = map_maplocal[i];
            for (int j = 0; j < str_line.length(); j++){
                int cur_char = str_line.charAt(j);
                if (cur_char == 'S') {
                    num_DoorX = j;
                    num_DoorY = i;
                }
            }
            System.out.println(str_line);
        }         
        System.out.println("X and y coordinates of Door");
        System.out.println("X coords");
        System.out.println(num_DoorX);
        System.out.println("Y Coords");
        System.out.println(num_DoorY);
        
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
            cur_Move = GetMovesTwoPoints(xpt_CoinXPos[i], ypt_CoinYPos[i],
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
                    cur_Move = GetMovesTwoPoints(cur_playerX,cur_playerY,xpt_CoinXPos[CoinBeingAnalysed],ypt_CoinYPos[CoinBeingAnalysed]);
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
    
    //Only takes x into account for now
    //Will need to include ladders and walls
    public int GetMovesTwoPoints(int inp_x1, int inp_y1,int inp_x2,
                                    int inp_y2){
        int result;
        result = java.lang.Math.abs(inp_x1 - inp_x2);
        return result;
    }
    
}
