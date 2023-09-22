package com.julhua.motti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword, editTextUserNames, editTextUserSurnames, editTextPasswordConfirm;
    Button buttonReg;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView, textViewMessageError;

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
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextUserNames = findViewById(R.id.user_names);
        editTextUserSurnames = findViewById(R.id.user_surnames);
        editTextPasswordConfirm = findViewById(R.id.password_confirm);
        buttonReg = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.loginNow);
        textViewMessageError = findViewById(R.id.message_error);


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
                name = editTextUserNames.getText().toString();
                surname = editTextUserSurnames.getText().toString();
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                passwordConfirm = editTextPasswordConfirm.getText().toString();

                if (TextUtils.isEmpty(name)) {
//                    Toast.makeText(Register.this, R.string.enter_email, Toast.LENGTH_SHORT).show();
                    textViewMessageError.setVisibility(View.VISIBLE);
                    textViewMessageError.setText(R.string.names_is_empty);
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(surname)) {
//                    Toast.makeText(Register.this, R.string.enter_email, Toast.LENGTH_SHORT).show();
                    textViewMessageError.setVisibility(View.VISIBLE);
                    textViewMessageError.setText(R.string.surnames_is_empty);
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(email)) {
//                    Toast.makeText(Register.this, R.string.enter_email, Toast.LENGTH_SHORT).show();
                    textViewMessageError.setVisibility(View.VISIBLE);
                    textViewMessageError.setText(R.string.enter_email);
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
//                    Toast.makeText(Register.this, R.string.enter_password, Toast.LENGTH_SHORT).show();
                    textViewMessageError.setVisibility(View.VISIBLE);
                    textViewMessageError.setText(R.string.enter_password);
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (!password.equals(passwordConfirm)){
//                    Toast.makeText(Register.this, R.string.password_not_equals, Toast.LENGTH_SHORT).show();
                    textViewMessageError.setVisibility(View.VISIBLE);
                    textViewMessageError.setText(R.string.password_not_equals);
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "Account created.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

}