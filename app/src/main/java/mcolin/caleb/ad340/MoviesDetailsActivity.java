package mcolin.caleb.ad340;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MoviesDetailsActivity extends BaseActivity {

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
}