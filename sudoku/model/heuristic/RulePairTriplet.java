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
		Contract.checkCondition(tabC != null);
		boolean existRow = false;
		boolean existCol = false;
		int row = -1;
		int col = -1;
		Set<ICoord> decisiveCellsRow = new HashSet<ICoord>();
		Set<ICoord> decisiveCellsCol = new HashSet<ICoord>();
		
		//type1
		//on regarde région par région
		int nbSW = grid.getNumberSectorByWidth();
		int nbSH = grid.getNumberSectorByHeight();
		for (int k = 1; k <= grid.numberCandidates(); k++) {
			for (int i = 0; i < grid.getWidthSector(); i++) {
				for (int j = 0; j < grid.getHeightSector(); j++) {
					for (int m = i * nbSW; m < grid.getHeightSector() * (i + 1); m++) {
						for (int n = j * nbSH; n < grid.getWidthSector() * (j + 1); n++) {
							if (tabC[m][n].isModifiable()) {
								if (tabC[m][n].isCandidate(k)) {
									if (!existRow) {
										existRow = true;
										row = m;
									}
									if (existRow) {
										if (row != m) {
											row = -1;
											decisiveCellsRow.clear();
										} else {
											decisiveCellsRow.add(new Coord(m, n));
										}
									}
									
									if (!existCol) {
										existCol = true;
										col = n;
									}
									if (existCol) {
										if (col != n) {
											col = -1;
											decisiveCellsCol.clear();
										} else {
											decisiveCellsCol.add(new Coord(m, n));
										}
									}
								}
							}
						}
					}
					if ((row != -1 && decisiveCellsRow.size() > 1) || (col != -1  && decisiveCellsCol.size() > 1)) {
						RemoveCandidateReport r = new RemoveCandidateReport(grid);
						r.addValue(k);
						if (row != -1) {
							for (ICoord coord : decisiveCellsRow) {
								r.addCell(CellSetName.DECISIVE_CELLS, coord);
								for(ICoord cd : grid.getSector(coord.getRow(), coord.getCol())) {
									r.addCell(CellSetName.DECISIVE_UNITS, cd);
								}
							}
							for (int l = 0; l < grid.size(); l++) {
								ICoord coord = new Coord(row, l);
								if (grid.getCell(coord).isCandidate(k) && grid.getCell(coord).isModifiable()
										&& !r.getCellSet(CellSetName.DECISIVE_CELLS).contains(coord)) {
									r.addCell(CellSetName.DELETION_CELLS, coord);
								} else {
									r.addCell(CellSetName.DELETION_UNITS, coord);
								}
							}
						} else {
							for (ICoord coord : decisiveCellsCol) {
								r.addCell(CellSetName.DECISIVE_CELLS, coord);
								for(ICoord cd : grid.getSector(coord.getRow(), coord.getCol())) {
									r.addCell(CellSetName.DECISIVE_UNITS, cd);
								}
							}
							for (int l = 0; l < grid.size(); l++) {
								ICoord coord = new Coord(l, col);
								if (grid.getCell(coord).isCandidate(k) && grid.getCell(coord).isModifiable()
										&& !r.getCellSet(CellSetName.DECISIVE_CELLS).contains(coord)) {
									r.addCell(CellSetName.DELETION_CELLS, coord);
								} else {
									r.addCell(CellSetName.DELETION_UNITS, coord);
								}
							}
						}
						

						if (r.getCellSet(CellSetName.DELETION_CELLS).size() == 0) {
							r = null;
							break;
						}
						String s = "Les " + r.getCellSet(CellSetName.DECISIVE_CELLS).size() + " candidats ";
						s += k + " alignés dans cette région, donnent la possibilité de" 
								+ " supprimer les " + k + " dans les autres régions de cette colonne.";
						r.setDescription(s);
						return r;
					}
					existRow = false;
					existCol = false;
					row = -1;
					col = -1;
				}
			}
		}
		
		//type 2
		
		
		return null;
	}
}
