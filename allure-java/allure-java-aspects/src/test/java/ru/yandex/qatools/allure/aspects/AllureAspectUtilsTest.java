package ru.yandex.qatools.allure.aspects;

import org.junit.Test;

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

    @Test
    public void getTitleWithStringArray(){
        String[] firstArg = {"one string", "two string", "three string"};
        int secondArg = 2454575;
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        assertThat("Название метода с массивом строк сформировано неверно", title,
                equalTo("getSomethingNew (first arg:[one string, two string, three string], second arg:2 454 575)"));
    }

    @Test
    public void getTitleWithLongArray(){
        long[] firstArg = {20000L, 464564L, 8798765465465465132L};
        Integer secondArg = 1546825;
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        assertThat("Название метода с массивом длинных целых чисел сформировано неверно", title,
                equalTo("getSomethingNew (first arg:[20000, 464564, 8798765465465465132], second arg:1 546 825)"));
    }

    @Test
    public void getTitleWithIntArray(){
        int[] firstArg = {1, 2, 3};
        Boolean secondArg = true;
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        assertThat("Название метода с массивом целых чисел сформировано неверно", title,
                equalTo("getSomethingNew (first arg:[1, 2, 3], second arg:true)"));
    }

    @Test
    public void getTitleWithShortArray(){
        short[] firstArg = {32767, 0, -32768};
        String secondArg = "aaabbbbccdddd";
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        assertThat("Название метода с массивом коротких целых чисел сформировано неверно", title,
                equalTo("getSomethingNew (first arg:[32767, 0, -32768], second arg:aaabbbbccdddd)"));
    }

    @Test
    public void getTitleWithCharArray(){
        char[] firstArg = {'a', 'b', 'z'};
        double secondArg = 25.546548946;
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        assertThat("Название метода с массивом символов сформировано неверно", title,
                equalTo("getSomethingNew (first arg:[a, b, z], second arg:25,547)"));
    }

    @Test
    public void getTitleWithByteArray(){
        byte[] firstArg = {1, 127, -128};
        char secondArg = 'x';
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        assertThat("Название метода с массивом байтов сформировано неверно", title,
                equalTo("getSomethingNew (first arg:[1, 127, -128], second arg:x)"));
    }

    @Test
    public void getTitleWithBooleanArray(){
        boolean[] firstArg = {true, false, false};
        float secondArg = 0.00005F;
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        assertThat("Название метода с массивом логических переменных сформировано неверно", title,
                equalTo("getSomethingNew (first arg:[true, false, false], second arg:0)"));
    }

    @Test
    public void getTitleWithFloatArray(){
        float[] firstArg = {0.1F, 1.002F, 6.45F};
        byte secondArg = 127;
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        assertThat("Название метода с массивом чисел с плавающей запятой сформировано неверно", title,
                equalTo("getSomethingNew (first arg:[0.1, 1.002, 6.45], second arg:127)"));
    }

    @Test
    public void getTitleWithDoubleArray(){
        double[] firstArg = {4.0000006, 0.4, 6.0};
        short secondArg = -5462;
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        assertThat("Название метода с массивом чисел с плавающей запятой двойной точности сформировано неверно", title,
                equalTo("getSomethingNew (first arg:[4.0000006, 0.4, 6.0], second arg:-5 462)"));
    }

    @Test
    public void getTitleWithoutArray() {
        double firstArg = 0.00000001;
        String secondArg = "second arg";
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        assertThat("Название метода без массивов сформировано неверно", title,
                equalTo("getSomethingNew (first arg:0, second arg:second arg)"));
    }

    @Test
    public void getTitleWithEmptyArray() {
        String[] firstArg = new String[0];
        long secondArg = 1000000000000L;
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        assertThat("Название метода с пустым массивом сформировано неверно", title,
                equalTo("getSomethingNew (first arg:[], second arg:1 000 000 000 000)"));
    }
}
