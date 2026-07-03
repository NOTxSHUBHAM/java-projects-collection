import java.util.Scanner;
import java.util.LinkedHashMap;
import java.util.Map;

public class StringEncoding {

    // ─── Run-Length Encoding ───
    public static String encode(String input) {
        if (input == null || input.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < input.length()) {
            char ch = input.charAt(i);
            int count = 1;
            while (i + count < input.length() && input.charAt(i + count) == ch) count++;
            sb.append(ch).append(count);
            i += count;
        }
        return sb.toString();
    }

    public static String decode(String encoded) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < encoded.length()) {
            char ch = encoded.charAt(i++);
            StringBuilder numStr = new StringBuilder();
            while (i < encoded.length() && Character.isDigit(encoded.charAt(i)))
                numStr.append(encoded.charAt(i++));
            int count = numStr.length() > 0 ? Integer.parseInt(numStr.toString()) : 1;
            for (int j = 0; j < count; j++) sb.append(ch);
        }
        return sb.toString();
    }

    // ─── Caesar Cipher ───
    public static String caesarEncrypt(String text, int shift) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                sb.append((char) ((c - base + shift) % 26 + base));
            } else sb.append(c);
        }
        return sb.toString();
    }

    public static String caesarDecrypt(String text, int shift) {
        return caesarEncrypt(text, 26 - shift % 26);
    }

    // ─── Word Frequency ───
    public static void wordFrequency(String text) {
        String[] words = text.toLowerCase().split("\\s+");
        LinkedHashMap<String, Integer> freq = new LinkedHashMap<>();
        for (String w : words) {
            w = w.replaceAll("[^a-z]", "");
            if (!w.isEmpty()) freq.put(w, freq.getOrDefault(w, 0) + 1);
        }
        System.out.println("\n--- Word Frequency ---");
        freq.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .forEach(e -> System.out.printf("%-15s: %d%n", e.getKey(), e.getValue()));
    }

    // ─── String Stats ───
    public static void stringStats(String input) {
        int vowels = 0, consonants = 0, spaces = 0, digits = 0, special = 0;
        for (char c : input.toCharArray()) {
            if ("aeiouAEIOU".indexOf(c) >= 0) vowels++;
            else if (Character.isLetter(c))    consonants++;
            else if (c == ' ')                 spaces++;
            else if (Character.isDigit(c))     digits++;
            else                               special++;
        }
        System.out.println("\n--- String Statistics ---");
        System.out.println("Length    : " + input.length());
        System.out.println("Vowels    : " + vowels);
        System.out.println("Consonants: " + consonants);
        System.out.println("Spaces    : " + spaces);
        System.out.println("Digits    : " + digits);
        System.out.println("Special   : " + special);
        System.out.println("Reversed  : " + new StringBuilder(input).reverse());
        System.out.println("Uppercase : " + input.toUpperCase());
        System.out.println("Palindrome: " + isPalindrome(input));
    }

    public static boolean isPalindrome(String s) {
        String clean = s.toLowerCase().replaceAll("[^a-z0-9]", "");
        return clean.equals(new StringBuilder(clean).reverse().toString());
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n===== String Encoding & Analysis =====");
            System.out.println("1. Run-Length Encode");
            System.out.println("2. Run-Length Decode");
            System.out.println("3. Caesar Cipher Encrypt");
            System.out.println("4. Caesar Cipher Decrypt");
            System.out.println("5. Word Frequency Analysis");
            System.out.println("6. String Statistics");
            System.out.println("7. Exit");
            System.out.print("Choice: ");
            int ch = sc.nextInt(); sc.nextLine();

            if (ch == 7) { running = false; break; }
            System.out.print("Enter string: ");
            String input = sc.nextLine();

            switch (ch) {
                case 1: System.out.println("Encoded: " + encode(input));   break;
                case 2: System.out.println("Decoded: " + decode(input));   break;
                case 3:
                    System.out.print("Shift (1-25): "); int s = sc.nextInt(); sc.nextLine();
                    System.out.println("Encrypted: " + caesarEncrypt(input, s)); break;
                case 4:
                    System.out.print("Shift (1-25): "); int s2 = sc.nextInt(); sc.nextLine();
                    System.out.println("Decrypted: " + caesarDecrypt(input, s2)); break;
                case 5: wordFrequency(input);  break;
                case 6: stringStats(input);    break;
                default: System.out.println("Invalid.");
            }
        }
        sc.close();
    }
}
