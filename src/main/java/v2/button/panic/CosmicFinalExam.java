package v2.button.panic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CosmicFinalExam {

    /**
     * You must scream "Fanta" n times.
     */
    public static void welcome(int n) {
        // TODO: Implement method to print "Fanta" n times
        for (int i = 0; i < n ; i++) {
            System.out.println("Fanta");
        }
    }

    /**
     * You tag into the Drakoris Dragon Transport Network.
     * This immediately deducts 12 orglings.
     */
    public static double tagIn(double balance) {
        // TODO: Implement tagIn fare deduction logic
        if (balance < 12) {
            throw new IllegalArgumentException();
        }
        return balance-12;
    }

    /**
     * You transfer from one dragon to another, costing 2 more orglings.
     */
    public static double transfer(double balance) {
        // TODO: Implement transfer fare deduction logic
        if (balance < 2) {
            throw new IllegalArgumentException();
        }
        return balance-2;
    }

    /**
     * You tag out when reaching your destination.
     * Destinations:
     *   Zenthros  - 20
     *   Kryndor   - 22
     *   Bryxaria  - 27
     * Unknown    - 0 (only the initial 12 was deducted)
     */
    public static double tagOut(String destination, double balance) throws IllegalArgumentException {
        // TODO: Implement tagOut fare deduction logic based on destination
        balance = tagIn(balance);
        Map<String, Double> fares = Map.of("zenthros", 20.0,"kryndor" ,
                22.0,"bryxaria",27.0);
        if (balance < fares.getOrDefault(destination.toLowerCase(),0.0)) {
            throw new IllegalArgumentException();
        }
        return balance - fares.getOrDefault(destination.toLowerCase(),0.0);
    }

    /**
     * Compute the gravitational force between origin and destination planets.
     * Formula: F = G * (m1 * m2) / (r * r)
     * G (in java) = 6.67430e-11;
     */
    public static double lawOfGravity(double m1, double m2, double r) {
        // TODO: Implement gravitational force calculation
        if (r == 0){
            throw new IllegalArgumentException();
        }
        return 6.67430e-11 * (m1 * m2) / (r*r);
    }

    /**
     * Compute the velocity of a spacecraft.
     * Formula: v = d / t
     *
     * Author's note:
     *      Maybe you need to use a float, or the Author is dumb, One is true
     */
    //todo: perhaps he is?
    public static double velocity(double distance, double time) {
        // TODO: Implement velocity calculation
        if (time<=0.0){
            throw new IllegalArgumentException();
        }
        return distance / time;
    }

    /**
     * Caesar Cipher implementation with a given shift.
     */
    public static String caesarCipher(String input, int shift) {
        // TODO: Implement Caesar Cipher encryption
        if (input == null || input.isEmpty()) {
            return input;
        }

        shift = shift % 26;
        if (shift < 0) {
            shift += 26;
        }

        StringBuilder result = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                char shifted = (char) ((c - base + shift) % 26 + base);
                result.append(shifted);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Draw a Mario-style pyramid of height n.
     */
    public static void drawPyramid(int height) {
        // TODO: Implement pyramid drawing using console output
        if (height<0){
            throw new IllegalArgumentException();
        }
            for (int i = 1; i <= height; i++) {
                for (int j = 0; j < height - i; j++) {
                    System.out.print(" ");
                }
                for (int k = 0; k < i; k++) {
                    System.out.print("#");
                }
                if (i != height) {
                    System.out.println();
                }
        }
    }

    /**
     * Bughar's code — Refactor this mess.
     * You may not change logic, only clarity and structure.
     */
    //todo: not quite sure what the problem requires
    public static boolean isPrime(int number) {
        if (number < 2) {
            return false;
        }
        if (number ==2 ){
            return true;
        }
        else if (number % 2 == 0) {
            return false;
        }
        for (int i = 3; i * i <= number; i += 2) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Write a function that display the orgling a cash denominations
     * The denomination should be from top to bottom, Ignore decimal Values
     *
     * Orgling Denominations
     * "A" = 10
     * "B" = 15
     * "Z" = 5
     * "Y" = 1
     * 
     * Sample Result
     * showOrglings(27) = "BAYY"
     * showOrglings(20) = "BZ"
     *
     * @param Orglings
     * @return
     */
    public static String showOrglings( double Orglings ) {
        // TODO: Implement this method
        StringBuilder orglin = new StringBuilder();
        ArrayList<List<String>>  denominations = new ArrayList<>();
        denominations.add(List.of("B","15.0"));
        denominations.add(List.of("A","10.0"));
        denominations.add(List.of("Z","5.0"));
        denominations.add(List.of("Y","1.0"));

        for (List<String> conversion : denominations){
            double currentDouble = Double.parseDouble(conversion.get(1));
            String currentOrg = conversion.get(0);
            while (Orglings >= currentDouble){
                Orglings -= currentDouble;
                orglin.append(currentOrg);
            }
        }
        return orglin.toString();
    }

    /**
     * <p>
     * Calculates the total number of orglings earned based on a sequence of decisions.
     * Each decision contributes a predefined number of orglings. The player is said
     * to <strong>ESCAPE</strong> if the total orglings earned is <code>&gt;= 50</code>.
     * Otherwise, the player remains <strong>TRAPPED</strong>.
     * </p>
     *
     * <p><strong>Orgling Contribution Table:</strong></p>
     * <ul>
     *     <li><code>FIGHT</code>  - 15 orglings</li>
     *     <li><code>HIDE</code>   - 5 orglings</li>
     *     <li><code>RUN</code>    - 10 orglings</li>
     *     <li><code>STEAL</code>  - 20 orglings</li>
     *     <li><code>BRIBE</code>  - 8 orglings</li>
     *     <li><code>WAIT</code>   - 0 orglings</li>
     *     <li><code>CRY</code>    - -5 orglings (negative impact)</li>
     * </ul>
     *
     * <p>
     * Any unrecognized decision contributes 0 orglings by default.
     * </p>
     *
     * <p><strong>Examples:</strong></p>
     * <pre>
     * getOut(new String[] {"FIGHT", "RUN", "HIDE"}) ➞ "TRAPPED"  (15 + 10 + 5 = 30)
     * getOut(new String[] {"STEAL", "FIGHT", "BRIBE"}) ➞ "ESCAPE" (20 + 15 + 8 = 43 → still TRAPPED)
     * getOut(new String[] {"STEAL", "STEAL", "FIGHT"}) ➞ "ESCAPE" (20 + 20 + 15 = 55)
     * </pre>
     *
     * @param decisions An array of string decisions made by the player.
     * @return "ESCAPE" if the total orglings are 50 or more, otherwise "TRAPPED".
     */
    public static String getOut(String[] decisions) {
        // TODO: Implement the logic using the orgling values listed above
        return "";
    }

}
