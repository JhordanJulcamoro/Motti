package com.julhua.motti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.julhua.motti.user.User;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private static final String TAG = "RegisterUser";
    TextInputEditText editTextEmail, editTextPassword, editTextUserNames, editTextUserSurnames, editTextPasswordConfirm;
    Button buttonReg;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    ProgressBar progressBar;
    TextView textView, textViewMessageError;
    RadioButton radioButtonDriver, radioButtonPassenger;
    private User user;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextUserNames = findViewById(R.id.user_names);
        editTextUserSurnames = findViewById(R.id.user_surnames);
        editTextPasswordConfirm = findViewById(R.id.password_confirm);
        buttonReg = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.loginNow);
        textViewMessageError = findViewById(R.id.message_error);
        radioButtonDriver = findViewById(R.id.driver);
        radioButtonPassenger = findViewById(R.id.passenger);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                textViewMessageError.setVisibility(View.GONE);
                String email, password, passwordConfirm, name, surname;
                boolean isDriver, isPassenger;
                name = editTextUserNames.getText().toString();
                surname = editTextUserSurnames.getText().toString();
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                passwordConfirm = editTextPasswordConfirm.getText().toString();
                isDriver = radioButtonDriver.isChecked();
                isPassenger = radioButtonPassenger.isChecked();

                if (TextUtils.isEmpty(name)) {
                    textViewMessageError.setVisibility(View.VISIBLE);
                    textViewMessageError.setText(R.string.names_is_empty);
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(surname)) {
                    textViewMessageError.setVisibility(View.VISIBLE);
                    textViewMessageError.setText(R.string.surnames_is_empty);
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    textViewMessageError.setVisibility(View.VISIBLE);
                    textViewMessageError.setText(R.string.enter_email);
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    textViewMessageError.setVisibility(View.VISIBLE);
                    textViewMessageError.setText(R.string.enter_password);
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (!password.equals(passwordConfirm)) {
                    textViewMessageError.setVisibility(View.VISIBLE);
                    textViewMessageError.setText(R.string.password_not_equals);
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (!isDriver && !isPassenger) {
                    textViewMessageError.setVisibility(View.VISIBLE);
                    textViewMessageError.setText("Select type driver or passenger");
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            registra_firestore_database(name, surname, isDriver, isPassenger);
                            Toast.makeText(Register.this, R.string.account_created, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                            finish();
                        } else {
                            textViewMessageError.setVisibility(View.VISIBLE);
                            textViewMessageError.setText(R.string.authentication_failed);
                        }
                    }
                });
            }
        });
    }

    private void registra_firestore_database(String name, String surname, boolean isDriver, boolean isPassenger) {
        user = new User(name,surname,isDriver,isPassenger);
        db.collection("user").
                add(user).
                addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, String.format(getString(R.string.user_register_in_database)));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, String.format(getString(R.string.user_not_register_in_database)));
                    }
                });

    }

}