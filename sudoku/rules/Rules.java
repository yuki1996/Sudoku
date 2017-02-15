package sudoku.rules;

import util.Contract;

public enum Rules {
	ONE_CANDIDATE() {
		String describe(Report report) {
			Contract.checkCondition(report != null);
			return "";
		}
		Report generateReport() {
			return null;
		}
		void execute(Report report) {
			Contract.checkCondition(report != null);
		}
		
	},
	ONLY_CANDIDATE() {
		String describe(Report report) {
			Contract.checkCondition(report != null);
			return "";
		}
		Report generateReport() {
			return null;
		}
		void execute(Report report) {
			Contract.checkCondition(report != null);
		}
		
	},
	PAIR_TRIPLET() {
		String describe(Report report) {
			Contract.checkCondition(report != null);
			return "";
		}
		Report generateReport() {
			return null;
		}
		void execute(Report report) {
			Contract.checkCondition(report != null);
		}
		
	};
	abstract String describe(Report report);
	abstract Report generateReport();
	abstract void execute(Report report);

}
