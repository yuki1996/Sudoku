package sudoku.model.heuristic;

import util.Contract;

public abstract class Report {
	//ATTRIBUTS
	/**
	 * on pourrait mettre un objet et utilisé toString mais il faudrait le définir
	 * pour chaque regle.
	 * Remarque: Sa serait pas long.
	 */
	private String name; 
	
	// CONSTRUCTEUR
	protected Report(String ruleName) {
		Contract.checkCondition(ruleName != null);
		name = ruleName;
	}
	
	//REQUETES
	public String getRuleName() {
		return name;
	}
	
	public abstract String describe();
	
	//COMMANDES
	public abstract void execute();
	
}