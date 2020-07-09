package app.buu.znsz.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.tencent.mmkv.MMKV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.buu.znsz.Controller.ExpenseController;
import app.buu.znsz.Controller.BalanceController;
import app.buu.znsz.Model.Expense;
import app.buu.znsz.Model.Balance;
import app.buu.znsz.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddExpActivity extends AppCompatActivity {

    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.edtDesc)
    EditText edtDesc;
    @BindView(R.id.edtMount)
    EditText edtMount;
    @BindView(R.id.btnAddExp)
    Button btnAddExp;
    MMKV kv = MMKV.defaultMMKV();//获取MMKV实例
    int uid = kv.decodeInt("uid");
    String Date = "";     //存时间
    private ExpenseController expense;//定义expense对象 作为控制器控制支出
    private BalanceController balance;//定义balance对象 作为控制器控制余额

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exp);
        ButterKnife.bind(this);//自动生成 绑定视图
        Intent intent = getIntent();
        Date = intent.getStringExtra("date");//接受来自“MainActivity”的数据
    }

    @OnClick(R.id.btnAddExp)
    public void onViewClicked() {
        String desc = edtDesc.getText().toString(); //描述
        String mount = edtMount.getText().toString();//支出金额
        Pattern p = Pattern.compile("[0-9]*");//创建一个正则表达式（样式）
        Matcher m = p.matcher(mount);   //（匹配）
        if (desc.length() == 0 || mount.length() == 0) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
        } else if (!m.matches()) {
            AlertDialog alert = new AlertDialog.Builder(AddExpActivity.this).create();
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
            Expense exp = new Expense();
            exp.setIdk(spinner.getSelectedItemPosition());
            exp.setDescription(desc);
            exp.setAmount(Integer.parseInt(mount));//整型
            exp.setDate(Date);
            expense = new ExpenseController(this);
            balance = new BalanceController(this);
            expense.addExpense(exp);
            balance.updateBalance(new Balance(uid, Integer.parseInt(balance.getBalance(uid)) - exp.getAmount()));
            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}