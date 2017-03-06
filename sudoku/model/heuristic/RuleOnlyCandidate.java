package sudoku.model.heuristic;

import sudoku.model.CellModel;
import sudoku.model.GridModel;
import sudoku.model.heuristic.Report.CellSetName;
import util.Contract;

public class RuleOnlyCandidate extends ReportGenerator {

	@Override
	protected Report generate(GridModel grid) {
		Contract.checkCondition(grid != null);
		CellModel [][] tabC = grid.cells();
		Contract.checkCondition(tabC != null);
		Report r = new SetValueReport();
		int l = -1;
		CellModel c;
		for (int i = 0; i < grid.size(); i++) {
			for (int j = 0; j < grid.size(); j++) {
				int k = 0;
				c = tabC[i][j];
				if (c.isModifiable() && ! c.hasValue()) {
					//parcourt tableau des candidats de la cellule
					for (int m = 0; m < grid.numberCandidates(); m++) {
						if (c.isCandidate(m)) {
							++k;
							l = m + 1;
						}
						if (k > 1) {
							l = -1;
							break;
						}
					}
				}
				if (k == 1) {
					r.addCell(CellSetName.DECISIVE_CELLS, c);
					r.addValue(l);
					String string = "Cette case contient un seul candidat avec le symbole " + l;
					r.setDescription(string);
					return r;
				}
			}
		}
		return null;
	}

}
