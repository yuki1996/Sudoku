package sudoku.model.heuristic;

import java.util.Set;

import sudoku.model.ICell;
import sudoku.model.IGrid;
import sudoku.model.heuristic.Report;
import sudoku.model.heuristic.Report.CellSetName;
import sudoku.util.ICoord;
import util.Contract;

public class RuleOneCandidate extends ReportGenerator {

	@Override
	protected Report generate(IGrid grid) {
		Contract.checkCondition(grid != null);
		ICell [][] tabC = grid.cells();
		Contract.checkCondition(tabC != null);
		Report r = new Report();
		ICell c = null;
		Object [][] tab = new Object[grid.numberCandidates()][2];
		
		//on regarde ligne par ligne
		for (int i = 0; i < grid.size(); i++) {
			for (int j = 0; j < grid.size(); j++) {
				
				for (int k = 0; k < grid.numberCandidates(); k++) {
					tab[k][0] = false;
					tab[k][1] = null;
				}
				c = tabC[i][j];
				//cellule modifiable sans valeur
				if (c.isModifiable() && ! c.hasValue()) {
					boolean [] tabB = c.candidates();
					//parcourt tableau des candidats de la cellule
					for (int k = 0; k < tabB.length; k++) {
		
						//le candidat existe dans la cellule et qu'il est déjà apparu
						if (tabB[k] && (Boolean) tab[k][0]) {
							tab[k][1] = null;
						}
						//le candidat existe dans la cellule et qu'il n'est pas déjà apparu
						if (tabB[k] && !(Boolean)tab[k][0]) {
							tab[k][0] = true;
							tab[k][1] = c;
						}
						
					}
				}
		
				// A la fin de chaque région on regarde si on a candidat unique dans la ligne
				for (int k = 0; k < grid.numberCandidates(); k++) {
		
					//le candidat existe dans la ligne et qu'il est apparu une seule et unique fois
					if ((Boolean) tab[k][0] && tab[k][1] != null) {
						for (ICell cell : grid.getCol(j)) {
							r.addCell(CellSetName.DECISIVE_UNITS, cell);
						}
						r.addCell(CellSetName.DECISIVE_CELLS, (ICell) tab[k][1]);
						r.addValue(k + 1);
						return r;
					}
				}
			}
		}
		//on regarde colonne par colonne
		for (int i = 0; i < grid.size(); i++) {
			for (int j = 0; j < grid.size(); j++) {
				
				for (int k = 0; k < grid.numberCandidates(); k++) {
					tab[k][0] = false;
					tab[k][1] = null;
				}
				c = tabC[j][i];
				//cellule modifiable sans valeur
				if (c.isModifiable() && ! c.hasValue()) {
					boolean [] tabB = c.candidates();
					//parcourt tableau des candidats de la cellule
					for (int k = 0; k < tabB.length; k++) {
		
						//le candidat existe dans la cellule et qu'il est déjà apparu
						if (tabB[k] && (Boolean) tab[k][0]) {
							tab[k][1] = null;
						}
						//le candidat existe dans la cellule et qu'il n'est pas déjà apparu
						if (tabB[k] && !(Boolean)tab[k][0]) {
							tab[k][0] = true;
							tab[k][1] = c;
						}
						
					}
				}
		
				// A la fin de chaque région on regarde si on a candidat unique dans la colonne
				for (int k = 0; k < grid.numberCandidates(); k++) {
		
					//le candidat existe dans la ligne et qu'il est apparu une seule et unique fois
					if ((Boolean) tab[k][0] && tab[k][1] != null) {
						for (ICell cell : grid.getRow(i)) {
							r.addCell(CellSetName.DECISIVE_UNITS, cell);
						}
						r.addCell(CellSetName.DECISIVE_CELLS, (ICell) tab[k][1]);
						r.addValue(k + 1);
						return r;
					}
				}
			}
		}
		//on regarde région par région
		int nbSW = grid.getNumberSectorByWidth();
		int nbSH = grid.getNumberSectorByHeight();
		for (int i = 0; i < grid.getWidthSector(); i++) {
			for (int j = 0; j < grid.getHeightSector(); j++) {
				for (int k = 0; k < grid.numberCandidates(); k++) {
					tab[k][0] = false;
					tab[k][1] = null;
				}
				for (int m = i * nbSW; m < grid.getHeightSector() * (i + 1); m++) {
					for (int n = j * nbSH; n < grid.getWidthSector() * (j + 1); n++) {
						
						c = tabC[m][n];
						//cellule modifiable sans valeur
						if (c.isModifiable() && ! c.hasValue()) {
							boolean [] tabB = c.candidates();
							//parcourt tableau des candidats de la cellule
							for (int k = 0; k < tabB.length; k++) {
		
								//le candidat existe dans la cellule et qu'il est déjà apparu
								if (tabB[k] && (Boolean) tab[k][0]) {
									tab[k][1] = null;
								}
								//le candidat existe dans la cellule et qu'il n'est pas déjà apparu
								if (tabB[k] && !(Boolean)tab[k][0]) {
									tab[k][0] = true;
									tab[k][1] = c;
								}
								
							}
						}
					}
				}
				// A la fin de chaque région on regarde si on a candidat unique dans la région
				for (int k = 0; k < grid.numberCandidates(); k++) {
					//le candidat existe dans la ligne et qu'il est apparu une seule et unique fois
					if ((Boolean) tab[k][0] && tab[k][1] != null) {
						for (ICell cell : grid.getSector(j, i)) {
							r.addCell(CellSetName.DECISIVE_UNITS, cell);
						}
						r.addCell(CellSetName.DECISIVE_CELLS, (ICell) tab[k][1]);
						r.addValue(k + 1);
						return r;
					}
				}
			}
		}
		return null;
	}

	/*
	protected String describe(IGrid g) {
		Contract.checkCondition(report != null);
		Contract.checkCondition(g != null);
		if (!report.getValues().isEmpty()) {
			Iterator<Integer> it = report.getValues().iterator();
			String s = "Le candidat " + it.next() + " n'est présent qu'une seule fois" 
					+ " dans cette ";
			switch (detecte_unit(report.getContextualCells(), g)) {
			case 0:
				s += "ligne.";
				return s;
			case 1: 
				s += "colonne.";
				return s;
			case 2:
				s += "région.";
				return s;
			default:
				return null;
			}
		}
		return null;
	}

	// -1 : set n'est pas dans une zone définie
	// 0 : set est une ligne
	// 1 : set est une colonne
	// 2 : set est une région
	private static int detecte_unit(Set<ICoord> set, IGrid grid) {
		assert set != null;
		assert grid != null;
		//teste si c'est une ligne
		boolean bool = true;
		int row = set.iterator().next().getRow();
		for (ICoord c : set) {
			bool &= (row == c.getRow());
			if (!bool) {
				break;
			}
		}
		if (bool) {
			return 0;
		}
		//teste si c'est une colonne
		bool = true;
		int col = set.iterator().next().getCol();
		int ws = grid.getWidthSector();
		int hs = grid.getHeightSector();
		for (ICoord c : set) {
			bool &= ((col / hs) == (c.getCol() / hs)) && ((row / ws) == (c.getRow() / ws));
			if (!bool) {
				break;
			}
		}
		if (bool) {
			return 1;
		}
		
		//teste si c'est une région
		bool = true;
		for (ICoord c : set) {
			bool &= (col == c.getCol());
			if (!bool) {
				break;
			}
		}
		if (bool) {
			return 2;
		}
		return -1;
		
	}*/

}