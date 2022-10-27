package com.example.chat_gps2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button logIn;
    TextView signUp;
    TextView forgot;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    EditText email;
    EditText pass;




    protected void onStart(){
        super.onStart();
        // Khi bắt đầu khởi tạo
        // Trước khi mở trang logIn kiểm tra xem người dùng đã đăng nhập hay chưa nếu đăng nhập rồi
        // Thì start MainActivity
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            Intent intent = new Intent(LoginActivity.this , com.example.chat_gps2.MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //Tham chiếu
    private void findID(){
        logIn = findViewById(R.id.Button_LogIn);
        signUp = findViewById(R.id.TextView_LogIn_orSignUp);
        forgot = findViewById(R.id.TextView_ForgotPassword);
        email = findViewById(R.id.EditText_LogIn_Email);
        pass = findViewById(R.id.EditText_LogIn_Passwword);
        auth = FirebaseAuth.getInstance();
    }

    //Chuyển sang MainActivity
    private void logInListener(){
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, com.example.chat_gps2.MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //Click Sign up và chuyển Activity Sign up
    private void sigUpListener(){
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, com.example.chat_gps2.SignUpActivity.class);
                startActivity(intent);
//                finish();
            }
        });
    }

    //Click Quên mật khẩu và chuyển Activity ForgotPassword
    private void forGotListener(){
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, com.example.chat_gps2.ForgotPassword.class);
                startActivity(intent);
//                finish();
            }
        });
    }


    //Kiểm tra sự kiện click login
    private View.OnClickListener logInonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            String txt_email = email.getText().toString();
            String txt_password = pass.getText().toString();

            if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                Toast.makeText(LoginActivity.this, "Yêu cầu nhập đủ", Toast.LENGTH_SHORT).show();
            } else {

                auth.signInWithEmailAndPassword(txt_email, txt_password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this, "Login thành công!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, com.example.chat_gps2.MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Xác minh thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        findID();

        sigUpListener();

        logInListener();

        forGotListener();

        logIn.setOnClickListener(logInonClickListener);



    }
}