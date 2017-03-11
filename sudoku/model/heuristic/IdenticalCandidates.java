package sudoku.model.heuristic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
		List<ICoord> list = new ArrayList<ICoord>();
		for (int i = 0; i < tab.length; ++i) {
			for (int j = 0; j < tab.length; ++j) {
				if (! tab[i][j].hasValue()) {
					list.add(new Coord(i, j));
				}
			}
		}
		
		
		Map<Integer, List<List<ICoord>>> map1 = sort1(list, 0, grid.numberCandidates(), grid); 
		Map<Integer, List<Couple<Unit, List<ICoord>>>> map2 = sort2(map1, grid);
		
		
		for (Entry<Integer, List<Couple<Unit, List<ICoord>>>> ent : map2.entrySet()) {
			System.out.println(ent.getKey() + "\n");
			for (Couple<Unit, List<ICoord>> couple : ent.getValue()) {
				if (couple.getFirst() == Unit.COL) {
					System.out.println("  col");
				} else if (couple.getFirst() == Unit.ROW) {
					System.out.println("  row");
				} else {
					System.out.println("  sector");
				}
				System.out.print("  ");
				for(ICoord c : couple.getSecond()) {
					System.out.print("L" + c.getRow() + "C" + c.getCol() + "\n");
				}
			}
			System.out.println();
		}
		
		
		
		
		for (int i = 2; i < grid.numberCandidates(); ++i) {
			List<Couple<Unit, List<ICoord>>> listCouple = map2.get(i);
			if (listCouple.size() > 0) {
				Couple<Unit, List<ICoord>> couple = listCouple.get(0);
				RemoveCandidateReport res = new RemoveCandidateReport(grid);
				
				//candidats a supprimé
				ICoord c = couple.getSecond().get(0);
				CellModel cell = grid.getCell(c);
				String str = "Avec la regle des candidats identiques, "
						+ "on peut supprimer les candidats ";
				for (Integer j = 1; j < cell.getCardinalCandidates(); ++j) {
					if (cell.isCandidate(j)) {
						res.addValue(j);
						str += j.toString() + " ";
					}
				}
				
				
				Set<ICoord> sector;
				if (couple.getFirst() == Unit.COL) {
					sector = grid.getCol(c);
				} else if (couple.getFirst() == Unit.ROW) {
					sector = grid.getRow(c);
				} else {
					sector = grid.getSector(c);
				}
				res.setCellSet(CellSetName.DELETION_UNITS, sector);
				sector.removeAll(couple.getSecond());
				res.setCellSet(CellSetName.DELETION_CELLS, sector);
				
				str += "des cellules";
				for (ICoord coord : sector) {
					str += " L" + coord.getRow() + "C" + coord.getCol();
				}
				str += ".";
				res.setDescription(str);
				return res;
			}
		}
		
		return null;
	}
	
	//OUTILS
	/**
	 * Renvoie une map tel que les clés soit le nombre de candidats des cellules de g 
	 * pointées par les coordonnées des listes de liste. Toutes les cordonnées d'une 
	 * liste ont exactement les meme candidats.
	 */
	private Map<Integer, List<List<ICoord>>> sort1(List<ICoord> list, 
			int numberCandidates, int candidateValue, GridModel g) {
		if (candidateValue == 0) {
			Map<Integer, List<List<ICoord>>> res = new HashMap<Integer, 
					List<List<ICoord>>>();
			List<List<ICoord>> listList = new ArrayList<List<ICoord>>();
			listList.add(list);
			res.put(numberCandidates, listList);
			return res;
		}
		Map<Integer, List<List<ICoord>>> res = new HashMap<Integer, 
				List<List<ICoord>>>();
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
			res.putAll(sort1(list1, numberCandidates + 1, candidateValue - 1, g));
		}
		if (! list2.isEmpty()) {
			res.putAll(sort1(list2, numberCandidates, candidateValue - 1, g));
		}
		return res;
	}
	
	/**
	 * renvoie une map tel que les clés soit le nombre de candidats des cellules de g 
	 * pointées par les coordonnées des listes de listes. Toutes les coordonnées d'une 
	 * liste ont exactement les meme candidats, font parties d'une meme unité et la 
	 * taille de la liste est egale au nombre de candidats des cellules que pointent 
	 * les coordonnées qu'il contient.
	 */
	private Map<Integer, List<Couple<Unit, List<ICoord>>>> sort2(Map<Integer, List<List<ICoord>>> map, 
			GridModel g) {
		Map<Integer, List<Couple<Unit, List<ICoord>>>> res = 
				new HashMap<Integer, List<Couple<Unit, List<ICoord>>>>();
		for (int i = 1; i <= g.numberCandidates(); ++i) {
			List<List<ICoord>> listList = map.get(i);
			List<Couple<Unit, List<ICoord>>> listCouple = new ArrayList<Couple<Unit, List<ICoord>>>();
			if (listList != null) {
				for (List<ICoord> list : listList) {
					//L'integer est le numero de l'unit
					Map<Integer, List<ICoord>> mapUnit = new HashMap<Integer, 
							List<ICoord>>();
					

					//coordonnées par ligne
					for (int j = 0; j < g.size(); ++j) {
						mapUnit.put(j, new ArrayList<ICoord>());
					}
					for (ICoord c : list) {
						mapUnit.get(c.getRow()).add(c);
					}
					
					for (int j = 0; j < g.size(); ++j) {
						List<ICoord> l = mapUnit.get(j);
						if (l != null) {
							int nbCellWithoutValueOnUnit = 0;
							for (ICoord c : g.getRow(j)) {
								if (! g.getCell(c).hasValue()) {
									++nbCellWithoutValueOnUnit;
								}
							}
							if (l.size() == i && l.size() < nbCellWithoutValueOnUnit) {
								listCouple.add(new Couple<Unit, List<ICoord>>(Unit.ROW,l));
							}
						}
					}
					
					//coordonnées par colonne
					for (int j = 0; j < g.size(); ++j) {
						mapUnit.put(j, new ArrayList<ICoord>());
					}
					for (ICoord c : list) {
						mapUnit.get(c.getCol()).add(c);
					}
					
					for (int j = 0; j < g.size(); ++j) {
						List<ICoord> l = mapUnit.get(j);
						if (l != null) {
							int nbCellWithoutValueOnUnit = 0;
							for (ICoord c : g.getCol(j)) {
								if (! g.getCell(c).hasValue()) {
									++nbCellWithoutValueOnUnit;
								}
							}
							if (l.size() == i && l.size() < nbCellWithoutValueOnUnit) {
								listCouple.add(new Couple<Unit, List<ICoord>>(Unit.COL,l));
							}
						}
					}
					
					//List<Set<ICoord>> listSector = new ArrayList<Set<ICoord>>();
					//coordonnées par region
					for (int j = 0; j < g.size(); ++j) {
						mapUnit.put(j, new ArrayList<ICoord>());
						/*listSector.add(j, g.getSector(j % g.getNumberSectorByWidth(),
								j / g.getNumberSectorByWidth()));*/
					}
					for (ICoord c : list) {
						mapUnit.get(c.getRow() / g.getHeightSector() * g.getNumberSectorByWidth() 
								+ c.getCol() / g.getWidthSector()).add(c);
					}
					
					for (int j = 0; j < g.size(); ++j) {
						List<ICoord> l = mapUnit.get(j);
						if (l != null) {
							int nbCellWithoutValueOnUnit = 0;
							for (ICoord c : g.getSector(j / g.getWidthSector(), 
									j % g.getWidthSector())) {
								if (! g.getCell(c).hasValue()) {
									++nbCellWithoutValueOnUnit;
								}
							}
							if (l.size() == i && l.size() < nbCellWithoutValueOnUnit) {
								listCouple.add(new Couple<Unit, List<ICoord>>(Unit.SECTOR,l));
							}
						}
					}
					
				}
			}
			res.put(i, listCouple);
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
