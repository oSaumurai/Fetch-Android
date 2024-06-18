package com.fetch.oa.model;

public class ListItem {
    private int listId;
    private String name;

    public ListItem(int listId, String name) {
        this.listId = listId;
        this.name = name;
    }

    public int getListId() {
        return listId;
    }

    public String getName() {
        return name;
    }
}
