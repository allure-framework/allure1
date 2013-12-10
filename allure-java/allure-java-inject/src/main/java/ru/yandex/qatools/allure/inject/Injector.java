package ru.yandex.qatools.allure.inject;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Type.getDescriptor;
import static org.objectweb.asm.Type.getInternalName;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 23.11.13
 */
@SuppressWarnings({"unchecked", "unused"})
public abstract class Injector {

    public static Class<?> defineClass(final String name, final byte[] bytes, final ClassLoader loader) {
        return (Class<?>) AccessController.doPrivileged(new PrivilegedAction() {
            @Override
            public Class<?> run() {
                return new ClassLoaderWithPublicDefine(loader).defineClass(name, bytes);
            }
        });
    }

    public static Class<?> defineClass(String name, byte[] bytes) {
        return defineClass(name, bytes, Injector.class.getClassLoader());
    }

    public byte[] inject(Class clazz) throws IOException  {
        ClassReader classReader = new ClassReader(getInternalName(clazz));
        return inject(classReader);
    }

    public byte[] inject(InputStream inputStream) throws IOException {
        ClassReader classReader = new ClassReader(inputStream);
        return inject(classReader);
    }

    public byte[] inject(ClassReader cr) {
        ClassNode cn = new ClassNode(ASM4);
        cr.accept(cn, 0);

        if (shouldInject(cn)) {
            inject(cn);
        }

        ClassWriter classWriter = new ClassWriter(ASM4);
        cn.accept(classWriter);
        return classWriter.toByteArray();
    }

    public abstract void inject(ClassNode cn);

    public abstract boolean shouldInject(final ClassNode cn);

    protected static boolean isInterface(ClassNode cn) {
        return (cn.access & ACC_INTERFACE) != 0;
    }

    protected static boolean isAbstract(ClassNode cn) {
        return (cn.access & ACC_ABSTRACT) != 0;
    }

    protected static boolean isPublic(MethodNode mn) {
        return (mn.access & ACC_PUBLIC) != 0;
    }

    protected static boolean isAnnotatedWith(List annotationNodes, Class<?> clazz) {
        if (annotationNodes == null) {
            return false;
        }
        for (AnnotationNode an : (List<AnnotationNode>) annotationNodes) {
            if (an.desc.equals(getDescriptor(clazz))) {
                return true;
            }
        }

        return false;
    }

    protected static boolean isMethodAnnotatedWith(MethodNode mn, Class<?> clazz) {
        return isAnnotatedWith(mn.visibleAnnotations, clazz);
    }

    protected static boolean isFieldAnnotatedWith(FieldNode fn, Class<?> clazz) {
        return isAnnotatedWith(fn.visibleAnnotations, clazz);
    }

}
