package sudoku.rules;


import java.util.HashSet;
import java.util.Set;
import sudoku.model.ICell;
import util.Contract;

public class Report {
	
	//ATTRIBUTS
	private Rules rule;
	private Set<ICell> cellsDecisives;
	private Set<ICell> cellsContextuals;
	private Set<ICell> cellsDeletions;
	private Set<ICell> cellsActualDeletions;
	private Set<Integer> values;
	
	//CONSTRUCTEURS
	public Report() {
		rule = null;
		cellsDecisives = new HashSet<ICell>();
		cellsContextuals = new HashSet<ICell>();
		cellsDeletions = new HashSet<ICell>();
		cellsActualDeletions = new HashSet<ICell>();
		values = new HashSet<Integer>();
	}
	//REQUÊTE
	
	public String describe() {
		return rule.describe(this);
	}
	
	//COMMANDE
	
	public void execute() {
		rule.execute(this);
	}

	public void setRule(Rules r) {
		Contract.checkCondition(r != null);
		rule = r;
		cellsDecisives = new HashSet<ICell>();
		cellsContextuals = new HashSet<ICell>();
		cellsDeletions = new HashSet<ICell>();
		cellsActualDeletions = new HashSet<ICell>();
		values = new HashSet<Integer>();
	}
}
