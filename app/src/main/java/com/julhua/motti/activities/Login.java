package com.julhua.motti.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.julhua.motti.R;
import com.julhua.motti.activities.client.MapClientActivity;
import com.julhua.motti.activities.driver.MapDriverActivity;
import com.julhua.motti.user.User;

import java.util.Map;

public class Login extends AppCompatActivity {
    private static final String TAG = "LoginUser";
    TextInputEditText editTextEmail, editTextPassword;
    Button buttonLogin;
    FirebaseAuth mAuth;
    CollectionReference usersRef;
    FirebaseFirestore db;
    ProgressBar progressBar;
    TextView textView;
    String uID;
    public User user;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            uID = currentUser.getUid();
            Log.d(TAG, "currentUser: " + uID);
            // Busca en bd, si es conductor o si es pasajero
            obtiene_data_from_firestoredb(uID);

        }
    }

    private void obtiene_data_from_firestoredb(String uID) {
        db = FirebaseFirestore.getInstance();
        db.collection("user")
                .document(uID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> userData = document.getData();
                                validaDataFromDB(userData);
                            } else {
                                Log.d(TAG, "currentUser: El documento no existe");
                            }
                        } else {
                            Log.d(TAG, "currentUser: Error al obtener el documento");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error " + e);
                    }
                });

    }

    private void validaDataFromDB(Map<String, Object> userData) {
        String names = "", surnames = "";
        boolean isDriver = false, isPassenger = false;
        if (userData != null) {
            if (userData.containsKey("name")) {
                names = (String) userData.get("name");
            }
            if (userData.containsKey("surnames")) {
                surnames = (String) userData.get("surnames");
            }
            if (userData.containsKey("driver")) {
                isDriver = (boolean) userData.get("driver");
            }
            if (userData.containsKey("passenger")) {
                isPassenger = (boolean) userData.get("passenger");
            }
        }
        user = new User(names, surnames, isDriver, isPassenger);
        goToMapActivity(user);
    }

    private void goToMapActivity(User user) {
        if (user.isDriver()) {
            Toast.makeText(getApplicationContext(), "Login Driver Successful", Toast.LENGTH_SHORT)
                    .show();
            Intent intent = new Intent(getApplicationContext(), MapDriverActivity.class);
            startActivity(intent);
            finish();
        } else if (user.isPassenger()) {
            Toast.makeText(getApplicationContext(), "Login Passenger Successful", Toast.LENGTH_SHORT)
                    .show();
            Intent intent = new Intent(getApplicationContext(), MapClientActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.registerNow);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                System.out.println("User: " + email + " Pass:" + password);

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    String uID = mAuth.getCurrentUser().getUid();
                                    obtiene_data_from_firestoredb(uID);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        });
            }
        });
    }
}