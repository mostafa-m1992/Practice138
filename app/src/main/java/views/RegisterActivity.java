package views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.practice138.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.app;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText_email, editText_password ;
    TextView textView_err_email, textView_password_err, textView_signIn ;
    Button button_signUp ;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init()    ;
        onclick() ;
    }

    private void init() {
        editText_email        = findViewById(R.id.editText_email) ;
        editText_password     = findViewById(R.id.editText_password) ;
        textView_err_email    = findViewById(R.id.textView_err_email) ;
        textView_password_err = findViewById(R.id.textView_password_err) ;
        textView_signIn       = findViewById(R.id.textView_signIn) ;
        button_signUp         = findViewById(R.id.button_signUp) ;

        requestQueue = Volley.newRequestQueue(this) ;
    }

    private void onclick() {
        textView_signIn.setOnClickListener(this) ;
        button_signUp  .setOnClickListener(this) ;
    }

    @Override
    public void onClick(View view) {
        if (view == textView_signIn) {
            finish();
        }

        if (view == button_signUp) {
            String email = editText_email.toString().trim();
            String password = editText_password.getText().toString().trim();

            if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                textView_err_email.setVisibility(View.VISIBLE) ;
            }
            else if (password.length() < 6) {
                textView_err_email.setVisibility(View.GONE) ;
                textView_password_err.setVisibility(View.VISIBLE) ;
            }
            else
                signUp(email, password) ;
        }
    }

    private void signUp(String email, String password) {

        double d = Math.random() ;
        int random = (int) (d * 100000) ;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, app.BASE_URL + "check_email.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response) ;
                    String message = jsonObject.getString("message") ;

                    if (message.equals("email_exists")) {
                        app.failedT(getString(R.string.toast_emailExists)) ;
                    }

                    if (message.equals("email_ok")) {
                        Intent intent = new Intent(RegisterActivity.this, EmailValidationActivity.class) ;
                        intent.putExtra("email", email) ;
                        intent.putExtra("password", password) ;
                        intent.putExtra("code", random + "") ;

                        startActivity(intent) ;
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
                params.put("code", random + "") ;

                return params ;
            }
        } ;

        requestQueue.add(stringRequest) ;
    }
}