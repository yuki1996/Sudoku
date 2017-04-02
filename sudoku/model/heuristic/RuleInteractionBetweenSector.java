package sudoku.model.heuristic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sudoku.model.CellModel;
import sudoku.model.GridModel;
import sudoku.model.heuristic.Report.CellSetName;
import sudoku.util.Coord;
import sudoku.util.ICoord;
import util.Contract;

public class RuleInteractionBetweenSector extends ReportGenerator {

	protected Report generate(GridModel grid) {
		Contract.checkCondition(grid != null);
		CellModel [][] tabC = grid.cells();
		Contract.checkCondition(tabC != null);
		CellModel c;
		Boolean[] tabB = new Boolean[grid.numberCandidates()];
		Map<Integer, Set<ICoord>> map = new HashMap<Integer, Set<ICoord>>();
		Integer[] unit= new Integer[grid.numberCandidates()];
		int nbSW = grid.getNumberSectorByWidth();
		int nbSH = grid.getNumberSectorByHeight();
		//on regarde région par ligne
		for (int i = 0; i < grid.getWidthSector(); i++) {
			for (int m = i * nbSW; m < grid.getHeightSector() * (i + 1); m++) {
				for (int k = 0; k < grid.numberCandidates(); k++) {
					tabB[k] = false;
					map.put(k, new HashSet<ICoord>());
					unit[k] = -1;
				}
				for (int j = 0; j < grid.size(); j++) {
					c = tabC[m][j];
					//cellule modifiable sans valeur
					if (c.isModifiable() && ! c.hasValue()) {
						//parcourt tableau des candidats de la cellule
						for (int k = 0; k < grid.numberCandidates(); k++) {
							//le candidat existe dans la cellule et qu'il n'est pas déjà apparu
							if (c.isCandidate(k + 1) && !tabB[k]) {
								tabB[k] = true;
								Set<ICoord> set = map.get(k);
								set.add(new Coord(m, j));
								map.put(k, set);
								unit[k] = (m / nbSH) * nbSW + ( j / nbSW) + 1;
							}
							if (c.isCandidate(k + 1) && tabB[k] && map.get(k) != null) {
								//le candidat existe dans la cellule et qu'il est déjà apparu dans la même région
								if (((m / nbSH) * nbSW + ( j / nbSW) + 1) == unit[k]) {
									Set<ICoord> set = map.get(k);
									set.add(new Coord(m, j));
								} else {
									//le candidat existe dans la cellule et qu'il est déjà apparu mais dans une autre ligne
									map.put(k, null);
								}
							}
						}
					}
				}
				// A la fin de la ligne
				for (int k = 0; k < grid.numberCandidates(); k++) {
					//le candidat n'est présent que sur une ligne 
					if (tabB[k] && (map.get(k) != null) && map.get(k).size() > 1) {
						RemoveCandidateReport r = new RemoveCandidateReport(grid);
						int value = k + 1;
						r.addValue(value);
						for (ICoord coord : map.get(k)) {
							r.addCell(CellSetName.DECISIVE_UNITS, coord);
						}
						for (ICoord coord : grid.getSector(map.get(k).iterator().next())) {
							if (grid.getCell(coord).isCandidate(value) && !grid.getCell(coord).hasValue() 
									&& !r.getCellSet(CellSetName.DECISIVE_UNITS).contains(coord)) {
								r.addCell(CellSetName.DELETION_CELLS, coord);
							} else {
								r.addCell(CellSetName.DELETION_UNITS, coord);
							}
						}
						for (int j = i * nbSW; j < grid.getWidthSector() * (i + 1); j++) {
							if (j != m)  {
								for (int l = 0; l < grid.size(); l++) {
									CellModel cell = tabC[j][l];
									ICoord coord = new Coord(j,l);
									if (!r.getCellSet(CellSetName.DELETION_CELLS).contains(coord) &&
											!r.getCellSet(CellSetName.DELETION_UNITS).contains(coord)) {
										if (cell.isCandidate(value) && !cell.hasValue()) {
											r.addCell(CellSetName.DECISIVE_CELLS, coord);
										} else {
											r.addCell(CellSetName.DECISIVE_UNITS, coord);
										}
									}
								}
							} else {
								int col = ((ICoord) r.getCellSet(CellSetName.DELETION_UNITS).toArray()[1]).getCol();
								int colSector = (col / grid.getWidthSector());
								for (int l = colSector * nbSH; l < grid.getHeightSector() * (colSector + 1); l++) {

									r.addCell(CellSetName.DECISIVE_UNITS, new Coord(m,l));
								}
							}
						}
						if (r.getCellSet(CellSetName.DELETION_CELLS).size() != 0) {
							String s = " Intéractions entre régions : le candidat " + value + " n'est présenté que dans 2 lignes de 2 régions.\n" +
								" On peut donc le supprimer dans ces lignes de la 3ème region.";
							r.setDescription(s);
							return r;
						}
						r = null;
					}
				}
			}
		}
		//on regarde région par colonne
		for (int j = 0; j < grid.getWidthSector(); j++) {
			for (int n = j * nbSH; n < grid.getHeightSector() * (j + 1); n++) {
				for (int k = 0; k < grid.numberCandidates(); k++) {
					tabB[k] = false;
					map.put(k, new HashSet<ICoord>());
					unit[k] = -1;
				}
				for (int i = 0; i < grid.size(); i++) {
					c = tabC[i][n];
					//cellule modifiable sans valeur
					if (c.isModifiable() && ! c.hasValue()) {
						//parcourt tableau des candidats de la cellule
						for (int k = 0; k < grid.numberCandidates(); k++) {
							//le candidat existe dans la cellule et qu'il n'est pas déjà apparu
							if (c.isCandidate(k + 1) && !tabB[k]) {
								tabB[k] = true;
								Set<ICoord> set = map.get(k);
								set.add(new Coord(i, n));
								map.put(k, set);
								unit[k] = (i / nbSH) * nbSW + (n / nbSW) + 1;
							}
							if (c.isCandidate(k + 1) && tabB[k] && map.get(k) != null) {
								//le candidat existe dans la cellule et qu'il est déjà apparu dans la même région
								if (((i / nbSH) * nbSW + ( n / nbSW) + 1) == unit[k]) {
									Set<ICoord> set = map.get(k);
									set.add(new Coord(i, n));
								} else {
									//le candidat existe dans la cellule et qu'il est déjà apparu mais dans une autre colonne
									map.put(k, null);
								}
							}
						}
					}
					
				}
				// A la fin de la colonne
				for (int k = 0; k < grid.numberCandidates(); k++) {
					//le candidat n'est présent que sur une colonne
					if (tabB[k] && (map.get(k) != null) && map.get(k).size() > 1) {
						RemoveCandidateReport r = new RemoveCandidateReport(grid);
						int value = k + 1;
						r.addValue(value);
						for (ICoord coord : map.get(k)) {
							r.addCell(CellSetName.DECISIVE_UNITS, coord);
						}
						for (ICoord coord : grid.getSector(map.get(k).iterator().next())) {
							if (grid.getCell(coord).isCandidate(value) && !grid.getCell(coord).hasValue() 
									&& !r.getCellSet(CellSetName.DECISIVE_UNITS).contains(coord)) {
								r.addCell(CellSetName.DELETION_CELLS, coord);
							} else {
								r.addCell(CellSetName.DELETION_UNITS, coord);
							}
						}
						for (int i = j * nbSH; i < grid.getHeightSector() * (j + 1); i++) {
							if (i != n)  {
								for (int l = 0; l < grid.size(); l++) {
									CellModel cell = tabC[l][i];
									ICoord coord = new Coord(l,i);
									if (!r.getCellSet(CellSetName.DELETION_CELLS).contains(coord) &&
											!r.getCellSet(CellSetName.DELETION_UNITS).contains(coord)) {
										if (cell.isCandidate(value) && !cell.hasValue()) {
											r.addCell(CellSetName.DECISIVE_CELLS, coord);
										} else {
											r.addCell(CellSetName.DECISIVE_UNITS, coord);
										}
									}
								}
							} else {
								int row = ((ICoord) r.getCellSet(CellSetName.DELETION_UNITS).toArray()[1]).getRow();
								int rowSector = (row / grid.getWidthSector());
								for (int l = rowSector * nbSW; l < grid.getWidthSector() * (rowSector + 1); l++) {
									r.addCell(CellSetName.DECISIVE_UNITS, new Coord(l,n));
								}
							}
						}
						if (r.getCellSet(CellSetName.DELETION_CELLS).size() != 0) {
							String s = " Intéractions entre régions : le candidat " + value + " n'est présenté que dans 2 colonnes de 2 régions." +
									"\n On peut donc le supprimer dans ces colonnes de la 3ème region.";
							r.setDescription(s);
							return r;
						}
						r = null;
					}
				}
			}
				
		}
		return null;
	}
}
