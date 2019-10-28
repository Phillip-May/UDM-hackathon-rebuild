package ca.umontreal.iro.hackathon.loderunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.lang.model.element.Element;

/**
 *
 */
public class Runner extends BasicRunner {
    //Symbolic constants for moving
    public static final int FALL = 0;    
    public static final int UP = 1;
    public static final int LEFT = 2;
    public static final int DOWN = 3;
    public static final int RIGHT = 4;
    public static final int DIGLEFT = 5;
    public static final int DIGRIGHT = 6;
    public static final int STALL = 100;
    
    public static final int ERROR = -1;
    //Symbolic constants used in moving pre simplification
    public static final int LADDERWALLDIGLEFT = 10;
    public static final int LADDERWALLDIGRIGHT = 11;
    public static final int DOWNWALLDIGLEFT = 20;
    public static final int DOWNWALLDIGRIGHT = 21;
    
    int skipCount;
    boolean fReRoute;
    //Variable for current position in list of moves
    public static int iFinalMoves;
    //Empty Array List     
    public static ArrayList<Integer> mvsFinalMoves;
    //Variables that store the original and modified ascii maps
    public static String[] mpMapOriginal;
    public static String[] mpMapCurrent; 
    //Variables for theoretical x and y position
    int xvTheoretical;
    int yvTheoretical;
    
    //Variables that I did not make
    //Room name
    public static final String ROOM = "Main155";
    public static final int START_LEVEL = 1;
    
    //Constructor from template
    public Runner() {
        super(ROOM, START_LEVEL);
    }

    //Basically my constructor
    @Override
    public void start(String[] grid) {
        System.out.println("Nouveau niveau ! Grille initiale reçue :");
        //First thing is to copy/save array so that it can used and modified
        mpMapOriginal = grid.clone();
        mpMapCurrent = grid.clone();
        //Reset predicted positions
        xvTheoretical = MapIO.xvPlayerFromMap(mpMapOriginal);
        yvTheoretical = MapIO.yvPlayerFromMap(mpMapOriginal);
        
        //Next code to figure out how to solve this map     
        //Main algorythm to get find a path        
        //Get list of moves and store in an array
        mvsFinalMoves = new ArrayList<Integer>();
        mvsFinalMoves = RunnerPathFind.GetMoveList(mpMapOriginal);
        //Also reset index in array
        iFinalMoves = 0;
        skipCount = 1;
        fReRoute = false;
        
        DebugPrinter.PrintMap(grid);
        //Sleep for a second to prevent accidentally moving too quickly
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
        }        
        
    }
    
    //The method that's run every move
    @Override
    public Move next(int xvReal, int yvReal) {
        //Determine the event that occurs
        Direction dir = Direction.fromInt(0);
        Move returnedMove = new Move(Event.MOVE, dir);
                
        //Server was ignoring inputs when sending them too fast.
        //Given I precalcualte all my moves it seems like the server cant keep
        //up.
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException ex) {
            Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        System.out.println("Real Pos du runner : (" + xvReal + ", " + yvReal + ")");
        System.out.println("Position du runner : (" + xvTheoretical + ", " + yvTheoretical + ")");
        
        //Update map and recalculate if there is a desync
        if ( (xvTheoretical != xvReal) || (yvTheoretical != yvReal) ) {
            MapIO.UpdatePlayerXY(mpMapCurrent,xvReal,yvReal);
            System.out.println("New map");
            DebugPrinter.PrintMap(mpMapCurrent);
            xvTheoretical = xvReal;
            yvTheoretical = yvReal;
            dir = Direction.fromInt((int) 0);
            returnedMove = new Move(Event.MOVE, dir);
            fReRoute = true;
            return returnedMove;
        }
        
        //Flag means moves need to be recalculated
        if (fReRoute == true) {
            mvsFinalMoves = new ArrayList<Integer>();
            mvsFinalMoves = RunnerPathFind.GetMoveList(mpMapCurrent);
            //Also reset index in array
            iFinalMoves = 0;         
            fReRoute = false;
            dir = Direction.fromInt((int) 0);
            returnedMove = new Move(Event.MOVE, dir);
            return returnedMove;
        }
        
        if ( (mvsFinalMoves.size() <= iFinalMoves) || (mvsFinalMoves == null) ) {
            //When all else fails do random stuff and hope
            //This actually fixes the edge case where the level does not 
            //Complete if the door is touched within 1 move of the last coin
            dir = Direction.fromInt((int) getRandomNumberInRange(0,4));
            returnedMove = new Move(Event.MOVE, dir);
            return returnedMove;
        }
        
        int moveDirection = mvsFinalMoves.get(iFinalMoves);
        int i=0;
        //Important lines to fix
        if (iFinalMoves<=(mvsFinalMoves.size()-1)){
            //Check if dig command is embeded
            int temp = mvsFinalMoves.get(iFinalMoves);
            if (temp == DIGLEFT){
                dir = dir.LEFT;
                returnedMove = new Move(Event.DIG, dir);
            }
            else if (temp == DIGRIGHT) {
                dir = dir.RIGHT;
                returnedMove = new Move(Event.DIG, dir);                
            }
            else{
                dir = Direction.fromInt(mvsFinalMoves.get(iFinalMoves));
                if (moveDirection == UP) {
                    yvTheoretical--;
                }
                else if (moveDirection == DOWN) {
                    yvTheoretical++;
                }
                else if (moveDirection == FALL) {
                    yvTheoretical++;
                }
                else if (moveDirection == STALL) {
                    
                }
                else if (moveDirection == LEFT) {
                    xvTheoretical--;
                }
                else if (moveDirection == RIGHT) {
                    xvTheoretical++;
                }
                
                
                returnedMove = new Move(Event.MOVE, dir);
            }
        }
        else {
            
        }
        //int direction = (int) (Math.random() * 4 + 1);
        //Direction dir = Direction.fromInt(direction);
        
        System.out.println("Last move executed");
        System.out.println(iFinalMoves);
        iFinalMoves++;
        return returnedMove;
    }
    
    private static int getRandomNumberInRange(int min, int max) {

	if (min >= max) {
		throw new IllegalArgumentException("max must be greater than min");
	}
            Random r = new Random();
            return r.nextInt((max - min) + 1) + min;
	}
}
