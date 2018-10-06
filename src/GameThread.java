import java.rmi.RemoteException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class GameThread extends Thread {

	private static final String GAME_MENU_MSG = "\n\n0) Refresh" + "\n1) Move West" + "\n2) Move South"
			+ "\n3) Move East" + "\n4) Move North" + "\n9) Exit" + "\nPlease Enter your Choice: ";

	private final Player player;

	public GameThread(Player player) {
		this.player = player;
	}

	@Override
	public void run() {

		/*
		 * Pinging thread
		 *
		 */
		PingThread pingThread = new PingThread(player);
		pingThread.start();
		// LogUtil.printMsg("Fixed Pinging");

		/*
		 * Start GUI
		 */
		MazeGui mazeGui = new MazeGui(player);

		/*
		 * Start Game
		 */
		// LogUtil.printMsg("Started Game");
		try {
			Scanner sc = new Scanner(System.in);
			boolean terminateGame = false;

			while (!terminateGame) {

				mazeGui.updateUI();
				System.out.print(GAME_MENU_MSG);
				// printGameState(player.getGameState());

				String input;
				try {
					input = sc.next();
				} catch (NoSuchElementException e) {
					input = "9";
				}

				switch (input) {
				case "0":
					PlayerActionUtil.refresh(player);
					break;
				case "1":
				case "2":
				case "3":
				case "4":
					PlayerActionUtil.move(player, input);
					break;
				case "9":
					PlayerRegistrationUtil.deregister(player.getPlayerName(), player);
					terminateGame = true;
					break;
				default:
					// LogUtil.printMsg("Invalid input. Press 9 to terminate");
					break;
				}
			}
			pingThread.terminate();
			sc.close();
			System.out.println("Game Over!");
		} catch (RemoteException e) {
			System.err.println("Game exception: " + e);
		}
		System.exit(0);
	}

}
