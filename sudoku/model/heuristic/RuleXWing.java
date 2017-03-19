package sudoku.model.heuristic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sudoku.model.CellModel;
import sudoku.model.GridModel;
import sudoku.model.heuristic.Report.CellSetName;
import sudoku.util.Coord;
import sudoku.util.ICoord;
import util.Contract;

public class RuleXWing extends ReportGenerator {

	@Override
	protected Report generate(GridModel grid) {
		Contract.checkCondition(grid != null);
		CellModel [][] tabC = grid.cells();
		Contract.checkCondition(tabC != null);
		CellModel c;
		Map <Integer, List<ICoord>> mapCandidate = new HashMap<Integer, List<ICoord>>() ;
		Map <Integer, List<ICoord>> mapCell = new HashMap<Integer, List<ICoord>>() ;
		
		for (int k = 1; k <= grid.numberCandidates(); k++) {
			mapCell.put(k, new ArrayList<ICoord>());
		}
		//on regarde ligne par ligne
		for (int i = 0; i < grid.size(); i++) {
			for (int k = 1; k <= grid.numberCandidates(); k++) {
				mapCandidate.put(k, new ArrayList<ICoord>());
			}
			for (int j = 0; j < grid.size(); j++) {
				c = tabC[i][j];
				for (int k = 1; k <= grid.numberCandidates(); k++) {
					//cellule modifiable sans valeur
					if (!c.hasValue()) {
						//le candidat existe dans la cellule
						if (c.isCandidate(k)) {
							List<ICoord> l = mapCandidate.get(k);
							l.add(new Coord(i, j));
							mapCandidate.put(k, l);

						}	
					}
				}
			}
			for (int k = 1; k <= grid.numberCandidates(); k++) {
				List<ICoord> l = mapCandidate.get(k);
				if (l.size() == 2) {
					List<ICoord> lcoord = mapCell.get(k);
					for (ICoord coord : lcoord) {
						if (l.get(0).isOnSameCol(coord)) {
							for (ICoord coord2 : lcoord) {
								if (l.get(1).isOnSameCol(coord2)) {

									RemoveCandidateReport r = new RemoveCandidateReport(grid);
									r.addValue(k);
									r.addCell(CellSetName.DECISIVE_CELLS, coord);
									r.addCell(CellSetName.DECISIVE_CELLS, coord2);
									r.addCell(CellSetName.DECISIVE_CELLS, l.get(0));
									r.addCell(CellSetName.DECISIVE_CELLS, l.get(1));
									for (ICoord cd : grid.getRow(l.get(0))) {
										if (!r.getCellSet(CellSetName.DECISIVE_CELLS).contains(cd)) {
											r.addCell(CellSetName.DECISIVE_UNITS, cd);
										}
									}
									for (ICoord cd : grid.getRow(l.get(1))) {
										if (!r.getCellSet(CellSetName.DECISIVE_CELLS).contains(cd)) {
											r.addCell(CellSetName.DECISIVE_UNITS, cd);
										}
									}
									for (ICoord cd : grid.getCol(l.get(0))) {
										if (grid.getCell(cd).isCandidate(k) && !grid.getCell(cd).hasValue() 
												&& !r.getCellSet(CellSetName.DECISIVE_CELLS).contains(cd)) {
											r.addCell(CellSetName.DELETION_CELLS, cd);
										} else {
											r.addCell(CellSetName.DELETION_UNITS, cd);
										}
									}
									for (ICoord cd : grid.getCol(l.get(1))) {
										if (grid.getCell(cd).isCandidate(k) && !grid.getCell(cd).hasValue() 
												&& !r.getCellSet(CellSetName.DECISIVE_CELLS).contains(cd)) {
											r.addCell(CellSetName.DELETION_CELLS, cd);
										} else {
											//verifier
											r.addCell(CellSetName.DELETION_UNITS, cd);
										}
									}
									if (r.getCellSet(CellSetName.DELETION_CELLS).size() != 0) {
										String s = "X-Wing avec les " + k + ". Il est possible de supprimer les candidats "
														+ k + " des cases en rouge.";
										r.setDescription(s);
										return r;
									}
								}
							}
						}
						if (l.get(0).isOnSameSector(coord, grid.getNumberSectorByHeight(), grid.getNumberSectorByWidth())) {
							for (ICoord coord2 : lcoord) {
								if (l.get(1).isOnSameSector(coord2, grid.getNumberSectorByHeight(), grid.getNumberSectorByWidth())) {
									RemoveCandidateReport r = new RemoveCandidateReport(grid);
									r.addValue(k);
									r.addCell(CellSetName.DECISIVE_CELLS, coord);
									r.addCell(CellSetName.DECISIVE_CELLS, coord2);
									r.addCell(CellSetName.DECISIVE_CELLS, l.get(0));
									r.addCell(CellSetName.DECISIVE_CELLS, l.get(1));
									for (ICoord cd : grid.getRow(l.get(0))) {
										if (!r.getCellSet(CellSetName.DECISIVE_CELLS).contains(cd)) {
											r.addCell(CellSetName.DECISIVE_UNITS, cd);
										}
									}
									for (ICoord cd : grid.getRow(l.get(1))) {
										if (!r.getCellSet(CellSetName.DECISIVE_CELLS).contains(cd)) {
											r.addCell(CellSetName.DECISIVE_UNITS, cd);
										}
									}
									for (ICoord cd : grid.getSector(l.get(0))) {
										if (grid.getCell(cd).isCandidate(k) && !grid.getCell(cd).hasValue() 
												&& !r.getCellSet(CellSetName.DECISIVE_CELLS).contains(cd)) {
											r.addCell(CellSetName.DELETION_CELLS, cd);
										} else {
											//verifier
											r.addCell(CellSetName.DELETION_UNITS, cd);
										}
									}
									for (ICoord cd : grid.getSector(l.get(1))) {
										if (grid.getCell(cd).isCandidate(k) && !grid.getCell(cd).hasValue() 
												&& !r.getCellSet(CellSetName.DECISIVE_CELLS).contains(cd)) {
											r.addCell(CellSetName.DELETION_CELLS, cd);
										} else {
											//verifier
											r.addCell(CellSetName.DELETION_UNITS, cd);
										}
									}
									if (r.getCellSet(CellSetName.DELETION_CELLS).size() != 0) {
										String s = "X-Wing avec les " + k + ". Il est possible de supprimer les candidats "
														+ k + " des cases en rouge.";
										r.setDescription(s);
										return r;
									}
								}
							}
						}
					}
					lcoord.addAll(l);
					mapCell.put(k, lcoord);
				}
			}
		}
		
		
		for (int k = 1; k <= grid.numberCandidates(); k++) {
			mapCell.put(k, new ArrayList<ICoord>());
		}
		//on regarde colonne par colonne
		for (int i = 0; i < grid.size(); i++) {
			for (int k = 1; k <= grid.numberCandidates(); k++) {
				mapCandidate.put(k, new ArrayList<ICoord>());
			}
			for (int j = 0; j < grid.size(); j++) {
				c = tabC[j][i];
				for (int k = 1; k <= grid.numberCandidates(); k++) {
					//cellule modifiable sans valeur
					if (!c.hasValue()) {
						//le candidat existe dans la cellule
						if (c.isCandidate(k)) {
							List<ICoord> l = mapCandidate.get(k);
							l.add(new Coord(j, i));
							mapCandidate.put(k, l);

						}	
					}
				}
			}
			for (int k = 1; k <= grid.numberCandidates(); k++) {
				List<ICoord> l = mapCandidate.get(k);
				if (l.size() == 2) {
					List<ICoord> lcoord = mapCell.get(k);
					for (ICoord coord : lcoord) {
						if (l.get(0).isOnSameRow(coord)) {
							for (ICoord coord2 : lcoord) {
								if (l.get(1).isOnSameRow(coord2)) {

									RemoveCandidateReport r = new RemoveCandidateReport(grid);
									r.addValue(k);
									r.addCell(CellSetName.DECISIVE_CELLS, coord);
									r.addCell(CellSetName.DECISIVE_CELLS, coord2);
									r.addCell(CellSetName.DECISIVE_CELLS, l.get(0));
									r.addCell(CellSetName.DECISIVE_CELLS, l.get(1));
									for (ICoord cd : grid.getCol(l.get(0))) {
										if (!r.getCellSet(CellSetName.DECISIVE_CELLS).contains(cd)) {
											r.addCell(CellSetName.DECISIVE_UNITS, cd);
										}
									}
									for (ICoord cd : grid.getCol(l.get(1))) {
										if (!r.getCellSet(CellSetName.DECISIVE_CELLS).contains(cd)) {
											r.addCell(CellSetName.DECISIVE_UNITS, cd);
										}
									}
									for (ICoord cd : grid.getRow(l.get(0))) {
										if (grid.getCell(cd).isCandidate(k) && !grid.getCell(cd).hasValue() 
												&& !r.getCellSet(CellSetName.DECISIVE_CELLS).contains(cd)) {
											r.addCell(CellSetName.DELETION_CELLS, cd);
										} else {
											r.addCell(CellSetName.DELETION_UNITS, cd);
										}
									}
									for (ICoord cd : grid.getRow(l.get(1))) {
										if (grid.getCell(cd).isCandidate(k) && !grid.getCell(cd).hasValue() 
												&& !r.getCellSet(CellSetName.DECISIVE_CELLS).contains(cd)) {
											r.addCell(CellSetName.DELETION_CELLS, cd);
										} else {
											//verifier
											r.addCell(CellSetName.DELETION_UNITS, cd);
										}
									}
									if (r.getCellSet(CellSetName.DELETION_CELLS).size() != 0) {
										String s = "X-Wing avec les " + k + ". Il est possible de supprimer les candidats "
														+ k + " des cases en rouge.";
										r.setDescription(s);
										return r;
									}
								}
							}
						}
						if (l.get(0).isOnSameSector(coord, grid.getNumberSectorByHeight(), grid.getNumberSectorByWidth())) {
							for (ICoord coord2 : lcoord) {
								if (l.get(1).isOnSameSector(coord2, grid.getNumberSectorByHeight(), grid.getNumberSectorByWidth())) {
									RemoveCandidateReport r = new RemoveCandidateReport(grid);
									r.addValue(k);
									r.addCell(CellSetName.DECISIVE_CELLS, coord);
									r.addCell(CellSetName.DECISIVE_CELLS, coord2);
									r.addCell(CellSetName.DECISIVE_CELLS, l.get(0));
									r.addCell(CellSetName.DECISIVE_CELLS, l.get(1));
									for (ICoord cd : grid.getCol(l.get(0))) {
										if (!r.getCellSet(CellSetName.DECISIVE_CELLS).contains(cd)) {
											r.addCell(CellSetName.DECISIVE_UNITS, cd);
										}
									}
									for (ICoord cd : grid.getCol(l.get(1))) {
										if (!r.getCellSet(CellSetName.DECISIVE_CELLS).contains(cd)) {
											r.addCell(CellSetName.DECISIVE_UNITS, cd);
										}
									}
									for (ICoord cd : grid.getSector(l.get(0))) {
										if (grid.getCell(cd).isCandidate(k) && !grid.getCell(cd).hasValue() 
												&& !r.getCellSet(CellSetName.DECISIVE_CELLS).contains(cd)) {
											r.addCell(CellSetName.DELETION_CELLS, cd);
										} else {
											//verifier
											r.addCell(CellSetName.DELETION_UNITS, cd);
										}
									}
									for (ICoord cd : grid.getSector(l.get(1))) {
										if (grid.getCell(cd).isCandidate(k) && !grid.getCell(cd).hasValue() 
												&& !r.getCellSet(CellSetName.DECISIVE_CELLS).contains(cd)) {
											r.addCell(CellSetName.DELETION_CELLS, cd);
										} else {
											//verifier
											r.addCell(CellSetName.DELETION_UNITS, cd);
										}
									}
									if (r.getCellSet(CellSetName.DELETION_CELLS).size() != 0) {
										String s = "X-Wing avec les " + k + ". Il est possible de supprimer les candidats "
														+ k + " des cases en rouge.";
										r.setDescription(s);
										return r;
									}
								}
							}
						}
					}
					lcoord.addAll(l);
					mapCell.put(k, lcoord);
				}
			}
		}
		
		//on regarde région par région
		int nbSW = grid.getNumberSectorByWidth();
		int nbSH = grid.getNumberSectorByHeight();
		

		for (int k = 1; k <= grid.numberCandidates(); k++) {
			mapCell.put(k, new ArrayList<ICoord>());
		}
		
		for (int i = 0; i < grid.getWidthSector(); i++) {
			for (int j = 0; j < grid.getHeightSector(); j++) {
				for (int k = 1; k <= grid.numberCandidates(); k++) {
					mapCandidate.put(k, new ArrayList<ICoord>());
				}
				for (int m = i * nbSW; m < grid.getHeightSector() * (i + 1); m++) {
					for (int n = j * nbSH; n < grid.getWidthSector() * (j + 1); n++) {
						c = tabC[m][n];
						for (int k = 1; k <= grid.numberCandidates(); k++) {
							//cellule modifiable sans valeur
							if (!c.hasValue()) {
								//le candidat existe dans la cellule
								if (c.isCandidate(k)) {
									List<ICoord> l = mapCandidate.get(k);
									l.add(new Coord(m, n));
									mapCandidate.put(k, l);

								}	
							}
						}
					}
				}
				for (int k = 1; k <= grid.numberCandidates(); k++) {
					List<ICoord> l = mapCandidate.get(k);
					if (l.size() == 2) {
						List<ICoord> lcoord = mapCell.get(k);
						for (ICoord coord : lcoord) {
							if (l.get(0).isOnSameRow(coord)) {
								for (ICoord coord2 : lcoord) {
									if (l.get(1).isOnSameRow(coord2)) {

										RemoveCandidateReport r = new RemoveCandidateReport(grid);
										r.addValue(k);
										r.addCell(CellSetName.DECISIVE_CELLS, coord);
										r.addCell(CellSetName.DECISIVE_CELLS, coord2);
										r.addCell(CellSetName.DECISIVE_CELLS, l.get(0));
										r.addCell(CellSetName.DECISIVE_CELLS, l.get(1));
										for (ICoord cd : grid.getSector(l.get(0))) {
											if (!r.getCellSet(CellSetName.DECISIVE_CELLS).contains(cd)) {
												r.addCell(CellSetName.DECISIVE_UNITS, cd);
											}
										}
										for (ICoord cd : grid.getSector(l.get(1))) {
											if (!r.getCellSet(CellSetName.DECISIVE_CELLS).contains(cd)) {
												r.addCell(CellSetName.DECISIVE_UNITS, cd);
											}
										}
										for (ICoord cd : grid.getRow(l.get(0))) {
											if (grid.getCell(cd).isCandidate(k) && !grid.getCell(cd).hasValue() 
													&& !r.getCellSet(CellSetName.DECISIVE_CELLS).contains(cd)) {
												r.addCell(CellSetName.DELETION_CELLS, cd);
											} else {
												r.addCell(CellSetName.DELETION_UNITS, cd);
											}
										}
										for (ICoord cd : grid.getRow(l.get(1))) {
											if (grid.getCell(cd).isCandidate(k) && !grid.getCell(cd).hasValue() 
													&& !r.getCellSet(CellSetName.DECISIVE_CELLS).contains(cd)) {
												r.addCell(CellSetName.DELETION_CELLS, cd);
											} else {
												//verifier
												r.addCell(CellSetName.DELETION_UNITS, cd);
											}
										}
										if (r.getCellSet(CellSetName.DELETION_CELLS).size() != 0) {
											String s = "X-Wing avec les " + k + ". Il est possible de supprimer les candidats "
															+ k + " des cases en rouge.";
											r.setDescription(s);
											return r;
										}
									}
								}
							}
							if (l.get(0).isOnSameCol(coord)) {
								for (ICoord coord2 : lcoord) {
									if (l.get(1).isOnSameCol(coord2)) {
										RemoveCandidateReport r = new RemoveCandidateReport(grid);
										r.addValue(k);
										r.addCell(CellSetName.DECISIVE_CELLS, coord);
										r.addCell(CellSetName.DECISIVE_CELLS, coord2);
										r.addCell(CellSetName.DECISIVE_CELLS, l.get(0));
										r.addCell(CellSetName.DECISIVE_CELLS, l.get(1));
										for (ICoord cd : grid.getSector(l.get(0))) {
											if (!r.getCellSet(CellSetName.DECISIVE_CELLS).contains(cd)) {
												r.addCell(CellSetName.DECISIVE_UNITS, cd);
											}
										}
										for (ICoord cd : grid.getSector(l.get(1))) {
											if (!r.getCellSet(CellSetName.DECISIVE_CELLS).contains(cd)) {
												r.addCell(CellSetName.DECISIVE_UNITS, cd);
											}
										}
										for (ICoord cd : grid.getCol(l.get(0))) {
											if (grid.getCell(cd).isCandidate(k) && !grid.getCell(cd).hasValue() 
													&& !r.getCellSet(CellSetName.DECISIVE_CELLS).contains(cd)) {
												r.addCell(CellSetName.DELETION_CELLS, cd);
											} else {
												//verifier
												r.addCell(CellSetName.DELETION_UNITS, cd);
											}
										}
										for (ICoord cd : grid.getCol(l.get(1))) {
											if (grid.getCell(cd).isCandidate(k) && !grid.getCell(cd).hasValue() 
													&& !r.getCellSet(CellSetName.DECISIVE_CELLS).contains(cd)) {
												r.addCell(CellSetName.DELETION_CELLS, cd);
											} else {
												//verifier
												r.addCell(CellSetName.DELETION_UNITS, cd);
											}
										}
										if (r.getCellSet(CellSetName.DELETION_CELLS).size() != 0) {
											String s = "X-Wing avec les " + k + ". Il est possible de supprimer les candidats "
															+ k + " des cases en rouge.";
											r.setDescription(s);
											return r;
										}
									}
								}
							}
						}
						lcoord.addAll(l);
						mapCell.put(k, lcoord);
					}	
				}		
			}
		}
		return null;
	}

}
