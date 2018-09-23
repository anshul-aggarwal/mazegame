import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

public class Game {
	public static class Gamestate {			//Proposal: Create nested gamestate class and share this information. this does not contain any player level information
		int playerCount;	//N
		int treasureCount; 	//K
		int gridSize;
		IPlayer primaryServer;
		IPlayer backupServer;
	}
	
    public static void main(String args[]) {
        try {

            /*
            Locate Registry and get Tracker Object
             */
            Registry registry = LocateRegistry.getRegistry();
            ITracker tracker = (ITracker) registry.lookup("Tracker");

            /*
            Create Player Object and Stub
             */
            Integer port = Integer.parseInt(args[0]);
            String playerName = args[1];
            IPlayer player = new Player(playerName, tracker);
            IPlayer stub = (IPlayer) UnicastRemoteObject.exportObject(player, port);

            /*
            Register Player stub with Tracker. Also add the List of Player Stubs obtained from tracker to the current player
             */
            ArrayList<IPlayer> players = tracker.add(stub);
            player.addPlayers(players);
            
            /*
            Start Game
             */
            Scanner sc = new Scanner(System.in);
            while (true) {
                System.out.print("Enter c to contact everyone, p to ping your target or anything else to quit: ");
                String input = sc.next();
                if (input.equals("c")) {
                    player.printPlayerNames();
                } else if (input.equals("p")){
                	player.pingNextPlayer();
                }else {
                    break;
                }
            }
            sc.close();
            System.out.println("Game Over!");

        } catch (Exception e) {
            System.err.println("Game exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
