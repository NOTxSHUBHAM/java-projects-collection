import java.util.*;
import java.util.regex.*;

public class WordFrequencyNLP {

    // ─── Word Cloud Data ───
    public static Map<String, Integer> getWordFrequency(String text) {
        String[] words = text.toLowerCase().split("\\s+");
        Set<String> stopWords = new HashSet<>(Arrays.asList(
            "the","is","a","an","and","or","but","in","on","at","to","for","of","with","it","this","that","was","are","be","has","had","have"
        ));
        Map<String, Integer> freq = new LinkedHashMap<>();
        for (String w : words) {
            w = w.replaceAll("[^a-z]", "");
            if (!w.isEmpty() && !stopWords.contains(w))
                freq.put(w, freq.getOrDefault(w, 0) + 1);
        }
        return freq;
    }

    public static void printWordCloud(Map<String, Integer> freq) {
        System.out.println("\n===== Word Cloud (sorted by frequency) =====");
        freq.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(20)
            .forEach(e -> {
                String bar = "█".repeat(e.getValue());
                System.out.printf("%-15s %2d | %s%n", e.getKey(), e.getValue(), bar);
            });
    }

    // ─── NLP: Named Entity Recognition (simple rule-based) ───
    public static void extractNamedEntities(String text) {
        System.out.println("\n===== Named Entity Recognition =====");

        // Persons: capitalized words (heuristic)
        List<String> persons = new ArrayList<>();
        Pattern personPattern = Pattern.compile("\\b([A-Z][a-z]+ [A-Z][a-z]+)\\b");
        Matcher m = personPattern.matcher(text);
        while (m.find()) persons.add(m.group());

        // Dates
        List<String> dates = new ArrayList<>();
        Pattern datePattern = Pattern.compile("\\b(\\d{1,2}[/-]\\d{1,2}[/-]\\d{2,4}|January|February|March|April|May|June|July|August|September|October|November|December)\\s*(\\d{4})?\\b");
        Matcher dm = datePattern.matcher(text);
        while (dm.find()) dates.add(dm.group().trim());

        // Organizations (words ending in Inc, Ltd, Corp)
        List<String> orgs = new ArrayList<>();
        Pattern orgPattern = Pattern.compile("\\b[A-Z][a-zA-Z]+ (?:Inc|Ltd|Corp|University|Institute|Hospital|School)\\b");
        Matcher om = orgPattern.matcher(text);
        while (om.find()) orgs.add(om.group());

        // Emails
        List<String> emails = new ArrayList<>();
        Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}");
        Matcher em = emailPattern.matcher(text);
        while (em.find()) emails.add(em.group());

        System.out.println("Persons        : " + (persons.isEmpty() ? "None found" : persons));
        System.out.println("Dates          : " + (dates.isEmpty()   ? "None found" : dates));
        System.out.println("Organizations  : " + (orgs.isEmpty()    ? "None found" : orgs));
        System.out.println("Emails         : " + (emails.isEmpty()  ? "None found" : emails));

        // Sentence count
        String[] sentences = text.split("[.!?]+");
        System.out.println("Total Sentences: " + sentences.length);
        System.out.println("Total Words    : " + text.split("\\s+").length);
        System.out.println("Total Chars    : " + text.length());
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("===== NLP & Word Frequency Tool =====");
        System.out.println("Enter or paste your text (type END on a new line when done):");

        StringBuilder sb = new StringBuilder();
        String line;
        while (!(line = sc.nextLine()).equals("END"))
            sb.append(line).append(" ");
        String text = sb.toString().trim();

        if (text.isEmpty()) { System.out.println("No text entered."); return; }

        Map<String, Integer> freq = getWordFrequency(text);
        printWordCloud(freq);
        extractNamedEntities(text);
        sc.close();
    }
}
