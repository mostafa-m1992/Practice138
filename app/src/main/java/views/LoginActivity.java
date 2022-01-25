package views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText_email, editText_password ;
    Button button_signIn ;
    CheckBox checkBox_remember ;
    TextView textView_forgetPass, textView_signUp   ;

    TextView textView_err_email, textView_password_err ;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        onClick();
    }

    private void init() {
        editText_email      = findViewById(R.id.editText_email) ;
        editText_password   = findViewById(R.id.editText_password) ;
        button_signIn       = findViewById(R.id.button_signIn) ;
        checkBox_remember   = findViewById(R.id.checkBox_remember) ;
        textView_forgetPass = findViewById(R.id.textView_forgetPass) ;
        textView_signUp     = findViewById(R.id.textView_signUp) ;

        textView_err_email = findViewById(R.id.textView_err_email) ;
        textView_password_err = findViewById(R.id.textView_password_err) ;

        requestQueue = Volley.newRequestQueue(this);
    }

    private void onClick() {
        button_signIn.setOnClickListener(this);
        textView_forgetPass.setOnClickListener(this);
        textView_signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == button_signIn) {

            /*if (checkBox_remember.isChecked()) {
                spref.getSharedPreferences().edit().putBoolean(spref.REMEMBER_ME, true).apply();
            }
            else
                spref.getSharedPreferences().edit().putBoolean(spref.REMEMBER_ME, false).apply();*/

            //spref.getSharedPreferences().edit().putBoolean(spref.REMEMBER_ME, checkBox_remember.isChecked()).apply() ;

            String email = editText_email.getText().toString().trim() ;
            String password = editText_password.getText().toString().trim() ;

            if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                textView_err_email.setVisibility(View.VISIBLE) ;
            }
            else if (password.length() < 6) {
                textView_err_email.setVisibility(View.GONE) ;
                textView_password_err.setVisibility(View.VISIBLE) ;
            }
            else
                login(email, password) ;
        }
        if (view == textView_forgetPass) {
            Intent intent = new Intent(this, ForgetPassActivity.class) ;
            startActivity(intent) ;
        }
        if (view == textView_signUp) {
            Intent intent = new Intent(this, RegisterActivity.class) ;
            startActivity(intent) ;
        }
    }

    private void login(String email, String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, app.BASE_URL + "login.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");

                    if (message.equals("login_ok")) {
                        //spref.getSharedPreferences().edit().putString(spref.EMAIL, email).apply() ;
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class) ;
                        startActivity(intent) ;
                        finish() ;
                    }
                    if (message.equals("failed_login")) {
                        app.failedT(getString(R.string.toast_loginFailed));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                app.failedT(getString(R.string.toast_errNet));
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}