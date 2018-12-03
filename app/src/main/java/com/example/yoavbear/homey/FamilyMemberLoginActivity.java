package com.example.yoavbear.homey;

import android.content.Intent;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FamilyMemberLoginActivity extends AppCompatActivity {

    private TextView mAddFamilyMember;
    private EditText mPass;
    private Button mLogin;
    private Spinner familyMemberSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_member_login);

        mAddFamilyMember = (TextView) findViewById(R.id.link_addFamilyMember);
        mPass = (EditText)  findViewById(R.id.input_fm_password);
        mLogin = (Button) findViewById(R.id.btn_fm_login);
        familyMemberSpinner = (Spinner) findViewById(R.id.familyMember_spinner);

        initFamilyMemberSpinner();

        mAddFamilyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FamilyMemberLoginActivity.this, AddFamilyMemberActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(familyMemberSpinner.getSelectedItem().toString().equals("Family Members"))
                    Toast.makeText(getApplicationContext(), "Please choose a family member!", Toast.LENGTH_LONG).show();
                else if(mPass.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "Please enter your password!", Toast.LENGTH_LONG).show();
                else{
                    String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference dRaffUsersPassword = FirebaseDatabase.getInstance().getReference().child("Households").child(user_id).child("Family Members").child(familyMemberSpinner.getSelectedItem().toString()).child("Password");
                    dRaffUsersPassword.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.getValue().toString().equals(mPass.getText().toString()))
                                Toast.makeText(getApplicationContext(), "Wrong password!", Toast.LENGTH_LONG).show();
                            else{
                                Intent intent = new Intent(FamilyMemberLoginActivity.this, MainActivity.class);
                                intent.putExtra("familyMember",familyMemberSpinner.getSelectedItem().toString());
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

    }

    public void initFamilyMemberSpinner() {

        // Spinner Drop down elements
        final List<String> names = new ArrayList<String>();
        names.add("Family Members");

        // Creating adapter for spinner
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, names);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        familyMemberSpinner.setAdapter(dataAdapter);

        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
}
