package sudoku.model.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

class CandidateRenderer implements TableCellRenderer {
	
	// ATTRIBUTS
	private final JLabel[] displayables;
	
	// CONSTRUCTEUR
	public CandidateRenderer(JLabel[] symbols) {
		displayables = new JLabel[symbols.length];
		for (int k = 0; k < symbols.length; ++k) {
			final int n = k;
			displayables[k] = new JLabel(symbols[k].getText());
			displayables[k].setHorizontalTextPosition(JLabel.CENTER);
			displayables[k].setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
			displayables[k].setOpaque(true);
			displayables[k].addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent me) {
					displayables[n].setBackground(Color.LIGHT_GRAY);
				}
				
				public void mouseExited(MouseEvent me) {
					displayables[n].setBackground(Color.WHITE);
				}
			});
		}
		
		for (JLabel jl : displayables) {
		}
	}

	// REQUETES
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Boolean isCandidate = (Boolean) value;
		int candidate = row * table.getModel().getRowCount() + column;
		Component ret = displayables[candidate];
		return ret;
	}
	
}
