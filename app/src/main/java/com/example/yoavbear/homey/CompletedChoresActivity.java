package com.example.yoavbear.homey;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CompletedChoresActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ArrayList<Chore> choresList = new ArrayList<>();
    private MyAdapter mAdapter;
    private Chore.Priority chosenPriority = Chore.Priority.Priorities;
    private String chosenFamilyMember = "Users";
    private Chore.Category chosenCategory = Chore.Category.Categories;
    private String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_chores);

        RecyclerView choresRecyclerView = (RecyclerView) findViewById(R.id.completed_chores_rView);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        choresRecyclerView.setLayoutManager(mLayoutManager);

        getCompletedChoresList();

        mAdapter = new MyAdapter(this, choresList,"", true);
        choresRecyclerView.setAdapter(mAdapter);

        initCatSpinner();
        initNamesSpinner();
        initPrioritySpinner();


    }

    public void initCatSpinner() {
        Spinner catSpinner = (Spinner) findViewById(R.id.completed_chores_types_spinner);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        for (Chore.Category category : Chore.Category.values()) {
            categories.add(category.toString());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        catSpinner.setAdapter(dataAdapter);
        addSpinnerListener(catSpinner);
    }

    public void initNamesSpinner() {
        Spinner namesSpinner = (Spinner) findViewById(R.id.completed_chores_names_spinner);

        // Spinner Drop down elements
        final List<String> names = new ArrayList<String>();
        names.add("Users");

        // Creating adapter for spinner
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, names);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        namesSpinner.setAdapter(dataAdapter);
        addSpinnerListener(namesSpinner);

        DatabaseReference dRaffUsers = FirebaseDatabase.getInstance().getReference().child("Households").child(user_id).child("Family Members");

        dRaffUsers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                names.add(dataSnapshot.getKey());
                dataAdapter.notifyDataSetChanged();
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
    }

    public void initPrioritySpinner() {
        Spinner prioritySpinner = (Spinner) findViewById(R.id.completed_chores_priorities_spinner);

        // Spinner Drop down elements
        List<String> priorities = new ArrayList<String>();
        for (Chore.Priority priority : Chore.Priority.values()) {
            priorities.add(priority.toString());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, priorities);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        prioritySpinner.setAdapter(dataAdapter);
        addSpinnerListener(prioritySpinner);

    }

    public void addSpinnerListener(final Spinner spinner) {

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!choresList.isEmpty()) {
                    if (spinner.getId() == R.id.completed_chores_priorities_spinner) {
                        Chore.Priority priority = Chore.Priority.valueOf((String) adapterView.getAdapter().getItem(i));
                        chosenPriority = priority;
                        updateChores();
                    }
                    if (spinner.getId() == R.id.completed_chores_names_spinner) {
                        String fm = (String) adapterView.getAdapter().getItem(i);
                        chosenFamilyMember = fm;
                        updateChores();
                    }
                    if (spinner.getId() == R.id.completed_chores_types_spinner) {
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
    public void updateChores() {
        ArrayList<Chore> tempList = new ArrayList<Chore>();
        for (Chore chore : choresList) {
            if ((chore.getPriority().equals(chosenPriority) || chosenPriority.equals(Chore.Priority.Priorities))
                    && (chore.getCategory().equals(chosenCategory) || chosenCategory.equals(Chore.Category.Categories))
                    && (chore.getAssignee().equals(chosenFamilyMember) || chosenFamilyMember.equals("Users"))) {
                tempList.add(chore);
            }
        }
        mAdapter.updateList(tempList);
    }

    public void getCompletedChoresList() {

        DatabaseReference dRaffAllChores = database.getReference().child("Households").child(user_id).child("Completed Chores");

        dRaffAllChores.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            //    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot choreSnapshot : dataSnapshot.getChildren()) {
                        MainActivity.ChorePost post = choreSnapshot.getValue(MainActivity.ChorePost.class);
                        Chore temp = new Chore(dataSnapshot.getKey(), post.getAssignee(), post.getCategory(), choreSnapshot.getKey(), post.getDescription(), post.getPriority());
                        if (!choresList.contains(temp)) {
                            choresList.add(temp);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
    //        }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
