package app.buu.znsz.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.song.http.QSHttp;
import org.song.http.framework.HttpCallback;
import org.song.http.framework.HttpException;
import org.song.http.framework.ResponseParams;

import app.buu.znsz.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.edtRegUsername)
    EditText edtRegUsername;
    @BindView(R.id.edtRegPassword)
    EditText edtRegPassword;
    @BindView(R.id.edtRegPassword2)
    EditText edtRegPassword2;
    @BindView(R.id.btnRegister)
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnRegister)
    public void onViewClicked() {
        String username = edtRegUsername.getText().toString();
        String password = edtRegPassword.getText().toString();
        String password2 = edtRegPassword2.getText().toString();
        if (username.length() == 0 || password.length() == 0 || password2.length() == 0) {
            Toast.makeText(this, "请输入用户名密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.equals(password2)) {
            String url = "https://api.buu.app/register";
            QSHttp.get(url)
                    .param("username", username)
                    .param("password", password)
                    .buildAndExecute(new HttpCallback() {
                        @Override
                        public void onSuccess(ResponseParams response) {
                            Log.d("TAG", "onSuccess: " + response.string());
                            switch (response.string()) {
                                case "0":
                                    Toast.makeText(RegisterActivity.this, "用户已存在", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(RegisterActivity.this, "注册成功，用户id：" + response.string(), Toast.LENGTH_SHORT).show();
                                    finish();
                                    break;
                            }
                            response.string();
                        }

                        @Override
                        public void onFailure(HttpException e) {
                            Log.d("TAG", "onFailure: " + e.getMessage());
                        }
                    });
        } else {
            Toast.makeText(this, "两次密码输入不一致!", Toast.LENGTH_SHORT).show();
        }
    }
}