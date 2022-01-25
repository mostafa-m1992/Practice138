package views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;
import com.example.practice138.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.app;
import app.spref;

public class EmailValidationActivity extends AppCompatActivity {

    String email, password, code ;

    PinView pinView ;
    ImageView imageView_back ;
    Button button_register;

    RequestQueue requestQueue ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_validation);

        getExtras() ;
        init()      ;
        onclick()   ;
    }

    private void getExtras() {
        email    = getIntent().getStringExtra("email") ;
        password = getIntent().getStringExtra("password") ;
        code     = getIntent().getStringExtra("code") ;
    }

    private void init() {
        app.l(code);
        pinView         = findViewById(R.id.pinView)         ;
        imageView_back  = findViewById(R.id.imageView_back)  ;
        button_register = findViewById(R.id.button_register) ;

        requestQueue    = Volley.newRequestQueue(this) ;
    }

    private void onclick() {
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pinViewCode = pinView.getText().toString().trim() ;

                if (pinViewCode.equals(code)) {
                    register() ;
                }
                else {
                    pinView.setLineColor(getResources().getColor(R.color.colorRed)) ;
                }
            }
        });

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish() ;
            }
        });
    }

    private void register() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, app.BASE_URL + "register.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response) ;
                    String message = jsonObject.getString("message") ;
                    if (message.equals("OK")) { // ok register
                        spref.getSharedPreferences().edit().putString(spref.EMAIL, email).apply() ;
                        Intent intent = new Intent(EmailValidationActivity.this, MainActivity.class) ;
                        startActivity(intent) ;
                        finish() ;
                    }
                    else {
                        // failed reg
                        app.failedT(getString(R.string.toast_errNet)) ;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                app.failedT(getString(R.string.toast_errNet)) ;
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>() ;
                params.put("email", email) ;
                params.put("password", password) ;

                return params ;
            }
        } ;

        requestQueue.add(stringRequest) ;
    }

}
