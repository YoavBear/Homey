package com.example.yoavbear.homey;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChoreCreator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chore_creator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent i = getIntent();
        String creator = i.getStringExtra("user");
        TextView creatorText = (TextView)findViewById(R.id.creator_text);
        creatorText.setText(creator);
        initCatSpinner();
        initNamesSpinner();
        initPrioritySpinner();
        initPostChoreToDatabase();

    }

    public void initPrioritySpinner(){
        Spinner namesSpinner = (Spinner) findViewById(R.id.priorities_spinner);

        // Spinner Drop down elements
        List<String> priorities = new ArrayList<String>();
        priorities.add("Priority");
        priorities.add("Urgent");
        priorities.add("Highest");
        priorities.add("High");
        priorities.add("Medium");
        priorities.add("Low");
        priorities.add("Lowest");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, priorities);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        namesSpinner.setAdapter(dataAdapter);
    }

    public void initNamesSpinner(){
        Spinner namesSpinner = (Spinner) findViewById(R.id.assignees_spinner);

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

    public void initCatSpinner(){
        Spinner namesSpinner = (Spinner) findViewById(R.id.categories_spinner);

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

    public void initPostChoreToDatabase() {

        Button choreAddButton = (Button) findViewById(R.id.submit_button);
        choreAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();

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
                    Toast.makeText(getApplicationContext(), "Please give the chore a title", Toast.LENGTH_LONG).show();
                    choreTitleText.setBackgroundColor(Color.RED);

                } else {
                    assigneeSpinner.setBackgroundColor(Color.WHITE);
                    choreTitleText.setBackgroundColor(Color.WHITE);

                    DatabaseReference dRaffChores = database.getReference().child("Chores").child(creator).child(choreTitle);

                    Map newChore = new HashMap();
                    newChore.put("assignee", assignee);
                    newChore.put("category", category);
                    newChore.put("priority", priority);
                    newChore.put("description", description);

                    dRaffChores.setValue(newChore);

                    Toast.makeText(getApplicationContext(),"The Chore was added successfully!",Toast.LENGTH_LONG).show();

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
