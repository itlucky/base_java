package com.itlucky.java8.collection;

import java.math.BigDecimal;

/**
 * @author itlucky
 * @date 2023/6/17 22:01
 */
public class Student {

    private String name;

    private String code;

    private BigDecimal money;

    public Student(String name, String code, BigDecimal money) {
        this.name = name;
        this.code = code;
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", money=" + money +
                '}';
    }
}
