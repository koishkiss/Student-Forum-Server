package student.forum.util;

public class TextUtil {

    public static String truncatedText(int truncatedSize, String text) {
        if (text.length() >= truncatedSize) {
            return text.substring(0,truncatedSize-1) + "...";
        } else {
            return text;
        }
    }
}
