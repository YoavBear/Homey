package com.example.yoavbear.homey;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {

    private String creator;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ArrayList<ShoppingListItem> choresList = new ArrayList<>();
    private ShoppingListAdapter mAdapter;
    private ShoppingListItem.Priority chosenPriority = ShoppingListItem.Priority.Priorities;
    private String chosenFamilyMember = "Users";
    private String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        //creator = getIntent().getStringExtra("familyMember");
       // handleChoresList(creator);

       // RecyclerView choresRecyclerView = (RecyclerView) findViewById(R.id.chores_rView);

       // LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        //choresRecyclerView.setLayoutManager(mLayoutManager);


      //  mAdapter = new ShoppingListAdapter(this, choresList, creator, false);
      //  choresRecyclerView.setAdapter(mAdapter);


    }

}
