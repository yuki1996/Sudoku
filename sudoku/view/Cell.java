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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import sudoku.model.CellModel;
import sudoku.model.StdCellModel;

// A SUPPRIMER
@SuppressWarnings("serial")
public class Cell extends JPanel {
    
    // ATTRIBUTS
    private static final Color DEFAULT_BACKGROUND = Color.WHITE;
    private static final Color HOVER_BACKGROUND = new Color(100, 100, 100, 80);
    private static final Color MODIF_COLOR = Color.BLUE;
    private static final Color NMODIF_COLOR = Color.BLACK;
    
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
    
    // COMMANDES
    public void setModel(CellModel cell) {
        model.giveListeners(CellModel.VALUE, cell);
        model.giveListeners(CellModel.CANDIDATE, cell);
        
        model = cell;
        
        refresh();
    }
    
    // OUTILS
    private void refresh() {
        for (int k = 0; k < candidateDisplayables.length; ++k) {
            candidateDisplayables[k].setVisible(model.isCandidate(k + 1));
        }
        cardLayout.show(this, String.valueOf(model.getValue()));
    }
    
    private void createModel(CellModel cell) {
        model = cell;
    }
    
    private void createView() {
        int nbValues = model.getCardinalCandidates();
        displayables = new JLabel[nbValues];
        candidateDisplayables = new JLabel[nbValues];
        Font font = new Font("Verdana", Font.BOLD, 30);
        for (int k = 0; k < displayables.length; ++k) {
            displayables[k] = new JLabel(String.valueOf(k + 1),
                    SwingConstants.CENTER);
            displayables[k].setFont(font);
            displayables[k].setForeground(model.isModifiable()
                                    ? MODIF_COLOR : NMODIF_COLOR);
            candidateDisplayables[k] = new JLabel(String.valueOf(k + 1),
                    SwingConstants.CENTER);
            candidateDisplayables[k].setFont(font.deriveFont(10.0f));
            candidateDisplayables[k].setForeground(model.isModifiable()
                                    ? MODIF_COLOR : NMODIF_COLOR);
        }
        
        cards = new JPanel[model.getCardinalCandidates() + 1];
        for (int k = 0; k < cards.length; ++k) {
            cards[k] = new JPanel();
            cards[k].setBackground(HOVER_BACKGROUND);
            cards[k].setOpaque(false);
        }
        candidates = new JPanel[model.getCardinalCandidates()];
        for (int k = 0; k < candidates.length; ++k) {
            candidates[k] = new JPanel();
            candidates[k].setBackground(HOVER_BACKGROUND);
            candidates[k].setOpaque(false);
        }
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setBackground(DEFAULT_BACKGROUND);
        cardLayout = new CardLayout();
    }
    
    private void placeComponents() {
        int squareSideLength = upperSqrt(model.getCardinalCandidates());
        
        this.setLayout(cardLayout);
        cards[0].setLayout(new GridLayout(squareSideLength,
                                          squareSideLength)); {
            for (int k = 0; k < candidates.length; ++k) {
                candidates[k].setLayout(new GridLayout(1, 1)); {
                    candidates[k].add(candidateDisplayables[k]);
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
        refresh();
    }
    
    private void createController() {
        // vers le modèle
        for (int k = 1; k < cards.length; ++k) {
            final int n = k;
            cards[k].addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (model.isModifiable()) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            Cell.this.firePropertyChange(CellModel.VALUE,
                                    model.getValue(), 0);
                        }
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    cards[n].setOpaque(true);
                    cards[n].repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    cards[n].setOpaque(false);
                    cards[n].repaint();
                }
            });
        }
        
        for (int k = 0; k < candidates.length; ++k) {
            final int n = k + 1;
            candidates[k].addMouseListener(new MouseAdapter() {
                
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (model.isModifiable()) {
                        if (SwingUtilities.isLeftMouseButton(e)
                        && model.isCandidate(n)) {
                            Cell.this.firePropertyChange(CellModel.VALUE,
                                    model.getValue(), n);
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            Cell.this.firePropertyChange(CellModel.CANDIDATE,
                                    0, n);
                        }
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    candidates[n - 1].setOpaque(true);
                    candidates[n - 1].repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    candidates[n - 1].setOpaque(false);
                    candidates[n - 1].repaint();
                }
            });
            
        }
        
        // reçu du modèle
        model.addPropertyChangeListener(CellModel.VALUE,
                new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                cardLayout.show(Cell.this, String.valueOf(evt.getNewValue()));
            }
        });
        
        model.addPropertyChangeListener(CellModel.CANDIDATE,
                new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                IndexedPropertyChangeEvent ievt =
                        (IndexedPropertyChangeEvent) evt;
                int index = ievt.getIndex() - 1;
                candidateDisplayables[index].setVisible(
                        model.isCandidate(index + 1));
                candidates[index].repaint();
            }
        });
    }
    
    /**
     * Calcule la partie entière supérieure de la racine carrée de n.
     * @pre <pre>
     *         n >= 0
     * </pre>
     */
    private int upperSqrt(int n) {
        int res = 0;
        while (res * res < n) {
            ++res;
        }
        return res;
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