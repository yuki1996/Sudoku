package sudoku.model.heuristic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sudoku.model.GridModel;
import sudoku.model.heuristic.Report.CellSetName;
import sudoku.util.Couple;
import sudoku.util.ICoord;

public class RuleMixedGroups extends ReportGenerator {
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
		Couple<Set<Integer>, List<ICoord>> couple = getValidGroup(grid, new HashSet<Integer>(), 
				listArg, new ArrayList<ICoord>(), new ArrayList<ICoord>(), novalueCellNb);
		if (couple == null) {
			return null;
		}
		
		RemoveCandidateReport res = new RemoveCandidateReport(grid);
		res.setValues(couple.getFirst());
		res.setCellSet(CellSetName.DELETION_UNITS, unit);
		Set<ICoord> decisiveCells = new HashSet<ICoord>(listArg);
		decisiveCells.removeAll(couple.getSecond());
		res.setCellSet(CellSetName.DECISIVE_CELLS, decisiveCells);
		
		String str = "Comme ces cellules";
		for (ICoord coord : couple.getSecond()) {
			str += " L" + coord.getRow() + "C" + coord.getCol();
			res.addCell(CellSetName.DELETION_CELLS, coord);
		}
		
		Set<Integer> keepCandidates = new HashSet<Integer>();
		for (ICoord coord : couple.getSecond()) {
			for (int i = 1; i <= grid.numberCandidates(); ++i) {
				if (grid.getCell(coord).isCandidate(i) && ! couple.getFirst().contains(i)) {
					keepCandidates.add(i);
				}
			}
		}
		str += " sont les seules à avoir les candidats ";
		for (Integer j : keepCandidates) {
			str += " " + j.toString();
		}
		
		str += ",\non peut leur retirer leurs autres candidats.";
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
			List<ICoord> lbase, List<ICoord> lexc, List<ICoord> lrest, int novalueCellNb) {
		int setSize = set.size();
		int excSize = lexc.size();
		
		if (novalueCellNb == setSize) {
			return null;
		}
		
		if (setSize == excSize && excSize != 0) {
			List<ICoord> lres = new ArrayList<ICoord>(lbase);
			lres.addAll(lrest);
			for (ICoord c : lres) {
				for (Integer i : set) {
					if (g.getCell(c).isCandidate(i)) {
						return new Couple<Set<Integer>, List<ICoord>>(set, lres);
					}
				}
			}
		}
		
		List<ICoord> newLbase = new ArrayList<ICoord>(lbase);
		for (ICoord c : lbase) {
			Set<Integer> newSet = new HashSet<Integer>(set);
			
			for (int j = 1; j <= g.numberCandidates(); ++j) {
				if (g.getCell(c).isCandidate(j)) {
					newSet.add(j);
				}
			}
			newLbase.remove(c);
			lexc.add(c);
			Couple<Set<Integer>, List<ICoord>> res = getValidGroup(g, newSet, newLbase, 
					lexc, new ArrayList<ICoord>(lrest), novalueCellNb);
			if (res != null) {
				return res;
			}
			lexc.remove(c);
			lrest.add(c);
			
		}
		return null;
	}
}

