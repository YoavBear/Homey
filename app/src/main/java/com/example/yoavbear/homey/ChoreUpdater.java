package com.example.yoavbear.homey;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChoreUpdater extends ChoreCreator {

    private FirebaseDatabase database;
    private TextView creatorTitleText;
    private EditText choreTitleText;
    private EditText descriptionTitleText;
    private Spinner assigneeSpinner;
    private Spinner categorySpinner;
    private Spinner prioritySpinner;
    private String creator;
    private String choreTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        Intent i = getIntent();
        creator = i.getStringExtra("user");
        choreTitle = i.getStringExtra("choreTitle");
        creatorTitleText = (TextView) findViewById(R.id.creator_text);
        choreTitleText = (EditText) findViewById(R.id.event_title);
        descriptionTitleText = (EditText) findViewById(R.id.event_description);
        assigneeSpinner = (Spinner) findViewById(R.id.assignees_spinner);
        categorySpinner = (Spinner) findViewById(R.id.categories_spinner);
        prioritySpinner = (Spinner) findViewById(R.id.priorities_spinner);

        initUpdateChoreInDatabase();
    }

    public void initUpdateChoreInDatabase() {

        DatabaseReference dRaffChores = database.getReference().child("Chores").child(creator).child(choreTitle);
        dRaffChores.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MainActivity.ChorePost post = dataSnapshot.getValue(MainActivity.ChorePost.class);
                Chore temp = new Chore(creator, post.getAssignee(), post.getCategory(), choreTitle, post.getDescription(), post.getPriority());
                choreTitleText.setText(temp.getTitle());
                descriptionTitleText.setText(temp.getDescription());
                assigneeSpinner.setSelection(((ArrayAdapter<String>)assigneeSpinner.getAdapter()).getPosition(post.getAssignee()));
                categorySpinner.setSelection(post.getCategory().ordinal());
                prioritySpinner.setSelection(post.getPriority().ordinal());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        initPostChoreToDatabase("update");
    }
}

