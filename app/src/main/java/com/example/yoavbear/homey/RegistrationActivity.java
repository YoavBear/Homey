package com.example.yoavbear.homey;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {
    private FirebaseAuth mAut;
    private EditText mName, mEmailReg, mPasswordReg;
    private Button mRegistration;
    private TextView mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mName = (EditText) findViewById(R.id.input_name);
        mEmailReg = (EditText) findViewById(R.id.input_email);
        mPasswordReg = (EditText) findViewById(R.id.input_password);
        mRegistration = (Button) findViewById(R.id.btn_signup);
        mLogin = (TextView) findViewById(R.id.link_login);

        mAut = FirebaseAuth.getInstance();

        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailReg.getText().toString();
                String pass = mPasswordReg.getText().toString();

                if (mName.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "Please enter a valid name!", Toast.LENGTH_LONG).show();
                else if (email.isEmpty())
                    Toast.makeText(getApplicationContext(), "Please enter a valid email!", Toast.LENGTH_LONG).show();
                else if (pass.isEmpty())
                    Toast.makeText(getApplicationContext(), "Please enter a valid password!", Toast.LENGTH_LONG).show();
                else if (pass.length() < 6)
                    Toast.makeText(getApplicationContext(), "Password must be at least 6 characters long!", Toast.LENGTH_LONG).show();
                else {
                    mAut.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Registration has failed, please try again!", Toast.LENGTH_LONG).show();
                            } else {
                                String name = mName.getText().toString();
                                String user_id = mAut.getCurrentUser().getUid();
                                DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                                currentUser.setValue(name);

                                Toast.makeText(getApplicationContext(), "Successfully registered!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                return;
                            }
                        }
                    });
                }
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });


    }


}
