import java.util.Set;

public interface IGrid {
	int size();
	Cell[][] cells();
	Cell getCell(Coord c);
	boolean isFull();
	Set<Cell> getRow(Coord coord);
	Set<Cell> getCol(Coord coord);
	Set<Cell> getSector(Coord coord);
	void reset();
	void clear();
	void changeValue(Coord coord, int value);
	void resetValue(Coord coord, int value);
	void addPossibility(Coord coord, int value);
	void removePossibility(Coord coord, int value);	
}
