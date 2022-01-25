package views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class ForgetPassActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout layout_email, layout_password, layout_pinView ;
    EditText editText_email, editText_password ;
    TextView textView_err_email, textView_password_err ;
    PinView pinView ;
    Button button_continue, button_checkCode, button_goBack ;

    String email ;
    int random ;

    RequestQueue requestQueue ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        init();
        onclick();
    }

    private void onclick() {
        button_continue.setOnClickListener(this) ;
        button_checkCode.setOnClickListener(this) ;
        button_goBack.setOnClickListener(this) ;
    }

    private void init() {
        layout_email = findViewById(R.id.layout_email) ;
        layout_password = findViewById(R.id.layout_password) ;
        layout_pinView = findViewById(R.id.layout_pinView) ;
        editText_email = findViewById(R.id.editText_email) ;
        editText_password = findViewById(R.id.editText_password) ;
        textView_err_email = findViewById(R.id.textView_err_email) ;
        textView_password_err = findViewById(R.id.textView_password_err) ;
        pinView = findViewById(R.id.pinView) ;
        button_continue = findViewById(R.id.button_continue) ;
        button_checkCode = findViewById(R.id.button_checkCode) ;
        button_goBack = findViewById(R.id.button_goBack) ;

        requestQueue = Volley.newRequestQueue(this) ;
    }

    @Override
    public void onClick(View view) {

        if (view == button_continue) {
            email = editText_email.getText().toString().trim() ;
            if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                textView_err_email.setVisibility(View.VISIBLE) ;
            }
            else {
                checkEmail() ;
            }
        }

        if (view == button_checkCode) {
            String code = pinView.getText().toString().trim() ;
            if (code.equals(random+"")) { // code ok
                layout_pinView.setVisibility(View.GONE) ;
                button_checkCode.setVisibility(View.GONE);

                layout_password.setVisibility(View.VISIBLE);
                button_goBack.setVisibility(View.VISIBLE);
            }
            else {
                pinView.setLineColor(getResources().getColor(R.color.colorRed)) ;
            }
        }

        if (view == button_goBack) {
            String password = editText_password.getText().toString().trim() ;
            if (password.length() < 6) {
                textView_password_err.setVisibility(View.VISIBLE) ;
            }
            else {
                changePass(password) ;
            }
        }

    }

    private void checkEmail() {
        double d = Math.random() ;
        random = (int) (d * 100000) ;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, app.BASE_URL + "forget_pass.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response) ;
                    String message = jsonObject.getString("message") ;

                    if (message.equals("email_notExists")) {
                        app.failedT(getString(R.string.toast_emailNotExists)) ;
                    }

                    if (message.equals("email_ok")) {
                        layout_email.setVisibility(View.GONE) ;
                        button_continue.setVisibility(View.GONE) ;

                        layout_pinView.setVisibility(View.VISIBLE) ;
                        button_checkCode.setVisibility(View.VISIBLE) ;
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

    private void changePass(String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, app.BASE_URL + "change_pass.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response) ;

                    String message = jsonObject.getString("message") ;
                    if (message.equals("OK")) {
                        app.successT(getString(R.string.toast_password_changed)); ;
                        finish() ;
                    }
                    if (message.equals("FAILED")) {
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

        requestQueue.add(stringRequest );
    }

}