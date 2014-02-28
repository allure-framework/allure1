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
                equalTo(String.format("%s (first arg:%s, second arg:%s)",
                        methodName, Arrays.toString(firstArg), "2" + (char)160 + "454" + (char)160 + "575")
                ));
    }

    @Test
    public void getTitleWithLongArray(){
        long[] firstArg = {20000L, 464564L, 8798765465465465132L};
        Integer secondArg = 1546825;
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        assertThat("Название метода с массивом длинных целых чисел сформировано неверно", title,
                equalTo(String.format("%s (first arg:%s, second arg:%s)",
                        methodName, Arrays.toString(firstArg), "1" + (char)160 + "546" + (char)160 + "825")
                ));
    }

    @Test
    public void getTitleWithIntArray(){
        int[] firstArg = {465,464,6,456,465,461,231,3,24,5641,321,32,135,46,46,51,321};
        Boolean secondArg = true;
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        assertThat("Название метода с массивом целых чисел сформировано неверно", title,
                equalTo(String.format("%s (first arg:%s, second arg:%s)",
                        methodName, Arrays.toString(firstArg), secondArg)
                ));
    }

    @Test
    public void getTitleWithShortArray(){
        short[] firstArg = {54, 856, -856};
        String secondArg = "aaabbbbccdddd";
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        assertThat("Название метода с массивом коротких целых чисел сформировано неверно", title,
                equalTo(String.format("%s (first arg:%s, second arg:%s)",
                        methodName, Arrays.toString(firstArg), secondArg)
                ));
    }

    @Test
    public void getTitleWithCharArray(){
        char[] firstArg = {'a', 'b', 'z'};
        double secondArg = 25.546548946;
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        assertThat("Название метода с массивом символов сформировано неверно", title,
                equalTo(String.format("%s (first arg:%s, second arg:%s)",
                        methodName, Arrays.toString(firstArg), "25,547")
                ));
    }

    @Test
    public void getTitleWithByteArray(){
        byte[] firstArg = {25, 60, -128};
        char secondArg = 'x';
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        assertThat("Название метода с массивом байтов сформировано неверно", title,
                equalTo(String.format("%s (first arg:%s, second arg:%s)",
                        methodName, Arrays.toString(firstArg), secondArg)
                ));
    }

    @Test
    public void getTitleWithBooleanArray(){
        boolean[] firstArg = {true, false, false, false, true, false, true, true};
        float secondArg = 0.00005F;
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        assertThat("Название метода с массивом логических переменных сформировано неверно", title,
                equalTo(String.format("%s (first arg:%s, second arg:%s)",
                        methodName, Arrays.toString(firstArg), "0")
                ));
    }

    @Test
    public void getTitleWithFloatArray(){
        float[] firstArg = {0.1215F, 8754.002F, 896.45F};
        byte secondArg = 127;
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        assertThat("Название метода с массивом чисел с плавающей запятой сформировано неверно", title,
                equalTo(String.format("%s (first arg:%s, second arg:%s)",
                        methodName, Arrays.toString(firstArg), secondArg)
                ));
    }

    @Test
    public void getTitleWithDoubleArray(){
        double[] firstArg = {456454.545646,4.564,6.4,6.54,56.4,6.54,65.4};
        short secondArg = -5462;
        String title = getTitle(namePattern, methodName, new Object[]{firstArg, secondArg});
        assertThat("Название метода с массивом чисел с плавающей запятой двойной точности сформировано неверно", title,
                equalTo(String.format("%s (first arg:%s, second arg:%s)",
                        methodName, Arrays.toString(firstArg),"-5" + (char)160 + "462")
                ));
    }
}
