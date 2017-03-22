package sudoku.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
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

import sudoku.model.CellModel;
import sudoku.model.StdCellModel;

// A SUPPRIMER
public class Cell extends JPanel {
	
	// ATTRIBUTS
	private static final Color DEFAULT_COLOR = Color.WHITE;
	// le dernier parametre est pour la transparence
	private static final Color HOVER_COLOR = new Color(128, 128, 128, 128);
	
	private CellModel model;
	
	private JPanel[] cards;
	private JPanel[] candidates;
	
	private JLabel[] displayables;
	private JLabel[] candidateDisplayables;
	
	private CardLayout cardLayout;
	
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
		displayables = new JLabel[9];
		candidateDisplayables = new JLabel[9];
		for (int k = 0; k < displayables.length; ++k) {
			displayables[k] = new JLabel(String.valueOf(k + 1));
			displayables[k].setFont(new Font("Verdana", Font.BOLD, 30));
			candidateDisplayables[k] = new JLabel(String.valueOf(k + 1));
			candidateDisplayables[k].setFont(new Font("Verdana", Font.BOLD, 10));
		}
		
		cards = new JPanel[model.getCardinalCandidates() + 1];
		for (int k = 0; k < cards.length; ++k) {
			cards[k] = new JPanel();
			cards[k].setBackground(DEFAULT_COLOR);
		}
		candidates = new JPanel[model.getCardinalCandidates()];
		for (int k = 0; k < candidates.length; ++k) {
			candidates[k] = new JPanel();
			candidates[k].setBackground(DEFAULT_COLOR);
		}
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		cardLayout = new CardLayout();
	}
	
	private void placeComponents() {
		this.setLayout(cardLayout);
		cards[0].setLayout(new GridLayout(3, 3)); {
			for (int k = 0; k < candidates.length; ++k) {
				candidates[k].setLayout(new GridBagLayout()); {
					candidates[k].add(candidateDisplayables[k]);
					candidateDisplayables[k].setVisible(model.isCandidate(k + 1));
				}
				cards[0].add(candidates[k]);
			}
		}
		this.add(cards[0], "0");
		for (int k = 1; k < cards.length; ++k) {
			cards[k].setLayout(new GridBagLayout()); {
				cards[k].add(displayables[k-1]);
			}
			this.add(cards[k], displayables[k-1].getText());
		}
		cardLayout.show(this, String.valueOf(model.getValue()));
	}
	
	private void createController() {

		for (int k = 1; k < cards.length; ++k) {
			cards[k].addMouseListener(new MouseAdapter() {
	
				@Override
				public void mouseClicked(MouseEvent e) {
					if (model.isModifiable()) {
						if (SwingUtilities.isLeftMouseButton(e)) {
							model.removeValue();
						}
					}
				}
	
			});
		}
		
		for (int k = 0; k < candidates.length; ++k) {
			final int n = k + 1;
			candidates[k].addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.println("clicked " + n);
					if (model.isModifiable()) {
						if (SwingUtilities.isLeftMouseButton(e)
						&& model.isCandidate(n)) {
							System.out.println("gauche");
							model.setValue(n);
						} else if (SwingUtilities.isRightMouseButton(e)) {
							System.out.println("droite");
							model.toggleCandidate(n);
						}
					}
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					candidates[n - 1].setBackground(HOVER_COLOR);
				}

				@Override
				public void mouseExited(MouseEvent e) {
					candidates[n - 1].setBackground(DEFAULT_COLOR);
				}
			});
			
		}
		
		model.addPropertyChangeListener(CellModel.VALUE, new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				cardLayout.show(Cell.this, String.valueOf(evt.getNewValue()));
			}
		});
		
		model.addPropertyChangeListener(CellModel.CANDIDATE, new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				IndexedPropertyChangeEvent ievt =
						(IndexedPropertyChangeEvent) evt;
				int index = ievt.getIndex() - 1;
				System.out.println("recu " + index);
				candidateDisplayables[index].setVisible(model.isCandidate(index + 1));
				candidates[index].repaint();
			}
		});
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