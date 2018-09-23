import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPlayer extends Remote {
    String getName() throws RemoteException;
}