package sudoku.model.heuristic;

import java.util.HashSet;
import java.util.Set;

import sudoku.model.ICell;
import util.Contract;

public class SetValueReport extends Report {
	//ATRIBUTS
	private Set<ICell> decisiveUnits;
	private ICell changedCell;
	private int value;
	
	//CONSTRUCTEURS
	protected SetValueReport(String ruleName, ICell changedCell, int value) {
		super(ruleName);
		Contract.checkCondition(changedCell.canTakeValue(value), 
				value + " n'est pas un candidats valable");
		decisiveUnits = new HashSet<ICell>();
		this.changedCell = changedCell;
		this.value = value;
	}
	
	//REQUETES
	@Override
	public String describe() {
		/*
		 * besoin d'un nom pour la cellule
		 */
		return null;
	}
	
	int getValue() {
		return value;
	}
	
	ICell changedCell() {
		return changedCell;
	}
	
	Set<ICell> decisiveUnits() {
		return decisiveUnits;
	}
	
	//COMMANDES
	@Override
	public void execute() {
		changedCell.setValue(value);
	}
	
	void addDecisiveUnits(ICell cell) {
		decisiveUnits.add(cell);
	}
	
	void setDecisiveUnits(Set<ICell> newSet) {
		decisiveUnits = newSet;
	}

}
