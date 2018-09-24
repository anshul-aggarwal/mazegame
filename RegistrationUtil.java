import java.rmi.RemoteException;
import java.util.Set;

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
        Set<IPlayer> players;

        /**
         * Keep on trying unless registered
         */
        do {
            players = gameState.getPlayers();
            if (players.size() == 0) {
                players = trackerStub.addFirstPlayer(playerStub);
            } else {

                // Try registering with primary Server first
                IPlayer primary = gameState.getPrimary();
                try {
                    gameState = primary.registerPlayer(playerStub);
                } catch (RemoteException e) {
                    if (players.size() == 1) {
                        trackerStub.removePlayer(primary);
                    }
                }

                // Try registering with backup server , if any
                IPlayer backup = gameState.getBackup();
                if (backup != null) {
                    try {
                        gameState = backup.registerPlayer(playerStub);
                    } catch (RemoteException e) {
                        players = trackerStub.getPlayers();
                    }
                }

            }
            gameState.setPlayers(players);
        } while (!players.contains(playerStub));

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
