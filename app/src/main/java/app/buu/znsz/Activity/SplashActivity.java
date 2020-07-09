package app.buu.znsz.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import app.buu.znsz.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.splash_btn_skip)
    Button splashBtnSkip;

    private Handler handler = new Handler();
    // 通过Handler启动线程 两秒后进入闪屏
    private Runnable runnableToLogin = new Runnable() {
        @Override
        public void run() {
            toLoginActivity();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        handler.postDelayed(runnableToLogin, 2000);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();//隐藏标题栏
        }
    }

    @OnClick(R.id.splash_btn_skip)
    public void onViewClicked() {
        //防止LoginActivity被打开两次
        handler.removeCallbacks(runnableToLogin);
        toLoginActivity();
    }

    private void toLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //防止内存泄漏
        handler.removeCallbacks(runnableToLogin);
    }

}