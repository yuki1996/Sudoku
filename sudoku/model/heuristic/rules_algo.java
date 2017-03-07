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

enum rules_algo {
  
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

}
