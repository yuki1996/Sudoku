package sudoku.model;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import sudoku.util.Coord;
import sudoku.util.ICoord;
import util.Contract;

public final class Main {
	
	private Main() {
		// rien
	}

	public static void main(String[] args) throws IOException {
		
		//Creation
		SudokuModel sudo = new StdSudokuModel(new File("./grille1.txt"));;

		while (!sudo.getGridSoluce().isFull() ) {
			sudo.resolve(sudo.getGridSoluce());
		}
		
		while (!sudo.getGridPlayer().isFull() ) {
			System.out.println(sudo.help());
			sudo.resolve(sudo.getGridPlayer());
			affiche_grille(sudo);
		}
		System.out.println(sudo.isWin() ? "gagné" : "perdu");
	}

	//OUTILS
	private static void affiche_grille(SudokuModel sudo) {
		Contract.checkCondition(sudo != null);
		for (int i = 0; i < sudo.getGridPlayer().size(); i++) {
			if (i % 3 == 0) {
				System.out.println("  -------------------------");
			}
			for (int j = 0; j < sudo.getGridPlayer().size(); j++) {
				if (j % 3 == 0) {
					System.out.print(" | ");
				}
				System.out.print(sudo.getGridPlayer().cells()[i][j].getValue()+" ");
				
			}
			System.out.println("");
		}
		System.out.println("  -------------------------");
		
	}
	
	private static void affiche_grille_soluce(SudokuModel sudo) {
		Contract.checkCondition(sudo != null);
		for (int i = 0; i < sudo.getGridSoluce().size(); i++) {
			if (i % 3 == 0) {
				System.out.println("  -------------------------");
			}
			for (int j = 0; j < sudo.getGridSoluce().size(); j++) {
				if (j % 3 == 0) {
					System.out.print(" | ");
				}
				System.out.print(sudo.getGridSoluce().cells()[i][j].getValue()+" ");
				
			}
			System.out.println("");
		}
		System.out.println("  -------------------------");
	}
	

	private static void affiche_possibilite(ICoord coord, SudokuModel sudo) {
		Contract.checkCondition(sudo != null || coord != null);
		CellModel c = sudo.getGridPlayer().getCell(coord);
	
		for (int i = 0; i < c.getCardinalCandidates(); i++) {
			System.out.print(i + 1 + " ");
		}

		System.out.println("");
		for (int i = 0; i < c.getCardinalCandidates(); i++) {
			System.out.print(c.isCandidate(i) ? 1 : 0);
			System.out.print(" ");
		}
		System.out.println("");
	}
	
	private static void affiche_grille_region(SudokuModel sudo) {
		Contract.checkCondition(sudo != null);
		GridModel grid = sudo.getGridPlayer();
		int nbSW = grid.getNumberSectorByWidth();
		int nbSH = grid.getNumberSectorByHeight();
		for (int i = 0; i < grid.getWidthSector(); i++) {
			for (int j = 0; j < grid.getHeightSector(); j++) {
				for (int m = i * nbSW; m < grid.getHeightSector() * (i + 1); m++) {
					for (int n = j * nbSH; n < grid.getWidthSector() * (j + 1); n++) {
						System.out.print(sudo.getGridPlayer().cells()[m][n].getValue()+" ");
					}

					System.out.println("");
				}

				System.out.println("-----");
				
			}
		}
	}
	
	private static void affiche_grille_region_ligne(SudokuModel sudo) {
		Contract.checkCondition(sudo != null);
		GridModel grid = sudo.getGridPlayer();
		int nbSW = grid.getNumberSectorByWidth();
	
		for (int i = 0; i < grid.getWidthSector(); i++) {
			for (int m = i * nbSW; m < grid.getHeightSector() * (i + 1); m++) {
				for (int j = 0; j < grid.size(); j++) {
					System.out.print(sudo.getGridPlayer().cells()[m][j].getValue()+" ");
				}
				System.out.println("");
				
			}

			System.out.println("-----");
		}
	}
	



	private static void affiche_grille_region_colonne(SudokuModel sudo) {
		Contract.checkCondition(sudo != null);
		GridModel grid = sudo.getGridPlayer();
		int nbSH = grid.getNumberSectorByHeight();
		for (int j = 0; j < grid.getHeightSector(); j++) {
			for (int m = j * nbSH; m < grid.getWidthSector() * (j + 1); m++) {
				for (int i = 0; i < grid.size(); i++) {
					System.out.print(sudo.getGridPlayer().cells()[i][m].getValue()+" ");
				}
				System.out.println("");
			}
			System.out.println("-----");
		}
	}
}
