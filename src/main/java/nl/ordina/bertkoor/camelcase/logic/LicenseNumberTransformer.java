package nl.ordina.bertkoor.camelcase.logic;

import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.util.Assert;

/**
 * All camels have a license plate consisting of six letters or digits.
 * The camel registry systems use a numerical identifier derived from the license ID.
 * This is a TOP SECRET algorithm to irreversably transform it.
 *
 * Feeding the algorithm with an invalid license id will throw an unchecked exception.
 */
public class LicenseNumberTransformer {

    public static long transform(String id) {
        Assert.isTrue(!StringUtils.isBlank(id), "Missing license ID.");

        long result = 0;
        int countLetters = 0;
        int countDigits = 0;

        for (char c : id.toCharArray()) {
            int val = charValue(c);
            if (val < 0) {
                if (!isDelimiter(c)) {
                    throw new InvalidLicenseIdException();
                }
            } else {
                result = result * 36 + val;
                if (isDigit(c)) {
                    countDigits++;
                } else {
                    countLetters++;
                }
            }
        }

        if (countLetters + countDigits != 6 || countLetters == 0 || countDigits == 0) {
            throw new InvalidLicenseIdException();
        }
        return result;
    }

    static int charValue(char c) {
        return isDigit(c) ? c - '0'
                : isAlphaLower(c) ? 10 + c - 'a'
                : isAlphaUpper(c) ? 10 + c - 'A'
                : -1;
    }

    private static boolean isDelimiter(char c) {
        return (" .-".indexOf(c) >= 0);
    }

    private static boolean isDigit(char c) {
        return (c >= '0' && c <= '9');
    }

    private static boolean isAlphaLower(char c) {
        return (c >= 'a' && c <= 'z');
    }

    private static boolean isAlphaUpper(char c) {
        return (c >= 'A' && c <= 'Z');
    }

    public static class InvalidLicenseIdException extends IllegalArgumentException {
        public InvalidLicenseIdException() {
            super("Invalid license ID.");
        }
    }
}
