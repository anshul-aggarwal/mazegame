import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class GameState implements Serializable {

    private final MazeDimensions mazeDimensions;
    private Set<IPlayer> players;

    public GameState(MazeDimensions mazeDimensions) {
        this.mazeDimensions = mazeDimensions;
        this.players = Collections.emptySet();
    }

    public GameState(MazeDimensions mazeDimensions, Set<IPlayer> players) {
        this.mazeDimensions = mazeDimensions;
        this.players = players;
    }

    public void setPlayers(Set<IPlayer> players) {
        this.players = players;
    }

    public Set<IPlayer> getPlayers() {
        return players;
    }

    public IPlayer getPrimary(){
        Iterator<IPlayer> it = players.iterator();
        if(it.hasNext()){
            return it.next();
        }
        return null;
    }

    public IPlayer getBackup(){
        IPlayer backupServer = null;
        Iterator<IPlayer> it = players.iterator();
        if(it.hasNext()){
            backupServer = it.next();
        }
        if(it.hasNext()){
            backupServer = it.next();
        }
        return backupServer;
    }

    public MazeDimensions getMazeDimensions() {
        return mazeDimensions;
    }
}
