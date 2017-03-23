package sudoku.model.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import sudoku.model.CellModel;
import sudoku.model.GridModel;

class SectorRenderer implements TableCellRenderer {
	
	// ATTRIBUTS
	private final JTable sector;
	
	// CONSTRUCTEUR
	public SectorRenderer(GridModel grid) {
		JLabel[] symbols = {new JLabel("1"), new JLabel("2"), new JLabel("3"),
							new JLabel("4"), new JLabel("5"), new JLabel("6"),
							new JLabel("7"), new JLabel("8"), new JLabel("9")};
		sector = new JTable() {
			public Dimension getPreferredSize() {
				return getMaximumSize();
			}
			
			public Dimension getMaximumSize() {
				return new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
			}
		};
		sector.setIntercellSpacing(new Dimension());
		sector.setShowGrid(false);
		sector.setOpaque(false);
		sector.setDefaultRenderer(CellModel.class, new CellRenderer(symbols));
		sector.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
		sector.setBackground(Color.YELLOW);
		//sector.setRowHeight(100);
		sector.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				
			}

        });
	}

	// REQUETES
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		sector.setModel((CellTableModel) value);
		int newWidth = table.getColumnModel().getTotalColumnWidth() / table.getColumnCount();
		int newHeight = table.getRowHeight(0);
		setRowHeight(table);
		System.out.println("w=" + sector.getSize().getWidth() + " h=" + sector.getSize().getHeight());
		return sector;
	}
	
	
	private void setRowHeight(JTable table) {
		int newHeight = table.getHeight() / table.getRowCount()
				/ sector.getRowCount();
		System.out.println("newHeight=" + newHeight);
		int gap = sector.getSize().height % sector.getRowCount();
		for (int k = 0; k < sector.getRowCount(); ++k) {
			sector.setRowHeight(k, newHeight + (k < gap ? 1 : 0));
		}
	}
}
