import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface ITracker extends Remote {

    /**
     *
     * @param player
     * @return
     */
    Set<IPlayer> add(IPlayer player) throws RemoteException;
}
