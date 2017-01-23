package sudoku;

import java.awt.BorderLayout;
import java.awt.Color;
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
    	open = new JMenuItem("Ouvrir");;
    	save = new JMenuItem("Sauvegarder");;
    	resetMenu = new JMenuItem("Réinitialiser");;
    	tuto = new JMenuItem("Tutoriel");;
	}
	
	private void placeComponents() {		
		mainFrame.setJMenuBar(createJMenuBar());
		
		// JPanel contenant la grille de sudoku
		JPanel p = new JPanel(new GridLayout(NUMBER_VALUES, NUMBER_VALUES)); {
			for (int i = 0; i < NUMBER_VALUES; ++i) {
	  			for (int j = 0; j < NUMBER_VALUES; ++j) {
	  				JPanel r = new JPanel(); {
	  					r.add(grid[i][j]);
	  					r.setBorder(BorderFactory.createLineBorder(Color.black));
	  					r.setMaximumSize(new Dimension(1, 1));
	  				}
	  				p.add(r);
	  			}
	  		}
		}
        mainFrame.add(p);
        
        p = new JPanel(new GridLayout(4, 2)); {
        	JPanel q = new JPanel(); {
        		q.add(reset);
        	}
        	p.add(q);
        	
        	q = new JPanel(); {
        		q.add(resolve);
        	}
        	p.add(q);
        	
        	// timer
        	q = new JPanel(); {
        		q.add(new JLabel("Temps :"));
        	}
        	p.add(q);
        	
        	q = new JPanel(); {
            	// TODO
            	// timer à rajouter       	
            	q.add(new JLabel("00:00"));
        	}
        	p.add(q);
        	
        	// progression
        	q = new JPanel(); {
            	q.add(new JLabel("progression : "));
        	}
        	p.add(q);
        	
        	q = new JPanel(); {
            	q.add(new JLabel("" + progression + "%"));
        	}
        	p.add(q);
        	
        	// boutons
        	q = new JPanel(new GridLayout(2, 1)); {
        		JPanel r = new JPanel(); {
        			r.add(switchCandidates);
        		}
        		q.add(r);
        		
        		r = new JPanel(); {
        			r.add(help);
        		}
        		q.add(r);
        	}	
        	p.add(q);
        	
        	q = new JPanel(); {
            	JPanel r = new JPanel(new GridLayout(NUMBER_VALUES / 3, NUMBER_VALUES / 3)); {
            		for (int k = 0; k < NUMBER_VALUES; ++k) {
              			r.add(digitButton[k]);
              		}
            	}
            	q.add(r);
        	}
        	p.add(q);
        }
        mainFrame.add(p, BorderLayout.EAST);
        
        
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
				} else {
					switchCandidates.setText("candidats");
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
