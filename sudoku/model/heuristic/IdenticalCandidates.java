package sudoku.model.heuristic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sudoku.model.CellModel;
import sudoku.model.GridModel;
import sudoku.model.heuristic.Report.CellSetName;
import sudoku.util.Coord;
import sudoku.util.Couple;
import sudoku.util.ICoord;
import util.Contract;

public class IdenticalCandidates extends ReportGenerator {
	
	//COMMANDES
	@Override
	protected Report generate(GridModel grid) {
		Contract.checkCondition(grid != null);
		CellModel [][] tab = grid.cells();
		Contract.checkCondition(tab != null);
		
		// on met toutes les cellules n'ayant pas de valeur dans list
		List<ICoord> list = new ArrayList<ICoord>();
		for (int i = 0; i < tab.length; ++i) {
			for (int j = 0; j < tab.length; ++j) {
				if (! tab[i][j].hasValue()) {
					list.add(new Coord(i, j));
				}
			}
		}
		
		
		List<List<ICoord>> []tab1 = sort1(list, 0, grid.numberCandidates(), grid);
		
		List<Couple<Unit, List<ICoord>>> []tab2 = sort2(tab1, grid);
		
		
		for (int i = 2; i < grid.numberCandidates(); ++i) {
			for (Couple<Unit, List<ICoord>> couple : tab2[i]) {
				//candidats a supprimé
				ICoord c = couple.getSecond().get(0);
				CellModel cell = grid.getCell(c);
				Set<Integer> Candidates = new HashSet<Integer>();
				for (Integer j = 1; j <= cell.getCardinalCandidates(); ++j) {
					if (cell.isCandidate(j)) {
						Candidates.add(j);
					}
				}
				Set<ICoord> unit;
				String unitStr;
				if (couple.getFirst() == Unit.COL) {
					unit = grid.getCol(c);
					unitStr = "de la colonne";
				} else if (couple.getFirst() == Unit.ROW) {
					unit = grid.getRow(c);
					unitStr = "de la ligne";
				} else {
					unit = grid.getSector(c);
					unitStr = "du secteur";
				}
				
				Set<ICoord> possiblyChangedCells = new HashSet<ICoord>(unit);
				possiblyChangedCells.removeAll(couple.getSecond());
				Set<ICoord> changedCells = new HashSet<ICoord>();
				
				// On verifie qu'il y a un candidat a suprimer dans une cellule sans valeur 
				boolean test = false;
				for (ICoord c1 : possiblyChangedCells) {
					if (! grid.getCell(c1).hasValue()) {
						CellModel cell1 = grid.getCell(c1);
						for (Integer j : Candidates) {
							if (cell1.isCandidate(j)) {
								changedCells.add(c1);
								test = true;
							}
						}
					}
				}
				
				if (test) {
					RemoveCandidateReport res = new RemoveCandidateReport(grid);
					
					String str = "Les cellules";
					for (ICoord coord : couple.getSecond()) {
						str += " L" + coord.getRow() + "C" + coord.getCol();
						res.addCell(CellSetName.DECISIVE_CELLS, coord);
					}
					str += " ont et ont uniquement les candidats";
					for (Integer j : Candidates) {
						res.addValue(j);
						str += " " + j.toString();
					}
					res.setCellSet(CellSetName.DELETION_UNITS, unit);
					res.setCellSet(CellSetName.DELETION_CELLS, changedCells);
					
					str += ".\nOn peut donc retirer ces candidats des autres cellules " + unitStr + ":";
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
		}
		
		return null;
	}
	
	//OUTILS
	/**
	 * Renvoie un tableau où les coordonnées d'une liste ont les même candidats exactement
	 * et où les coordonnnées de la liste de liste d'indice i ont i candidats.
	 */
	@SuppressWarnings("unchecked")
	private List<List<ICoord>>[] sort1(List<ICoord> list, 
			int numberCandidates, int candidateValue, GridModel g) {
		if (candidateValue == 0) {
			List<List<ICoord>> []res = new List[g.numberCandidates() + 1];
			res[numberCandidates] =  new ArrayList<List<ICoord>>();
			res[numberCandidates].add(list);
			return res;
		}
		List<List<ICoord>> []res = new List[g.numberCandidates() + 1];
		List<ICoord> list1 = new ArrayList<ICoord>();
		List<ICoord> list2 = new ArrayList<ICoord>();
		for (ICoord c : list) {
			if (g.getCell(c).isCandidate(candidateValue)) {
				list1.add(c);
			} else {
				list2.add(c);
			}
		}
		
		if (! list1.isEmpty()) {
			List<List<ICoord>> []res1 = sort1(list1, numberCandidates + 1, candidateValue - 1, g);
			
			for (int i = 0; i < res.length; ++i) {
				if (res[i] != null) {
					if (res1[i] != null) {
						res[i].addAll(res1[i]);
					}
				} else {
					res[i] =res1[i];
				}
			}
			
		}
		
		if (! list2.isEmpty()) {
			List<List<ICoord>> []res2 = sort1(list2, numberCandidates, candidateValue - 1, g);
			for (int i = 0; i < res.length; ++i) {
				if (res[i] != null) {
					if (res2[i] != null) {
						res[i].addAll(res2[i]);
					}
				} else {
					res[i] = res2[i];
				}
			}
		}
		
		return res;
	}
	
	/**
	 * Renvoie un tableau tel que la liste d'indice i soit composé de couple dont un 
	 * élément est une liste de i coordonnées tel que ces coordonnées aient i candidats 
	 * et aient les même candidats que les autres cellules de la liste et fassent partie 
	 * de la même unité dans g et l'autre élément du couple est de type Unit est représente
	 * le type de l'unité dont les cellules de la liste font toutes partie.
	 */
	private List<Couple<Unit, List<ICoord>>>[] sort2(List<List<ICoord>>[] tab, 
			GridModel g) {
		@SuppressWarnings("unchecked")
		List<Couple<Unit, List<ICoord>>>[] res = new List[g.numberCandidates() + 1];
		for (int i = 1; i <= g.numberCandidates(); ++i) {

			res[i] = new ArrayList<Couple<Unit, List<ICoord>>>(); //listCouple
			if (tab[i] != null) {
				for (List<ICoord> list : tab[i]) {
					//L'indice est le numero de l'unit dans la grille
					@SuppressWarnings("unchecked")
					List<ICoord>[] tabUnit = new List[g.size()];
					

					//coordonnées par ligne
					for (int j = 0; j < g.size(); ++j) {
						tabUnit[j] = new ArrayList<ICoord>();
					}
					for (ICoord c : list) {
						tabUnit[c.getRow()].add(c);
					}
					
					for (int j = 0; j < g.size(); ++j) {
						if (tabUnit[j] != null) {
							int nbCellWithoutValueOnUnit = 0;
							for (ICoord c : g.getRow(j)) {
								if (! g.getCell(c).hasValue()) {
									++nbCellWithoutValueOnUnit;
								}
							}
							if (tabUnit[j].size() == i && tabUnit[j].size() < nbCellWithoutValueOnUnit) {
								res[i].add(new Couple<Unit, List<ICoord>>(Unit.ROW, tabUnit[j]));
							}
						}
					}
					
					//coordonnées par colonne
					for (int j = 0; j < g.size(); ++j) {
						tabUnit[j] = new ArrayList<ICoord>();
					}
					for (ICoord c : list) {
						tabUnit[c.getCol()].add(c);
					}
					
					for (int j = 0; j < g.size(); ++j) {
						if (tabUnit[j] != null) {
							int nbCellWithoutValueOnUnit = 0;
							for (ICoord c : g.getCol(j)) {
								if (! g.getCell(c).hasValue()) {
									++nbCellWithoutValueOnUnit;
								}
							}
							if (tabUnit[j].size() == i && tabUnit[j].size() < nbCellWithoutValueOnUnit) {
								res[i].add(new Couple<Unit, List<ICoord>>(Unit.COL, tabUnit[j]));
							}
						}
					}
					
					//coordonnées par region
					for (int j = 0; j < g.size(); ++j) {
						tabUnit[j] = new ArrayList<ICoord>();
					}
					for (ICoord c : list) {
						tabUnit[c.getRow() / g.getHeightSector() * g.getNumberSectorByWidth() 
								+ c.getCol() / g.getWidthSector()].add(c);
					}
					
					for (int j = 0; j < g.size(); ++j) {
						if (tabUnit[j] != null) {
							int nbCellWithoutValueOnUnit = 0;
							for (ICoord c : g.getSector(j / g.getWidthSector(), 
									j % g.getWidthSector())) {
								if (! g.getCell(c).hasValue()) {
									++nbCellWithoutValueOnUnit;
								}
							}
							if (tabUnit[j].size() == i && tabUnit[j].size() < nbCellWithoutValueOnUnit) {
								res[i].add(new Couple<Unit, List<ICoord>>(Unit.SECTOR,tabUnit[j]));
							}
						}
					}
					
				}
			}
		}
		return res;
	}

	//CLASSE INTERNE
	
	private enum Unit {
		SECTOR,
		COL,
		ROW
	}
}
