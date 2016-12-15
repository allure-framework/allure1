package ru.yandex.qatools.allure.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static ru.yandex.qatools.allure.data.utils.TextUtils.humanize;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 17.12.13
 */
@RunWith(Parameterized.class)
public class TextUtilsHumanizeTest {

    private String text;

    private String expected;

    public TextUtilsHumanizeTest(String text, String expected) {
        this.text = text;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[]{"", ""},
                new Object[]{"empty-string", "Empty-string"},
                new Object[]{"XMLParser", "XML parser"},
                new Object[]{"MyClass", "My class"},
                new Object[]{"13Friends", "13 friends"},
                new Object[]{"AreYouNowThat2Is2", "Are you now that 2 is 2"},
                new Object[]{"GL11", "GL 11"},
                new Object[]{"May5", "May 5"},
                new Object[]{"small_case_worlds", "Small case worlds"},
                new Object[]{"Another_Case_Worlds", "Another case worlds"},
                new Object[]{"1I2YOU", "1 I 2 YOU"},
                new Object[]{"1I2You", "1 I 2 you"},
                new Object[]{"my.company.MyTest", "My test"},
                new Object[]{".my.", "My."},
                new Object[]{"another...", "Another..."},
                new Object[]{"my.company.another...", "Another..."},
                new Object[]{"my.company.params[1.2, 1.3]", "Params [1.2, 1.3]"},
                new Object[]{"params[1.2, 1.3]", "Params [1.2, 1.3]"},
                new Object[]{"my.company.params[1.2, [1.3, 1.4]]", "Params [1.2, [1.3, 1.4]]"},
                new Object[]{"params[1.2, [1.3, 1.4]]", "Params [1.2, [1.3, 1.4]]"},
                new Object[]{"e1.e2", "E 2"},
                new Object[]{"withParams[1]", "With params [1]"}
        );
    }

    @Test
    public void testHumanizedTextEqualsExpected() throws Exception {
        assertEquals(expected, humanize(text));
    }
}
