package ca.umontreal.iro.hackathon.loderunner;

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
    int num_listofmoves[] = {LEFT,LEFT,LEFT,LEFT,LEFT,LEFT,LEFT,LEFT,LEFT,LEFT,LEFT,LEFT,LEFT,LEFT,LEFT,LEFT,LEFT,LEFT,LEFT,RIGHT,RIGHT,RIGHT,RIGHT,RIGHT,RIGHT,RIGHT,RIGHT,RIGHT,RIGHT,RIGHT,RIGHT,RIGHT,RIGHT,RIGHT,RIGHT,RIGHT,RIGHT,RIGHT,RIGHT,RIGHT,RIGHT,RIGHT,RIGHT,RIGHT,RIGHT,RIGHT,RIGHT,RIGHT};
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
    
    
    //Room name
    public static final String ROOM = "Main8";
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
    }

    @Override
    public Move next(int x, int y) {
        
        
        System.out.println("Position du runner : (" + x + ", " + y + ")");
        
        Direction dir = Direction.fromInt(num_listofmoves[num_movespos]);
        num_movespos++;
        return new Move(Event.MOVE, dir);
    }
    
    public int PathFind() {
        GetCoinCoords();
        GetPlayerCoords();
        GetDoorCoords();
        //Calculate best order to get coins
        //This sorts the array so that the best coin to get first is on top.
        
        //Rigging of numbers for the sake of testing
        num_PlayerX = 12;
        num_PlayerY = 5;
        num_DoorX = 20;
        num_DoorY = 5;        
        xpt_CoinXPos[0] = 12;
        ypt_CoinYPos[0] = 5;        
        xpt_CoinXPos[1] = 6;
        ypt_CoinYPos[1] = 5;        
        xpt_CoinXPos[2] = 3;
        ypt_CoinYPos[2] = 5;        
        
        
        
        SortCoinList();
        
        
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
        System.out.println(num_PlayerX);
        System.out.println("Y Coords");
        System.out.println(num_PlayerY);
        
        return 0;
    }    

    public static int GetFactorial(int input){  
     int i,fact=1;  
     int number=input;//It is the number to calculate factorial    
     for(i=1;i<=number;i++){    
         fact=fact*i;    
     }    
     System.out.println("Factorial of "+number+" is: "+fact);
     return fact;
    }
    
//The algorythm for generating all the possible combinations on an array.
public int factorial(int n){
    int result;
    if(n==0 || n==1){
        return 1;
    }
    
    result = factorial(n-1) * n;
    return result;
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
        //lst_CoinOrder = new int[num_coincount];
        lst_CoinOrder = new int[6];

        
        //Local variable for current position during calculation
        int cur_playerX = num_PlayerX;
        int cur_playerY = num_PlayerY;
        
        //Variables for the outer loop
        //Outer number for move count.
        int bst_movecount = 1000;
        //Solved list
        int lst_CoinOrderSolved[] = new int[50];
        
        
        //The coin nearest the door will always be last so that can be
        //calculated first
        //Set best move to high value so anything calculated will beat it.
        cur_BestMove = 1000;
        cur_Move = 1000;      
        for (int i = 0; i < (num_coincount); i++) {
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
        //Debug value override.
        lst_CoinOrder[0] = 10;
        lst_CoinOrder[1] = 20;
        lst_CoinOrder[2] = 30;
        lst_CoinOrder[3] = 40;
        lst_CoinOrder[4] = 50;
        lst_CoinOrder[5] = 60;
        
        
        //Calculate all combination that need to be tested
        //Object[] elements = new Object[] {1,2,3,4,5};
        //Object[] elements = new Object[] xpt_CoinXPos[];
        //No need to -1 because it starts from 1 not 0 like my other counters.
        int aa = factorial(10);
        System.out.println("Factorail 10:"+aa);
        
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
                cur_Move = GetMovesTwoPoints(cur_playerX,cur_playerY,xpt_CoinXPos[i],ypt_CoinYPos[i]);
                System.out.println("Current move:"+cur_Move);
                //Check if coin i is better
                if (cur_Move < cur_BestMove) {
                    cur_BestMove = cur_Move;
                    cur_BestCoin = i;
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
        //See if this iteration beat the last.
        if (cur_MoveCount < bst_movecount){
            bst_movecount = cur_MoveCount;                
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
