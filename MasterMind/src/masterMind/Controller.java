package masterMind;


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
import java.io.File;
import java.io.IOException;

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

    GameCode game = new GameCode();

    private int[] colorIndex = new int[]{0, 0, 0, 0};
    private int rowcounter;
    private boolean win = false;
    private int[] pattern = new int[4];
    private int counter;
    private int rows;


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
    public void initialize() {
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
            TextFeedback.setText("");
            CodeC1.setFill(Color.BLACK);
            CodeC2.setFill(Color.BLACK);
            CodeC3.setFill(Color.BLACK);
            CodeC4.setFill(Color.BLACK);
            Vbox.getChildren().clear();
            Vboxpins.getChildren().clear();
            try {
                game = new GameCode();
            }
            catch (Exception ee){
                System.out.println("This is wrong");
            }
            game.loadConfig();
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
                    //System.out.println("Please change the color of all circles");
                else {
                    TextFeedback.setText("");
                    counter = game.pins(colorIndex);
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
                    //System.out.println(colorIndex[0] + " " + colorIndex[1] +" " + colorIndex[2] +" " + colorIndex[3]);
                }
            });
        }
        //System.out.println(game.getPins(0) + " " + game.getPins(1) +" " + game.getPins(2) + " " + game.getPins(3));
        //System.out.println(game.getPatternAr(0) + " " + game.getPatternAr(1) +" " + game.getPatternAr(2) + " " + game.getPatternAr(3));
        //game.getPattern();
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
            int j = k;
            Circle c = new Circle(10);
            c.setFill(changeColor(game.getPins(k), c));
            Guessline.getChildren().add(c);
        }
    }

    //{"R","B","Y","M","O","W"}
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

    private Color changeColor(int i, Shape c) {
        {
            if (i == 1)
                return Color.WHITE;
            else
                return Color.BLACK;
        }
    }

    private int counterColor(int i) {
        if (colorIndex[i] == 7) {
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
