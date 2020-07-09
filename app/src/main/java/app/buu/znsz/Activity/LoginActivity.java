package app.buu.znsz.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.mmkv.MMKV;

import org.song.http.QSHttp;
import org.song.http.framework.HttpCallback;
import org.song.http.framework.HttpException;
import org.song.http.framework.ResponseParams;

import app.buu.znsz.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.edtLogUsername)
    EditText edtLogUsername;
    @BindView(R.id.edtLogPassword)
    EditText edtLogPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnGoReg)
    Button btnGoReg;
    MMKV kv = MMKV.defaultMMKV();//获取MMKV实例
    String username = kv.decodeString("username");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (username != null) {
            Intent intent = new Intent(LoginActivity.this, FirstActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @OnClick({R.id.btnLogin, R.id.btnGoReg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                String username = edtLogUsername.getText().toString();
                String password = edtLogPassword.getText().toString();
                if (username.length() == 0 || password.length() == 0) {
                    Toast.makeText(this, "请输入用户名密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                String url = "https://api.buu.app/login";
                QSHttp.get(url)
                        .param("username", username)
                        .param("password", password)
                        .buildAndExecute(new HttpCallback() {
                            @Override
                            public void onSuccess(ResponseParams response) {
                                //Log.d("TAG", "onSuccess: " + response.string());
                                switch (response.string()) {
                                    case "0":
                                        Toast.makeText(LoginActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "-1":
                                        Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(LoginActivity.this, "登陆成功，用户id：" + response.string(), Toast.LENGTH_SHORT).show();
                                        kv.encode("uid", Integer.parseInt(response.string()));
                                        kv.encode("username", username);   //登录就存储
                                        Intent intent = new Intent(LoginActivity.this, FirstActivity.class);
                                        startActivity(intent);
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
                break;
            case R.id.btnGoReg:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}