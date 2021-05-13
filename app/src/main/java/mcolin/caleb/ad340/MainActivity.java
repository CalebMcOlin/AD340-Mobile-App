package mcolin.caleb.ad340;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {


    // Button for the gridView
    private final String[] btnNames = {"Movies", "Traffic Cams", "Locations", "Button 4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Home");

        // Initiate and create the GridView
        GridView gridView = findViewById(R.id.gridView);
        final ButtonAdapter buttonAdapter = new ButtonAdapter(this, btnNames);
        gridView.setAdapter(buttonAdapter);

        // Send button
        Button mSendBtn = findViewById(R.id.sendBtn);
        mSendBtn.setOnClickListener(new BtnOnClickListener());
    }

    /**
     * Shows all the MENU options in the ActionBar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * EventHandler for all the options in the MENU
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.fire:
                Toast.makeText(this, "Refresh!", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
                return true;
            case R.id.about:
                Intent intent = new Intent(getBaseContext(), AboutActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
            // TO DO: make a switch statement
            if (btn.getText() == "Movies") {
                Intent intent = new Intent(getBaseContext(), MoviesActivity.class);
                startActivity(intent);
            } else if (btn.getText() == "Traffic Cams") {
                Intent intent = new Intent(getBaseContext(), TrafficActivity.class);
                startActivity(intent);
            } else if ((btn.getText() == "Locations")){
                Intent intent = new Intent(getBaseContext(), LocationActivity.class);
                startActivity(intent);
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