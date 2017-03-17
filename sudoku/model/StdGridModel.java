package sudoku.model;

import java.util.HashSet;
import java.util.Set;

import sudoku.util.Coord;
import sudoku.util.ICoord;
import util.Contract;

public class StdGridModel implements GridModel {
	// ATTRIBUTS
	private int numberSectorByWidth;
	private int numberSectorByHeight;
	private CellModel[][] cells;
	
	public StdGridModel(int numberSectorByWidth, int numberSectorByHeight)  {
		Contract.checkCondition(numberSectorByWidth > 0 && numberSectorByHeight > 0);
		this.numberSectorByWidth = numberSectorByWidth;
		this.numberSectorByHeight = numberSectorByHeight;
		int size = numberSectorByWidth * numberSectorByHeight;
		cells = new CellModel[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				cells[i][j] = new StdCellModel(size);
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
	public CellModel[][] cells() {
		return cells.clone();
	}

	@Override
	public CellModel getCell(ICoord coord) {
		Contract.checkCondition(coord != null 
			&& isValidCoord(coord));
		return cells()[coord.getRow()][coord.getCol()];
	}
	
	@Override
	public ICoord getCoord(CellModel cell) {
		for (int k = 0; k < size(); ++k) {
			for (int j = 0; j < size(); ++j) {
				if (cells[k][j] == cell) {
					return new Coord(k, j);
				}
			}
		}
		return null;
	}

	@Override
	public boolean isFull() {
		boolean bool = true;
		for (CellModel[] cell: cells()) {
			for (CellModel c: cell) {
				bool &= c.hasValue();
			}
		}
		return bool;
	}

	@Override
	public Set<ICoord> getRow(ICoord coord) {
		Contract.checkCondition(coord != null
			&& isValidCoord(coord));
		Set<ICoord> set = new HashSet<ICoord>();
		for (int i = 0; i < size(); i++) {
			set.add(new Coord(coord.getRow(),i));
		}
		return set;
	}

	@Override
	public Set<ICoord> getCol(ICoord coord) {
		Contract.checkCondition(coord != null
				&& isValidCoord(coord));
		Set<ICoord> set = new HashSet<ICoord>();
		for (int i = 0; i < size(); i++) {
			set.add(new Coord(i, coord.getCol()));
		}
		return set;
	}

	@Override
	public Set<ICoord> getSector(ICoord coord) {
		Contract.checkCondition(coord != null
				&& isValidCoord(coord));
		Set<ICoord> set = new HashSet<ICoord>();
		int col = coord.getCol();
		int row = coord.getRow();
		for (int i = 0; i < getNumberSectorByWidth(); i++) {
			for (int j = 0; j < getNumberSectorByHeight(); j++) {
				set.add(new Coord((row / getNumberSectorByWidth()) * getNumberSectorByWidth() + i,
						(col / getNumberSectorByHeight()) * getNumberSectorByHeight() + j));
			}
		}
		return set;
	}

	@Override
	public Set<ICoord> getRow(int rowNum) {
		Contract.checkCondition(0 <= rowNum && rowNum < size());
		Set<ICoord> set = new HashSet<ICoord>();
		for (int i = 0; i < size(); i++) {
			set.add(new Coord(rowNum, i));
		}
		return set;
	}

	@Override
	public Set<ICoord> getCol(int colNum) {
		Contract.checkCondition(0 <= colNum && colNum < size());
		Set<ICoord> set = new HashSet<ICoord>();
		for (int i = 0; i < size(); i++) {
			set.add(new Coord(i, colNum));
		}
		return set;
	}

	@Override
	public Set<ICoord> getSector(int sectorRowNum, int sectorColNum) {
		Contract.checkCondition(0 <= sectorRowNum && sectorRowNum < getNumberSectorByHeight());
		Contract.checkCondition(0 <= sectorColNum && sectorColNum < getNumberSectorByWidth());
		Set<ICoord> set = new HashSet<ICoord>();
		for (int i = 0; i < getNumberSectorByWidth(); i++) {
			for (int j = 0; j < getNumberSectorByHeight(); j++) {
				set.add(new Coord(sectorRowNum * getHeightSector() + i,
						sectorColNum * getWidthSector() + j));
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
	public Set<ICoord> getUnitCoords(ICoord coord) {
		Contract.checkCondition(coord != null
				&& isValidCoord(coord));
		Set<ICoord> set = getRow(coord);
		set.addAll(getSector(coord));
		set.addAll(getCol(coord));
		return set;
	}

	@Override
	public Object clone() {
		StdGridModel clone = null;
		try {
			clone = (StdGridModel) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError("echec clonage");
		}
		clone.numberSectorByHeight = this.numberSectorByHeight;
		clone.numberSectorByWidth = this.numberSectorByWidth;
		clone.cells = new CellModel[size()][size()];
		for (int i = 0; i < size() ; ++i) {
			for (int j = 0; j < size() ; ++j) {
				clone.cells[i][j] = (StdCellModel) cells[i][j].clone();
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
				cells[i][j] = new StdCellModel(size());
			}
		}
	}

	@Override
	public void setValue(CellModel c, int value) {
		Contract.checkCondition(c != null 
				&& 1 <= value  && value <= numberCandidates());
		c.setValue(value);
		for (int i = 0; i < size(); i++) {
			for (int j = 0; j < size(); j++) {
				if (! cells()[i][j].hasValue()) {
					updateEasyPossibilities(new Coord(i, j));
				}
			}
		}
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
	public void changeCells(CellModel[][] tabCells) {
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
	private boolean checkTab(CellModel[][] tabCells) {
		assert tabCells != null;
		boolean bool = true;
		for (CellModel[] cell: cells()) {
			for (CellModel c: cell) {
				bool &= (c != null);
			}
		}
		return bool;
	}
	//mise à jour des cellules
	private void updateEasyPossibilities(ICoord c) {
		Contract.checkCondition(c != null);
		Contract.checkCondition(isValidCoord(c)); 
		Contract.checkCondition(getCell(c).getValue() > 0);
		int n = getCell(c).getValue();
		Set<ICoord> set = getUnitCoords(c);
		for (ICoord coord : set) {
			getCell(coord).removeCandidate(n);
		} 
		
	}

}
