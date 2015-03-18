package ru.yandex.qatools.allure.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static ru.yandex.qatools.allure.config.AllureNamingUtils.replaceBadXmlCharactersBySpace;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 18.03.15
 */
@RunWith(Parameterized.class)
public class ReplaceBadXmlCharactersBySpaceTest {

    @Parameterized.Parameter(0)
    public String string;

    @Parameterized.Parameter(1)
    public int off;

    @Parameterized.Parameter(2)
    public int len;

    @Parameterized.Parameter(3)
    public String expected;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[]{"asdzxcADSZXC", 0, 12, "asdzxcADSZXC"},
                new Object[]{"апрячсАПРЯЧС", 0, 12, "апрячсАПРЯЧС"},
                new Object[]{"hi\nall", 0, 6, "hi\nall"},
                new Object[]{"hi\u0019all", 0, 6, "hi all"},
                new Object[]{"hi all\u0019", 0, 6, "hi all\u0019"},
                new Object[]{"hi\u0019 all\u0019", 5, 2, "hi\u0019 all\u0019"},
                new Object[]{"hi\u0019 all\u0019", 5, 3, "hi\u0019 all "}
        );
    }

    @Test
    public void shouldDetectBadCharacter() throws Exception {
        char[] cbuf = string.toCharArray();
        replaceBadXmlCharactersBySpace(cbuf, off, len);

        String actual = String.copyValueOf(cbuf);

        assertEquals(expected, actual);
    }
}
