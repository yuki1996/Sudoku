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
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.sun.media.sound.Toolkit;

public class Game4 implements MouseListener {
	
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
	private JButton pause;
	private JButton reset;
	private JButton resolve;
	private JButton solution;
	
	// CONSTRUCTEURS
	public Game4() {
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
    	
    	pause = new JButton("pause");
    	reset = new JButton("réinitialiser");
    	resolve = new JButton("résoudre");
    	solution = new JButton("solution");
	}
	
	private void placeComponents() {
		// barre de menu
		mainFrame.setJMenuBar(createJMenuBar());
		
		JPanel p = new JPanel(new GridLayout(1, 2)); {
			JPanel q = new JPanel(new GridLayout(3, 3)); {
				int k = 0;
				for (int i = 0 ; i < 9 ; ++i) {
					JPanel r = new JPanel(new GridLayout(3, 3)); {
						for (int j = 0 ; j < 9 ; ++j) {
							JPanel s = new JPanel(new GridLayout(3, 3)); {
								for (int l = 0; l < 9; ++l) {
									s.add(digitButton[k]);
									++k;
								}
							}
							r.add(s);
							s.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
						}
					}
					q.add(r);
					r.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
				}
			}
			q.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			p.add(q, BorderLayout.WEST);
			
			q = new JPanel(new GridLayout(3, 1)); {
				JPanel r = new JPanel(new GridLayout(2, 2)); {
					JPanel s = new JPanel(); {
						s.add(new JLabel("Progression : "));
					}
					r.add(s);
					
					s = new JPanel(); {
						s.add(new JLabel("00%"));
					}
					r.add(s);
					
					s = new JPanel(); {
						s.add(new JLabel("Temps : "));
					}
					r.add(s);
					
					s = new JPanel(); {
						s.add(new JLabel("00:00"));
					}
					r.add(s);
				}
				q.add(r);
				
				r = new JPanel(new GridLayout(2, 2)); {
					// Pause
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
				
				JTextArea text = new JTextArea("-- Création de grille --\n"); 
				text.append("-- Aide --\n");
				q.add(text);
				text.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			}
			p.add(q, BorderLayout.EAST);
		}
		mainFrame.add(p);
		
	}

	private void createController() {
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// barre de menu
		// Nouvelle grille
		newGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	new Game4().display();
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
        
        // gestion des possibilités
        for (int i = 0 ; i < 729 ; ++i) {
        	digitButton[i].addMouseListener(null);
        }
	}
	
	// gestion de la souris
	@Override
	public void mouseClicked(MouseEvent e) {
	
			System.out.println("appui sur le bouton.");
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
                new Game4().display();
            }
        });
	}
}
