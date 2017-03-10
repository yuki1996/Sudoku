package sudoku.model.heuristic;

import sudoku.model.CellModel;
import sudoku.model.GridModel;
import sudoku.util.Coord;
import sudoku.util.ICoord;
import util.Contract;

public class RuleOnlyCandidate extends ReportGenerator {

	@Override
	protected Report generate(GridModel grid) {
		Contract.checkCondition(grid != null);
		CellModel [][] tabC = grid.cells();
		Contract.checkCondition(tabC != null);
		int l = -1;
		for (int i = 0; i < grid.size(); i++) {
			for (int j = 0; j < grid.size(); j++) {
				int k = 0;
				ICoord c = new Coord(i, j);
				if (grid.getCell(c).isModifiable() && ! grid.getCell(c).hasValue()) {
					//parcourt tableau des candidats de la cellule
					for (int m = 1; m <= grid.numberCandidates(); m++) {
						if (grid.getCell(c).isCandidate(m)) {
							++k;
							l = m;
						}
						if (k > 1) {
							l = -1;
							break;
						}
					}
				}
				if (k == 1) {
					SetValueReport r = new SetValueReport(grid, c, l);
					String string = "Cette case contient un seul candidat avec le symbole " + l;
					r.setDescription(string);
					return r;
				}
			}
		}
		return null;
	}

}
