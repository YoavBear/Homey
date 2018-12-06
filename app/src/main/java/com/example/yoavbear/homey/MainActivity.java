package com.example.yoavbear.homey;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String creator;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ArrayList<Chore> choresList = new ArrayList<>();
    private MyAdapter mAdapter;
    private Chore.Priority chosenPriority = Chore.Priority.Priorities;
    private String chosenFamilyMember = "Users";
    private Chore.Category chosenCategory = Chore.Category.Categories;
    private String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        creator = getIntent().getStringExtra("familyMember");
        handleChoresList(creator);
        RecyclerView choresRecyclerView = (RecyclerView) findViewById(R.id.chores_rView);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        choresRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new MyAdapter(this, choresList, creator, false);
        choresRecyclerView.setAdapter(mAdapter);

// Navigation bar
        dl = (DrawerLayout)findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl,R.string.open, R.string.close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView)findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.addChores:
                        Intent i = new Intent(getApplicationContext(), ChoreCreator.class);
                        i.putExtra("user", creator);
                        startActivity(i);
                        dl.closeDrawers();
                        return true;
                    case R.id.addShoppingCart:
                        Toast.makeText(MainActivity.this, "TODO: ADD SHOPPING CART",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.completedChores:
                        Intent i2 = new Intent(getApplicationContext(), CompletedChoresActivity.class);
                        dl.closeDrawers();
                        startActivity(i2);
                        return true;
                    default:
                        return true;
                }

            }
        });
//End Navigation bar

        initCatSpinner();
        initNamesSpinner();
        initPrioritySpinner();

    }

    public void initCatSpinner() {
        Spinner catSpinner = (Spinner) findViewById(R.id.types_spinner);

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
        Spinner namesSpinner = (Spinner) findViewById(R.id.names_spinner);

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
        Spinner prioritySpinner = (Spinner) findViewById(R.id.priorities_spinner);

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
                    if (spinner.getId() == R.id.priorities_spinner) {
                        Chore.Priority priority = Chore.Priority.valueOf((String) adapterView.getAdapter().getItem(i));
                        chosenPriority = priority;
                        updateChores();
                    }
                    if (spinner.getId() == R.id.names_spinner) {
                        String fm = (String) adapterView.getAdapter().getItem(i);
                        chosenFamilyMember = fm;
                        updateChores();
                    }
                    if (spinner.getId() == R.id.categories_spinner) {
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

    public void handleChoresList(final String creator) {

        DatabaseReference dRaffAllChores = database.getReference().child("Households").child(user_id).child("Chores");

        dRaffAllChores.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot choreSnapshot : userSnapshot.getChildren()) {
                        ChorePost post = choreSnapshot.getValue(ChorePost.class);
                        Chore temp = new Chore(userSnapshot.getKey(), post.getAssignee(), post.getCategory(), choreSnapshot.getKey(), post.getDescription(), post.getPriority());
                        if (!choresList.contains(temp)) {
                            choresList.add(temp);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference dRaffChores = dRaffAllChores.child(creator);

        dRaffChores.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChorePost post = dataSnapshot.getValue(ChorePost.class);
                Chore temp = new Chore(creator, post.getAssignee(), post.getCategory(), dataSnapshot.getKey(), post.getDescription(), post.getPriority());
                if (!choresList.contains(temp)) {
                    choresList.add(temp);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                int index;
                ChorePost post = dataSnapshot.getValue(ChorePost.class);
                Chore temp = new Chore(creator, post.getAssignee(), post.getCategory(), dataSnapshot.getKey(), post.getDescription(), post.getPriority());
                index = choresList.indexOf(temp);
                if (index >= 0 && index < choresList.size()) {
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
                if (index >= 0 && index < choresList.size()) {
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

    @Override // Nav bar
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    public static class ChorePost {

        private String assignee;
        private Chore.Category category;
        private String description;
        private Chore.Priority priority;

        public ChorePost() {
        }

        public ChorePost(String assignee, Chore.Category category, String description, Chore.Priority priority) {
            this.assignee = assignee;
            this.category = category;
            this.description = description;
            this.priority = priority;
        }

        public String getAssignee() {
            return assignee;
        }

        public void setAssignee(String assignee) {
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

