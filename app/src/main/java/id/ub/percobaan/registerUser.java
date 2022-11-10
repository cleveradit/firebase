package id.ub.percobaan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class registerUser extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private TextView registerUser;
    private EditText txtNama, txtUmur, txtEmail, txtPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        txtNama = (EditText) findViewById(R.id.txtNama);
        txtUmur = (EditText) findViewById(R.id.txtUmur);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registerUser:
                registerUser();
        }
    }

    private void registerUser() {
        String email = txtEmail.getText().toString().trim();
        String umur = txtUmur.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        String nama = txtNama.getText().toString().trim();

        if(nama.isEmpty()){
            txtNama.setError("Nama perlu diisi!");
            txtNama.requestFocus();
            return;
        }
        if(umur.isEmpty()){
            txtUmur.setError("Umur perlu diisi!");
            txtUmur.requestFocus();
            return;
        }
        if(email.isEmpty()){
            txtEmail.setError("Email perlu diisi!");
            txtEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtEmail.setError("Masukan Email yang Valid!");
            txtEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            txtNama.setError("Nama perlu diisi!");
            txtNama.requestFocus();
            return;
        }
        if(password.length() < 6){
            txtPassword.setError("Minimum password 6 karakter!");
            txtPassword.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            User user = new User(nama, umur, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(registerUser.this, "Registrasi Sukses!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);


                                    }else{
                                        Toast.makeText(registerUser.this, "Registrasi Gagal!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(registerUser.this, "Registrasi Gagal!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });


    }
}