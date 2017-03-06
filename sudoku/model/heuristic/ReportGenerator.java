package sudoku.model.heuristic;

import sudoku.model.GridModel;

abstract class ReportGenerator {
	
	
	// génération des Report
	protected abstract Report generate(GridModel grid);
	
}
