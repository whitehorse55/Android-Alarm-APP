package com.example.wangl_000.music_project.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.wangl_000.music_project.Model.UserModel;
import com.example.wangl_000.music_project.R;
import com.example.wangl_000.music_project.UiUtils.UiUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth            mAuth;
    private DatabaseReference       mDatabase;

    private AutoCompleteTextView    mUserNameAV, mUserEmailAV, mUserPassAV;
    private View                    mProgressView;
    private LinearLayout            mSignupForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth           = FirebaseAuth.getInstance();
        mDatabase       = FirebaseDatabase.getInstance().getReference();

        mSignupForm     = (LinearLayout)findViewById(R.id.singup_form);
        mProgressView   = (ProgressBar)findViewById(R.id.singup_progress);
        mUserEmailAV    = (AutoCompleteTextView)findViewById(R.id.singup_email);
        mUserNameAV     = (AutoCompleteTextView)findViewById(R.id.singup_name);
        mUserPassAV     = (AutoCompleteTextView)findViewById(R.id.singup_password);

        Button signup_button = (Button)findViewById(R.id.singup_button);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidate())
                {
                    attempSignup();
                }
            }
        });
    }

    private void attempSignup()
    {
        showProgress(true);
        String email        = mUserEmailAV.getText().toString();
        String password     = mUserPassAV.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                UiUtils.showShortToast("Completed");
                showProgress(false);
                if (!task.isSuccessful())
                {
                    UiUtils.showMessageDialog(SignupActivity.this, "","Signup Failed","OK");
                }
                else
                {
                    onAuthSuccess(task.getResult().getUser());
                }
            }
        }).addOnFailureListener(SignupActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                UiUtils.showMessageDialog(SignupActivity.this, "","Sign up failed try it later again","OK");
            }
        });

    }

    private void onAuthSuccess(final FirebaseUser user)
    {
        String str_name = mUserNameAV.getText().toString();
        String str_pass = mUserPassAV.getText().toString();
        String str_emali = user.getEmail();

        UserModel  model = new UserModel(str_emali,str_pass,str_name);

        mDatabase.child("users").child(user.getUid()).setValue(model);

        user.sendEmailVerification().addOnCompleteListener(SignupActivity.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                AlertDialog alertDialog = new AlertDialog.Builder(SignupActivity.this).create();
                alertDialog.setTitle("Verify Email");
                alertDialog.setMessage("Please check your email to verify");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                                finish();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                alertDialog.show();
            }
        });

    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mSignupForm.setVisibility(show ? View.GONE : View.VISIBLE);
            mSignupForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSignupForm.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mSignupForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private boolean checkValidate()
    {
        if (mUserNameAV.getText().toString().equals(""))
        {
            mUserNameAV.setError("Invalid User Name!");
            mUserNameAV.requestFocus();
            return false;
        }
        if (mUserPassAV.getText().toString().length() < 6)
        {
            mUserPassAV.setError("Invalid Password!");
            mUserPassAV.requestFocus();
            return false;
        }
        if (!mUserEmailAV.getText().toString().contains("@"))
        {
            mUserEmailAV.setError("User Email is Invaild");
            mUserEmailAV.requestFocus();
            return false;
        }
        return true;
    }
}
