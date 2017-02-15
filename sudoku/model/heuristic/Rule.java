package sudoku.model.heuristic;

import java.util.Vector;
import sudoku.model.IGrid;
import util.Contract;

enum Rule {

  ONE_CANDIDATE() {
	@Override
	public String describe(Report report) {
		Contract.checkCondition(report != null);
		return "";
	}

	@Override
	public Report generate(IGrid grid) {
		Contract.checkCondition(grid != null);
		return null;
	}

	@Override
	public void execute(Report report) {
		Contract.checkCondition(report != null);
	}
  },

  ONLY_CANDIDATE() {
  @Override
	public String describe(Report report) {
		Contract.checkCondition(report != null);
		return "";
	}

	@Override
	public Report generate(IGrid grid) {
		Contract.checkCondition(grid != null);
		return null;
	}

	@Override
	public void execute(Report report) {
		Contract.checkCondition(report != null);
	}
  },

  PAIR_TRIPLET() {
  @Override
	public String describe(Report report) {
		Contract.checkCondition(report != null);
		return "";
	}

	@Override
	public Report generate(IGrid grid) {
		Contract.checkCondition(grid != null);
		return null;
	}

	@Override
	public void execute(Report report) {
		Contract.checkCondition(report != null);
	}
  };

//  INTERACTION_BETWEEN_SECTORS,
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

  public abstract String describe(Report report);

  public abstract Report generate(IGrid grid);

  public abstract void execute(Report report);

}