package sudoku.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import sudoku.model.GridModel;
import sudoku.model.StdSudokuModel;
import sudoku.model.SudokuModel;

public class Grid extends JPanel {
	
	// ATTRIBUTS
	public final int BORDER_SIZE = 1;
	
	private final int SIZE = 900;
	
	private GridModel model;
	
	private JLabel[] symbols;
	private JPanel squareGrid;	// la classe Grid n'est qu'une enveloppe
	
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
		squareGrid = new JPanel();
		squareGrid.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		this.setPreferredSize(new Dimension(SIZE, SIZE));
	}
	
	private void placeComponents() {
		this.setLayout(new BorderLayout()); {	// layout à changer
			squareGrid.setLayout(new GridLayout(model.getNumberSectorByHeight(),
					model.getNumberSectorByWidth())); {
				for (int k = 0; k < model.numberCandidates(); ++k) {
					JPanel q = new JPanel(new GridLayout(model.getHeightSector(),
							model.getWidthSector())); {
						for (int j = 0; j < model.numberCandidates(); ++j) {
							int row = (k / model.getNumberSectorByWidth())
									* model.getWidthSector()
									+ j / model.getWidthSector();
							int col = (k % model.getNumberSectorByHeight())
									* model.getHeightSector()
									+ j % model.getHeightSector();
							q.add(new Cell(model.cells()[row][col]));
						}
					}
					q.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
					squareGrid.add(q);
				}
			}
		}
		this.add(squareGrid, BorderLayout.CENTER);
	}
	
	private void createController() {
		squareGrid.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				
			}

        });
	}
	
	// TEST
	public static void main(String[] args) {
		class Bla {
			private static final String filename = "grille2.txt";
			JFrame mainFrame = new JFrame();
			SudokuModel model;
			public Bla() {
				try {
					model = new StdSudokuModel(new File(filename));
				} catch (IOException ioe) {
					System.out.println("fichier \"" + filename + "\" introuvable");
				}
				mainFrame.add(new Grid(model.getGridPlayer()), BorderLayout.WEST);
				mainFrame.add(new Grid(model.getGridSoluce()), BorderLayout.EAST);
				//GridModel grid = new StdGridModel(3,3);
				//grid.setValue(new Coord(1, 1), 1);
				//grid.removeCandidate(new Coord(1, 2), 5);
				//mainFrame.add(new Grid(grid), BorderLayout.CENTER);
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