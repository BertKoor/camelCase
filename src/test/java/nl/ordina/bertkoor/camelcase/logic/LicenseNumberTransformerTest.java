package nl.ordina.bertkoor.camelcase.logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LicenseNumberTransformerTest {

    @Test
    void testCharValue() {
        assertEquals(0, LicenseNumberTransformer.charValue('0'));
        assertEquals(9, LicenseNumberTransformer.charValue('9'));

        assertEquals(10, LicenseNumberTransformer.charValue('a'));
        assertEquals(10, LicenseNumberTransformer.charValue('A'));

        assertEquals(35, LicenseNumberTransformer.charValue('z'));
        assertEquals(35, LicenseNumberTransformer.charValue('Z'));

        assertEquals(-1, LicenseNumberTransformer.charValue(' '));
        assertEquals(-1, LicenseNumberTransformer.charValue('.'));
        assertEquals(-1, LicenseNumberTransformer.charValue('-'));
    }

    @Test
    void checkTransformations() {
        check("00000a", 10l); // smallest value
        check("0000a0", 360l); // shifting left...
        check("000a00", 12960l);
        check("0a0000", 16796160l);
        check("a00000", 604661760l);
        check("zzzzz9", 2176782309l); // largest value


        check("02Z60W", 5000000l); // nice round number
        check("02Z60Z", 5000003l); // wrap around ...
        check("02Z610", 5000004l);

        check("abc123", 623698779l); // this is fine
        check("abcdef", null); // only letters
        check("123456", null); // only digits

        check("a0000", null); // just five chars
        check("a00000", 604661760l); // six is fine
        check("a000000", null); // seven chars

        check(" 00-00-0a.", 10l); // delimiters get ignored
        check("a00_000", null); // illegal character
    }

    void check(String id, Long expected) {
        try {
            long result = LicenseNumberTransformer.transform(id);
            if (expected == null) {
                fail("InvalidLicenseIdException expected, but got " + result);
            } else {
                assertEquals(expected, result);
            }
        } catch (LicenseNumberTransformer.InvalidLicenseIdException ex) {
            if (expected != null) {
                fail ("Expected result " + expected + " but InvalidLicenseIdException was thrown");
            }
        }
    }
}