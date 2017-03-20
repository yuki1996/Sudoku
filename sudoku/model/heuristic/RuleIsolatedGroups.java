package sudoku.model.heuristic;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sudoku.model.GridModel;
import sudoku.model.heuristic.Report.CellSetName;
import sudoku.util.Couple;
import sudoku.util.ICoord;

public class RuleIsolatedGroups extends ReportGenerator {
	//COMMANDES
	@Override
	protected Report generate(GridModel grid) {
		Report res;
		for (int i = 0; i < grid.size(); ++i) {
			res = getReport(grid, grid.getCol(i));
			if (res != null) {
				return res;
			}
		}
		for (int i = 0; i < grid.size(); ++i) {
			res = getReport(grid, grid.getRow(i));
			if (res != null) {
				return res;
			}
		}
		for (int i = 0; i < grid.getNumberSectorByWidth(); ++i) {
			for (int j = 0; j < grid.getNumberSectorByHeight(); ++j) {
				res = getReport(grid, grid.getSector(i, j));
				if (res != null) {
					return res;
				}
			}
		}
		return null;
	}
	
	//OUTILS
	private Report getReport(GridModel grid, Set<ICoord> Unit) {
		Set<Integer> candidates = new HashSet<Integer>();
		for(int i = 1; i <= grid.size(); ++i) {
			candidates.add(i);
		}
		GraphIG graph = new GraphIG(candidates);
		for (ICoord c : Unit) {
			if (! grid.getCell(c).hasValue()) {
				graph.add(c, grid);
			}
		}
		for (Couple<Set<Integer>, List<ICoord>> couple : graph.getGoodGroup()) {
			Set<ICoord> possibleChangedCells = Unit;
			possibleChangedCells.removeAll(couple.getSecond());
			Set<ICoord> changedCells = new HashSet<ICoord>();
			
			boolean test = false;
			for (ICoord coord : possibleChangedCells) {
				boolean test2 = false;
				if (! grid.getCell(coord).hasValue()) {
					for (Integer i : couple.getFirst()) {
						if (grid.getCell(coord).isCandidate(i)) {
							test = true;
							test2 = true;
							break;
						}
					}
				}
				if (test2) {
					changedCells.add(coord);
				}
			}
			
			if (test) {
				RemoveCandidateReport res = new RemoveCandidateReport(grid);
				
				String str = "Les cellules";
				for (ICoord coord : couple.getSecond()) {
					str += " L" + coord.getRow() + "C" + coord.getCol();
				}
				str += " n'ont pas d'autres candidats que:";
				for (Integer j : couple.getFirst()) {
					res.addValue(j);
					str += " " + j.toString();
				}
				res.setCellSet(CellSetName.DELETION_UNITS, Unit);
				res.setCellSet(CellSetName.DELETION_CELLS, changedCells);
				
				str += ".\nOn peut donc retirer ces candidats des cellules:";
				for (ICoord coord : changedCells) {
					if (! grid.getCell(coord).hasValue()) {
						str += " L" + coord.getRow() + "C" + coord.getCol();
					}
				}
				str += ".";
				res.setDescription(str);
				return res;
			}
		}
		return null;
	}
}
