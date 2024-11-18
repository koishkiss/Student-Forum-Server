package student.forum.util;

public class TextUtil {

    private static final String truncatedTail = "...";

    public static String truncatedWords(int wordsSize, String text) {
        if (text.length() >= wordsSize) {
            return text.substring(0,wordsSize-1) + truncatedTail;
        } else {
            return text;
        }
    }

    public static String truncatedLines(int linesSize, String text) {
        String[] lines = text.split("\n");
        if (lines.length >= linesSize) {
            StringBuilder newText = new StringBuilder();
            for (int i = 0; i < linesSize-1; i++) {
                newText.append(lines[i]).append('\n');
            }
            newText.append(truncatedTail);
            return newText.toString();
        } else {
            return text;
        }
    }

    public static String truncatedLinesAndWords(int linesSize, int wordsSize, String text) {
        String[] lines = text.split("\n");
        if (lines.length >= linesSize) {
            StringBuilder newText = new StringBuilder();
            for (int i = 0; i < linesSize-1; i++) {
                newText.append(lines[i]).append('\n');
            }
            newText.append(truncatedTail);
            return newText.toString();
        } else if (text.length() >= wordsSize) {
            return text.substring(0,wordsSize-1) + truncatedTail;
        } else {
            return text;
        }
    }
}
