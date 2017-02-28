package sudoku;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class BeanGrid {
	
	// ATTRIBUTS
	private final int heightGrid;
	private final int widthGrid;
	private final int heightCell;
	private final int widthCell;
	private final int[] alpha;
	private JButton[] digitButton; 
	
	/**
	 * Construit une grille de sudoku de taille h * w 
	 * et avec un alphabet a de type générique E.
	 * 
	 */
	public BeanGrid(int hg, int wg, int hc, int wc, int[] a) {
		heightGrid = hg;
		widthGrid = wg;
		heightCell = hc;
		widthCell = wc;
		alpha = a;
		initDigitButton();
	}
	
	public JButton[] getDigitButton() {
		return digitButton;
	}
	
	public JPanel makeGrid() {
		JPanel p = new JPanel(new GridLayout(heightGrid, widthGrid)); {
			int k = 0;
			for (int i = 0 ; i < alpha.length ; ++i) {
				JPanel q = new JPanel(new GridLayout(heightCell, widthCell)); {
					for (int j = 0 ; j < alpha.length ; ++j) {
						JPanel r = new JPanel(new GridLayout(heightCell, widthCell)); {
							for (int l = 0; l < alpha.length; ++l) {
								r.add(digitButton[k]);
								++k;
							}
						}
						q.add(r);
						r.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
					}
				}
				p.add(q);
				q.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
			}
		}
		p.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		return p;
	}
	
	private void initDigitButton() { 
    	int k = 1;
    	int n = (heightGrid * widthGrid) * heightCell ;
    	n *= n;
    	digitButton = new JButton[n];
    	for (int i = 0 ; i < n ; ++i) {
    		digitButton[i] = new JButton("" + k);
    		digitButton[i].setName("" + i);
    		digitButton[i].setBorder(BorderFactory.createLineBorder(Color.WHITE));
    		digitButton[i].setBackground(Color.WHITE);
    		if (k == 9) { 
    			k = 0;
    		}
    		++k;
    	}
	}
	
}
