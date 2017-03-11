package sudoku.model.heuristic;

enum Rule {
	
	IDENTICAL_CANDIDATES(new IdenticalCandidates()),
	
	ONLY_CANDIDATE(new RuleOnlyCandidate()),
	
	ONE_CANDIDATE(null),
	
	PAIR_TRIPLET(new RulePairTriplet()),
	
	INTERACTION_BETWEEN_SECTORS(null),
	
	ISOLATED_GROUPS(null),
	
	MIXED_GROUPS(null),
	
	X_WING(null),
	
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
