package com.example.chat_gps2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    EditText email;
    Button but;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.editText2);
        but = findViewById(R.id.button);

        //Gửi Email để khôi phục mật khẩu
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString();

                FirebaseAuth.getInstance().sendPasswordResetEmail(mail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPassword.this, "Kiểm tra hộp thư Email!"
                                            , Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(ForgotPassword.this, "Lỗi"
                                            , Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
}