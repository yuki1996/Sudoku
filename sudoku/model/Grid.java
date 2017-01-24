package sudoku.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import sudoku.util.ICoord;
import util.Contract;

public class Grid implements IGrid {
	
	// ATTRIBUTS
	private int width;
	private int height;
	private ICell[][] cells;
	
	
	// CONSTRUCTEURS
	
	public Grid(int width, int height, Map<ICoord, Integer> map) {
		Contract.checkCondition(map != null && width > 0 && height > 0);
		this.width = width;
		this.height = height;
		int size = width * height;
		cells = new ICell[size()][size];
		for (ICoord c : map.keySet()) {
			cells[c.getCol()][c.getRow()] = new Cell(map.get(c).intValue(),false, size);
		}
	}
	
	public Grid(int width, int height)  {
		Contract.checkCondition(width > 0 && height > 0);
		this.width = width;
		this.height = height;
		int size = width * height;
		cells = new ICell[size][size];
	}
	
	public Grid() {
		width = IGrid.DEFAULT_WIDTH;
		height = IGrid.DEFAULT_HEIGHT;
		int size = width * height;
		cells = new ICell[size][size];
	}
	
	// REQUÊTES
	
	public int size() {
		return getWidth() * getHeight();
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	public ICell[][] cells() {
		return cells.clone();
	}

	public ICell getCell(ICoord coord) {
		Contract.checkCondition(coord != null);
		return cells[coord.getCol()][coord.getRow()];
	}

	public boolean isFull() {
		boolean bool = true;
		for (ICell[] cell: cells()) {
			for (ICell c: cell) {
				bool &= (c.getValue() != 0);
			}
		}
		return bool;
	}

	public Set<ICell> getRow(ICoord coord) {
		Contract.checkCondition(coord != null);
		Set<ICell> set = new HashSet<ICell>();
		for (int i = 0; i < size(); i++) {
			set.add(cells()[i][coord.getRow()]);
		}
		return set;
	}

	public Set<ICell> getCol(ICoord coord) {
		Contract.checkCondition(coord != null);
		Set<ICell> set = new HashSet<ICell>();
		for (int i = 0; i < size(); i++) {
			set.add(cells()[coord.getCol()][i]);
		}
		return set;
	}
	
	//À REVOIR
	public Set<ICell> getSector(ICoord coord) {
		Contract.checkCondition(coord != null);
		Set<ICell> set = new HashSet<ICell>();
		int col = coord.getCol();
		int row = coord.getRow();
		for (int j = 0; j < getWidth(); j++) {
			set.add(cells()[(col / getWidth()* getWidth()) + j][row]);
		}
		for (int j = 0; j < getHeight(); j++) {
			set.add(cells()[col] [(row / getHeight()) * getHeight() + j]);
		}
		return set;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public void changeValue(ICoord coord, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resetValue(ICoord coord) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addPossibility(ICoord coord, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removePossibility(ICoord coord, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void changeCells(ICell[][] tabCells) {
		// TODO Auto-generated method stub

	}

}
