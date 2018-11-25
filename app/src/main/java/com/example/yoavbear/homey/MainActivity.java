package com.example.yoavbear.homey;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnEditClickListener, MyAdapter.OnDeleteClickListener {

    private Chore.FamilyMember creator;
    private ArrayList<Chore> choresList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        creator = Chore.FamilyMember.Yoav;

        initCatSpinner();
        initNamesSpinner();
        initPrioritySpinner();

        RecyclerView choresRecyclerView = (RecyclerView) findViewById(R.id.chores_rView);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        choresRecyclerView.setLayoutManager(mLayoutManager);

        getAllChores(creator);
        MyAdapter mAdapter = new MyAdapter(this, choresList);
        choresRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ChoreCreator.class);
                i.putExtra("user", creator.toString());
                startActivity(i);
            }
        });

    }

    public void initPrioritySpinner() {
        Spinner namesSpinner = (Spinner) findViewById(R.id.priorities_spinner);

        // Spinner Drop down elements
        List<String> priorities = new ArrayList<String>();
        priorities.add("Priority");
        priorities.add("Urgent");
        priorities.add("Highest");
        priorities.add("High");
        priorities.add("Medium");
        priorities.add("Low");
        priorities.add("Very Low");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, priorities);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        namesSpinner.setAdapter(dataAdapter);
    }

    public void initNamesSpinner() {
        Spinner namesSpinner = (Spinner) findViewById(R.id.names_spinner);

        // Spinner Drop down elements
        List<String> names = new ArrayList<String>();
        names.add("Users");
        names.add("Aviad");
        names.add("Vlad B");
        names.add("Vlad M");
        names.add("Yoav");
        names.add("Yotam");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, names);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        namesSpinner.setAdapter(dataAdapter);
    }

    public void initCatSpinner() {
        Spinner namesSpinner = (Spinner) findViewById(R.id.types_spinner);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Category");
        categories.add("General");
        categories.add("Laundry");
        categories.add("Cleaning");
        categories.add("Dishes");
        categories.add("Shopping");
        categories.add("Errands");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        namesSpinner.setAdapter(dataAdapter);
    }


    @Override
    public void onEditClick(Chore chore) {
        String b = "bla bla";
    }

    @Override
    public void onDeleteClick(Chore chore) {
        String b = "bla bla";

    }

    public void getAllChores(final Chore.FamilyMember creator) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dRaffChores = database.getReference().child("Chores").child(creator.name());


        dRaffChores.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                ChorePost post = dataSnapshot.getValue(ChorePost.class);
//                Chore temp = new Chore(creator, post.getAssignee(), post.getCategory(), dataSnapshot.getKey(), post.getDescription(), post.getPriority());
//                choresList.add(temp);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Chore first_chore = new Chore(Chore.FamilyMember.Yoav, Chore.FamilyMember.Yotam, Chore.Category.General, "First chore", "very important", Chore.Priority.Urgent);
        choresList.add(first_chore);
    }

    public static class ChorePost {

        private Chore.FamilyMember assignee;
        private Chore.Category category;
        private String description;
        private Chore.Priority priority;

        public ChorePost() {
        }

        public ChorePost(Chore.FamilyMember assignee, Chore.Category category, String description, Chore.Priority priority ) {
            this.assignee = assignee;
            this.category = category;
            this.description = description;
            this.priority = priority;
        }

        public Chore.FamilyMember getAssignee() {
            return assignee;
        }

        public void setAssignee(Chore.FamilyMember assignee) {
            this.assignee = assignee;
        }

        public Chore.Category getCategory() {
            return category;
        }

        public void setCategory(Chore.Category category) {
            this.category = category;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Chore.Priority getPriority() {
            return priority;
        }

        public void setPriority(Chore.Priority priority) {
            this.priority = priority;
        }
    }

}

