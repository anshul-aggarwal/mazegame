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
    Set<IPlayer> addFirstPlayer(IPlayer player) throws RemoteException;

    /**
     *
     * Suggestions:
     *
     * For add,
     *
     * Check if the caller is present in the player list, only then allow addPlayer
     *
     * @param player
     * @return
     * @throws RemoteException
     */
    Set<IPlayer> addPlayer(IPlayer player) throws RemoteException;

    /**
     *
     * Suggestions:
     *
     * For removal,
     *
     * Case i) Someone who has not yet joined the game calls remove. In this case, check the players list size. If its 1, allow!
     * Case ii) For every other case, just do a contains if the caller is present in the players list, only then allow!
     *
     * @param player
     * @return
     * @throws RemoteException
     */
    Set<IPlayer> removePlayer(IPlayer player) throws RemoteException;
}
