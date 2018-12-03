package com.example.yoavbear.homey;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChoreCreator extends AppCompatActivity {

    private String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
@Override
  protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chore_creator);
        Intent i = getIntent();
        String creator = i.getStringExtra("user");
        TextView creatorText = (TextView) findViewById(R.id.creator_text);
        creatorText.setText(creator);
        initCatSpinner();
        initNamesSpinner();
        initPrioritySpinner();
        initPostChoreToDatabase("add");

    }

    public void initPrioritySpinner(){

       Spinner prioritiesSpinner = (Spinner) findViewById(R.id.priorities_spinner);

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
        prioritiesSpinner.setAdapter(dataAdapter);
    }


    public void initNamesSpinner() {
        final Spinner namesSpinner = (Spinner) findViewById(R.id.assignees_spinner);
      
        // Spinner Drop down elements
         final List<String> names = new ArrayList<String>();
         names.add("Users");

        // Creating adapter for spinner
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, names);
    
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        namesSpinner.setAdapter(dataAdapter);

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

    public void initCatSpinner(){

        Spinner namesSpinner = (Spinner) findViewById(R.id.categories_spinner);

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
        namesSpinner.setAdapter(dataAdapter);
    }

    public void initPostChoreToDatabase(final String action) {

        Button choreAddButton = (Button) findViewById(R.id.submit_button);
        choreAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView creatorTitleText = (TextView) findViewById(R.id.creator_text);
                EditText choreTitleText = (EditText) findViewById(R.id.event_title);
                EditText descriptionTitleText = (EditText) findViewById(R.id.event_description);
                Spinner assigneeSpinner = (Spinner) findViewById(R.id.assignees_spinner);
                Spinner categorySpinner = (Spinner) findViewById(R.id.categories_spinner);
                Spinner prioritySpinner = (Spinner) findViewById(R.id.priorities_spinner);

                String creator = creatorTitleText.getText().toString();
                String choreTitle = choreTitleText.getText().toString();
                String description = descriptionTitleText.getText().toString();
                String assignee = assigneeSpinner.getSelectedItem().toString();
                String category = categorySpinner.getSelectedItem().toString();
                String priority = prioritySpinner.getSelectedItem().toString();

                if (assigneeSpinner.getSelectedItemId() == 0) {
                    Toast.makeText(getApplicationContext(), "Please assign the chore to someone", Toast.LENGTH_LONG).show();
                    assigneeSpinner.setBackgroundColor(Color.RED);

                } else if (choreTitle.isEmpty()) {
                    assigneeSpinner.setBackgroundColor(Color.WHITE);
                    Toast.makeText(getApplicationContext(), "Please enter a chore title", Toast.LENGTH_LONG).show();
                    choreTitleText.setBackgroundColor(Color.RED);

                } else {
                    assigneeSpinner.setBackgroundColor(Color.WHITE);
                    choreTitleText.setBackgroundColor(Color.WHITE);

                    DatabaseReference dRaffChores = FirebaseDatabase.getInstance().getReference().child("Households").child(user_id).child("Chores").child(creator).child(choreTitle);

                    Map newChore = new HashMap();
                    newChore.put("assignee", assignee);
                    newChore.put("category", category);
                    newChore.put("priority", priority);
                    newChore.put("description", description);

                    dRaffChores.setValue(newChore);
                    if (action == "update") {
                        Toast.makeText(getApplicationContext(), "The Chore was successfully updated!", Toast.LENGTH_LONG).show();
                        finish();
                    } else
                        Toast.makeText(getApplicationContext(), "The Chore was added successfully!", Toast.LENGTH_LONG).show();

                    //reset fields
                    choreTitleText.setText("");
                    descriptionTitleText.setText("");
                    assigneeSpinner.setSelection(0);
                    categorySpinner.setSelection(0);
                    prioritySpinner.setSelection(0);
                }
            }
        });

    }

}
