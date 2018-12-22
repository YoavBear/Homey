package com.example.yoavbear.homey;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingListItemCreator extends AppCompatActivity {
    private String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_item_creator);
        Intent i = getIntent();
        String creator = i.getStringExtra("user");
        TextView creatorText = (TextView) findViewById(R.id.creator_text);
        creatorText.setText(creator);
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

    public void initPostChoreToDatabase(final String action) {

        Button choreAddButton = (Button) findViewById(R.id.submit_button);
        choreAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView creatorTitleText = (TextView) findViewById(R.id.creator_text);
                EditText ItemTitleText = (EditText) findViewById(R.id.event_title);
                EditText descriptionTitleText = (EditText) findViewById(R.id.event_description);
                Spinner prioritySpinner = (Spinner) findViewById(R.id.priorities_spinner);

                String creator = creatorTitleText.getText().toString();
                String ItemTitle = ItemTitleText.getText().toString();
                String description = descriptionTitleText.getText().toString();
                String priority = prioritySpinner.getSelectedItem().toString();

                 if (ItemTitle.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter an Item title", Toast.LENGTH_LONG).show();
                     ItemTitleText.setBackgroundColor(Color.RED);
                }
                else {
                     ItemTitleText.setBackgroundColor(Color.WHITE);
                    DatabaseReference dRaffChores = FirebaseDatabase.getInstance().getReference().child("Households").child(user_id).child("ShoppingListItems").child(creator).child(ItemTitle);

                    Map newItem = new HashMap();
                    newItem.put("Title", ItemTitle);
                    newItem.put("priority", priority);
                    newItem.put("description", description);

                    dRaffChores.setValue(newItem);
                    if (action == "update") {
                        Toast.makeText(getApplicationContext(), "The shopping list item was successfully updated!", Toast.LENGTH_LONG).show();
                        finish();
                    } else
                        Toast.makeText(getApplicationContext(), "The shopping list item was added successfully!", Toast.LENGTH_LONG).show();

                    //reset fields
                     ItemTitleText.setText("");
                    descriptionTitleText.setText("");
                    prioritySpinner.setSelection(0);
                }
            }
        });

    }
}
