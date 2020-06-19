package com.hfad.myapplication;

import java.util.ArrayList;

public class Assignments {

    private String day;
    private ArrayList<String> courses;

    public static Assignments[] array = {
            new Assignments("Monday", new ArrayList<String>()),
            new Assignments("Tuesday", new ArrayList<String>()),
            new Assignments("Wednesday", new ArrayList<String>()),
            new Assignments("Thursday", new ArrayList<String>()),
            new Assignments("Friday", new ArrayList<String>()),
            new Assignments("Saturday", new ArrayList<String>()),
            new Assignments("Sunday", new ArrayList<String>()),
    };

    protected Assignments(String theTitle, ArrayList<String> theContents) {
        day = theTitle;
        courses = theContents;
    }

    public static void addTask(int id, String task) {
        array[id].getContents().add(task);
    }

    public String getTitle() {
        return day;
    }

    public ArrayList<String> getContents() {
        return courses;
    }

    /**
     * @return title of the note to be displayed in the list
     */
    public String toString() {
        return day;
    }
}

