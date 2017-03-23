package sudoku;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Guide {
	
	// CONSTANTES
	
	
	// ATTRIBUTS
	private JFrame mainFrame;
	private JButton play;
	private JButton tutorial;
	
	
	// CONSTRUCTEURS
	public Guide() {
		createView();
		placeComponents();
		createController();
	}
	
	// COMMANDES
	public void display() {
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }
	
	// OUTILS  
	private void createView() {
		final int frameWidth = 500;
        final int frameHeight = 500;
         
        mainFrame = new JFrame("Guide");
        mainFrame.setPreferredSize(new Dimension(frameWidth, frameHeight));
        
        play = new JButton("jouer");
        tutorial = new JButton("tutoriel");
	}
	
	private void placeComponents() {		
		JPanel p = new JPanel(new GridLayout(1, 1)); {
			JTextArea area = new JTextArea(); 
			
			area.append("Pour pouvoir remplir les grilles de sudoku,\n"
					+ "il vous suffit de selectionner une valeur en effectuant \n"
					+ "un clic gauche sur la valeur afin de fixer cette valeur \n" 
					+ "en tant que valeur candidate à la cellule ou en \n" 
					+ "effectuant un clic droit si vous voulez mettre \n" 
					+ "la valeur en tant que possibilitée.\n\n"
					+ "Les boutons situé à droite de la grille de jeu \n" 
					+ "vous permettront de défaire l'action réalisée à l'instant \n" 
					+ "(flèche vers la gauche), refaire l'action \n" 
					+ "(flèche vers la droite), consulter le temps \n" 
					+ "de résolution de la grille, demarrer le chrono, \n"
					+ "le mettre en pause, demander une réinitialisation \n"
					+ "de la grille, demander une résolution pas à pas \n"
					+ "ou encore demander la solution complète.");
			
			area.setEditable(false);
			p.add(area);
			p.setPreferredSize(new Dimension(400, 400));
			area.setMargin(new Insets(10, 10, 10, 10));
		}
		mainFrame.add(p, BorderLayout.CENTER);
		
		p = new JPanel(new GridLayout(1, 2)); {
			p.add(tutorial);
			p.add(play);
		}
		mainFrame.add(p, BorderLayout.SOUTH);
	}

	private void createController() {
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	mainFrame.dispose();
            }
        });
        
        tutorial.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	new Tutorial().display();
            	mainFrame.dispose();
            }
        });
	}
  
	// LANCEUR
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Guide().display();
            }
        });
	}
}
