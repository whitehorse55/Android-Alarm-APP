package com.example.wangl_000.music_project.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wangl_000.music_project.R;
import com.example.wangl_000.music_project.UiUtils.Constants;
import com.example.wangl_000.music_project.UiUtils.SharedPrefUtil;
import com.example.wangl_000.music_project.UiUtils.UiUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {


    private FirebaseAuth            auth;
    private DatabaseReference       mDatabase;

    private AutoCompleteTextView    mEmailTextView;
    private EditText                mPasswordTextView;

    private View                    mProgressView;
    private View                    mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth                = FirebaseAuth.getInstance();
        mDatabase           = FirebaseDatabase.getInstance().getReference();

        mEmailTextView      = (AutoCompleteTextView)findViewById(R.id.email);
        mPasswordTextView   = (EditText)findViewById(R.id.password);
        mProgressView       = findViewById(R.id.login_progress);
        mLoginFormView      = findViewById(R.id.login_form);

        mPasswordTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == R.id.login || i == EditorInfo.IME_NULL)
                {
                    validateLogin();
                    return true;
                }
                return false;
            }
        });

        Button signin_button = (Button)findViewById(R.id.email_signin_button);
        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateLogin();
            }
        });


    }

    private boolean isEmailVaild(String email)
    {
        return email.contains("@");
    }

    private boolean isPasswordVaild(String password)
    {
        return password.length() > 5;
    }

    private void validateLogin()
    {
        mEmailTextView.setError(null);
        mPasswordTextView.setError(null);

        String email = mEmailTextView.getText().toString();
        String password = mPasswordTextView.getText().toString();

        View focusView = null;
        boolean cancel = false;

        if (!isPasswordVaild(password))
        {
            mPasswordTextView.setError("This Password is too short");
            focusView = mPasswordTextView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email))
        {
            mEmailTextView.setError("This field is required");
            focusView = mEmailTextView;
            cancel = true;
        }
        else if (!isEmailVaild(email))
        {
            mEmailTextView.setError("This email is invaild");
            focusView = mEmailTextView;
            cancel = true;

        }

        if (cancel)
        {
            focusView.requestFocus();
        }
        else
        {
            showProgress(true);
            firebaseSignIn(email, password);

        }
    }


    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    private void firebaseSignIn(String email, String password){
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        showProgress(false);
                        if (!task.isSuccessful()) {
                        } else {
                            signInSuccess(task.getResult().getUser());
                        }
                    }
                })
                .addOnFailureListener(LoginActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("login fail", e.getMessage());
                        UiUtils.showMessageDialog(LoginActivity.this, "Oops!", "Authentication failed, try again later", "OK");
                    }
                });
    }

    private void signInSuccess(final FirebaseUser user) {
        if (!user.isEmailVerified()) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Verify Email");
            alertDialog.setMessage("Sorry. Your email address has not yet been verified. Do you want us to send another verification email?");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            user.sendEmailVerification();
                            dialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        } else {
            SharedPrefUtil.saveBoolean(this, Constants.PREF_LOGINSTATE, true);
            UiUtils.showShortToast("Login Success");
//            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(i);
//            finish();
        }
    }


}
