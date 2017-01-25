package sudoku.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import sudoku.util.Coord;
import sudoku.util.ICoord;
import util.Contract;

public class main {

	public static void main(String arg[]) {
		
		//Creation
		Map<ICoord, Integer> map = new HashMap<ICoord, Integer>();
		ajout(map);
		IGrid grille = new Grid(3, 3, map);
		
		//affichage de la création
		affiche_grille(grille);

		//test colonne
		System.out.print("\ncol2 ");
		Set<ICell> col2 = grille.getCol(new Coord(1,2));
		for (ICell cell : col2) {
			System.out.print(cell.getValue()+" ");
			
		}
		
		//test ligne
		System.out.print("\nlig2 ");
		Set<ICell> lig2 = grille.getRow(new Coord(6,1));
		for (ICell cell : lig2) {
			System.out.print(cell.getValue()+" ");
			
		}

		//test région
		System.out.print("\nreg9 ");
		Set<ICell> reg9 = grille.getSector(new Coord(7,7));
		for (ICell cell : reg9) {
			System.out.print(cell.getValue()+" ");
			
		}

		//test ajout valeur
		System.out.println("\n\najout 2 dans coord(3,2)");
		grille.changeValue(new Coord(3,2), 2);
		affiche_grille(grille);
		

		//test supprime valeur modifiable
		System.out.println("\nsupprimer valeur dans coord(3,2)");
		grille.resetValue(new Coord(3,2));
		affiche_grille(grille);
		

		//test réinitilisation
		System.out.println("\najout 2 dans coord(3,2)");
		grille.changeValue(new Coord(3,2), 2);
		affiche_grille(grille);
		System.out.println("\nreset");
		grille.reset();
		affiche_grille(grille);
		
		//test réinitilisation
		System.out.println("\najout 2 dans coord(3,2)");
		grille.changeValue(new Coord(3,2), 2);
		affiche_grille(grille);
		System.out.println("\nclear");
		grille.clear();
		affiche_grille(grille);
		
		//test grille pleine
		System.out.println("\ngrille pleine ?");
		if (grille.isFull()) {
			System.out.println("C'est bien tu as fini mais est-elle correcte???");
		} else {
			System.out.println("T'as pas fini !!! bosse plus!!");
		}
		
		
	}

	//OUTILS
	private static void affiche_grille(IGrid grille) {
		Contract.checkCondition(grille != null);
		for (int i = 0; i < grille.size(); i++) {
			for (int j = 0; j < grille.size(); j++) {
				System.out.print(grille.cells()[j][i].getValue()+" ");
				
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
