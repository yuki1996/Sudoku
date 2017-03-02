package sudoku;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Sudoku {
	
	// CONSTANTES
	public final static int NUMBER_VALUES = 9;
	public final static int NUMBER_POSSIBILITIES = 9;
	
	// ATTRIBUTS
	private JFrame mainFrame;
	//private Model model;
	
	private JLabel[][] grid;
	private Timer timer;
	private int progression;
	
	private JButton[] digitButton;
	private JButton help;
	private JButton reset;
	private JButton resolve;
	private JButton switchCandidates;
	private JTextArea text;
	
	private JMenuItem newGame;
	private JMenuItem open;
	private JMenuItem save;
	private JMenuItem resetMenu;
	private JMenuItem tuto;
	private JMenuItem resolveMenu;
	private JMenuItem switchMenu;
	
	// CONSTRUCTEURS
	public Sudoku() {
		createModel();
		createView();
		placeComponents();
		createController();
	}
	
	// COMMANDES
	public void display() {
    	refresh();
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }
	
	// OUTILS
	private void refresh() {
		// rien pour l'instant
	}
	
	public void createModel() {
        // model = new Model();
    }
  
	private void createView() {
		final int frameWidth = 600;
        final int frameHeight = 400;
         
        mainFrame = new JFrame("Sudoku");
        mainFrame.setPreferredSize(new Dimension(frameWidth, frameHeight));
        
        grid = new JLabel[NUMBER_VALUES][NUMBER_VALUES];
        initGrid();
        
    	timer = new Timer();
    	progression = 0;
    	
    	digitButton = new JButton[NUMBER_VALUES];
    	initDigitButton();
    	
    	help = new JButton("aide");
    	reset = new JButton("réinitialiser");
    	resolve = new JButton("résoudre");
    	switchCandidates = new JButton("candidats");
    	
    	text = new JTextArea("Ici se trouvera l'aide.");
    	text.setBorder(BorderFactory.createLineBorder(Color.black));
    	text.setSize(500, 50);
    	
    	newGame = new JMenuItem("Nouveau");
    	open = new JMenuItem("Ouvrir");
    	save = new JMenuItem("Sauvegarder");
    	resetMenu = new JMenuItem("Réinitialiser");
    	tuto = new JMenuItem("Tutoriel");
    	resolveMenu = new JMenuItem("Résoudre");
    	switchMenu = new JMenuItem("Possibilités");
	}
	
	private void placeComponents() {
		// barre de menu
		mainFrame.setJMenuBar(createJMenuBar());
		
		// JPanel contenant la grille de sudoku
		int n = NUMBER_VALUES / 3;
		JPanel p = new JPanel(new GridLayout(n, n)); {
			JPanel[][] q = new JPanel[n][n]; {
				// disposition des régions
				for (int i = 0; i < n; ++i) {
					for (int j = 0; j < n; ++j) {
						q[i][j] = new JPanel(new GridLayout(n, n)); {
							// disposition des cellules
							for (int k = 0; k < n; ++k) {
					  			for (int l = 0; l < n; ++l) {
					  				JPanel r = new JPanel(); {
					  					r.add(grid[k][l]);
					  					r.setBorder(BorderFactory.createLineBorder(Color.GRAY));
					  				}
					  				q[i][j].add(r);
					  				q[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
					  			}
							}
							p.add(q[i][j]);
						}
					}
				}
				
			}
		}
        mainFrame.add(p);
        
        // Partie Est, les différentes actions possibles
        p = new JPanel(new GridLayout(4, 2)); {
       		// remise à zéro
        	p.add(reset);
        	// résoudre
       		p.add(resolve);
        	
        	// timer
    		p.add(new JLabel("Temps :"));
    		// TODO
        	// timer à rajouter       	
        	p.add(new JLabel("00:00"));
        	p.add(new JLabel("progression : "));
    	 	p.add(new JLabel("" + progression + "%"));
  
        	
        	// boutons
   			p.add(switchCandidates);
        	
        	JPanel q = new JPanel(new GridLayout(NUMBER_VALUES / 3, NUMBER_VALUES / 3)); {
        		for (int k = 0; k < NUMBER_VALUES; ++k) {
              		q.add(digitButton[k]);
           		}            	
        	}
        	p.add(q);
        }
        mainFrame.add(p, BorderLayout.EAST);
        
        // zone de texte pour l'aide, au Sud
        JScrollPane scroll = new JScrollPane(text);
        
        mainFrame.add(scroll, BorderLayout.SOUTH);
	}
	
	private void createController() {
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		switchCandidates.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// pour l'exemple :
				if (switchCandidates.getText().compareTo("candidats") == 0) {
					switchCandidates.setText("possibilités");
					switchMenu.setText("Candidats");
				} else {
					switchCandidates.setText("candidats");
					switchMenu.setText("Possibilités");
				}
			}
		});
		
		switchMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if (switchMenu.getText().compareTo("Candidats") == 0) {
					switchCandidates.setText("candidats");
					switchMenu.setText("possibilités");
				} else {
					switchCandidates.setText("possibilités");
					switchMenu.setText("Candidats");
				}
            }
        });
	}
  
	/**
	 * Retourne une JMenuBar.
	 */
  private JMenuBar createJMenuBar() {
        JMenuBar bar = new JMenuBar(); {
            JMenu m = new JMenu("Fichier"); {
                m.add(newGame);
                m.add(open);
                m.add(save);
                m.addSeparator();
                JMenuItem n = new JMenuItem("Quitter"); {
                    n.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            System.exit(0);
                        }
                    });
                }
                m.add(n);
            }
            bar.add(m);
            
            m = new JMenu("Édition"); {
                m.add(resetMenu);
                m.add(resolveMenu);
                m.add(switchMenu);
            }
            bar.add(m);
            
            m = new JMenu("Aide"); {
                m.add(tuto);
            }
            bar.add(m);
        }
        return bar;
    }
  
  	/**
  	 * Initialise le tableau de JButton des valeurs de 1 à NUMBER_VALUES.
  	 */
  	private void initDigitButton() {
  		for (int k = 0; k < NUMBER_VALUES; ++k) {
  			digitButton[k] = new JButton("" + k);
  		}
  	}
  	/**
  	 * Initialise le tableau de JPanel des valeurs de 1 à NUMBER_VALUES.
  	 */
  	private void initGrid() {
  		for (int i = 0; i < NUMBER_VALUES; ++i) {
  			for (int j = 0; j < NUMBER_VALUES; ++j) {
  				grid[i][j] = new JLabel("");
  			}
  		}
  	}
	
	// LANCEUR
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Sudoku().display();
            }
        });
	}
}
