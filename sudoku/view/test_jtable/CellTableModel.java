package sudoku.model.gui;

import javax.swing.table.AbstractTableModel;

import sudoku.model.CellModel;
import sudoku.model.GridModel;
import sudoku.util.Coord;
import sudoku.util.ICoord;

class CellTableModel extends AbstractTableModel {

	// ATTRIBUTS
	private GridModel model;
	private ICoord sectorCoord;
	
	// CONSTRUCTEUR
	public CellTableModel(GridModel grid, ICoord sectorCoord) {
		model = grid;
		this.sectorCoord = sectorCoord;
	}
	
	@Override
	public int getRowCount() {
		return model.getHeightSector();
	}

	@Override
	public int getColumnCount() {
		return model.getWidthSector();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		int row = model.getWidthSector() * sectorCoord.getRow() + rowIndex;
		int col = model.getWidthSector() * sectorCoord.getCol() + columnIndex;
		return model.getCell(new Coord(row, col));
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return CellModel.class;
	}

}
