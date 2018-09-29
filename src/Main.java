import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;


public class Main extends Application {
	private String playerName;		//Get actual Player Name Here
    private AnchorPane root;


	@Override
	public void start(Stage primaryStage) {
		try {
			
			playerName = controller.getPlayerName();
			primaryStage.setTitle("Player " + playerName + " -- MazeGame");
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("scene.fxml"));
            root = (AnchorPane) loader.load();

            GridPane gameGrid = controller.createGrid();
            
            root.getChildren().add(gameGrid);
            AnchorPane.setTopAnchor(gameGrid, 100.0);
            AnchorPane.setLeftAnchor(gameGrid, 362.0);
            
            Scene scene = new Scene(root);
			primaryStage.setScene(scene);
		    primaryStage.setResizable(false);

			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void mainGUI(String[] args) {
		launch(args);
	}
}
