package be.utopia.library.validation;

import java.util.regex.Pattern;

public class Guard {

    private static final String REGEX_NICKNAME = "^([a-zA-Z0-9_.-]){2,30}$";
    private static final String REGEX_ONLYLETTERS = "^[a-zA-Z]+$";
    private static final String REGEX_URL = "^(https):\\/\\/[a-zA-Z0-9-_.]+\\.[a-zA-Z]{2,5}(\\/.*)?$";
    private static final String REGEX_TITLE = "^.{2,80}$";
    private static final String REGEX_TEXTBLOCK = "^.{2,500}$";


    /**
     * Checks if the provided parameter is equal to null
     * @param t
     * @param caseOfException
     * @return the provided parameter if it is not null
     */
    public static<T> T notNull (T t, String caseOfException){
        if (t == null){
            throw new IllegalArgumentException(caseOfException + " Exception during notNull Guard: " + t + " is equal to zero");
        }
        return t;
    }

    /**
     * Checks if the provided string is either null or empty
     * @param s
     * @param caseOfException
     * @return the provided string if it is not null or empty
     */
    public static String notBlank (String s, String caseOfException){
        if (Guard.notNull(s, caseOfException).isBlank())
            throw new IllegalArgumentException(caseOfException + " Exception during notBlank Guard: " + s + " is blank");
        return s;
    }

    /**
     * Checks if the lenght of the provided string is between a given min (not inclusive) and max bound (not inclusive)
     * @param s
     * @param min
     * @param max
     * @param caseOfException
     * @return the provided string if it is between the given bounds
     */
    public static String limitSize(String s, int min, int max, String caseOfException){
        String sNotNull = Guard.notNull(s, caseOfException);
        if (sNotNull.length() < min || sNotNull.length() > max)
            throw new IllegalArgumentException(caseOfException + " Exception during limitSize Guard: " + s + " is not between " + min + " and " + max);
        return s;
    }

    /**
     * Checks if the provided string contains only alphabetic characters
     * @param s
     * @param caseOfException
     * @return the provided string if it contains only alphabetic characters
     */
    public static String onlyLetters(String s, String caseOfException){
        if(!Pattern.matches(REGEX_ONLYLETTERS, Guard.notNull(s, caseOfException)))
            throw new IllegalArgumentException(caseOfException + " Exception during onlyLetters Guard: " + s + " contains non alphabetic characters");
        return s;
    }

    /**
     * Checks if the provided string is a valid nickname
     * @param s
     * @param caseOfException
     * @return the provided string if it is a valid nickname
     * @throws IllegalArgumentException
     */
    public static String validNickname(String s, String caseOfException){
        if(!Pattern.matches(REGEX_NICKNAME, Guard.notNull(s, caseOfException)))
            throw new IllegalArgumentException(caseOfException + " Exception during validNickname Guard: " + s + " is not a valid nickname");
        return s;

    }

    /**
     * Checks if the provided string is a valid URL
     * @param s
     * @param caseOfException
     * @return the provided string if it is a valid URL
     * @throws IllegalArgumentException
     */
    public static String validURL(String s, String caseOfException){
        if(!Pattern.matches(REGEX_URL, Guard.notNull(s, caseOfException)))
            throw new IllegalArgumentException(caseOfException + " Exception during validURL Guard: " + s + " is not a valid URL");
        return s;
    }

    /**
     * Checks if the provided string is a valid title
     * @param s
     * @param caseOfException
     * @return the provided string if it is a valid title
     * @throws IllegalArgumentException
     */

    public static String validTitle(String s, String caseOfException){
        if(!Pattern.matches(REGEX_TITLE, Guard.notNull(s, caseOfException)))
            throw new IllegalArgumentException(caseOfException + " Exception during validTitle Guard: " + s + " is not a valid title");
        return s;
    }

    /**
     * Checks if the provided string is a valid textblock
     * @param s
     * @param caseOfException
     * @return the provided string if it is a valid textblock
     * @throws IllegalArgumentException
     */

    public static String validTextBlock(String s, String caseOfException){
        if(!Pattern.matches(REGEX_TEXTBLOCK, Guard.notNull(s, caseOfException)))
            throw new IllegalArgumentException(caseOfException + " Exception during validTextBlock Guard: " + s + " is not a valid textblock");
        return s;
    }

    /**
     * Checks if the provided string is valid according to a custom regex
     * @param pattern
     * The regex to use for testing
     * @param s
     * The string to test against the custom regex
     * @param caseOfException
     * The case of exception to throw if the string is not valid
     * @return the provided string if it is valid according to the custom regex
     * @throws IllegalArgumentException
     */

    public static String validCustom(String pattern, String s, String caseOfException){
        if(!Pattern.matches(pattern, Guard.notNull(s, caseOfException)))
            throw new IllegalArgumentException(caseOfException + " Exception during validCustom Guard: " + s + " is not valid according to the custom regex");
        return s;
    }

    /**
     * Checks if the provided integer lies between a given min (not inclusive) and max (not inclusive)
     * @param i
     * @param min
     * @param max
     * @param caseOfException
     * @return the provided integer if it lies between the given bounds
     * @throws IllegalArgumentException
     */
    public static int between (int i, int min, int max, String caseOfException){
        if ((i < min || i > max))
            throw new IllegalArgumentException(caseOfException + " Exception during between Guard: " + i + " is not between min " + min + " and max " + max);
        return i;
    }
}
