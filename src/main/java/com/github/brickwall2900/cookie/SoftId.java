package com.github.brickwall2900.cookie;

import com.github.brickwall2900.cookie.items.Item;
import com.github.brickwall2900.cookie.items.ItemDescription;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

public class SoftId {
    private static final SoftId id = new SoftId();

    static {
        if (!StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass().isAssignableFrom(SaveFile.class)) {
            throw new UnsupportedOperationException();
        }
    }

    private SoftId() {
        if (!StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass().isAssignableFrom(SoftId.class)) {
            throw new UnsupportedOperationException();
        }
    }

    public static final SoftId getId() {
        if (!StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass().isAssignableFrom(SaveFile.class)) {
            throw new UnsupportedOperationException();
        }
        return id;
    }

    public final long getSoftId() {
        if (!StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass().isAssignableFrom(SaveFile.class)) {
            throw new UnsupportedOperationException();
        }
        long id;
        try {
            id = getMethodClId(getMethodName(SoftId.class, "getSoftId"));
            id = 37 * id + getMethodClId(getMethodName(SoftId.class, "getMethodName"));
            id = 37 * id + getMethodClId(getMethodName(SoftId.class, "getMethodClId"));
            id = 37 * id + getMethodClId(getMethodName(SoftId.class, "getClId"));
            id = 37 * id + getMethodClId(getMethodName(SoftId.class, "getLoaderClId"));
            id = 37 * id + getMethodClId(getMethodName(SaveFile.class, "loadStage1"));
            id = 37 * id + getMethodClId(getMethodName(SaveFile.class, "loadStage2"));
            id = 37 * id + getMethodClId(getMethodName(SaveFile.class, "loadStage3"));
            id = 37 * id + getMethodClId(getMethodName(SaveFile.class, "loadStage4"));
            id = 37 * id + getMethodClId(getMethodName(SaveFile.class, "saveStage1"));
            id = 37 * id + getMethodClId(getMethodName(SaveFile.class, "saveStage2"));
            id = 37 * id + getMethodClId(getMethodName(SaveFile.class, "saveStage3"));
            id = 37 * id + getMethodClId(getMethodName(SaveFile.class, "saveStage4"));
            id = 37 * id + getMethodClId(getMethodName(SaveFile.class, "getNameStage1"));
            id = 37 * id + getMethodClId(getMethodName(SaveFile.class, "getNameStage2"));
            id = 37 * id + getMethodClId(getMethodName(SaveFile.class, "loadSave"));
            id = 37 * id + getMethodClId(getMethodName(SaveFile.class, "save"));
            id = 37 * id + getMethodClId(getMethodName(SaveFile.class, "encrypt"));
            id = 37 * id + getMethodClId(getMethodName(SaveFile.class, "decrypt"));
            id = 37 * id + getMethodClId(getMethodName(SaveFile.class, "generateBIv"));
            id = 37 * id + getMethodClId(getMethodName(SaveFile.class, "deleteSave"));
            id = 37 * id + getMethodClId(getMethodName(SaveFile.class, "getFileOnName"));
            id = 37 * id + getMethodClId(getMethodName(SaveFile.class, "panicOnLoad"));
            id = 37 * id + getMethodClId(getMethodName(SaveFile.class, "getNameOnFile"));
            id = 37 * id + getMethodClId(getMethodName(SaveFile.class, "listSaves"));
            id = 37 * id + getMethodClId(getMethodName(SaveFile.class, "doesSaveExist"));
            id = 37 * id + getMethodClId(getMethodName(SaveFile.class, "bytesToHex"));
            id = 37 * id + getMethodClId(getMethodName(SaveFile.class, "longToBytes"));
            id = 37 * id + getMethodClId(getMethodName(SaveFile.class, "bytesToChars"));
            id = 37 * id + getMethodClId(getMethodName(SaveFile.class, "decrypt"));
            id = 37 * id + getFieldClId(getFieldName(SaveFile.class, "SAVE_FILE_DIRECTORY"));
            id = 37 * id + getClId(SaveFile.class, 0);
            id = 37 * id + getClId(SoftId.class, 0);
            id = 37 * id + getClId(Game.class, 0);
            id = 37 * id + getClId(SoundSystem.class, 0);
            id = 37 * id + getClId(FileSelectController.class, 0);
            id = 37 * id + getClId(FileSelectWindow.class, 0);
            id = 37 * id + getClId(GameWindow.class, 0);
            id = 37 * id + getClId(ShopWindow.class, 0);
            id = 37 * id + getClId(ShopController.class, 0);
            id = 37 * id + getClId(ClassResource.class, 0);
            id = 37 * id + getClId(Item.class, 0);
            id = 37 * id + getClId(ItemDescription.class, 0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    private Method getMethodName(Class<?> cls, String name) throws Throwable {
        if (!StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass().isAssignableFrom(SoftId.class)) {
            throw new UnsupportedOperationException();
        }
        try {
            return findMethod(cls, name);
        } catch (NoSuchMethodException e) {
            try {
                return findMethod(cls.getSuperclass(), name);
            } catch (NoSuchMethodException exc) {
                exc.initCause(e);
                throw new RuntimeException(exc);
            }
        }
    }

    private Field getFieldName(Class<?> cls, String name) throws Throwable {
        if (!StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass().isAssignableFrom(SoftId.class)) {
            throw new UnsupportedOperationException();
        }
        try {
            return cls.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            try {
                return cls.getField(name);
            } catch (NoSuchFieldException e1) {
                try {
                    return cls.getSuperclass().getDeclaredField(name);
                } catch (NoSuchFieldException e2) {
                    return cls.getSuperclass().getField(name);
                }
            }
        }
    }

    private Method findMethod(Class<?> cls, String name) throws Throwable {
        if (!StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass().isAssignableFrom(SoftId.class)) {
            throw new UnsupportedOperationException();
        }
        Method[] methods = cls.getMethods();
        Method[] declaredMethods = cls.getDeclaredMethods();
        for (Method m : declaredMethods) {
            if (m.getName().equals(name)) return m;
        }
        for (Method m : methods) {
            if (m.getName().equals(name)) return m;
        }
        throw new NoSuchMethodException(name);
    }

    private long getFieldClId(Field field) {
        if (!StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass().isAssignableFrom(SoftId.class)) {
            throw new UnsupportedOperationException();
        }
        return 34621854L << field.getModifiers() * field.getName().hashCode() + 0x93baa;
    }

    protected long getMethodClId(Method method) throws Throwable {
        if (!StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass().isAssignableFrom(SoftId.class)) {
            throw new UnsupportedOperationException();
        }
        int modifiers = method.getModifiers();
        int pc = method.getParameterCount();
        boolean def = method.isDefault();
        char[] name = method.getName().toCharArray();
        Class<?> rt = method.getReturnType();
        boolean varArgs = method.isVarArgs();
        Class<?>[] exc = method.getExceptionTypes();

        long id = 35689012;
        id *= pc + id;
        id = 37 * id + modifiers << 3;
        id = def ? id * id : id * id * id;
        for (char c : name) id *= 671 * id * id + c * 56;
        for (char c : (rt.getName() + rt.getPackageName()).toCharArray()) id *= 5765 - id * id + c >> 2;
        for (Class<?> c : exc) id *= 7212_23 + id * 2 | c.getConstructors().length - c.getName().hashCode();
        return varArgs ? id : 0b10100011 | id;
    }

    private long getClId(Class<?> cls, int i1) throws Throwable {
        if (!StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass().isAssignableFrom(SoftId.class)) {
            throw new UnsupportedOperationException();
        }
        if (i1 > 15) {
            return (long) cls.getName().hashCode() * 21341 * 21341 << 4;
        }
        int ms = cls.getModifiers();
        Method[] mt = cls.getMethods();
        Method[] dmt = cls.getDeclaredMethods();
        Constructor<?>[] cn = cls.getConstructors();
        Constructor<?>[] dcn = cls.getDeclaredConstructors();
        Field[] f = cls.getFields();
        Field[] df = cls.getDeclaredFields();
        ClassLoader l = cls.getClassLoader();
        Annotation[] a = cls.getAnnotations();
        Annotation[] da = cls.getDeclaredAnnotations();
        char[] sm = cls.getName().toCharArray();
        Package p = cls.getPackage();
        Class<?>[] in = cls.getInterfaces();
        Class<?>[] dc = cls.getDeclaredClasses();
        Class<?> sc = cls.getSuperclass();

        long id = 3125896351L + cls.getName().hashCode();
        id *= ms + id;
        id = 723 * id - mt.length >> dmt.length;
        for (Method m : mt) id *= -getMethodClId(m) + id * id;
        for (Method m : dmt) id += getMethodClId(m);
        id = "35tetGG".hashCode() + cn.length - id - "51".hashCode();
        id *= " ".hashCode() + " ".length() + id * id - dcn.length;
        for (Constructor<?> m : cn) id *= id * m.hashCode() * m.getModifiers() << m.getParameterCount();
        for (Constructor<?> m : dcn) id -= id + "".length() + 331L * m.getName().hashCode();
        id += f.length * id + "562gejkw".substring(2).hashCode() << df.length;
        for (Field rf : f) id <<= rf.getModifiers();
        for (Field rf : df) id *= rf.getName().hashCode() + id;
        id -= a.length * id * id << da.length;
        for (Annotation ra : a) id += (long) ra.toString().hashCode() * " asd2134256t".hashCode();
        for (Annotation ra : da) id *= ra.toString().toLowerCase().hashCode() + 111;
        for (char c : sm) id *= 7942 * id + c * -12462 + p.getName().toUpperCase(Locale.ROOT).indexOf(c % p.getName().length());
        for (Class<?> c1 : in) id *= -getClId(c1, i1 + 1);
        for (Class<?> c1 : dc) id -= -getClId(c1, i1 + 1);
        if (i1 < 10 && sc != null) {
            id *= 23L - getClId(sc, i1 + 1) * 0x31 + "d221".indexOf(3);
        }
        long[] lcid = getLoaderClId(l);
        id += (lcid[0] + lcid[" ".length()] + lcid[" ".repeat(1).length()] + lcid["  ".repeat(1).length()]) / 4;
        return id;
    }

    protected final long[] getLoaderClId(ClassLoader loader) {
        if (!StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass().isAssignableFrom(SoftId.class)) {
            throw new UnsupportedOperationException();
        }
        long[] id = new long[4];
        if (loader != null) {
            Module m = loader.getUnnamedModule();
            String n = m.getName() != null ? m.getName() : loader.getName();
            for (char c : n.toCharArray()) id[c % 4] *= m.getPackages().hashCode() - id[3] - id[2] + c >> 2; id[2] = -id[3] + m.toString().hashCode() * 1541L;
            return id;
        }
        return new long[] {0, 0, 0, 0};
    }
}