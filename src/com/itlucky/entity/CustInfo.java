package com.itlucky.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class CustInfo {
    //主键
    private int id;
    //身份证号
    private String idCard;
    //姓名
    private String name;
    //年龄
    private int age;
    //住址
    private String address;
    //薪资
    private BigDecimal money;
    //员工状态
    private Status status;

    public CustInfo() {
    }

    public CustInfo(Integer id){
        this.id =id;
    }

    public CustInfo(Integer id,String name){
        this.id =id;
        this.name = name;
    }

    public CustInfo(int id, String idCard, String name, int age, String address, BigDecimal money) {
        this.id = id;
        this.idCard = idCard;
        this.name = name;
        this.age = age;
        this.address = address;
        this.money = money;
    }

    public CustInfo(int id, String idCard, String name, int age, String address, BigDecimal money, Status status) {
        this.id = id;
        this.idCard = idCard;
        this.name = name;
        this.age = age;
        this.address = address;
        this.money = money;
        this.status = status;
    }

    private void testM(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CustInfo{" +
                "id=" + id +
                ", idCard='" + idCard + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", money=" + money +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustInfo custInfo = (CustInfo) o;
        return id == custInfo.id && age == custInfo.age && Objects.equals(idCard, custInfo.idCard) && Objects.equals(name, custInfo.name) && Objects.equals(address, custInfo.address) && Objects.equals(money, custInfo.money);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idCard, name, age, address, money);
    }

    public enum Status {
        FREE,
        BUSY,
        VOCATION
    }
}
