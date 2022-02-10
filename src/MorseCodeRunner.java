
import javax.swing.JFrame;

public class MorseCodeRunner {

    /**
     * creates a frame to displace to the user
     * @param args
     */
    public static void main( String args[] ){

        MorseCodeFrame morseCodeFrame = new MorseCodeFrame();
        morseCodeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        morseCodeFrame.setSize(1500,200);
        morseCodeFrame.setVisible(true);
    }
}
