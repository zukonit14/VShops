package com.example.vshops;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    private EditText mEmailEditText,mPasswordEditText,mConfirmPasswordEditText;
    private Button mSignUpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mEmailEditText=findViewById(R.id.email_edit_text_sign_up);

        mPasswordEditText=findViewById(R.id.password_edit_text_sign_up);
        mPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        mConfirmPasswordEditText=findViewById(R.id.confirm_password_edit_text_sign_up);
        mConfirmPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        mSignUpButton=findViewById(R.id.sign_up_button_sign_up);


        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((Patterns.EMAIL_ADDRESS.matcher(mEmailEditText.getText()).matches())&&
                        (mPasswordEditText.getText().toString().equals(mConfirmPasswordEditText.getText().toString()))&&
                        (mPasswordEditText.getText().toString().trim().isEmpty()==false)
                )
                {
                    Intent intent = new Intent(SignUpActivity.this,NewUserDetailsActivity.class);
                    intent.putExtra("EmailID",mEmailEditText.getText().toString());
                    intent.putExtra("password",mPasswordEditText.getText().toString());
                    startActivity(intent);

                }
                else
                {
                    Toast.makeText(SignUpActivity.this,"Wrong(or blank) Username or password!!",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}