import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class GameState implements Serializable {

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
    public String getPrimaryName(){
        Iterator<String> it = playerMap.keySet().iterator();
        if(it.hasNext()){
            return it.next();
        }
        return null;
    }

    /**
     *
     * @return
     */
    public String getBackupName(){
        String backupServer = null;
        Iterator<String> it = playerMap.keySet().iterator();
        if(it.hasNext()){
            backupServer = it.next();
        }
        if(it.hasNext()){
            backupServer = it.next();
        }
        return backupServer;
    }

    /**
     *
     * @return
     */
    public MazeDimensions getMazeDimensions() {
        return mazeDimensions;
    }
    
}
