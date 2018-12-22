package com.example.yoavbear.homey;


public class ShoppingListItem {

    @Override
    public boolean equals(Object o) {
        if ((this.getCreator().equals(((ShoppingListItem) o).getCreator())) && (this.getTitle().equals(((ShoppingListItem) o).getTitle())))
            return true;
        else return false;
    }


    public enum Priority {Priorities, Urgent, High, Medium, Low}
    private String creator;
    private String title;
    private String description;
    private Priority priority;

    public ShoppingListItem(String creator, String title, String description, Priority priority) {
        this.creator = creator;
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public ShoppingListItem() {

    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}


