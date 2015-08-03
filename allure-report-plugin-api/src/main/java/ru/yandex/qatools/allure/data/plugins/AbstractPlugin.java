package ru.yandex.qatools.allure.data.plugins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Base class for all plugins with resources.
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 17.04.15
 * @see ProcessPlugin
 */
public abstract class AbstractPlugin implements WithResources, WithData, WithPriority {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPlugin.class);

    private String name = getClass().isAnnotationPresent(Plugin.Name.class) ?
            getClass().getAnnotation(Plugin.Name.class).value() : null;

    private int priority = getClass().isAnnotationPresent(Plugin.Priority.class) ?
            getClass().getAnnotation(Plugin.Priority.class).value() : 0;

    /**
     * Get name of plugin. Name should be specified in {@link Plugin.Name}
     * annotation.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Get plugin priority. Plugins with higher priority will be processed firstly.
     */
    @Override
    public int getPriority() {
        return priority;
    }

    /**
     * This method creates a {@link PluginData} for each field with
     * {@link Plugin.Data} annotation.
     *
     * @see #getFileName(Field)
     * @see #getFieldValue(Field)
     */
    @Override
    public List<PluginData> getPluginData() {
        List<PluginData> results = new ArrayList<>();
        for (Field field : getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Plugin.Data.class)) {
                String fileName = getFileName(field);
                results.add(new PluginData(fileName, getFieldValue(field)));
            }
        }
        return results;
    }

    /**
     * Try to get field value. Field with {@link Plugin.Data} annotation should
     * be accessible.
     */
    private Object getFieldValue(Field field) {
        try {
            field.setAccessible(true);
            return field.get(this);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            LOGGER.error("Can't access to field value", e);
            return null;
        }
    }

    /**
     * Get file name for field with {@link Plugin.Data} annotation. If name
     * is default then use {@link #name} as file name and <code>json</code>
     * as file extension.
     *
     * @see #getName()
     */
    private String getFileName(Field field) {
        String fileName = field.getAnnotation(Plugin.Data.class).value();
        if ("##default".equals(fileName)) {
            fileName = getName() + ".json";
        }
        return fileName;
    }

    /**
     * Verify plugin class. Plugin with resources plugin should not be private, abstract or interface.
     * Also class should be annotated with {@link Plugin.Name}
     * annotation. Returns true if given class is valid plugin, false otherwise.
     * Plugin class should not be null.
     */
    public static boolean isValid(Class<? extends AbstractPlugin> pluginClass) {
        return pluginClass != null && pluginClass.isAnnotationPresent(Plugin.Name.class) &&
                checkModifiers(pluginClass) && checkFieldsWithDataAnnotation(pluginClass);
    }

    /**
     * Check given class modifiers. Plugin with resources plugin should not be private or abstract
     * or interface.
     */
    private static boolean checkModifiers(Class<? extends AbstractPlugin> pluginClass) {
        int modifiers = pluginClass.getModifiers();
        return !Modifier.isAbstract(modifiers) &&
                !Modifier.isInterface(modifiers) &&
                !Modifier.isPrivate(modifiers);
    }

    /**
     * Check fields with {@link Plugin.Data} annotation. Firstly filter all declared fields
     * and then check it using {@link #shouldHasUniqueValues(List)}
     */
    private static boolean checkFieldsWithDataAnnotation(Class<? extends AbstractPlugin> pluginClass) {
        List<Field> dataFields = new ArrayList<>();
        for (Field field : pluginClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Plugin.Data.class)) {
                dataFields.add(field);
            }
        }
        return shouldHasUniqueValues(dataFields);
    }

    /**
     * Check that each given field has unique value in {@link Plugin.Data} annotation.
     */
    private static boolean shouldHasUniqueValues(List<Field> dataFields) {
        Set<String> dataValues = new HashSet<>();
        for (Field field : dataFields) {
            String value = field.getAnnotation(Plugin.Data.class).value();
            if (dataValues.contains(value)) {
                return false;
            }
            dataValues.add(value);
        }
        return true;
    }
}
