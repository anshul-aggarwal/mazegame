import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
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
    void addPlayers(ArrayList<IPlayer> players) throws RemoteException;

    /**
     *
     * @throws RemoteException
     */
    void printPlayerNames() throws RemoteException;
    
    /**
    *
    * @throws RemoteException
    */
    void pingNextPlayer() throws RemoteException;
    
    /**
    *
    * @throws RemoteException
    */
    boolean respondToPing() throws RemoteException;
    
    /**
    *
    * @throws RemoteException
    */
    void setPingTarget() throws RemoteException;
    

    /**
     *
     * @return
     * @throws RemoteException
     */
    ITracker getTracker() throws RemoteException;
}
