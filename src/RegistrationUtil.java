import java.rmi.RemoteException;
import java.util.*;

public class RegistrationUtil {

    private RegistrationUtil() {
    }

    /**
     * This method takes care of the entire registration process of a player.
     * Be it first player or any other player.
     * <p>
     * This method will also update the communication/Game state maintained inside the player object!
     *
     * @param playerName
     * @param playerStub
     */
    public static void register(String playerName, IPlayer playerStub) throws RemoteException {

        ITracker trackerStub = playerStub.getTrackerStub();
        GameState gameState = null;
        LinkedHashMap<String, IPlayer> playerMap = null;
        Iterator<Map.Entry<String, IPlayer>> playerMapIterator = null;

        /**
         * Keep on trying unless registered
         */
        do{
            // Get player Map from Tracker
            playerMap = trackerStub.getPlayerMap();

            if(playerMap.size()==0){
                playerMap = trackerStub.addFirstPlayer(playerName, playerStub);
            } else  {

                // Try registering with primary player
                playerMapIterator = playerMap.entrySet().iterator();
                Map.Entry<String, IPlayer> primaryServer = playerMapIterator.next();
                String primaryName = primaryServer.getKey();
                IPlayer primaryStub = primaryServer.getValue();

                try {
                    gameState = primaryStub.registerPlayer(playerName, playerStub);
                    playerMap = gameState.getPlayerMap();
                }catch (RemoteException e){

                    if (!playerMapIterator.hasNext()) {
                        // If primary Server is the only player in the game and it is not responding
                        trackerStub.removePlayer(primaryName);
                    }else{
                        // Try registering with backup Server
                        Map.Entry<String, IPlayer> backupServer = playerMapIterator.next();
                        String backupName = primaryServer.getKey();
                        IPlayer backupStub = primaryServer.getValue();

                        try {
                            gameState = backupStub.registerPlayer(playerName, playerStub);
                            playerMap = gameState.getPlayerMap();
                        } catch (RemoteException e1) {
                        }
                    }
                }
            }
        }while (!playerMap.containsKey(playerName));

        // Set game state inside current player
        if(gameState==null){
            // Happens only when you are the primary Server/First player to be registered
            gameState = new GameState(trackerStub.getMazeDimensions(), playerMap);
            /**
             * Place yourself in the maze somewhere, and generate treasures
             */
        }
        playerStub.setGameState(gameState);
        System.out.println("Registered");

		//PingUtil.pingNextPlayer(PingUtil.setPingTarget(gameState.getPlayerMap(), playerStub.getPlayerName()));

    }

}
