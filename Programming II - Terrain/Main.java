/*
Carson Seese - CIT260-02 - Terrain Assignment
02-19-19

This program takes in a grid of values and returns the top 5 highest and lowest points of the grid, as well as their locations in the grid
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

    	List<String> lines  = null; //Initialize the list as null

    	//Try and read lines from the input file
        try {
            lines = readLines("terrain.dat");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String gridSize = lines.get(0); //Set gridSize variable to contain the first line of the file
        String[] yx = gridSize.substring(1, gridSize.length() - 1).split(","); //Extract values defining the size of the table of data
        int [][] grid = new int[Integer.parseInt(yx[0])][Integer.parseInt(yx[1])]; //Initialize grid array with size data of the table
        //grid[rows][columns]

        //Populate rows and columns with data by iterating through the rows
        String[] sLine; //sLine array used to temporarily hold the individual values of a row as strings
        for (int i = 0; i < grid.length; i++) {
            sLine = lines.get(i+1).split("\\s"); //Split lines at all spaces or tabs and add to the sLine array
            for (int y = 0; y < grid[i].length; y++) {
                grid[i][y] = Integer.parseInt(sLine[y]); //Populate the values of the grid array of ints by converting values from the temporary array from strings to integers
            }
        }

        //Get the high points and low points by passing in the grid of data and the number of points to be returned
        GridPoint[] highs = getHighPoints(grid, 5);
        GridPoint[] lows = getLowPoints(grid, 5);

		//Build string of data
	    StringBuilder data = new StringBuilder("The top 5 highest points are:\n\n");

	    data.append(String.format("%5s %-10s %2s %-15s %n", "", "Height:", "", "Coordinates:"));
	    for(int i = 0; i < highs.length; i++) {
	    	data.append(String.format("%5s %-10d %2s %-15s %n", "", highs[i].getValue(), "", highs[i].getCoordinates()));
	    }

	    data.append("\nThe top 5 lowest points are:\n\n" +  String.format("%5s %-10s %2s %-15s %n", "", "Height:", "", "Coordinates:"));
	    for(int i = 0; i < lows.length; i++) {
	    	data.append(String.format("%5s %-10d %2s %-15s %n", "", lows[i].getValue(), "", lows[i].getCoordinates()));
	    }

	    //Print the results
	    System.out.println(data);
    }

	/**
	 * Returns an array of GridPoints that represent the lowest points on the supplied grid.
	 * @param grid A 2-D array of integers representing values on a grid
	 * @param numPoints The total number of lowest points to return as an array of GridPoints
	 * @return
	 */
	public static GridPoint[] getLowPoints(int[][] grid, int numPoints) {
    	GridPoint[] lowPoints = new GridPoint[numPoints];
    	GridPoint lowPoint;

	    for(int i = 0; i < lowPoints.length; i++) {
		    lowPoint = new GridPoint(0, 0, Integer.MAX_VALUE);

		    for (int r = 0; r < grid.length; r++) {
			    for (int c = 0; c < grid[r].length; c++) {
				    if(i >= 1) {
					    if(grid[r][c] < lowPoint.getValue() && grid[r][c] > lowPoints[i-1].getValue()) {
						    lowPoint = new GridPoint(r, c, grid[r][c]);
						    lowPoints[i] = lowPoint;
					    }
				    } else {
					    if(grid[r][c] < lowPoint.getValue()) {
						    lowPoint = new GridPoint(r, c, grid[r][c]);
						    lowPoints[i] = lowPoint;
					    }
				    }
			    }
		    }
	    }
	    return lowPoints;
    }

	/**
	 * Returns an array of GridPoints that represent the highest points on the supplied grid.
	 * @param grid A 2-D array of integers representing values on a grid
	 * @param numPoints The total number of highest points to return as an array of GridPoints
	 * @return
	 */
    public static GridPoint[] getHighPoints(int[][] grid, int numPoints) {
    	GridPoint[] highPoints = new GridPoint[numPoints];
    	GridPoint highPoint;

	    for(int i = 0; i < highPoints.length; i++) {
		    highPoint = new GridPoint(0, 0, Integer.MIN_VALUE);

		    for (int r = 0; r < grid.length; r++) {
			    for (int c = 0; c < grid[r].length; c++) {
				    if(i >= 1) {
					    if(grid[r][c] > highPoint.getValue() && grid[r][c] < highPoints[i-1].getValue()) {
						    highPoint = new GridPoint(r, c, grid[r][c]);
						    highPoints[i] = highPoint;
					    }
				    } else {
					    if(grid[r][c] > highPoint.getValue()) {
						    highPoint = new GridPoint(r, c, grid[r][c]);
						    highPoints[i] = highPoint;
					    }
				    }
			    }
		    }
	    }
	    return highPoints;
    }

	/**
	 * Reads the lines of a file into a list of strings
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static List<String> readLines(String fileName) throws Exception {
        List<String> lines = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;

        while ((line = br.readLine()) != null)
            lines.add(line);

        br.close();
        return lines;
    }
}