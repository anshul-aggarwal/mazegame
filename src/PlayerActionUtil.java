/**
 * This utility is meant for handling all Player actions (except
 * register,de-register)
 *
 */
public class PlayerActionUtil {

	/**
	 * 
	 * @param localPlayerStub
	 * @param direction
	 */
	public static void move(IPlayer localPlayerStub, String direction) {

		// Contains dummy code for movement. TODO add movement code
		switch (direction) {
		case "1":
			System.out.println("Moved West");
			break;
		case "2":
			System.out.println("Moved South");
			break;
		case "3":
			System.out.println("Moved East");
			break;
		case "4":
			System.out.println("Moved North");
			break;
		}

	}

	/**
	 * 
	 * @param localPlayerStub
	 */
	public static void refresh(IPlayer localPlayerStub) {

	}
}
