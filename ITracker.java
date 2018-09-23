import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface ITracker extends Remote {

    /**
     *
     * @return
     * @throws RemoteException
     */
    Set<IPlayer> getPlayers() throws RemoteException;

    /**
     *
     * @return
     * @throws RemoteException
     */
    MazeDimensions getMazeDimensions() throws RemoteException;

    /**
     *
     * @param player
     * @return
     * @throws RemoteException
     */
    boolean addFirstPlayer(IPlayer player) throws RemoteException;

    /**
     *
     * @param player
     * @return
     * @throws RemoteException
     */
    Set<IPlayer> addPlayer(IPlayer player) throws RemoteException;

    /**
     *
     * @param player
     * @return
     * @throws RemoteException
     */
    Set<IPlayer> removePlayer(IPlayer player) throws RemoteException;
}
