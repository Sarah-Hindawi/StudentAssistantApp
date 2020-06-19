package com.hfad.myapplication;

import java.util.ArrayList;
import java.util.List;

public class TodoList {

    private String list;
    private static ArrayList<String> tasks;
    public static List<TodoList> array = new ArrayList<>();
    public static String CHOSEN = "chosen";

    protected TodoList(String theTitle, ArrayList<String> theContents) {
        list = theTitle;
        tasks = theContents;
    }

    public static void addList(String title) {
        array.add(new TodoList(title, new ArrayList<String>()));
    }

    public static void addTask(int id, String task) {
        array.get(id).getContents().add(task);
    }

    public String getTitle() {
        return list;
    }

    public static ArrayList<String> getContents() {
        return tasks;
    }

    /**
     * @return title of the note to be displayed in the list
     */
    public String toString() {
        return list;
    }
}

