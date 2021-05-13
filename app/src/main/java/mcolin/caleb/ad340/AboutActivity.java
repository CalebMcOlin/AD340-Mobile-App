package mcolin.caleb.ad340;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Back Button
        Button mbackBtn = (Button) findViewById(R.id.exitAbout);
        mbackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}