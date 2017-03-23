package sudoku.model.heuristic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sudoku.model.GridModel;
import sudoku.model.heuristic.Report.CellSetName;
import sudoku.util.Coord;
import sudoku.util.Couple;
import sudoku.util.ICoord;

public class RuleIsolatedGroups extends ReportGenerator {
	//COMMANDES
	@Override
	protected Report generate(GridModel grid) {
		for (int i = 0; i < grid.size(); ++i) {
			Report res = getReport(grid, grid.getCol(i));
			if (res != null) {
				return res;
			}
		}
		for (int i = 0; i < grid.size(); ++i) {
			Report res = getReport(grid, grid.getRow(i));
			if (res != null) {
				return res;
			}
		}
		for (int i = 0; i < grid.getNumberSectorByWidth(); ++i) {
			for (int j = 0; j < grid.getNumberSectorByHeight(); ++j) {
				Report res = getReport(grid, grid.getSector(i, j));
				if (res != null) {
					return res;
				}
			}
		}
		return null;
	}
	
	//OUTILS
	/*
	 * Renvoie un rapport pour la régle groupe isolé pour l'unité unit de grid ou null s'il on ne peut pas
	 * appliquer cette régle à cette unité.
	 */
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
		Couple<Set<Integer>, List<ICoord>> couple = getValidGroup(grid, new HashSet<Integer>(), 
				listArg, new ArrayList<ICoord>(), novalueCellNb, new ArrayList<ICoord>());
		if (couple == null) {
			return null;
		}
		RemoveCandidateReport res = new RemoveCandidateReport(grid);
		res.setCellSet(CellSetName.DELETION_UNITS, unit);
		res.setCellSet(CellSetName.DECISIVE_UNITS, unit);
		String str = "Les cellules";
		for (ICoord coord : couple.getSecond()) {
			str += " L" + coord.getRow() + "C" + coord.getCol();
			res.addCell(CellSetName.DECISIVE_CELLS, coord);
		}
		str += " n'ont pas d'autres candidats que:";
		for (Integer j : couple.getFirst()) {
			res.addValue(j);
			str += " " + j.toString();
		}
		
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
	
	/*
	 * Renvoie un couple d'un ensemble de candidat et d'une liste de coordonnées tel que chaques cellules de g
	 * qui sont désigné par une coordonnée de la liste ne possédent que des candidats contenu dans l'ensemble 
	 * de candidats et qu'il y ai autant de coordonnées dans la liste qu'il n'y a de candidats dans l'ensemble
	 * De plus, la liste et l'ensemble ont une taille strictement inférieure a novalueCellNb.
	 */
	private Couple<Set<Integer>, List<ICoord>> getValidGroup(GridModel g, Set<Integer> set, 
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
			List<ICoord> newlrest = new ArrayList<ICoord>(lrest);
			for (int j = 1; j <= g.numberCandidates(); ++j) {
				if (g.getCell(c).isCandidate(j)) {
					newSet.add(j);
				}
			}
			list.remove(c);
			lres.add(c);
			Couple<Set<Integer>, List<ICoord>> res = getValidGroup(g, newSet, list, lres, novalueCellNb, newlrest);
			if (res != null) {
				return res;
			}
			lres.remove(c);
			newlrest.add(c);
			
		}
		return null;
	}
}
