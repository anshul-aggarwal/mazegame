import java.io.Serializable;
import java.util.LinkedHashMap;

public class GameState implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2594162138114462023L;

	private final MazeDimensions mazeDimensions;
	LinkedHashMap<String, IPlayer> playerMap;

	/**
	 * Constructor
	 *
	 * @param mazeDimensions
	 * @param playerMap
	 */
	public GameState(MazeDimensions mazeDimensions, LinkedHashMap<String, IPlayer> playerMap) {
		this.mazeDimensions = mazeDimensions;
		this.playerMap = playerMap;
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

}
