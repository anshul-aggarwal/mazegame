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
     * @param gameState
     */
    void setGameState(GameState gameState) throws RemoteException;

    /**
     *
     * @param playerStub
     * @return
     * @throws RemoteException
     */
    GameState registerPlayer(IPlayer playerStub) throws RemoteException;

    /**
     *
     * @return
     * @throws RemoteException
     */
    ITracker getTrackerStub() throws RemoteException;
}
