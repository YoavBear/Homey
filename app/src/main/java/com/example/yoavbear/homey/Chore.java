package com.example.yoavbear.homey;


public class Chore {

    @Override
    public boolean equals(Object o) {
        if ((this.getCreator().equals(((Chore) o).getCreator())) && (this.getTitle().equals(((Chore) o).getTitle())))
            return true;
        else return false;
    }

    public enum Category {Categories, General, Laundry, Cleaning, Dishes, Shopping, Errands}

    public enum Priority {Priorities, Urgent, High, Medium, Low}

    private String creator;
    private String assignee;
    private Category category;
    private String title;
    private String description;
    private Priority priority;

    public Chore(String creator, String assignee, Category category, String title, String description, Priority priority) {
        this.creator = creator;
        this.assignee = assignee;
        this.category = category;
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public Chore() {

    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
}
