package sudoku.model.heuristic;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sudoku.model.ICell;
import sudoku.model.IGrid;
import util.Contract;

public class Report {

	private String description;
	private Map<CellSetName, Set<ICell>> cellSets;
	private Set<Integer> values;

	// pas nécessaire de le mettre ici : à voir
	public enum CellSetName {
		DECISIVE_CELLS,
		DECISIVE_UNITS,
		DELETION_CELLS,
		DELETION_UNITS;
	}
	
	// CONSTRUCTEUR
	protected Report() {
		description = null;
		cellSets = new EnumMap<CellSetName, Set<ICell>>(CellSetName.class);
		values = new HashSet<Integer>();
	}
	
	public Set<Integer> getValueSet() {
		return new HashSet<Integer>(values);
	}
	
	public Set<ICell> getCellSet(CellSetName csn) {
		return new HashSet<ICell>(cellSets.get(csn));
	}
	
//	public String describe() {
//		return rule.getGenerator().describe(this);
//	}

	public void execute() {
		// à voir
	}
	
	void addCell(CellSetName csn, ICell cell) {
		
	}
	
	void addValue(int n) {
		values.add(n);
	}
	
}