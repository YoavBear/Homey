package com.example.yoavbear.homey;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
        priorities.add("Very Low");


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

}
