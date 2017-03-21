package sudoku.model.heuristic;

import java.util.ArrayList;
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
	private Report getReport(GridModel grid, Set<ICoord> unit) {
		int novalueCellNb = 0;
		List<ICoord> listArg = new ArrayList<ICoord>();
		for (ICoord c : unit) {
			if (! grid.getCell(c).hasValue()) {
				++novalueCellNb;
				listArg.add(c);
			}
		}
		Set<ICoord> possiblyChangedCells = new HashSet<ICoord>(listArg);
		Couple<Set<Integer>, List<ICoord>> couple = getValidIsolatedGroup(grid, new HashSet<Integer>(), 
				listArg, new ArrayList<ICoord>(), novalueCellNb, new ArrayList<ICoord>());
		if (couple == null) {
			return null;
		}
		RemoveCandidateReport res = new RemoveCandidateReport(grid);
		String str = "Les cellules";
		for (ICoord coord : couple.getSecond()) {
			str += " L" + coord.getRow() + "C" + coord.getCol();
			res.setCellSet(CellSetName.DECISIVE_CELLS, unit);
		}
		str += " n'ont pas d'autres candidats que:";
		for (Integer j : couple.getFirst()) {
			res.addValue(j);
			str += " " + j.toString();
		}
		res.setCellSet(CellSetName.DELETION_UNITS, unit);
		possiblyChangedCells.removeAll(couple.getSecond());
		Set<ICoord> changedCells = new HashSet<ICoord>();
		for (ICoord c : possiblyChangedCells) {
			for (Integer i : couple.getFirst()) {
				if (grid.getCell(c).isCandidate(i)) {
					changedCells.add(c);
					break;
				}
			}
		}
		res.setCellSet(CellSetName.DELETION_CELLS, changedCells);
		
		str += ".\nOn peut donc retirer ces candidats des cellules";
		for (ICoord coord : changedCells) {
			if (! grid.getCell(coord).hasValue()) {
				str += " L" + coord.getRow() + "C" + coord.getCol();
			}
		}
		str += ".";
		res.setDescription(str);
		return res;
	}
	
	
	private Couple<Set<Integer>, List<ICoord>> getValidIsolatedGroup(GridModel g, Set<Integer> set, 
			List<ICoord> l, List<ICoord> lres, int novalueCellNb, List<ICoord> lrest) {
		int setSize = set.size();
		int resSize = lres.size();
		if (setSize >= novalueCellNb || resSize >= novalueCellNb) {
			return null;
		}
		if (setSize == resSize) {
			List<ICoord> test = new ArrayList<ICoord>(l);
			test.addAll(lrest);
			for (ICoord c : test) {
				for (Integer i : set) {
					if (g.getCell(c).isCandidate(i)) {
						return new Couple<Set<Integer>, List<ICoord>>(set, lres);
					}
				}
			}
		}
		List<ICoord> list = new ArrayList<ICoord>(l);
		for (ICoord c : l) {
			Set<Integer> newSet = new HashSet<Integer>(set);
			for (int j = 1; j <= g.numberCandidates(); ++j) {
				if (g.getCell(c).isCandidate(j)) {
					newSet.add(j);
				}
			}
			list.remove(c);
			lres.add(c);
			Couple<Set<Integer>, List<ICoord>> res = getValidIsolatedGroup(g, newSet, list, lres, novalueCellNb, lrest);
			if (res != null) {
				return res;
			}
			lres.remove(c);
			lrest.add(c);
			
		}
		return null;
	}
}
