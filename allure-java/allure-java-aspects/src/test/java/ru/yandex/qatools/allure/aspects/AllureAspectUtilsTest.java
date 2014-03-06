package ru.yandex.qatools.allure.aspects;

import org.junit.Test;

import java.text.MessageFormat;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static ru.yandex.qatools.allure.aspects.AllureAspectUtils.getTitle;

/**
 * User: hrundelb
 * Date: 28.02.14
 * Time: 16:51
 */
public class AllureAspectUtilsTest {

    String namePattern = "{method} (first arg:{0}, second arg:{1})";
    String methodName = "getSomethingNew";
    String titleString = "{0} (first arg:{1}, second arg:{2})";

    @Test
    public void getTitleWithStringArray(){
        String[] firstArg = {"one string", "two string", "three string"};
        int secondArg = 2454575;
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        Object[] args = { methodName, Arrays.toString(firstArg), secondArg};
        assertThat("Название метода с массивом строк сформировано неверно", title,
                equalTo(MessageFormat.format(titleString, args)));
    }

    @Test
    public void getTitleWithLongArray(){
        long[] firstArg = {20000L, 464564L, 8798765465465465132L};
        Integer secondArg = 1546825;
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        Object[] args = { methodName, Arrays.toString(firstArg), secondArg};
        assertThat("Название метода с массивом длинных целых чисел сформировано неверно", title,
                equalTo(MessageFormat.format(titleString, args)));
    }

    @Test
    public void getTitleWithIntArray(){
        int[] firstArg = {1, 2, 3};
        Boolean secondArg = true;
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        Object[] args = { methodName, Arrays.toString(firstArg), secondArg};
        assertThat("Название метода с массивом целых чисел сформировано неверно", title,
                equalTo(MessageFormat.format(titleString, args)));
    }

    @Test
    public void getTitleWithShortArray(){
        short[] firstArg = {32767, 0, -32768};
        String secondArg = "aaabbbbccdddd";
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        Object[] args = { methodName, Arrays.toString(firstArg), secondArg};
        assertThat("Название метода с массивом коротких целых чисел сформировано неверно", title,
                equalTo(MessageFormat.format(titleString, args)));
    }

    @Test
    public void getTitleWithCharArray(){
        char[] firstArg = {'a', 'b', 'z'};
        double secondArg = 25.546548946;
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        Object[] args = { methodName, Arrays.toString(firstArg), secondArg};
        assertThat("Название метода с массивом символов сформировано неверно", title,
                equalTo(MessageFormat.format(titleString, args)));
    }

    @Test
    public void getTitleWithByteArray(){
        byte[] firstArg = {1, 127, -128};
        char secondArg = 'x';
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        Object[] args = { methodName, Arrays.toString(firstArg), secondArg};
        assertThat("Название метода с массивом байтов сформировано неверно", title,
                equalTo(MessageFormat.format(titleString, args)));
    }

    @Test
    public void getTitleWithBooleanArray(){
        boolean[] firstArg = {true, false, false};
        float secondArg = 0.00005F;
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        Object[] args = { methodName, Arrays.toString(firstArg), secondArg};
        assertThat("Название метода с массивом логических переменных сформировано неверно", title,
                equalTo(MessageFormat.format(titleString, args)));
    }

    @Test
    public void getTitleWithFloatArray(){
        float[] firstArg = {0.1F, 1.002F, 6.45F};
        byte secondArg = 127;
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        Object[] args = { methodName, Arrays.toString(firstArg), secondArg};
        assertThat("Название метода с массивом чисел с плавающей запятой сформировано неверно", title,
                equalTo(MessageFormat.format(titleString, args)));
    }

    @Test
    public void getTitleWithDoubleArray(){
        double[] firstArg = {4.0000006, 0.4, 6.0};
        short secondArg = -5462;
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        Object[] args = { methodName, Arrays.toString(firstArg), secondArg};
        assertThat("Название метода с массивом чисел с плавающей запятой двойной точности сформировано неверно", title,
                equalTo(MessageFormat.format(titleString, args)));
    }

    @Test
    public void getTitleWithoutArray() {
        double firstArg = 0.00000001;
        String secondArg = "second arg";
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        Object[] args = { methodName, firstArg, secondArg};
        assertThat("Название метода без массивов сформировано неверно", title,
                equalTo(MessageFormat.format("{0} (first arg:{1}, second arg:{2})", args)));
    }

    @Test
    public void getTitleWithEmptyArray() {
        String[] firstArg = new String[0];
        long secondArg = 1000000000000L;
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        Object[] args = { methodName, Arrays.toString(firstArg), secondArg};
        assertThat("Название метода с пустым массивом сформировано неверно", title,
                equalTo(MessageFormat.format(titleString, args)));
    }
}
