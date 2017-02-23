package sudoku.model.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import sudoku.model.Cell;
import sudoku.model.ICell;

public class GuiCell extends JPanel {
	
	// ATTRIBUTS
	private ICell model;
	
	private JLabel value;
	private JLabel[] candidates;
	
	// CONSTRUCTEUR
	public GuiCell(ICell cell) {
		createModel(cell);
		createView();
		placeComponents();
		createController();
	}
	
	// OUTILS
	private void createModel(ICell cell) {
		model = cell;
	}
	
	private void createView() {
		value = new JLabel("V");
		candidates = new JLabel[model.getCardinalCandidates()];
		for (int k = 0; k < candidates.length; ++k) {
			candidates[k] = new JLabel(Integer.toString(k + 1));
		}
	}
	
	private void placeComponents() {
		this.setLayout(new CardLayout());
		JPanel p = new JPanel(new GridLayout(3, 3)); {
			for (JLabel c : candidates) {
				p.add(c);
			}
		}
		this.add(p);
		this.add(value);
	}
	
	private void createController() {
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				flip();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
		});
	}
	
	private void flip() {
		((CardLayout)this.getLayout()).next(this);
	}
	
	// TEST
	public static void main(String[] args) {
		class Bla {
			JFrame mainFrame = new JFrame();
			public Bla() {
				mainFrame.add(new GuiCell(new Cell(9)), BorderLayout.CENTER);
				mainFrame.add(new GuiCell(new Cell(9)), BorderLayout.NORTH);
				mainFrame.add(new GuiCell(new Cell(9)), BorderLayout.SOUTH);
				mainFrame.add(new GuiCell(new Cell(9)), BorderLayout.WEST);
				mainFrame.add(new GuiCell(new Cell(8)), BorderLayout.EAST);
				mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
			
			public void display() {
				mainFrame.pack();
				mainFrame.setLocationRelativeTo(null);
				mainFrame.setVisible(true);
			}
		}
		
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new Bla().display();
			}
			
		});
	}
}