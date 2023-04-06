package com.example.geodetective.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.geodetective.R;
import com.example.geodetective.helpers.AccountDetailsChecker;
import com.example.geodetective.helpers.LoginEncoder;
import com.example.geodetective.singletons.ActiveUser;
import com.example.geodetective.singletons.DbConnection;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

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

        // Get output messages
        TextView userMsg = findViewById(R.id.MsgUsernameTxt);
        TextView passMsg = findViewById(R.id.MsgPasswordTxt);

        //Set on click listeners
        returnBtn.setOnClickListener(v -> {
            // Start create profile activity
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        });

        updateUsernameBtn.setOnClickListener(v -> {
            String oldUsername = user.getUsername();
            String newUsername = newUserEditText.getText().toString().trim();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure you want to edit your Username?");
            builder.setMessage("Attempts made on the old Username will be deleted.");
            builder.setPositiveButton("Yes", (dialog, which) -> updateUsername(oldUsername, newUsername, userMsg));
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss()).show();
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
            assert hashOldPass != null;
            if(hashOldPass.equals(user.getPassword())){
                db.users.document(user.getUsername()).get().addOnCompleteListener(task -> {
                    String Msg;
                    if (task.isSuccessful()) {
                        DocumentSnapshot User = task.getResult();
                        if (User.exists()) {
                            User.getReference().update("Password",hashNewPass);
                            user.setPassword(hashNewPass);
                            Msg = "Profile updated successfully";
                            msg.setTextColor(Color.BLACK);
                        } else {
                            Msg = "Username is not in Database";
                        }
                    } else {
                        Msg = "Error getting data from Database";
                    }
                    msg.setText(Msg);
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
            db.users.document(newUsername).get().addOnCompleteListener(task -> {
                String Msg = "";
                if (task.isSuccessful()) {
                    DocumentSnapshot User = task.getResult();
                    if (User.exists()) {
                        Msg = "Username is already in use";
                    } else {
                        // Username can be used
                        Query userAttempts = db.attempts.whereEqualTo("Username", oldUsername);
                        db.deleteAttempts(userAttempts);
                        replaceUser(oldUsername, newUsername, msg);
                    }
                } else {
                    Msg = "Error getting data from Database";
                }
                msg.setText(Msg);
            });
        } catch (IllegalArgumentException e) {
            msg.setText(e.getMessage());
        }

    }

    @SuppressLint("SetTextI18n")
    private void replaceUser(String deletedUser, String newUsername, TextView msg) {

        db.updateQuestCreator(deletedUser, newUsername);
        db.users.document(deletedUser).delete().addOnSuccessListener(aVoid -> {
            db.createNewUser(newUsername, user.getPassword(), user.getTrusted());
            user.setUsername(newUsername);
            msg.setText("Profile updated successfully");
            msg.setTextColor(Color.BLACK);

        })
        .addOnFailureListener(e -> {
            String Msg = "Error when removing old Username";
            msg.setText(Msg);
        });
    }
}