package sudoku.model.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import sudoku.model.CellModel;

class CellRenderer implements TableCellRenderer {
	
	// ATTRIBUTS
	private final JTable candidates;
	private JLabel[] displayables;
	
	// CONSTRUCTEUR
	public CellRenderer(JLabel[] symbols) {
		displayables = new JLabel[symbols.length];
		for (int k = 0; k < symbols.length; ++k) {
			displayables[k] = new JLabel(symbols[k].getText());
			displayables[k].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 1));
		}
		candidates = new JTable();
		candidates.setIntercellSpacing(new Dimension());
		candidates.setShowGrid(false);
		candidates.setOpaque(false);
		candidates.setRowHeight(30);
		candidates.setDefaultRenderer(Boolean.class, new CandidateRenderer(symbols));
		candidates.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
	}

	// REQUETES
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		System.out.println("cell:" + row + "," + column);
		CellModel cell = (CellModel) value;
		if (cell.hasValue()) {
			return displayables[cell.getValue() - 1];
		} else {
			candidates.setModel(new CandidateTableModel(cell));
			return candidates;
		}
	}
	
}
