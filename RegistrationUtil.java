import java.rmi.RemoteException;
import java.util.Collections;
import java.util.LinkedHashSet;

public class RegistrationUtil {

    private RegistrationUtil() {
    }

    /**
     * This method takes care of the entire registration process of a player.
     * Be it first player or any other player.
     * <p>
     * This method will also update the communication/Game state maintained inside the player object!
     *
     * @param playerStub
     */
    public static void register(IPlayer playerStub) throws RemoteException {

        ITracker trackerStub = playerStub.getTrackerStub();
        GameState gameState = new GameState(null, trackerStub.getPlayers());
        LinkedHashSet<IPlayer> players = gameState.getPlayers();

        /**
         * Keep on trying unless registered
         */
        while (!players.contains(playerStub)) {
            if (players.size() == 0) {
                players = trackerStub.addFirstPlayer(playerStub);
                gameState.setPlayers(players);
            } else {
                // Try registering with primary Server first
                IPlayer primary = gameState.getPrimary();
                try {
                    gameState = primary.registerPlayer(playerStub);
                } catch (RemoteException e) {
                    
                	if (players.size() == 1) {
                        trackerStub.removePlayer(primary);
                    }
                    
                    // Try registering with backup server , if any
                    IPlayer backup = gameState.getBackup();
                    if (backup != null) {
                        try {
                            gameState = backup.registerPlayer(playerStub);
                        } catch (RemoteException e1) {
                            players = trackerStub.getPlayers();
                            gameState.setPlayers(players);
                        }
                    }
                }
            }
            players = gameState.getPlayers();
        }
        
        System.out.println("Registered");
		//PingUtil.pingNextPlayer(PingUtil.setPingTarget(gameState.getPlayers(), playerStub.getPlayerName()));

        
        
        
        /**
         * Set the game state back in this player
         */
        if(gameState.getMazeDimensions()==null){
           gameState = new GameState(trackerStub.getMazeDimensions(), players);
            /**
             * Place yourself in the maze somewhere, and generate treasures
             */
        }
        playerStub.setGameState(gameState);
    }

}
