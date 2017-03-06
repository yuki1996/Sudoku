package sudoku.model.heuristic;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import sudoku.model.ICell;
import util.Contract;

public class BruteForceReport extends Report {
	//ATRIBUTS
	private Map<ICell, Integer> map;
	
	//CONSTRUCTEURS
	protected BruteForceReport(String ruleName) {
		super(ruleName);
		map = new HashMap<ICell, Integer>();
	}
	
	//REQUETES
	@Override
	public String describe() {
		/*
		 * besoin d'un nom pour les cellules
		 */
		return null;
	}
	
	Map<ICell, Integer> getValue() {
		return map;
	}
	
	//COMMANDES
	@Override
	public void execute() {
		for(Entry<ICell, Integer> entry : map.entrySet()) {
			entry.getKey().setValue(entry.getValue());
		}
	}
	
	void addCellValue(ICell cell, Integer value) {
		Contract.checkCondition(cell.canTakeValue(value), 
				value + " n'est pas un candidats valable");
		map.put(cell, value);
	}
	
	void setDecisiveUnits(Map<ICell, Integer> newMap) {
		for(Entry<ICell, Integer> entry : map.entrySet()) {
			Contract.checkCondition(entry.getKey().canTakeValue(entry.getValue()), 
					entry.getValue() + " n'est pas un candidats valable");
		}
		map = newMap;
	}
}
