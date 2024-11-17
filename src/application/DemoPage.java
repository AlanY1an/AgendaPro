package application;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class DemoPage {
	
	private Pane pane;
    private Main mainApp;

    public DemoPage(Main mainApp) {
        this.mainApp = mainApp;
        initialize();
    }
    
    private void initialize() {
    	pane = new Pane();
		Text text = new Text();
		text.setText("Hello World");	
		pane.getChildren().add(text);

        // Center the text whenever the pane's size changes
        pane.widthProperty().addListener((obs, oldVal, newVal) -> centerText(text));
        pane.heightProperty().addListener((obs, oldVal, newVal) -> centerText(text));
    }

    // Method to center the text within the pane
    private void centerText(Text text) {
        text.setX((pane.getWidth() - text.getLayoutBounds().getWidth()) / 2);
        text.setY((pane.getHeight() - text.getLayoutBounds().getHeight()) / 2);
    }
    
    public Pane getView() {
        return pane;
    }
}