package sudoku.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
		Sudoku sudo = new Sudoku(new File("./test.txt"));;

		System.out.println("grille joueur ");
		//affichage de la création
		affiche_grille(sudo);
		
		System.out.println("grille soluce ");
		//affichage de la création
		affiche_grille_soluce(sudo);

		//test colonne
		System.out.print("\ncol2 ");
		Set<ICell> col2 = sudo.getGridPlayer().getCol(new Coord(1,2));
		for (ICell cell : col2) {
			System.out.print(cell.getValue()+" ");
			
		}
		
		//test ligne
		System.out.print("\nlig2 ");
		Set<ICell> lig2 = sudo.getGridPlayer().getRow(new Coord(6,1));
		for (ICell cell : lig2) {
			System.out.print(cell.getValue()+" ");
			
		}

		//test région
		System.out.print("\nreg9 ");
		Set<ICell> reg9 = sudo.getGridPlayer().getSector(new Coord(7,7));
		for (ICell cell : reg9) {
			System.out.print(cell.getValue()+" ");
			
		}

		//test ajout valeur
		System.out.println("\n\najout 2 dans coord(3,2)");
		sudo.getGridPlayer().changeValue(new Coord(3,2), 2);
		affiche_grille(sudo);
		

		//test supprime valeur modifiable
		System.out.println("\nsupprimer valeur dans coord(3,2)");
		sudo.getGridPlayer().resetValue(new Coord(3,2));
		affiche_grille(sudo);
		

		//test réinitilisation
		System.out.println("\najout 2 dans coord(3,2)");
		sudo.getGridPlayer().changeValue(new Coord(3,2), 2);
		affiche_grille(sudo);
		System.out.println("\nreset");
		sudo.getGridPlayer().reset();
		affiche_grille(sudo);
		
		//test réinitilisation
		System.out.println("\najout 2 dans coord(3,2)");
		sudo.getGridPlayer().changeValue(new Coord(3,2), 2);
		affiche_grille(sudo);
		System.out.println("\nclear");
		sudo.getGridPlayer().clear();
		affiche_grille(sudo);
		
		//test grille pleine
		System.out.println("\ngrille pleine ?");
		if (sudo.isWin()) {
			System.out.println("C'est bien tu as fini mais est-elle correcte???");
		} else {
			System.out.println("T'as pas fini !!! bosse plus!!");
		}

		System.out.println("grille soluce ");
		//affichage de la création
		affiche_grille_soluce(sudo);
		
		sudo.save("test2.txt");
		
		Sudoku sudo2 = new Sudoku(3,3);
		try {
			sudo2.load(new File("test2.txt"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("grille joueur ");
		//affichage de la création
		affiche_grille(sudo2);
		
		System.out.println("grille soluce ");
		//affichage de la création
		affiche_grille_soluce(sudo2);
		
		
	}

	//OUTILS
	private static void affiche_grille(ISudoku sudo) {
		Contract.checkCondition(sudo != null);
		for (int i = 0; i < sudo.getGridPlayer().size(); i++) {
			for (int j = 0; j < sudo.getGridPlayer().size(); j++) {
				System.out.print(sudo.getGridPlayer().cells()[j][i].getValue()+" ");
				
			}
			System.out.println("");
		}
	}
	
	private static void affiche_grille_soluce(ISudoku sudo) {
		Contract.checkCondition(sudo != null);
		for (int i = 0; i < sudo.getGridSoluce().size(); i++) {
			for (int j = 0; j < sudo.getGridSoluce().size(); j++) {
				System.out.print(sudo.getGridSoluce().cells()[j][i].getValue()+" ");
				
			}
			System.out.println("");
		}
	}
	//MAP bassé sur la grille 1 de sudoku motcroise.ch
	private static void ajout(Map<ICoord, Integer> map) {
		Contract.checkCondition(map != null);
		map.put(new Coord(0,2), 6);
		map.put(new Coord(0,4), 1);
		
		map.put(new Coord(1,2), 5);
		map.put(new Coord(1,3), 4);
		map.put(new Coord(1,7), 9);
		
		map.put(new Coord(2,0), 1);
		map.put(new Coord(2,1), 2);
		map.put(new Coord(2,8), 4);
		
		map.put(new Coord(3,0), 3);
		map.put(new Coord(3,3), 9);
		map.put(new Coord(3,4), 7);
		map.put(new Coord(3,7), 8);
		map.put(new Coord(3,8), 5);
		
		map.put(new Coord(4,0), 5);
		map.put(new Coord(4,2), 4);
		map.put(new Coord(4,4), 2);
		
		map.put(new Coord(5,0), 9);
		map.put(new Coord(5,3), 1);
		map.put(new Coord(5,4), 8);
		map.put(new Coord(5,7), 4);
		map.put(new Coord(5,8), 2);
		
		map.put(new Coord(6,0), 2);
		map.put(new Coord(6,1), 9);
		map.put(new Coord(6,8), 3);
		
		map.put(new Coord(7,2), 3);
		map.put(new Coord(7,3), 5);
		map.put(new Coord(7,7), 7);
		
		map.put(new Coord(8,2), 1);
		map.put(new Coord(8,4), 4);
	}
}
