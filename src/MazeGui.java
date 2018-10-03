import java.awt.Color;
import java.awt.GridLayout;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

public class MazeGui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 455097013978973213L;
	// private static final Integer SPLIT_LOCATION = 150;
	// private static final Integer MAZE_WIDTH = 800;
	// private static final Integer MAZE_HEIGHT = 600;

	// private final Random random = new Random();
	private final IPlayer player;
	private final Integer N;

	private JList<String> playerInfoPanel;
	private JTextField[][] mazeData;

	public MazeGui(IPlayer player) throws RemoteException {

		// Initialize final fields
		this.player = player;
		this.N = this.player.getGameState().getMazeDimensions().getN();

		// Create all Panels and set initial Data
		this.createMainUI();

		// Set Defaults
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Player " + this.player.getPlayerName() + " -- MazeGame");
		// this.setSize(MAZE_WIDTH, MAZE_HEIGHT);
		this.pack();
		this.setLocationRelativeTo(null);
		// this.setLocation(random.nextInt(1000), random.nextInt(1000));

		// Show Window
		this.setVisible(true);
	}

	private void createMainUI() throws RemoteException {

		// Initialize playerInfoPanel & MazeData
		this.playerInfoPanel = new JList<>();
		this.playerInfoPanel.setBorder(BorderFactory.createTitledBorder("Player Information"));

		this.mazeData = new JTextField[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				this.mazeData[i][j] = new JTextField();
				this.mazeData[i][j]
						.setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED, Color.GRAY, Color.GRAY));
			}
		}

		// Creating Panel UI
		JScrollPane leftPanel = new JScrollPane(this.playerInfoPanel);
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new GridLayout(N, N));
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				rightPanel.add(this.mazeData[i][j]);
			}
		}
		JSplitPane containerPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
		containerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		// containerPanel.setDividerLocation(SPLIT_LOCATION);
		this.add(containerPanel);

		// Update Data
		this.updateUI();
	}

	public void updateUI() throws RemoteException {
		GameState gameState = this.player.getGameState();
		synchronized (DummyLock.class) {
			this.updatePlayerList(gameState);
			this.updateMaze(gameState);
		}
	}

	private void updateMaze(GameState gameState) throws RemoteException {
		String[][] maze = gameState.getMaze();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				String mazeTextValue = maze[i][j] == null ? "" : maze[i][j];
				this.mazeData[i][j].setText(mazeTextValue);
			}
		}
	}

	private void updatePlayerList(GameState gameState) throws RemoteException {
		Map<String, Integer> scoreMap = gameState.getPlayerScore();
		Set<String> players = gameState.getPlayerMap().keySet(); // Sorted Order Guaranteed
		List<String> playerInfoList = new ArrayList<>();
		int i = 0;
		for (String pid : players) {
			StringBuilder sb = new StringBuilder(pid);
			sb.append(" - ");
			sb.append(scoreMap.get(pid));
			if (i == 0) {
				sb.append(" -- Primary Server");
			} else if (i == 1) {
				sb.append(" -- Backup Server");
			}
			i++;
			playerInfoList.add(sb.toString());
		}

		this.playerInfoPanel.setListData(playerInfoList.toArray(new String[playerInfoList.size()]));
	}

}
