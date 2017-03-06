package sudoku.model.heuristic;

import java.util.HashSet;
import java.util.Set;

import sudoku.model.CellModel;
import util.Contract;

public class SetValueReport extends Report {
	//ATRIBUTS
	private Set<CellModel> decisiveUnits;
	private CellModel changedCell;
	private int value;
	
	//CONSTRUCTEURS
	protected SetValueReport(String ruleName, CellModel changedCell, int value) {
		super(ruleName);
		Contract.checkCondition(changedCell.isCandidate(value), 
				value + " n'est pas un candidats valable");
		decisiveUnits = new HashSet<CellModel>();
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
	
	CellModel changedCell() {
		return changedCell;
	}
	
	Set<CellModel> decisiveUnits() {
		return decisiveUnits;
	}
	
	//COMMANDES
	@Override
	public void execute() {
		changedCell.setValue(value);
	}
	
	void addDecisiveUnits(CellModel cell) {
		decisiveUnits.add(cell);
	}
	
	void setDecisiveUnits(Set<CellModel> newSet) {
		decisiveUnits = newSet;
	}

}
