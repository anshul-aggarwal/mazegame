import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ITracker extends Remote {
    IPlayer[] getPlayers() throws RemoteException;
    GameState getBasicGameState() throws RemoteException;
    boolean addFirstPlayer(IPlayer player) throws RemoteException;
    void addPlayer(IPlayer player) throws RemoteException;
    void removePlayer(IPlayer player) throws RemoteException;
}
