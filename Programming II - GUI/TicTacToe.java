/*
Carson Seese - CIT260-02 - GUI Assignment - Tic-Tac-Toe
04-27-19

This program starts a basic game of Tic-Tac-Toe built with JavaFX. X starts first.
*/

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class TicTacToe extends Application {

	private GridPane pane;
	private Button[][] buttons;
	private Button btnReset;
	private Label lblWinner;

	private int[][] gameState = new int[3][3];

	private boolean xTurn = true;

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Initializes the GUI elements for the Tic-Tac-Toe Application
	 * @param primaryStage The main stage element
	 */
	@Override
	public void start(Stage primaryStage) {
		ColumnConstraints col = new ColumnConstraints(100);
		RowConstraints row = new RowConstraints(100);

		pane = new GridPane();
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setPadding(new Insets(10, 10, 10, 10));
		pane.getColumnConstraints().addAll(col, col, col);
		pane.getRowConstraints().addAll(row, row, row);

		Scene scene = new Scene(pane, 330, 400);
		scene.getStylesheets().add("TicTacToe.css");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Tic-Tac-Toe - X Starts");

		buttons = new Button[3][3];
		for (int c = 0; c < buttons.length; c++) {
			for (int r = 0; r < buttons[c].length; r++) {
				buttons[c][r] = new Button("");
				buttons[c][r].getStyleClass().add("xoButton");
				buttons[c][r].addEventHandler(MouseEvent.MOUSE_CLICKED, buttonHandler);

				buttons[c][r].setMaxWidth(Double.MAX_VALUE);
				buttons[c][r].setMaxHeight(Double.MAX_VALUE);
				pane.add(buttons[c][r], c, r);
			}
		}

		lblWinner = new Label();
		lblWinner.setMaxWidth(Double.MAX_VALUE);
		lblWinner.setAlignment(Pos.CENTER);

		btnReset = new Button("Reset Game");
		btnReset.setDisable(true);
		btnReset.setOnAction(resetHandler);
		btnReset.setMaxWidth(Double.MAX_VALUE);

		pane.add(lblWinner, 1, 3);
		pane.add(btnReset, 1, 4);

		primaryStage.show();
	}

	//Called when any button is pressed.
	//Gets the button's location and updates that index in the gameState array with 10 for X or 100 for O
	private EventHandler<MouseEvent> buttonHandler = event -> {
		Button btn = (Button) event.getSource();
		btn.setDisable(true);

		int row = pane.getRowIndex(btn);
		int col = pane.getColumnIndex(btn);

		if(xTurn) {
			btn.setText("X");
			gameState[row][col] = 10;
			xTurn = false;
		} else {
			btn.setText("O");
			gameState[row][col] = 100;
			xTurn = true;
		}

		winner(checkGame());
	};

	//Called when the reset button is pressed. Resets the gameState array and clears the board
	private EventHandler<ActionEvent> resetHandler = event -> {
		Button btn = (Button) event.getSource();

		xTurn = true;
		btn.setDisable(true);
		lblWinner.setText("");

		for (int c = 0; c < buttons.length; c++) {
			for (int r = 0; r < buttons[c].length; r++) {
				buttons[c][r].setText("");
				buttons[c][r].setDisable(false);
			}
		}

		for(int r = 0; r < gameState.length; r++) {
			for (int c = 0; c < gameState[r].length; c++) {
				gameState[r][c] = 0;
			}
		}
	};

	/**
	 * Displays the winner and prepares the board for reset
	 * @param w The winning int (0 or O, 1 for X, 2 for Tie)
	 */
	private void winner(int w) {
		switch (w) {
			case 0:
				lblWinner.setText("O Won!");
				break;
			case 1:
				lblWinner.setText("X Won!");
				break;
			case 2:
				lblWinner.setText("Tie!");
				break;
			case 3:
				return;
		}

		btnReset.setDisable(false);

		for (int c = 0; c < buttons.length; c++) {
			for (int r = 0; r < buttons[c].length; r++) {
				buttons[c][r].setDisable(true);
			}
		}
	}

	/**
	 * Checks for a winner. Probably not the most efficient solution, but it gets the job done
	 * @return The winning value. 0 for O, 1 for X, 2 for tie, 3 for no winner (e.g. keep playing)
	 */
	private int checkGame() {
		int[] rowTotals = new int[gameState.length];
		int[] colTotals = new int[gameState[0].length];
		int[] diagTotal = new int[gameState.length];


		for(int r = 0; r < gameState.length; r++) {
			for(int c = 0; c < gameState[r].length; c++) {
				rowTotals[r] += gameState[r][c];
				colTotals[c] += gameState[r][c];

				if(r==c)
					diagTotal[0] += gameState[r][c];

				if(r==0 && c==2)
					diagTotal[1] += gameState[r][c];

				if(r==1 && c==1)
					diagTotal[1] += gameState[r][c];

				if(r==2 && c==0)
					diagTotal[1] += gameState[r][c];
			}
		}

		for (int val : rowTotals) {
			if (val == 30) {
				return 1;
			} else if (val == 300) {
				return 0;
			}
		}

		for (int val : colTotals) {
			if(val == 30) {
				return 1;
			} else if (val == 300) {
				return 0;
			}
		}

		for (int val : diagTotal) {
			if(val == 30) {
				return 1;
			} else if (val == 300) {
				return 0;
			}
		}

		for(int r = 0; r < gameState.length; r++) {
			for(int c = 0; c < gameState[r].length; c++) {
				if (gameState[r][c] == 0) {
					return 3;
				}
			}
		}

		return 2;
	}
}
