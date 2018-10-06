import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
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
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

public class MazeGui extends JFrame {

	private static final long serialVersionUID = 455097013978973213L;
	private static final Integer SPLIT_LOCATION = 120;
	private static final Integer WINDOW_WIDTH_MULTIPLIER = 45;
	private static final Integer WINDOW_HEIGHT_MULTIPLIER = 32;
	private static final Color TREASURE_COLOR = new Color(205, 255, 153);
	private static final Color NORMAL_SQUARE_COLOR = Color.WHITE;
	private static final Color CURRENT_PLAYER_COLOR = new Color(255, 178, 102);
	private static final Color OTHER_PLAYER_COLOR = new Color(191, 223, 255);

	// private final Random random = new Random();
	private final Player player;
	private final Integer N;
	private final Integer WINDOW_WIDTH;
	private final Integer WINDOW_HEIGHT;

	private JList<String> playerInfoPanel;
	private JTextField[][] mazeData;
	private String playerName;

	public MazeGui(Player player) {

		// Initialize final fields
		this.player = player;
		this.N = this.player.getGameState().getMazeDimensions().getN();
		this.playerName = this.player.getPlayerName();

		// Create all Panels and set initial Data
		this.createMainUI();

		// Set Window Dimensions
		WINDOW_WIDTH = N * WINDOW_WIDTH_MULTIPLIER;
		WINDOW_HEIGHT = N * WINDOW_HEIGHT_MULTIPLIER;

		// Set Defaults
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Player " + playerName + " -- MazeGame");
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		// this.pack();
		this.setLocationRelativeTo(null);
		// this.setLocation(random.nextInt(1000), random.nextInt(1000));

		// Show Window
		this.setVisible(true);
	}

	private void createMainUI() {

		// Initialize playerInfoPanel & MazeData
		this.playerInfoPanel = new JList<>();
		this.playerInfoPanel.setBorder(BorderFactory.createTitledBorder(null, "PLAYERS", TitledBorder.CENTER,
				TitledBorder.TOP, new Font(Font.MONOSPACED, Font.BOLD, 13), Color.BLACK));
		this.playerInfoPanel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));

		this.mazeData = new JTextField[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				this.mazeData[i][j] = new JTextField();
				this.mazeData[i][j]
						.setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED, Color.GRAY, Color.GRAY));
				this.mazeData[i][j].setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));
				this.mazeData[i][j].setEditable(false);
				this.mazeData[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				this.mazeData[i][j].setBackground(NORMAL_SQUARE_COLOR);
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
		containerPanel.setDividerLocation(SPLIT_LOCATION);
		this.add(containerPanel);

		// Update Data
		this.updateUI();
	}

	public void updateUI() {
		synchronized (DummyLock.class) {
			GameState gameState = this.player.getGameState();
			this.updatePlayerList(gameState);
			this.updateMaze(gameState);
		}
	}

	private void updateMaze(GameState gameState) {
		String[][] maze = gameState.getMaze();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				String mazeTextValue = maze[i][j] == null ? "" : maze[i][j];
				this.mazeData[i][j].setText(mazeTextValue);
				if (mazeTextValue.equals("")) {
					this.mazeData[i][j].setBackground(NORMAL_SQUARE_COLOR);
				} else if (mazeTextValue.equals(GameState.TREASURE_MARKING)) {
					this.mazeData[i][j].setBackground(TREASURE_COLOR);
				} else if (mazeTextValue.equals(this.playerName)) {
					this.mazeData[i][j].setBackground(CURRENT_PLAYER_COLOR);
				} else {
					this.mazeData[i][j].setBackground(OTHER_PLAYER_COLOR);
				}
			}
		}
	}

	private void updatePlayerList(GameState gameState) {
		Map<String, Integer> scoreMap = gameState.getPlayerScore();
		Set<String> players = gameState.getPlayerMap().keySet(); // Sorted Order Guaranteed
		List<String> playerInfoList = new ArrayList<>();
		int i = 0;
		for (String pid : players) {
			String playerID = pid;
			if (pid.equals(playerName)) {
				playerID = "<html><font color=#FF0000>" + pid;
			} else {
				playerID = "<html><font color=#000000>" + pid;
			}

			StringBuilder sb = new StringBuilder(playerID);
			sb.append(" - ");
			sb.append(scoreMap.get(pid));
			if (i == 0) {
				sb.append(" (PS)");
			} else if (i == 1) {
				sb.append(" (BS)");
			}
			sb.append("</font></html>");
			i++;
			playerInfoList.add(sb.toString());
		}

		this.playerInfoPanel.setListData(playerInfoList.toArray(new String[playerInfoList.size()]));
	}

}
