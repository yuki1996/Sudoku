package sudoku.model.heuristic;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sudoku.model.ICell;
import sudoku.model.IGrid;
import util.Contract;

public abstract class Report {

	private String description;
	protected Map<CellSetName, Set<ICell>> cellSets;
	protected Set<Integer> values;

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
		cellSets.put(CellSetName.DECISIVE_CELLS, new HashSet<ICell>());
		cellSets.put(CellSetName.DECISIVE_UNITS, new HashSet<ICell>());
		cellSets.put(CellSetName.DELETION_CELLS, new HashSet<ICell>());
		cellSets.put(CellSetName.DELETION_UNITS, new HashSet<ICell>());	
		values = new HashSet<Integer>();
	}
	
	public Set<Integer> getValueSet() {
		return new HashSet<Integer>(values);
	}
	
	public Set<ICell> getCellSet(CellSetName csn) {
		return new HashSet<ICell>(cellSets.get(csn));
	}
	
	public String describe() {
		return description;
	}

	public abstract void execute(IGrid grid);
	
	void addCell(CellSetName csn, ICell cell) {
		Contract.checkCondition(cell != null);
		cellSets.get(csn).add(cell);
	}
	
	void setDescription(String describe) {
		description = describe;
	}
	
	void addValue(int n) {
		values.add(n);
	}
	
}