package mcolin.caleb.ad340;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MoviesDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_details);

        // Declaring view for population
        TextView details = findViewById(R.id.movieDetails);
        ImageView imageView = findViewById(R.id.movieImageDetails);

        // Getting the details from the previous activity and populating the text view with them
        String[] movieDetails = getIntent().getStringArrayExtra("EXTRA_MOVIE_DETAILS");

        // Setting the given info in the activity
        Objects.requireNonNull(getSupportActionBar()).setTitle(movieDetails[0]);
        getSupportActionBar().setSubtitle(movieDetails[2] + " - " + movieDetails[1]);
        Picasso.get().load(movieDetails[3]).into(imageView);
        details.setText(movieDetails[4]);
    }

    /**
     * MENU
     * Shows all the menu options in the ActionBar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * MENU
     * EventHandler for all the options in the menu
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
}