package com.vcredit.rxjavademo;

/**
 * Created by qiubangbang on 2016/12/28.
 */

public class Student {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    String name;
    Course course;

}
