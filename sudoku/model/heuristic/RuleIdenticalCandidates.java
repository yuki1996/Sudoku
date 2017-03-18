package sudoku.model.heuristic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import sudoku.model.CellModel;
import sudoku.model.GridModel;
import sudoku.model.heuristic.Report;
import sudoku.model.heuristic.Report.CellSetName;
import sudoku.util.Coord;
import sudoku.util.ICoord;
import util.Contract;

public class RuleIdenticalCandidates extends ReportGenerator {

	@Override
	protected Report generate(GridModel grid) {
		Contract.checkCondition(grid != null);
		CellModel [][] tabC = grid.cells();
		Contract.checkCondition(tabC != null);
		CellModel c = null;
		List<CoupleIntergerCoord> listCouple = new LinkedList<CoupleIntergerCoord>();
		Boolean flag = false;
		int min = 0;
		Set<ICoord> setCoord = new HashSet<ICoord>();
		List<Integer> listInteger = new LinkedList<Integer>();
		
		//on regarde ligne par ligne
		for (int i = 0; i < grid.size(); i++) {
			for (int j = 0; j < grid.size(); j++) {
				c = tabC[i][j];
				//cellule modifiable sans valeur
				if (!c.hasValue()) {
					List<Integer> listCandidateOfCell = getListCandidate(c);
					for (CoupleIntergerCoord couple : listCouple) {
						List<Integer> list = couple.getListInteger();
						if ((list.size() == listCandidateOfCell.size()) && list.containsAll(listCandidateOfCell)) {
							couple.getListCoord().add(new Coord(i, j));
							flag = true;
							break;
						}
					}
					if (!flag) {
						List<ICoord> list = new ArrayList<ICoord>();
						list.add(new Coord(i, j));
						listCouple.add(new CoupleIntergerCoord(listCandidateOfCell, list));
					}
					flag = false;
					min += 1;
				}
			}
			// A la fin de chaque ligne 
			for (CoupleIntergerCoord couple : listCouple) {
				int size = couple.getListInteger().size();
				if (min > size) {
					min = size;
					setCoord.clear();
					for (ICoord coord : couple.getListCoord()) {
						setCoord.add(coord);
					}
					listInteger = couple.getListInteger();
				}
			}
			if (setCoord.size() > 1) {
				RemoveCandidateReport r = new RemoveCandidateReport(grid);
				for (int v : listInteger) {
					r.addValue(v);
				}
				r.setCellSet(CellSetName.DECISIVE_CELLS, setCoord);
				for (int j = 0; j < grid.size(); j++) {
					Coord coord = new Coord(i, j);
					if (!setCoord.contains(coord)) {
						if (!grid.getCell(coord).hasValue()) { 
							r.addCell(CellSetName.DELETION_CELLS, coord);
						} else {
							r.addCell(CellSetName.DELETION_UNITS, coord);
						}
					}
				}
				if (r.getCellSet(CellSetName.DELETION_CELLS).size() != 0) {
					String s = "Les " + min + " candidats { ";
					for (int v : listInteger) {
						s += String.valueOf(v) + " ";
					}
					s += "} sont présents tous les " + min + " dans les "+ min +" de la même"
						+ " ligne, il est donc possible de les supprimer des autres cases";
					r.setDescription(s);
					return r;
				}
				r = null;
			} else {
				setCoord.clear();
				min = 0;
				listCouple.clear();
			}
		}
		
		//on regarde colonne par colonne
		for (int i = 0; i < grid.size(); i++) {
			for (int j = 0; j < grid.size(); j++) {
				c = tabC[j][i];
				//cellule modifiable sans valeur
				if (!c.hasValue()) {
					List<Integer> listCandidateOfCell = getListCandidate(c);
					for (CoupleIntergerCoord couple : listCouple) {
						List<Integer> list = couple.getListInteger();
						if ((list.size() == listCandidateOfCell.size()) && list.containsAll(listCandidateOfCell)) {
							couple.getListCoord().add(new Coord(j, i));
							flag = true;
							break;
						}
					}
					if (!flag) {
						List<ICoord> list = new ArrayList<ICoord>();
						list.add(new Coord(j, i));
						listCouple.add(new CoupleIntergerCoord(listCandidateOfCell, list));
					}
					flag = false;
					min += 1;
				}
			}
			// A la fin de chaque ligne 
			for (CoupleIntergerCoord couple : listCouple) {
				int size = couple.getListInteger().size();
				if (min > size) {
					min = size;
					setCoord.clear();
					for (ICoord coord : couple.getListCoord()) {
						setCoord.add(coord);
					}
					listInteger = couple.getListInteger();
				}
			}
			if (setCoord.size() > 1) {
				RemoveCandidateReport r = new RemoveCandidateReport(grid);
				for (int v : listInteger) {
					r.addValue(v);
				}
				r.setCellSet(CellSetName.DECISIVE_CELLS, setCoord);
				for (int j = 0; j < grid.size(); j++) {
					Coord coord = new Coord(j, i);
					if (!setCoord.contains(coord)) {
						if (!grid.getCell(coord).hasValue()) { 
							r.addCell(CellSetName.DELETION_CELLS, coord);
						} else {
							r.addCell(CellSetName.DELETION_UNITS, coord);
						}
					}
				}
				if (r.getCellSet(CellSetName.DELETION_CELLS).size() != 0) {
					String s = "Les " + min + " candidats { ";
					for (int v : listInteger) {
						s += String.valueOf(v) + " ";
					}
					s += "} sont présents tous les " + min + " dans les "+ min +" de la même"
						+ " colonne, il est donc possible de les supprimer des autres cases";
					r.setDescription(s);
					return r;
				}
				r = null;
			} else {
				setCoord.clear();
				min = 0;
				listCouple.clear();
			}
		}
		
		//on regarde région par région
		int nbSW = grid.getNumberSectorByWidth();
		int nbSH = grid.getNumberSectorByHeight();
		for (int i = 0; i < grid.getWidthSector(); i++) {
			for (int j = 0; j < grid.getHeightSector(); j++) {
				for (int m = i * nbSW; m < grid.getHeightSector() * (i + 1); m++) {
					for (int n = j * nbSH; n < grid.getWidthSector() * (j + 1); n++) {
						c = tabC[m][n];
						//cellule modifiable sans valeur
						if (!c.hasValue()) {
							List<Integer> listCandidateOfCell = getListCandidate(c);
							for (CoupleIntergerCoord couple : listCouple) {
								List<Integer> list = couple.getListInteger();
								if ((list.size() == listCandidateOfCell.size()) && list.containsAll(listCandidateOfCell)) {
									couple.getListCoord().add(new Coord(m, n));
									flag = true;
									break;
								}
							}
							if (!flag) {
								List<ICoord> list = new ArrayList<ICoord>();
								list.add(new Coord(m, n));
								listCouple.add(new CoupleIntergerCoord(listCandidateOfCell, list));
							}
							flag = false;
							min += 1;
						}
					}
				}
				// A la fin de chaque région 
				for (CoupleIntergerCoord couple : listCouple) {
					int size = couple.getListInteger().size();
					if (min > size) {
						min = size;
						setCoord.clear();
						for (ICoord coord : couple.getListCoord()) {
							setCoord.add(coord);
						}
						listInteger = couple.getListInteger();
					}
				}
				if (setCoord.size() > 1) {
					RemoveCandidateReport r = new RemoveCandidateReport(grid);
					for (int v : listInteger) {
						r.addValue(v);
					}
					r.setCellSet(CellSetName.DECISIVE_CELLS, setCoord);

					Coord coord = (Coord) setCoord.iterator().next();
					for (ICoord cd : grid.getSector(coord)) {
						if (!setCoord.contains(cd)) {
							if (!grid.getCell(cd).hasValue()) { 
								r.addCell(CellSetName.DELETION_CELLS, cd);
							} else {
								r.addCell(CellSetName.DELETION_UNITS, cd);
							}
						}
					}
					if (r.getCellSet(CellSetName.DELETION_CELLS).size() != 0) {
						String s = "Les " + min + " candidats { ";
						for (int v : listInteger) {
							s += String.valueOf(v) + " ";
						}
						s += "} sont présents tous les " + min + " dans les "+ min +" de la même"
							+ " région, il est donc possible de les supprimer des autres cases";
						r.setDescription(s);
						return r;
					}
					r = null;
				} else {
					setCoord.clear();
					min = 0;
					listCouple.clear();
				}
			}
		}
		return null;
	}
	
	//OUTILS
	private List<Integer> getListCandidate(CellModel c) {
		Contract.checkCondition(c != null);
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int k = 1; k <= c.getCardinalCandidates(); k++) {
			if (c.isCandidate(k)) {
				list.add(k);
			}
		}
		return list;
	}
	
	private class CoupleIntergerCoord {
		
		//ATTRIBUTS
		private List<Integer>getListInteger;
		private List<ICoord> getListCoord;
		
		//CONSTRUCTEURS
		public CoupleIntergerCoord(List<Integer> listInteger, List<ICoord> listCoord) {
			getListInteger = listInteger;
			getListCoord = listCoord;
		}
		
		public List<Integer> getListInteger() {
			return getListInteger;
		}
		
		public List<ICoord> getListCoord() {
			return getListCoord;
		}
	}
}
