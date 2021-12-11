package edu.wit.Comp1050;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MasterMindPatternClient {
    //Simple client that just waits for server to ask for pattern and then sends the pattern
    public static void main(String[] args) {
        try {
            System.out.println("Waiting for connection..");
            Socket socket = new Socket("localHost", 1234);
            BufferedReader inputServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(),true);
            while(true) {
                String respones = inputServer.readLine();
                System.out.println(respones);
                Scanner scanner = new Scanner(System.in);
                String pattern;
                pattern = scanner.nextLine();
                output.println(pattern);
            }
        } catch (Exception e){
            System.out.println();
        }
    }
}
