package com.example.yoavbear.homey;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initCatSpinner();
        initNamesSpinner();
        initPrioritySpinner();

        RecyclerView choresRecyclerView = (RecyclerView)findViewById(R.id.chores_rView);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        choresRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        MyAdapter mAdapter = new MyAdapter(myDataset);
        choresRecyclerView.setAdapter(mAdapter);

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

    public void initCatSpinner(){
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


}

