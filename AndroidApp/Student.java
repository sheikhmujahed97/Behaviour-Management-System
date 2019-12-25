package com.example.rramesh.emotiveperformance;

import java.io.Serializable;

public class Student implements Serializable {
    String name;
    String rollno;
    String section;
    String phone;




    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



    public Student(String name, String rollno, String section, String phone) {
        this.name = name;
        this.rollno = rollno;
        this.section = section;
        this.phone=phone;


    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
