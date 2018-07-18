package ca.umontreal.iro.hackathon.loderunner;

import java.lang.Math;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class Runner extends BasicRunner {

    // TODO : Remplacer ceci par votre clé secrète
    public static final String ROOM = "PhillipAI15";

    /* Utilisez cette variable pour choisir le niveau de départ
     *
     * Notez: le niveau de départ sera 1 pour tout
     * le monde pendant la compétition :v)
     */
    //#define is now 4 words and has a type
    public static final int START_LEVEL = 2;
    public static final int UP          = 1;
    public static final int LEFT        = 2;
    public static final int DOWN        = 3;
    public static final int RIGHT       = 4;
    
    //My variables
    public int num_direction;
    public int num_countermoves;
    public int num_level = (START_LEVEL-1);
    public int map_height;
    public int map_length;
    public String[] lcl_grid = new String[500];
    public int[] coinxpos = new int[500];
    public int[] coinypos = new int[500];
    public int[] brickxpos = new int[500];
    public int[] brickypos = new int[500];
    public int[] ladderxpos = new int[500];
    public int[] ladderypos = new int[500];
    public int[] ropexpos = new int[500];
    public int[] ropeypos = new int[500];
    public int exitxpos;
    public int exitypos;
    public int playerxpos;
    public int playerypos;
    public int num_totalcoin;
    public Boolean obtainedallcoin = false;
    //level 2 and 3 variables
    Boolean seekawaycoin = false;
    Boolean seektowardcoin = false;
    Boolean climbladder   = false;
    Boolean reachladder   = false;
    
    
    public int height;
    public int length;    
    public ArrayList<Integer> coinX = new ArrayList();
    public ArrayList<Integer> coinY = new ArrayList();
    public ArrayList<Integer> distances = new ArrayList();
    public int posLadderX;//h
    public int posLadderY;//h
//    public int posSpaceX;//escpace
//    public int posSpaceY;//escpace
//    public int posBrickX;//#
//    public int posBrickY;//#
//    public int posBlock;//@
    public int posRope;//-
    public int posExitX;//S
    public int posExitY;//S
    public int posRunner;
    
    public char[][] myGrid = new char[height][length];

    public Runner() {
        super(ROOM, START_LEVEL);
    }

    @Override
    public void start(String[] grid) {
        //System.out.println("Nouveau niveau ! Grille initiale reçue :");
        //Variable reseting betwen levels
        map_height = grid.length;
        map_length = grid[0].length();
        obtainedallcoin = false;
        num_countermoves = (START_LEVEL-1);
        seekawaycoin = false;
        seektowardcoin = false;
        climbladder   = false;
        reachladder   = false;

        
        
        for (int i=0; i<grid.length; i++) {
            String ligne = grid[i];
            lcl_grid[i]  = grid[i];
            map_height   = i + 1;
            System.out.println(lcl_grid[i]);
        }
        System.out.println(map_height);
        
        scanMap();
        

        
        
        
        //Setup code
        num_level = num_level + 1;
                        
        
    }  
    
    
    @Override
    public Move next(int x, int y) 
    {
        System.out.println("Position du runner :" + x + y);
        //System.out.println("Current switchcase value; " + num_level);
        
        //Refresh x and y position
        playerxpos = x;
        //Main code to run everytime a new move is possible
        //Main code
        switch (num_level)
        {
            case 1:
            LEVEL1();
            break;
            
            case 2:
            LEVEL2();
            break;
            
            default:
                System.out.println("Job done");
            
            break;
        }
                
        
        switch (num_direction)
        {
            case UP:
                playerypos = playerypos + 1;
                break;
            case DOWN:
                playerypos = playerypos - 1;
                break;    
        }
        
        Direction dir = Direction.fromInt(num_direction);                 
        return new Move(Event.MOVE, dir);
    }

    public void LEVEL1()
    {
        
        if (( ((0 +playerxpos) < coinFarFromDoorX() )&& (obtainedallcoin == false)))
        {
            num_direction = RIGHT;
        }
        else if (0 + (playerxpos) > coinFarFromDoorX() && (obtainedallcoin == false) )
        {
            num_direction = LEFT;
        }
        else if ( (playerxpos) == coinFarFromDoorX() && (obtainedallcoin == false) )
        {
            obtainedallcoin = true;
        }
        if (obtainedallcoin == true)
        {
            if (playerxpos >= exitxpos)
            {
                num_direction = LEFT;
            }
            else if (playerxpos < exitxpos)
            {
                num_direction = RIGHT;
            }
        }

        //
        //System.out.println("Coin far from door x " + coinFarFromDoorX());
        //System.out.println("Exit x " + exitxpos);       
    }

    
    
    public int scanMap()
    {
        //Local variables
        String cur_line;
        char cur_char;
        int k_coin = 0;
        int k_brick = 0;
        int k_ladder = 0;
        int k_rope = 0;
        int scanlinex = 0;
        int scanliney = 0;
                  
        
        for (int i=0; i<map_height; i++) {
            cur_line = lcl_grid[i];
            
            for (int j=0; j<map_length; j++)
            {
                
                cur_char = cur_line.charAt(j);
                //Scanning individual characters as of here
                if (cur_char == '$')
                {
                    //System.out.println("Coin x value: " + scanlinex);
                    //System.out.println("Coin y value: " + scanliney);
                    coinxpos[k_coin] = scanlinex;
                    coinypos[k_coin] = scanliney;
                    num_totalcoin = k_coin;
                    k_coin++;
                }
                else if (cur_char == '@')
                {
                    brickxpos[k_brick] = scanlinex;
                    brickypos[k_brick] = scanliney;
                    k_brick++;                
                }
                else if (cur_char == 'H')
                {
                    ladderxpos[k_ladder] = scanlinex;
                    ladderypos[k_ladder] = scanliney;
                    k_ladder++;
                }
                else if (cur_char == '-')
                {
                    ropexpos[k_rope] = scanlinex;
                    ropeypos[k_rope] = scanliney;
                    k_rope++;
                }
                else if (cur_char == 'S')
                {
                    exitxpos = scanlinex;
                    exitypos = scanliney;
                }
                else if (cur_char == '&')
                {
                    playerxpos = scanlinex;
                    playerypos = scanliney;
                }
                scanlinex = scanlinex + 1;
            }
            scanlinex = 0;
            scanliney = scanliney + 1;
        }      
        return 0;
    }
    
    public int coinFarFromDoorX()
    {
        int ret_coinxpos;
        ret_coinxpos = coinxpos[0];
        
        for (int i = 0; i<(num_totalcoin+1); i++)
        {
            if ( (Math.abs(coinxpos[i] - exitxpos) > (Math.abs(ret_coinxpos - exitxpos)) ) && (coinxpos[i] != 0) )
            {
                ret_coinxpos = coinxpos[i];
            }
        //System.out.println("Coin being analysed " + coinxpos[i]);  
        //System.out.println("Current winner " + ret_coinxpos);  
        
        }
        
        //System.out.println("Subrtoutine coin x: " + ret_coinxpos); 
        
        return (ret_coinxpos);
    }
 
    public void LEVEL2()
    {
        System.out.println("PART 2 running"); 
        //find all coins on current level
        //    Boolean seekawaycoin = false;
        //    Boolean seektowardcoin = false;
        //    Boolean climbladder   = false;
        //    Boolean reachladder   = false;
        //if door position is greater than 8 go right else left
        //Just invert direction as solution for left right mirror
        
        System.out.println("First boolean" + seekawaycoin);
        System.out.println("Second boolean" + seektowardcoin);
        System.out.println("Climb Ladder boolean" + climbladder);
        System.out.println("Reach ladder boolean" + reachladder);        
        System.out.println("Player Y" + playerypos);
        System.out.println("Coin 2 Y" + coinypos[2]);        
        System.out.println("Coin 1 Y" + coinypos[1]);        
        System.out.println("Coin 0 Y" + coinypos[0]);      
        System.out.println("Top ladder Y" + ladderypos[0]);
        
        
        //Check x away from ladder
        if (seekawaycoin == false)
        {
         if ((coinypos[2] == playerypos))
         {
             num_direction = LEFT;            
             if (coinxpos[2] == playerxpos)
             {
                 seekawaycoin = true;
             }
             if (coinxpos[2] < playerxpos)
             {
                 num_direction = RIGHT;
             }
             else 
             {
                 num_direction = LEFT;
             }
             
         }
         else
         {
             seekawaycoin = true;
         }
        }
        else if (seektowardcoin == false)
        {
            if ((coinypos[1] == playerypos))
            {
                num_direction = RIGHT;
                if ((coinypos[1] == playerxpos))
                {
                    seektowardcoin = true;
                }
            }
            else
            {
                seektowardcoin = true;
            }
        }
        else if (reachladder == false)
        {             
             if (ladderxpos[1] < playerxpos)
             {
                 num_direction = RIGHT;
             }
             else if (ladderxpos[1] > playerxpos)
             {
                 num_direction = LEFT;
             }
             else
             {
                 reachladder = true;
             }
        }

        else if ((climbladder == false))
        {
            num_direction = UP;
        }
        else if ( (playerxpos == ladderxpos[1]) && (climbladder == false) && (playerypos >= ladderypos[0]))
        {
            climbladder = true;
        }
        //Go up ladder and go towards door
        else if ( (playerxpos != ladderxpos[1]) && (climbladder == false) )
        {
            if (playerxpos < ladderxpos[1])
            {
            num_direction = RIGHT; //Towards ladder
            }
            else
            {
               num_direction = LEFT; 
            }
        }        
        else if (climbladder == true)
        {
            num_direction = LEFT;
        }
        
        //Rescan player position
        
        //Direction reverse code
        if (exitxpos > 8)
        {
            if (num_direction == LEFT)
            {
                num_direction = RIGHT;
                playerxpos = playerxpos - 1;
            }
            else if (num_direction == RIGHT)
            {
                num_direction = LEFT;
                playerxpos = playerxpos + 1;
            }
        }
        
    }    
    
    
}
