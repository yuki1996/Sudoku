package sudoku.model.heuristic;

import java.util.HashSet;
import java.util.Set;

import sudoku.model.ICell;
import sudoku.model.IGrid;
import sudoku.model.heuristic.Report.CellSetName;
import util.Contract;

public class RulePairTriplet extends ReportGenerator {

	@Override
	protected Report generate(IGrid grid) {
		Contract.checkCondition(grid != null);
		Report r = new RemoveReport();
		ICell [][] tabC = grid.cells();
		ICell c;
		Object[][] tab = new Object[grid.numberCandidates()][3];
		Set<Integer> setRow = new HashSet<Integer>();
		Set<Integer> setCol = new HashSet<Integer>();
		//on regarde région par ligne
		int nbSW = grid.getNumberSectorByWidth();
		for (int i = 0; i < grid.getWidthSector(); i++) {
			for (int k = 0; k < grid.numberCandidates(); k++) {
				tab[k][0] = false;
				tab[k][1] = new HashSet<ICell>();
				tab[k][2] = -1;
			}
			for (int m = i * nbSW; m < grid.getHeightSector() * (i + 1); m++) {
				for (int j = 0; j < grid.size(); j++) {
					c = tabC[m][j];
					//cellule modifiable sans valeur
					if (c.isModifiable() && ! c.hasValue()) {
						boolean [] tabB = c.candidates();
						//parcourt tableau des candidats de la cellule
						for (int k = 0; k < tabB.length; k++) {
							//le candidat existe dans la cellule et qu'il n'est pas déjà apparu
							if (tabB[k] && !(Boolean)tab[k][0]) {
								tab[k][0] = true;
								((Set<ICell>) tab[k][1]).add(c);
								tab[k][2] = m;
								setCol.add(j);
							}
							if (tabB[k] && (Boolean)tab[k][0] && tab[k][1] != null) {
								//le candidat existe dans la cellule et qu'il est déjà apparu dans la même ligne
								if (m == ((Integer) tab[k][2])) {
									((Set<ICell>) tab[k][1]).add(c);
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
				if ((Boolean) tab[k][0] && tab[k][1] != null) {
					for (ICell cell : (Set<ICell>) tab[k][1]) {
						r.addCell(CellSetName.DECISIVE_UNITS, cell);
					}
					r.addCell(CellSetName.DECISIVE_CELLS, (ICell) tab[k][1]);
					for (int col : setCol) {
						for (ICell cell : grid.getSector(i, col)) {
							r.addCell(CellSetName.DELETION_UNITS, cell);
						}
					}
					for (ICell cell : grid.getRow((Integer) tab[k][2])) {
						r.addCell(CellSetName.DELETION_CELLS, cell);
					}
					int value = k + 1;
					r.addValue(value);
					String s = "Les " + r.getCellSet(CellSetName.DECISIVE_CELLS).size() + " candidats ";
					s += value + " alignés dans cette région, donnent la possibilitée de" 
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
				tab[k][1] = new HashSet<ICell>();
				tab[k][2] = -1;
			}
			for (int m = j * nbSH; m < grid.getWidthSector() * (j + 1); m++) {
				for (int i = 0; i < grid.size(); i++) {
					c = tabC[i][m];
					//cellule modifiable sans valeur
					if (c.isModifiable() && ! c.hasValue()) {
						boolean [] tabB = c.candidates();
						//parcourt tableau des candidats de la cellule
						for (int k = 0; k < tabB.length; k++) {
							//le candidat existe dans la cellule et qu'il n'est pas déjà apparu
							if (tabB[k] && !(Boolean)tab[k][0]) {
								tab[k][0] = true;
								((Set<ICell>) tab[k][1]).add(c);
								tab[k][2] = m;
								setRow.add(i);
							}
							if (tabB[k] && (Boolean)tab[k][0] && tab[k][1] != null) {
								//le candidat existe dans la cellule et qu'il est déjà apparu dans la même colonne
								if (m == ((Integer) tab[k][2])) {
									((Set<ICell>) tab[k][1]).add(c);
								} else {
									//le candidat existe dans la cellule et qu'il est déjà apparu mais dans une autre colonne
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
				if ((Boolean) tab[k][0] && tab[k][1] != null) {
					for (ICell cell : (Set<ICell>) tab[k][1]) {
						r.addCell(CellSetName.DECISIVE_UNITS, cell);
					}
					r.addCell(CellSetName.DECISIVE_CELLS, (ICell) tab[k][1]);
					for (int row : setRow) {
						for (ICell cell : grid.getSector(row, j)) {
							r.addCell(CellSetName.DELETION_UNITS, cell);
						}
					}
					for (ICell cell : grid.getRow((Integer) tab[k][2])) {
						r.addCell(CellSetName.DELETION_CELLS, cell);
					}

					int value = k + 1;
					r.addValue(value);
					String s = "Les " + r.getCellSet(CellSetName.DECISIVE_CELLS).size() + " candidats ";
					s += value + " alignés dans cette région, donnent la possibilitée de" 
							+ " supprimer les " + value + " dans les autres régions de cette colonne.";
					r.setDescription(s);
					return r;
				}
			}
		}
		return null;
	}
}