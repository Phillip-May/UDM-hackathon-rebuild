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
    //Variable for current position in list of moves
    int num_movespos = 0;
    //Empty Array List
    //List<Integer> mvs_MasterList = new ArrayList<>();        
    int[] mvs_MasterList;        

    
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
        mvs_MasterList = Pathfinder.GetMoveList();
        
        
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
        
        //Important line to fix
        Direction dir = Direction.fromInt(mvs_MasterList[num_movespos]);
        
        //int direction = (int) (Math.random() * 4 + 1);
        //Direction dir = Direction.fromInt(direction);
        
        System.out.println("Last move executed");
        System.out.println(num_movespos);
        num_movespos++;
        return new Move(Event.MOVE, dir);
    }

}
