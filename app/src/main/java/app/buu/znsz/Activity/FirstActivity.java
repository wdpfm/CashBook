package app.buu.znsz.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.mmkv.MMKV;

import app.buu.znsz.Controller.BalanceController;
import app.buu.znsz.Model.Balance;
import app.buu.znsz.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FirstActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.DefaultBalance)
    EditText DefaultBalance;
    @BindView(R.id.btnFirst)
    Button btnFirst;
    @BindView(R.id.txtFirstShow)
    TextView txtFirstShow;

    MMKV kv = MMKV.defaultMMKV();//获取MMKV实例
    int uid = kv.decodeInt("uid");
    String username = kv.decodeString("username");

    private BalanceController balance;//定义balance对象 作为控制器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_setting);
        ButterKnife.bind(this);//绑定视图
        balance = new BalanceController(this);
        boolean first = kv.containsKey("first");
        txtFirstShow.setText("当前用户id:" + uid + ",用户名:" + username);
        //判断用户是否第一次打开app
        if (!first) {
            balance.addBalance(new Balance(uid, 0));//新增数据
        } else {
            launchMain();//跳转首页
        }
    }

    @OnClick(R.id.btnFirst)
    public void onViewClicked() {
        if (DefaultBalance.length() == 0) {
            Toast.makeText(this, "输入框的值不能为空", Toast.LENGTH_SHORT).show();
        } else {
            balance.updateBalance(new Balance(uid, Integer.parseInt(DefaultBalance.getText().toString())));
            kv.encode("first", true);//编码 插入
            launchMain();
        }
    }

    public void launchMain() {
        Intent i = new Intent(FirstActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}