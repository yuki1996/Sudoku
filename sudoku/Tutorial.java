package sudoku;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
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
	}
	
	private void placeComponents() {		
		JPanel p = new JPanel(); {
			JPanel q = new JPanel(new GridLayout(2, 1)); {
				// explications
				q.add(new JLabel("Pour jouer :"));
				JTextArea area = new JTextArea(); 
				
				area.append("Le mot SuDoku signifie nombre (Su) unique (Doku) en japonais et, \n" +
						"comme son nom l'indique, le but du jeu est de remplir la grille \n" +
						"avec une série de chiffres (ou de lettres ou de symboles) \n" +
						"tous différents, qui ne se trouvent jamais plus d’une fois \n" +
						"sur une même ligne, dans une même colonne ou dans une même sous-grille. \n\n" +
						"La plupart du temps, les symboles sont des chiffres allant de 1 à 9, \n" +
						"les sous-grilles étant alors des carrés de 3 × 3. \n\n" +
						"Quelques symboles sont déjà disposés dans la grille, \n" +
						"ce qui autorise une résolution progressive du problème complet.\n");
				area.setEditable(false);
				q.add(area);
			}
			p.add(q);
		}
		mainFrame.add(p);
		
		p = new JPanel(); {
			p.add(play);
		}
		mainFrame.add(p, BorderLayout.SOUTH);
	}

	private void createController() {
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // guide comment utiliser cette application
        play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
