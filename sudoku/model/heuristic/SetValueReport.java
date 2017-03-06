package sudoku.model.heuristic;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sudoku.model.GridModel;
import sudoku.model.history.cmd.AddValue;
import sudoku.model.history.cmd.Command;
import sudoku.util.ICoord;

abstract class SetValueReport implements Report {
	//ATRIBUTS
	private Set<ICoord> decisiveUnits;
	private ICoord changedCoord;
	private int value;
	private GridModel grid;
	
	//CONSTRUCTEURS
	protected SetValueReport(GridModel grid, ICoord changedCoord, int value) {
		decisiveUnits = new HashSet<ICoord>();
		this.changedCoord = changedCoord;
		this.value = value;
		this.grid = grid;
	}
	
	//REQUETES
	@Override
	abstract public String describe();
	
	public int getValue() {
		return value;
	}
	
	public ICoord changedCell() {
		return changedCoord;
	}
	
	public Set<ICoord> decisiveUnits() {
		return decisiveUnits;
	}
	
	//COMMANDES
	public void addDecisiveUnits(ICoord coord) {
		decisiveUnits.add(coord);
	}
	
	public void setDecisiveUnits(Set<ICoord> newSet) {
		decisiveUnits = newSet;
	}

	@Override
	public Map<CellSetName, Set<ICoord>> importantSets() {
		Map<CellSetName, Set<ICoord>> cellSets = 
				new EnumMap<CellSetName, Set<ICoord>>(CellSetName.class);
		Set<ICoord> decisiveCells = new HashSet<ICoord>();
		decisiveCells.add(changedCoord);
		cellSets.put(CellSetName.DECISIVE_CELLS, decisiveCells);
		cellSets.put(CellSetName.DECISIVE_UNITS, new HashSet<ICoord>(decisiveUnits));
		cellSets.put(CellSetName.DELETION_CELLS, new HashSet<ICoord>());
		cellSets.put(CellSetName.DELETION_UNITS, new HashSet<ICoord>());
		return cellSets;
	}

	@Override
	public Command getCommand() {
		return new AddValue(grid, changedCoord, value);
	}

}
