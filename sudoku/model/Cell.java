package sudoku.model;

import util.Contract;

public class Cell implements ICell {
	//ATTRIBUTS
	private int value;
	private final boolean modifiable;
	private boolean[] possibilities;
	
	//CONSTRUCTEURS
	
	public Cell(int cardinal) {
		Contract.checkCondition(cardinal > 0, "value doit être strictement positif.");
		value = 0;
		modifiable = true;
		possibilities = new boolean[cardinal];
		for (int i = 0; i < cardinal; ++i) {
			possibilities[i] = true;
		}
	}
	
	public Cell(int value, boolean modifiable, int cardinal) {
		Contract.checkCondition(cardinal > 0, "cardinal doit être strictement positif.");
		Contract.checkCondition(value > 0 && value <= cardinal, 
				"value doit être strictement positif.");
		this.value = value;
		this.modifiable = modifiable;
		possibilities = new boolean[cardinal];
		for (int i = 0; i < cardinal; ++i) {
			possibilities[i] = true;
		}
	}
	
	public Cell(boolean []possibilities) {
		Contract.checkCondition(possibilities.length > 0, 
				"le tableau doit être strictement positif.");
		value = 0;
		modifiable = true;
		this.possibilities = possibilities.clone();
	}
	
	//REQUETES

	/**
	 * Renvoie le nombre de possibilité.
	 */
	public int getCardinalPossibilities() {
		return possibilities.length;
	}
	
	@Override
	public int getValue() {
		return  value;
	}

	@Override
	public boolean canTakeValue(int n) {
		Contract.checkCondition(n > 0 && n < possibilities.length);
		return possibilities[n - 1];
	}

	@Override
	public boolean hasValue() {
		return value != 0;
	}

	@Override
	public boolean isModifiable() {
		return modifiable;
	}

	@Override
	public boolean[] possibilities() {
		return possibilities.clone();
	}
	
	//COMMANDES
	@Override
	public void setValue(int n) {
		Contract.checkCondition(n > 0 && n < possibilities.length);
		Contract.checkCondition(canTakeValue(n));
		value = n;
	}

	@Override
	public void removeValue() {
		Contract.checkCondition(modifiable);
		value = 0;
	}

	@Override
	public void addPossibility(int n) {
		Contract.checkCondition(n > 0 && n < possibilities.length);
		Contract.checkCondition(modifiable);
		possibilities[n - 1] = true;
	}

	@Override
	public void removePossibility(int n) {
		Contract.checkCondition(n > 0 && n < possibilities.length);
		Contract.checkCondition(modifiable);
		possibilities[n - 1] = false;
	}

}
