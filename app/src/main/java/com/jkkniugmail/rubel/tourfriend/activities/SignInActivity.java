package com.jkkniugmail.rubel.tourfriend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jkkniugmail.rubel.tourfriend.Constants;
import com.jkkniugmail.rubel.tourfriend.R;
import com.jkkniugmail.rubel.tourfriend.databases.DatabaseManager;
import com.jkkniugmail.rubel.tourfriend.models.User;
import com.jkkniugmail.rubel.tourfriend.utils.MySharedPreferences;
import com.jkkniugmail.rubel.tourfriend.utils.Validation;

public class SignInActivity extends AppCompatActivity {

    private int sign_activity_flag;
    private SignViewHolder viewHolder;
    private DatabaseManager manager;
    private MySharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int user_id;
        super.onCreate(savedInstanceState);
        preferences = new MySharedPreferences(this);
        user_id = preferences.getUser();
        if(user_id != -1){
            signOk(user_id);
        }

        else {
            Intent splash = new Intent(this, Splash.class);
            startActivityForResult(splash, 0);

            manager = new DatabaseManager(this);
            setContentView(R.layout.activity_sign_in);
            viewHolder = new SignViewHolder();
            sign_activity_flag = Constants.SING_IN_ACTIVITY;
            setActivityView();
            viewHolder.sign_ok_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sign_activity_flag == Constants.SING_IN_ACTIVITY) {
                        signIn();
                    } else if (sign_activity_flag == Constants.SING_UP_ACTIVITY) {
                        signUp();
                    }
                }
            });

            viewHolder.sign_cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            viewHolder.sign_switch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchActivity();
                }
            });
        }


    }

    void resetViewHolder(){
        viewHolder.first_name_et.setText("");
        viewHolder.last_name_et.setText("");
        viewHolder.phone_no_et.setText("");
        viewHolder.email_et.setText("");
        viewHolder.password_et.setText("");
        viewHolder.re_password_et.setText("");
    }

    public void signOk(int user_id){
        Intent intent = new Intent(this, BaseActivity.class);
        intent.putExtra(Constants.USER_ID, user_id);
        preferences.saveUser(user_id);
        startActivity(intent);
        finish();
    }

    public void signIn() {
        Validation v = new Validation();
        String email = viewHolder.email_et.getText().toString();
        String password = viewHolder.password_et.getText().toString();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            if (v.isValidEmail(email) && v.isValidPassword(password)) {

                int user_id = manager.userLogin(email, password);
                if (user_id > 0) {
                    signOk(user_id);
                } else {
                    viewHolder.email_et.setText("");
                    viewHolder.password_et.setText("");
                    Toast.makeText(this, "Email or password is not correct", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (!v.isValidEmail(email))
                    viewHolder.email_et.setError(getString(R.string.error_invalid_email));
                if ((!v.isValidPassword(password)))
                    viewHolder.password_et.setError(getString(R.string.error_invalid_password));
            }
        } else {
            if (TextUtils.isEmpty(email))
                viewHolder.email_et.setError(getString(R.string.error_field_required));
            if (TextUtils.isEmpty(password))
                viewHolder.password_et.setError(getString(R.string.error_field_required));
        }
    }

    public void signUp() {
        Validation v = new Validation();
        String first_name = viewHolder.first_name_et.getText().toString();
        String last_name = viewHolder.last_name_et.getText().toString();
        String phone_no = viewHolder.phone_no_et.getText().toString();
        String email = viewHolder.email_et.getText().toString();
        String password = viewHolder.password_et.getText().toString();
        String re_password = viewHolder.re_password_et.getText().toString();

        if (!TextUtils.isEmpty(first_name) && !TextUtils.isEmpty(last_name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(phone_no) && !TextUtils.isEmpty(re_password)) {
            if (password.equals(re_password)) {
                if (v.isValidEmail(email) && v.isValidPassword(password) && v.isValidPhone(phone_no)) {
                    User user = new User(first_name, last_name, phone_no, email, password);
                    boolean is_signed_up = manager.addNewUser(user);
                    if (is_signed_up) {
                        Toast.makeText(this, "successfull", Toast.LENGTH_SHORT).show();
                        switchActivity();
                    } else {
                        Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (!v.isValidEmail(email))
                        viewHolder.email_et.setError(getString(R.string.error_invalid_email));
                    if ((!v.isValidPassword(password)))
                        viewHolder.password_et.setError(getString(R.string.error_invalid_password));
                    if (v.isValidPhone(phone_no))
                        viewHolder.phone_no_et.setError(getString(R.string.error_invalid_phone));
                }
            } else {
                viewHolder.re_password_et.setError("password didn't match");
            }

        } else {
            if (TextUtils.isEmpty(first_name))
                viewHolder.first_name_et.setError(getString(R.string.error_field_required));
            if (TextUtils.isEmpty(last_name))
                viewHolder.last_name_et.setError(getString(R.string.error_field_required));
            if (TextUtils.isEmpty(phone_no))
                viewHolder.phone_no_et.setError(getString(R.string.error_field_required));
            if (TextUtils.isEmpty(email))
                viewHolder.email_et.setError(getString(R.string.error_field_required));
            if (TextUtils.isEmpty(password))
                viewHolder.password_et.setError(getString(R.string.error_field_required));
            if (TextUtils.isEmpty(re_password))
                viewHolder.re_password_et.setError(getString(R.string.error_field_required));
        }
    }

    public void switchActivity() {
        if (sign_activity_flag == Constants.SING_UP_ACTIVITY) {
            sign_activity_flag = Constants.SING_IN_ACTIVITY;
        } else if (sign_activity_flag == Constants.SING_IN_ACTIVITY) {
            sign_activity_flag = Constants.SING_UP_ACTIVITY;
        }
        setActivityView();
    }

    public void setActivityView() {
        resetViewHolder();
        if (sign_activity_flag == Constants.SING_IN_ACTIVITY) {
            viewHolder.header.setText("Sign in");
            viewHolder.first_name_et.setVisibility(View.GONE);
            viewHolder.last_name_et.setVisibility(View.GONE);
            viewHolder.phone_no_et.setVisibility(View.GONE);
            viewHolder.re_password_et.setVisibility(View.GONE);
            viewHolder.sign_switch.setText("Sign up now");
            viewHolder.sign_switch_tv.setText("Not Registerd yet?");
        } else if (sign_activity_flag == Constants.SING_UP_ACTIVITY) {
            viewHolder.header.setText("Sign Up");
            viewHolder.first_name_et.setVisibility(View.VISIBLE);
            viewHolder.last_name_et.setVisibility(View.VISIBLE);
            viewHolder.phone_no_et.setVisibility(View.VISIBLE);
            viewHolder.re_password_et.setVisibility(View.VISIBLE);
            viewHolder.sign_switch.setText("Sign in");
            viewHolder.sign_switch_tv.setText("Already signed up?");
        }

    }


    class SignViewHolder {
        private TextView header;
        private EditText first_name_et;
        private EditText last_name_et;
        private EditText phone_no_et;
        private EditText email_et;
        private EditText password_et;
        private EditText re_password_et;
        private TextView sign_switch_tv;
        private TextView sign_switch;
        private Button sign_cancel_btn;
        private Button sign_ok_btn;

        SignViewHolder() {

            header = (TextView) findViewById(R.id.sign_tv);
            first_name_et = (EditText) findViewById(R.id.first_name);
            last_name_et = (EditText) findViewById(R.id.last_name);
            phone_no_et = (EditText) findViewById(R.id.phone_no);
            email_et = (EditText) findViewById(R.id.email);
            password_et = (EditText) findViewById(R.id.password);
            re_password_et = (EditText) findViewById(R.id.re_password);
            sign_switch_tv = (TextView) findViewById(R.id.sign_switch_tv);
            sign_switch = (TextView) findViewById(R.id.sign_switch);
            sign_cancel_btn = (Button) findViewById(R.id.sign_cancel);
            sign_ok_btn = (Button) findViewById(R.id.sign_ok);
        }
    }

}
