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
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
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
import javax.swing.filechooser.FileNameExtensionFilter;

import sudoku.model.StdSudokuModel;
import sudoku.model.SudokuModel;
import sudoku.model.history.cmd.Command;
import sudoku.view.Grid;

//import com.sun.media.sound.Toolkit;

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
	
	// Nombre de possibilités et leurs valeurs
	public final static int NUMBER_POSSIBILITIES = 9;
	public final int[] VALUES = {1,2,3,4,5,6,7,8,9};
	
	// Durée de rafraîchissement du chrono (en ms)
	public final static int DELAY_REFRESH_CHRONO = 1000;
	
	// ATTRIBUTS
	// Fenêtre de l'application
	private JFrame mainFrame;
	
	// modèles
	private Grid grid;
	private SudokuModel sudokuModel;
	// boutons de menu
	private JMenuItem exit;
	private JMenuItem newGameV4;
	private JMenuItem open;
	private JMenuItem save;
	private JMenuItem resetMenu;
	private JMenuItem tuto;
	private JMenuItem resolveMenu;
	private JMenuItem clueMenu;
	private JMenuItem solutionMenu;
	private JMenuItem userGuide;
	private JMenuItem undoMenu;
	private JMenuItem doMenu;	
	
	// boutons raccourcis
	private JButton pause;
	private JButton reset;
	private JButton resolve;
	private JButton clue;
	private JButton solution;
	private JButton undoAction;
	private JButton doAction;
	
	// gestion du chrono
	private JLabel time;
	private Chrono chrono;
	private Timer timer;
	
	// zone de texte pour l'aide
    private JTextField textArea;
	
	// CONSTRUCTEURS
	public Game() {
		createModel();
		createView();
		placeComponents();
		createController();
		// permet d'avoir le focus sur la fenêtre de notre application
		//mainFrame.requestFocus();
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
		sudokuModel = new StdSudokuModel(WIDTH_SECTOR, HEIGHT_SECTOR);
    }
  
	private void createView() {
		final int frameWidth = 800;
        final int frameHeight = 600;
       
		grid = new Grid(sudokuModel);
		
		chrono = new Chrono();
         
        mainFrame = new JFrame("Game");
        mainFrame.setPreferredSize(new Dimension(frameWidth, frameHeight));
        
        // boutons du menu et leurs raccourcis
        // quitter le jeu
    	exit = new JMenuItem("Quitter");
    	exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_MASK));
    	
    	// nouveau jeu
    	newGameV4 = new JMenuItem("Nouveau");
    	newGameV4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,KeyEvent.CTRL_MASK));
    	
    	// ouverture 
    	open = new JMenuItem("Ouvrir");
    	open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
    	
    	// sauvegarde
    	save = new JMenuItem("Sauvegarder");
    	save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
    	
    	// réinitialisation
    	resetMenu = new JMenuItem("Réinitialiser");
    	resetMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK));
    	
    	// tutoriel
    	tuto = new JMenuItem("Tutoriel");
    	tuto.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_MASK));
    	
    	// résoudre pas à pas
    	resolveMenu = new JMenuItem("Résoudre pas à pas");
    	resolveMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK));
    	
    	// résoudre pas à pas
    	clueMenu = new JMenuItem("Indice");
    	clueMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK));
    	
    	// solution final
    	solutionMenu = new JMenuItem("Solution");
    	solutionMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_MASK));
    	
    	// guide utilisateur
    	userGuide = new JMenuItem("Comment jouer");
    	userGuide.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.CTRL_MASK));
    	
    	// annuler la dernière action
    	undoMenu = new JMenuItem("Annuler l'action");
    	undoMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK));
    	undoMenu.enable(false);
    	
    	// refaire l'action
    	doMenu = new JMenuItem("Refaire l'action");
    	doMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_MASK));
    	doMenu.enable(false);
    	
    	// pause
    	pause = new JButton();
    	pause.setBackground(Color.WHITE);
    	pause.setIcon(
    			new ImageIcon(
    					new ImageIcon(getClass().getResource("pictures/pause.png")).getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT)));
    	pause.setMargin(new Insets(0, 0, 0, 0));    	
    	pause.setPreferredSize(new Dimension(50, 75));
    	pause.setName("pause");
    	pause.setToolTipText("pause");
    	
    	//réinitialisation
    	reset = new JButton();
    	reset.setBackground(Color.WHITE);
    	reset.setIcon(
    			new ImageIcon(
    					new ImageIcon(getClass().getResource("pictures/reset.png")).getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT)));
    	reset.setMargin(new Insets(0, 0, 0, 0));
    	reset.setPreferredSize(new Dimension(50, 75));
    	reset.setToolTipText("réinitialisation de la grille");
    	
    	// solution
    	solution = new JButton();
    	solution.setBackground(Color.WHITE);
    	solution.setIcon(
    			new ImageIcon(
    					new ImageIcon(getClass().getResource("pictures/solution.png")).getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT)));
    	solution.setMargin(new Insets(0, 0, 0, 0));
    	solution.setPreferredSize(new Dimension(50, 75));
    	solution.setToolTipText("solution complète");
    	
    	// indice 
    	clue = new JButton();
    	clue.setBackground(Color.WHITE);
    	clue.setIcon(
    			new ImageIcon(
    					new ImageIcon(getClass().getResource("pictures/clue.png")).getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT)));
    	clue.setMargin(new Insets(0, 0, 0, 0));
    	clue.setPreferredSize(new Dimension(50, 75));
    	clue.setToolTipText("Indice");
    	
    	
    	// résoudre pas-à-pas
    	resolve  = new JButton();
    	resolve.setBackground(Color.WHITE);
    	resolve.setIcon(
    			new ImageIcon(
    					new ImageIcon(getClass().getResource("pictures/step.png")).getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT)));
    	resolve.setMargin(new Insets(0, 0, 0, 0));
    	resolve.setPreferredSize(new Dimension(50, 75));
    	resolve.setToolTipText("résoudre pas à pas");
    	
    	// annuler l'action
    	undoAction  = new JButton();
    	undoAction.setBackground(Color.WHITE);
    	undoAction.setIcon(
    			new ImageIcon(
    					new ImageIcon(getClass().getResource("pictures/undo.png")).getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT)));
    	undoAction.setMargin(new Insets(0, 0, 0, 0));
    	undoAction.setPreferredSize(new Dimension(50, 75));
    	undoAction.setToolTipText("annuler l'action");
    	undoAction.enable(false);
    	
    	// refaire l'action
    	doAction = new JButton();
    	doAction.setBackground(Color.WHITE);
    	doAction.setIcon(
    			new ImageIcon(
    					new ImageIcon(getClass().getResource("pictures/do.png")).getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT)));
    	doAction.setMargin(new Insets(0, 0, 0, 0));
    	doAction.setPreferredSize(new Dimension(50, 75));
    	doAction.setToolTipText("refaire l'action");
    	doAction.enable(false);
    	
    	// chronomètre
    	//chrono = new Chrono();
    	//chrono.start();
    	time = new JLabel(chrono.getChrono());
    	time.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

    	// Zone de texte pour l'aide
    	textArea = new JTextField("-- Aide : -- \n");
    	textArea.setPreferredSize(new Dimension(800, 75));
    	textArea.setEditable(false);
	}
	
	private void placeComponents() {
		// Barre de menu
		mainFrame.setJMenuBar(createJMenuBar());
		
		// Grille de sudoku
		mainFrame.add(grid, BorderLayout.CENTER);
		
		// Barre des raccourcis menu
		JPanel p = new JPanel(new GridLayout(1, 1)); {
			JPanel q = new JPanel(new GridLayout(7, 1)); {
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
				q.add(clue);
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
            	newGame();
            }
        });
		
		// ouvrir un fichier 
        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	open();
            }
        });
        
        // sauvegarder
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
		
        // quitter
		exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	exit();
            }
        });
		
		resetMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetMenu();
            }
        });
		
		// résoudre pas à pas 
        resolveMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resolveMenu();
            }
        });

		// indice
        clueMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clue();
            }
        });
        
        // solution finale
        solutionMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                solutionMenu();
            }
        });
        
        // refait l'action
        doMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doMenu();
            }
        });
        
        // défait l'action
        undoMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                undoMenu();
            }
        });
        
        // tutoriel
        tuto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	tutorial();
            }
        });
        
        // guide comment utiliser cette application
        userGuide.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guide();
            }
        });
        
        // vérification avant de quitter définitivement le programme
        mainFrame.addWindowListener(new WindowAdapter() {
  		  public void windowClosing(WindowEvent e) {
  		        exit();
  		  }
        });
        
        // pause
        pause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pause();
            }
        });
        
        // réinitialisation
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	resetMenu();
            }
         });
        
        // système d'écouteurs pour les raccouris clavier
        mainFrame.addKeyListener(new KeyAdapter() {
  	      public void keyPressed(KeyEvent e) {
  	    	int code = e.getKeyCode();
  			
  	    	// n pour nouveau jeu 
  	    	if (code == KeyEvent.VK_N) {
  	    		newGame();
  	    	}
  	    	
  	    	if (code == KeyEvent.VK_C) {
  	    		clue();
  	    	}
  	    	
  			// p pour pause
  			if (code == KeyEvent.VK_P) {
  				pause();
  			}
  			
  			// t pour tutoriel
  			if (code == KeyEvent.VK_T) {
  				tutorial();
  			}
  			
  		    // g pour guide
  			if (code == KeyEvent.VK_G) {
  				guide();
  			}
  	      }
  	    });
        
        // gestion du rafraîchissement du chrono
        TimerTask actionChrono = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				time.setText(chrono.getChrono());
			}
        };
        timer = new Timer(true);
        timer.schedule(actionChrono, DELAY_REFRESH_CHRONO);
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
                m.add(clueMenu);
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
	
	// gestion des fonctions 
	private void newGame() {
		new Game().display();
	}
	
	private void open() {
		// TODO
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter flt = new FileNameExtensionFilter("Fichier", "txt");
		fc.setFileFilter(flt);
		int res = fc.showOpenDialog(mainFrame);
		if (res == JFileChooser.APPROVE_OPTION) {
			try {
				sudokuModel= new StdSudokuModel(fc.getSelectedFile());
				grid = new Grid(sudokuModel);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		display();
	}
	
	private void save() {
		// TODO
		String name = "Enregistrement1";
		try {
			sudokuModel.save(name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
	private void exit() {
		int exit = JOptionPane.showConfirmDialog(null,
	        		"Êtes-vous sûr de vouloir quitter?\n" +
	        		"Toutes les données non-sauvegardées seront perdues.",
	        		"Quitter?", JOptionPane.YES_OPTION);
        if (exit == JOptionPane.YES_OPTION) {
        	System.exit(1);
        }
	}
	
	private void pause() {
		// bouton pause au moment d'appuyer, on stoppe le chrono 
    	// et on verrouille la grille en la faisant disparaitre momentanément 
    	// et en affichant un joli petit message, l'image change également
    	if (pause.getName().compareTo("pause") == 0) {
    	   pause.setName("start");
    	   pause.setIcon(
       			new ImageIcon(
       					new ImageIcon(getClass().getResource("pictures/pause.png")).getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT)));
       	   chrono.pause();
           mainFrame.hide();
           String[] msg = {"Reprendre le jeu", "Quittez le jeu"};
           String result = (String) JOptionPane.showInputDialog(null, 
        	 "Petite pause ! Que voulez vous faire ?",
        	 "Pause", JOptionPane.QUESTION_MESSAGE, null, msg, msg[0]);
           if (result.compareTo("Quittez le jeu") == 0) {
        	   exit();
           } 
           mainFrame.show();
        // bouton démarrer/start au moment d'appuyer, on change 
    	//   le bouton en pause et on affiche le logo adéquat.  
    	} else {
    	   pause.setName("pause");
    	   pause.setIcon(
       			new ImageIcon(
       					new ImageIcon(getClass().getResource("pictures/pause.png")).getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT)));
    	   chrono.start();
    	}
	}
    
	private void resetMenu() {
		sudokuModel.reset();
		JOptionPane.showMessageDialog(null, "Réinitialisation de la grille", "réinitialisation", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void resolveMenu() {
		sudokuModel.check();
	}
	
	private void clue() {
		sudokuModel.help();
	}
	
	private void solutionMenu() {
		sudokuModel.finish();
		JOptionPane.showMessageDialog(null, "Résolution de la grille", "Solution", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void doMenu() {
		if (sudokuModel.canRedo()) {
			sudokuModel.redo();
		}
		if (!sudokuModel.canRedo()) {
			doMenu.enable(false);
		}
	}
	
	private void undoMenu() {
		if (sudokuModel.canUndo()) {
			sudokuModel.undo();
		}
		if (!sudokuModel.canUndo()) {
			undoMenu.enable(false);
		}
	}
	
	private void tutorial() {
		new Tutorial().display();
	}
	
	private void guide() {
		new Guide().display();
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
