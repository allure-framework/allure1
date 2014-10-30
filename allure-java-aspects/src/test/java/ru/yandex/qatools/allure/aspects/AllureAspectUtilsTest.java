package ru.yandex.qatools.allure.aspects;

import org.junit.Test;
import ru.yandex.qatools.allure.config.AllureConfig;

import java.text.MessageFormat;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.yandex.qatools.allure.aspects.AllureAspectUtils.cutEnd;
import static ru.yandex.qatools.allure.aspects.AllureAspectUtils.getName;
import static ru.yandex.qatools.allure.aspects.AllureAspectUtils.getTitle;

/**
 * User: hrundelb
 * Date: 28.02.14
 * Time: 16:51
 */
public class AllureAspectUtilsTest {

    private final static String NAME_PATTERN_WITH_TWO_ARGS = "{method} (first arg:{0}, second arg:{1})";

    private final static String NAME_PATTERN_WITH_ONE_ARG = "{method} (arg:{0})";

    private final static String NAME_PATTERN_WITH_THIS = "{method} this:{this} ()";

    private final static String METHOD_NAME = "getSomethingNew";

    private final static String TITLE_STRING_WITH_TWO_ARGS = "{0} (first arg:{1}, second arg:{2})";

    private final static String TITLE_STRING_WITH_ONE_ARG = "{0} (arg:{1})";

    private final static String TITLE_STRING_WITH_THIS = "{0} this:{1} ()";

    public static final String TOO_LONG_NAME = "this name pattern is too long, over 150 symbols! Guys, what are" +
            " you thinking for when you made so long title??? Could you please made it more carefully...?";

    @Test
    public void getTitleWithStringArray() {
        String[] firstArg = {"one string", "two string", "three string"};
        int secondArg = 2454575;
        String title = getTitle(NAME_PATTERN_WITH_TWO_ARGS, METHOD_NAME, null, new Object[]{firstArg, secondArg});
        Object[] args = {METHOD_NAME, Arrays.toString(firstArg), secondArg};
        assertThat("Method with String[] and int arguments is processed incorrectly", title,
                equalTo(MessageFormat.format(TITLE_STRING_WITH_TWO_ARGS, args)));
    }

    @Test
    public void getTitleWithLongArray() {
        long[] firstArg = {20000L, 464564L, 8798765465465465132L};
        Integer secondArg = 1546825;
        String title = getTitle(NAME_PATTERN_WITH_TWO_ARGS, METHOD_NAME, null, new Object[]{firstArg, secondArg});
        Object[] args = {METHOD_NAME, Arrays.toString(firstArg), secondArg};
        assertThat("Method with long[] and Integer arguments is processed incorrectly", title,
                equalTo(MessageFormat.format(TITLE_STRING_WITH_TWO_ARGS, args)));
    }

    @Test
    public void getTitleWithIntArray() {
        int[] firstArg = {1, 2, 3};
        String title = getTitle(NAME_PATTERN_WITH_TWO_ARGS, METHOD_NAME, null, new Object[]{firstArg, true});
        Object[] args = {METHOD_NAME, Arrays.toString(firstArg), true};
        assertThat("Method with int[] and String arguments is processed incorrectly", title,
                equalTo(MessageFormat.format(TITLE_STRING_WITH_TWO_ARGS, args)));
    }

    @Test
    public void getTitleWithShortArray() {
        short[] firstArg = {32767, 0, -32768};
        String secondArg = "aaabbbbccdddd";
        String title = getTitle(NAME_PATTERN_WITH_TWO_ARGS, METHOD_NAME, null, new Object[]{firstArg, secondArg});
        Object[] args = {METHOD_NAME, Arrays.toString(firstArg), secondArg};
        assertThat("Method with short[] and String arguments is processed incorrectly", title,
                equalTo(MessageFormat.format(TITLE_STRING_WITH_TWO_ARGS, args)));
    }

    @Test
    public void getTitleWithCharArray() {
        char[] firstArg = {'a', 'b', 'z'};
        double secondArg = 25.546548946;
        String title = getTitle(NAME_PATTERN_WITH_TWO_ARGS, METHOD_NAME, null, new Object[]{firstArg, secondArg});
        Object[] args = {METHOD_NAME, Arrays.toString(firstArg), secondArg};
        assertThat("Method with char[] and double arguments is processed incorrectly", title,
                equalTo(MessageFormat.format(TITLE_STRING_WITH_TWO_ARGS, args)));
    }

    @Test
    public void getTitleWithByteArray() {
        byte[] firstArg = {1, 127, -128};
        char secondArg = 'x';
        String title = getTitle(NAME_PATTERN_WITH_TWO_ARGS, METHOD_NAME, null, new Object[]{firstArg, secondArg});
        Object[] args = {METHOD_NAME, Arrays.toString(firstArg), secondArg};
        assertThat("Method with byte[] and char arguments is processed incorrectly", title,
                equalTo(MessageFormat.format(TITLE_STRING_WITH_TWO_ARGS, args)));
    }

    @Test
    public void getTitleWithBooleanArray() {
        boolean[] firstArg = {true, false, false};
        float secondArg = 0.00005F;
        String title = getTitle(NAME_PATTERN_WITH_TWO_ARGS, METHOD_NAME, null, new Object[]{firstArg, secondArg});
        Object[] args = {METHOD_NAME, Arrays.toString(firstArg), secondArg};
        assertThat("Method with boolean[] and float arguments is processed incorrectly", title,
                equalTo(MessageFormat.format(TITLE_STRING_WITH_TWO_ARGS, args)));
    }

    @Test
    public void getTitleWithFloatArray() {
        float[] firstArg = {0.1F, 1.002F, 6.45F};
        byte secondArg = 127;
        String title = getTitle(NAME_PATTERN_WITH_TWO_ARGS, METHOD_NAME, null, new Object[]{firstArg, secondArg});
        Object[] args = {METHOD_NAME, Arrays.toString(firstArg), secondArg};
        assertThat("Method with float[] and byte arguments is processed incorrectly", title,
                equalTo(MessageFormat.format(TITLE_STRING_WITH_TWO_ARGS, args)));
    }

    @Test
    public void getTitleWithDoubleArray() {
        double[] firstArg = {4.0000006, 0.4, 6.0};
        short secondArg = -5462;
        String title = getTitle(NAME_PATTERN_WITH_TWO_ARGS, METHOD_NAME, null, new Object[]{firstArg, secondArg});
        Object[] args = {METHOD_NAME, Arrays.toString(firstArg), secondArg};
        assertThat("Method with double[] and short arguments is processed incorrectly", title,
                equalTo(MessageFormat.format(TITLE_STRING_WITH_TWO_ARGS, args)));
    }

    @Test
    public void getTitleWithoutArray() {
        double firstArg = 0.00000001;
        String secondArg = "second arg";
        String title = getTitle(NAME_PATTERN_WITH_TWO_ARGS, METHOD_NAME, null, new Object[]{firstArg, secondArg});
        Object[] args = {METHOD_NAME, firstArg, secondArg};
        assertThat("Method with double and String arguments is processed incorrectly", title,
                equalTo(MessageFormat.format("{0} (first arg:{1}, second arg:{2})", args)));
    }

    @Test
    public void getTitleWithEmptyArray() {
        String[] firstArg = new String[0];
        long secondArg = 1000000000000L;
        String title = getTitle(NAME_PATTERN_WITH_TWO_ARGS, METHOD_NAME, null, new Object[]{firstArg, secondArg});
        Object[] args = {METHOD_NAME, Arrays.toString(firstArg), secondArg};
        assertThat("Method with empty String[] and long arguments is processed incorrectly", title,
                equalTo(MessageFormat.format(TITLE_STRING_WITH_TWO_ARGS, args)));
    }

    @Test
    public void getTitleWithoutParams() {
        String title = getTitle("{method}", METHOD_NAME, null, null);
        Object[] args = {METHOD_NAME};
        assertThat("Method without arguments is processed incorrectly", title,
                equalTo(MessageFormat.format("{0}", args)));
    }

    @Test
    public void getTitleWithTooLongTitle() {
        String title = getTitle(TOO_LONG_NAME, METHOD_NAME, null, null);
        assertThat("Wrong title shortcut", title,
                equalTo(cutEnd(TOO_LONG_NAME, AllureConfig.newInstance().getMaxTitleLength())));
    }

    @Test
    public void getTitleWithNullParam() {
        String title = getTitle(NAME_PATTERN_WITH_ONE_ARG, METHOD_NAME, null, new Object[]{null});
        Object[] args = {METHOD_NAME, null};
        assertThat("Method with null String argument is processed incorrectly", title,
                equalTo(MessageFormat.format(TITLE_STRING_WITH_ONE_ARG, args)));
    }

    @Test
    public void getTitleWithNullArgInArrayParam() {
        String[] firstArg = new String[]{null, "something"};
        String title = getTitle(NAME_PATTERN_WITH_ONE_ARG, METHOD_NAME, null, new Object[]{firstArg});
        Object[] args = {METHOD_NAME, Arrays.toString(firstArg)};
        assertThat("Method with String[] argument that has nulls is processed incorrectly", title,
                equalTo(MessageFormat.format(TITLE_STRING_WITH_ONE_ARG, args)));
    }

    @Test
    public void getTitleWithArrayOfArrays() {
        Object[] firstArg = new Object[]{new String[]{"a", "b"}, "something", null};
        String title = getTitle(NAME_PATTERN_WITH_ONE_ARG, METHOD_NAME, null, new Object[]{firstArg});
        Object[] args = {METHOD_NAME, Arrays.toString(firstArg)};
        assertThat("Method with array of arrays argument that has nulls is processed incorrectly", title,
                equalTo(MessageFormat.format(TITLE_STRING_WITH_ONE_ARG, args)));
    }

    @Test
    public void getTitleWithThis() {
        String thisObject = "something";
        String title = getTitle(NAME_PATTERN_WITH_THIS, METHOD_NAME, thisObject, new Object[]{});
        Object[] args = {METHOD_NAME, thisObject};
        assertThat("Method with {this} is processed incorrectly", title,
                equalTo(MessageFormat.format(TITLE_STRING_WITH_THIS, args)));
    }

    @Test
    public void getNameLongMethodNameTest() throws Exception {
        String name = getName(TOO_LONG_NAME, null);
        assertThat("Invalid method name short cut", name,
                equalTo("... 150 symbols! Guys, what are you thinking for when you made so long title??? Could" +
                        " you please made it more carefully...?"));
    }

    @Test
    public void getNameLongMethodNameAndParametersTest() throws Exception {
        String name = getName(METHOD_NAME, new Object[]{TOO_LONG_NAME});
        assertThat("Invalid method name short cut", name,
                equalTo("getSomethingNew[this name pattern is too long, over 150 symbols! Guys, what are you " +
                        "thinking for when you made so long t...]"));
    }
}
