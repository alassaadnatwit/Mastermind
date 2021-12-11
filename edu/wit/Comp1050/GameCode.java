package edu.wit.Comp1050;

import com.sun.javafx.scene.control.Properties;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.apache.commons.configuration2.BaseConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.Socket;
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
    //String pat;
    private String[] correctPins = new String[]{"0","0","0","0"}; // zero equals wrong, 1 correct
    private String[] guessPattern = new String[4];

    //Even though pattern is still stored it is not used to check the if the player is correct, it is just used to reveal the pattern at the end
    public GameCode(String pat) throws ConfigurationException {
        loadConfig();
        colorArray(pat);
    }
    //Sets the pattern
    private void colorArray(String pat){
        for(int i = 0; i < 4; i ++){
            if(pat.charAt(i) == 'R')
                pattern[i] = colors[0];
            if(pat.charAt(i) == 'B')
                pattern[i] = colors[1];
            if(pat.charAt(i) == 'Y')
                pattern[i] = colors[2];
            if(pat.charAt(i) == 'M')
                pattern[i] = colors[3];
            if(pat.charAt(i) == 'O')
                pattern[i] = colors[4];
            if(pat.charAt(i) == 'W')
                pattern[i] = colors[5];

        }
    }

    //My orginal randomly generated code
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

    //How many guesses the player has
    public int getRows(){
        return rows;
    }

    //sets the pattern into index colors
    public void getPatternAr(){
        for(int i = 0; i < 4; i++){
            intPattern[i] = Integer.parseInt(pattern[i]);
        }
    }

    public int getPatternAr(int i){
        return intPattern[i];
    }

    //Was used for configuration purposes.
    Configurations settings = new Configurations();
    BaseConfiguration config = settings.properties(new File("mmind.properties"));

    public void loadConfig() {
        try {
            rows = 8;
            k = config.getBoolean("dupsAllowedInCode");
        } catch (Exception e) {
            System.out.println("wrong");
        }
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
