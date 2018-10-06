import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class GameState implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2594162138114462023L;
	private static final String TREASURE_MARKING = "*";

	private final MazeDimensions mazeDimensions;
	private LinkedHashMap<String, IPlayer> playerMap;
	private String[][] maze;
	private Map<String, Location> playerLocationMap;
	private Map<String, Integer> playerScore;

	private AtomicInteger version;

	/**
	 * Constructor
	 *
	 * @param mazeDimensions
	 */
	public GameState(MazeDimensions mazeDimensions) {
		this.mazeDimensions = mazeDimensions;
		this.playerMap = new LinkedHashMap<>();
		this.playerLocationMap = new HashMap<>();
		this.playerScore = new HashMap<>();
		this.generateMaze(mazeDimensions.getN(), mazeDimensions.getK());
		this.version = new AtomicInteger();
	}

	/**
	 * 
	 * @return
	 */
	public int getVersion() {
		return this.version.get();
	}

	/**
	 *
	 * @param playerMap
	 */
	public void setPlayerMap(LinkedHashMap<String, IPlayer> playerMap) {
		this.playerMap = playerMap;
		this.version.incrementAndGet();
	}

	/**
	 *
	 * @return
	 */
	public LinkedHashMap<String, IPlayer> getPlayerMap() {
		return playerMap;
	}

	/**
	 *
	 * @return
	 */
	public MazeDimensions getMazeDimensions() {
		return mazeDimensions;
	}

	public String[][] getMaze() {
		return this.maze;
	}

	public Map<String, Integer> getPlayerScore() {
		return this.playerScore;
	}

	private void generateMaze(int N, int K) {
		this.maze = new String[N][N];
		for (int i = 0; i < K; i++) {
			addTreasure();
		}
	}

	private void addTreasure() {
		int N = this.mazeDimensions.getN();
		while (true) {
			int Y = ThreadLocalRandom.current().nextInt(0, N);
			int X = ThreadLocalRandom.current().nextInt(0, N);
			if (this.maze[Y][X] == null) {
				this.maze[Y][X] = TREASURE_MARKING;
				break;
			}
		}
	}

	public void addPlayer(String playerName) {
		int N = this.mazeDimensions.getN();
		while (true) {
			int Y = ThreadLocalRandom.current().nextInt(0, N);
			int X = ThreadLocalRandom.current().nextInt(0, N);
			if (this.maze[Y][X] == null) {
				this.maze[Y][X] = playerName;
				this.playerLocationMap.put(playerName, new Location(Y, X));
				this.playerScore.put(playerName, 0);
				break;
			}
		}
		this.version.incrementAndGet();
	}

	public void removePlayer(String playerName) {
		Location location = this.playerLocationMap.get(playerName);
		if (location != null) {
			int Y = location.getY();
			int X = location.getX();
			this.maze[Y][X] = null;
			this.playerLocationMap.remove(playerName);
			this.playerScore.remove(playerName);
		}
		this.version.incrementAndGet();
	}

	public void movePlayer(String playerName, int dY, int dX) {
		int N = this.mazeDimensions.getN();
		Location location = this.playerLocationMap.get(playerName);
		int Y = location.getY();
		int X = location.getX();
		int newY = Y + dY;
		int newX = X + dX;
		// LogUtil.printMsg("Original (X,Y): (" + X + "," + Y + ")");
		// LogUtil.printMsg("New (X,Y): (" + newX + "," + newY + ")");

		// out of bound
		if (newY < 0 || newX < 0 || newY >= N || newX >= N) {
			return;
		}
		// there is another player in the location
		if (this.maze[newY][newX] != null && !TREASURE_MARKING.equals(this.maze[newY][newX])) {
			// LogUtil.printMsg("there is player " + this.maze[newY][newX]);
			return;
		}

		boolean isTreasure = TREASURE_MARKING.equals(this.maze[newY][newX]);
		this.maze[newY][newX] = this.maze[Y][X];
		this.maze[Y][X] = null;
		// this.playerLocationMap.put(playerName, new Location(newY, newX));
		location.setXAndY(newY, newX);
		// LogUtil.printMsg("PlayerLocationMap: " + this.playerLocationMap.toString());
		if (isTreasure) {
			this.playerScore.put(playerName, this.playerScore.get(playerName) + 1);
			this.addTreasure();
		}
		this.version.incrementAndGet();
	}

}
