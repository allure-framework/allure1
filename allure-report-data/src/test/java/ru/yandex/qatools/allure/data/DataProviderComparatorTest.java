package ru.yandex.qatools.allure.data;

import org.junit.Before;
import org.junit.Test;
import ru.yandex.qatools.allure.data.providers.DataProvider;
import ru.yandex.qatools.allure.data.providers.DataProviderPhase;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.yandex.qatools.allure.data.AllureReportGenerator.DataProviderComparator;
import static ru.yandex.qatools.allure.data.providers.DataProviderPhase.*;

/**
 * @author Mihails Volkovs
 *         Date: 12/12/14
 */
public class DataProviderComparatorTest {

    private DataProviderComparator comparator;
    private List<DataProvider> providers;

    @Before
    public void setUp() {
        comparator = new DataProviderComparator();
        providers = newArrayList(create(DEFAULT), create(POST_PROCESS), create(PRE_PROCESS));
    }

    @Test
    public void compare() {
        Collections.sort(providers, comparator);

        Iterator<DataProvider> iterator = providers.iterator();
        assertEquals(PRE_PROCESS, iterator.next().getPhase());
        assertEquals(DEFAULT, iterator.next().getPhase());
        assertEquals(POST_PROCESS, iterator.next().getPhase());
    }

    private DataProvider create(DataProviderPhase phase) {
        DataProvider dataProvider = mock(DataProvider.class);
        when(dataProvider.getPhase()).thenReturn(phase);
        return dataProvider;
    }

}
