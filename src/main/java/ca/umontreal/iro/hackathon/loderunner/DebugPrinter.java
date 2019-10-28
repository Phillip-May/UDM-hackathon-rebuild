/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.umontreal.iro.hackathon.loderunner;

/**
 *
 * @author Admin123
 */
public class DebugPrinter {
    
    public static int PrintMap(String[] grid) {
         System.out.println("Place Holder to check array conts with debugger");
        for (int i=0; i<grid.length; i++) {
            String ligne = grid[i];
            System.out.println(ligne);
        }
        return 0;
    }

    public static int RouteFail(int inp_x1, int inp_y1, int inp_x2,int inp_y2) {
        System.out.println("Failed to find path with parameters:.");
        System.out.println("Path find start x and y");
        System.out.println(inp_x1);
        System.out.println(inp_y1);
        System.out.println("Path find end x and y");
        System.out.println(inp_x2);
        System.out.println(inp_y2);        
        return 0;
    }
        
}
