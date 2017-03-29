package sudoku;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import sudoku.model.GridModel;
import sudoku.model.StdGridModel;

//import com.sun.media.sound.Toolkit;

public class trucASUpprimer implements MouseListener {
	
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
	public final int[] VALUES = {1,2,3,4,5,6,7,8,9};
	
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
	
	private BeanGrid grid;
	private GridModel gridModel;
	
	//private long startTime;
	//private JLabel currentTime;
	
	// CONSTRUCTEURS
	public trucASUpprimer() {
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
		// ceci est un exemple :
		grid = new BeanGrid(HEIGHT_GRID, WIDTH_GRID, 
				HEIGHT_CELL, WIDTH_CELL, VALUES);
		gridModel = new StdGridModel(WIDTH_SECTOR, HEIGHT_SECTOR);		
    }
  
	private void createView() {
		final int frameWidth = 1000;
        final int frameHeight = 600;
         
        mainFrame = new JFrame("Game");
        mainFrame.setPreferredSize(new Dimension(frameWidth, frameHeight));
        
        //mainFrame.setDefaultLookAndFeelDecorated(true);
        //mainFrame.setExtendedState(mainFrame.MAXIMIZED_BOTH);
        
    	/*timer = new Timer();*/
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
    	
    	pause = new JButton("démarrer");
    	reset = new JButton("réinitialiser");
    	resolve = new JButton("résoudre");
    	solution = new JButton("solution");
    	
    	digitButton = grid.getDigitButton();
 
    	//startTime = System.currentTimeMillis();
    	//currentTime = new JLabel("" + (System.currentTimeMillis() - startTime));
	}
	
	private void placeComponents() {
		// barre de menu
		mainFrame.setJMenuBar(createJMenuBar());
		
		JPanel p = new JPanel(new GridLayout(1, 2)); {
			JPanel q = grid.makeGrid();
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
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
		
		// barre de menu
		// Nouvelle grille
		newGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	new GameV1().display();
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
            	int exit = JOptionPane.showConfirmDialog(null,
  		        		"Êtes-vous sûr de vouloir quitter?\n" +
  		        		"Toutes les données non-sauvegardées seront perdues.",
  		        		"Quitter?", JOptionPane.YES_OPTION);
  		        if (exit == JOptionPane.YES_OPTION) {
  		        	System.exit(1);
  		        }
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
        int n = (HEIGHT_GRID * WIDTH_GRID) * HEIGHT_CELL ;
    	n *= n;
        for (int i = 0 ; i < n ; ++i) {
        	digitButton[i].addMouseListener(null);
        }
        
        // vérification avant de quitter définitivement le programme
        mainFrame.addWindowListener(new WindowAdapter() {
  		  public void windowClosing(WindowEvent e) {
  		        int exit = JOptionPane.showConfirmDialog(null,
  		        		"Êtes-vous sûr de vouloir quitter?\n" +
  		        		"Toutes les données non-sauvegardées seront perdues.",
  		        		"Quitter?", JOptionPane.YES_OPTION);
  		        if (exit == JOptionPane.YES_OPTION) {
  		        	System.exit(1);
  		        }
  		  }
        });
        
        pause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               if (pause.getText().compareTo("pause") == 0) {
            	   pause.setText("démarrer");
            	   //currentTime.setText("" + (System.currentTimeMillis() - startTime));
               } else {
            	   pause.setText("pause");
               }
            }
        });
        
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //startTime = System.currentTimeMillis();
                //currentTime.setText("" + (System.currentTimeMillis() - startTime));
                pause.setText("démarrer");
             }
         });
        
        // exemple qui fonctionne
//        digitButton[0].addMouseListener(new MouseAdapter(){
//        	public void mousePressed(MouseEvent mouseEvent) { 
//                // clic gauche
//        		if (SwingUtilities.isLeftMouseButton(mouseEvent)) {  
//        			digitButton[0].setBackground(Color.RED);
//                } 
//        		
//        		// clic droit
//        		if (SwingUtilities.isRightMouseButton(mouseEvent)) { 
//        			digitButton[0].setBackground(Color.GREEN);     
//                }  
//        	}
//		});
        
        for (int k = 0 ; k < n ; ++k) {
	        digitButton[k].addMouseListener(new MouseAdapter(){
	        	public void mousePressed(MouseEvent mouseEvent) { 
	        		JButton button = (JButton) mouseEvent.getSource();
	        		int i = Integer.parseInt(button.getName());
	        		
	                // clic gauche
	        		if (SwingUtilities.isLeftMouseButton(mouseEvent)) {  
	        			
	        			digitButton[i].setBackground(Color.BLUE);
	        			digitButton[i].setForeground(Color.WHITE);
	                } 
	        		
	        		// clic droit
	        		if (SwingUtilities.isRightMouseButton(mouseEvent)) { 
	        			digitButton[i].setBackground(Color.BLACK); 
	        			digitButton[i].setForeground(Color.WHITE);
	                }  
	        	}
			});
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
                new trucASUpprimer().display();
            }
        });
	}
}