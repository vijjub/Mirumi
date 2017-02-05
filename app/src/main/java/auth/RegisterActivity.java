package auth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.dbxlab.vijjub.mirumy.MainActivity;
import com.dbxlab.vijjub.mirumy.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;
import helper.SQliteManager;
import helper.SessionManager;

/**
 * Created by vijjub on 7/25/16.
 */
public class RegisterActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputUserName;
    private EditText inputEmail;
    private EditText inputPassword,inputConfirmPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQliteManager db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputEmail = (EditText) findViewById(R.id.email);
        inputUserName = (EditText) findViewById(R.id.userid);
        inputPassword = (EditText) findViewById(R.id.password);
        inputConfirmPassword = (EditText) findViewById(R.id.confirmpassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQliteManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                String email = inputEmail.getText().toString().trim();
                String username = inputUserName.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String cpassword = inputConfirmPassword.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty() && !cpassword.isEmpty() && !username.isEmpty()) {
                    if(isValidEmail(email)){
                        if(password.equals(cpassword))
                            registerUser(email,username, password,cpassword);
                        else {Toast.makeText(getApplicationContext(),"Passwords Do not match",Toast.LENGTH_LONG).show();}
                    }else {
                        Toast.makeText(getApplicationContext(),"Invalid Email Address",Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */

    private void registerUser( final String email,final String username,final String password,final String cpassword) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("username",username);
        params.put("password1", password);
        params.put("password2",cpassword);

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                AppConfig.URL_REGISTER,new JSONObject(params), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = response;
                   // String key = jObj.getString("key");
                    //boolean error = jObj.getBoolean("error");
                    if (jObj.has("key")) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String key = jObj.getString("key");

                        // Inserting row in users table
                        //db.addUser(email,username);//db.addUser(name, email, uid);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!"+key, Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if(jObj.has("username")) {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("username");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }

                    if(jObj.has("email")){
                        String errorMsg = jObj.getString("email");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse nresponse = error.networkResponse;
                if (error instanceof ServerError && nresponse != null) {
                    try {
                        String res = new String(nresponse.data);
                        // Now you can use any deserializer to make sense of data
                        JSONObject jObj = new JSONObject(res);
                        Log.i("StartActivity %s", res.toString());

                        if(jObj.has("username")) {

                            // Error occurred in registration. Get the error
                            // message
                            String errorMsg = jObj.getString("username");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }

                        if(jObj.has("email")){
                            String errorMsg = jObj.getString("email");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();

                        }

                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }

                Log.e(TAG, "Registration Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public final static boolean isValidEmail(CharSequence email){
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

}
