import java.rmi.RemoteException;
import java.util.Map;
import java.util.Random;

import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class controller {
    private static GridPane gameGrid;
    private static double GRID_WIDTH = 600;
    private static double GRID_HEIGHT = 600;
    
    private static IPlayer player;
    private static int playerCount;
    private final static int maxPlayers = 20;
    
    //Update with actual maze dimensions
    private static int mazeDim;
    
    private static Label label[][];
    private static Label playersList[][];
    
    
    public static void setPlayer(IPlayer gamePlayer)
    {
    	player = gamePlayer;
    }
    
    public static String getPlayerName()
    {
    	String playerName = null;
    	try {
			playerName = player.getPlayerName();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	return playerName;
    }
    
    public static void initializeGUI()
    {
    	GameState gamestate = null;
    	try {
    		gamestate = player.getGameState();
			mazeDim = gamestate.getMazeDimensions().getN();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	
//
//    	
//    	//String[][] maze = gamestate.getMaze();
//		for (int i = 0; i < mazeDim; i++) {
//			for (int j = 0; j < mazeDim; j++) {
//				label[i][j] = new Label();
//				label[i][j].setText("");
//			}
//		}
		
		

    }
    
    public static void updateGUI()
    {
    	GameState gamestate = null;
    	try {
    		gamestate = player.getGameState();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	
    	String[][] maze = gamestate.getMaze();
		for (int i = 0; i < mazeDim; i++) {
			for (int j = 0; j < mazeDim; j++) {
				if (maze[i][j] == null)
					label[i][j].setText("");
				else
					label[i][j].setText("" + maze[i][j]);
			}
		}
		
		Map<String, Integer> playerScore = gamestate.getPlayerScore();
		playerCount = playerScore.size();
		
		int i=0;
		for (String name : playerScore.keySet()) {
			playersList[i][0].setText(name);
			playersList[i][1].setText(playerScore.get(name).toString());
			i++;
		}
		for (int j=playerCount; j<maxPlayers; j++)
		{
			playersList[j][0].setText("");
			playersList[j][1].setText("");
		}
    }
    
    /**
     * Add relevant movement code here. Not Handled yet: input using keyboard
     */
	public void moveNorth() {
		System.out.println("Moved N");
		refreshGameState();
	}
	
	public void moveSouth() {
		System.out.println("Moved S");
		refreshGameState();
	}

	public void moveEast() {
		System.out.println("Moved E");
		refreshGameState();
	}

	public void moveWest() {
		System.out.println("Moved W");
		refreshGameState();
	}

	public void refreshGameState() {
		System.out.println("Refreshed");
		updateGUI();
	}
	
	public static void updateGrid()
	{
		for (int i=0;i<mazeDim;i++)
		{
			for (int j=0;j<mazeDim;j++)
			{
			    Random rand = new Random();

			    int r1 = rand.nextInt(10);
			    int r2 = rand.nextInt(10);
			    
	            label[i][j].setText(Integer.toString(r1) +"," + Integer.toString(r2));	//Set maze contents here
			}
		}
	}

	
	public static GridPane createGrid() {
		System.out.println("Creating Grid");
		gameGrid = new GridPane();
		
		gameGrid.setMaxSize(GRID_WIDTH, GRID_HEIGHT);
		gameGrid.setMinSize(GRID_WIDTH, GRID_HEIGHT);
		
		gameGrid.setGridLinesVisible(true);
		
        for (int i = 0; i < mazeDim; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / mazeDim);
            gameGrid.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < mazeDim; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / mazeDim);
            gameGrid.getRowConstraints().add(rowConst);         
        }
        
		label = new Label[mazeDim][mazeDim];
		
		for (int i=0;i<mazeDim;i++)
		{
			for (int j=0;j<mazeDim;j++)
			{
				label[i][j] = new Label();
	            label[i][j].setText("");	//Set maze contents here
	            GridPane.setHalignment(label[i][j], HPos.CENTER);
	            gameGrid.add(label[i][j], i, j);
			}
		}
		
		playersList = new Label[maxPlayers][2];
		for (int i=0;i<maxPlayers;i++) {
			playersList[i][0] = new Label();
			playersList[i][1] = new Label();
			playersList[i][0].setText("");
			playersList[i][1].setText("");
		}
		
		return gameGrid;

	}
}
