package sudoku;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
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

public class Game {
	
	// CONSTANTES
	// hauteur et largeur de la grille
	public final static int HEIGHT_GRID = 3;
	public final static int WIDTH_GRID = 3;
	// hauteur et largeur des régions
	public final static int HEIGHT_SECTOR = 3;
	public final static int WIDTH_SECTOR = 3;
	// hauteur et largeur des cellules
	public final static int HEIGHT_CELL = 3;
	public final static int WIDTH_CELL = 3;
	
	public final static int NUMBER_POSSIBILITIES = 9;
	
	// ATTRIBUTS
	private JFrame mainFrame;
	//private Model model;
	
	//private Timer timer;
	private int progression;
	
	private JButton pause;
	private JButton solution;
	private JButton reset;
	private JButton resolve;
	private JTextArea text;
	
	private JMenuItem exit;
	private JMenuItem newGame;
	private JMenuItem open;
	private JMenuItem save;
	private JMenuItem resetMenu;
	private JMenuItem tuto;
	private JMenuItem resolveMenu;
	private JMenuItem solutionMenu;
	private JMenuItem userGuide;
	private JMenuItem undoMenu;
	private JMenuItem doMenu;	
	
	private JPanel[][] grid;
	private JPanel[] sector;
	private JButton[] digitButton;
	
	// CONSTRUCTEURS
	public Game() {
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
		final int frameWidth = 800;
        final int frameHeight = 600;
         
        mainFrame = new JFrame("Game");
        mainFrame.setPreferredSize(new Dimension(frameWidth, frameHeight));
        
    	//timer = new Timer();
    	progression = 0;
    	
    	pause = new JButton("pause");
    	solution = new JButton("solution");
    	reset = new JButton("réinitialiser");
    	resolve = new JButton("résoudre pas à pas");
    	
    	text = new JTextArea("Aide :");
    	text.setBorder(BorderFactory.createLineBorder(Color.black));
    	
    	exit = new JMenuItem("Quitter");
    	newGame = new JMenuItem("Nouveau");
    	open = new JMenuItem("Ouvrir");
    	save = new JMenuItem("Sauvegarder");
    	resetMenu = new JMenuItem("Réinitialiser");
    	tuto = new JMenuItem("Tutoriel");
    	resolveMenu = new JMenuItem("Résoudre pas à pas");
    	solutionMenu = new JMenuItem("Solution");
    	userGuide = new JMenuItem("Comment jouer");
    	undoMenu = new JMenuItem("Annuler l'action");
    	doMenu = new JMenuItem("Refaire l'action");
    	
    	grid = new JPanel[3][3];
    	sector = new JPanel[729];
    	digitButton = new JButton[729];
    	
    	initGrid();
		initDigitButton();
		makeSector();
		makeGrid();
  
	}
	
	private void placeComponents() {
		// barre de menu
		mainFrame.setJMenuBar(createJMenuBar());
		
		JPanel p = new JPanel(new GridLayout(1, 2)); {
			// grille de sudoku
			JPanel q = new JPanel(new GridLayout(3, 3)); {
				
				for (int i = 0 ; i < 3 ; ++i) {
					for (int j = 0 ; j < 3 ; ++j) {
						q.add(grid[i][j]);
					}
				}
			}
			p.add(q);
			
			// boutons et aide
			q = new JPanel(new GridLayout(3, 1)); {					
				JPanel r = new JPanel(new GridLayout(2, 2)); { 
					// pause
					JPanel s = new JPanel(); {
						s.add(pause);
					}
					r.add(s);
					
					// remise à zéro
					s = new JPanel(); {
						s.add(reset);
					}
					r.add(s);
					
					// résoudre la cellule
					s = new JPanel(); {
						s.add(resolve);
					}
					r.add(s);
					
					// donner la solution complète
					s = new JPanel(); {
						s.add(solution);
					}			
					r.add(s);
				}
				q.add(r);
				
				// progression
				r = new JPanel(new GridLayout(2, 2)); {
						r.add(new JLabel("Progression : "));
						r.add(new JLabel("00%"));
						r.add(new JLabel("Temps : "));
						r.add(new JLabel("00:00"));
				}
				q.add(r);
				
				// aide
				q.add(new JScrollPane(text));
			}
			p.add(q);
		}
		
		mainFrame.add(p);
	}

	private void createController() {
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// barre de menu
		// Nouvelle grille
		newGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO
            }
        });
		
		// ouvrir un fichier 
        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//TODO
            }
        });
        
        // sauvegarder
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO
            }
        });
		
        // quitter
		exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
		
		resetMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO
            }
        });
		
        resolveMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO
            }
        });
        
        solutionMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO
            }
        });
        
        doMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO
            }
        });
        
        undoMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO
            }
        });
        
        tuto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO
            }
        });
        
        userGuide.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO
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
                m.add(exit);
            }
            bar.add(m);
            
            m = new JMenu("Édition"); {
                m.add(resetMenu);
                m.add(resolveMenu);
                m.add(solutionMenu);
                m.add(doMenu);
                m.add(undoMenu);
            }
            bar.add(m);
            
            m = new JMenu("Aide"); {
                m.add(tuto);
                m.add(userGuide);
            }
            bar.add(m);
        }
        return bar;
    }
  
	private void initGrid() {
		for (int i = 0 ; i < 3 ; ++i) {
			for (int j = 0 ; j < 3 ; ++j) {
				grid[i][j] = new JPanel(new GridLayout(9, 9));
				grid[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
			}
		}
		
		for (int i = 0 ; i < 729 ; ++i) {
			sector[i] = new JPanel();
			sector[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}
	}
	
	/**
	 * Initialise les boutons.
	 */
	private void initDigitButton() {
		int k = 1;
		for (int l = 0 ; l < 729 ; ++l) {
			digitButton[l] = new JButton("" + k);
			if (k == 9) {
				k = 0; 
			}
			++k ;
		}
	}
	
	/**
	 * Construction des secteurs.
	 */
	private void makeSector() {
		int k = 0;
		for (int i = 0 ; i < 729 ; ++i) {
  				sector[i].add(digitButton[k]);
  				++k;			
		}
	}
	
  	/**
  	 * Construction de la grille de sudoku.
  	 * 
  	 */
  	private void makeGrid() {
  		int l = 0;
  		for (int i = 0 ; i < 3; ++i) {
			for (int j = 0 ; j < 3; ++j) {
				for (int k = 0 ; k < 81; ++k) {
					grid[i][j].add(sector[l]);
					++l;
				}
				// TODO 
				// Rajouter des jpanel pour réorganiser les boutons
			}
  		}
  	}
	
	// LANCEUR
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Game().display();
            }
        });
	}
}
