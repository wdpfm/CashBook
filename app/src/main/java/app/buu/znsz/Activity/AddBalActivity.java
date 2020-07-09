package app.buu.znsz.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.tencent.mmkv.MMKV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.buu.znsz.Controller.BalanceController;
import app.buu.znsz.Model.Balance;
import app.buu.znsz.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddBalActivity extends AppCompatActivity {

    @BindView(R.id.edtBal)
    EditText edtBal;
    @BindView(R.id.btnAddBal)
    Button btnAddBal;
    MMKV kv = MMKV.defaultMMKV();//获取MMKV实例
    int uid = kv.decodeInt("uid");
    private BalanceController balance;//定义balance控制器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bal);
        ButterKnife.bind(this);//自动生成 绑定视图
    }

    @OnClick(R.id.btnAddBal)
    public void onViewClicked() {
        String mount = edtBal.getText().toString();
        Pattern p = Pattern.compile("[0-9]*");   //创建一个正则表达式（样式）
        Matcher m = p.matcher(mount);    //（匹配）
        if (mount.length() == 0) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
        } else if (!m.matches()) {    //匹配整个字符串
            AlertDialog alert = new AlertDialog.Builder(AddBalActivity.this).create();
            alert.setTitle("格式错误");    //标题
            alert.setIcon(R.mipmap.tip);   //图标
            alert.setMessage("\n" + "请输入正确的数字作为金额纪录！");
            alert.setButton(AlertDialog.BUTTON_POSITIVE, "好哒！", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alert.show();   //显示警告窗口
        } else {
            balance = new BalanceController(this);
            balance.updateBalance(new Balance(uid, Integer.parseInt(balance.getBalance(uid)) + Integer.parseInt(mount)));
            finish();
        }
    }
}