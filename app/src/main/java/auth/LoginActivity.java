package auth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.dbxlab.vijjub.mirumy.adapters.User;

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
public class LoginActivity extends Activity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputUsername;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQliteManager db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputUsername = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQliteManager(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String username = inputUsername.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!username.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(username, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

    }


    private void checkLogin(final String username, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        Map<String, String> params = new HashMap<String, String>();
        params.put("username",username);
        params.put("password", password);

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                AppConfig.URL_LOGIN,new JSONObject(params), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = response;

                    // Check for error node in json
                    if (jObj.has("key")) {
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String token = jObj.getString("key");
                        getUserDetails(token);

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

                        if(jObj.has("non_field_errors")) {

                            // Error occurred in registration. Get the error
                            // message
                            String errorMsg = jObj.getString("non_field_errors");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(),"Invalid Username/Password",Toast.LENGTH_LONG).show();
                        }



                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
                Log.e(TAG, "Login Error: " + error.getMessage());
               // Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
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

    private void getUserDetails(final String token){

        // Tag used to cancel the request
        String tag_string_req = "req_details";

        pDialog.setMessage("Loading details from the server ...");
        showDialog();



        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                AppConfig.URL_GET_DETAILS,null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = response;

                    // Check for error node in json
                    if (jObj.has("first_name")) {

                        // Now store the user in SQLite
                        String fname = jObj.getString("username");
                        String email = jObj.getString("email");

                        db.addCompleteDetails(jObj,token);
                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();


                        Toast.makeText(getApplicationContext(),"Details: "+fname+" "+email,Toast.LENGTH_LONG).show();
                        Log.i("Details %s",response.toString());
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

                        if(jObj.has("non_field_errors")) {

                            // Error occurred in registration. Get the error
                            // message
                            String errorMsg = jObj.getString("non_field_errors");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(),"Invalid Username/Password",Toast.LENGTH_LONG).show();
                        }



                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
                Log.e(TAG, "Login Error: " + error.getMessage());
                // Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Token "+token);
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
}
