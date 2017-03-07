package sudoku.util;

public final class Coord implements ICoord {
    
    // ATTRIBUTS
    
    private final int col;
    private final int row;

    // CONSTRUCTEURS
    
    public Coord( int r,int c) {
        row = r;
        col = c;
    }
    
    // REQUETES
    
    public int getCol() {
        return col;
    }
    
    public int getRow() {
        return row;
    }
    
    public String toString() {
        return String.valueOf(col) + String.valueOf(row);
    }
    
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Coord c = (Coord) obj;
        return c.col == this.col && c.row == this.row;
    }
    
    public int hashCode() {
        return getRow() + getCol();
    }
    
    public boolean isOnSameRow(ICoord c) {
    	return row == c.getRow();
    }
    
    public boolean isOnSameCol(ICoord c) {
    	return col == c.getCol();
    }
}
