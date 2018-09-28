import java.io.Serializable;

public class Location implements Serializable {

	private static final long serialVersionUID = -1080700555170258302L;

	private int Y;
	private int X;

	public Location(int Y, int X) {
		this.Y = Y;
		this.X = X;
	}

	public int getY() {
		return this.Y;
	}

	public int getX() {
		return this.X;
	}
}