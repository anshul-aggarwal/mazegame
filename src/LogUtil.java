public class LogUtil {

	private LogUtil() {
	}

	public static void printPlayers(Player player, String prelimMsg) {
		printMsg(prelimMsg);
		System.out.println("[Thread: " + Thread.currentThread().getName() + "]  ,Players: "
				+ player.getGameState().getPlayerMap().keySet().toString());
	}

	public static void printMsg(String msg) {
		System.out.println("[Thread: " + Thread.currentThread().getName() + "]  ," + msg);
	}

}
