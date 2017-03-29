package sudoku;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import sudoku.model.StdSudokuModel;
import sudoku.model.SudokuModel;
import sudoku.view.Grid;

//import com.sun.media.sound.Toolkit;

public class GameV4 {
	
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
	private JMenuItem newGameV4;
	private JMenuItem open;
	private JMenuItem save;
	private JMenuItem resetMenu;
	private JMenuItem tuto;
	private JMenuItem resolveMenu;
	private JMenuItem solutionMenu;
	private JMenuItem userGuide;
	private JMenuItem undoMenu;
	private JMenuItem doMenu;	
	
	private JButton pause;
	private JButton reset;
	private JButton resolve;
	private JButton solution;
	private JButton undoAction;
	private JButton doAction;
	
	private JLabel time;
	private Chrono chrono;
	// timer pour générer le rafraîchissement
    private Timer timer;	
	
	private Grid grid;
	private SudokuModel model;
	
	private JTextField textArea;
	
	//private long startTime;
	//private JLabel currentTime;
	
	// CONSTRUCTEURS
	public GameV4() {
		createModel();
		createView();
		placeComponents();
		createController();
		mainFrame.requestFocus();
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
		model = new StdSudokuModel(WIDTH_SECTOR, HEIGHT_SECTOR);
    }
  
	private void createView() {
		final int frameWidth = 800;
        final int frameHeight = 600;
       
		grid = new Grid(model);
         
        mainFrame = new JFrame("Game");
        mainFrame.setPreferredSize(new Dimension(frameWidth, frameHeight));
        
        //mainFrame.setDefaultLookAndFeelDecorated(true);
        //mainFrame.setExtendedState(mainFrame.MAXIMIZED_BOTH);
        
    	/*timer = new Timer();*/
    	exit = new JMenuItem("Quitter");
//    	exit.addKeyListener(new KeyListener() {
//
//			@Override
//			public void keyPressed(KeyEvent arg0) {
//				// TODO Auto-generated method stub
//				if(arg0.getKeyCode() == KeyEvent.VK_ESCAPE || arg0.getKeyCode() == KeyEvent.VK_Q) {
//					int exit = JOptionPane.showConfirmDialog(null,
//	  		        		"Êtes-vous sûr de vouloir quitter?\n" +
//	  		        		"Toutes les données non-sauvegardées seront perdues.",
//	  		        		"Quitter?", JOptionPane.YES_OPTION);
//	  		        if (exit == JOptionPane.YES_OPTION) {
//	  		        	System.exit(1);
//	  		        }
//				}
//			}
//
//			@Override
//			public void keyReleased(KeyEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void keyTyped(KeyEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//    		
//    	});
    	exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_MASK));
    	newGameV4 = new JMenuItem("Nouveau");
    	newGameV4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,KeyEvent.CTRL_MASK));
    	open = new JMenuItem("Ouvrir");
    	open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
    	save = new JMenuItem("Sauvegarder");
    	save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
    	resetMenu = new JMenuItem("Réinitialiser");
    	resetMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK));
    	tuto = new JMenuItem("Tutoriel");
    	tuto.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_MASK));
    	resolveMenu = new JMenuItem("Résoudre pas à pas");
    	resolveMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK));
    	solutionMenu = new JMenuItem("Solution");
    	solutionMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_MASK));
    	userGuide = new JMenuItem("Comment jouer");
    	userGuide.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.CTRL_MASK));
    	undoMenu = new JMenuItem("Annuler l'action");
    	undoMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK));
    	doMenu = new JMenuItem("Refaire l'action");
    	doMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_MASK));
    	
    	pause = new JButton();
    	pause.setBackground(Color.WHITE);
    	//pause.setIcon(new ImageIcon(getClass().getResource("pictures/play.png")));
//    	pause.setIcon(
//    			new ImageIcon(
//    					new ImageIcon(getClass().getResource("pictures/pause.png")).getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT)));
    	pause.setMargin(new Insets(0, 0, 0, 0));    	
    	pause.setPreferredSize(new Dimension(50, 90));
    	pause.setName("pause");
    	pause.setToolTipText("pause");
    	//pause.setMnemonic(KeyEvent.VK_SPACE);
    	
    	reset = new JButton();
    	reset.setBackground(Color.WHITE);
    	// reset.setIcon(new ImageIcon(getClass().getResource("pictures/delete.jpg")));
//    	reset.setIcon(
//    			new ImageIcon(
//    					new ImageIcon(getClass().getResource("pictures/delete.jpg")).getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT)));
    	reset.setMargin(new Insets(0, 0, 0, 0));
    	reset.setPreferredSize(new Dimension(50, 90));
    	reset.setToolTipText("réinitialisation de la grille");
    	//reset.setMnemonic(KeyEvent.VK_R);
    	
    	solution= new JButton();
    	solution.setBackground(Color.WHITE);
//    	solution.setIcon(new ImageIcon(getClass().getResource("pictures/solution.jpg")));
//    	solution.setIcon(
//    			new ImageIcon(
//    					new ImageIcon(getClass().getResource("pictures/solution.png")).getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT)));
    	solution.setMargin(new Insets(0, 0, 0, 0));
    	solution.setPreferredSize(new Dimension(50, 90));
    	solution.setToolTipText("solution complète");
    	
    	resolve  = new JButton();
    	resolve.setBackground(Color.WHITE);
//    	resolve.setIcon(new ImageIcon(getClass().getResource("pictures/step.png")));7
//    	resolve.setIcon(
//    			new ImageIcon(
//    					new ImageIcon(getClass().getResource("pictures/step.png")).getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT)));
    	resolve.setMargin(new Insets(0, 0, 0, 0));
    	resolve.setPreferredSize(new Dimension(50, 90));
    	resolve.setToolTipText("résoudre pas à pas");
    	
    	undoAction  = new JButton();
    	undoAction.setBackground(Color.WHITE);
//    	undoAction.setIcon(new ImageIcon(getClass().getResource("pictures/undo.png")));
//    	undoAction.setIcon(
//    			new ImageIcon(
//    					new ImageIcon(getClass().getResource("pictures/undo.png")).getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
    	undoAction.setMargin(new Insets(0, 0, 0, 0));
    	undoAction.setPreferredSize(new Dimension(50, 90));
    	undoAction.setToolTipText("annuler l'action");
    	
    	doAction = new JButton();
    	doAction.setBackground(Color.WHITE);
//    	doAction.setIcon(new ImageIcon(getClass().getResource("pictures/do.png")));
//    	doAction.setIcon(
//    			new ImageIcon(
//    					new ImageIcon(getClass().getResource("pictures/do.png")).getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
    	doAction.setMargin(new Insets(0, 0, 0, 0));
    	doAction.setPreferredSize(new Dimension(50, 90));
    	doAction.setToolTipText("refaire l'action");
    	
    	//chrono = new Chrono();
    	//chrono.start();
    	
    	timer = new Timer();
    	time = new JLabel("00:00:00");
    	time.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    	
//    	refreshChrono();
    	
    	textArea = new JTextField("-- Aide : -- \n");
    	textArea.setPreferredSize(new Dimension(780, 50));
    	textArea.setEditable(false);
    	
    	//digitButton = grid.getDigitButton();
 
    	//startTime = System.currentTimeMillis();
    	//currentTime = new JLabel("" + (System.currentTimeMillis() - startTime));
    	
	}
	
	private void placeComponents() {
		// Barre de menu
		mainFrame.setJMenuBar(createJMenuBar());
		
		// Grille de sudoku
		mainFrame.add(grid, BorderLayout.CENTER);
		
		// Barre des raccourcis menu
		JPanel p = new JPanel(new GridLayout(1, 1)); {
			JPanel q = new JPanel(new GridLayout(6, 1)); {
				JPanel r = new JPanel(new GridLayout(1, 2)); {
					r.add(undoAction);
					r.add(doAction);
				}
				q.add(r);
				
				r = new JPanel(new GridLayout(1, 1)); {
					r.add(time);
				}
				r.setBackground(Color.WHITE);
				
				q.add(r);
				q.add(pause);
				q.add(reset);
				q.add(solution);
				q.add(resolve);
			}
			p.add(q);
		}
		mainFrame.add(p, BorderLayout.EAST);
		
		// Zone de texte
		p = new JPanel(new GridLayout(1, 1)); {
			JScrollPane scroll = new JScrollPane(textArea);
			p.add(scroll);
			scroll.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}
		mainFrame.add(p, BorderLayout.SOUTH);
	}

	private void createController() {
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
		
		// barre de menu
		// Nouvelle grille
		newGameV4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	new GameV4().display();
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
                // bouton pause au moment d'appuyer, on stoppe le chrono 
            	// et on verrouille la grille en la faisant disparaitre momentanément 
            	// et en affichant un joli petit message, l'image change également
            	if (pause.getName().compareTo("pause") == 0) {
            	   pause.setName("start");
            	   //currentTime.setText("" + (System.currentTimeMillis() - startTime));
            	   pause.setIcon(
               			new ImageIcon(
               					new ImageIcon(getClass().getResource("pictures/play.png")).getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT)));
               	   //chrono.pause();
	               //Boîte du message d'information
	               JOptionPane jop1 = new JOptionPane();
	               mainFrame.hide();
	               jop1.showMessageDialog(null, "Jeu en pause", "Pause", JOptionPane.INFORMATION_MESSAGE);
	               mainFrame.show();
                // bouton démarrer/start au moment d'appuyer, on change 
            	//   le bouton en pause et on affiche le logo adéquat.  
            	} else {
            	   pause.setName("pause");
            	   pause.setIcon(
               			new ImageIcon(
               					new ImageIcon(getClass().getResource("pictures/pause.png")).getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT)));
               }
            }
        });
        
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //startTime = System.currentTimeMillis();
                //currentTime.setText("" + (System.currentTimeMillis() - startTime));
                pause.setName("démarrer");
                pause.setIcon(
               			new ImageIcon(
               					new ImageIcon(getClass().getResource("pictures/play.png")).getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT)));
                JOptionPane jop1 = new JOptionPane();
	            jop1.showMessageDialog(null, "Réinitialisation de la grille", "réinitialisation", JOptionPane.INFORMATION_MESSAGE);
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
        
        mainFrame.addKeyListener(new KeyAdapter() {
  	      public void keyPressed(KeyEvent e) {
  	    	int code = e.getKeyCode();
  			
  			// r pour reset
  			if (code == KeyEvent.VK_R) {
  				pause.setName("démarrer");
  	            pause.setIcon(
  	           			new ImageIcon(
  	           					new ImageIcon(getClass().getResource("pictures/play.png")).getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT)));
  	            JOptionPane.showMessageDialog(null, "Réinitialisation de la grille", "réinitialisation", JOptionPane.INFORMATION_MESSAGE);
  			}
  			
  			// t pour tutoriel
  			if (code == KeyEvent.VK_T) {
  				new Tutorial().display();
  			}
  			
  		    // g pour guide
  			if (code == KeyEvent.VK_G) {
  				new Guide().display();
  			}
  	      }
  	    });
        
        //registerKetStroke();
	}
	
	/**
	 * Retourne une JMenuBar.
	 */
	private JMenuBar createJMenuBar() {
        JMenuBar bar = new JMenuBar(); {
            JMenu m = new JMenu("Fichier"); {
                m.add(newGameV4);
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
	
	// Rafraîchissement du chronomètre
	private void refreshChrono() {
		timer.schedule(new TimerTask() {
	        @Override
	        public void run() {
	        	//chrono.resume();
                //time.setText(chrono.getDureeTxt());
	        }
	    }, 1000, 1000); 
	}
	
//	private void registerKetStroke() {
//        JRootPane rootPane = this.getRootPane();
//        KeyStroke ctrlh = KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK);
//        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlh, "hideMenu");
//        rootPane.getActionMap().put("hideMenu", hideMenu);
//    }
//	
//    private Action hideMenu = new AbstractAction() {
//        public void actionPerformed(ActionEvent e) {
//            if (menu.isVisible() == true) {
//                menu.setVisible(false);
//            } else {
//                menu.setVisible(true);
//            }   
//        }
//    };
	
	// LANCEUR
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GameV4().display();
            }
        });
	}
}
