import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class GameState implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2594162138114462023L;

	private final MazeDimensions mazeDimensions;
	LinkedHashMap<String, IPlayer> playerMap;
	private String[][] maze;
	private Map<String, Location> playerLocationMap;
	private Map<String, Integer> playerScore;

	/**
	 * Constructor
	 *
	 * @param mazeDimensions
	 * @param playerMap
	 */
	public GameState(MazeDimensions mazeDimensions) {
		this.mazeDimensions = mazeDimensions;
		this.playerMap = new LinkedHashMap<>();
		this.playerLocationMap = new HashMap<>();
		this.playerScore = new HashMap<>();
		this.generateMaze(mazeDimensions.getN(), mazeDimensions.getK());
	}

	/**
	 *
	 * @param playerMap
	 */
	public void setPlayerMap(LinkedHashMap<String, IPlayer> playerMap) {
		this.playerMap = playerMap;
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

	private synchronized void addTreasure() {
		int N = this.mazeDimensions.getN();
		while (true) {
			int Y = ThreadLocalRandom.current().nextInt(0, N);
			int X = ThreadLocalRandom.current().nextInt(0, N);
			if (this.maze[Y][X] == null) {
				this.maze[Y][X] = "T";
				break;
			}
		}
	}

	public synchronized void addPlayer(String playerName) {
		int N = this.mazeDimensions.getN();
		while (true) {
			int Y = ThreadLocalRandom.current().nextInt(0, N);
			int X = ThreadLocalRandom.current().nextInt(0, N);
			if (this.maze[Y][X] == null) {
				this.maze[Y][X] = playerName;
				this.playerLocationMap.put(playerName, new Location(Y,X));
				this.playerScore.put(playerName, 0);
				break;
			}
		}
	}

	public synchronized void removePlayer(String playerName) {
		int Y = this.playerLocationMap.get(playerName).getY();
		int X = this.playerLocationMap.get(playerName).getX();
		this.maze[Y][X] = null;
		this.playerLocationMap.remove(playerName);
		this.playerScore.remove(playerName);
	}

	public synchronized void movePlayer(String playerName, int dY, int dX) {
		int N = this.mazeDimensions.getN();
		int Y = this.playerLocationMap.get(playerName).getY();
		int X = this.playerLocationMap.get(playerName).getX();
		int newY = Y + dY;
		int newX = X + dX;
		// out of bound
		if (newY < 0 || newX < 0 || newY >= N || newX >= N) {
			System.out.println("out of bound");
			return;
		}
		// there is another player in the location
		if (this.maze[newY][newX] != null && !"T".equals(this.maze[newY][newX])) {
			System.out.println("there is player " + this.maze[newY][newX]);
			return;
		}

		boolean isTreasure = "T".equals(this.maze[newY][newX]);
		this.maze[newY][newX] = this.maze[Y][X];
		this.maze[Y][X] = null;
		this.playerLocationMap.put(playerName, new Location(newY, newX));
		if (isTreasure) {
			this.playerScore.put(playerName, this.playerScore.get(playerName) + 1);
			this.addTreasure();
		}
	}

}
