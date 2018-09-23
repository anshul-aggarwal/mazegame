import java.util.Map;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class GameState implements Serializable {
    private int N;
    private int K;
    private Map<IPlayer, Integer> playerScore;

    public GameState(int N, int K, IPlayer[] players) {
        this.N = N;
        this.K = K;
        this.playerScore = new HashMap<>();
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) {
                playerScore.put(players[i], 0);
            }
        }
    }

    public IPlayer[] getPlayerList() {
        if (this.playerScore == null) {
            return new IPlayer[0];
        }
        Set<IPlayer> playerSet = this.playerScore.keySet();
        IPlayer playerList[] = new IPlayer[playerSet.size()];
        int i = 0;
        for (IPlayer player : playerSet) {
            playerList[i++] = player;
        }
        return playerList;
    }
}