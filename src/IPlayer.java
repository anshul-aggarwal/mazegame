import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;


public interface IPlayer extends Remote {

    /**
    *
    * @param gameState
    */
   void setGameState(GameState gameState) throws RemoteException;

    /**
     *
     * @param playerMap
     */
   void setPlayerMap(LinkedHashMap<String, IPlayer> playerMap) throws RemoteException;

   /**
    *
    * @param playerStub
    * @return
    * @throws RemoteException
    */
   GameState registerPlayer(String name, IPlayer playerStub) throws RemoteException;

   /**
    *
    * @return
    * @throws RemoteException
    */
   ITracker getTrackerStub() throws RemoteException;

    /**
    *
    * @throws RemoteException
    */
    //void pingNextPlayer() throws RemoteException;
    
    /**
    *
    * @throws RemoteException
    */
    boolean respondToPing() throws RemoteException;
    
    /**
    *
    * @throws RemoteException
    */
    //void setPingTarget() throws RemoteException;
    
}
