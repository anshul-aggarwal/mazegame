package application;

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
    
    //Update with actual maze dimensions
    private static int nCols = 20 ;
    private static int nRows = 20 ;
	
    private static Label label[][];

    
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
		updateGrid();
	}
	
	public static void updateGrid()
	{
		for (int i=0;i<nRows;i++)
		{
			for (int j=0;j<nCols;j++)
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
		
		label = new Label[nRows][nCols];
		
        for (int i = 0; i < nCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / nCols);
            gameGrid.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < nRows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / nRows);
            gameGrid.getRowConstraints().add(rowConst);         
        }
        
		
		for (int i=0;i<nRows;i++)
		{
			for (int j=0;j<nCols;j++)
			{
			    Random rand = new Random();

			    int r1 = rand.nextInt(10);
			    int r2 = rand.nextInt(10);
			    
				label[i][j] = new Label();
	            label[i][j].setText(Integer.toString(r1) +"," + Integer.toString(r2));	//Set maze contents here
	            GridPane.setHalignment(label[i][j], HPos.CENTER);
	            gameGrid.add(label[i][j], i, j);

			}
		}
		
		return gameGrid;

	}
}
