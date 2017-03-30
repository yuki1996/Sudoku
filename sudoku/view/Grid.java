package sudoku.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import sudoku.model.CellModel;
import sudoku.model.GridModel;
import sudoku.model.StdSudokuModel;
import sudoku.model.SudokuModel;
import sudoku.model.heuristic.Report;
import sudoku.model.heuristic.Report.CellSetName;
import sudoku.model.heuristic.RuleManager;
import sudoku.model.history.cmd.AddCandidate;
import sudoku.model.history.cmd.AddValue;
import sudoku.model.history.cmd.Command;
import sudoku.model.history.cmd.RemoveCandidate;
import sudoku.model.history.cmd.RemoveValue;
import sudoku.util.Coord;
import sudoku.util.ICoord;

public class Grid extends JPanel {
    
    // ATTRIBUTS
    public final int BORDER_SIZE = 1;
    
    private final int SIZE = 600;
    
    private SudokuModel model;
    
    private Cell[][] cells;
    
    // CONSTRUCTEUR
    public Grid(SudokuModel model) {
        createModel(model);
        createView();
        placeComponents();
        createController();
    }
    
    // REQUETES
    public SudokuModel getModel() {
        return model;
    }
    
    // COMMANDES
    public void setModel(SudokuModel model) {
        this.model = model;
        GridModel grid = model.getGridPlayer();
        if (grid.size() == cells.length) {
            for (int k = 0; k < grid.size(); ++k) {
                for (int j = 0; j < grid.size(); ++j) {
                    cells[k][j].setModel(grid.getCell(new Coord(k, j)));
                }
            }
            
            this.model.addPropertyChangeListener(RuleManager.LAST_REPORT,
            		new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					boolean paint = evt.getOldValue() == null;
					highlightCells(((Report) (paint ? evt.getNewValue()
						: evt.getOldValue())).importantSets(), paint);
				}
            });
        } else {
        	this.removeAll();
            createView();
            placeComponents();
            createController();
            this.revalidate();
        }
    }
    
    // OUTILS
    private void createModel(SudokuModel model) {
        this.model = model;
    }
    
    private void createView() {
        GridModel grid = model.getGridPlayer();
        this.setPreferredSize(new Dimension(SIZE, SIZE));
        cells = new Cell[grid.size()][grid.size()];
        for (int k = 0; k < grid.size(); ++k) {
            for (int j = 0; j < grid.size(); ++j) {
                cells[k][j] = new Cell(grid.cells()[k][j]);
            }
        }
    }
    
    private void placeComponents() {
        GridModel grid = model.getGridPlayer();
        this.setLayout(new BorderLayout()); {    // layout à changer
            JPanel p = new JPanel(new GridLayout(grid.getNumberSectorByHeight(),
                    grid.getNumberSectorByWidth())); {
                for (int k = 0; k < grid.numberCandidates(); ++k) {
                    JPanel q = new JPanel(new GridLayout(grid.getHeightSector(),
                            grid.getWidthSector())); {
                        for (int j = 0; j < grid.numberCandidates(); ++j) {
                            int row = grid.getHeightSector()
                            		* (k / grid.getNumberSectorByWidth())
                            		+ j / grid.getWidthSector();
                            int col = grid.getWidthSector()
                            		* (k % grid.getNumberSectorByWidth())
                            		+ j % grid.getWidthSector();
                            q.add(cells[row][col]);
                        }
                    }
                    q.setBorder(BorderFactory.createLineBorder(Color.BLUE,
                            BORDER_SIZE));
                    p.add(q);
                }
            }
            p.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
            this.add(p, BorderLayout.CENTER);
        }
    }
    
    private void createController() {
        PropertyChangeListener cellListener = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                CellModel c = ((Cell) evt.getSource()).getModel();
                GridModel g = model.getGridPlayer();
                Command cmd = null;
                String propertyName = evt.getPropertyName();
                int newValue = (Integer) evt.getNewValue();
                if (propertyName.equals(CellModel.VALUE)) {
                    if (newValue == 0) {
                        cmd = new RemoveValue(g, c);
                    } else {
                        cmd = new AddValue(g, c, newValue);
                    }
                } else if (propertyName.equals(CellModel.CANDIDATE)) {
                    if (c.isCandidate(newValue)) {
                        cmd = new RemoveCandidate(g, c, newValue);
                    } else {
                        cmd = new AddCandidate(g, c, newValue);
                    }
                }
                model.act(cmd);
            }
            
        };
        
        for (Cell[] ctab : cells) {
            for (Cell c : ctab) {
                c.addPropertyChangeListener(CellModel.CANDIDATE, cellListener);
                c.addPropertyChangeListener(CellModel.VALUE, cellListener);
            }
        }
        
        model.addPropertyChangeListener(SudokuModel.GRID,
        		new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						setModel(model);
					}
        	
        });
        
        model.addPropertyChangeListener(RuleManager.LAST_REPORT,
        		new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				boolean paint = evt.getOldValue() == null;
				highlightCells(((Report) (paint ? evt.getNewValue()
					: evt.getOldValue())).importantSets(), paint);
			}
        	
        });
    }
    
    private void highlightCells(Map<CellSetName, Set<ICoord>> cells,
    		boolean paint) {
    	
    	disjoint(cells, CellSetName.DECISIVE_UNITS,
    			CellSetName.DECISIVE_CELLS);
    	disjoint(cells, CellSetName.DELETION_UNITS,
    			CellSetName.DELETION_CELLS);

    	disjoint(cells, CellSetName.DELETION_CELLS,
    			CellSetName.DECISIVE_CELLS);

    	disjoint(cells, CellSetName.DELETION_UNITS,
    			CellSetName.DECISIVE_CELLS);
    	
    	disjoint(cells, CellSetName.DELETION_CELLS,
    			CellSetName.DECISIVE_UNITS);

    	disjoint(cells, CellSetName.DELETION_UNITS,
    			CellSetName.DECISIVE_UNITS);
    	
    	for (Entry<CellSetName, Set<ICoord>> e : cells.entrySet()) {
    		Set<ICoord> set = e.getValue();
    		Color c = paint ? e.getKey().getColor() : Cell.DEFAULT_BACKGROUND;
    		for (ICoord coord : set) {
    			this.cells[coord.getRow()][coord.getCol()].setBackground(c);
    		}
    	}
    }
    
    private void disjoint(Map<CellSetName, Set<ICoord>> cells,
    		CellSetName bigger, CellSetName smaller) {

    	Set<ICoord> unit = cells.get(bigger);
    	unit.removeAll(cells.get(smaller));
    	cells.put(bigger, unit);
    }
    
    // TEST
    public static void main(String[] args) {
        class Bla {
            private static final String filename = "grille2.txt";
            JFrame mainFrame = new JFrame(filename);
            SudokuModel model;
            public Bla() {
                try {
                    model = new StdSudokuModel(new File(filename));
                } catch (IOException ioe) {
                    System.out.println("fichier \"" + filename + "\" introuvable");
                }
                mainFrame.add(new Grid(model), BorderLayout.CENTER);
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