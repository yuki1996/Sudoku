package sudoku.model.heuristic;

import sudoku.model.ICell;
import sudoku.model.IGrid;
import sudoku.model.heuristic.Report.CellSetName;
import util.Contract;

public class RuleOnlyCandidate extends ReportGenerator {

	@Override
	protected Report generate(IGrid grid) {
		Contract.checkCondition(grid != null);
		ICell [][] tabC = grid.cells();
		Contract.checkCondition(tabC != null);
		Report r = new SetValueReport();
		int l = -1;
		ICell c;
		for (int i = 0; i < grid.size(); i++) {
			for (int j = 0; j < grid.size(); j++) {
				int k = 0;
				c = tabC[i][j];
				if (c.isModifiable() && ! c.hasValue()) {
					boolean[] tabB = c.candidates();
					//parcourt tableau des candidats de la cellule
					for (int m = 0; m < tabB.length; m++) {
						if (tabB[m]) {
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
