package sudoku.model.heuristic;

import java.util.Vector;

enum Rule {

  ONE_CANDIDATE,

  ONLY_CANDIDATE,

  PAIR_TRIPLET,

  INTERACTION_BETWEEN_SECTORS,

  IDENTICAL_CANDIDATES,

  ISOLATED_GROUPS,

  MIXED_GROUPS,

  X_WING,

  XY_WING,

  XYZ_WING,

  UNITY,

  SWORDFISH,

  JELLYFISH_AND_SQUIRMBAG,

  BURMA,

  COLORING,

  TURBOT_FISH,

  XY_CHAIN,

  XY_COLORING,

  THREED_MEDUSA,

  CHAINED_FORCED_CANDIDATE,

  NISHIO,

  BRUTE_FORCE;

    public Vector  myReport;

  public abstract String describe(int report);

  public abstract Report generate();

  public abstract void execute(Report report);

}