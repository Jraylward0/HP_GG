/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GuessingGame;

import java.io.*;
import java.util.Scanner;
/**
 *
 * @author Jacob Aylward
 */
public class QuestionTree{
    private QuestionNode overallRoot;
    private final UserInterface ui;
    private int totalGames;
    private int gamesWon;
    private final Scanner console;
    /*
    Pre: UI ! = null throws IllegalArgumentException
    Post: constructs the first questionNode
    */
    /**
     * Constructs first node.
     * QuestionTree(UserInterface UI)
     * @param ui
     */
    public QuestionTree(UserInterface ui){
        nullOrNot(ui, "UI");
        console = new Scanner(System.in);
        //overallRool = new QuestionNode();
        this.ui = ui;
        totalGames = 0;
        gamesWon = 0;
    }
    /**
     * Creates new QuestionTree from provided text file
     * read(Scanner input)
     * @param input
     */
    public void read(Scanner input){
        overallRoot = read(input, overallRoot);
    }
    /**
     * Returns QuestionNode with data from input file
     * Recursively updates sub-tree
     * read(Scanner input, QuestionNode root)
     */
    private QuestionNode read(Scanner input, QuestionNode root){
        if(input.hasNextLine()){
            String selection = input.nextLine();
            String text = input.nextLine();
            if(selection.equals("Q:")){
                root = new QuestionNode(selection, text);
                root.left = read(input, root.left);
                root.right = read(input, root.right);
            }
            else{ 
                root = new QuestionNode(selection, text);
            }
        }
        return root;
    }
    /**
     * write(printStream output)
     * @param output
     */
    public void write(PrintStream output){
        output = write(output, overallRoot);
    }
    /**
     * Prints to output file in preorder
     * write(PrintStream output, QuestionNode root)
     */
    private PrintStream write(PrintStream output, QuestionNode root){
        if(root != null){
            output.println(root.data);
            output.println(root.text);
            write(output, root.left);
            write(output, root.right);
        }
        return output;
    }
    /**
     * Post: uses the current QuestionTree to play()
     * play()
     */
    public void play(){
        overallRoot = play(overallRoot);
        totalGames++;
    }
    /**
     * Helper method
     * play(QuestionNode root)
     */
    private QuestionNode play(QuestionNode root){
        if(root != null){
            if(root.data.equals("A:")){
                if(yesTo("The Character you're thinking of was: " + root.text + "?")){
                    ui.print("Cool, I guessed right");
                    gamesWon++;
                }
                else{
                    QuestionNode temp = root;
                    ui.print("What is the name of your character? ");
                    QuestionNode answer = new QuestionNode("A:", console.nextLine());
                    ui.print("Please insert a y/n Question");
                    ui.print("Distinguishing your character from everyone else: ");
                    String question = console.nextLine();
                    //Adds new question to text file
                    if(yesTo("The answer for your new character (y/n) ?")){
                        root = new QuestionNode("Q:", question, answer, temp);
                    }
                    else{
                        root = new QuestionNode("Q:", question, temp, answer);
                    }
                }
            }
            else{
                /*
                Recursively Climbs Tree
                */
                if(yesTo(root.text)){
                    root.left = play(root.left);
                }
                else{
                    root.right = play(root.right);
                }
            }
        }
        return root;
    }
    /**
     * Pre: output != null throws IllegalArgumentException
     * Post: saves the QuestionTree to an output file in preorder traversal
     * save(PrintStream output)
     * @param output
     */
    public void save(PrintStream output){
        nullOrNot(output, "output");
        save(output, overallRoot);
    }
    /**
     * Helper method to save QuestionTree to an output file
     * save(PrintStream output, QuestionNode root)
     */
    private void save(PrintStream output, QuestionNode root){
        /*
        Leaf node = answer
        */
        if(root.left == null && root.right == null){
            output.println("A:" + root.data);
        }
        else{
            output.println("Q:" + root.data);
            save(output, root.left); //recurse through left side first
            save(output, root.right); // recurse through right side if possible
        }
    }
    /**
     * Pre: input != null throws IllegalArgumentException
     * Post: replaces the current QuestionTree by reading another tree from a file
     * load(Scanner input)
     * @param input
     */
    public void load(Scanner input){
        overallRoot = loadHelper(input, overallRoot);
    }
    /**
     * Helper method to replace the current QuestionTree by reading another tree from a file
     * loadHelper(Scanner input, QuestionNode root)
     */
    private QuestionNode loadHelper(Scanner input, QuestionNode root){
        if(input.hasNextLine()){
            String data = input.nextLine();
            String text = input.nextLine();
            if(data.equals("Q:")){
                root = new QuestionNode(data, text);
                root.left = loadHelper(input, root.left);
                root.right = loadHelper(input, root.right);
            }
            else{
                root = new QuestionNode(data, text);
            }
        }
        return root;
    }
    /**
     * Post: returns the total number of games played
     * totalGames()
     * @return 
     */
    public int totalGames(){
        return totalGames;
    }
    /**
     * Post: returns the total number of games program has won
     * gamesWon()
     * @return 
     */
    public int gamesWon(){
        return gamesWon;
    }
    /**
     * Throws IllegalArgumentException (passed by Object, check, is null
     * nullOrNot()
     */
    private void nullOrNot(Object check, String name){
        if(check == null){
            throw new IllegalArgumentException(name + ": " + check);
        }
    }
    /**
     * boolean
     * yesTo(String prompt)
     * @param prompt
     * @return 
     */
    public boolean yesTo(String prompt){
        System.out.print(prompt + "(y/n)");
        String response = ui.nextLine().trim().toLowerCase();
        while(!response.equals("y") && !response.equals("n")){
            ui.print("Please answer y or n.");
            ui.print(prompt + "(y/n)");
            response = ui.nextLine().trim().toLowerCase();
        }
        return response.equals("y");
    }
}