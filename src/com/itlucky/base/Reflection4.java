package com.itlucky.base;

import java.lang.annotation.*;
import java.lang.reflect.Field;


// 反射操作注解
public class Reflection4 {

    public static void main(String[] args)
        throws ClassNotFoundException, NoSuchFieldException {

        Class cs = Class.forName("com.itlucky.base.Student");

        //通过反射获得注解
        Annotation[] annotations = cs.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(annotation);
        }

//        Field name = cs.getDeclaredField("name");
//        Fieldhj fieldhj = name.getAnnotation(Fieldhj.class);
//        String s = fieldhj.columnName();
//        String type = fieldhj.type();
//        int length = fieldhj.length();
//
//        System.out.println(s+"***"+type+"***"+length);
        Field[] declaredFields = cs.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            Fieldhj fieldhj = field.getAnnotation(Fieldhj.class);
            System.out.println("%%%%%"+field.getName());
            System.out.println(fieldhj.columnName());
            System.out.println(fieldhj.type());
            System.out.println(fieldhj.length());


        }

    }
}


@Tablehj("db_student")
class Student {

    @Fieldhj(columnName = "p_id", type = "int", length = 5)
    private int id;

    @Fieldhj(columnName = "p_name", type = "varchar", length = 6)
    private String name;

    @Fieldhj(columnName = "p_age", type = "int", length = 3)
    private int age;

    public Student() {
    }

    public Student(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    /**
     * 取得id的值
     *
     * @return id 的值
     */
    public int getId() {
        return id;
    }

    /**
     * 设定id的值
     *
     * @param id 设定值
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 取得name的值
     *
     * @return name 的值
     */
    public String getName() {
        return name;
    }

    /**
     * 设定name的值
     *
     * @param name 设定值
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 取得age的值
     *
     * @return age 的值
     */
    public int getAge() {
        return age;
    }

    /**
     * 设定age的值
     *
     * @param age 设定值
     */
    public void setAge(int age) {
        this.age = age;
    }
}


// 类名的注解
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface Tablehj {
    String value() default "";
}


// 属性的注解
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface Fieldhj {
    String columnName();

    String type();

    int length();
}


