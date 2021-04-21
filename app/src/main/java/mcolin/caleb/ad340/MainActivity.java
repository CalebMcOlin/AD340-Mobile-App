package mcolin.caleb.ad340;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    // Button names
    private String[] btnNames = {"Button 1", "Button 2", "Button 3", "Button 4"};
    private Button mSendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initiate and create the GridView
        GridView gridView = findViewById(R.id.gridView);
        final ButtonAdapter buttonAdapter = new ButtonAdapter(this, btnNames);
        gridView.setAdapter(buttonAdapter);

        // Send button
        mSendBtn = findViewById(R.id.sendBtn);
        mSendBtn.setOnClickListener(new BtnOnClickListener());

    }

    /**
     * Inner Adapter class
     */
    private class ButtonAdapter extends BaseAdapter {
        private Context context;
        private String[] btnView;

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

    /**
     * Inner Button Click listener Class
     */
    private class BtnOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // Cast to button to get the text
            Button btn = (Button) v;
            Toast toast = Toast.makeText(getApplicationContext(), btn.getText(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}