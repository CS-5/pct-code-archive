/*
Carson Seese - CIT260-02 - GUI Assignment - Event Registration
04-27-19

This program allows the user to register for an event with several options to choose from. When they have made their final selection, the total is presented to them.
*/

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;


public class Registration extends Application {

	ToggleGroup tgTicket;
	RadioButton btnGeneralTicket;
	RadioButton btnStudentTicket;
	CheckBox cbxDinner;

	CheckBox cbxECommerce;
	CheckBox cbxFuture;
	CheckBox cbxAdvancedJava;
	CheckBox cbxNetwork;

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Initializes the GUI elements for the Event Registration Application
	 * @param primaryStage The main stage element
	 */
	@Override
	public void start(Stage primaryStage) {
		//Top box with controls for user interaction
		tgTicket = new ToggleGroup();
		btnGeneralTicket = new RadioButton("General Ticket - $895");
		btnGeneralTicket.setToggleGroup(tgTicket);
		btnGeneralTicket.setUserData(0);
		btnStudentTicket = new RadioButton("Student Ticket - $495");
		btnStudentTicket.setToggleGroup(tgTicket);
		btnStudentTicket.setUserData(1);
		cbxDinner = new CheckBox("Optional Dinner - $30");
		VBox topBox = new VBox(5, btnGeneralTicket, btnStudentTicket, cbxDinner);

		//Middle box with controls for user interaction
		cbxECommerce = new CheckBox("Intro to E-Commerce - $295");
		cbxFuture = new CheckBox("Future of the Web - $295");
		cbxAdvancedJava = new CheckBox("Advanced Java Programming - $395");
		cbxNetwork = new CheckBox("Network Security - $395");
		VBox midBox = new VBox(5, cbxECommerce, cbxFuture, cbxAdvancedJava, cbxNetwork);

		//Finalize button
		Button btnFinalize = new Button("Finalize");
		btnFinalize.setOnAction(finalize);
		btnFinalize.setMaxWidth(Double.MAX_VALUE);


		//Organization Labels
		Label lblStep1 = new Label("Step 1:\nTicket Type");
		lblStep1.setAlignment(Pos.TOP_LEFT);
		lblStep1.setMaxWidth(Double.MAX_VALUE);
		lblStep1.setMaxHeight(Double.MAX_VALUE);

		Label lblStep2 = new Label("Step 2:\nWorkshop Selection");
		lblStep2.setAlignment(Pos.TOP_LEFT);
		lblStep2.setMaxWidth(Double.MAX_VALUE);
		lblStep2.setMaxHeight(Double.MAX_VALUE);

		Label lblStep3 = new Label("Step 3:\nFinalize Ticket");
		lblStep3.setAlignment(Pos.TOP_LEFT);
		lblStep3.setMaxWidth(Double.MAX_VALUE);
		lblStep3.setMaxHeight(Double.MAX_VALUE);

		//Grid Pane initialization
		GridPane pane = new GridPane();
		pane.setHgap(25);
		pane.setVgap(20);
		pane.setPadding(new Insets(10, 10, 10, 10));
		pane.add(lblStep1, 0, 0);
		pane.add(lblStep2, 0, 1);
		pane.add(lblStep3, 0, 2);
		pane.add(topBox, 1, 0);
		pane.add(midBox, 1, 1);
		pane.add(btnFinalize, 1, 2);

		Scene scene = new Scene(pane, 375, 225);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Event Registration");

		primaryStage.show();
	}

	//Called when the finalize button is pressed. Creates a JOptionPane to inform the user of their total ticket price
	EventHandler<ActionEvent> finalize = event -> {
		double ticketPrice = 0;

		if((int) tgTicket.getSelectedToggle().getUserData() == 0) {
			ticketPrice += 895;
		} else {
			ticketPrice += 495;
		}

		if(cbxDinner.isSelected())
			ticketPrice += 30;

		if(cbxECommerce.isSelected())
			ticketPrice += 295;

		if(cbxFuture.isSelected())
			ticketPrice += 295;

		if(cbxAdvancedJava.isSelected())
			ticketPrice += 395;

		if(cbxNetwork.isSelected())
			ticketPrice += 395;

		JOptionPane.showMessageDialog(new Frame(), "Total Ticket Cost: $" + ticketPrice);
	};
}
