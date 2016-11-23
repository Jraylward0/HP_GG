/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GuessingGame;

/**
 *
 * @author Jacob Aylward
 */
public interface UserInterface {
    /**
     * Waits for the user to input a yes/no answer and returns that boolean value.
     * nextBoolean()
     * @return the typed or pressed value
     */
    boolean nextBoolean();
    /**
     * Waits for the user to input a text vale.
     * nextLine()
     * @return the text value as String
     */
    String nextLine();
    /**
     * Displays the given output message to the user.
     * print(String message)
     * @param message the message to display //assume not null/(n/a)
     */
    void print(String message);
    /**
     * Displays the given output message to the user.
     * println(String message)
     * @param message The message to display //assume not null/(n/a)
     */
    void println(String message);
    /*
    Messages outputted by the User Interface
    */
    final String PLAY_AGAIN_MESSAGE = "Play Again?";
    final String SAVE_MESSAGE = "Shall I remeber these games?";
    final String LOAD_MESSAGE = "Shall I recall our previous games?";
    final String SAVE_LOAD_FILENAME_MESSAGE = "What is the file name?";
    final String STATUS_MESSAGE = "Gamesplayed: %d\nGames won: %d";
    final String BANNER_MESSAGE = "Think of a character, and I will guess him/her?";
}