package v2.button.panic;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CosmicFinalExamTest {

    @Test
    void testWelcome() {
        // Test normal case
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        CosmicFinalExam.welcome(3);
        assertEquals("Fanta\nFanta\nFanta\n", outContent.toString());

        // Test zero case
        outContent.reset();
        CosmicFinalExam.welcome(0);
        assertEquals("", outContent.toString());

        // Test negative case - should we throw exception instead?
        outContent.reset();
        CosmicFinalExam.welcome(-1);
        assertEquals("", outContent.toString()); // or should we verify exception?
    }

    @Test
    void testTagIn() {
        // Normal case
        assertEquals(88.0, CosmicFinalExam.tagIn(100.0), 0.001);

        // Exact amount case
        assertEquals(0.0, CosmicFinalExam.tagIn(12.0), 0.001);

        // Insufficient funds case (should go negative)
        assertEquals(-12.0, CosmicFinalExam.tagIn(0.0), 0.001);

        // Large number case
        assertEquals(Double.MAX_VALUE - 12, CosmicFinalExam.tagIn(Double.MAX_VALUE), 0.001);
    }

    @Test
    void testTransferDeductsExactly2Orglings() {
        // Case 1: Sufficient balance (normal case)
        assertEquals(48.0, CosmicFinalExam.transfer(50.0), 0.001 );

        // Case 2: Exact deduction (balance = 2)
        assertEquals(0.0, CosmicFinalExam.transfer(2.0), 0.001 );

        // Case 3: Insufficient funds (balance < 2)
        assertEquals(-2.0, CosmicFinalExam.transfer(0.0), 0.001 );

        // Case 4: Floating-point precision (e.g., 5.5 - 2)
        assertEquals(3.5, CosmicFinalExam.transfer(5.5), 0.001 );
    }

    @Test
    void testTransferRejectsNegativeBalances() {
        // Case 1: Sufficient balance (normal case)
        assertEquals(48.0, CosmicFinalExam.transfer(50.0), 0.001 );
        // Case 2: Exact deduction (balance = 2)
        assertEquals(0.0, CosmicFinalExam.transfer(2.0), 0.001 );

        // Case 3: Insufficient funds (balance < 2) → THROWS EXCEPTION
        assertThrows(IllegalArgumentException.class, () -> {
            CosmicFinalExam.transfer(0.0);
        }, "Should throw if balance < 2");

        // Case 4: Floating-point precision (e.g., 5.5 - 2)
        assertEquals(3.5, CosmicFinalExam.transfer(5.5),
                "Should handle decimals correctly");

        // Case 5: Attempt to transfer with balance = 1.999 → THROWS
        assertThrows(IllegalArgumentException.class, () -> {
            CosmicFinalExam.transfer(1.999);
        }, "Should throw if deduction would result in negative");
    }

    @Test
    void testTagInRejectsNegativeBalances() {
        // Case 1: Sufficient balance (normal case)
        assertEquals(88.0, CosmicFinalExam.tagIn(100.0),
                "Should deduct 12 from 100 → 88");

        // Case 2: Exact deduction (balance = 12)
        assertEquals(0.0, CosmicFinalExam.tagIn(12.0),
                "Should deduct 12 from 12 → 0");

        // Case 3: Insufficient funds (balance < 12) → THROWS
        assertThrows(IllegalArgumentException.class, () -> {
            CosmicFinalExam.tagIn(0.0);
        }, "Should throw if balance < 12");

        // Case 4: Floating-point precision (e.g., 12.5 - 12 = 0.5)
        assertEquals(0.5, CosmicFinalExam.tagIn(12.5),
                "Should handle decimals correctly");

        // Case 5: Attempt with balance = 11.999 → THROWS
        assertThrows(IllegalArgumentException.class, () -> {
            CosmicFinalExam.tagIn(11.999);
        }, "Should throw if deduction would result in negative");
    }

    @Test
    void testTagOutDeductsDestinationFare() {
        // Case 1: Zenthros (20 orglings) //Todo: tagging in while tagging out is not intuitive
        assertEquals(68, CosmicFinalExam.tagOut("Zenthros", 100.0),
                "100 - 12 (tagIn) - 20 (Zenthros) = 68");

        // Case 2: Kryndor (22 orglings)
        assertEquals(66.0, CosmicFinalExam.tagOut("Kryndor", 100.0),
                "100 - 12 - 22 = 66");

        // Case 3: Unknown destination (0 additional)
        assertEquals(88.0, CosmicFinalExam.tagOut("Unknown", 100.0),
                "100 - 12 - 0 = 88");
    }

    @Test
    void testTagOutRejectsNegativeBalances() {
        // Case 4: Insufficient funds for Zenthros (12 + 20 = 32 needed)
        assertThrows(IllegalArgumentException.class, () -> {
            CosmicFinalExam.tagOut("Zenthros", 31.9);
        }, "Should throw if balance < 32");

        // Case 5: Exact fare (balance = 12 + 27 = 39 for Bryxaria)
        assertEquals(0.0, CosmicFinalExam.tagOut("Bryxaria", 39.0),
                "39 - 12 - 27 = 0");
    }

    @Test
    void testTagOutCaseInsensitiveDestination() {
        // Case 6: Case-insensitive destination (e.g., "zEnThRoS")
        assertEquals(68.0, CosmicFinalExam.tagOut("zEnThRoS", 100.0),
                "Should handle case-insensitive destinations");
    }

    @Test
    void testLawOfGravityStandardCase() {
        double G = 6.67430e-11;
        double earthMass = 5.972e24;   // kg
        double moonMass = 7.342e22;    // kg
        double earthMoonDist = 3.844e8; // meters

        double expectedForce = G * (earthMass * moonMass) / Math.pow(earthMoonDist, 2);
        double actualForce = CosmicFinalExam.lawOfGravity(earthMass, moonMass, earthMoonDist);

        assertEquals(expectedForce, actualForce, 1e-20,
                "Should calculate Earth-Moon gravitational force");
    }

    @Test
    void testLawOfGravityZeroMass() {
        // Case: One mass is zero → Force should be zero
        assertEquals(0.0, CosmicFinalExam.lawOfGravity(0.0, 10.0, 5.0),
                "If m1=0, force should be 0");
    }

    @Test
    void testLawOfGravityZeroDistance() {
        // Case: Distance is zero → Should throw (division by zero)
        assertThrows(IllegalArgumentException.class, () -> {
            CosmicFinalExam.lawOfGravity(10.0, 20.0, 0.0);
        }, "r=0 should throw IllegalArgumentException");
    }

    @Test
    void testLawOfGravitySmallValues() {
        // Case: Tiny masses/distance (precision handling)
        double G = 6.67430e-11;
        double result = CosmicFinalExam.lawOfGravity(1e-10, 1e-10, 1e-5);
        double expected = G * (1e-10 * 1e-10) / Math.pow(1e-5, 2);
        assertEquals(expected, result, 1e-30,
                "Should handle very small values accurately");
    }

    @Test
    void testVelocityStandardCase() {
        // Case 1: Normal values (d=100m, t=10s → v=10m/s)
        assertEquals(10.0, CosmicFinalExam.velocity(100.0, 10.0));

        // Case 2: Decimal values (d=7.5m, t=1.5s → v=5m/s)
        assertEquals(5.0, CosmicFinalExam.velocity(7.5, 1.5));

        // Case 3: Zero distance (d=0m, t=5s → v=0m/s)
        assertEquals(0.0, CosmicFinalExam.velocity(0.0, 5.0));
    }

    @Test
    void testVelocityRejectsZeroTime() {
        // Case 4: Time = 0 → Throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            CosmicFinalExam.velocity(50.0, 0.0);
        }, "t=0 should throw IllegalArgumentException");

        // Case 5: Negative time → Throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            CosmicFinalExam.velocity(50.0, -2.0);
        }, "Negative time should throw");
    }

    @Test
    void testVelocityExtremeValues() {
        // Case 6: Very large distance/small time
        assertEquals(1e10, CosmicFinalExam.velocity(1e20, 1e10));

        // Case 7: Tiny distance/large time
        assertEquals(1e-10, CosmicFinalExam.velocity(1e-5, 1e5));
    }

    @Test
    void testCaesarCipherStandardCase() {
        // Case 1: Basic shift (shift=3)
        assertEquals("Khoor", CosmicFinalExam.caesarCipher("Hello", 3));

        // Case 2: Wrap-around (shift=5, 'V' → 'A')
        assertEquals("Afdhf", CosmicFinalExam.caesarCipher("Vasco", 5));

        // Case 3: Mixed case and non-letters
        assertEquals("Dpef 123!", CosmicFinalExam.caesarCipher("Bmcd 123!", 2));
    }

    @Test
    void testCaesarCipherEdgeCases() {
        // Case 4: Empty string
        assertEquals("", CosmicFinalExam.caesarCipher("", 5));

        // Case 5: shift=0 (no change)
        assertEquals("Hello", CosmicFinalExam.caesarCipher("Hello", 0));

        // Case 6: shift=26 (no change)
        assertEquals("Hello", CosmicFinalExam.caesarCipher("Hello", 26));

        // Case 7: Large shift (shift=30 → equivalent to shift=4)
        assertEquals("Lipps", CosmicFinalExam.caesarCipher("Hello", 30));
    }

    @Test
    void testCaesarCipherNegativeShift() {
        // Case 8: Negative shift (shift=-3)
        assertEquals("Ebiil", CosmicFinalExam.caesarCipher("Hello", -3));

        // Case 9: Negative wrap-around (shift=-30 → equivalent to shift=-4)
        assertEquals("Dahhk", CosmicFinalExam.caesarCipher("Hello", -30));
    }

    @Test
    void testDrawPyramidStandardCase() {
        // Redirect System.out to capture output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Case 1: height=3
        CosmicFinalExam.drawPyramid(3);
        String expected =
                """
                          #
                         ##
                        ###""";
        assertEquals(expected, outContent.toString());

        // Reset
        outContent.reset();
    }

    @Test
    void testDrawPyramidHeightOne() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Case 2: height=1
        CosmicFinalExam.drawPyramid(1);
        assertEquals("#\n", outContent.toString());
    }

    @Test
    void testDrawPyramidLarge() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        CosmicFinalExam.drawPyramid(11);

        // Level 1 (10 spaces + #)
        // Level 2 (9 spaces + ##)
        String expected =
                """
                                  #
                                 ##
                                ###
                               ####
                              #####
                             ######
                            #######
                           ########
                          #########
                         ##########
                        ###########""";      // Level 11 (0 spaces + 11 #s, NO trailing \n)

        assertEquals(expected, outContent.toString());
    }

    @Test
    void testDrawPyramidZeroHeight() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Case 3: height=0 → prints nothing
        CosmicFinalExam.drawPyramid(0);
        assertEquals("", outContent.toString());
    }

    @Test
    void testDrawPyramidRejectsNegativeHeight() {
        // Case 4: height=-1 → throws exception
        assertThrows(IllegalArgumentException.class, () -> {
            CosmicFinalExam.drawPyramid(-1);
        });
    }

    @Test
    void testIsPrimeStandardCases() {
        // Small primes
        assertTrue(CosmicFinalExam.isPrime(2));
        assertTrue(CosmicFinalExam.isPrime(3));
        assertTrue(CosmicFinalExam.isPrime(17));

        // Small non-primes
        assertFalse(CosmicFinalExam.isPrime(4));
        assertFalse(CosmicFinalExam.isPrime(9));
        assertFalse(CosmicFinalExam.isPrime(15));
    }

    @Test
    void testIsPrimeEdgeCases() {
        // Numbers ≤1
        assertFalse(CosmicFinalExam.isPrime(1));
        assertFalse(CosmicFinalExam.isPrime(0));
        assertFalse(CosmicFinalExam.isPrime(-5));

        // Large primes/non-primes
        assertTrue(CosmicFinalExam.isPrime(7919));  // Known large prime
        assertFalse(CosmicFinalExam.isPrime(7920)); // 7920 = 2^4 × 3^2 × 5 × 11
    }

    @Test
    void testIsPrimeMethodLength() throws Exception {
        Path path = Paths.get("src/main/java/v2/button/panic/CosmicFinalExam.java");
        List<String> lines = Files.readAllLines(path);

        boolean inMethod = false;
        int charCount = 0;

        for (String line : lines) {
            if (line.contains("public static boolean isPrime")) {
                inMethod = true;
            }
            if (inMethod) {
                charCount += line.trim().length(); // Only count non-whitespace
                if (line.trim().equals("}")) {
                    break;
                }
            }
        }

        int allowedLimit = 280; // Customize this limit based on your intended restriction
        assertTrue(charCount <= allowedLimit,
                "The isPrime method exceeds the allowed character count. Found: " + charCount);
    }


    @Test
    void testIsPrimePerformance() {
        // Verify efficiency (should handle large numbers without delay)
        long startTime = System.nanoTime();
        long duration = System.nanoTime() - startTime;
        
        // Run tests on efficiency multiple times to avoid lucky runs
        assertTrue(CosmicFinalExam.isPrime(1_000_003));
        assertTrue(duration < 1_000_000_000, "Should complete within 1 second");
        assertTrue(CosmicFinalExam.isPrime(1_000_003));
        assertTrue(duration < 1_000_000_000, "Should complete within 1 second");
        assertTrue(CosmicFinalExam.isPrime(1_000_003));
        assertTrue(duration < 1_000_000_000, "Should complete within 1 second");
    }
    
    // There should be a test here to check initial character count and final character count
    // The Idea is I want you to solve this with as little changes as possible, and without
    // applying a whole different method 

    @Test
    void testOrglingPresentationEasy() {
    // Orglings are dumb, so do not expect this to make sense
    String result;

    result = CosmicFinalExam.showOrglings(0);
    assertEquals("", result); // No value to convert

    result = CosmicFinalExam.showOrglings(1);
    assertEquals("Y", result); // Single unit

    result = CosmicFinalExam.showOrglings(2);
    assertEquals("YY", result); // 1 + 1

    result = CosmicFinalExam.showOrglings(5);
    assertEquals("Z", result); // Exact match with Z

    result = CosmicFinalExam.showOrglings(9);
    assertEquals("ZYYYY", result); // 5 + 4*1

    result = CosmicFinalExam.showOrglings(4);
    assertEquals("YYYY", result); // Just Ys

    result = CosmicFinalExam.showOrglings(14);
    assertEquals("AZYYYY", result); // 10 + 5 = 15 → too much → must be 10 + 1 + 1 + 1 + 1
    assertEquals("AYYYY", CosmicFinalExam.showOrglings(14));
}


    @Test
    void testOrglingPresentationMedium() {
        String result;

        result = CosmicFinalExam.showOrglings(27);
        assertEquals("BAYY", result); // 15 + 10 + 1 + 1

        result = CosmicFinalExam.showOrglings(20);
        assertEquals("BZ", result); // 15 + 5

        result = CosmicFinalExam.showOrglings(11);
        assertEquals("AY", result); // 10 + 1

        result = CosmicFinalExam.showOrglings(5);
        assertEquals("Z", result); // Just one Z
    }

    @Test
    void testOrglingPresentationHard() {
        String result;

        result = CosmicFinalExam.showOrglings(56);
        assertEquals("BBBAAYY", result); 

        result = CosmicFinalExam.showOrglings(56);
        assertEquals("BBAAY", result); // 15 + 15 + 10 + 10 + 1 + 1

        result = CosmicFinalExam.showOrglings(100);
        // Try 15*6 = 90, then 10 = 100 → "BBBBBBA"
        assertEquals("BBBBBBA", result);

        result = CosmicFinalExam.showOrglings(3);
        assertEquals("YYY", result); // 1 + 1 + 1

        result = CosmicFinalExam.showOrglings(99);
        // 15*6 = 90, then 5 = 95, 1*4 = 99 → "BBBBBBZYYYY"
        assertEquals("BBBBBBZYYYY", result);
    }


    // Arigato...

}
