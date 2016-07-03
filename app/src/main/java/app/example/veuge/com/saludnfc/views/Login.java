package app.example.veuge.com.saludnfc.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.example.veuge.com.saludnfc.HashMapTransformation;
import app.example.veuge.com.saludnfc.R;
import app.example.veuge.com.saludnfc.Variables;
import app.example.veuge.com.saludnfc.network.PostAsyncTask;

public class Login extends AppCompatActivity {

    private String path = "api/auth";
    private String response, token = "", message, status_code;

    private PostAsyncTask mAuthTask = null;

    private AutoCompleteTextView emailView;
    private EditText passwordView;
    private View progressView;
    private View loginForm;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        emailView = (AutoCompleteTextView) findViewById(R.id.email);

        passwordView = (EditText) findViewById(R.id.password);
        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        button = (Button) findViewById(R.id.email_sign_in_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        loginForm = findViewById(R.id.login_form);
        progressView = findViewById(R.id.login_progress);
    }


    /**
     * Attempts to sign in or register the account specified by the activity_login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual activity_login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        emailView.setError(null);
        passwordView.setError(null);

        // Store values at the time of the activity_login attempt.
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt activity_login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user activity_login attempt.
            showProgress(true);
            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);
            String url = ((Variables) this.getApplication()).getUrl();

            mAuthTask = new PostAsyncTask(url, path, token);

            List<NameValuePair> credentials = new ArrayList<NameValuePair>(2);
            credentials.add(new BasicNameValuePair("email", email));
            credentials.add(new BasicNameValuePair("password", password));
            try{
                mAuthTask.execute(credentials);
                response = mAuthTask.get();
                evaluateResponse(response);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void evaluateResponse(String response) {
        HashMapTransformation hmt = new HashMapTransformation(null);
        try {
            JSONArray x = hmt.getJsonFromString(response);
            HashMap login = hmt.buildLoginHashmap(x);

            if(login.containsKey(2)){
                message = login.get(1).toString();
                status_code = login.get(2).toString();
                Log.i("evaluateResponse", "LOGIN FAILED " + message + " " + status_code);
                loginFailed();
            }
            else {
                token = login.get(1).toString();
                Log.i("evaluateResponse", "LOGIN SUCCESS " + token);
                loginSuccess();
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void loginSuccess(){
        mAuthTask = null;
        showProgress(false);

        Log.i("evaluateResponse", "Login successful");
        //finish();
        Intent intent = new Intent(Login.this, PatientsList.class);
        ((Variables) this.getApplication()).setToken(token);
        //intent.putExtra("token", token);
        startActivity(intent);
    }

    public void loginFailed(){
        mAuthTask = null;
        showProgress(false);

        Log.i("evaluateResponse", "Login failed");
        passwordView.setError(getString(R.string.error_incorrect_password));
        passwordView.requestFocus();
    }


    /**
     * Validation rules
     */
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the activity_login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
            loginForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(Login.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        emailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
}
