package ru.yandex.qatools.allure.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.qatools.allure.data.utils.UidGenerationUtils;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertFalse;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 09.12.13
 */
@RunWith(Parameterized.class)
public class UtilsTest {

    private String data;

    public UtilsTest(String data) {
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
        String result = UidGenerationUtils.generateUid(data);
        assertFalse("Uid should be not empty", result.isEmpty());
    }
}
