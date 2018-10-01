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
    //Symbolic constants for moving
    public static final int UP = 1;
    public static final int LEFT = 2;
    public static final int DOWN = 3;
    public static final int RIGHT = 4;
    public static final int DIGLEFT = 5;
    public static final int DIGRIGHT = 5;
    public static final int ERROR = -1;
        
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
    public static final String ROOM = "Main131";
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
        //Reset theoretical positions
        RunnerMapIO MapGetterMaster = new RunnerMapIO();
        xvTheoretical = MapGetterMaster.xvPlayerFromMap();
        yvTheoretical = MapGetterMaster.yvPlayerFromMap();
        
        //Next code to figure out how to solve this map
        RunnerPathFind Pathfinder = new RunnerPathFind();
        
        //Main algorythm to get find a path
        Pathfinder.PathFind();        
        
        //Get list of moves and store in an array
        mvsFinalMoves = new ArrayList<Integer>();
        mvsFinalMoves = Pathfinder.GetMoveList();
        //Also reset index in array
        iFinalMoves = 0;
        
        System.out.println("Place Holder to check array conts with debugger");
        for (int i=0; i<grid.length; i++) {
            String ligne = grid[i];
            System.out.println(ligne);
        }
    }
    
    //The method that's run every move
    @Override
    public Move next(int xvReal, int yvReal) {
        
        
        //Server was ignoring inputs when sending them too fast.
        //Given I precalcualte all my moves it seems like the server cant keep
        //up.
        try {
            TimeUnit.MILLISECONDS.sleep(250);
        } catch (InterruptedException ex) {
            Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Position du runner : (" + xvReal + ", " + yvReal + ")");
        Direction dir = Direction.fromInt(0);
        int i=0;
        //Important lines to fix
        if (iFinalMoves<=(mvsFinalMoves.size()-1)){
            dir = Direction.fromInt(mvsFinalMoves.get(iFinalMoves));
        }
        else {
            dir = Direction.fromInt((int) (0));
        }
        //int direction = (int) (Math.random() * 4 + 1);
        //Direction dir = Direction.fromInt(direction);
        
        System.out.println("Last move executed");
        System.out.println(iFinalMoves);
        iFinalMoves++;
        return new Move(Event.MOVE, dir);
    }

}
