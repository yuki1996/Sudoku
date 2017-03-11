package sudoku.model.heuristic;

import sudoku.model.CellModel;
import sudoku.model.GridModel;
import sudoku.model.heuristic.Report;
import sudoku.model.heuristic.Report.CellSetName;
import sudoku.util.Coord;
import sudoku.util.ICoord;
import util.Contract;

public class RuleOneCandidate extends ReportGenerator {

	@Override
	protected Report generate(GridModel grid) {
		Contract.checkCondition(grid != null);
		CellModel [][] tabC = grid.cells();
		Contract.checkCondition(tabC != null);
		CellModel c = null;
		Integer[] tabI = new Integer[grid.numberCandidates()];
		Integer[] tabCol = new Integer[grid.numberCandidates()];
		Integer[] tabRow = new Integer[grid.numberCandidates()];
		
		//on regarde ligne par ligne
		for (int i = 0; i < grid.size(); i++) {
			for (int k = 0; k < grid.numberCandidates(); k++) {
				tabI[k] = 0;
				tabCol[k] = -1;
				tabRow[k] = -1;
			}
			for (int j = 0; j < grid.size(); j++) {
				c = tabC[i][j];
				//cellule modifiable sans valeur
				if (!c.hasValue()) {
					//parcourt tableau des candidats de la cellule
					for (int k = 0; k < grid.numberCandidates(); k++) {
						//le candidat existe dans la cellule
						if (c.isCandidate(k + 1)) {
							tabI[k] += 1;
							tabCol[k] = i;
							tabRow[k] = j;
						}		
					}
				}
			}
			// A la fin de chaque ligne on regarde si on a candidat unique dans la ligne
			for (int k = 0; k < grid.numberCandidates(); k++) {
				//le candidat existe dans la ligne et qu'il est apparu une seule et unique fois
				if ( tabI[k] == 1) {
					int value = k + 1;
					ICoord coord = new Coord(tabCol[k],tabRow[k]);
					SetValueReport r = new SetValueReport(grid, coord, value);
					for (int j = 0; j < grid.numberCandidates(); j++) {
						r.addDecisiveUnits(new Coord(i,j));
					}
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
				tabCol[k] = -1;
				tabRow[k] = -1;
			}
			for (int j = 0; j < grid.size(); j++) {
				c = tabC[j][i];
				//cellule modifiable sans valeur
				if (c.isModifiable() && ! c.hasValue()) {
					//parcourt tableau des candidats de la cellule
					for (int k = 0; k < grid.numberCandidates(); k++) {
						//le candidat existe dans la cellule
						if (c.isCandidate(k + 1)) {
							tabI[k] += 1;
							tabCol[k] = j;
							tabRow[k] = i;
						}
					}
				}
			}
			
			// A la fin de chaque colonne on regarde si on a candidat unique dans la colonne
			for (int k = 0; k < grid.numberCandidates(); k++) {
	
				//le candidat existe dans la ligne et qu'il est apparu une seule et unique fois
				if (tabI[k] == 1) {
					int value = k + 1;
					ICoord coord = new Coord(tabCol[k],tabRow[k]);
					SetValueReport r = new SetValueReport(grid, coord, value);
					for (int j = 0; j < grid.numberCandidates(); j++) {
						r.addDecisiveUnits(new Coord(j,i));
					}
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
					tabCol[k] = -1;
					tabRow[k] = -1;
				}
				for (int m = i * nbSW; m < grid.getHeightSector() * (i + 1); m++) {
					for (int n = j * nbSH; n < grid.getWidthSector() * (j + 1); n++) {
						c = tabC[m][n];
						//cellule modifiable sans valeur
						if (c.isModifiable() && ! c.hasValue()) {
							//parcourt tableau des candidats de la cellule
							for (int k = 0; k < grid.numberCandidates(); k++) {
								//le candidat existe dans la cellule et qu'il n'est pas déjà apparu
								if (c.isCandidate(k + 1)) {
									tabI[k] += 1; 
									tabRow[k] = m;
									tabCol[k] = n;
								}
								
							}
						}
					}
				}
				// A la fin de chaque région on regarde si on a candidat unique dans la région
				for (int k = 0; k < grid.numberCandidates(); k++) {
					//le candidat existe dans la ligne et qu'il est apparu une seule et unique fois
					if (tabI[k] == 1) {
						int value = k + 1;
						ICoord coord = new Coord(tabRow[k],tabCol[k]);
						SetValueReport r = new SetValueReport(grid, coord, value);
						for (ICoord cd : grid.getSector(tabRow[k], tabCol[k])) {
							r.addDecisiveUnits(cd);
						}
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