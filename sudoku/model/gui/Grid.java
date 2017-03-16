package sudoku.model.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import sudoku.model.GridModel;
import sudoku.model.StdGridModel;
import sudoku.util.Coord;

public class Grid extends JPanel {
	
	// ATTRIBUTS
	private GridModel model;
	
	private Cell[][] cells;
	
	// CONSTRUCTEUR
	public Grid(GridModel model) {
		createModel(model);
		createView();
		placeComponents();
		createController();
	}
	
	// REQUETES
	public GridModel getModel() {
		return model;
	}
	
	// OUTILS
	private void createModel(GridModel model) {
		this.model = model;
	}
	
	private void createView() {
		cells = new Cell[model.size()][model.size()];
		for (int k = 0; k < cells.length; ++k) {
			Cell[] ctab = cells[k];
			for (int j = 0; j < ctab.length; ++j) {
				ctab[j] = new Cell(model.getCell(new Coord(k, j)));
			}
		}
	}
	
	private void placeComponents() {
		this.setLayout(new GridLayout(model.getNumberSectorByHeight(),
									  model.getNumberSectorByWidth()));
		for (Cell[] ctab : cells) {
			JPanel p = new JPanel(new GridLayout(model.getHeightSector(),
												 model.getWidthSector())); {
				for (Cell c : ctab) {
					p.add(c);
				}
				p.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
			}
			this.add(p);
		}
	}
	
	private void createController() {
		// rien pour l'instant
	}
	
	// TEST
	public static void main(String[] args) {
		class Bla {
			JFrame mainFrame = new JFrame();
			public Bla() {
				mainFrame.add(new Grid(new StdGridModel(3, 3)), BorderLayout.CENTER);
				mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
			
			public void display() {
				mainFrame.pack();
				mainFrame.setLocationRelativeTo(null);
				mainFrame.setVisible(true);
			}
		}
		
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new Bla().display();
			}
			
		});
	}
	
}