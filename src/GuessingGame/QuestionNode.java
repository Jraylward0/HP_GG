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
public class QuestionNode{
    public String data; //data stored in this node
    public QuestionNode left; //reference to left subtree
    public QuestionNode right; //reference to right subtree
    String text;
    /*
    Post: Constructs a leaf node with the given data
    */
    public QuestionNode(String data, String text){
        this(data, text, null, null);
    }
    /*
    Post: Constructs a QuestionNode with the given data and reference to other nodes
    */
    public QuestionNode(String data, String text, QuestionNode left, QuestionNode right){
        this.data = data;
        this.text = text;
        this.left = left;
        this.right = right;
    }
}
