package sudoku.model.gui;

import javax.swing.table.AbstractTableModel;

import sudoku.model.GridModel;
import sudoku.util.Coord;
import sudoku.util.ICoord;

class SectorTableModel extends AbstractTableModel {

	// ATTRIBUTS
	private CellTableModel[][] model;
	
	// CONSTRUCTEUR
	public SectorTableModel(GridModel grid) {
		model = new CellTableModel[grid.getNumberSectorByHeight()][grid.getNumberSectorByWidth()];
		for (int k = 0; k < model.length; ++k) {
			for (int j = 0; j < model[k].length; ++j) {
				model[k][j] = new CellTableModel(grid, new Coord(k, j));
			}
		}
	}
	
	@Override
	public int getRowCount() {
		return model.length;
	}

	@Override
	public int getColumnCount() {
		return model[0].length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return model[rowIndex][columnIndex];
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return CellTableModel.class;
	}

}
