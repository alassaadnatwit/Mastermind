package edu.wit.Comp1050;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class MasterMindServer {
    static class guessRequest implements Runnable{

        private PrintWriter outToPattern;
        private PrintWriter outToGuesser;
        private BufferedReader inFromClient;
        private BufferedReader inFromGuesser;
        private int[] intpattern = new int[4];
        public Socket guessSocket;
        public Socket patternSocket;

        public guessRequest(Socket s,Socket c){
                patternSocket = s;
                guessSocket = c;
                try {
                    outToPattern = new PrintWriter(patternSocket.getOutputStream(), true);
                    inFromClient = new BufferedReader(new InputStreamReader(patternSocket.getInputStream()));
                    outToGuesser = new PrintWriter(guessSocket.getOutputStream(),true);
                    inFromGuesser = new BufferedReader(new InputStreamReader(guessSocket.getInputStream()));
                }
                catch(Exception e){
                    System.out.println();
                }
        }
        //Takes in pattern socket and then guesser socket in that order and creates a guessRequest object
        //and starts the thread
        public static void main(String args[]) {
            System.out.println("Waiting for Connection");
            try (ServerSocket serversocket = new ServerSocket(1234)) {
                    Socket patternSocket = serversocket.accept();
                    System.out.println("Pattern Client connected");
                    Socket guessSocket = serversocket.accept();
                    System.out.println("guess Client connected");
                    guessRequest serverThread = new guessRequest(patternSocket, guessSocket);
                    Thread thread = new Thread(serverThread);
                    thread.start();
            } catch (Exception e) {
                System.out.println("error");

            }
        }

        //Uses a true while loop to and a if statement to be able play as many games as possible
        //then anothor while loop for each turn of the game until won or lose due to the count while is how many colors are correct
        @Override
        public void run() {
            while(true) {
                try {
                    int correctColors = 0;
                    int rows = 0;
                    if(inFromGuesser.readLine().equals("ready")) {
                        outToPattern.println("Please input 4 Colors, R,B,Y,M,O,W");
                        String pattern = inFromClient.readLine();
                        parsePattern(pattern);
                        System.out.println(intpattern[0] +""+intpattern[1] +""+intpattern[2] +""+intpattern[3]);
                        outToGuesser.println(pattern);
                        while(correctColors !=4 && rows < 8){
                            correctColors = pins(inFromGuesser.readLine());
                            outToGuesser.println(correctColors);
                            rows++;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //checks how many colors are correct
        public int pins(String s) {
            int pincounter = 0;
            for(int i = 0; i < 4; i++){
                if (Integer.parseInt(s.substring(i,i+1)) == intpattern[i]){
                    pincounter++;
                }
            }
            return pincounter;
        }
        //"1","2","3","4","5","6"}; //{"R","B","Y","M","O","W"}
        //Since my orignal game used indexs to check the colors, this just coverts the string into indexs to make it easier
        public void parsePattern(String s){
            for(int i = 0; i<4; i++){
                if(s.charAt(i) == 'R'){
                    intpattern[i] = 1;
                }
                if(s.charAt(i) == 'B'){
                    intpattern[i] = 2;
                }
                if(s.charAt(i) == 'Y'){
                    intpattern[i] = 3;
                }
                if(s.charAt(i) == 'M'){
                    intpattern[i] = 4;
                }
                if(s.charAt(i) == 'O'){
                    intpattern[i] = 5;
                }
                if(s.charAt(i) == 'W'){
                    intpattern[i] = 6;
                }
            }
        }
    }
}
