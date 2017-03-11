package sudoku.model.heuristic;

import java.util.HashSet;
import java.util.Set;

import sudoku.model.CellModel;
import sudoku.model.GridModel;
import sudoku.model.heuristic.Report.CellSetName;
import sudoku.util.Coord;
import sudoku.util.ICoord;
import util.Contract;

public class RulePairTriplet extends ReportGenerator {

	@Override
	protected Report generate(GridModel grid) {
		Contract.checkCondition(grid != null);
		CellModel [][] tabC = grid.cells();
		CellModel c;
		Object[][] tab = new Object[grid.numberCandidates()][3];
		//on regarde région par ligne
		int nbSW = grid.getNumberSectorByWidth();
		for (int i = 0; i < grid.getWidthSector(); i++) {
			for (int k = 0; k < grid.numberCandidates(); k++) {
				tab[k][0] = false;
				tab[k][1] = new HashSet<ICoord>();
				tab[k][2] = -1;
			}
			for (int m = i * nbSW; m < grid.getHeightSector() * (i + 1); m++) {
				for (int j = 0; j < grid.size(); j++) {
					c = tabC[m][j];
					//cellule modifiable sans valeur
					if (c.isModifiable() && ! c.hasValue()) {
						//parcourt tableau des candidats de la cellule
						for (int k = 0; k < grid.numberCandidates(); k++) {
							//le candidat existe dans la cellule et qu'il n'est pas déjà apparu
							if (c.isCandidate(k + 1)&& !(Boolean)tab[k][0]) {
								tab[k][0] = true;
								((Set<ICoord>) tab[k][1]).add(new Coord(m, j));
								tab[k][2] = m;
							}
							if (c.isCandidate(k + 1) && (Boolean)tab[k][0] && tab[k][1] != null) {
								//le candidat existe dans la cellule et qu'il est déjà apparu dans la même ligne
								if (m == ((Integer) tab[k][2])) {
									((Set<ICoord>) tab[k][1]).add(new Coord(m, j));
								} else {
									//le candidat existe dans la cellule et qu'il est déjà apparu mais dans une autre ligne
									tab[k][1] = null;
								}
							}
						}
					}
				}
			}
			// A la fin de chaque bloc région on regarde si on a candidat unique dans la région
			for (int k = 0; k < grid.numberCandidates(); k++) {
				//le candidat n'est présent que sur une ligne 
				if ((Boolean) tab[k][0] && tab[k][1] != null && ((Set<ICoord>) tab[k][1]).size() > 1) {
					RemoveCandidateReport r = new RemoveCandidateReport(grid);
					int value = k + 1;
					r.addValue(value);
					for (ICoord coord : (Set<ICoord>) tab[k][1]) {
						System.out.println("case("+coord.getRow()+","+coord.getCol()+")");
						r.addCell(CellSetName.DECISIVE_CELLS, coord);
						for(ICoord cd : grid.getSector(coord.getRow(), coord.getCol())) {
							r.addCell(CellSetName.DECISIVE_UNITS, cd);
						}
					}
					for (int j = 0; j < grid.size(); j++) {
						ICoord coord = new Coord((Integer) tab[k][2], j);
						if (grid.getCell(coord).isCandidate(value) && grid.getCell(coord).isModifiable() 
								&& !r.getCellSet(CellSetName.DECISIVE_CELLS).contains(coord)) {
							System.out.println("case("+coord.getRow()+","+coord.getCol()+ ") supprime "+ value);
							r.addCell(CellSetName.DELETION_CELLS, coord);
						} else {
							r.addCell(CellSetName.DELETION_UNITS, coord);
						}
					}
					if (r.getCellSet(CellSetName.DELETION_CELLS).size() == 0) {
						r = null;
						break;
					}
					String s = "Les " + r.getCellSet(CellSetName.DECISIVE_CELLS).size() + " candidats ";
					s += value + " alignés dans cette région, donnent la possibilité de" 
							+ " supprimer les " + value + " dans les autres régions de cette ligne.";
					r.setDescription(s);
					return r;
				}
			}
		}

		//on regarde région par colonne
		int nbSH = grid.getNumberSectorByHeight();
		for (int j = 0; j < grid.getHeightSector(); j++) {
			for (int k = 0; k < grid.numberCandidates(); k++) {
				tab[k][0] = false;
				tab[k][1] = new HashSet<CellModel>();
				tab[k][2] = -1;
			}
			for (int m = j * nbSH; m < grid.getWidthSector() * (j + 1); m++) {

				for (int i = 0; i < grid.size(); i++) {
					c = tabC[i][m];
					//cellule modifiable sans valeur
					if (c.isModifiable() && ! c.hasValue()) {
						//parcourt tableau des candidats de la cellule
						for (int k = 0; k < grid.numberCandidates(); k++) {
							//le candidat existe dans la cellule et qu'il n'est pas déjà apparu
							if (c.isCandidate(k + 1) && !(Boolean)tab[k][0]) {
								tab[k][0] = true;
								((Set<ICoord>) tab[k][1]).add(new Coord(m, j));
								tab[k][2] = m;
							}
							if (c.isCandidate(k + 1) && (Boolean)tab[k][0] && tab[k][1] != null) {
								//le candidat existe dans la cellule et qu'il est déjà apparu dans la même colonne
								if (m == ((Integer) tab[k][2])) {
									((Set<ICoord>) tab[k][1]).add(new Coord(m, j));
								} else {
									//le candidat existe dans la cellule et qu'il est déjà apparu mais dans une autre colonne
									tab[k][1] = null;
								}
							}
						}
					}
				}
			}
			// A la fin de chaque bloc région on regarde si on a candidat unique dans la colonne
			for (int k = 0; k < grid.numberCandidates(); k++) {
				//le candidat n'est présent que sur une ligne 
				if ((Boolean) tab[k][0] && tab[k][1] != null && ((Set<ICoord>) tab[k][1]).size() > 1) {
					RemoveCandidateReport r = new RemoveCandidateReport(grid);
					int value = k + 1;
					r.addValue(value);
					for (ICoord coord : (Set<ICoord>) tab[k][1]) {
						System.out.println("case("+coord.getRow()+","+coord.getCol()+")");
						r.addCell(CellSetName.DECISIVE_CELLS, coord);
						for(ICoord cd : grid.getSector(coord.getRow(), coord.getCol())) {
							r.addCell(CellSetName.DECISIVE_UNITS, cd);
						}
					}
					for (int i = 0; i < grid.size(); i++) {
						ICoord coord = new Coord(i, (Integer) tab[k][2]);
						if (grid.getCell(coord).isCandidate(value) && grid.getCell(coord).isModifiable()
								&& !r.getCellSet(CellSetName.DECISIVE_CELLS).contains(coord)) {
							System.out.println("case("+coord.getRow()+","+coord.getCol()+ ") supprime "+ value);
							r.addCell(CellSetName.DELETION_CELLS, coord);
						} else {
							r.addCell(CellSetName.DELETION_UNITS, coord);
						}
					}

					if (r.getCellSet(CellSetName.DELETION_CELLS).size() == 0) {
						r = null;
						break;
					}
					String s = "Les " + r.getCellSet(CellSetName.DECISIVE_CELLS).size() + " candidats ";
					s += value + " alignés dans cette région, donnent la possibilité de" 
							+ " supprimer les " + value + " dans les autres régions de cette colonne.";
					r.setDescription(s);
					return r;
					
					
				}
			}
		}
		return null;
	}
}
