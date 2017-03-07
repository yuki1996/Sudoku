package sudoku.model.heuristic;

import java.util.Iterator;

import sudoku.model.CellModel;
import sudoku.model.GridModel;
import util.Contract;

public class SetValueReport extends Report {

	public SetValueReport() {
		super();
	}
	
	public void execute(GridModel grid) {
		Contract.checkCondition(grid != null);
		Iterator<CellModel> it = cellSets.get(CellSetName.DECISIVE_CELLS).iterator();
		CellModel c = it.next();
		grid.setValue(c, values.iterator().next());
	}
}
