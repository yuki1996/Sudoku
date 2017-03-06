
public interface IModifiable {
	boolean[] possibilities();
	void setValue(int value);
	void addPossibility(int value);
	void removePossibility(int value);
}
