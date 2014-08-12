package ru.yandex.qatools.allure.config;

import org.apache.commons.beanutils.Converter;
import ru.yandex.qatools.allure.model.Status;

import java.util.HashSet;
import java.util.Set;

/**
 * User: eoff (eoff@yandex-team.ru)
 * Date: 04.08.14
 */
public class AllureStatusFilterConverter implements Converter {
    private String delimeter;

    public AllureStatusFilterConverter() {
        this(",");
    }

    public AllureStatusFilterConverter(String delimeter) {
        this.delimeter = delimeter;
    }

    @Override
    public Object convert(Class aClass, Object o) {
        if (!(o instanceof String)) {
            return new HashSet<Status>();
        }

        String str = (String) o;
        Set<Status> set = new HashSet<>();
        for (String s : str.split(delimeter)) {
            set.add(Status.fromValue(s.trim()));
        }
        return set;
    }
}
