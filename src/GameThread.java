import java.rmi.RemoteException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class GameThread extends Thread {

	private final IPlayer localPlayerStub;

	public GameThread(IPlayer localPlayerStub) {
		this.localPlayerStub = localPlayerStub;
	}

	@Override
	public void run() {

		/**
		 * Pinging thread
		 *
		 */
		PingUtil ping = new PingUtil(localPlayerStub);
		ping.start();
		LogUtil.printMsg("Fixed Pinging");

		/**
		 * Start GUI
		 */
		MazeGui mazeGui;
		try {
			mazeGui = new MazeGui(localPlayerStub);
		} catch (RemoteException e) {
			System.err.println("Unable to render UI. Exiting");
			e.printStackTrace();
			return;
		}

		/**
		 * Start Game
		 */
		LogUtil.printMsg("Started Game");
		try {
			Scanner sc = new Scanner(System.in);
			boolean terminateGame = false;

			while (!terminateGame) {

				mazeGui.updateUI();
				// printGameState(player.getGameState());

				String input;
				try {
					input = sc.next();
				} catch (NoSuchElementException e) {
					input = "9";
				}

				switch (input) {
				case "0":
					PlayerActionUtil.refresh(localPlayerStub);
					break;
				case "1":
				case "2":
				case "3":
				case "4":
					PlayerActionUtil.move(localPlayerStub, input);
					break;
				case "9":
					PlayerRegistrationUtil.deregister(localPlayerStub.getPlayerName(), localPlayerStub);
					terminateGame = true;
					break;
				default:
					LogUtil.printMsg("Invalid input. Press 9 to terminate");
					break;
				}
			}
			ping.terminate();
			sc.close();
			LogUtil.printMsg("Game Over!");
		} catch (RemoteException e) {
			System.err.println("Game exception: ");
			e.printStackTrace();
		}

		System.exit(0);
	}

}
