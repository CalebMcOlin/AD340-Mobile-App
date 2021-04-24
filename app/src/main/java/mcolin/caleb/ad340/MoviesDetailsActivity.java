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

public class MoviesDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_details);

        // Declaring view for population
        TextView details = findViewById(R.id.movieDetails);
        ImageView imageView = findViewById(R.id.movieImage);

        // Getting the details from the previous activity and populating the text view with them
        String[] movieDetails = getIntent().getStringArrayExtra("EXTRA_MOVIE_DETAILS");

        // Setting the given info in the activity
        getSupportActionBar().setTitle(movieDetails[0]);
        getSupportActionBar().setSubtitle(movieDetails[2] + " - " + movieDetails[1]);
        details.setText(movieDetails[4]);
        // TODO Find a way to make this work
//        Picasso.get().load("https://cdn.collider.com/wp-content/uploads/2016/10/night-of-comet.jpg").into(imageView);
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
                Toast.makeText(this, "Fire Baby!", Toast.LENGTH_SHORT).show();
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