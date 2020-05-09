package com.example.dp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class GameView extends View {
    private enum Direction {
        DOWN, LEFT, NE, NW, RIGHT, SE, SW, UP,
    }

    private Cell[][] cells;
    private int[][] gridpointer;
    private Cell player,exit;
    private static final int COLS = 9, ROWS = 10;
    private static final int COL = 10,ROW = 9;
    private float cellSize,hMargin,vMargin;
    private static final int WALL_THICKNESS = 5;
    private Paint wallPaint,playerPaint,exitPaint,obstaclePaint,tempPaint,solPaint;
    private float margin;
    private Context cnt;
    private static Stack<pair> Path;

    int[][] grid =
            {
                    { 1, 0, 1, 1, 1, 1, 0, 1, 1, 1 },
                    { 1, 1, 1, 0, 1, 1, 1, 0, 1, 1 },
                    { 1, 1, 1, 0, 1, 1, 0, 1, 0, 1 },
                    { 0, 0, 1, 0, 1, 0, 0, 0, 0, 1 },
                    { 1, 1, 1, 0, 1, 1, 1, 0, 1, 0 },
                    { 1, 0, 1, 1, 1, 1, 0, 1, 0, 0 },
                    { 1, 0, 0, 0, 0, 1, 0, 0, 0, 1 },
                    { 1, 0, 1, 1, 1, 1, 0, 1, 1, 1 },
                    { 1, 1, 1, 0, 0, 0, 1, 0, 0, 1 },
            };

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        cnt = context;
        playerPaint = new Paint();
        playerPaint.setColor(Color.RED);
        exitPaint = new Paint();
        exitPaint.setColor(Color.YELLOW);
        wallPaint = new Paint();
        obstaclePaint = new Paint();
        solPaint = new Paint();
        solPaint.setColor(Color.GREEN);
        obstaclePaint.setColor(Color.CYAN);
        wallPaint.setColor(Color.WHITE);
        wallPaint.setStrokeWidth(WALL_THICKNESS);
        creatMaze();
    }

    private void creatMaze(){
        Stack<Cell> stack = new Stack<>();
        Cell current, next;

        cells = new Cell[COLS][ROWS];
        for(int x = 0;x<COLS;x++){
            for(int y =0;y<ROWS;y++){
                cells[x][y] = new Cell(x,y);
            }
        }

        player = cells[0][0];
        exit = cells[COLS-1][ROWS-1];

        generateObstacles();

    }

    private void generateObstacles() {


        gridpointer = grid;

        for(int x =0;x<COLS;x++){
            for(int y = 0;y<ROWS;y++){
                if(grid[x][y] == 0)
                    cells[x][y].obstacle = true;
            }
        }

    }

    private void movePlayer(Direction direction){
        switch (direction){
            case UP: if(player.row -1 >= 0 && !cells[player.col][player.row -1].obstacle){
                player = cells[player.col][player.row -1];
            }
                break;
            case DOWN: if(player.row +1< ROWS && !cells[player.col][player.row+1].obstacle){
                player = cells[player.col][player.row+1];
            }
                break;
            case LEFT: if(player.col -1 >= 0 && !cells[player.col-1][player.row].obstacle){
                player = cells[player.col-1][player.row];
            }
                break;
            case RIGHT: if(player.col+ 1 < COLS && !cells[player.col+1][player.row].obstacle){
                player = cells[player.col+1][player.row];
            }
                break;
            case NE: if(player.row -1 >= 0 && player.col +1 < COLS && !cells[player.col+1][player.row-1].obstacle){
                player = cells[player.col+1][player.row-1];
            }
                break;
            case NW: if(player.row -1 >= 0  && player.col -1 >= 0 && !cells[player.col-1][player.row-1].obstacle){
                player = cells[player.col-1][player.row-1];
            }
                break;
            case SE: if(player.row +1 < ROWS&& player.col+1 < COLS && !cells[player.col+1][player.row+1].obstacle){
                player = cells[player.col+1][player.row+1];
            }
                break;
            case SW: if(player.col -1 >= 0 && player.row +1 < ROWS && !cells[player.col-1][player.row+1].obstacle){
                player = cells[player.col-1][player.row+1];
            }
                break;
        }
        checkExit();
        invalidate();
    }

    private void checkExit(){
        if(player == exit){
            Solution();
            Toast.makeText(cnt,"completed",Toast.LENGTH_SHORT).show();
            // creatMaze();
        }
    }


    private void Solution(){
        pair source = new pair(0,0);
        pair dest = new pair(ROW-1,COL-1);
        astarSearch(grid,source,dest);

        for(pair p: Path){
            cells[p.row][p.col].includesol = true;
            Log.i("coords",p.col+" "+p.row);

        }

        //  refreshDrawableState();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN) return true;

        if(event.getAction() == MotionEvent.ACTION_MOVE){
            float x = event.getX();
            float y = event.getY();

            float playerCenterX = hMargin + (player.col+0.5f)*cellSize;
            float playerCenterY = vMargin + (player.row+0.5f)*cellSize;

            float dx = x - playerCenterX;
            float dy = y - playerCenterY;

            float absDx = Math.abs(dx);
            float absDy = Math.abs(dy);

            if(absDx > cellSize &&  absDy > cellSize){
                if (dx > 0  && dy > 0){
                    movePlayer(Direction.SE);
                }
                else if( dx > 0 && dy < 0){
                    movePlayer(Direction.NE);
                }
                else if( dx > 0  && dy == 0){
                    movePlayer(Direction.RIGHT);
                }
                else if(dx <0 && dy > 0){
                    movePlayer(Direction.SW);
                }
                else if(dx < 0 && dy < 0){
                    movePlayer(Direction.NW);
                }

            }
            else if(absDy < cellSize){
                if(dx > 0){
                    movePlayer(Direction.RIGHT);
                }
                else{
                    movePlayer(Direction.LEFT);
                }
            }

            else if(absDx < cellSize){
                if(dy > 0){
                    movePlayer(Direction.DOWN);
                }
                else{
                    movePlayer(Direction.UP);
                }
            }



        }

        return super.onTouchEvent(event);
    }

    /**
     *
     *
     *helper functions
     */
    private static boolean isValid(int row, int col) {

        return ((row >= 0) && (row < ROW) && (col >= 0) && (col < COL));

    }


    private static boolean isUnBlocked(int[][] grid, int row2, int col2) {

        if (grid[row2][col2] == 1) {
            return true;
        } else {
            return false;
        }

    }




    private static boolean isDestination(int row2, int col2, pair dest) {

        if (row2 == dest.row && col2 == dest.col) {
            return true;
        } else {
            return false;
        }

    }


    private static double calculateHValue(int i, int j, pair dest) {

        return ((double) Math.sqrt((i - dest.row) * (i - dest.row) + (j - dest.col) * (j - dest.col)));

    }
    /*******/

    /*
     *
     *
     *
     * traceapath
     *
     *
     *
     *
     * */


    private  static  void tracePath(cell[][] cellDetails, pair dest) {

        System.out.println("\n The path is ");

        int row = dest.row;
        int col = dest.col;


        Path = new Stack<pair>();

        while (!(cellDetails[row][col].parent_i == row
                && cellDetails[row][col].parent_j == col)) {
            Path.push(new pair(row, col));
            int temp_row = cellDetails[row][col].parent_i;
            int temp_col = cellDetails[row][col].parent_j;
            row = temp_row;
            col = temp_col;
        }

        Path.push(new pair(row, col));

//        while(!(Path.isEmpty()))
//        {
//            pair p=Path.lastElement();
//            Path.pop();
//            System.out.println("-->"+"("+p.row+","+p.col+")");
//        }

        return;

    }

    /*
     *
     *
     * a star implementation
     *
     *
     *
     * */
    public static void astarSearch(int[][] grid, pair src, pair dest) {


        if (isValid(src.row, src.col) == false) {
            System.out.println("Source is Invalid");
            return;
        }
        if (isValid(dest.row, dest.col) == false) {
            System.out.println("Destination is Invalid");
            return;
        }

        if (isUnBlocked(grid, src.row, src.col) == false || isUnBlocked(grid, dest.row, dest.col) == false) {

            System.out.println("Either the source or destination is Blocked");
            return;
        }

        if (isDestination(src.row, src.col, dest) == true) {
            System.out.println("We are already at the destination\n");
            return;
        }


        boolean closedList[][] = new boolean[ROW][COL];


        int i, j;

        for (i = 0; i < ROW; i++) {
            for (j = 0; j < COL; j++) {
                closedList[i][j] = false;
            }
        }

        cell cellDetails[][] = new cell[ROW][COL];

        for (i = 0; i < ROW; i++) {
            for (j = 0; j < COL; j++) {

                cellDetails[i][j] = new cell(-1,-1, Float.MAX_VALUE,Float.MAX_VALUE,Float.MAX_VALUE);
            }
        }


        i = src.row;
        j = src.col;

        cellDetails[i][j].f = 0.0;
        cellDetails[i][j].g = 0.0;
        cellDetails[i][j].h = 0.0;
        cellDetails[i][j].parent_i = i;
        cellDetails[i][j].parent_j = j;

        ArrayList<pPair> openList = new ArrayList<pPair>();
        openList.add(new pPair(0.0, new pair(i, j)));
        boolean foundDest = false;

        while (!(openList.isEmpty())) {


            pPair p = openList.get(0);

            openList.remove(0);


            i = p.a.row;
            j = p.a.col;

            closedList[i][j] = true;


            double gNew, hNew, fNew;


            if (isValid(i - 1, j) == true) {
                if (isDestination(i - 1, j, dest) == true) {
                    cellDetails[i - 1][j].parent_i = i;
                    cellDetails[i - 1][j].parent_j = j;

                    System.out.println("The destination is found\n");

                    tracePath(cellDetails, dest);

                    foundDest = true;

                    return;
                } else if (closedList[i - 1][j] == false &&
                        isUnBlocked(grid, i - 1, j) == true) {
                    gNew = cellDetails[i][j].g + 1.0;
                    hNew = calculateHValue(i - 1, j, dest);
                    fNew = gNew + hNew;


                    if (cellDetails[i - 1][j].f == Float.MAX_VALUE ||
                            cellDetails[i - 1][j].f > fNew) {
                        openList.add(new pPair(fNew, new pair(i - 1, j)));

                        // Update the details of this cell
                        cellDetails[i - 1][j].f = fNew;
                        cellDetails[i - 1][j].g = gNew;
                        cellDetails[i - 1][j].h = hNew;
                        cellDetails[i - 1][j].parent_i = i;
                        cellDetails[i - 1][j].parent_j = j;
                    }
                }

            }

            /// -----------------------------------------------

            if (isValid(i + 1, j) == true) {
                // If the destination cell is the same as the
                // current successor
                if (isDestination(i + 1, j, dest) == true) {
                    // Set the Parent of the destination cell
                    cellDetails[i + 1][j].parent_i = i;
                    cellDetails[i + 1][j].parent_j = j;
                    System.out.println("The destination cell is found\n");
                    tracePath(cellDetails, dest);
                    foundDest = true;
                    return;
                } else if (closedList[i + 1][j] == false &&
                        isUnBlocked(grid, i + 1, j) == true) {
                    gNew = cellDetails[i][j].g + 1.0;
                    hNew = calculateHValue(i + 1, j, dest);
                    fNew = gNew + hNew;


                    if (cellDetails[i + 1][j].f == Float.MAX_VALUE ||
                            cellDetails[i + 1][j].f > fNew) {
                        openList.add(new pPair(fNew, new pair(i + 1, j)));

                        cellDetails[i + 1][j].f = fNew;
                        cellDetails[i + 1][j].g = gNew;
                        cellDetails[i + 1][j].h = hNew;
                        cellDetails[i + 1][j].parent_i = i;
                        cellDetails[i + 1][j].parent_j = j;
                    }
                }
            }

            //-------------------------------------------------

            if (isValid(i, j + 1) == true) {
                // If the destination cell is the same as the
                // current successor
                if (isDestination(i, j + 1, dest) == true) {
                    // Set the Parent of the destination cell
                    cellDetails[i][j + 1].parent_i = i;
                    cellDetails[i][j + 1].parent_j = j;
                    System.out.println("The destination cell is found\n");
                    tracePath(cellDetails, dest);
                    foundDest = true;
                    return;
                }

                // If the successor is already on the closed
                // list or if it is blocked, then ignore it.
                // Else do the following
                else if (closedList[i][j + 1] == false &&
                        isUnBlocked(grid, i, j + 1) == true) {
                    gNew = cellDetails[i][j].g + 1.0;
                    hNew = calculateHValue(i, j + 1, dest);
                    fNew = gNew + hNew;


                    if (cellDetails[i][j + 1].f == Float.MAX_VALUE ||
                            cellDetails[i][j + 1].f > fNew) {
                        openList.add(new pPair(fNew,
                                new pair(i, j + 1)));

                        // Update the details of this cell
                        cellDetails[i][j + 1].f = fNew;
                        cellDetails[i][j + 1].g = gNew;
                        cellDetails[i][j + 1].h = hNew;
                        cellDetails[i][j + 1].parent_i = i;
                        cellDetails[i][j + 1].parent_j = j;
                    }
                }
            }


            //-----------------------------------------------------------

            if (isValid(i, j - 1) == true) {

                if (isDestination(i, j - 1, dest) == true) {

                    cellDetails[i][j - 1].parent_i = i;
                    cellDetails[i][j - 1].parent_j = j;
                    System.out.println("The destination cell is found\n");
                    tracePath(cellDetails, dest);
                    foundDest = true;
                    return;
                } else if (closedList[i][j - 1] == false &&
                        isUnBlocked(grid, i, j - 1) == true) {
                    gNew = cellDetails[i][j].g + 1.0;
                    hNew = calculateHValue(i, j - 1, dest);
                    fNew = gNew + hNew;


                    if (cellDetails[i][j - 1].f == Float.MAX_VALUE ||
                            cellDetails[i][j - 1].f > fNew) {
                        openList.add(new pPair(fNew,
                                new pair(i, j - 1)));

                        // Update the details of this cell
                        cellDetails[i][j - 1].f = fNew;
                        cellDetails[i][j - 1].g = gNew;
                        cellDetails[i][j - 1].h = hNew;
                        cellDetails[i][j - 1].parent_i = i;
                        cellDetails[i][j - 1].parent_j = j;
                    }
                }
            }

            if (isValid(i - 1, j + 1) == true) {

                if (isDestination(i - 1, j + 1, dest) == true) {

                    cellDetails[i - 1][j + 1].parent_i = i;
                    cellDetails[i - 1][j + 1].parent_j = j;
                    System.out.println("The destination cell is found\n");
                    tracePath(cellDetails, dest);
                    foundDest = true;
                    return;
                } else if (closedList[i - 1][j + 1] == false &&
                        isUnBlocked(grid, i - 1, j + 1) == true) {
                    gNew = cellDetails[i][j].g + 1.414;
                    hNew = calculateHValue(i - 1, j + 1, dest);
                    fNew = gNew + hNew;


                    if (cellDetails[i - 1][j + 1].f == Float.MAX_VALUE ||
                            cellDetails[i - 1][j + 1].f > fNew) {
                        openList.add(new pPair(fNew,
                                new pair(i - 1, j + 1)));

                        // Update the details of this cell
                        cellDetails[i - 1][j + 1].f = fNew;
                        cellDetails[i - 1][j + 1].g = gNew;
                        cellDetails[i - 1][j + 1].h = hNew;
                        cellDetails[i - 1][j + 1].parent_i = i;
                        cellDetails[i - 1][j + 1].parent_j = j;
                    }
                }
            }


            //---------------------------------------------------------------

            if (isValid(i - 1, j - 1) == true) {

                if (isDestination(i - 1, j - 1, dest) == true) {

                    cellDetails[i - 1][j - 1].parent_i = i;
                    cellDetails[i - 1][j - 1].parent_j = j;
                    System.out.println("The destination cell is found\n");
                    tracePath(cellDetails, dest);
                    foundDest = true;
                    return;
                } else if (closedList[i - 1][j - 1] == false &&
                        isUnBlocked(grid, i - 1, j - 1) == true) {
                    gNew = cellDetails[i][j].g + 1.414;
                    hNew = calculateHValue(i - 1, j - 1, dest);
                    fNew = gNew + hNew;


                    if (cellDetails[i - 1][j - 1].f == Float.MAX_VALUE ||
                            cellDetails[i - 1][j - 1].f > fNew) {
                        openList.add(new pPair(fNew, new pair(i - 1, j - 1)));
                        // Update the details of this cell
                        cellDetails[i - 1][j - 1].f = fNew;
                        cellDetails[i - 1][j - 1].g = gNew;
                        cellDetails[i - 1][j - 1].h = hNew;
                        cellDetails[i - 1][j - 1].parent_i = i;
                        cellDetails[i - 1][j - 1].parent_j = j;
                    }
                }
            }

            //-------------------------------------------------------


            if (isValid(i + 1, j + 1) == true) {

                if (isDestination(i + 1, j + 1, dest) == true) {

                    cellDetails[i + 1][j + 1].parent_i = i;
                    cellDetails[i + 1][j + 1].parent_j = j;
                    System.out.println("The destination cell is found\n");
                    tracePath(cellDetails, dest);
                    foundDest = true;
                    return;
                } else if (closedList[i + 1][j + 1] == false &&
                        isUnBlocked(grid, i + 1, j + 1) == true) {
                    gNew = cellDetails[i][j].g + 1.414;
                    hNew = calculateHValue(i + 1, j + 1, dest);
                    fNew = gNew + hNew;


                    if (cellDetails[i + 1][j + 1].f == Float.MAX_VALUE ||
                            cellDetails[i + 1][j + 1].f > fNew) {
                        openList.add(new pPair(fNew,
                                new pair(i + 1, j + 1)));

                        // Update the details of this cell
                        cellDetails[i + 1][j + 1].f = fNew;
                        cellDetails[i + 1][j + 1].g = gNew;
                        cellDetails[i + 1][j + 1].h = hNew;
                        cellDetails[i + 1][j + 1].parent_i = i;
                        cellDetails[i + 1][j + 1].parent_j = j;
                    }
                }
            }


            //----------------------------------------------------------

            if (isValid(i + 1, j - 1) == true) {

                if (isDestination(i + 1, j - 1, dest) == true) {

                    cellDetails[i + 1][j - 1].parent_i = i;
                    cellDetails[i + 1][j - 1].parent_j = j;
                    System.out.println("The destination cell is found\n");
                    tracePath(cellDetails, dest);
                    foundDest = true;
                    return;
                } else if (closedList[i + 1][j - 1] == false &&
                        isUnBlocked(grid, i + 1, j - 1) == true) {
                    gNew = cellDetails[i][j].g + 1.414;
                    hNew = calculateHValue(i + 1, j - 1, dest);
                    fNew = gNew + hNew;


                    if (cellDetails[i + 1][j - 1].f == Float.MAX_VALUE ||
                            cellDetails[i + 1][j - 1].f > fNew) {
                        openList.add(new pPair(fNew,
                                new pair(i + 1, j - 1)));

                        // Update the details of this cell
                        cellDetails[i + 1][j - 1].f = fNew;
                        cellDetails[i + 1][j - 1].g = gNew;
                        cellDetails[i + 1][j - 1].h = hNew;
                        cellDetails[i + 1][j - 1].parent_i = i;
                        cellDetails[i + 1][j - 1].parent_j = j;
                    }
                }
            }

        }


        if (foundDest == false)
            System.out.println("Failed to find the Destination Cell\n");

        return;


    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        int width = getWidth();
        int height = getHeight();

        if(width/height < COLS/ROWS){
            cellSize = width/(COLS +8);
        }
        else{
            cellSize = height/(ROWS+8 );
        }

        hMargin = (width - COLS*cellSize)/2;
        vMargin = (height - ROWS*cellSize)/2;
        margin = cellSize/10;

        canvas.translate(hMargin,vMargin);

        for(int x = 0;x<COLS;x++){
            for(int y =0;y<ROWS;y++) {
                // cells[x][y] = new Cell(x,y);
                if(cells[x][y].obstacle == true){
                    tempPaint = obstaclePaint;
                }else if(cells[x][y].includesol == true){
                    tempPaint = solPaint;
                }
                else{
                    tempPaint = wallPaint;
                }
                canvas.drawRect(x*cellSize+margin,
                        y*cellSize+margin,
                        (x+1)*cellSize-margin,
                        (y+1)*cellSize-margin,
                        tempPaint);
            }

        }
        canvas.drawRect(
                player.col*cellSize+margin,
                player.row*cellSize+margin,
                (player.col+1)*cellSize-margin,
                (player.row+1)*cellSize-margin,
                playerPaint
        );

        canvas.drawRect(
                exit.col*cellSize+margin,
                exit.row*cellSize+margin,
                (exit.col+1)*cellSize-margin,
                (exit.row+1)*cellSize-margin,
                exitPaint
        );




    }

    private class Cell{
        boolean
                obstacle = false,
                includesol = false;
        int col,row ;


        public Cell(int col, int row) {
            this.col = col;
            this.row = row;
        }
    }

}

