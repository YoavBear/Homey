package com.example.yoavbear.homey;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddFamilyMemberActivity extends AppCompatActivity {
    private EditText mName, mPasswordReg;
    private Button mAddFamilyMember, mBirthday;
    private TextView mGoBack;
    private RadioButton mParent, mChild;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String birthDay = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_family_member);

        mName = (EditText) findViewById(R.id.input_familyMemberName);
        mPasswordReg = (EditText) findViewById(R.id.input_FamilyMemberPassword);
        mAddFamilyMember = (Button) findViewById(R.id.btn_addMember);
        mGoBack = (TextView) findViewById(R.id.link_goBack);
        mParent = (RadioButton) findViewById(R.id.radioButton_parent);
        mChild = (RadioButton) findViewById(R.id.radioButton_child);
        mBirthday = (Button) findViewById(R.id.button_dbay);

        mParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mChild.isChecked())
                    mChild.setChecked(false);
            }
        });

        mChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mParent.isChecked())
                    mParent.setChecked(false);
            }
        });

        mGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddFamilyMemberActivity.this, FamilyMemberLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mAddFamilyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference householdReffDB;
                String familyRole;

                if (mName.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "Please enter a valid name!", Toast.LENGTH_LONG).show();
                else if (mPasswordReg.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "Please enter a valid Password!", Toast.LENGTH_LONG).show();
                else if(birthDay.isEmpty())
                    Toast.makeText(getApplicationContext(), "Please enter a birthday!", Toast.LENGTH_LONG).show();
                else if(!mParent.isChecked() && !mChild.isChecked())
                    Toast.makeText(getApplicationContext(), "Please choose a family role, Parent/Child", Toast.LENGTH_LONG).show();
                else {
                        if(mParent.isChecked())
                            familyRole = mParent.getText().toString();
                        else
                            familyRole = mChild.getText().toString();

                    householdReffDB = FirebaseDatabase.getInstance().getReference().child("Households").child(user_id).child("Family Members").child(mName.getText().toString());
                    Map FamilyMemberMap = new HashMap();

                    FamilyMemberMap.put("Family Role", familyRole);
                    FamilyMemberMap.put("Birthday", birthDay);
                    FamilyMemberMap.put("Password", mPasswordReg.getText().toString());

                    householdReffDB.setValue(FamilyMemberMap);
                    Toast.makeText(getApplicationContext(), "New member was added to the family!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AddFamilyMemberActivity.this, FamilyMemberLoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        mBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddFamilyMemberActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                birthDay = day + "/" + month + "/" + year;
            }
        };

    }


}
