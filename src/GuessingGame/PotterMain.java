/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GuessingGame;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.*;
import javax.swing.*;

/** 
 * The graphical user interface (GUI)
 * @author Jacob Aylward
 */
public class PotterMain implements ActionListener, KeyListener, Runnable, UserInterface{
    private static final boolean CONSOLE_OUTPUT = true;  //True = print I/O messages
    /*
    Sound Options
    */
    private static final boolean MUSIC = true;
    private static final boolean SOUNDFX = true;
    private static final double SOUND_PERCENTAGE = 0.6;  // In-Game Sound Effects
    private static int NUM_SOUNDS = 8; //Number of wav files
    private static int NUM_MUSICS = 2; //Number of wav files
    /*
    Text that appears on Frame
    */
    private static final String MUSIC_MESSAGE = "Music";
    private static final String SFX_MESSAGE = "Sound FX";
    private static final String YES_MESSAGE = "Yes";
    private static final String NO_MESSAGE = "No";
    private static final String ERROR_MESSAGE = "Error";
    private static String TITLE = "HarryPotter_GuessingGame";
    /*
    File Names, Paths, URL's
    */
    private static final String RESOURCE_URL = "http://www."; //jacobsCloud
    private static final String SAVE_DEFAULT_FILE_NAME = "HarryPotter.txt";
    //private static final String MUSIC_FILE_NAME = "HedwigsTheme.wav";
    private static final String MUSIC_FILE_NAME = "ThemeRemix.wav";
    private static String BACKGROUND_IMAGE_FILE_NAME = "background.png";
    private static String SOUND_FILE_NAME = "yourawizardharry.wav";
    // For Funny Sound effects uncomment this line private static String SOUND_FILE_NAME = "ThemeRemix.wav";
    /*
    Graphical emelents
    */
    private static final Font FONT = new Font("new", Font.BOLD, 25); //This may cause issues if font isnt preinstalled
    //private static final Font FONT = new Font("cambria", Font.BOLD, 25);
    private static final Color COLOR = new Color(6, 226, 240);  // RGB Light Teal
    /*
    Runs Program
    */
    public static void main(String[] args){
        new PotterMain();
    }
    /*
    Data Fields
    */
    private JFrame frame;
    private JLabel harryPotter, bannerLabel;
    private JTextArea statsArea, messageLabel;
    private JTextField inputField;
    private JButton yesButton, noButton;
    private JCheckBox musicBox, soundBox;
    private AudioClip musicClip;
    private QuestionTree game;
    /*
    Queues hold boolean values of string user input
    */
    private BlockingQueue<Boolean> booleanQueue = new LinkedBlockingQueue<Boolean>();
    private BlockingQueue<String> stringQueue = new LinkedBlockingQueue<String>();
    private boolean waitingForBoolean = false;
    private boolean waitingForString = false;
    /*
    Constructs GUI and Components
    */
    public PotterMain(){
        game = new QuestionTree(this);
        /*
        Constructs Everything
        */
        frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.addKeyListener(this);
        harryPotter = new JLabel();
        harryPotter.setLayout(null);
        if(ensureFile(BACKGROUND_IMAGE_FILE_NAME)){
            harryPotter.setIcon(new ImageIcon(BACKGROUND_IMAGE_FILE_NAME));
        }
        /*
        Layout
        */
        frame.add(harryPotter);
        frame.pack();
        center(frame);
        /*
        Constructs other components
        */
        //JTextField The Prompt for inputing text data
        inputField = new JTextField(30);
        setupComponent(inputField, new Point(10, 240), new Dimension(300, 25));
        inputField.setCaretColor(Color.GREEN);
        inputField.addActionListener(this);
        //JTextArea The display text for Questions and Answers
        messageLabel = new JTextArea();
        setupComponent(messageLabel, new Point(10, 120), new Dimension(500, 125));
        messageLabel.setLineWrap(true);
        messageLabel.setWrapStyleWord(true);
        messageLabel.setEditable(false);
        messageLabel.setFocusable(false);
        //JLabel For the Banner Message
        bannerLabel = new JLabel();
        setupComponent(bannerLabel, new Point(0, 0), new Dimension(harryPotter.getWidth(), 30));
        bannerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        //JTextArea Yes No buttons
        statsArea = new JTextArea();
        setupComponent(statsArea, 
                new Point(harryPotter.getWidth() - 200, harryPotter.getHeight() - 450),
                new Dimension(290, 90));
        statsArea.setEditable(false);
        statsArea.setFocusable(false);
        yesButton = makeButton(YES_MESSAGE, new Point(40, 80), new Dimension(80, 37));
        noButton = makeButton(NO_MESSAGE, new Point(140, 80), new Dimension(80, 37));
        yesButton.addKeyListener(this);
        noButton.addKeyListener(this);
        
        musicBox = makeCheckBox(MUSIC_MESSAGE, MUSIC,
                new Point(harryPotter.getWidth() - 200, harryPotter.getHeight() - 520), 
                new Dimension(120, 20));
        
        soundBox = makeCheckBox(SFX_MESSAGE, SOUNDFX,
                new Point(harryPotter.getWidth() - 200, harryPotter.getHeight() - 495),
                new Dimension(200, 20));
        musicBox.addKeyListener(this);
        soundBox.addKeyListener(this);
        doEnabling();
        frame.setVisible(true);
        /*
        Background Thread (looping)
        */
        new Thread(this).start();
    }
    /**
     * Handles user interactions with graphic buttons
     * actionPerformed(ActionEvent event)
     */
    public void actionPerformed(ActionEvent event){
        Object src = event.getSource();
        if(src == yesButton){
            yes();
        } else if(src == noButton){
            no();
        } else if(src == inputField){
            input();
        } else if(src == musicBox){
            // play or stop music
            if(musicClip != null){
                if(musicBox.isSelected()){
                    musicClip.loop();
                } else{
                    musicClip.stop();
                }
            }
        }
        playSound();
    }
    /**
     * Key listener interface
     * KeyPressed(KeyEvent event)
     */
    public void keyPressed(KeyEvent event){
        if(!yesButton.isVisible() || event.isAltDown() || event.isControlDown()){
            return;
        }
        char key = Character.toLowerCase(event.getKeyChar());
        if(key == 'y'){
            yes();
        } else if(key == 'n'){
            no();
        }
    }
    /**
     * Key listener interface
     * KeyReleased(KeyEvent event)
     */
    public void keyReleased(KeyEvent event){}
    /**
     * Key listener interface
     * KeyTyped(KeyEvent event)
     */
    public void keyTyped(KeyEvent event){}
    /**
     * Return user input
     * nextLine()
     */
    public String nextLine(){
        return nextLine(null);
    }
    /**
     * Outputs given text to GUI
     * print(String text)
     */
    public void print(String text){
        messageLabel.setText(text);
        if(CONSOLE_OUTPUT){
            System.out.print(text + " ");
        }
    }
    /**
     * Outputs the given text onto the GUI
     * println(String text)
     */
    public void println(String text){
        messageLabel.setText(text);
        if(CONSOLE_OUTPUT){
            System.out.println(text);
        }
    }
    /**
     * Outputs the given text onto the GUI
     * println()
     */
    public void println(){
        if(CONSOLE_OUTPUT){
            System.out.println();
        }
    }
    /**
     * Game Loop
     * run()
     */
    public void run(){
        /*
        Audio Resource
        */
        playMusic();
        /*
        Load Data
        */
        saveLoad(false);
        /*
        Play multiple games
        */
        do{
            if(CONSOLE_OUTPUT) System.out.println();
            game.play();
            print(PLAY_AGAIN_MESSAGE);
        } while(nextBoolean());
        //game = new QuestionTree(this);
        /*
        Save Data
        */
        saveLoad(true);
        bannerLabel.setVisible(false);
        // shut down
        // frame.setVisible(false);
        // frame.dispose();
        // System.exit(0);
    }
    /**
     * Waits for user input and returns value
     * nextBoolean()
     */
    public boolean nextBoolean(){
        setWaitingForBoolean(true);
        try {
            boolean result = booleanQueue.take();
            messageLabel.setText(null);
            if(CONSOLE_OUTPUT){
                System.out.println(result ? "yes" : "no");
            }
            return result;
        } catch(InterruptedException e){
            return false;
        } finally{
            setWaitingForBoolean(false);
        }
    }
    /**
     * Sets JFrame position
     * center(JFrame frame)
     * @param frame 
     */
    private void center(JFrame frame){
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((screen.width - frame.getWidth()) / 2,
                (screen.height - frame.getHeight()) / 2);
    }
    /**
     * Turns on/off necessary graphical components
     * doEnabling()
     */
    private void doEnabling(){
        inputField.setVisible(waitingForString);
        if(waitingForString){
            inputField.requestFocus();
            inputField.setCaretPosition(inputField.getText().length());
        }
        yesButton.setVisible(waitingForBoolean);
        noButton.setVisible(waitingForBoolean);
        bannerLabel.setText(BANNER_MESSAGE);
        statsArea.setText(String.format(STATUS_MESSAGE,
                game.totalGames(), game.gamesWon()));
    }
    /**
     * Helper to download resources to local file from jacobscloud
     * @param urlString
     * @param filename
     * @throws IOException
     * @throws MalformedURLException 
     */
    private static void download(String urlString, String filename) throws IOException, MalformedURLException{
        File file = new File(filename);
        System.out.println("Downloading");
        System.out.println("from: " + urlString);
        System.out.println("  to: " + file.getAbsolutePath());
        System.out.println();        
        URL url = new URL(urlString);
        InputStream stream = url.openStream();
        /*
        Read bytes from URL into a byte[]
        */
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        while(true){
            int b = stream.read();
            if (b < 0) {
                break;
            }
            bytes.write(b);
        }
        /*
        Write bytes from byte[] to file
        */
        FileOutputStream out = new FileOutputStream(filename);
        out.write(bytes.toByteArray());
        out.close();
    }
    /**
     * Download if file doesn't exist
     * @param filename
     * @return 
     */
    private boolean ensureFile(String filename){
        File file = new File(filename);
        if(!file.exists() || file.length() == 0){
            try{
                download(RESOURCE_URL + filename, filename);
            } catch(Exception e){
                JOptionPane.showMessageDialog(frame, e.toString(), ERROR_MESSAGE,
                        JOptionPane.ERROR_MESSAGE);
                if(CONSOLE_OUTPUT){
                    System.out.println(e);
                    e.printStackTrace();
                }
            }
        }
        return file.exists();
    }
    /**
     * Response to pressing enter on input field
     * input()
     */
    private void input(){
        try{
            String text = inputField.getText();
            inputField.setText(null);
            stringQueue.put(text);
            doEnabling();
        } catch(InterruptedException e){}
    }
    /**
     * Helper method to create button
     * makeButton(String text, Point location, Dimension size)
     * @param text
     * @param location
     * @param size
     * @return 
     */
    private JButton makeButton(String text, Point location, Dimension size){
        JButton button = new JButton(text);
        button.setMnemonic(text.charAt(0));
        setupComponent(button, location, size);
        button.setOpaque(true);
        button.setContentAreaFilled(false);
        button.addActionListener(this);
        button.setFocusPainted(false);
        return button;
    }
    /**
     * Helper method to create checkbox
     * makeCheckBox(String text, boolean selected, Point location, Dimension size)
     * @param text
     * @param selected
     * @param location
     * @param size
     * @return 
     */
    private JCheckBox makeCheckBox(String text, boolean selected, Point location, Dimension size){
        JCheckBox box = new JCheckBox(text, selected);
        box.setMnemonic(text.charAt(0));
        setupComponent(box, location, size);
        box.setOpaque(true);
        box.setContentAreaFilled(false);
        box.addActionListener(this);
        box.setFocusPainted(false);
        return box;
    }
    /**
     * Private helper for asking a question
     * @param defaultValue
     * @return 
     */
    private String nextLine(String defaultValue){
        inputField.setText(defaultValue);
        setWaitingForString(true);
        try{
            // grab/store text from box; clear the box text
            String result = stringQueue.take();
            messageLabel.setText(null);
            if(CONSOLE_OUTPUT){
                System.out.println(result);
            }
            return result;
        } catch(InterruptedException e){
            return "";
        } finally{
            setWaitingForString(false);
        }
    }
    /**
     * Response to a no event
     * no()
     */
    private void no(){
        try{
            booleanQueue.put(false);
            doEnabling();
        } catch(InterruptedException e){}
    }
    /**
     * loops audio
     * playAudioClip(String filename, boolean loop)
     * @param filename
     * @param loop
     * @return 
     */
    private AudioClip playAudioClip(String filename, boolean loop) {
        if(!ensureFile(filename)){
            return null;
        }
        URL url = ClassLoader.getSystemResource(filename);
        if(url == null){
            try{
                url = new File(filename).toURI().toURL();
            } catch(MalformedURLException e){}
        }
        if(CONSOLE_OUTPUT){
            System.out.println("Playing sound: " + url);
        }
        AudioClip clip = null;
        try{
            clip = Applet.newAudioClip(url);
            if(loop){
                clip.loop();
            } else{
                clip.play();
            }
        } catch(NullPointerException e){
            if (CONSOLE_OUTPUT) {
                System.out.println("Error: Unable to load audio clip");
            }
        }
        return clip;
    }
    /**
     * Plays background music
     * playMusic()
     */
    private void playMusic(){
        if (musicBox.isSelected()){
            try{
                if(musicClip == null){
                    int rand = 1 + (int) (Math.random() * NUM_MUSICS);
                    if(Math.random() < 0.66){
                        rand = 1;   // 2/3 default
                    }
                    String filename = String.format(MUSIC_FILE_NAME, rand);
                    musicClip = playAudioClip(filename, true);
                }
            } catch(NullPointerException e){
                if (CONSOLE_OUTPUT) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * Randomly picks wav file
     * playSound()
     */
    private void playSound(){
        if(soundBox.isSelected() && Math.random() <= SOUND_PERCENTAGE){
            new Thread(new SoundPlayer()).start();
        }
    }
    /**
     * Randomly plays sound
     * playSoundReally()
     */
    private void playSoundReally(){
        int rand = 1 + (int) (Math.random() * NUM_SOUNDS);
        String filename = String.format(SOUND_FILE_NAME, rand);
        playAudioClip(filename, false);
    }
    /**
     * Ask user if they want to save game
     * saveLoad(boolean save)
     * @param save 
     */
    private void saveLoad(boolean save){
        print(save ? SAVE_MESSAGE : LOAD_MESSAGE);
        if(nextBoolean()){
            print(SAVE_LOAD_FILENAME_MESSAGE);
            String filename = nextLine(SAVE_DEFAULT_FILE_NAME);
            try{
                if(save){
                    PrintStream out = new PrintStream(new File(filename));
                    game.save(out);
                } else{
                    Scanner in = new Scanner(new File(filename));
                    game.load(in);
                }
            } catch(FileNotFoundException e){
                JOptionPane.showMessageDialog(frame, e.toString(), ERROR_MESSAGE,
                        JOptionPane.ERROR_MESSAGE);
                if(CONSOLE_OUTPUT){
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * Sets standard fonts colors and locations
     * setupComponent(JComponent comp, Point location, Dimension size)
     * @param comp
     * @param location
     * @param size 
     */
    private void setupComponent(JComponent comp, Point location, Dimension size){
        comp.setLocation(location);
        comp.setSize(size);
        comp.setForeground(COLOR);
        comp.setFont(FONT);
        comp.setOpaque(false);
        harryPotter.add(comp);
    }
    /**
     * Sets the GUI to wait for user input
     * setWaitingForBoolean(boolean value)
     * @param value 
     */
    private void setWaitingForBoolean(boolean value){
        waitingForBoolean = value;
        doEnabling();
    }
    /**
     * Sets the GUI to wait for user input
     * setWaitingForString(boolean value)
     * @param value 
     */
    // sets the GUI to wait for a text user input
    private void setWaitingForString(boolean value){
        waitingForString = value;
        doEnabling();
    }
    /**
     * response to yes by button or text
     * yes()
     */
    private void yes(){
        try{
            booleanQueue.put(true);
            doEnabling();
        } catch(InterruptedException e){}
    }
    /**
     * Fixes error with audio file locking up the game
     */
    private class SoundPlayer implements Runnable{
        public void run(){
            playSoundReally();
        }
    }
}