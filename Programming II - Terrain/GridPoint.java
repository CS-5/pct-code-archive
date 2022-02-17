/*
Carson Seese - CIT260-02 - Terrain Assignment
02-19-19

This class defines a point on a grid represented by it's row and column as well as it's value. It is used to store and retrieve this data as needed in the Terrain_Assignment main application
 */

public class GridPoint {

    private int row = 0, column = 0, value = 0; //Init default values

	/**
	 * This is the constructor for GridPoint, accepting values for the row, column, and the value itself.
	 * @param row
	 * @param column
	 * @param value
	 */
	public GridPoint(int row, int column, int value) {
        super();
        this.row = row;
        this.column = column;
        this.value = value;
    }

	/**
	 * The getCoordinates returns a formatted string of this GridPoint's coordinates
	 * @return
	 */
	public String getCoordinates() {
		return String.format("(%d, %d)", column, row);
    }

	/**
	 * toString returns a formatted string of this GridPoint
	 * @return
	 */
	@Override
	public String toString() {
		return "GridPoint{" +
				"row=" + row +
				", column=" + column +
				", value=" + value +
				'}';
	}

	/**
	 * getValue returns the value of a specific GridPoint
	 * @return
	 */
    public int getValue() {
        return value;
    }
}
