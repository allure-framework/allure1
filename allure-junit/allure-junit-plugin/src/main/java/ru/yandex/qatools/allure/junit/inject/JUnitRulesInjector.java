package ru.yandex.qatools.allure.junit.inject;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.Theory;
import org.objectweb.asm.tree.*;
import ru.yandex.qatools.allure.inject.Injector;
import ru.yandex.qatools.allure.junit.TestCaseReportRule;
import ru.yandex.qatools.allure.junit.TestSuiteReportRule;

import java.util.Iterator;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Type.getDescriptor;
import static org.objectweb.asm.Type.getInternalName;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 23.11.13
 */
@SuppressWarnings("unchecked")
public class JUnitRulesInjector extends Injector {

    public static final String TEST_SUITE_RULE_FIELD_NAME =
            "ccda659af08708ef640aba172c7a527d1d5bd2137456933c96a80e8b1ff3b797";
    public static final String TEST_CASE_RULE_FIELD_NAME =
            "c8eef17c59a7166a1b86e0d94e37610694c4d2c11faa8fce94df8f37865e750f";

    private static final String TEST_SUITE_RULE_DESC = getDescriptor(TestSuiteReportRule.class);

    private static final String TEST_CASE_RULE_DESC = getDescriptor(TestCaseReportRule.class);

    @Override
    public void inject(ClassNode cn) {
        cn.fields.add(getTestSuiteReportRuleNode());
        cn.fields.add(getTestCaseReportRuleNode());

        boolean staticConstructorPresent = false;

        for (MethodNode mn : (List<MethodNode>) cn.methods) {
            if ("<clinit>".equals(mn.name) && mn.instructions.size() > 0) {
                initTestSuiteReportRule(cn.name, mn);
                staticConstructorPresent = true;
            }
            if ("<init>".equals(mn.name) && mn.instructions.size() > 0) {
                initTestCaseReportRule(cn.name, mn);
            }
        }

        if (!staticConstructorPresent) {
            MethodNode mn = new MethodNode(ACC_STATIC, "<clinit>", "()V", null, null);
            mn.maxStack = 4;
            mn.maxLocals = 1;
            mn.instructions = new InsnList();
            mn.instructions.add(getTestSuiteRuleInitInstructions(cn.name));
            mn.instructions.add(new InsnNode(RETURN));
            cn.methods.add(mn);
        }
    }

    private static FieldNode getTestSuiteReportRuleNode() {
        FieldNode testSuiteReportRuleNode = new FieldNode(
                ACC_PUBLIC + ACC_STATIC,
                TEST_SUITE_RULE_FIELD_NAME,
                TEST_SUITE_RULE_DESC,
                null,
                null
        );
        testSuiteReportRuleNode.visitAnnotation(getDescriptor(ClassRule.class), true);
        return testSuiteReportRuleNode;
    }

    private static FieldNode getTestCaseReportRuleNode() {
        FieldNode testCaseReportRuleNode = new FieldNode(
                ACC_PUBLIC,
                TEST_CASE_RULE_FIELD_NAME,
                TEST_CASE_RULE_DESC,
                null,
                null
        );
        testCaseReportRuleNode.visitAnnotation(getDescriptor(Rule.class), true);
        return testCaseReportRuleNode;
    }

    private static void addInitInstructions(MethodNode mn, InsnList initInstructions) {
        InsnList instructions = mn.instructions;
        Iterator<AbstractInsnNode> j = instructions.iterator();
        while (j.hasNext()) {
            AbstractInsnNode in = j.next();
            int op = in.getOpcode();
            if ((op >= IRETURN && op <= RETURN) || op == ATHROW) {
                instructions.insert(in.getPrevious(), initInstructions);
            }
        }
    }

    private static void initTestSuiteReportRule(String owner, MethodNode mn) {
        addInitInstructions(mn, getTestSuiteRuleInitInstructions(owner));
        mn.maxStack += 3;
        mn.maxLocals += 1;
    }

    private static void initTestCaseReportRule(String owner, MethodNode mn) {
        addInitInstructions(mn, getTestCaseRuleInitInstructions(owner));
        mn.maxStack += 3;
        mn.maxLocals += 2;
    }

    private static InsnList getTestSuiteRuleInitInstructions(String owner) {
        InsnList il = new InsnList();
        il.add(new TypeInsnNode(NEW, getInternalName(TestSuiteReportRule.class)));
        il.add(new InsnNode(DUP));
        il.add(new MethodInsnNode(INVOKESPECIAL, getInternalName(TestSuiteReportRule.class), "<init>", "()V"));
        il.add(new FieldInsnNode(PUTSTATIC, owner, TEST_SUITE_RULE_FIELD_NAME, TEST_SUITE_RULE_DESC));
        return il;
    }

    private static InsnList getTestCaseRuleInitInstructions(String owner) {
        InsnList il = new InsnList();
        il.add(new VarInsnNode(ALOAD, 0));
        il.add(new TypeInsnNode(NEW, getInternalName(TestCaseReportRule.class)));
        il.add(new InsnNode(DUP));
        il.add(new FieldInsnNode(GETSTATIC, owner, TEST_SUITE_RULE_FIELD_NAME, TEST_SUITE_RULE_DESC));
        il.add(new MethodInsnNode(INVOKESPECIAL, getInternalName(TestCaseReportRule.class), "<init>",
                String.format("(%s)V", TEST_SUITE_RULE_DESC)));
        il.add(new FieldInsnNode(PUTFIELD, owner, TEST_CASE_RULE_FIELD_NAME, TEST_CASE_RULE_DESC));
        return il;
    }

    @Override
    public boolean shouldInject(ClassNode cn) {
        return !isInterface(cn) && !isAbstract(cn) && isTestClass(cn) && thereIsNoAllureRules(cn);
    }

    private static boolean isTestClass(ClassNode cn) {
        for (MethodNode mn : (List<MethodNode>) cn.methods) {
            if (isPublic(mn) && (isMethodAnnotatedWith(mn, Test.class) || (isMethodAnnotatedWith(mn, Theory.class)))) {
                return true;
            }
        }
        return false;
    }

    private static boolean thereIsNoAllureRules(ClassNode cn) {
        for (FieldNode fieldNode : (List<FieldNode>) cn.fields) {
            if (isFieldAnnotatedWith(fieldNode, ClassRule.class) && fieldNode.desc.equals(TEST_SUITE_RULE_DESC)) {
                return false;
            }
            if (isFieldAnnotatedWith(fieldNode, Rule.class) && fieldNode.desc.equals(TEST_CASE_RULE_DESC)) {
                return false;
            }
        }
        return true;
    }
}
