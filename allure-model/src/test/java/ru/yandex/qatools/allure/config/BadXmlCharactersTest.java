package ru.yandex.qatools.allure.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static ru.yandex.qatools.allure.config.AllureNamingUtils.isBadXmlCharacter;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 18.03.15
 */
@RunWith(Parameterized.class)
public class BadXmlCharactersTest {

    @Parameterized.Parameter(0)
    public char character;

    @Parameterized.Parameter(1)
    public boolean isBadCharacter;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[]{'\u0020', false},
                new Object[]{'\u0019', true},
                new Object[]{'\n', false},
                new Object[]{'\t', false},
                new Object[]{'\r', false},
                new Object[]{'а', false},
                new Object[]{'я', false},
                new Object[]{'А', false},
                new Object[]{'Я', false},
                new Object[]{'a', false},
                new Object[]{'z', false},
                new Object[]{'A', false},
                new Object[]{'Z', false},
                new Object[]{'!', false},
                new Object[]{'@', false},
                new Object[]{'\uFFFE', true},
                new Object[]{'\uFFFF', true}
        );
    }

    @Test
    public void shouldDetectBadCharacter() throws Exception {
        assertEquals(isBadXmlCharacter(character), isBadCharacter);
    }
}
