package views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.practice138.R;

import app.spref;

public class SplashActivity extends AppCompatActivity {

    ImageView imageView ;
    LinearLayout layout_refresh ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init() ;
        onclick() ;
    }

    private void init() {
        imageView = findViewById(R.id.imageView) ;
        layout_refresh = findViewById(R.id.layout_refresh) ;

        layout_refresh.setVisibility(View.GONE) ;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (app.app.isConnected()) { // net connected
                    if (spref.getSharedPreferences().getBoolean(spref.REMEMBER_ME, false)) {
                        if (spref.getSharedPreferences().getString(spref.EMAIL, "").equals("")) {
                            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this, imageView, "splash") ;
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class) ;
                            startActivity(intent, activityOptionsCompat.toBundle()) ;
                            finish() ;
                        }
                        else {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class)) ;
                            finish() ;
                        }
                    }
                    else {
                        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this, imageView, "splash") ;
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class) ;
                        startActivity(intent, activityOptionsCompat.toBundle()) ;
                        finish() ;
                    }
                }
                else {
                    layout_refresh.setVisibility(View.VISIBLE) ;
                }
            }
        }, 3000) ;
    }

    private void onclick() {
        layout_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init() ;
            }
        });
    }
}