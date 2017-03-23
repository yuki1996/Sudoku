package sudoku.model.heuristic;

enum Rule {
	
	
	ONE_CANDIDATE(new RuleOneCandidate()),
	
	ONLY_CANDIDATE(new RuleOnlyCandidate()),
	
	PAIR_TRIPLET(null),
	
	INTERACTION_BETWEEN_SECTORS(new RuleInteractionBetweenSector()),
	
	IDENTICAL_CANDIDATES(new IdenticalCandidates()),
	
	X_WING(new RuleXWing()),
	
	ISOLATED_GROUPS(new RuleIsolatedGroups()),
	
	MIXED_GROUPS(new RuleMixedGroups()),
	
	XY_WING(null),
	
	XYZ_WING(null),
	
	UNITY(null),
	
	SWORDFISH(null),
	
	JELLYFISH_AND_SQUIRMBAG(null),
	
	BURMA(null),
	
	COLORING(null),
	
	TURBOT_FISH(null),
	
	XY_CHAIN(null),
	
	COLORED_XY(null),
	
	XY_COLORING(null),
	
	THREED_MEDUSA(null),
	
	CHAINED_FORCED_CANDIDATE(null),
	
	NISHIO(null),
	
	BRUTE_FORCE(null);
	
	private ReportGenerator myGenerator;
	
	Rule(ReportGenerator generator) {
		myGenerator = generator;
	}
	
	public ReportGenerator getGenerator() {
		return myGenerator;
	}
	
}
