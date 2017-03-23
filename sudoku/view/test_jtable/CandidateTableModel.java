package sudoku.model.gui;

import javax.swing.table.AbstractTableModel;

import sudoku.model.CellModel;

class CandidateTableModel extends AbstractTableModel {

	// ATTRIBUTS
	private CellModel model;
	private int size;
	
	// CONSTRUCTEUR
	public CandidateTableModel(CellModel cell) {
		model = cell;
		size = upperSquareRoot(cell.getCardinalCandidates());
	}
	
	// REQUETES
	@Override
	public int getRowCount() {
		return size;
	}

	@Override
	public int getColumnCount() {
		return size;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		int candidate = rowIndex * size + columnIndex + 1;
		return candidate <= model.getCardinalCandidates()
				? (Boolean) model.isCandidate(candidate)
				: Boolean.FALSE;
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return Boolean.class;
	}
	
	// OUTILS
	/**
	 * Si n >= 0, calcule la partie entière supérieure de la racine carrée de n.
	 * Sinon, renvoie 0.
	 * @pre
	 * 		n > 0
	 */
	private int upperSquareRoot(int n) {
		int res = 0;
		while (res * res < n) {
			++res;
		}
		return res;
	}

}
