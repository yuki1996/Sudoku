package sudoku.model;

import java.util.Arrays;

import util.Contract;

public class Cell implements ICell {
	//ATTRIBUTS
	private int value;
	private boolean modifiable;
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

	public int getCardinalPossibilities() {
		return possibilities.length;
	}
	
	@Override
	public int getValue() {
		return value;
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
	

	public boolean canTakeValue(int n) {
		Contract.checkCondition(n > 0 && n < possibilities.length);
		return possibilities[n - 1];
	}
	
	public Object clone() {
		Cell clone = null;
		try {
			clone = (Cell) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError("echec clonage");
		}
		clone.value = this.getValue();
		clone.modifiable = this.isModifiable();
		clone.possibilities = this.possibilities();
		return clone;
		
	}
	
	public boolean equals(Object obj) {
		if ((obj != null) && (obj.getClass() == this.getClass())) {
			Cell o = (Cell) obj;
			return this.isModifiable() == o.isModifiable() 
					&& this.getValue() == o.getValue()
					&& Arrays.equals(this.possibilities(),o.possibilities());
		}
		return false;
		
	}
	
	//COMMANDES
	@Override
	public void setValue(int n) {
		Contract.checkCondition(isModifiable());
		Contract.checkCondition(n > 0 && n <= possibilities.length);
		value = n;
	}

	@Override
	public void removeValue() {
		Contract.checkCondition(modifiable);
		value = 0;
	}

	@Override
	public void addPossibility(int n) {
		Contract.checkCondition(n > 0 && n <= possibilities.length);
		Contract.checkCondition(modifiable);
		possibilities[n - 1] = true;
	}

	@Override
	public void removePossibility(int n) {
		Contract.checkCondition(n > 0 && n <= possibilities.length);
		Contract.checkCondition(modifiable);
		possibilities[n - 1] = false;
	}
	
	public void setModifiable(boolean bool) {
		modifiable = bool;
	}
	
	public void reset() {
		for (int i = 1; i <= possibilities.length; i++) {
			addPossibility(i);
		}
		modifiable = true;
		value = 0;
	}

}
