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

public class Tutorial {
	
	// CONSTANTES
	
	
	// ATTRIBUTS
	private JFrame mainFrame;
	private JButton play;
	private JButton guide;
	
	
	// CONSTRUCTEURS
	public Tutorial() {
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
         
        mainFrame = new JFrame("Tutorial");
        mainFrame.setPreferredSize(new Dimension(frameWidth, frameHeight));
        
        play = new JButton("jouer");
        guide = new JButton("guide");
	}
	
	private void placeComponents() {		
		JPanel p = new JPanel(new GridLayout(1, 1)); {
			JTextArea area = new JTextArea(); 
			
			area.append("Les règles du sudoku sont très simples. \n"
						+ "Un sudoku classique contient neuf lignes et \n" 
						+ "neuf colonnes, donc 81 cases au total. \n\n" 
						+ "Le but du jeu est de remplir ces cases \n"
						+ "avec des chiffres allant de 1 à 9 en veillant \n" 
						+ "toujours à ce qu'un même chiffre ne figure \n"
						+ "qu'une seule fois par colonne, une seule fois par ligne, \n" 
						+ "et une seule fois par carré de neuf cases.\n\n" 
						+ "Au début du jeu, une vingtaine de chiffres \n" 
						+ "sont déjà placés et il vous reste à trouver les autres.\n" 
						+ "En effet, une grille initiale de sudoku correctement \n"
						+ "constituée ne peut aboutir qu'à une et une seule solution.\n"
						+ "Pour trouver les chiffres manquants, tout est une question \n" 
						+ "de logique et d'observation.\n\n"
						+ "De nombreuses variantes peuvent être apportées \n"
						+ "au sudoku classique (aussi appelé 9x9), ne serait-ce \n" 
						+ "qu'en modifiant le nombre de lignes et de colonnes.\n" 
						+ "Vous pouvez ainsi trouver sur le Sudoku toute \n"
						+ "la combinaison des grilles en allant du sudoku \n"
						+ "enfant 4x4 au sudoku géant 12x12.\n");
			
			area.setEditable(false);
			p.add(area);
			p.setPreferredSize(new Dimension(400, 400));
			area.setMargin(new Insets(10, 10, 10, 10));
		}
		mainFrame.add(p, BorderLayout.CENTER);
		
		p = new JPanel(new GridLayout(1, 2)); {
			p.add(guide);
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
        
        guide.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	new Guide().display();
            	mainFrame.dispose();
            }
        });
	}
  
	// LANCEUR
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Tutorial().display();
            }
        });
	}
}
