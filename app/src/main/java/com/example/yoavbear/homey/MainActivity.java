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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnEditClickListener, MyAdapter.OnDeleteClickListener {

    private Chore.FamilyMember creator;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ArrayList<Chore> choresList = new ArrayList<>();
    private MyAdapter mAdapter;
    private Chore.Priority chosenPriority = Chore.Priority.Priority;
    private Chore.FamilyMember chosenFamilyMember = Chore.FamilyMember.Users;
    private Chore.Category chosenCategory = Chore.Category.Category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        creator = Chore.FamilyMember.Yoav;



        handleChoresList(creator);

        RecyclerView choresRecyclerView = (RecyclerView) findViewById(R.id.chores_rView);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        choresRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(this, choresList);
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
        initCatSpinner();
        initNamesSpinner();
        initPrioritySpinner();
    }

    public void initPrioritySpinner() {
        Spinner prioritySpinner = (Spinner) findViewById(R.id.priorities_spinner);

        // Spinner Drop down elements
        List<String> priorities = new ArrayList<String>();
        priorities.add("Priority");
        priorities.add("Urgent");
        priorities.add("High");
        priorities.add("Medium");
        priorities.add("Low");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, priorities);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        prioritySpinner.setAdapter(dataAdapter);
        addSpinnerListener(prioritySpinner);

    }

    public void addSpinnerListener(final Spinner spinner){

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!choresList.isEmpty())
                {
                    if(spinner.getId()==R.id.priorities_spinner)
                    {
                        Chore.Priority priority = Chore.Priority.valueOf((String) adapterView.getAdapter().getItem(i));
                        chosenPriority = priority;
                        updateChores();
                    }
                    if(spinner.getId()==R.id.names_spinner)
                    {
                        Chore.FamilyMember fm = Chore.FamilyMember.valueOf((String) adapterView.getAdapter().getItem(i));
                        chosenFamilyMember = fm;
                        updateChores();
                    }
                    if(spinner.getId()==R.id.categories_spinner)
                    {
                        Chore.Category category = Chore.Category.valueOf((String) adapterView.getAdapter().getItem(i));
                        chosenCategory = category;
                        updateChores();
                    }



                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void initNamesSpinner() {
        Spinner namesSpinner = (Spinner) findViewById(R.id.names_spinner);

        // Spinner Drop down elements
        List<String> names = new ArrayList<String>();
        names.add("Users");
        names.add("Aviad");
        names.add("Vlad_B");
        names.add("Vlad_M");
        names.add("Yoav");
        names.add("Yotam");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, names);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        namesSpinner.setAdapter(dataAdapter);
        addSpinnerListener(namesSpinner);
    }

    public void initCatSpinner() {
        Spinner catSpinner = (Spinner) findViewById(R.id.types_spinner);

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
        catSpinner.setAdapter(dataAdapter);
        addSpinnerListener(catSpinner);
    }

    @Override
    public void onEditClick(Chore chore) {

    }

    @Override
    public void onDeleteClick(Chore chore) {
        Button deleteChoreBtn;
        DatabaseReference dRaffChores = database.getReference().child("Chores").child(chore.getCreator().name());
        deleteChoreBtn = (Button) findViewById(R.id.choreButton_delete);
        deleteChoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public void handleChoresList(final Chore.FamilyMember creator) {

        DatabaseReference dRaffChores = database.getReference().child("Chores").child(creator.name());

        dRaffChores.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChorePost post = dataSnapshot.getValue(ChorePost.class);
                Chore temp = new Chore(creator, post.getAssignee(), post.getCategory(), dataSnapshot.getKey(), post.getDescription(), post.getPriority());
                choresList.add(temp);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                int index;
                ChorePost post = dataSnapshot.getValue(ChorePost.class);
                Chore temp = new Chore(creator, post.getAssignee(), post.getCategory(), dataSnapshot.getKey(), post.getDescription(), post.getPriority());
                index = choresList.indexOf(temp);
                if(index >= 0 && index < choresList.size()) {
                    choresList.remove(index); // remove the old chore
                    choresList.add(index, temp); // add the new chore
                    mAdapter.notifyItemRemoved(index);
                    mAdapter.notifyItemRangeChanged(index, choresList.size());
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                int index;
                ChorePost post = dataSnapshot.getValue(ChorePost.class);
                Chore temp = new Chore(creator, post.getAssignee(), post.getCategory(), dataSnapshot.getKey(), post.getDescription(), post.getPriority());
                index = choresList.indexOf(temp);
                if(index >= 0 && index < choresList.size()) {
                    choresList.remove(index);
                    mAdapter.notifyItemRemoved(index);
                    mAdapter.notifyItemRangeChanged(index, choresList.size());
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    public void updateChores()
    {
        ArrayList<Chore> tempList = new ArrayList<Chore>();
        for (Chore chore : choresList) {
            if((chore.getPriority().equals(chosenPriority) || chosenPriority.equals(Chore.Priority.Priority))
                    && (chore.getCategory().equals(chosenCategory) || chosenCategory.equals(Chore.Category.Category))
                    && (chore.getAssignee().equals(chosenFamilyMember) || chosenFamilyMember.equals(Chore.FamilyMember.Users))){
                tempList.add(chore);
            }
        }
        mAdapter.updateList(tempList);
    }

}

