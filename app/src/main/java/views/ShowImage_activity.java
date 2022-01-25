package views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.practice138.R;

import app.app;

public class ShowImage_activity extends AppCompatActivity {

    ImageView imageView_back, imageView ;

    String link ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        link = getIntent().getStringExtra("link") ;

        imageView_back = findViewById(R.id.imageView_back) ;
        imageView      = findViewById(R.id.imageView) ;

        Glide.with(this).load(app.BASE_URL + link).into(imageView) ;

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition() ;

        super.onBackPressed();
    }
}