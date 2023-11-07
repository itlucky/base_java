package com.itlucky.java9;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.logging.Logger;

/**
 * java9的模块新特性 ， 给项目添加【module-info.java】
 *
 * @author itlucky
 * @date 2023/5/31 23:04
 */
public class Java9Test {

    /**
     * 会发现，如果项目中添加了 module-info.java 之后， 如果没有引入任何东西，这些平常正常使用功能都不能用了，比如这里的@Test注解也不能生效了。
     *
     * 只要在 module-info.java添加需要的内容引入即可，
     * 比如 requires java.base; (点击进去可以看到里面配了很多Java基础功能的包依赖引入) requires junit;(使用@Test注解就可以用了)
     *
     * module-info.java : 就是添加需要依赖的jar，requires xxx方式。
     *
     */
    @Test
    public void test1() throws NoSuchFieldException, IllegalAccessException {

        Class<String> stringClass = String.class;

        Field field = stringClass.getDeclaredField("value");
        // 这里执行会报错如下：
        // java.lang.reflect.InaccessibleObjectException: Unable to make field private final byte[]
        // java.lang.String.value accessible: module java.base does not "opens java.lang" to unnamed module @28c97a5
        // 原因：反射API的Java 9封装和安全性得到了改进，如果模块没有明确授权给其他模块使用反射的权限，那么其他模块是不允许使用反射进行修改的，看来Unsafe类是玩不成了。
        field.setAccessible(true);

        System.out.printf((String) field.get("ABCD"));
    }
}
