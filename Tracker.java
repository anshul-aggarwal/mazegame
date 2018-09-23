import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;
import java.util.HashSet;

public class Tracker implements ITracker {

    private int N, K;
    private Set<IPlayer> playerList;

    public Tracker(int N, int K) {
        this.N = N;
        this.K = K;
        this.playerList = new HashSet<>();
    }

    private IPlayer[] getTwoActivePlayers() {
        int i = 0;
        IPlayer players[] = new IPlayer[2];
        for (IPlayer player : playerList) {
            players[i++] = player;
            if (i == 2) {
                break;
            }
        }
        return players;
    }

    @Override
    public synchronized IPlayer[] getPlayers() {
        return getTwoActivePlayers();
    }

    @Override
    public synchronized GameState getBasicGameState() {
        IPlayer[] players = getTwoActivePlayers();
        return new GameState(N, K, players);
    }

    @Override
    public synchronized boolean addFirstPlayer(IPlayer player) {
        if (this.playerList.size() == 0) {
            this.playerList.add(player);
            return true;
        }
        return false;
    }

    @Override
    public synchronized void addPlayer(IPlayer player) {
        this.playerList.add(player);
    }

    @Override
    public synchronized void removePlayer(IPlayer player) {
        this.playerList.remove(player);
    }

    public static void main(String args[]) {
        int port, N, K;
        try {
            port = Integer.parseInt(args[0]);
            N = Integer.parseInt(args[1]);
            K = Integer.parseInt(args[2]);
        } catch(Exception e) {
            System.err.println("Wrong arguments. It should be: java Tracker [port-number] [N] [K]");
            return;
        }

        ITracker stub = null;
        Registry registry = null;
        try {
            Tracker obj = new Tracker(N, K);
            stub = (ITracker) UnicastRemoteObject.exportObject(obj, port);
            registry = LocateRegistry.getRegistry();
            registry.bind("Tracker", stub);
            System.err.println("Tracker ready");
        } catch (Exception e) {
            try{
                registry.unbind("Tracker");
                registry.bind("Tracker",stub);
                System.err.println("Tracker ready");
            }catch(Exception ee){
                System.err.println("Tracker exception: " + ee.toString());
                ee.printStackTrace();
            }
        }
    }
}
