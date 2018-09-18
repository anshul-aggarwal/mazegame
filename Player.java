import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

public class Player implements IPlayer, Serializable {

    private final String playerName;
    private final ITracker tracker;
    private Set<IPlayer> players;

    public Player(String name, ITracker tracker) {
        this.playerName = name;
        this.tracker = tracker;
    }

    @Override
    public String getPlayerName() {
        System.out.println("\nHello, I am " + playerName + ". My function getPlayerName was called.");
        return playerName;
    }

    @Override
    public void addPlayers(Set<IPlayer> players) {
        if (this.players == null) {
            this.players = new HashSet<>();
        }
        this.players.addAll(players);
    }

    @Override
    public void printPlayerNames() throws RemoteException {
        if (players != null && !players.isEmpty()) {
            for (IPlayer p: players) {
                System.out.println(p.getPlayerName());
            }
        }
    }

    @Override
    public ITracker getTracker() {
        return tracker;
    }

    @Override
    public boolean equals(Object o) {
        try {
            if (o != null && (o instanceof IPlayer) && this.playerName.equals(((IPlayer) o).getPlayerName())) {
                return true;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }


}
