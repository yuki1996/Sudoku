package sudoku.model.heuristic;

import java.util.Iterator;

import sudoku.model.ICell;
import sudoku.model.IGrid;
import sudoku.util.Coord;
import util.Contract;

public class SetValueReport extends Report {

	public SetValueReport() {
		super();
	}
	
	public void execute(IGrid grid) {
		Contract.checkCondition(grid != null);
		Iterator<ICell> it = cellSets.get(CellSetName.DECISIVE_CELLS).iterator();
		ICell c = it.next();
		grid.SetValue(c, values.iterator().next());
	}
}
