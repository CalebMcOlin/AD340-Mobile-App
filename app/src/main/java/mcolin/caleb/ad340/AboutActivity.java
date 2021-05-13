package mcolin.caleb.ad340;


import android.os.Bundle;
import android.widget.Button;

import java.util.Objects;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Hide the Action Bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Back Button
        Button mbackBtn = findViewById(R.id.exitAbout);
        mbackBtn.setOnClickListener(v -> finish());
    }
}