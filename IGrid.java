package appli_info;
import java.util.Set;

public interface IGrid {
	int size();
	ICell[][] cells();
	ICell getCell(Coord c);
	boolean isFull();
	Set<ICell> getRow(ICoord coord);
	Set<ICell> getCol(ICoord coord);
	Set<ICell> getSector(ICoord coord);
	void reset();
	void clear();
	void changeValue(ICoord coord, int value);
	void resetValue(ICoord coord, int value);
	void addPossibility(ICoord coord, int value);
	void removePossibility(ICoord coord, int value);	
}
