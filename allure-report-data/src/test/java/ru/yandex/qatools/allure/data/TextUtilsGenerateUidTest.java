package ru.yandex.qatools.allure.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.qatools.allure.data.utils.TextUtils;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertFalse;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 09.12.13
 */
@RunWith(Parameterized.class)
public class TextUtilsGenerateUidTest {

    private String data;

    public TextUtilsGenerateUidTest(String data) {
        this.data = data;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[]{"some-string"},
                new Object[]{"next-string-is-empty"},
                new Object[]{""},
                new Object[]{"123asdasd123ASDadkllmlk(*&(*&*#(&*($@#$$@#"},
                new Object[]{"-="}
        );
    }

    @Test
    public void generateUid() throws Exception {
        String result = TextUtils.generateUid(data);
        assertFalse("Uid should be not empty", result.isEmpty());
    }
}
