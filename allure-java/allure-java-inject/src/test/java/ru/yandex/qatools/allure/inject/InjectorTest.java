package ru.yandex.qatools.allure.inject;

import org.junit.Rule;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import ru.yandex.qatools.allure.inject.testdata.AbstractClass;
import ru.yandex.qatools.allure.inject.testdata.SimpleClass;
import ru.yandex.qatools.allure.inject.testdata.SimpleInterface;
import ru.yandex.qatools.allure.inject.testdata.SomeAnnotation;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Type.getInternalName;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.12.13
 */
public class InjectorTest {

    @Test
    public void isInterfaceTest() throws Exception {
        ClassNode cn = toClassNode(SimpleInterface.class);
        assertTrue(Injector.isInterface(cn));
    }

    @Test
    public void isAbstractClassTest() throws Exception {
        ClassNode cn = toClassNode(AbstractClass.class);
        assertTrue(Injector.isAbstract(cn));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void isMethodAnnotatedWithTest() throws Exception {
        ClassNode cn = toClassNode(SimpleClass.class);

        for (MethodNode mn : (List<MethodNode>) cn.methods) {
            if ("someMethod".equals(mn.name)) {
                assertTrue(Injector.isMethodAnnotatedWith(mn, SomeAnnotation.class));
            }
        }

    }

    @SuppressWarnings("unchecked")
    @Test
    public void isFieldAnnotatedWithTest() throws Exception {
        ClassNode cn = toClassNode(SimpleClass.class);

        for (FieldNode fn : (List<FieldNode>) cn.fields) {
            if ("privateTestNameRule".equals(fn.name) || "publicTestNameRule".equals(fn.name)) {
                assertTrue(Injector.isFieldAnnotatedWith(fn, Rule.class));
            }
        }

    }

    @Test
    public void emptyInjectorTest() throws Exception {
        Class<?> origin = SimpleClass.class;
        Class<?> modify = Injector.defineClass(origin.getName(), new EmptyInjector().inject(origin));

        byte[] originBytes = getBytes(origin);
        byte[] modifyBytes = getBytes(modify);

        assertTrue("Empty injector should not change class.", Arrays.equals(originBytes, modifyBytes));
    }

    public byte[] getBytes(Class<?> clazz) throws IOException {
        ClassWriter classWriter = new ClassWriter(ASM4);
        toClassNode(clazz).accept(classWriter);
        return classWriter.toByteArray();
    }

    public ClassNode toClassNode(Class<?> clazz) throws IOException {
        ClassReader classReader = new ClassReader(getInternalName(clazz));
        ClassNode cn = new ClassNode(ASM4);
        classReader.accept(cn, 0);
        return cn;
    }
}
