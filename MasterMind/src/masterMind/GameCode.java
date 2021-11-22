package masterMind;

import com.sun.javafx.scene.control.Properties;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.apache.commons.configuration2.BaseConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;

//I tried everything I could to be able to edit those configs and couldnt find how to do it.

public class GameCode {
    private int rows;
    private boolean k;
    private final String[] colors = new String[] {"1","2","3","4","5","6"}; //{"R","B","Y","M","O","W"}
    private boolean repeat = false;
    private String[] pattern = new String[4];
    private int[] intPattern = new int[4];
    private int[] intPins = new int[4];
    private String[] correctPins = new String[]{"0","0","0","0"}; // zero equals wrong, 1 correct
    private String[] guessPattern = new String[4];

    public GameCode() throws ConfigurationException {
        loadConfig();
        colorArray();
    }

    private void colorArray(){
        if(k){
            for(int i = 0;i < 4;i++){
                pattern[i] = (colors[(int) (Math.random() * colors.length)]);
            }
        }
        else{
            String temp ="";
            int checker;
            for(int i = 0;i < 4;i++){
                temp = colors[(int) (Math.random() * colors.length)];
                checker = i;
                if (i != 0){
                    for (String s : pattern) {
                        if (temp.equals(s)) {
                            i--;
                            break;
                        }
                    }
                    if(checker == i){
                        pattern[i] = temp;
                    }
                }
                else {
                    pattern[i] = temp;
                }
            }
        }
    }

    public void checkColors(int[] nums){

    }

    public int getRows(){
        return rows;
    }

    public void getPattern(){
        for(String s: pattern){
            System.out.print(s);
        }
    }

    public void getPatternAr(){
        for(int i = 0; i < 4; i++){
            intPattern[i] = Integer.parseInt(pattern[i]);
        }
    }

    public int getPatternAr(int i){
        return intPattern[i];
    }

    public int pins(int[] index) {
        int pincounter = 0;
        for(int i = 0; i < 4; i++){
            if (index[i] == intPattern[i]){
                pincounter++;
            }
        }
        for(int i = 0; i < pincounter; i++){
            intPins[i] = 1;
        }
        for(int i = pincounter; i < 4; i++){
            intPins[i] = 0;
        }
        return pincounter;
    }

    public int getPins(int i){
            return intPins[i];

    }

    public void clear(){
        for(int i = 0; i < 4; i++)
            guessPattern[i] = null;
    }

    public int checkGame(int[] num){
        int count = 0;
        for(int i = 0; i < 4; i++){
            if(num[i] == 1)
                count++;
        }
        return count;
    }

    public void setPins(){
        for(int i = 0; i < 4; i++){
            correctPins[i] = "0";
        }
    }

    public void oneGuess(){
        //checkGame();
        setPins();
    }

    Configurations settings = new Configurations();
    BaseConfiguration config = settings.properties(new File("mmind.properties"));

    public void loadConfig() {
        try {
            //config.setProperty("codeRows",2);
            rows = config.getInt("codeRows");
            k = config.getBoolean("dupsAllowedInCode");
        } catch (Exception e) {
            System.out.println("wrong");
        }
    }

    //public void loadConfigRows()

    public void setRows(int i){
        rows = i;
    }

    public void setDups(Boolean i){
        k = i;
    }

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
    public void initialize(){
        yesbtn.setOnAction((event) -> {
            try {
                config.setProperty("dupsAllowedInCode",true);
                k = true;
            } catch (Exception e) {
                System.out.println("wrong");
            }
        });
        nobtn.setOnAction((event) -> {
            try {
                config.setProperty("dupsAllowedInCode",false);
                k = false;
            } catch (Exception e) {
                System.out.println("wrong");
            }
        });
        row8.setOnAction((event) -> {
            try {
                config.setProperty("codeRows",8);
            } catch (Exception e) {
                System.out.println("wrong");
            }
        });
        row10.setOnAction((event) -> {
            try {
                config.setProperty("codeRows",10);
            } catch (Exception e) {
                System.out.println("wrong");
            }
        });
        row12.setOnAction((event) -> {
            try {
                config.setProperty("codeRows",12);
            } catch (Exception e) {
                System.out.println("wrong");
            }
        });
    }
}
