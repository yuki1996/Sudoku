package sudoku.model.heuristic;

import sudoku.model.IGrid;
import util.Contract;

public class RemoveReport extends Report {

	public RemoveReport() {
		super();
	}

	public void execute(IGrid grid) {
		Contract.checkCondition(grid != null);
		for (int i = 0; i < grid.size(); i++) {
			for (int j = 0; j < grid.size(); j++) {
				if (cellSets.get(CellSetName.DELETION_CELLS).contains(grid.cells()))  {
					for (Integer v : values) {
						grid.cells()[i][j].removeCandidate(v);
					}
				}
			}
		}
	}

}
