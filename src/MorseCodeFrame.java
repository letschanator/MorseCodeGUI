
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import java.lang.*;

public class MorseCodeFrame extends JFrame{

    /**
     * used to display input: so the user knows where to type
     */
    private final JTextField textField1;

    /**
     * used for the user to type in their morse code or english to be translated
     */
    private final JTextField textField2;

    /**
     * used to dispay output: so the user knows where there translated text will come out
     */
    private final JTextField textField3;

    /**
     * used for the translated text itself
     */
    private final JTextField textField4;

    /**
     * used to inform the user when an error has occurred
     */
    private final JTextField errorField;

    /**
     * used to allow the user to restart and thus type the other type of text/morse code into the input
     */
    private final JButton restartButton;

    /**
     * stores 0-9 and a-z in an ordered list for ease of use
     */
    final static char alphanumeric[] = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};

    /**
     * stores all the valid charters the user could input, includes 0-9, A-Z, a-z, -, ., and space
     */
    final static char validChar[] = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',' ','.','-'};

    /**
     * stores every alphanumeric morse code representation in order
     */
    final static String morseCode[] = {"-----",".----","..---","...--","....-",".....","-....","--...","---..","----.",".-","-...","-.-.","-..",".","..-.","--.","....","..",".---","-.-",".-..","--","-.","---",".--.","--.-",".-.","...","-","..-","...-",".--","-..-","-.--","--.."};

    /**
     * used to store all the charters of morse the user has input until they type a full letter
     */
    private String currentMorse = "";

    /**
     * stores whether the user is typing english or morse code into the text box
     */
    private static boolean isEnglish = true;

    /**
     * checks if the typing has started yet, used to see if the user can change between morse and english, because
     * that can only be done at the start.
     */
    private static boolean typingStarted = false;

    /**
     * creates the gui to run the code in, also contains the handlers within
     */
    public MorseCodeFrame(){
        super("Morse Code Converter");  // creates a basic frame with a title of Morse Code Converter
        setLayout(new FlowLayout());

        //creates the text field to to instruct the user where to type and adds it to the frame
        textField1 = new JTextField("Enter Morse Code or english here:");
        textField1.setEditable(false);
        add(textField1);

        //creates the text box the user is to type in and adds it to the frame
        textField2 = new JTextField(100);
        textField2.setFocusable(true);
        add(textField2);

        //creates the text field to show the user where their input will be translated and adds it to the frame
        textField3 = new JTextField("Conversion will appear here:");
        textField3.setEditable(false);
        add(textField3);

        //creates the text field where the translated text or morse code will be displayed and adds it to the frame
        textField4 = new JTextField(100);
        textField4.setEditable(false);
        add(textField4);

        //creates the text fiend where the error messages will go
        errorField = new JTextField(75);
        errorField.setEditable(false);
        add(errorField);

        //creates the restart button for the user and adds it to the frame
        restartButton = new JButton("restart");
        add(restartButton);
        restartButton.addActionListener(new ActionListener() {  // action listener for when the button is clicked
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                textField2.setText("");  // resets everything back to their starting values so the user can switch between
                textField4.setText("");  // morse code and english
                errorField.setText("");
                currentMorse = "";
                isEnglish = true;
                typingStarted = false;
            }
        });

        textField2.addKeyListener(new KeyListener() {  // adds and defines the key listener to know when and what the user is typing
            @Override
            public void keyTyped(KeyEvent e) { // dont need to do anything when key is typed

            }

            @Override
            public void keyPressed(KeyEvent e) { // when key is pressed, and thus before it is put in the text field need to make the backspace key not screw up future translation
                if(typingStarted){
                    if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){  //only need to do something if a back space was pressed
                        if (isEnglish){ //need to do different things depending if the user is typing english or morse code
                            if(textField4.getText().endsWith("  ")){ // if the previous input was a space
                                textField4.setText(textField4.getText().substring(0,textField4.getText().length()-3)); // we only need to get rid of the last 3 spaces in the translation (1 space converts to 3 spaces)
                            }else{ // other wise we do a bunch of calculations to take off what the previous letter was in morse code
                                textField4.setText(textField4.getText().substring(0,textField4.getText().length()-letterToMorse(textField2.getText().substring(textField2.getText().length()-1).toUpperCase()).length()-1));
                            }
                        }else{ //the user is typing in morse code
                            if(textField2.getText().endsWith(" ") && !textField2.getText().endsWith("   ") ) { // if the user just finished typing a letter
                                currentMorse = letterToMorse(textField4.getText().substring(textField4.getText().length()-1)); // sets the current morse back to what it was before
                                textField4.setText(textField4.getText().substring(0, textField4.getText().length() - 1)); // deletes the last letter of the output textbook
                            }else if (textField2.getText().endsWith("   ")){ // if the user just put in 3 spaces to indicate a word space
                                textField4.setText(textField4.getText().substring(0, textField4.getText().length() - 1));  // gets rid of the space at the end of the output
                                currentMorse = "  "; // set to space space so we know the most recent morse
                            }else{ //otherwise they are in the middle of a word in morse
                                currentMorse = currentMorse.substring(0,currentMorse.length()-1); //only need to chop one off the end of current morse
                            }
                        }if(textField2.getText().length() == 1){//if there was only one charter typed in the input text box
                            typingStarted = false;
                        }
                        return;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) { // after the key is released and thus after it is put into the the input text field
                String input = String.valueOf(e.getKeyChar()).toUpperCase(); // need to counter to upper case for looking through alphanumeric array
                if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){return;} // already handled this in previous method
                if(!isValidKey(e)){ // if the key is not a valid key
                    errorField.setText("Error: invalid input"); // gives the user an error message
                    textField2.setText(textField2.getText().substring(0,textField2.getText().length()-1)); // deletes the last thing they typed
                    return; //dont run the rest of the code in this method
                }else{
                    errorField.setText(""); // if the last input was an error need to make sure error no longer pops up
                }
                if(!typingStarted ){  // if this is the first input into the text field
                    if (input.equals(".") || input.equals("-")) {  // if the user is typing morse code
                        isEnglish = false; // sets english to false
                    }else if(!letterToMorse(input).equals(" ")){ // if the user typed an alphanumeric
                        isEnglish = true; //sets english to true
                    }
                }
                typingStarted = true;  // typing has now started and the user can't switch between morse code and english
                if(isEnglish){
                    if(letterToMorse(input).equals(" ") && !input.equals(" ")){ // the user typed to type morse code
                        errorField.setText("error: can not switch to morse code after typing english, press the restart button to switch"); //givers the user an error
                        textField2.setText(textField2.getText().substring(0,textField2.getText().length()-1)); // deletes the input
                        return;
                    }
                    if(!input.equals(" ")) { // the input is not a space
                        textField4.setText(textField4.getText() + letterToMorse(input) + " "); //just adds the morse representation to the end of the output box
                    }else{
                        textField4.setText(textField4.getText() + "   "); //adds 3 spaces to the end of the output box (3 spaces in morse represent 1 space for a word break)
                    }
                }else{ // the user is inputting morse code
                    if(!letterToMorse(input).equals(" ")){ // the user tried to input an alphanumeric
                        errorField.setText("error: can not switch to english text after typing morse code, press the restart button to switch"); // gives an error message
                        textField2.setText(textField2.getText().substring(0,textField2.getText().length()-1));  // deletes the input
                        return;
                    }
                    currentMorse += input; //adds the input the the end of current morse
                    if(input.equals(" ")){ //if the input is a space the user is done with that letter and can be converted in the output
                        if (textField2.getText().endsWith("   ")){ // if the last 2 inputs were also spaces
                            textField4.setText(textField4.getText() + " "); //means a word break and should add a space to the end of current morse
                            currentMorse = "";
                        }else if(!textField2.getText().endsWith("  ")){ //if exactly the last input was not a space
                            String letter = morseToLetter(currentMorse.substring(0, currentMorse.length() - 1)); // gets the letter representation of the morse
                            if (!letter.equals(" ")) { //if the last letter in morse was a correct translation of a letter
                                textField4.setText(textField4.getText() + letter); // adds that letter to the end of the output
                                currentMorse = ""; // and resets current morse
                            } else {
                                errorField.setText("error: invalid morse code"); // gives the user an error
                                textField2.setText(textField2.getText().substring(0, textField2.getText().lastIndexOf(currentMorse))); // gets rid of the input letter in morse
                                currentMorse = "";
                            }
                        }

                    }
                }

            }
        });

    }

    /**
     * checks if the key that was pressed is valid and thus is either: 0-9, a-z, A-Z, ., -, or space
     * @param e the key event that triggered this
     * @return  true if it is a valid charter false otherwise
     */
    private boolean isValidKey(KeyEvent e){
        for(int i = 0; i<validChar.length; i++){
            if ( e.getKeyChar() == validChar[i]){
                return true;
            }
        }
        return false;
    }

    /**
     * returns a string of the representation letter or number in morse code
     * @param letter the letter to be converted to morse code
     * @return the morse code or space if it's an invalid letter/number
     */
    public String letterToMorse(String letter){
        for (int i = 0; i<alphanumeric.length; i++){
            if (letter.equals(String.valueOf( alphanumeric[i]))){
                return (morseCode[i]);
            }
        }
        return (" ");

    }

    /**
     * returns the letter or number that the morse code represents
     * @param morse a string of the morse code to be converted
     * @return the letter or number that the morse code represents, space if the morse code is invalid
     */
    public String morseToLetter(String morse){
        for (int i = 0; i<morseCode.length; i++){
            if(morse.equals(morseCode[i])){
                return (String.valueOf(alphanumeric[i]));
            }
        }
        return (" ");
    }


}
