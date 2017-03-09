package sudoku.model.heuristic;


import java.util.Map;
import java.util.Set;

import sudoku.model.history.cmd.Command;
import sudoku.util.ICoord;

public interface Report {

	public enum CellSetName {
		DECISIVE_CELLS,
		DECISIVE_UNITS,
		DELETION_CELLS,
		DELETION_UNITS;
	}
	
	
	//REQUETES
	/**
	 * Renvoie une description d'une modification possible du sudoku.
	 */
	String describe();
	
	/**
	 * Renvoie les ensembles de cellules importants pour la règle utilisée.
	 */
	Map<CellSetName, Set<ICoord>> importantSets();
	
	//COMMANDES
	/**
	 * Renvoie la commande à executer pour faire les changements indiquer par la 
	 * regle.
	 */
	Command getCommand();
}