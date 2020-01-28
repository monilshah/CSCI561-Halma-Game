/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Vector;

class coordinatePointStore {

    int row_coordinate, column_coordinate;
    boolean isJump;

    coordinatePointStore(int row_coordinate, int column_coordinate) {
        this.column_coordinate = column_coordinate;
        this.row_coordinate = row_coordinate;
    }
    coordinatePointStore parent;

    @Override
    public boolean equals(Object test) {
        if (this == test) {
            return true;
        }

        if (test == null || test.getClass() != this.getClass()) {
            return false;
        }
        coordinatePointStore coordinatePointVariable = (coordinatePointStore) test;
        return (coordinatePointVariable.row_coordinate == this.row_coordinate && coordinatePointVariable.column_coordinate == this.column_coordinate);
    }

    @Override
    public int hashCode() {
        return (19 * row_coordinate + 29 * column_coordinate);
    }
}

public class HalmaGame {

    /**
     * @param args the command line arguments
     */
    static double[][] evalValueBlack = new double [16][16];
    static double[][] evalValueWhite = new double [16][16];
    static List<Game> data = new ArrayList<Game>();
    static boolean coordinate_is_in_boundary(int row, int column, int no_columns, int no_rows) {
        return column >= 0 && column < no_columns && row >= 0 && row < no_rows;
    }

     static double eval_function(int from_row, int from_column, int to_row, int to_column) {

        return Math.sqrt((from_row-to_row)*(from_row-to_row)+(from_column-to_column)*(from_column-to_column));
    }


     static List<coordinatePointStore> WhiteSingleMovesInCamp(List<coordinatePointStore> ListWhiteMove, char grid[][]
     , boolean[][] visited){

        List <coordinatePointStore> moves = new ArrayList<>();

          for (coordinatePointStore current_point : ListWhiteMove) {

                    for (int row_chng = -1; row_chng <= 1; row_chng++) {
                        for (int column_chng = -1; column_chng <= 1; column_chng++) {
                            if (row_chng == 0 && column_chng == 0) {
                                continue;
                            }

                            if ( coordinate_is_in_boundary(current_point.row_coordinate + row_chng, current_point.column_coordinate
                                    + column_chng, 16, 16)) {

                                if (
                                         grid[current_point.row_coordinate][current_point.column_coordinate] == 'W'
                                        && grid[current_point.row_coordinate + row_chng][current_point.column_coordinate + column_chng] == '.')
                                         //&&  visited[current_point.row_coordinate + row_chng][current_point.column_coordinate + column_chng] == false)
                                {
                                     coordinatePointStore intermediatePoint = new coordinatePointStore(current_point.row_coordinate + row_chng, current_point.column_coordinate + column_chng);
                                  //   visited[current_point.row_coordinate + row_chng][current_point.column_coordinate + column_chng] = true;

                                    intermediatePoint.isJump = false;
                                    intermediatePoint.parent = current_point;
                                    if(!isInCampWhite(intermediatePoint.row_coordinate, intermediatePoint.column_coordinate))
                                        moves.add(intermediatePoint);
                                   //   visited[current_point.row_coordinate + row_chng][current_point.column_coordinate + column_chng] = false;
                                }
                            }
                        }
                    }
          }

        return moves;
     }

    static List<coordinatePointStore> WhiteSingleMoves(List<coordinatePointStore> ListWhiteMove, char grid[][]
     , boolean[][] campWhiteCheck, boolean[][] visited){

        List <coordinatePointStore> moves = new ArrayList<>();

          for (coordinatePointStore current_point : ListWhiteMove) {

                    for (int row_chng = -1; row_chng <= 1; row_chng++) {
                        for (int column_chng = -1; column_chng <= 1; column_chng++) {
                            if (row_chng == 0 && column_chng == 0) {
                                continue;
                            }

                            if ( coordinate_is_in_boundary(current_point.row_coordinate + row_chng, current_point.column_coordinate
                                    + column_chng, 16, 16)) {

                                if (
                                         grid[current_point.row_coordinate][current_point.column_coordinate] == 'W'
                                        && grid[current_point.row_coordinate + row_chng][current_point.column_coordinate + column_chng] == '.'
                                        && campWhiteCheck[current_point.row_coordinate + row_chng][current_point.column_coordinate + column_chng] == false)

                                {

                                     coordinatePointStore intermediatePoint = new coordinatePointStore(current_point.row_coordinate + row_chng, current_point.column_coordinate + column_chng);
                                     intermediatePoint.isJump = false;
                                    intermediatePoint.parent = current_point;
                                     if(isInGoalWhite(current_point.row_coordinate, current_point.column_coordinate)){
                                         if(isInGoalWhite(intermediatePoint.row_coordinate, intermediatePoint.column_coordinate)){
                                             moves.add(intermediatePoint);
                                         }
                                     }
                                     else if(!isInCampWhite(intermediatePoint.row_coordinate, intermediatePoint.column_coordinate))
                                        moves.add(intermediatePoint);
                                }
                            }
                        }
                    }
          }

        return moves;
     }

    static List<coordinatePointStore> WhiteJumpMovesInCamp(List<coordinatePointStore> ListWhiteMove, char dummyGrid[][],
             boolean[][] campWhiteCheck, boolean [][] visited, int depth, List<coordinatePointStore> moves){



               for ( coordinatePointStore current_point1 : ListWhiteMove){

                        for (int row_chng = -2; row_chng <= 2; row_chng = row_chng + 2) {
                            for (int column_chng = -2; column_chng <= 2; column_chng = column_chng + 2) {
                                if (row_chng == 0 && column_chng == 0) {
                                    continue;
                                }

                                if (coordinate_is_in_boundary(current_point1.row_coordinate + row_chng, current_point1.column_coordinate
                                        + column_chng, 16, 16)) {
                                    if (dummyGrid[current_point1.row_coordinate][current_point1.column_coordinate] == 'W'
                                            && dummyGrid[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] == '.'
                                            && dummyGrid[(current_point1.row_coordinate + row_chng / 2)][(current_point1.column_coordinate + column_chng / 2)] != '.'
                                            &&  visited[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] == false) {
                                        dummyGrid[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] = 'W';

                                        dummyGrid[current_point1.row_coordinate][current_point1.column_coordinate] = '.';
                                         visited[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] = true;


                                         coordinatePointStore intermediatePoint = new coordinatePointStore(current_point1.row_coordinate + row_chng, current_point1.column_coordinate + column_chng);
                                        intermediatePoint.isJump = true;
                                    intermediatePoint.parent = current_point1;
                                    if(!isInCampWhite(intermediatePoint.row_coordinate, intermediatePoint.column_coordinate))
                                        moves.add(intermediatePoint);
                                    List<coordinatePointStore> temp = new LinkedList<>();
                                    temp.add(intermediatePoint);

                                    moves = WhiteJumpMovesInCamp(temp, dummyGrid, campWhiteCheck, visited, depth+1, moves);

                                    dummyGrid[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] = '.';

                                    dummyGrid[current_point1.row_coordinate][current_point1.column_coordinate] = 'W';
                                    visited[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] = false;

                                    }
                                }

                            }

                        }
                    }
        return moves;
     }

     static List<coordinatePointStore> WhiteJumpMoves(List<coordinatePointStore> ListWhiteMove, char dummyGrid[][],
             boolean[][] campWhiteCheck, boolean [][] visited, int depth, List<coordinatePointStore> moves){

               for ( coordinatePointStore current_point1 : ListWhiteMove){

                        for (int row_chng = -2; row_chng <= 2; row_chng = row_chng + 2) {
                            for (int column_chng = -2; column_chng <= 2; column_chng = column_chng + 2) {
                                if (row_chng == 0 && column_chng == 0) {
                                    continue;
                                }

                                if (coordinate_is_in_boundary(current_point1.row_coordinate + row_chng, current_point1.column_coordinate
                                        + column_chng, 16, 16)) {

                                    if (dummyGrid[current_point1.row_coordinate][current_point1.column_coordinate] == 'W'
                                            && dummyGrid[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] == '.'
                                            && dummyGrid[(current_point1.row_coordinate + row_chng / 2)][(current_point1.column_coordinate + column_chng / 2)] != '.'
                                           // && campWhiteCheck[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] == false
                                            &&  visited[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] == false) {

                                        dummyGrid[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] = 'W';

                                        dummyGrid[current_point1.row_coordinate][current_point1.column_coordinate] = '.';
                                         visited[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] = true;


                                         coordinatePointStore intermediatePoint = new coordinatePointStore(current_point1.row_coordinate + row_chng, current_point1.column_coordinate + column_chng);
                                        intermediatePoint.isJump = true;
                                    intermediatePoint.parent = current_point1;

                                     if(isInGoalWhite(current_point1.row_coordinate, current_point1.column_coordinate)){
                                         if(isInGoalWhite(intermediatePoint.row_coordinate, intermediatePoint.column_coordinate)){
                                             moves.add(intermediatePoint);
                                         }
                                     }
                                     else if(!isInCampWhite(intermediatePoint.row_coordinate, intermediatePoint.column_coordinate))
                                        moves.add(intermediatePoint);
                                   // System.out.println("**" + intermediatePoint.row_coordinate + " " + intermediatePoint.column_coordinate);
                                    List<coordinatePointStore> temp = new LinkedList<>();
                                    temp.add(intermediatePoint);

                                    moves = WhiteJumpMoves(temp, dummyGrid, campWhiteCheck, visited, depth+1, moves);

                                    dummyGrid[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] = '.';

                                    dummyGrid[current_point1.row_coordinate][current_point1.column_coordinate] = 'W';
                                    visited[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] = false;

                                    }
                                }

                            }

                        }
                    }
        return moves;
     }


    static List<coordinatePointStore> BlackSingleMovesInCamp(List<coordinatePointStore> ListBlackMove, char grid[][]
     , boolean[][] visited){

        List <coordinatePointStore> moves = new ArrayList<>();

          for (coordinatePointStore current_point : ListBlackMove) {

                    for (int row_chng = -1; row_chng <= 1; row_chng++) {
                        for (int column_chng = -1; column_chng <= 1; column_chng++) {
                            if (row_chng == 0 && column_chng == 0) {
                                continue;
                            }

                            if ( coordinate_is_in_boundary(current_point.row_coordinate + row_chng, current_point.column_coordinate
                                    + column_chng, 16, 16)) {

                                if (
                                         grid[current_point.row_coordinate][current_point.column_coordinate] == 'B'
                                        && grid[current_point.row_coordinate + row_chng][current_point.column_coordinate + column_chng] == '.')
                                         //&&  visited[current_point.row_coordinate + row_chng][current_point.column_coordinate + column_chng] == false)
                                {
                                     coordinatePointStore intermediatePoint = new coordinatePointStore(current_point.row_coordinate + row_chng, current_point.column_coordinate + column_chng);
                                  //   visited[current_point.row_coordinate + row_chng][current_point.column_coordinate + column_chng] = true;

                                    intermediatePoint.isJump = false;
                                    intermediatePoint.parent = current_point;
                                    if(!isInCampBlack(intermediatePoint.row_coordinate, intermediatePoint.column_coordinate))
                                        moves.add(intermediatePoint);
                                   //   visited[current_point.row_coordinate + row_chng][current_point.column_coordinate + column_chng] = false;
                                }
                            }
                        }
                    }
          }

        return moves;
     }

    static List<coordinatePointStore> BlackSingleMoves(List<coordinatePointStore> ListBlackMove, char grid[][]
     , boolean[][] campBlackCheck, boolean[][] visited){

        List <coordinatePointStore> moves = new ArrayList<>();

          for (coordinatePointStore current_point : ListBlackMove) {

                    for (int row_chng = -1; row_chng <= 1; row_chng++) {
                        for (int column_chng = -1; column_chng <= 1; column_chng++) {
                            if (row_chng == 0 && column_chng == 0) {
                                continue;
                            }

                            if ( coordinate_is_in_boundary(current_point.row_coordinate + row_chng, current_point.column_coordinate
                                    + column_chng, 16, 16)) {

                                if (
                                         grid[current_point.row_coordinate][current_point.column_coordinate] == 'B'
                                        && grid[current_point.row_coordinate + row_chng][current_point.column_coordinate + column_chng] == '.'
                                        && campBlackCheck[current_point.row_coordinate + row_chng][current_point.column_coordinate + column_chng] == false)

                                {

                                     coordinatePointStore intermediatePoint = new coordinatePointStore(current_point.row_coordinate + row_chng, current_point.column_coordinate + column_chng);
                                     intermediatePoint.isJump = false;
                                    intermediatePoint.parent = current_point;
                                     if(isInGoalBlack(current_point.row_coordinate, current_point.column_coordinate)){
                                         if(isInGoalBlack(intermediatePoint.row_coordinate, intermediatePoint.column_coordinate)){
                                             moves.add(intermediatePoint);
                                         }
                                     }
                                     else if(!isInCampBlack(intermediatePoint.row_coordinate, intermediatePoint.column_coordinate))
                                        moves.add(intermediatePoint);
                                }
                            }
                        }
                    }
          }

        return moves;
     }

    static List<coordinatePointStore> BlackJumpMovesInCamp(List<coordinatePointStore> ListBlackMove, char dummyGrid[][],
             boolean[][] campBlackCheck, boolean [][] visited, int depth, List<coordinatePointStore> moves){



               for ( coordinatePointStore current_point1 : ListBlackMove){

                        for (int row_chng = -2; row_chng <= 2; row_chng = row_chng + 2) {
                            for (int column_chng = -2; column_chng <= 2; column_chng = column_chng + 2) {
                                if (row_chng == 0 && column_chng == 0) {
                                    continue;
                                }

                                if (coordinate_is_in_boundary(current_point1.row_coordinate + row_chng, current_point1.column_coordinate
                                        + column_chng, 16, 16)) {
                                    if (dummyGrid[current_point1.row_coordinate][current_point1.column_coordinate] == 'B'
                                            && dummyGrid[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] == '.'
                                            && dummyGrid[(current_point1.row_coordinate + row_chng / 2)][(current_point1.column_coordinate + column_chng / 2)] != '.'
                                            &&  visited[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] == false) {
                                        dummyGrid[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] = 'B';

                                        dummyGrid[current_point1.row_coordinate][current_point1.column_coordinate] = '.';
                                         visited[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] = true;


                                         coordinatePointStore intermediatePoint = new coordinatePointStore(current_point1.row_coordinate + row_chng, current_point1.column_coordinate + column_chng);
                                        intermediatePoint.isJump = true;
                                    intermediatePoint.parent = current_point1;
                                    if(!isInCampBlack(intermediatePoint.row_coordinate, intermediatePoint.column_coordinate))
                                        moves.add(intermediatePoint);
                                    List<coordinatePointStore> temp = new LinkedList<>();
                                    temp.add(intermediatePoint);

                                    moves = BlackJumpMovesInCamp(temp, dummyGrid, campBlackCheck, visited, depth+1, moves);

                                    dummyGrid[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] = '.';

                                    dummyGrid[current_point1.row_coordinate][current_point1.column_coordinate] = 'B';
                                    visited[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] = false;

                                    }
                                }

                            }

                        }
                    }
        return moves;
     }

     static List<coordinatePointStore> BlackJumpMoves(List<coordinatePointStore> ListBlackMove, char dummyGrid[][],
             boolean[][] campBlackCheck, boolean [][] visited, int depth, List<coordinatePointStore> moves){

               for ( coordinatePointStore current_point1 : ListBlackMove){

                        for (int row_chng = -2; row_chng <= 2; row_chng = row_chng + 2) {
                            for (int column_chng = -2; column_chng <= 2; column_chng = column_chng + 2) {
                                if (row_chng == 0 && column_chng == 0) {
                                    continue;
                                }

                                if (coordinate_is_in_boundary(current_point1.row_coordinate + row_chng, current_point1.column_coordinate
                                        + column_chng, 16, 16)) {

                                    if (dummyGrid[current_point1.row_coordinate][current_point1.column_coordinate] == 'B'
                                            && dummyGrid[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] == '.'
                                            && dummyGrid[(current_point1.row_coordinate + row_chng / 2)][(current_point1.column_coordinate + column_chng / 2)] != '.'
                                           // && campWhiteCheck[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] == false
                                            &&  visited[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] == false) {

                                        dummyGrid[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] = 'B';

                                        dummyGrid[current_point1.row_coordinate][current_point1.column_coordinate] = '.';
                                         visited[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] = true;


                                         coordinatePointStore intermediatePoint = new coordinatePointStore(current_point1.row_coordinate + row_chng, current_point1.column_coordinate + column_chng);
                                        intermediatePoint.isJump = true;
                                    intermediatePoint.parent = current_point1;

                                     if(isInGoalBlack(current_point1.row_coordinate, current_point1.column_coordinate)){
                                         if(isInGoalBlack(intermediatePoint.row_coordinate, intermediatePoint.column_coordinate)){
                                             moves.add(intermediatePoint);
                                         }
                                     }
                                     else if(!isInCampBlack(intermediatePoint.row_coordinate, intermediatePoint.column_coordinate))
                                        moves.add(intermediatePoint);
                                   // System.out.println("**" + intermediatePoint.row_coordinate + " " + intermediatePoint.column_coordinate);
                                    List<coordinatePointStore> temp = new LinkedList<>();
                                    temp.add(intermediatePoint);

                                    moves = BlackJumpMoves(temp, dummyGrid, campBlackCheck, visited, depth+1, moves);

                                    dummyGrid[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] = '.';

                                    dummyGrid[current_point1.row_coordinate][current_point1.column_coordinate] = 'B';
                                    visited[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] = false;

                                    }
                                }

                            }

                        }
                    }
        return moves;
     }


    public static void main(String[] args) throws IOException {
        // TODO code application logic here


        data = Play.read();


        String which_Game_to_play = "";
        float timeLimit = 0;
        String player_color = "";
        //int no_of_moves = 0;
        // taking input from the file
        File file = new File("input.txt");
        Scanner sc = new Scanner(file);

        which_Game_to_play = sc.nextLine();

        //System.out.println(which_Game_to_play);
        player_color = sc.nextLine();

        String line = sc.nextLine();

        timeLimit = Float.parseFloat(line);

        char grid[][];
        grid = new char[16][16];

        char dummyGrid[][];
        dummyGrid = new char[16][16];

        List<coordinatePointStore> ListWhiteMove = new ArrayList<>();

                List<coordinatePointStore> ListWhiteMoveInCamp = new ArrayList<>();

        List<coordinatePointStore> ListBlackMove = new ArrayList<>();

            List<coordinatePointStore> ListBlackMoveInCamp = new ArrayList<>();

        ArrayList<String> camp_white = new ArrayList<>();
        boolean [][] campWhiteCheck = new boolean [16][16];
        ArrayList<String> camp_black = new ArrayList<>();
        boolean [][] campBlackCheck = new boolean [16][16];

        for(int i=0;i<5;i++){
             for(int j=0;j<5;j++){
                 if(i+j<=5){
                     camp_black.add(i+","+j);
                     campBlackCheck[i][j] = true;
                     campWhiteCheck[15-i][15-j] = true;
                     camp_white.add((15-i)+","+(15-j));
                 }
             }
         }

        for (int i = 0; i < 16; i++) {
            line = sc.nextLine();
            char[] x = line.toCharArray();

            for (int j = 0; j < 16; j++) {
                grid[i][j] = x[j];
                dummyGrid[i][j] = x[j];

                if (grid[i][j] == 'W' && isInCampWhite(i, j)) {
                    coordinatePointStore whitePawnIdx = new coordinatePointStore(i, j);
                    ListWhiteMoveInCamp.add(whitePawnIdx);
                }

                if (grid[i][j] == 'B' && isInCampBlack(i, j)) {
                    coordinatePointStore blackPawnIdx = new coordinatePointStore(i, j);
                    ListBlackMoveInCamp.add(blackPawnIdx);
                }
            }
        }

        for(int i=0; i<16; i++){
            for(int j=0; j<16; j++){

                if (grid[i][j] == 'W' && !isInCampWhite(i, j)) {
                    coordinatePointStore whitePawnIdx = new coordinatePointStore(i, j);
                    ListWhiteMove.add(whitePawnIdx);
                }

                if (grid[i][j] == 'B' && !isInCampBlack(i, j)) {
                    coordinatePointStore blackPawnIdx = new coordinatePointStore(i, j);
                    ListBlackMove.add(blackPawnIdx);
                }

            }
        }

       // for(int m=0;m<ListWhiteMove.size();m++)
         //           System.out.println(ListWhiteMove.get(m).column_coordinate + " " + ListWhiteMove.get(m).row_coordinate);

         for(int i=0; i<16; i++){
             for(int j=0; j<16; j++){
                 evalValueBlack[i][j] = (double)eval_function(i, j, 15, 15);
             }
         }

         for(int i=15; i>=0; i--){
             for(int j=15; j>=0; j--){
                 evalValueWhite[i][j] = (double)eval_function(i, j, 0, 0);
             }
         }


            if(player_color.equalsIgnoreCase("WHITE")){

                String output="";
                String write_output_to_file = "";
                String path = "";
                List <coordinatePointStore> list = new ArrayList<>();
                List <coordinatePointStore> listJump = new ArrayList<>();
                boolean visited[][] = new boolean[16][16];
                boolean visitedSingle[][] = new boolean[16][16];
                for(int i=0; i<16; i++){
                    for(int j=0; j<16; j++){
                        visitedSingle[i][j] =false;
                    }
                }

                for(coordinatePointStore piece: ListWhiteMove){
                    visited[piece.row_coordinate][piece.column_coordinate] = true;
                }

                List<coordinatePointStore> pieces = new ArrayList<>(ListWhiteMove);
                pieces.addAll(ListWhiteMoveInCamp);
                List<coordinatePointStore> ListWhiteMoveInCampCopy = new ArrayList<>();

                int campMove = 0;
                 List<coordinatePointStore> moves = new ArrayList<>();

                if(!ListWhiteMoveInCamp.isEmpty()){

                    list = WhiteSingleMovesInCamp(ListWhiteMoveInCamp, grid, campWhiteCheck);
                    listJump = WhiteJumpMovesInCamp(ListWhiteMoveInCamp, dummyGrid, campWhiteCheck, visited, 0, moves);
                    list.addAll(listJump);

                }
                if(list.isEmpty()){
                    campMove =1;
                    list = FurtherWhiteSingleMovesInCamp(ListWhiteMoveInCamp, grid, campWhiteCheck);
                    listJump = FurtherWhiteJumpMovesInCamp(ListWhiteMoveInCamp, dummyGrid, campWhiteCheck, visited, 0, moves);
                    list.addAll(listJump);
                }
                if(list.isEmpty()&& campMove == 1){
                  // System.out.println("$$$");
                     list = WhiteSingleMoves(ListWhiteMove, grid, campWhiteCheck, visitedSingle);
                    listJump = WhiteJumpMoves(ListWhiteMove, dummyGrid, campWhiteCheck, visited, 0, moves);
                    list.addAll(listJump);

                }
                //System.out.println(list.size());
                /* for seeing all the white moves generated
                for(int i=0;i<list.size();i++){
                    System.out.println(list.get(i).column_coordinate + " " + list.get(i).row_coordinate);
                }
                */
               // System.out.println(list.size()+ " **");
                coordinatePointStore bestMove = null;
                double bestEval = 100000;
                for(coordinatePointStore move: list){
                    coordinatePointStore piece = move;
                    int to_row = move.row_coordinate;
                    int to_col = move.column_coordinate;
                    while(piece.parent != null){
                        piece = piece.parent;
                    }
                    if(Play.isIn(new Game(piece, move),data))
                        continue;

                        pieces.remove(piece);
                        pieces.add(move);

                    double evalValue = 0.0d;
                     // if(WhiteMaxInGoal(grid))
                     //    evalValue = EvalWhite2(pieces, grid);
                     // else
                         evalValue = calculateEvalValueWhite(pieces);

                    if(evalValue < bestEval) {
                        bestEval = evalValue;
                        bestMove = move;
                         bestMove.isJump = move.isJump;
                    }
                    pieces.remove(move);
                    pieces.add(piece);
                }

                if(bestMove.isJump){
                 // System.out.println("Jump");
                  coordinatePointStore piece = bestMove;
                  coordinatePointStore temp = null;
                   while(piece != null){
                        path = path + piece.column_coordinate+","+piece.row_coordinate + " ";
                        temp = piece;
                        piece = piece.parent;
                        // if(piece.parent == null){
                        //   temp = piece;
                        // }
                    }

                   String child, parent;
                   String[] childParent = path.split(" ");
                   for(int i=0; i<childParent.length-1; i++){
                    child = childParent[i];
                    parent = childParent[i+1];
                         output = "J " + parent + " " +child + "\r\n" + output;

                     }
                 // System.out.println(output);

                  try ( // writing the output in back in file
                FileWriter new_file_output = new FileWriter("output.txt")) {
                    for (int char_i = 0; char_i < output.length()-1; char_i++) {
                    new_file_output.write(output.charAt(char_i));
                    }
                }
                 if(data.size()>=30){
                    data.remove(0);
                }
                data.add(new Game(temp, bestMove));
                Play.writeToData(data);
        }
                else{
                          path = path + "E " + bestMove.parent.column_coordinate+"," + bestMove.parent.row_coordinate + " " + bestMove.column_coordinate+"," + bestMove.row_coordinate;
                         try ( // writing the output in back in file
                             FileWriter new_file_output = new FileWriter("output.txt")) {
                             for (int char_i = 0; char_i < path.length(); char_i++) {
                                new_file_output.write(path.charAt(char_i));
                                }
                            }
                            if(data.size()>=30){
                                data.remove(0);
                            }
                            data.add(new Game(bestMove.parent, bestMove));
                            Play.writeToData(data);
                    }

       }// white move func ends



            if(player_color.equalsIgnoreCase("BLACK")){

                String path = "";
                String output = "";
                List <coordinatePointStore> list = new ArrayList<>();
                List <coordinatePointStore> listJump = new ArrayList<>();
                boolean visited[][] = new boolean[16][16];
                boolean visitedSingle[][] = new boolean[16][16];
                for(int i=0; i<16; i++){
                    for(int j=0; j<16; j++){
                        visitedSingle[i][j] =false;
                    }
                }

                for(coordinatePointStore piece: ListBlackMove){
                    visited[piece.row_coordinate][piece.column_coordinate] = true;
                }

                List<coordinatePointStore> pieces = new ArrayList<>(ListBlackMove);
                pieces.addAll(ListBlackMoveInCamp);
                int campMove = 0;

                 List<coordinatePointStore> moves = new ArrayList<>();
                 System.out.println(ListBlackMoveInCamp.size());
                if(!ListBlackMoveInCamp.isEmpty()){

                    list = BlackSingleMovesInCamp(ListBlackMoveInCamp, grid, campBlackCheck);
                    listJump = BlackJumpMovesInCamp(ListBlackMoveInCamp, dummyGrid, campWhiteCheck, visited, 0, moves);
                    list.addAll(listJump);

                }
                if(list.isEmpty()){
                    campMove = 1;
                    list = FurtherBlackSingleMovesInCamp(ListBlackMoveInCamp, grid, campBlackCheck);

                    listJump = FurtherBlackJumpMovesInCamp(ListBlackMoveInCamp, dummyGrid, campBlackCheck, visited, 0, moves);

                    list.addAll(listJump);
                }
                if(list.isEmpty()&& campMove==1){

                     list = BlackSingleMoves(ListBlackMove, grid, campBlackCheck, visitedSingle);
                    listJump = BlackJumpMoves(ListBlackMove, dummyGrid, campBlackCheck, visited, 0, moves);
                    list.addAll(listJump);

                }
               //System.out.println(list.size());
               // for seeing all the moves generated
//                for(int i=0;i<list.size();i++){
//                    System.out.println(list.get(i).column_coordinate + " " + list.get(i).row_coordinate);
//                }
//
                coordinatePointStore bestMove = null;
                double bestEval = 100000;

                for(coordinatePointStore move: list){
                    coordinatePointStore piece = move;
                    int to_row = move.row_coordinate;
                    int to_col = move.column_coordinate;
                    while(piece.parent != null){
                        piece = piece.parent;
                    }
                    if(Play.isIn(new Game(piece, move),data))
                        continue;
                        pieces.remove(piece);
                        pieces.add(move);

                    double evalValue = 0.0d;
                     // if(BlackMaxInGoal(grid))
                     //    evalValue = EvalBlack2(pieces, grid);
                     // else
                         evalValue = calculateEvalValueBlack(pieces);

                    if(evalValue < bestEval) {
                        bestEval = evalValue;
                        bestMove = move;
                        bestMove.isJump = move.isJump;
                    }
                    pieces.remove(move);
                    pieces.add(piece);
                }

                if(bestMove.isJump == true){

                  coordinatePointStore piece = bestMove;
                  coordinatePointStore temp = null;
                   while(piece!= null){
                        path = path + piece.column_coordinate+","+piece.row_coordinate + " ";
                        temp = piece;
                        piece = piece.parent;
                        // if(piece.parent == null){
                        //   temp = piece;
                        // }

                    }

                   String child, parent;

                   String[] childParent = path.split(" ");
                   for(int i=0; i<childParent.length-1; i++){

                    child = childParent[i];
                    parent = childParent[i+1];
                         output = "J " + parent + " " +child + "\r\n" + output;

                     }
                 // System.out.println(output);

                  try ( // writing the output in back in file
                FileWriter new_file_output = new FileWriter("output.txt")) {
            for (int char_i = 0; char_i < output.length()-1; char_i++) {
                new_file_output.write(output.charAt(char_i));
            }
            }
                  if(data.size()>=30){
                    data.remove(0);
                }
                data.add(new Game(temp, bestMove));
                Play.writeToData(data);
                }
                else {
                    path = path + "E " + bestMove.parent.column_coordinate+"," + bestMove.parent.row_coordinate + " " + bestMove.column_coordinate+"," + bestMove.row_coordinate;
                    try ( // writing the output in back in file
                FileWriter new_file_output = new FileWriter("output.txt")) {
            for (int char_i = 0; char_i < path.length(); char_i++) {
                new_file_output.write(path.charAt(char_i));
            }
            }
                    if(data.size()>=30){
                        data.remove(0);
                    }
                    data.add(new Game(bestMove.parent, bestMove));
                    Play.writeToData(data);
                }

            }// black move func ends


    }// public void main function ends

    public static double calculateEvalValueWhite(List<coordinatePointStore> pieces){
        double eval = 0.0d;
        for(coordinatePointStore piece : pieces){
            eval += evalValueWhite[piece.row_coordinate][piece.column_coordinate];
        }
        return eval;
    }

     public static double calculateEvalValueBlack(List<coordinatePointStore> pieces){
        double eval = 0.0d;
        for(coordinatePointStore piece : pieces){
            eval += evalValueBlack[piece.row_coordinate][piece.column_coordinate];
        }
        return eval;
    }

     public static boolean isInGoalWhite(int row, int col){
         if(row<5 && row>=0 && col<5 && col>=0){
             if(row == 4 && col == 4)
                 return false;
             else if (row == 4 && col == 3)
                 return false;
             else if (row == 4 && col == 2)
                 return false;
             else if (row == 3 && col == 3)
                 return false;
             else if (row == 3 && col == 4)
                 return false;
             else if (row == 2 && col == 4)
                 return false;
             else
                 return true;
         }
         return false;
     }

     public static boolean isInGoalBlack(int row, int col){
         if(row<16 && row>10 && col<16 && col>10){
             if(row == 11 && col == 11)
                 return false;
             else if (row == 11 && col == 12)
                 return false;
             else if (row == 11 && col == 13)
                 return false;
             else if (row == 12 && col == 11)
                 return false;
             else if (row == 12 && col == 12)
                 return false;
             else if (row == 13 && col == 11)
                 return false;
             else
                 return true;
         }
         return false;
     }

     public static boolean isInCampWhite(int row, int col){
         if(row<16 && row>10 && col<16 && col>10){
             if(row == 11 && col == 11)
                 return false;
             else if (row == 11 && col == 12)
                 return false;
             else if (row == 11 && col == 13)
                 return false;
             else if (row == 12 && col == 11)
                 return false;
             else if (row == 12 && col == 12)
                 return false;
             else if (row == 13 && col == 11)
                 return false;
             else
                 return true;
         }
         return false;
     }

     public static boolean isInCampBlack(int row, int col){
         if(row<5 && row>=0 && col<5 && col>=0){
             if(row == 4 && col == 4)
                 return false;
             else if (row == 4 && col == 3)
                 return false;
             else if (row == 4 && col == 2)
                 return false;
             else if (row == 3 && col == 3)
                 return false;
             else if (row == 3 && col == 4)
                 return false;
             else if (row == 2 && col == 4)
                 return false;
             else
                 return true;
         }
         return false;
     }

     static List<coordinatePointStore> FurtherWhiteSingleMovesInCamp(List<coordinatePointStore> ListWhiteMove, char grid[][]
     , boolean[][] visited){

        List <coordinatePointStore> moves = new ArrayList<>();

          for (coordinatePointStore current_point : ListWhiteMove) {

                    for (int row_chng = -1; row_chng <= 1; row_chng++) {
                        for (int column_chng = -1; column_chng <= 1; column_chng++) {
                            if (row_chng == 0 && column_chng == 0) {
                                continue;
                            }

                            if ( coordinate_is_in_boundary(current_point.row_coordinate + row_chng, current_point.column_coordinate
                                    + column_chng, 16, 16)) {

                                if (
                                         grid[current_point.row_coordinate][current_point.column_coordinate] == 'W'
                                        && grid[current_point.row_coordinate + row_chng][current_point.column_coordinate + column_chng] == '.')
                                         //&&  visited[current_point.row_coordinate + row_chng][current_point.column_coordinate + column_chng] == false)
                                {
                                     coordinatePointStore intermediatePoint = new coordinatePointStore(current_point.row_coordinate + row_chng, current_point.column_coordinate + column_chng);
                                  //   visited[current_point.row_coordinate + row_chng][current_point.column_coordinate + column_chng] = true;

                                    intermediatePoint.isJump = false;
                                    intermediatePoint.parent = current_point;
                                    if(intermediatePoint.row_coordinate<=current_point.row_coordinate && intermediatePoint.column_coordinate <= current_point.column_coordinate)
                                        moves.add(intermediatePoint);

                                   //   visited[current_point.row_coordinate + row_chng][current_point.column_coordinate + column_chng] = false;
                                }
                            }
                        }
                    }
          }

        return moves;
     }

     static List<coordinatePointStore> FurtherWhiteJumpMovesInCamp(List<coordinatePointStore> ListWhiteMove, char dummyGrid[][],
             boolean[][] campWhiteCheck, boolean [][] visited, int depth, List<coordinatePointStore> moves){

               for ( coordinatePointStore current_point1 : ListWhiteMove){

                        for (int row_chng = -2; row_chng <= 2; row_chng = row_chng + 2) {
                            for (int column_chng = -2; column_chng <= 2; column_chng = column_chng + 2) {
                                if (row_chng == 0 && column_chng == 0) {
                                    continue;
                                }

                                if (coordinate_is_in_boundary(current_point1.row_coordinate + row_chng, current_point1.column_coordinate
                                        + column_chng, 16, 16)) {
                                    if (dummyGrid[current_point1.row_coordinate][current_point1.column_coordinate] == 'W'
                                            && dummyGrid[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] == '.'
                                            && dummyGrid[(current_point1.row_coordinate + row_chng / 2)][(current_point1.column_coordinate + column_chng / 2)] != '.'
                                            &&  visited[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] == false) {
                                        dummyGrid[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] = 'W';

                                        dummyGrid[current_point1.row_coordinate][current_point1.column_coordinate] = '.';
                                         visited[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] = true;


                                         coordinatePointStore intermediatePoint = new coordinatePointStore(current_point1.row_coordinate + row_chng, current_point1.column_coordinate + column_chng);
                                        intermediatePoint.isJump = true;
                                    intermediatePoint.parent = current_point1;
                                    if(intermediatePoint.row_coordinate<=current_point1.row_coordinate && intermediatePoint.column_coordinate <= current_point1.column_coordinate)
                                        moves.add(intermediatePoint);
                                    List<coordinatePointStore> temp = new LinkedList<>();
                                    temp.add(intermediatePoint);

                                    moves = FurtherWhiteJumpMovesInCamp(temp, dummyGrid, campWhiteCheck, visited, depth+1, moves);

                                    dummyGrid[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] = '.';

                                    dummyGrid[current_point1.row_coordinate][current_point1.column_coordinate] = 'W';
                                    visited[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] = false;

                                    }
                                }

                            }

                        }
                    }
        return moves;
     }

     static List<coordinatePointStore> FurtherBlackSingleMovesInCamp(List<coordinatePointStore> ListBlackMove, char grid[][]
     , boolean[][] visited){

        List <coordinatePointStore> moves = new ArrayList<>();

          for (coordinatePointStore current_point : ListBlackMove) {

                    for (int row_chng = -1; row_chng <= 1; row_chng++) {
                        for (int column_chng = -1; column_chng <= 1; column_chng++) {
                            if (row_chng == 0 && column_chng == 0) {
                                continue;
                            }

                            if ( coordinate_is_in_boundary(current_point.row_coordinate + row_chng, current_point.column_coordinate
                                    + column_chng, 16, 16)) {

                                if (
                                         grid[current_point.row_coordinate][current_point.column_coordinate] == 'B'
                                        && grid[current_point.row_coordinate + row_chng][current_point.column_coordinate + column_chng] == '.')
                                         //&&  visited[current_point.row_coordinate + row_chng][current_point.column_coordinate + column_chng] == false)
                                {
                                     coordinatePointStore intermediatePoint = new coordinatePointStore(current_point.row_coordinate + row_chng, current_point.column_coordinate + column_chng);
                                  //   visited[current_point.row_coordinate + row_chng][current_point.column_coordinate + column_chng] = true;

                                    intermediatePoint.isJump = false;
                                    intermediatePoint.parent = current_point;
                                    if(intermediatePoint.row_coordinate>=current_point.row_coordinate && intermediatePoint.column_coordinate >= current_point.column_coordinate)
                                        moves.add(intermediatePoint);
                                   //   visited[current_point.row_coordinate + row_chng][current_point.column_coordinate + column_chng] = false;
                                }
                            }
                        }
                    }
          }

        return moves;
     }

     static List<coordinatePointStore> FurtherBlackJumpMovesInCamp(List<coordinatePointStore> ListBlackMove, char dummyGrid[][],
             boolean[][] campBlackCheck, boolean [][] visited, int depth, List<coordinatePointStore> moves){

               for ( coordinatePointStore current_point1 : ListBlackMove){

                        for (int row_chng = -2; row_chng <= 2; row_chng = row_chng + 2) {
                            for (int column_chng = -2; column_chng <= 2; column_chng = column_chng + 2) {
                                if (row_chng == 0 && column_chng == 0) {
                                    continue;
                                }

                                if (coordinate_is_in_boundary(current_point1.row_coordinate + row_chng, current_point1.column_coordinate
                                        + column_chng, 16, 16)) {

                                    if (dummyGrid[current_point1.row_coordinate][current_point1.column_coordinate] == 'B'
                                            && dummyGrid[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] == '.'
                                            && dummyGrid[(current_point1.row_coordinate + row_chng / 2)][(current_point1.column_coordinate + column_chng / 2)] != '.'
                                            &&  visited[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] == false) {
                                        dummyGrid[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] = 'B';

                                        dummyGrid[current_point1.row_coordinate][current_point1.column_coordinate] = '.';
                                         visited[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] = true;
                                         System.out.println("FBJSJS");

                                         coordinatePointStore intermediatePoint = new coordinatePointStore(current_point1.row_coordinate + row_chng, current_point1.column_coordinate + column_chng);
                                        intermediatePoint.isJump = true;
                                    intermediatePoint.parent = current_point1;
                                    if(intermediatePoint.row_coordinate>=current_point1.row_coordinate && intermediatePoint.column_coordinate >= current_point1.column_coordinate)
                                        moves.add(intermediatePoint);
                                    List<coordinatePointStore> temp = new LinkedList<>();
                                    temp.add(intermediatePoint);

                                    moves = FurtherBlackJumpMovesInCamp(temp, dummyGrid, campBlackCheck, visited, depth+1, moves);

                                    dummyGrid[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] = '.';

                                    dummyGrid[current_point1.row_coordinate][current_point1.column_coordinate] = 'W';
                                    visited[current_point1.row_coordinate + row_chng][current_point1.column_coordinate + column_chng] = false;

                                    }
                                }

                            }

                        }
                    }
        return moves;
     }

     public static boolean WhiteMaxInGoal(char [][] grid){

         int counter =0;
        for(int i=0; i<16; i++)
        {
            for(int j=0;j<16;j++)
            {
                if(grid[i][j]=='W' && isInGoalWhite(i, j))
                    counter++;
            }
        }
        if(counter >= 12)
            return true;
        else
        return false;
    }

     public static boolean BlackMaxInGoal(char [][] grid){

         int counter =0;
        for(int i=0; i<16; i++)
        {
            for(int j=0;j<16;j++)
            {
                if(grid[i][j]=='B' && isInGoalBlack(i, j))
                    counter++;
            }
        }
        if(counter >= 12)
            return true;
        else
        return false;
    }

      public static double EvalWhite2(List<coordinatePointStore> pieces, char[][] grid){

          List<coordinatePointStore> whiteSpaces = new ArrayList<>();
          List<coordinatePointStore> whiteOutside = new ArrayList<>();
          for(int i=0; i<16; i++)
        {
            for(int j=0;j<16;j++)
            {
                if(grid[i][j]=='.' && isInGoalWhite(i, j))
                    whiteSpaces.add(new coordinatePointStore(i, j));
                else if(grid[i][j] == 'W' && !isInGoalWhite(i, j))
                    whiteOutside.add(new coordinatePointStore(i, j));
           }
        }

          double eval = 0.0d;
          for(int p=0; p<whiteOutside.size() ;p++){
              for(int q=0 ;q<whiteSpaces.size();q++){
                  int r = Math.abs(whiteOutside.get(p).row_coordinate -  whiteSpaces.get(q).row_coordinate);
                  int c = Math.abs(whiteOutside.get(p).column_coordinate -  whiteSpaces.get(q).column_coordinate);

                  eval = eval + Math.sqrt(r*r + c*c);
              }
          }
        return eval;
    }

     public static double EvalBlack2(List<coordinatePointStore> pieces, char[][] grid){
        double eval = 0.0d;

        List<coordinatePointStore> blackSpaces = new ArrayList<>();
          List<coordinatePointStore> blackOutside = new ArrayList<>();

          for(int i=0; i<16; i++)
        {
            for(int j=0;j<16;j++)
            {
                if(grid[i][j]=='.' && isInGoalBlack(i, j))
                    blackSpaces.add(new coordinatePointStore(i, j));
                else if(grid[i][j] == 'B' && !isInGoalBlack(i, j))
                    blackOutside.add(new coordinatePointStore(i, j));
           }
        }


          for(int p=0; p<blackOutside.size() ;p++){
              for(int q=0 ;q<blackSpaces.size();q++){
                  int r = Math.abs(blackOutside.get(p).row_coordinate -  blackSpaces.get(q).row_coordinate);
                  int c = Math.abs(blackOutside.get(p).column_coordinate -  blackSpaces.get(q).column_coordinate);

                  eval = eval + Math.sqrt(r*r + c*c);
              }
          }
        return eval;
    }

}

class Play{
    public static List<Game> read() throws IOException{
        File f = new File("playdata.txt");
        if(!f.exists())
            return new ArrayList<Game>();
        else{
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            List<Game> data = new ArrayList<Game>();
            while((line = br.readLine())!=null){
                data.add(new Game(line));
            }
            return data;
        }
    }

    public static void writeToData(List<Game> data) throws IOException{
        File f = new File("playdata.txt");
        FileWriter fw = new FileWriter(f,false);
        BufferedWriter bw = new BufferedWriter(fw);
        for(Game m: data){
            bw.write(m.toString());
            bw.newLine();
        }
        bw.close();
    }

    public static boolean isIn(final Game move, final List<Game> data){
        for(Game m: data){
            if(move.isGameEqual(m))
                return true;
        }
       return false;
    }
}

class Game{

    coordinatePointStore oldpt;
    coordinatePointStore newpt;

    Game(){

    }
    Game(coordinatePointStore oldpt, coordinatePointStore newpt){
        this.oldpt = oldpt;
        this.newpt = newpt;
    }

    Game(final String s){
        String[] points = s.split(" ");
        if(points.length == 2){
            String[] oldpt = points[0].split(",");
            String[] newpt = points[1].split(",");
            this.oldpt = new coordinatePointStore(Integer.parseInt(oldpt[0]), Integer.parseInt(oldpt[1]));
            this.newpt = new coordinatePointStore(Integer.parseInt(newpt[0]), Integer.parseInt(newpt[1]));
        }
    }

    public String toString(){
        return this.oldpt.row_coordinate + "," + this.oldpt.column_coordinate + " " + this.newpt.row_coordinate + "," + this.newpt.column_coordinate;
    }

    public boolean isGameEqual(Game other){
        return (this.oldpt.row_coordinate == other.oldpt.row_coordinate &&
                this.oldpt.column_coordinate == other.oldpt.column_coordinate &&
                this.newpt.row_coordinate == other.newpt.row_coordinate &&
                this.newpt.column_coordinate == other.newpt.column_coordinate);
    }
}
