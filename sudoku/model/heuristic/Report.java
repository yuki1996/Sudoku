package sudoku.model.heuristic;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sudoku.model.CellModel;
import sudoku.model.GridModel;
import util.Contract;

public abstract class Report {

	private String description;
	protected Map<CellSetName, Set<CellModel>> cellSets;
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
		cellSets = new EnumMap<CellSetName, Set<CellModel>>(CellSetName.class);
		cellSets.put(CellSetName.DECISIVE_CELLS, new HashSet<CellModel>());
		cellSets.put(CellSetName.DECISIVE_UNITS, new HashSet<CellModel>());
		cellSets.put(CellSetName.DELETION_CELLS, new HashSet<CellModel>());
		cellSets.put(CellSetName.DELETION_UNITS, new HashSet<CellModel>());	
		values = new HashSet<Integer>();
	}
	
	public Set<Integer> getValueSet() {
		return new HashSet<Integer>(values);
	}
	
	public Set<CellModel> getCellSet(CellSetName csn) {
		return new HashSet<CellModel>(cellSets.get(csn));
	}
	
	public String describe() {
		return description;
	}

	public abstract void execute(GridModel grid);
	
	void addCell(CellSetName csn, CellModel cell) {
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