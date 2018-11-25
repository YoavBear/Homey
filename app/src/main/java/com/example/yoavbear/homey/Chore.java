package com.example.yoavbear.homey;

public class Chore {

    @Override
    public boolean equals(Object o) {
        if ((this.getCreator().equals(((Chore)o).getCreator())) && (this.getTitle().equals(((Chore)o).getTitle())))
        return true;
        else return false;
    }

    public enum FamilyMember {Users, Aviad, Vlad_B, Vlad_M, Yoav, Yotam}
    public enum Category {Category, General, Laundry, Cleaning, Dishes, Shopping, Errands}
    public enum Priority {Priority, Urgent, High, Medium, Low}

    private FamilyMember creator;
    private FamilyMember assignee;
    private Category category;
    private String title;
    private String description;
    private Priority priority;

    public Chore(FamilyMember creator, FamilyMember assignee, Category category, String title, String description, Priority priority) {
        this.creator = creator ;
        this.assignee = assignee;
        this.category = category;
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public Chore() {

    }

    public FamilyMember getCreator() {
        return creator;
    }

    public void setCreator(FamilyMember creator) {
        this.creator = creator;
    }

    public FamilyMember getAssignee() {
        return assignee;
    }

    public void setAssignee(FamilyMember assignee) {
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

    public FamilyMember getFamilyMember() {
        return creator;
    }

    public void setFamilyMember(FamilyMember familyMember) {
        this.creator = familyMember;
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
}
