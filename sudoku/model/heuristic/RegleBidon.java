package sudoku.model.heuristic;

import sudoku.model.IGrid;

class RegleBidon extends Report {
	
	RegleBidon(Rule rule) {
		super(rule);
	}
	
	class RegleBidonGenerator extends ReportGenerator {
		protected Report generate(IGrid grid) {
			return null;
		}
	}
}