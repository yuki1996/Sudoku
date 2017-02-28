package sudoku.model;

import java.util.HashSet;
import java.util.Set;
import sudoku.util.ICoord;
import util.Contract;

public class Grid implements IGrid {
	// ATTRIBUTS
	private int numberSectorByWidth;
	private int numberSectorByHeight;
	private ICell[][] cells;
	
	public Grid(int numberSectorByWidth, int numberSectorByHeight)  {
		Contract.checkCondition(numberSectorByWidth > 0 && numberSectorByHeight > 0);
		this.numberSectorByWidth = numberSectorByWidth;
		this.numberSectorByHeight = numberSectorByHeight;
		int size = numberSectorByWidth * numberSectorByHeight;
		cells = new ICell[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				cells[i][j] = new Cell(size);
			}
		}
	}
	
	// REQUÊTES
	@Override
	public int size() {
		return getNumberSectorByWidth() * getNumberSectorByHeight();
	}

	@Override
	public int getNumberSectorByWidth() {
		return numberSectorByWidth;
	}

	@Override
	public int getNumberSectorByHeight() {
		return numberSectorByHeight;
	}

	@Override
	public int numberCandidates() {
		return size();
	}

	@Override
	public int getWidthSector() {
		return getNumberSectorByHeight();
	}

	@Override
	public int getHeightSector() {
		return getNumberSectorByWidth();
	}
	

	@Override
	public ICell[][] cells() {
		return cells.clone();
	}

	@Override
	public ICell getCell(ICoord coord) {
		Contract.checkCondition(coord != null 
			&& isValidCoord(coord));
		return cells()[coord.getRow()][coord.getCol()];
	}

	@Override
	public boolean isFull() {
		boolean bool = true;
		for (ICell[] cell: cells()) {
			for (ICell c: cell) {
				bool &= (c.getValue() != 0);
			}
		}
		return bool;
	}

	@Override
	public Set<ICell> getRow(ICoord coord) {
		Contract.checkCondition(coord != null
			&& isValidCoord(coord));
		Set<ICell> set = new HashSet<ICell>();
		for (int i = 0; i < size(); i++) {
			set.add(cells()[coord.getRow()][i]);
		}
		return set;
	}

	@Override
	public Set<ICell> getCol(ICoord coord) {
		Contract.checkCondition(coord != null
				&& isValidCoord(coord));
		Set<ICell> set = new HashSet<ICell>();
		for (int i = 0; i < size(); i++) {
			set.add(cells()[i][coord.getCol()]);
		}
		return set;
	}

	@Override
	public Set<ICell> getSector(ICoord coord) {
		Contract.checkCondition(coord != null
				&& isValidCoord(coord));
		Set<ICell> set = new HashSet<ICell>();
		int col = coord.getCol();
		int row = coord.getRow();
		for (int i = 0; i < getNumberSectorByWidth(); i++) {
			for (int j = 0; j < getNumberSectorByHeight(); j++) {
				set.add(cells()[(row / getNumberSectorByWidth())* getNumberSectorByWidth() + i]
						[(col / getNumberSectorByHeight()) * getNumberSectorByHeight() + j]);
			}
		}
		return set;
	}

	@Override
	public Set<ICell> getRow(int rowNum) {
		Contract.checkCondition(0 <= rowNum && rowNum < size());
		Set<ICell> set = new HashSet<ICell>();
		for (int i = 0; i < size(); i++) {
			set.add(cells()[i][rowNum]);
		}
		return set;
	}

	@Override
	public Set<ICell> getCol(int colNum) {
		Contract.checkCondition(0 <= colNum && colNum < size());
		Set<ICell> set = new HashSet<ICell>();
		for (int i = 0; i < size(); i++) {
			set.add(cells()[colNum][i]);
		}
		return set;
	}

	@Override
	public Set<ICell> getSector(int sectorRowNum, int sectorColNum) {
		Contract.checkCondition(0 <= sectorRowNum && sectorRowNum < getNumberSectorByHeight());
		Contract.checkCondition(0 <= sectorColNum && sectorColNum < getNumberSectorByWidth());
		Set<ICell> set = new HashSet<ICell>();
		for (int i = 0; i < getNumberSectorByWidth(); i++) {
			for (int j = 0; j < getNumberSectorByHeight(); j++) {
				set.add(cells()[sectorColNum * getWidthSector() + j]
						[sectorRowNum * getHeightSector() + i]);
			}
		}
		return set;
	}

	@Override
	public boolean isValidCoord(ICoord coord) {
		Contract.checkCondition(coord != null);
		return 0 <= coord.getCol() && coord.getCol() < size()
				&& 0 <= coord.getRow() && coord.getRow() < size();
	}

	@Override
	public Set<ICell> getUnitCells(ICoord coord) {
		Contract.checkCondition(coord != null
				&& isValidCoord(coord));
		Set<ICell> set = getRow(coord);
		set.addAll(getSector(coord));
		set.addAll(getCol(coord));
		return set;
	}

	@Override
	public Object clone() {
		Grid clone = null;
		try {
			clone = (Grid) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError("echec clonage");
		}
		clone.numberSectorByHeight = this.numberSectorByHeight;
		clone.numberSectorByWidth = this.numberSectorByWidth;
		clone.cells = new ICell[size()][size()];
		for (int i = 0; i < size() ; ++i) {
			for (int j = 0; j < size() ; ++j) {
				clone.cells[i][j] = (Cell) cells[i][j].clone();
			}
		}
		return clone;
	}
	
	// COMMANDES
	@Override
	public void reset() {
		for (int i = 0; i < size(); i++) {
			for (int j = 0; j < size(); j++) {
				if (cells[i][j].isModifiable()) {
					cells[i][j].removeValue();
				}
			}
		}
	}

	@Override
	public void clear() {
		for (int i = 0; i < size(); i++) {
			for (int j = 0; j < size(); j++) {
				cells[i][j] = new Cell(size());
			}
		}
	}

	@Override
	public void changeValue(ICoord coord, int value) {
		Contract.checkCondition(coord != null 
				&& isValidCoord(coord)
				&& 1 <= value  && value < numberCandidates());
		cells[coord.getRow()][coord.getCol()].setValue(value);
	}

	@Override
	public void resetValue(ICoord coord) {
		Contract.checkCondition(coord != null
				&& isValidCoord(coord));
		cells[coord.getRow()][coord.getCol()].removeValue();
	}

	@Override
	public void addCandidate(ICoord coord, int value) {
		Contract.checkCondition(coord != null && 1 <= value  && value <= numberCandidates());
		cells[coord.getRow()][coord.getCol()].addCandidate(value);
	}

	@Override
	public void removeCandidate(ICoord coord, int value) {
		Contract.checkCondition(coord != null 
				&& isValidCoord(coord)
				&& 1 <= value  && value <= numberCandidates());
		cells[coord.getRow()][coord.getCol()].removeCandidate(value);
	}

	@Override
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
	 * Retourne vrai si toutes les cellules du tableau ne sont pas nulles.
	 * @pre <pre>
	 * 		tabCells != null
	 * </pre>
	 */
	private boolean checkTab(ICell[][] tabCells) {
		assert tabCells != null;
		boolean bool = true;
		for (ICell[] cell: cells()) {
			for (ICell c: cell) {
				bool &= (c != null);
			}
		}
		return bool;
	}

}
