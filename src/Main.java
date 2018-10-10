import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * ONE-TIME-PAD CRIB DRAGGING 1.0
 * Created by robertbarta on 06.04.16
 * An application created for VU Introduction To Security at Vienna University Of Technology for the second assignment
 * !Conversion methods were taken from StackOverflow and were not written by me!
 *
 * This application takes two cyphertexts, XORs them and then XORs the result with a guessed word or phrase at each position
 * to reveal readable text (cribs). Below, I've listed the cyphertexts for the assignment and the solution, which was entirely
 * computed using this program.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("ONE-TIME-PAD Crib Dragging 1.0");
        System.out.println("#############################################");
        String cyphertext1 = "";
        String cyphertext2 = "";
        String guessedWord = "";

        Scanner sc = new Scanner(System.in);

        System.out.println();
        System.out.println("Enter a cyphertext in hex:");
        if (sc.hasNext()) {
            cyphertext1 = sc.next();
        }

        System.out.println();
        System.out.println("And another cyphertext in hex:");
        if (sc.hasNext()) {
            cyphertext2 = sc.next();
        }

        sc.useDelimiter("\n");
        System.out.println();
        System.out.println("Now guess a word or phrase:");
        if (sc.hasNext()) {
                guessedWord = sc.next();
        }

        String word = convertStringToHex(guessedWord);

        System.out.println();

        List<String> results = oneTimePad(cyphertext1, cyphertext2, word);
        for (String s : results) {
            System.out.println(s);
        }
    }

    /**
     * A crib dragging method for finding cribs in two given cyphertexts.
     * @param cyphertext1 First cyphertext in hex.
     * @param cyphertext2 Second cyphertext in hex.
     * @param guessedWord A guessed word or phrase in ASCII.
     * @return A list of (cyphertext1 XOR cyphertext2) XOR guessedWord at each position of
     * the smaller cyphertext in readable ASCII.
     */
    private static List<String> oneTimePad(String cyphertext1, String cyphertext2, String guessedWord) {
        List<String> results = new ArrayList<String>();

        for (int i = 0; i < Math.min(cyphertext1.length() - guessedWord.length() + 1, cyphertext2.length() - guessedWord.length() + 1); i += 2) {
            String sub1 = cyphertext1.substring(i, i + guessedWord.length());
            String sub2 = cyphertext2.substring(i, i + guessedWord.length());

            String xOrCypher = xorHex(sub1, sub2);
            String res = xorHex(xOrCypher, guessedWord);

            String converted = convertHexToString(res);

            results.add(i/2+1 + ": " + converted);
        }

        return results;
    }

    public static String convertHexToString(String hex){

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for( int i=0; i<hex.length()-1; i+=2 ) {

            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char) decimal);

            temp.append(decimal);
        }

        return sb.toString();
    }

    public static String convertStringToHex(String ascii){
        StringBuilder hex = new StringBuilder();

        for (int i=0; i < ascii.length(); i++) {
            hex.append(Integer.toHexString(ascii.charAt(i)));
        }
        return hex.toString();
    }

    public static String xorHex(String a, String b) {
        char[] chars = new char[a.length()];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = toHex(fromHex(a.charAt(i)) ^ fromHex(b.charAt(i)));
        }
        return new String(chars);
    }

    private static int fromHex(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'F') {
            return c - 'A' + 10;
        }
        if (c >= 'a' && c <= 'f') {
            return c - 'a' + 10;
        }
        throw new IllegalArgumentException();
    }

    private static char toHex(int nybble) {
        if (nybble < 0 || nybble > 15) {
            throw new IllegalArgumentException();
        }
        return "0123456789ABCDEF".charAt(nybble);
    }
}
