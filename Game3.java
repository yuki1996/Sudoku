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

import com.sun.media.sound.Toolkit;

public class Game3 {
	
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
	
	private JButton[] digitButton;
	
	// CONSTRUCTEURS
	public Game3() {
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
//		final int frameWidth = 800;
//        final int frameHeight = 600;
         
        mainFrame = new JFrame("Game");
//        mainFrame.setPreferredSize(new Dimension(frameWidth, frameHeight));
        
        mainFrame.setDefaultLookAndFeelDecorated(true);
        mainFrame.setExtendedState(mainFrame.MAXIMIZED_BOTH);
        
    	//timer = new Timer();
    	
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
    	
    	digitButton = new JButton[729];
    	int k = 1;
    	for (int i = 0 ; i < 729 ; ++i) {
    		digitButton[i] = new JButton("" + k);
    		digitButton[i].setBorder(BorderFactory.createLineBorder(Color.WHITE));
    		digitButton[i].setBackground(Color.WHITE);
    		if (k == 9) { 
    			k = 0;
    		}
    		++k;
    	}
	}
	
	private void placeComponents() {
		// barre de menu
		mainFrame.setJMenuBar(createJMenuBar());
		
		JPanel p = new JPanel(new GridLayout(3, 3)); {
			int k = 0;
			for (int i = 0 ; i < 9 ; ++i) {
				JPanel q = new JPanel(new GridLayout(3, 3)); {
					for (int j = 0 ; j < 9 ; ++j) {
						JPanel r = new JPanel(new GridLayout(3, 3)); {
							for (int l = 0; l < 9; ++l) {
								r.add(digitButton[k]);
								++k;
							}
						}
						q.add(r);
						r.setBorder(BorderFactory.createLineBorder(Color.GRAY));
					}
				}
				p.add(q);
				q.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			}
		}
		p.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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
		
		// résoudre pas à pas 
        resolveMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO
            }
        });
        
        // solution finale
        solutionMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO
            }
        });
        
        // refait l'action
        doMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO
            }
        });
        
        // défait l'action
        undoMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO
            }
        });
        
        // tutoriel
        tuto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	new Tutorial().display();
            }
        });
        
        // guide comment utiliser cette application
        userGuide.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO
            	new Guide().display();
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
	
	// LANCEUR
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Game3().display();
            }
        });
	}
}
