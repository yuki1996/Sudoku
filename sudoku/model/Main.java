package sudoku.model;

import java.io.File;
import java.io.IOException;

import sudoku.util.Coord;
import sudoku.util.ICoord;
import util.Contract;

public final class Main {
	
	private Main() {
		// rien
	}

	public static void main(String[] args) throws IOException {
		
		//Creation
		Sudoku sudo = new Sudoku(new File("./grille1.txt"));;

		System.out.println("grille joueur \n");
		//affichage de la création
		affiche_grille(sudo);
		
		System.out.println("\n grille soluce \n");
		//affichage de la création
		affiche_grille_soluce(sudo);
		

		System.out.println("\npossibilté en (5,2) \n");
		affiche_possibilite(new Coord(5, 2), sudo);
		sudo.updateEasyPossibilities(new Coord(6, 2));
		

		System.out.println("\n possibilté en (5,2) \n");
		affiche_possibilite(new Coord(5, 2), sudo);
		
		
	}

	//OUTILS
	private static void affiche_grille(ISudoku sudo) {
		Contract.checkCondition(sudo != null);
		for (int i = 0; i < sudo.getGridPlayer().size(); i++) {
			for (int j = 0; j < sudo.getGridPlayer().size(); j++) {
				System.out.print(sudo.getGridPlayer().cells()[i][j].getValue()+" ");
				
			}
			System.out.println("");
		}
	}
	
	private static void affiche_grille_soluce(ISudoku sudo) {
		Contract.checkCondition(sudo != null);
		for (int i = 0; i < sudo.getGridSoluce().size(); i++) {
			for (int j = 0; j < sudo.getGridSoluce().size(); j++) {
				System.out.print(sudo.getGridSoluce().cells()[i][j].getValue()+" ");
			}
			System.out.println("");
		}
	}
	

	private static void affiche_possibilite(ICoord coord, ISudoku sudo) {
		Contract.checkCondition(sudo != null || coord != null);
		ICell c = sudo.getGridPlayer().getCell(coord);
	
		for (int i = 0; i < c.getCardinalPossibilities(); i++) {
			System.out.print(i + 1 + " ");
		}

		System.out.println("");
		for (int i = 0; i < c.getCardinalPossibilities(); i++) {
			System.out.print(c.possibilities()[i] ? 1 : 0);
			System.out.print(" ");
		}
		System.out.println("");
	}
}
