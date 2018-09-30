import java.awt.GridLayout;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

public class MazeGui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 455097013978973213L;
	// private static final Integer SPLIT_LOCATION = 150;
	// private static final Integer MAZE_WIDTH = 800;
	// private static final Integer MAZE_HEIGHT = 600;

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

		// Show Window
		this.setVisible(true);
	}

	private void createMainUI() throws RemoteException {

		// Initialize playerInfoPanel & MazeData
		this.playerInfoPanel = new JList<>();
		this.mazeData = new JTextField[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				this.mazeData[i][j] = new JTextField();
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
		// containerPanel.setDividerLocation(SPLIT_LOCATION);
		this.add(containerPanel);

		// Update Data
		this.updateUI();
	}

	public void updateUI() throws RemoteException {
		this.updatePlayerList();
		this.updateMaze();
	}

	private void updateMaze() throws RemoteException {
		String[][] maze = this.player.getGameState().getMaze();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				String mazeTextValue = maze[i][j] == null ? "" : maze[i][j];
				this.mazeData[i][j].setText(mazeTextValue);
			}
		}
	}

	private void updatePlayerList() throws RemoteException {
		Map<String, Integer> scoreMap = this.player.getGameState().getPlayerScore();
		Set<String> players = this.player.getGameState().getPlayerMap().keySet(); // Sorted Order Guaranteed
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
