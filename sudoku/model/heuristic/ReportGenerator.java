package sudoku.model.heuristic;

import sudoku.model.IGrid;

abstract class ReportGenerator {
	
	
	// génération des Report
	protected abstract Report generate(IGrid grid);
	
}
