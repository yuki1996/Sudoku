package sudoku.model.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import sudoku.model.StdCellModel;
import sudoku.model.CellModel;

public class Cell extends JPanel {
	
	// ATTRIBUTS
	private static final Color DEFAULT_COLOR = Color.WHITE;
	private static final Color HOVER_COLOR = Color.LIGHT_GRAY;
	
	private CellModel model;
	
	// imperativement autre chose que des JLabel
	// - probleme au niveau de la visibilité
	// - flexibilité d'affichage (JComponent ?)
	private JLabel value;
	private JLabel[] candidates;
	
	// CONSTRUCTEUR
	public Cell(CellModel cell) {
		createModel(cell);
		createView();
		placeComponents();
		createController();
	}
	
	// REQUETES
	public CellModel getModel() {
		return model;
	}
	
	// OUTILS
	private void createModel(CellModel cell) {
		model = cell;
	}
	
	private void createView() {
		value = new JLabel();
		value.setOpaque(true);
		value.setBackground(DEFAULT_COLOR);
		candidates = new JLabel[model.getCardinalCandidates()];
		for (int k = 0; k < candidates.length; ++k) {
			candidates[k] = new JLabel(Integer.toString(k + 1));
			candidates[k].setOpaque(true);
			candidates[k].setBackground(DEFAULT_COLOR);
		}
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}
	
	private void placeComponents() {
		this.setLayout(new CardLayout());
		JPanel p = new JPanel(new GridLayout(3, 3)); {
			for (JLabel c : candidates) {
				p.add(c);
			}
		}
		this.add(p);
		this.add(value);
	}
	
	private void createController() {
		value.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (model.isModifiable()) {
					switch (e.getButton()) {
						case MouseEvent.BUTTON1: // clique gauche
							model.removeValue();
							break;
						default:;
					}
				}
			}

		});
		
		for (int k = 0; k < candidates.length; ++k) {
			final int n = k;
			candidates[n].addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.println("clicked " + n);
					if (model.isModifiable()) {
						switch (e.getButton()) {
							case MouseEvent.BUTTON1: // clique gauche
								System.out.println("gauche");
								model.setValue(n + 1);
								break;
							case MouseEvent.BUTTON3: // clique droit
								System.out.println("droite");
								model.toggleCandidate(n + 1);
								break;
							default:;
						}
					}
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					candidates[n].setBackground(HOVER_COLOR);
				}

				@Override
				public void mouseExited(MouseEvent e) {
					candidates[n].setBackground(DEFAULT_COLOR);
				}
			});
			
		}
		
		model.addPropertyChangeListener(CellModel.VALUE, new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ((Integer) evt.getNewValue() != 0) {
					value.setText(String.valueOf(model.getValue()));
					swapToValue();
				} else {
					swapToCandidates();
				}
			}
		});
		
		model.addPropertyChangeListener(CellModel.CANDIDATE, new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				IndexedPropertyChangeEvent ievt =
						(IndexedPropertyChangeEvent) evt;
				int index = ievt.getIndex() - 1;
				System.out.println("recu " + index);
				candidates[index].setVisible(model.isCandidate(index)); // pas bon setVisible(false) ne permet pas de recliquer dessus
				candidates[index].repaint();
			}
		});
	}

	private void swapToValue() {
		((CardLayout)getLayout()).last(this);;
	}

	private void swapToCandidates() {
		((CardLayout)getLayout()).first(this);;
	}
	
	private void swap() {
		((CardLayout)getLayout()).next(this);
	}
	
	// TEST
	public static void main(String[] args) {
		class Bla {
			JFrame mainFrame = new JFrame();
			public Bla() {
				mainFrame.add(new Cell(new StdCellModel(9)), BorderLayout.CENTER);
				mainFrame.add(new Cell(new StdCellModel(9)), BorderLayout.NORTH);
				mainFrame.add(new Cell(new StdCellModel(9)), BorderLayout.SOUTH);
				mainFrame.add(new Cell(new StdCellModel(9)), BorderLayout.WEST);
				mainFrame.add(new Cell(new StdCellModel(9)), BorderLayout.EAST);
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