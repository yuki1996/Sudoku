package sudoku.model.heuristic;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import sudoku.model.ICell;
import sudoku.model.IGrid;
import sudoku.util.ICoord;
import sudoku.util.Coord;
import util.Contract;

enum Rule {

  ONE_CANDIDATE() {
	@Override
	public String describe(Report report, IGrid g) {
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

	@Override
	public Report generate(IGrid grid) {
		Contract.checkCondition(grid != null);
		ICell [][] tabC = grid.cells();
		Contract.checkCondition(tabC != null);
		Report r = new Report();
		int row = -1;
		int col = -1;
		int k = 0;
		//on regarde ligne par ligne
		for (int l = 0; l < grid.numberCandidates(); l++) {
			for (int i = 0; i < grid.size(); i++) {
				for (int j = 0; j < grid.size(); j++) {
					if (k >= 2) {
						break;
					}
					if (tabC[i][j].isModifiable() && ! tabC[i][j].hasValue()) {
						if (tabC[i][j].candidates()[l]) {
							row = i;
							col = j;
							k++;
						}
					}
				}
				if (k == 1) {
					Set<ICoord> setCoord = new HashSet<ICoord>();
					for (int j = 0; j <= grid.size(); j++) {
						setCoord.add(new Coord(j,row));
					}
					r.setContextualCells(setCoord);
					Set<ICoord> setCell = new HashSet<ICoord>();
					setCell.add(new Coord(col, row));
					r.setDecisiveCells(setCell);
					Set<Integer> setValue = new HashSet<Integer>();
					setValue.add(l + 1);
					r.setValues(setValue);
					return r;
				}
				k = 0;
			}
		}
		//on regarde colonne par colonne
		for (int l = 0; l < grid.numberCandidates(); l++) {
			for (int i = 0; i < grid.size(); i++) {
				for (int j = 0; j < grid.size(); j++) {
					if (k >= 2) {
						break;
					}
					if (tabC[j][i].isModifiable() && ! tabC[j][i].hasValue()) {
						if (tabC[j][i].candidates()[l]) {
							row = j;
							col = i;
							k++;
						}
					}
				}
				if (k == 1) {
					Set<ICoord> setCoord = new HashSet<ICoord>();
					for (int j = 0; j <= grid.size(); j++) {
						setCoord.add(new Coord(col,j));
					}
					r.setContextualCells(setCoord);
					Set<ICoord> setCell = new HashSet<ICoord>();
					setCell.add(new Coord(col, row));
					r.setDecisiveCells(setCell);
					Set<Integer> setValue = new HashSet<Integer>();
					setValue.add(l + 1);
					r.setValues(setValue);
					return r;
				}
				k = 0;
			}
		}
		//on regarde région par région
		int nbSW = grid.getNumberSectorByWidth();
		int nbSH = grid.getNumberSectorByHeight();
		for (int l = 0; l < grid.numberCandidates(); l++) {
			for (int i = 0; i < grid.getWidthSector(); i++) {
				for (int j = 0; j < grid.getHeightSector(); j++) {
					for (int m = i * nbSH; m < grid.getWidthSector() * (i + 1); m++) {
						for (int n = j * nbSW; m < grid.getHeightSector() * (j + 1); m++) {
							if (k >= 2) {
								break;
							}
							if (tabC[m][n].isModifiable() && ! tabC[m][n].hasValue()) {
								if (tabC[m][n].candidates()[l]) {
									row = m;
									col = n;
									k++;
								}
							}
						}
					}
					
				}
				if (k == 1) {
					Set<ICoord> setCoord = new HashSet<ICoord>();
					for (int m = 0; m < grid.getNumberSectorByWidth(); m++) {
						for (int n = 0; n < grid.getNumberSectorByHeight(); n++) {
							setCoord.add(new Coord(col * grid.getWidthSector() + n,
									row * grid.getHeightSector() + m));
						}
					}
					r.setContextualCells(setCoord);
					Set<ICoord> setCell = new HashSet<ICoord>();
					setCell.add(new Coord(col, row));
					r.setDecisiveCells(setCell);
					Set<Integer> setValue = new HashSet<Integer>();
					setValue.add(l + 1);
					r.setValues(setValue);
					return r;
				}
				k = 0;
			}
		}
		return null;
	}

	@Override
	public void execute(Report report, IGrid grid) {
		Contract.checkCondition(report != null);
		Contract.checkCondition(grid != null);
		Iterator<ICoord> it1 = report.getDecisiveCells().iterator();
		Iterator<Integer> it2 = report.getValues().iterator();
		grid.changeValue(it1.next(), it2.next());
	}
  },
  
  ONLY_CANDIDATE() {
  @Override
	public String describe(Report report, IGrid g) {
	  Contract.checkCondition(report != null);
		Contract.checkCondition(g != null);
		if (!report.getValues().isEmpty()) {
			Iterator<Integer> it = report.getValues().iterator();
			String s = "Cette case contient un seul candidat avec le symbole " + it.next()+".";
			return s;
		}
		return null;
	}

	@Override
	public Report generate(IGrid grid) {
		Contract.checkCondition(grid != null);
		ICell [][] tabC = grid.cells();
		Contract.checkCondition(tabC != null);
		Report r = new Report();
		int row = -1;
		int col = -1;
		int k = 0;
		for (int l = 0; l < grid.numberCandidates(); l++) {
			for (int i = 0; i < grid.size(); i++) {
				for (int j = 0; j < grid.size(); j++) {
					if (k >= 2) {
						break;
					}
					if (tabC[i][j].isModifiable() && ! tabC[i][j].hasValue()) {
						if (tabC[i][j].candidates()[l]) {
							row = i;
							col = j;
							k++;
						}
					}
				}
				if (k == 1) {
					Set<ICoord> setCell = new HashSet<ICoord>();
					setCell.add(new Coord(col, row));
					r.setDecisiveCells(setCell);
					Set<Integer> setValue = new HashSet<Integer>();
					setValue.add(l + 1);
					r.setValues(setValue);
					return r;
				}
				k = 0;
			}
		}
		return null;
	}

	@Override
	public void execute(Report report, IGrid grid) {
		Contract.checkCondition(report != null);
		Contract.checkCondition(report != null);
		Contract.checkCondition(grid != null);
		Iterator<ICoord> it1 = report.getDecisiveCells().iterator();
		Iterator<Integer> it2 = report.getValues().iterator();
		grid.changeValue(it1.next(), it2.next());
	}
  },

  PAIR_TRIPLET() {
  @Override
	public String describe(Report report, IGrid g) {
		Contract.checkCondition(report != null);
		Contract.checkCondition(g != null);
		String s = "Les " + report.getDecisiveCells().size() + " candidats ";
		if (!report.getValues().isEmpty()) {
			Iterator<Integer> it = report.getValues().iterator();
			int value = it.next();
			s += value + " alignés dans cette région, donnent la possibilitée de" 
					+ " supprimer les " + value + " dans les autres régions de cette";
			switch (detecte_unit(report.getContextualCells(), g)) {
			case 0:
				s += "ligne.";
				return s;
			case 1: 
				s += "colonne.";
				return s;
			default:
				return null;
			}
		}
		return null;
	}

	@Override
	public Report generate(IGrid grid) {
		Contract.checkCondition(grid != null);
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(Report report, IGrid grid) {
		Contract.checkCondition(report != null);
		Contract.checkCondition(grid != null);
		Iterator<Integer> it = report.getValues().iterator();
		int value = it.next();
		for (ICoord c : report.getDeletionCells()) {
			grid.removeCandidate(c, value);
		}
	}
  },
  
  INTERACTION_BETWEEN_SECTORS() {
	@Override
	public String describe(Report report, IGrid g) {
		Contract.checkCondition(report != null);
		Contract.checkCondition(g != null);
		if (!report.getValues().isEmpty()) {
			Iterator<Integer> it = report.getValues().iterator();
			int value = it.next();
			String s = "Le candidat " + value + " n'est présent que dans les deux";
			String zone;
			switch (detecte_unit(report.getContextualCells(), g)) {
			case 0:
				zone = "ligne";
				break;
			case 1: 
				zone = "colonne";
				break;
			default:
				return null;
			}
			s += zone + " de 2 régions alignées. On peut donc le supprimer dans ces " + zone 
					+ " de la 3ème régions.";
			return s;
		}
		return null;
	}

	@Override
	public Report generate(IGrid grid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(Report report, IGrid grid) {
		Contract.checkCondition(report != null);
		Contract.checkCondition(grid != null);
		Iterator<Integer> it = report.getValues().iterator();
		int value = it.next();
		for (ICoord c : report.getDeletionCells()) {
			grid.removeCandidate(c, value);
		}
	}
	  
  };
//  
//  IDENTICAL_CANDIDATES,
//
//  ISOLATED_GROUPS,

//  MIXED_GROUPS,
//
//  X_WING,
//
//  XY_WING,
//
//  XYZ_WING,
//
//  UNITY,
//
//  SWORDFISH,
//
//  JELLYFISH_AND_SQUIRMBAG,
//
//  BURMA,
//
//  COLORING,
//
//  TURBOT_FISH,
//
//  XY_CHAIN,
//
//  XY_COLORING,
//
//  THREED_MEDUSA,
//
//  CHAINED_FORCED_CANDIDATE,
//
//  NISHIO,
//
//  BRUTE_FORCE;

  public Vector myReport;

  public abstract String describe(Report report, IGrid g);

  public abstract Report generate(IGrid grid);

  public abstract void execute(Report report, IGrid grid);

	
	class Report {
	
	  private Rule rule;
	  //cases bleu foncées
	  private Set<ICoord> decisiveCells;
	  //cases bleu claires
	  private Set<ICoord> contextualCells;
	  //cases rouge claires
	  private Set<ICoord> deletionCells;
	  //cases rouge foncées
	  private Set<ICoord>actualDeletionCells;
	  private Set<Integer> values;
	  
	  //CONSTRUCTEURS
	  public Report() {
		rule = null;
		decisiveCells = new HashSet<ICoord>();
		contextualCells = new HashSet<ICoord>();
		deletionCells = new HashSet<ICoord>();
		actualDeletionCells = new HashSet<ICoord>();
		values = new HashSet<Integer>();
	  }
	  
	  //REQUÊTES
	  public String describe(IGrid g) {
		return rule.describe(this, g);
	  }
	  public Set<ICoord> getDecisiveCells() {
		  return decisiveCells;
	  }
	  public Set<ICoord> getContextualCells() {
		  return contextualCells;
	  }
	  public Set<ICoord> getDeletionCells() {
		  return deletionCells;
	  }
	  public Set<ICoord> getActualDeletionCells() {
		  return actualDeletionCells;
	  }
	  public Set<Integer> getValues() {
		  return values;
	  }
	  
	  
	  //COMMANDES
	  public void execute(IGrid g) {
		  Contract.checkCondition(g != null);
		  rule.execute(this, g);
	  }
	  
	  public void setRule(Rule r) {
		  Contract.checkCondition(r != null);
		  rule = r;
		  decisiveCells.clear();
		  contextualCells.clear();
		  deletionCells.clear();
		  actualDeletionCells.clear();
		  values.clear();
	  }
	  
	  public void setDecisiveCells(Set<ICoord> set) {
		  Contract.checkCondition(set != null);
		  decisiveCells = set;
	  }
	  public void setContextualCells(Set<ICoord> set) {
		  Contract.checkCondition(set != null);
		  contextualCells = set;
	  }
	  public void setDeletionCells(Set<ICoord> set) {
		  Contract.checkCondition(set != null);
		  deletionCells = set;
	  }
	  public void setActualDeletionCells(Set<ICoord> set) {
		  Contract.checkCondition(set != null);
		  actualDeletionCells = set;
	  }
	  public void setValues(Set<Integer> set) {
		  Contract.checkCondition(set != null);
		  values = set;;
	  }
	
	}

	/**
	 * -1 : set n'est pas dans une zone définie
	 * 0 : set est une ligne
	 * 1 : set est une colonne
	 * 2 : set est une région
	 */
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
		
	}
}