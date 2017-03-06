package sudoku.model.heuristic;

import sudoku.model.CellModel;
import sudoku.model.GridModel;
import sudoku.model.heuristic.Report.CellSetName;

class BruteForce extends ReportGenerator {

	@Override
	protected Report generate(GridModel grid) {
		for (CellModel[] unit : grid.cells()) {
			for (CellModel cell : unit) {
				if (! cell.hasValue()) {
					for (int i = 1; i <= cell.getCardinalCandidates(); ++i) {
						if (cell.canTakeValue(i)) {
							cell.setValue(i);
							Report report = generate(grid);
							if (report == null) {
								cell.removeValue();
							} else {
								report.addCell(CellSetName.DECISIVE_CELLS, cell);
								return report;
							}
						}
					}
					return null;
				}
			}
		}
		
		return new Report();
	}
	
}