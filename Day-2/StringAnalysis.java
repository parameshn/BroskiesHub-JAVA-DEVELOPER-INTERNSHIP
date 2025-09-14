/* Key Concepts:Work with String class, loops, StringBuilder, and conditional statements.


Steps:
 1.Take a string input from the user.
 2.Reverse the string using StringBuilder.
 3.Count vowels and consonants using a loop.
 4.Check if the string equals its reverse (palindrome).
 5.Print a l results. */


import java.util.Scanner;

public class StringAnalysis {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Step 1: Take string input from user
        System.out.print("Enter a string: ");
        String input = scanner.nextLine();

        // Step 2: Reverse the string using StringBuilder
        StringBuilder sb = new StringBuilder(input);
        String reversed = sb.reverse().toString();

        // Step 3: Count vowels and consonants using a loop
        int vowelCount = 0;
        int consonantCount = 0;
        String vowels = "aeiouAEIOU";

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (Character.isLetter(ch)) {
                if (vowels.indexOf(ch) != -1) {
                    vowelCount++;
                } else {
                    consonantCount++;
                }
            }
        }

        // Step 4: Check if the string equals its reverse (palindrome)
        boolean isPalindrome = input.equalsIgnoreCase(reversed);

        // Step 5: Print all results
        System.out.println("\n--- STRING ANALYSIS RESULTS ---");
        System.out.println("Original string: " + input);
        System.out.println("Reversed string: " + reversed);
        System.out.println("Number of vowels: " + vowelCount);
        System.out.println("Number of consonants: " + consonantCount);
        System.out.println("Total letters: " + (vowelCount + consonantCount));
        System.out.println("Is palindrome? " + (isPalindrome ? "Yes" : "No"));

        // Additional analysis
        System.out.println("\n--- ADDITIONAL INFORMATION ---");
        System.out.println("String length: " + input.length());
        System.out.println("Uppercase version: " + input.toUpperCase());
        System.out.println("Lowercase version: " + input.toLowerCase());

        scanner.close();
    }
}