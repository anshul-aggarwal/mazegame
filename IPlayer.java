import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedHashSet;


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
    void addPlayers(LinkedHashSet<IPlayer> players) throws RemoteException;

    /**
     *
     * @throws RemoteException
     */
    void printPlayerNames() throws RemoteException;
    
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
