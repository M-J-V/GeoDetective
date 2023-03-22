package com.example.geodetective;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class EditDetailsActivity extends AppCompatActivity {
    ActiveUser user = ActiveUser.getInstance();
    DbConnection db = DbConnection.getInstance();
    AccountDetailsChecker checker = AccountDetailsChecker.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

        // Get buttons from activity
        ImageButton returnBtn = findViewById(R.id.BackBtn);
        Button updateUsernameBtn = findViewById(R.id.changeUsernameBtn);
        Button updatePassBtn = findViewById(R.id.changePasswordBtn);

        // Get editText fields
        EditText newUserEditText = findViewById(R.id.newUsernameEditTxt);
        EditText oldPassInputEditText = findViewById(R.id.oldPasswordEditTxt);
        EditText newPassInputEditText = findViewById(R.id.newPasswordEditTxt);

        // Get output msgs
        TextView userMsg = findViewById(R.id.MsgUsernameTxt);
        TextView passMsg = findViewById(R.id.MsgPasswordTxt);

        //Set on click listeners
        returnBtn.setOnClickListener(v -> {
            // Start create profile activity
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        });

        updateUsernameBtn.setOnClickListener(v -> {
            String oldUsername = user.getUsername();
            String newUsername = newUserEditText.getText().toString();

            updateUsername(oldUsername, newUsername, userMsg);
        });

        updatePassBtn.setOnClickListener(v -> {
            String oldPassword = oldPassInputEditText.getText().toString();
            String newPassword = newPassInputEditText.getText().toString();

            updatePassword(oldPassword, newPassword, passMsg);
        });

    }

    private void updatePassword(String oldPass, String newPass, TextView msg) {
        msg.setTextColor(Color.RED);
        try{
            // Check that is valid password
            checker.checkPassword(newPass, newPass);

            if (oldPass == null) {
                throw new IllegalArgumentException("Please enter Old password");
            }

            String hashOldPass = LoginEncoder.hashWord(oldPass);
            String hashNewPass = LoginEncoder.hashWord(newPass);
            // Check if oldPassword is the same as the actual password
            if(hashOldPass.equals(user.getPassword())){
                db.users.document(user.getUsername()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String Msg = "";
                        if (task.isSuccessful()) {
                            DocumentSnapshot User = task.getResult();
                            if (User.exists()) {
                                replaceUser(user.getUsername(), user.getUsername(), hashNewPass, msg);
                            } else {
                                Msg = "Username is not in Database";
                            }
                        } else {
                            Msg = "Error getting data from Database";
                        }
                        msg.setText(Msg);
                    }
                });
            } else {
                throw new IllegalArgumentException("Old password is incorrect");
            }
        } catch (IllegalArgumentException e) {
            msg.setText(e.getMessage());
        }
    }

    private void updateUsername(String oldUsername, String newUsername, TextView msg) {
        msg.setTextColor(Color.RED);
        try {
            // First we check the new username is valid
            checker.checkUsername(newUsername);
            // We check if the new username already exists in the database
            db.users.document(newUsername).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    String Msg = "";
                    if (task.isSuccessful()) {
                        DocumentSnapshot User = task.getResult();
                        if (User.exists()) {
                            Msg = "Username is already in use";
                        } else {
                            // Username can be used
                            replaceUser(oldUsername, newUsername, user.getPassword(), msg);
                        }
                    } else {
                        Msg = "Error getting data from Database";
                    }
                    msg.setText(Msg);
                }
            });
        } catch (IllegalArgumentException e) {
            msg.setText(e.getMessage());
        }

    }

    private void replaceUser(String deletedUser, String newUsername, String newPassword, TextView msg) {
        db.users.document(deletedUser).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                db.createNewUser(newUsername, newPassword);
                user.setUsername(newUsername);
                user.setPassword(newPassword);
                msg.setText("Profile updated succesfully");
                msg.setTextColor(Color.BLACK);

            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String Msg = "Error when removing old Username";
                msg.setText(Msg);
            }
        });
    }
}
