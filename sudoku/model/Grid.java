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
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (cells[i][j] == null) {
					cells[i][j] = new Cell(size);
				}
			}
		}
	}
	
	public Grid(int width, int height)  {
		Contract.checkCondition(width > 0 && height > 0);
		this.width = width;
		this.height = height;
		int size = width * height;
		cells = new ICell[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				cells[i][j] = new Cell(size);
			}
		}
	}
	
	public Grid() {
		width = IGrid.DEFAULT_WIDTH;
		height = IGrid.DEFAULT_HEIGHT;
		int size = width * height;
		cells = new ICell[size][size];
		for (int i = 0; i < size(); i++) {
			for (int j = 0; j < size; j++) {
				cells[i][j] = new Cell(size);
			}
		}
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
	
	public Set<ICell> getSector(ICoord coord) {
		Contract.checkCondition(coord != null);
		Set<ICell> set = new HashSet<ICell>();
		int col = coord.getCol();
		int row = coord.getRow();
		for (int j = 0; j < getHeight(); j++) {
			set.add(cells()[(col /getHeight()) * getHeight() + j][row]);
		}
		for (int j = 0; j < getWidth(); j++) {
			set.add(cells()[col] [(row / getWidth())* getWidth() + j]);
		}
		return set;
	}

	// COMMANDES
	public void reset() {
		for (int i = 0; i < size(); i++) {
			for (int j = 0; j < size(); j++) {
				if (cells[i][j].isModifiable()) {
					cells[i][j].removeValue();
				}
			}
		}
	}

	public void clear() {
		cells = null;
	}
	
	public void changeValue(ICoord coord, int value) {
		Contract.checkCondition(coord != null && 1 <= value  && value <= size());
		cells[coord.getCol()][coord.getRow()].setValue(value);
	}

	public void resetValue(ICoord coord) {
		Contract.checkCondition(coord != null);
		cells[coord.getCol()][coord.getRow()].removeValue();
	}

	public void addPossibility(ICoord coord, int value) {
		Contract.checkCondition(coord != null && 1 <= value  && value <= size());
		cells[coord.getCol()][coord.getRow()].addPossibility(value);
	}

	public void removePossibility(ICoord coord, int value) {
		Contract.checkCondition(coord != null && 1 <= value  && value <= size());
		cells[coord.getCol()][coord.getRow()].removePossibility(value);
	}

	public void changeCells(ICell[][] tabCells) {
		Contract.checkCondition(tabCells != null && tabCells.length == size() 
								&& checkTab(tabCells));
		for (int i = 0; i < size(); i++) {
			for (int j = 0; j < size(); j++) {
				cells[i][j] = tabCells[i][j];
			}
		}

	}
	
	//OUTILS
	/**
	 * Retourne vrai si toutes les cellues du tableau nt sont pas nulles.
	 * @pre <pre>
	 * 		tabCells != null
	 * </pre>
	 */
	private boolean checkTab(ICell[][] tabCells) {
		Contract.checkCondition(tabCells != null);
		boolean bool = true;
		for (ICell[] cell: cells()) {
			for (ICell c: cell) {
				bool &= (c != null);
			}
		}
		return bool;
	}

}
