package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

public class Login {
	
	private FlowPane pane;
	private Main mainApp;
	
	public Login(Main mainApp) {
        this.mainApp = mainApp;
        initialize();
        
    }
	
	private void initialize() {
		pane = new FlowPane();
		pane.setPadding(new Insets(11,12,13,14));
		pane.setHgap(5);
		pane.setVgap(5);

		pane.getChildren().addAll(new Label("User Name: "), new TextField());
		pane.getChildren().addAll(new Label("Password: "), new TextField());
		
        Button switchButton = new Button("Switch to Secondary View");

        // 按钮点击事件，切换到 SecondaryView
        switchButton.setOnAction(e -> mainApp.showSecondaryView());

        pane.getChildren().add(switchButton);
    }

    public FlowPane getView() {
        return pane;
    }

}