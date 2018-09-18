import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface IPlayer extends Remote {

    /**
     *
     * @return
     */
    String getPlayerName() throws RemoteException;

    /**
     *
     * @param players
     */
    void addPlayers(Set<IPlayer> players) throws RemoteException;

    /**
     *
     * @throws RemoteException
     */
    void printPlayerNames() throws RemoteException;

    /**
     *
     * @return
     * @throws RemoteException
     */
    ITracker getTracker() throws RemoteException;
}
