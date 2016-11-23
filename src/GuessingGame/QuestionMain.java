/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GuessingGame;

import java.io.*;
import java.util.Scanner;
/**
 * Basic text user interface.
 * @author Jacob Aylward
 */
public abstract class QuestionMain implements UserInterface{ //implements
    public static void main(String[] args){
        QuestionMain tq = new QuestionMain(){
            @Override
            public void println(String message) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        tq.run();
    }
    /*
    Fields
    */
    private Scanner console;
    private QuestionTree tree;
    /**
     * Constructs a text user interface and its question tree.
     * QuestionMain()
     */
    public QuestionMain(){
        console= new Scanner(System.in);
        tree = new QuestionTree(this);
    }
    /**
     * Returns the User's response as a String
     * nextLine()
     */
    public String nextLine(){
        return console.nextLine();
    }
    /**
     * Prints the given string to the console.
     * print(String message)
     */
    public void print(String message){
        System.out.print(message);
        System.out.print(" ");
    }
    /**
     * Prints the given String to the console.
     * println()
     */
    public void println(){
        System.out.println();
    }
    /**
     * Waits for the user to answer a yes/no question on the console and return
     * the users response as a boolean
     * nextBoolean()
     */
    public boolean nextBoolean(){
        String answer = console.nextLine();
        return answer.trim().toLowerCase().startsWith("y");
    }
    /**
     * Private helper for overall game(s) loop
     * run()
     */
    private void run(){
        System.out.println("                                         _ __");
        System.out.println("        ___                             | '  \\");
        System.out.println("   ___  \\ /              ,'\\_           | .-. \\        /|");
        System.out.println("   \\ /  | |  ___     _   |   \\          | | | |       ,' |_   /|");
        System.out.println("   \\ /  | |,'__ \\  ,'\\_  |  _\\          | | | |   _  ,' |_   /|");
        System.out.println("// | |  | |____| | | |\\_|| |__    //    | .-. | ,'_`. | | '-. .-',' `. ,'\\_");
        System.out.println("\\\\_| |_,' .-, _  | | |   | |\\ \\  //    .| |\\_/ | / \\ || |   | | / |\\  \\|   \\");
        System.out.println(" `-. .-'| |/ / | | | |   | | \\ \\//     |  |    | | | || |   | | | |_\\ || |\\_|");
        System.out.println("   | |  | || \\_| | | |   /_\\  \\ /      | |`    | | | || |   | | | .---'| |");
        System.out.println("   | |  | |\\___,_\\ /_\\ _      //       | |     | \\_/ || |   | | | |  /\\| |");
        System.out.println("   /_\\  | |           //_____//       .||`      `._,' | |   | | \\ `-' /| |");
        System.out.println("        /_\\           `------'        \\ |   Guess      `.\\  | |  `._,' /_\\");
        System.out.println("                                       \\|        20          `.\\");
        System.out.println("                                                    Character Question Game!");
        System.out.println("By: Jacob Aylward, Adeiza Jatto and Matthew Bryson");
        load();
        /*
        Think of a character 
        */
        System.out.println("\n" + BANNER_MESSAGE);
        do{
            /*
            Play 1 complete game
            */
            println();
            tree.play();
            print(PLAY_AGAIN_MESSAGE);
        } while(nextBoolean()); //Prompt to play again
        //prints overall stats
        System.out.println("\n" + String.format(STATUS_MESSAGE, tree.totalGames(), tree.gamesWon()));
        save();
    }
    /**
     * Ask the user whether they want to save or load.
     * load()
     */
    private void load(){
        print(LOAD_MESSAGE);
        if(nextBoolean()){
            print(SAVE_LOAD_FILENAME_MESSAGE);
            String filename = nextLine();
            try{
                Scanner in = new Scanner(new File(filename));
                tree.load(in);
            } catch(FileNotFoundException e){
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    /**
     * Ask user whether they want to save or load.
     * save()
     */
    private void save(){
        print(SAVE_MESSAGE);
        if(nextBoolean()){
            print(SAVE_LOAD_FILENAME_MESSAGE);
            String filename = nextLine();
            try{
                PrintStream out = new PrintStream(new File(filename));
                tree.save(out);
                out.close();
            } catch(FileNotFoundException e){
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}