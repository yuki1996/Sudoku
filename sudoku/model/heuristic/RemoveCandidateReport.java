package sudoku.model.heuristic;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sudoku.model.GridModel;
import sudoku.model.history.cmd.Command;
import sudoku.model.history.cmd.CommandSet;
import sudoku.model.history.cmd.RemoveCandidate;
import sudoku.util.ICoord;
import util.Contract;

class RemoveCandidateReport implements Report {
	
	//ATRIBUTS
	private Map<CellSetName, Set<ICoord>> cellSets;
	private Set<Integer> values;
	private GridModel grid;
	private String description;
	
	// CONSTRUCTEUR
	RemoveCandidateReport(GridModel grid) {
		cellSets = new EnumMap<CellSetName, Set<ICoord>>(CellSetName.class);
		for (CellSetName csn : CellSetName.values()) {
			cellSets.put(csn, new HashSet<ICoord>());
		}
		values = new HashSet<Integer>();
		this.grid = grid;
	}
	
	//REQUETES
	public String describe() {
		return description;
	}
	
	public Set<Integer> getValueSet() {
		return new HashSet<Integer>(values);
	}
	
	public Set<ICoord> getCellSet(CellSetName csn) {
		return new HashSet<ICoord>(cellSets.get(csn));
	}

	//COMMANDES
	public void setDescription(String s) {
		Contract.checkCondition(s != null, "s vaut null");
		description = s;
	}
	
	public void addCell(CellSetName csn, ICoord cell) {
		cellSets.get(csn).add(cell);
	}
	
	public void addValue(int n) {
		values.add(n);
	}
	
	public void setCellSet(CellSetName csn, Set<ICoord> newSet) {
		cellSets.put(csn, newSet);
	}

	@Override
	public Map<sudoku.model.heuristic.Report.CellSetName, Set<ICoord>> importantSets() {
		return cellSets;
	}

	@Override
	public Command generateCommand() {
		Set<Command> set = new HashSet<Command>();
		for (ICoord c : cellSets.get(CellSetName.DELETION_CELLS)) {
			for (Integer v : values) {
				if (grid.getCell(c).isCandidate(v)) {
					set.add(new RemoveCandidate(grid, c, v));
				}
			}
		}
		return new CommandSet(grid, set);
	}
}
