import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Game {

    private static GameState gameState;
    
    public static void main(String[] args) {

        Integer port;
        String playerName;

        try {
            port = Integer.parseInt(args[0]);
            playerName = args[1];
        } catch (Exception e) {
            System.err.println("Wrong arguments. It should be: java Game [port-number] [player name]");
            return;
        }
        
        try {
            Registry registry = LocateRegistry.getRegistry();
            ITracker tracker = (ITracker) registry.lookup("Tracker");
            IPlayer player = new Player(playerName);
            IPlayer stub = (IPlayer) UnicastRemoteObject.exportObject(player, port);

            gameState = tracker.getBasicGameState();
            IPlayer[] playerList = gameState.getPlayerList();
            boolean added = false;
            if (playerList.length == 0) {
                if (tracker.addFirstPlayer(stub)) {
                    added = true;
                    // make self to main server
                } else {
                    playerList = tracker.getPlayers();
                }
            }
            if (!added) {
                // call player from playerList to get the main server and backup server
                // register self to main server
            }
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
