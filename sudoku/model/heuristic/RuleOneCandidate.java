package sudoku.model.heuristic;

import sudoku.model.ICell;
import sudoku.model.IGrid;
import sudoku.model.heuristic.Report;
import sudoku.model.heuristic.Report.CellSetName;
import util.Contract;

public class RuleOneCandidate extends ReportGenerator {

	@Override
	protected Report generate(IGrid grid) {
		Contract.checkCondition(grid != null);
		ICell [][] tabC = grid.cells();
		Contract.checkCondition(tabC != null);
		Report r = new SetValueReport();
		ICell c = null;
		Integer[] tabI = new Integer[grid.numberCandidates()];
		ICell[] tabCell = new ICell[grid.numberCandidates()];
		//on regarde ligne par ligne
		for (int i = 0; i < grid.size(); i++) {
			for (int k = 0; k < grid.numberCandidates(); k++) {
				tabI[k] = 0;
				tabCell[k] = null;
			}
			for (int j = 0; j < grid.size(); j++) {
				c = tabC[i][j];
				//cellule modifiable sans valeur
				if (!c.hasValue()) {
					boolean [] tabB = c.candidates();
					//parcourt tableau des candidats de la cellule
					for (int k = 0; k < tabB.length; k++) {
						//le candidat existe dans la cellule
						if (tabB[k]) {
							tabI[k] += 1;
							tabCell[k] = c;
						}		
					}
				}
			}
			// A la fin de chaque région on regarde si on a candidat unique dans la ligne
			for (int k = 0; k < grid.numberCandidates(); k++) {
				//le candidat existe dans la ligne et qu'il est apparu une seule et unique fois
				if ( tabI[k] == 1 && tabCell[k] != null) {
					for (ICell cell : grid.getRow(i)) {
						r.addCell(CellSetName.DECISIVE_UNITS, cell);
					}
					r.addCell(CellSetName.DECISIVE_CELLS, tabCell[k]);
					int value = k + 1;
					r.addValue(value);
					String s = "Le candidat " + value + " n'est présent qu'une seule fois dans cette ligne.";
					r.setDescription(s);
					return r;
				}
			}
		}
		//on regarde colonne par colonne
		for (int i = 0; i < grid.size(); i++) {
			for (int k = 0; k < grid.numberCandidates(); k++) {
				tabI[k] = 0;
				tabCell[k] = null;
			}
			for (int j = 0; j < grid.size(); j++) {
				c = tabC[j][i];
				//cellule modifiable sans valeur
				if (c.isModifiable() && ! c.hasValue()) {
					boolean [] tabB = c.candidates();
					//parcourt tableau des candidats de la cellule
					for (int k = 0; k < tabB.length; k++) {
						//le candidat existe dans la cellule
						if (tabB[k]) {
							tabI[k] += 1;
							tabCell[k] = c;
						}
					}
				}
			}
			// A la fin de chaque région on regarde si on a candidat unique dans la colonne
			for (int k = 0; k < grid.numberCandidates(); k++) {
	
				//le candidat existe dans la ligne et qu'il est apparu une seule et unique fois
				if (tabI[k] == 1 && tabCell[k] != null) {
					for (ICell cell : grid.getCol(i)) {
						r.addCell(CellSetName.DECISIVE_UNITS, cell);
					}
					r.addCell(CellSetName.DECISIVE_CELLS, tabCell[k]);
					int value = k + 1;
					r.addValue(value);
					String s = "Le candidat " + value + " n'est présent qu'une seule fois dans cette colonne.";
					r.setDescription(s);
					return r;
				}
			}
		}
		//on regarde région par région
		int nbSW = grid.getNumberSectorByWidth();
		int nbSH = grid.getNumberSectorByHeight();
		for (int i = 0; i < grid.getWidthSector(); i++) {
			for (int j = 0; j < grid.getHeightSector(); j++) {
				for (int k = 0; k < grid.numberCandidates(); k++) {
					tabI[k] = 0;
					tabCell[k] = null;
				}
				for (int m = i * nbSW; m < grid.getHeightSector() * (i + 1); m++) {
					for (int n = j * nbSH; n < grid.getWidthSector() * (j + 1); n++) {
						c = tabC[m][n];
						//cellule modifiable sans valeur
						if (c.isModifiable() && ! c.hasValue()) {
							boolean [] tabB = c.candidates();
							//parcourt tableau des candidats de la cellule
							for (int k = 0; k < tabB.length; k++) {
								//le candidat existe dans la cellule et qu'il n'est pas déjà apparu
								if (tabB[k]) {
									tabI[k] += 1; 
									tabCell[k] = c;
								}
								
							}
						}
					}
				}
				// A la fin de chaque région on regarde si on a candidat unique dans la région
				for (int k = 0; k < grid.numberCandidates(); k++) {
					//le candidat existe dans la ligne et qu'il est apparu une seule et unique fois
					if (tabI[k] == 1 && tabCell[k] != null) {
						for (ICell cell : grid.getSector(j, i)) {
							r.addCell(CellSetName.DECISIVE_UNITS, cell);
						}
						r.addCell(CellSetName.DECISIVE_CELLS, tabCell[k]);
						int value = k + 1;
						r.addValue(value);
						String s = "Le candidat " + value + " n'est présent qu'une seule fois dans cette région.";
						r.setDescription(s);
						return r;
					}
				}
			}
		}
		return null;
	}
}