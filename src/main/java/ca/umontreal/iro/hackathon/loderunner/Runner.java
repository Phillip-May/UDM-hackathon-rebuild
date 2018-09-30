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
    public static final int DIG = 5;
    public static final int ERROR = -1;
        
    //Variable for current position in list of moves
    int num_movespos;
    //Empty Array List     
    ArrayList<Integer> FinalMoves;

    
    //Room name
    public static final String ROOM = "Main4";
    public static final int START_LEVEL = 1;

    
    public Runner() {
        super(ROOM, START_LEVEL);
    }

    @Override
    public void start(String[] grid) {
        System.out.println("Nouveau niveau ! Grille initiale reçue :");
        //First thing is to copy/save array so that it can used and modified
        RunnerPathFind Pathfinder = new RunnerPathFind();
        Pathfinder.SetMapArray(grid);
        
        //Main algorythm to get find a path
        Pathfinder.PathFind();        
        
        //Get list of moves and store in an array
        FinalMoves = new ArrayList<Integer>();
        FinalMoves = Pathfinder.GetMoveList();
        //Also reset index in array
        num_movespos = 0;
        
        System.out.println("Place Holder to check array conts with debugger");
        for (int i=0; i<grid.length; i++) {
            String ligne = grid[i];
            System.out.println(ligne);
        }
    }

    @Override
    public Move next(int x, int y) {
        
        
        //Server was ignoring inputs when sending them too fast.
        //Given I precalcualte all my moves it seems like the server cant keep
        //up.
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Position du runner : (" + x + ", " + y + ")");
        Direction dir = Direction.fromInt(0);
        int i=0;
        //Important lines to fix
        if (num_movespos<=(FinalMoves.size()-1)){
            dir = Direction.fromInt(FinalMoves.get(num_movespos));
        }
        else {
            dir = Direction.fromInt((int) (0));
        }
        //int direction = (int) (Math.random() * 4 + 1);
        //Direction dir = Direction.fromInt(direction);
        
        System.out.println("Last move executed");
        System.out.println(num_movespos);
        num_movespos++;
        return new Move(Event.MOVE, dir);
    }

}
