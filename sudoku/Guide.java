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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Guide {
	
	// CONSTANTES
	
	
	// ATTRIBUTS
	private JFrame mainFrame;
	private JButton play;
	
	
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
	}
	
	private void placeComponents() {		
		JPanel p = new JPanel(new GridLayout(2, 1)); {
			JPanel q = new JPanel(new GridLayout(0, 1)); {
				// explications
				q.add(new JLabel("Guide de l'utilisateur :"));
				
				JTextArea area = new JTextArea(50, 20); 
				area.append("-Pour ajouter/enlever une possibilité dans une cellule,\n " +
						"il vous suffit de faire un clic gauche sur le chiffre voulu.\n\n");
				area.append("-Pour ajouter/enlever une valeur dans une cellule,\n " +
						"il vous suffit de faire un clic droit sur le chiffre voulu.\n\n");
				area.append("-Sur le côté droit de votre écran, vous disposez \n" +
						"d'une horloge qui vous indiquera le temps mis que \n" +
						"vous mettez, que vous avez mis à résoudre le sudoku.\n" +
						"Ce temps peut être réinitialisé à n'importe \n" +
						"quel moment grâce au bouton réinitialiser ou être mis en suspens (bouton pause).\n\n");
				area.append("-Le pourcentage de progression vous indique \n" +
						"de combien vous avez progressé et vous incite à aller jusqu'au 100% !\n");
				area.append("-Pour ajouter/enlever une valeur dans une cellule,\n " +
						"il vous suffit de faire un clic droit sur le chiffre voulu.");
				area.append("-Pour avoir la solution finale du sudoku, \n" +
						"il vous suffit d'appuyer sur le bouton solution.");
				area.append("-Pour résoudre pas à pas, il vous suffit d'appuyer \n" +
						"sur le bouton de même nom, ce qui entraînera \n" +
						"une résolution cellule par cellule avec une petite explication.");
				area.setEditable(false);
				JScrollPane scroll = new JScrollPane(area);
				q.add(scroll);
			}
			p.add(q);
			
			q = new JPanel(); {
				q.add(play);
			}
			p.add(q);
		}
		mainFrame.add(p);
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
                new Guide().display();
            }
        });
	}
}