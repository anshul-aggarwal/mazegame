import java.rmi.RemoteException;

public class PingUtil extends Thread {

	// This class handles the pinging between players
	Player player;
	boolean terminated;

	public PingUtil(Player player) {
		this.player = player;
		this.terminated = false;
	}

	@Override
	public void run() {
		try {
			while (!terminated) {
				pingNextPlayer(player.getPingPlayer());
				Thread.sleep(500);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void terminate() {
		this.terminated = true;
	}

	public void pingNextPlayer(IPlayer pingPlayer) throws RemoteException {
		if (pingPlayer != null)
			try {
				if (pingPlayer.respondToPing()) {
					// System.out.println("Successfully received a response to the ping. The next
					// player is active.");
				}
			} catch (RemoteException e) {
				// LogUtil.printMsg("Ping Failed. The next player is dead.");
				PlayerRegistrationUtil.deregister(this.player.getPingPlayerName(), this.player);
			} catch (Exception f) {
				// LogUtil.printMsg("Ping failed. Some other unknown Exception");
			}
	}

}
