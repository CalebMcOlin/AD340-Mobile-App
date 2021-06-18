package mcolin.caleb.ad340;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class MainActivity extends BaseActivity {

    // Button for the gridView
    private final String[] btnNames = {"Movies", "Traffic Cams", "Locations", "Button 4"};

    // Login Fields
    private EditText username;
    private EditText email;
    private EditText password;

    // Saving shared Preferences
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";

    // Loading shared Preferences
    public String savedUsername;
    public String savedEmail;
    public String savedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Home");

        // Initiate and create the GridView
        GridView gridView = findViewById(R.id.gridView);
        final ButtonAdapter buttonAdapter = new ButtonAdapter(this, btnNames);
        gridView.setAdapter(buttonAdapter);

        // Login Button
        Button mSendBtn = findViewById(R.id.button);
        mSendBtn.setOnClickListener(new BtnOnClickListener());

        loadData();
        updateViews();
    }

    private void signIn() {
        Log.d("FIREBASE", "Login");

        // Sign into Firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Log.d("FIREBASE", "Email: " + email.getText().toString());
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("FIREBASE", "signIn:onComplete:" + task.isSuccessful());

                if (task.isSuccessful()) {
                    // update profile. username is the value entered in UI
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username.getText().toString())
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("FIREBASE", "User profile updated.");
                                        // Go to FirebaseActivity
                                        startActivity(new Intent(MainActivity.this, FirebaseActivity.class));
                                    }
                                }
                            });
                } else {
                    Log.d("FIREBASE", "Login failed");
                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * SHARED_PREFS
     * When called all the data within each edit text will be saved in shared preferences
     */
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USERNAME, username.getText().toString());
        editor.putString(EMAIL, email.getText().toString());
        editor.putString(PASSWORD, password.getText().toString());

        editor.apply();
    }

    /**
     * SHARED_PREFS
     * When called the saved data (if any) will be loading into the global variables
     */
    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        savedUsername = sharedPreferences.getString(USERNAME, "");
        savedEmail = sharedPreferences.getString(EMAIL, "");
        savedPassword = sharedPreferences.getString(PASSWORD, "");
    }

    /**
     * SHARED_PREFS
     * When called the data within the global variables will be loading into each Edittext
     */
    public void updateViews() {
        username = findViewById(R.id.user_name);
        password = findViewById(R.id.user_pw);
        email = findViewById(R.id.user_email);

        // setting the values
        username.setText(savedUsername);
        email.setText(savedEmail);
        password.setText(savedPassword);
    }

    /**
     * Checks each field for logging in.
     * Notifies the user if a field is empty.
     *
     * @return if all fields contain values or not
     */
    private boolean validateCredentials() {
        if (username.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Name field is empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (email.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Email field is empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Password field is empty", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Toast.makeText(getApplicationContext(), "Trying to login...", Toast.LENGTH_SHORT).show();
            saveData();
            return true;
        }
    }

    /**
     * Inner Button Click listener Class
     */
    private class BtnOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // Cast to button to get the text
            Button btn = (Button) v;
            // TODO: make a switch statement
            if (btn.getText() == "Movies") {
                Intent intent = new Intent(getBaseContext(), MoviesActivity.class);
                startActivity(intent);
            } else if (btn.getText() == "Traffic Cams") {
                Intent intent = new Intent(getBaseContext(), TrafficActivity.class);
                startActivity(intent);
            } else if (btn.getText() == "Locations") {
                Intent intent = new Intent(getBaseContext(), LocationActivity.class);
                startActivity(intent);
            } else if (v == findViewById(R.id.button)) {
                if (validateCredentials()) {
                    signIn();
                }
            } else {
                Toast.makeText(getApplicationContext(), btn.getText(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Inner Adapter class
     */
    private class ButtonAdapter extends BaseAdapter {
        private final Context context;
        private final String[] btnView;

        public ButtonAdapter(Context c, String[] b) {
            this.context = c;
            this.btnView = b;
        }

        public int getCount() {
            return btnView.length;
        }

        // NOT USED
        public String getItem(int i) {
            return btnView[i];
        }

        // NOT USED
        public long getItemId(int i) {
            return i;
        }

        public View getView(int i, View view, ViewGroup parent) {
            Button btn;
            if (view == null) {
                btn = new Button(context);
            } else {
                btn = (Button) view;
            }
            btn.setText(btnView[i]);
            btn.setId(i);
            btn.setOnClickListener(new BtnOnClickListener());
            return btn;
        }
    }
}