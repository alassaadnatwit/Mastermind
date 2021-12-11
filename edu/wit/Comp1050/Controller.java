package edu.wit.Comp1050;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.commons.configuration2.BaseConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Controller {
    @FXML
    private Text TextFeedback;
    @FXML
    private Button yesbtn;
    @FXML
    private Button row8;
    @FXML
    private Button row10;
    @FXML
    private Button row12;
    @FXML
    private Button nobtn;
    @FXML
    private MenuItem configsettings;
    @FXML
    private Menu filemenu;
    @FXML
    private VBox Vboxpins;
    @FXML
    private Circle CodeC1;
    @FXML
    private Circle CodeC2;
    @FXML
    private Circle CodeC3;
    @FXML
    private Circle CodeC4;
    @FXML
    private VBox Vbox;

    private boolean repeatBoon = false;

    private GameCode game = new GameCode("HOLD");

    private int[] colorIndex = new int[]{0, 0, 0, 0};
    private int rowcounter;
    private boolean win = false;
    private int[] pattern = new int[4];
    private int counter;
    private Socket socket;
    private BufferedReader inputServer;
    private PrintWriter output;
    private String patternHolder;

    @FXML
    private Button NEWGAME;
    @FXML
    private Button NEWGUESS;

    public Controller() throws IOException, ConfigurationException {
    }

    public Controller getController() {
        return this;
    }

    @FXML
    //initializes the buttons and creates the socket needed for multiplayer
    public void initialize() {
        try {
            socket = new Socket("localHost", 1234);
            inputServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(),true);
        }
        catch(Exception e){
            System.out.println();
        }
        configsettings.setOnAction((event) -> {
            Stage configsetting = new Stage();
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("configbox.fxml"));
            } catch (IOException e) {
                System.out.println("Wrong");
            }
            configsetting.setScene(new Scene(root, 450, 400));
            configsetting.show();

        });
        NEWGAME.setOnAction((event) -> {
            try {
                output.println("ready");
                patternHolder = inputServer.readLine();
            } catch (Exception e){
                System.out.println("PatternHolder error");
            }
            TextFeedback.setText("");
            CodeC1.setFill(Color.BLACK);
            CodeC2.setFill(Color.BLACK);
            CodeC3.setFill(Color.BLACK);
            CodeC4.setFill(Color.BLACK);
            Vbox.getChildren().clear();
            Vboxpins.getChildren().clear();
            try {
                game = new GameCode(patternHolder);
            }
            catch (Exception ee){
                System.out.println();
            }
            NEWGAME.setOpacity(0);
            NEWGUESS.setOpacity(100);
            rowcounter = 1;
            createHbox();
        });
        playgame();
    }

    private void playgame() {
        for (int row = rowcounter; row < game.getRows(); row++) {
            NEWGUESS.setOnAction(event -> {
                if (colorIndex[0] == 0 || colorIndex[1] == 0 || colorIndex[2] == 0 || colorIndex[3] == 0)
                    TextFeedback.setText("Please change the color of all circles");
                else {
                    TextFeedback.setText("");
                    try {
                        output.println(colorIndex[0] + "" + colorIndex[1] + "" + colorIndex[2] + "" + colorIndex[3]);
                        counter = Integer.parseInt(inputServer.readLine());
                    }catch(Exception e){
                        System.out.println();
                    }
                    createHboxPins();
                    checkgame();
                    resetCounter();
                    rowcounter++;
                    if(rowcounter != game.getRows() + 1 || (win && rowcounter < game.getRows() + 1))
                        createHbox();
                }
            });
        }
    }

    //uses the count and rowcounter to check if the game is over
    private void checkgame() {
        if (counter == 4) {
            TextFeedback.setText("You win");
            changeColor(CodeC1, game.getPatternAr(0));
            changeColor(CodeC2, game.getPatternAr(1));
            changeColor(CodeC3, game.getPatternAr(2));
            changeColor(CodeC4, game.getPatternAr(3));
            NEWGUESS.setOpacity(0);
            NEWGAME.setOpacity(100);
        }
        if (rowcounter == game.getRows()) {
            TextFeedback.setText("You Lose");
            changeColor(CodeC1, game.getPatternAr(0));
            changeColor(CodeC2, game.getPatternAr(1));
            changeColor(CodeC3, game.getPatternAr(2));
            changeColor(CodeC4, game.getPatternAr(3));
            NEWGUESS.setOpacity(0);
            NEWGAME.setOpacity(100);
        }
    }

    //Visual stuff needed for the gui
    private void createHbox() {
        HBox Guessline = new HBox();
        Guessline.setAlignment(Pos.CENTER);
        Guessline.setMinHeight(40);
        Guessline.setSpacing(10.0);
        Vbox.setSpacing(4);
        HBox space = new HBox(1);
        Vbox.getChildren().add(space);
        Vbox.getChildren().add(Guessline);
        game.getPatternAr();
        for (int k = 0; k < 4; k++) {
            int j = k;
            Circle c = new Circle(20);
            Guessline.getChildren().add(c);
            c.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    changeColor(c, counterColor(j));
                }
            });
        }
    }

    private void createHboxPins() {
        HBox Guessline = new HBox();
        Guessline.setSpacing(6.0);
        Guessline.setAlignment(Pos.BASELINE_CENTER);
        Vboxpins.setSpacing(15);
        HBox space = new HBox(0);
        Vboxpins.getChildren().add(space);
        Vboxpins.getChildren().add(Guessline);
        for (int k = 0; k < 4; k++) {
            Circle c = new Circle(10);
            c.setFill(changeColor(counter, k));
            Guessline.getChildren().add(c);
        }
    }

    //{"R","B","Y","M","O","W"}
    //Changes the color of the circle
    private void changeColor(Shape c, int i) {
        {
            if (i == 1)
                c.setFill(Color.RED);
            if (i == 2)
                c.setFill(Color.BLUE);
            if (i == 3)
                c.setFill(Color.YELLOW);
            if (i == 4)
                c.setFill(Color.MAGENTA);
            if (i == 5)
                c.setFill(Color.ORANGE);
            if (i == 6)
                c.setFill(Color.WHITE);

        }
    }

    //changes colors of the hint pins
    private Color changeColor(int i,int k) {
        {
            if (i >= k+1)
                return Color.WHITE;
            else
                return Color.BLACK;
        }
    }

    //changes the color everytime you click the circle
    private int counterColor(int i) {
        if (colorIndex[i] == 6) {
            colorIndex[i] = 1;
            return colorIndex[i];
        } else {
            colorIndex[i] = colorIndex[i] + 1;
            return colorIndex[i];
        }
    }

    private void resetCounter() {
        colorIndex = new int[]{0, 0, 0, 0};
    }

}
