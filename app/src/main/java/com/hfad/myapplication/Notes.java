package com.hfad.myapplication;

import java.util.ArrayList;
import java.util.List;

public class Notes {

    private String title, contents;

    public static List<Notes> array = new ArrayList<>();

    protected Notes(String theTitle, String theContents) {
        title = theTitle;
        contents = theContents;
    }

    public static void addNote(String t, String c) {
        array.add(new Notes(t, c));
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    /**
     * @return title of the note to be displayed in the list
     */
    public String toString() {
        return title;
    }
}

