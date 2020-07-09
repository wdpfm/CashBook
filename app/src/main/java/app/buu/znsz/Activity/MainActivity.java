package app.buu.znsz.Activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.tencent.mmkv.MMKV;

import org.song.http.QSHttp;
import org.song.http.framework.HttpCallback;
import org.song.http.framework.HttpException;
import org.song.http.framework.ResponseParams;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import app.buu.znsz.Controller.ExpenseController;
import app.buu.znsz.Controller.BalanceController;
import app.buu.znsz.Model.Expense;
import app.buu.znsz.Model.Balance;
import app.buu.znsz.R;
import app.buu.znsz.Utils.DatePicker;
import app.buu.znsz.Utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btnAddexp)
    Button btnAddexp;
    @BindView(R.id.info)
    TextView info;
    @BindView(R.id.currentBalance)
    TextView currentBalance;
    @BindView(R.id.btnAddbal)
    Button btnAddbal;
    @BindView(R.id.outList)
    ListView outList;
    @BindView(R.id.todayOut)
    TextView todayOut;
    @BindView(R.id.tgl)
    Button tgl;
    @BindView(R.id.btnShowByDate)
    Button btnShowByDate;
    @BindView(R.id.txtShowUser)
    TextView txtShowUser;
    Expense selectedExpense;
    ArrayAdapter<Expense> adapter;
    List<Expense> expensesList;
    View mView;
    EditText inputAmount, inputDesc;
    Spinner spinCategory;
    MMKV kv = MMKV.defaultMMKV();//获取MMKV实例
    int uid = kv.decodeInt("uid");
    String username = kv.decodeString("username");
    private BalanceController balance;
    private ExpenseController expense;
    int SELECTED_ID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tgl.setText(Utils.getDateNow());
        setMyBalance();
    }

    @Override
    protected void onResume() {//重写onresume方法
        super.onResume();
        setMyBalance();
        balance = new BalanceController(this);
        expense = new ExpenseController(this);  //声明控制器
        expensesList = expense.getExpenseByDate(tgl.getText().toString());//通过日期查询支出数据 返回一个list
        setListAdapter(expensesList);//设置适配器
        outList.setAdapter(adapter);//绑定适配器
        txtShowUser.setText("用户id:" + uid + "   用户名:" + username + "   ");
        if (expense.getTotal(tgl.getText().toString())==null){
            todayOut.setText("0￥");
        }else{
            todayOut.setText(expense.getTotal(tgl.getText().toString())+"￥");
        }
        outList.setOnItemClickListener(new AdapterView.OnItemClickListener() {//监听listview点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedExpense = (Expense) parent.getItemAtPosition(position);//点击的对象赋给selectedExpense
                SELECTED_ID = selectedExpense.getId();
                menuItemDialog();//调用对话框方法
            }
        });
        LinearLayout lineout = (LinearLayout) findViewById(R.id.emptyview);
        outList.setEmptyView(lineout);
        //在ListView数据为空，调用setEmptyView方法为ListView指定提示视图

    }
    private void setMyBalance() {
        balance = new BalanceController(this);
        currentBalance.setText(balance.getBalance(uid)+"￥");  //通过控制器获取余额
    }

    @OnClick({R.id.btnAddexp, R.id.btnAddbal, R.id.prev, R.id.next, R.id.tgl, R.id.btnShowByDate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tgl:
                showDatePicker();
                break;
            case R.id.btnAddexp:
                launchAddExp();
                break;
            case R.id.btnAddbal:
                launchAddBal();
                break;
            case R.id.btnShowByDate:
                launchShowByDate();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuUpload:
                String url = "https://api.buu.app/upload";
                QSHttp.get(url)
                        .param("uid", uid)
                        .param("balance", balance.getBalance(uid))
                        .buildAndExecute(new HttpCallback() {
                            @Override
                            public void onSuccess(ResponseParams response) {
                                Log.d("TAG", "onSuccess: " + response.string());
                                switch (response.string()) {
                                    case "0":
                                        Toast.makeText(MainActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "1":
                                        Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
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
            case R.id.menuDownload:
                String url2;
                url2 = "https://api.buu.app/download";
                QSHttp.get(url2)
                        .param("uid", uid)
                        .buildAndExecute(new HttpCallback() {
                            @Override
                            public void onSuccess(ResponseParams response) {
                                Log.d("TAG", "onSuccess: " + response.string());
                                switch (response.string()) {
                                    case "-1":
                                        Toast.makeText(MainActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        balance.updateBalance(new Balance(uid, Integer.parseInt(response.string())));
                                        setMyBalance();
                                        Toast.makeText(MainActivity.this, "同步成功，当前余额:" + response.string(), Toast.LENGTH_SHORT).show();
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
            case R.id.menuLogout:
                kv.removeValueForKey("uid");
                kv.removeValueForKey("username");
                kv.removeValueForKey("first");
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                break;
        }
        return true;
    }

    public void menuItemDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);//定义AlertDialog.Builder对消
        builder.setTitle("详细信息");
        builder.setMessage("id" + selectedExpense.getId() + "\n金额" + selectedExpense.getAmount() + "\n日期" + selectedExpense.getDate());
        builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showFormDialog();
            }
        });
        builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                expense.deleteExpenseById(SELECTED_ID);
                balance.updateBalance(new Balance(uid, Integer.parseInt(balance.getBalance(uid)) + selectedExpense.getAmount()));
                onResume();
            }
        });
        builder.show();
    }

    public void showFormDialog() {
        LayoutInflater mainLayout = LayoutInflater.from(this);//获取这个实例
        mView = mainLayout.inflate(R.layout.dialog_expense, null);
        inputAmount = (EditText) mView.findViewById(R.id.inputJumlah);//金额
        inputDesc = (EditText) mView.findViewById(R.id.inputDesk);//描述
        spinCategory = (Spinner) mView.findViewById(R.id.spinner); //下拉列表
        spinCategory.setSelection(selectedExpense.getIdk());//通过当前选中的分类id 给对话框的分类设置对应的选择值
        inputAmount.setText(String.valueOf(selectedExpense.getAmount()));
        inputDesc.setText(selectedExpense.getDescription());
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);//构建新的对话框
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        if (inputAmount.length() == 0 || inputDesc.length() == 0) {
                            Toast.makeText(MainActivity.this, "输入框的值不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            Expense exp = new Expense();
                            exp.setId(SELECTED_ID);
                            exp.setIdk(spinCategory.getSelectedItemPosition());
                            exp.setDescription(inputDesc.getText().toString());
                            exp.setAmount(Integer.parseInt(inputAmount.getText().toString()));
                            exp.setDate(tgl.getText().toString());
                            expense.updateExpense(exp);//修改支出记录
                            balance.updateBalance(new Balance(uid, changeBalance(exp.getAmount())));//更新余额
                        }
                        onResume();
                    }
                })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });
        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    private int changeBalance(int newAmount) {   //通过支出变化 调整余额
        int oldAmount = selectedExpense.getAmount();
        if (newAmount > oldAmount) {     //修改后的金额   >修改前的金额
            return Integer.parseInt(balance.getBalance(uid)) - (newAmount - oldAmount);
        } else if (newAmount < oldAmount) {
            return Integer.parseInt(balance.getBalance(uid)) + (oldAmount - newAmount);
        } else {
            return Integer.parseInt(balance.getBalance(uid));
        }
    }
    private void showDatePicker() {
        DatePicker date = new DatePicker();//日期选择器
        Calendar calender = Calendar.getInstance();   //获取指定时间
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(onGetDate);
        date.show(getFragmentManager(), "Date Picker");
    }
    DatePickerDialog.OnDateSetListener onGetDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(c.getTime());
            tgl.setText(formattedDate);
            onResume();
        }
    };


    public void setListAdapter(List<Expense> expense) {
        adapter = new ArrayAdapter<Expense>(this, android.R.layout.simple_dropdown_item_1line, expense) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Expense current = getItem(position);
                convertView = getLayoutInflater().inflate(R.layout.item_expense, null, false);   //获取列表下标  current当前位置的一个对象
                TextView txtDes = (TextView) convertView.findViewById(R.id.deskrip); //标题
                TextView txtKat = (TextView) convertView.findViewById(R.id.namakat);//分类
                TextView txtJml = (TextView) convertView.findViewById(R.id.jumlah);  //钱
                String[] ExpClass = getResources().getStringArray(R.array.expClass);//定义分类字符串数组
                txtDes.setText(current.getDescription());
                txtKat.setText(ExpClass[current.getIdk()]);
                txtJml.setText(current.getAmount()+"￥");
                return convertView;
            }
        };
    }



    public void launchAddExp() {   //点击“新增支出”按钮   先intent传值 再页面跳转
        Intent intent = new Intent();
        intent.putExtra("date", tgl.getText().toString());
        intent.setClass(MainActivity.this, AddExpActivity.class);
        startActivity(intent);
    }

    public void launchAddBal() {
        Intent i = new Intent(MainActivity.this, AddBalActivity.class);
        startActivity(i);
    }

    public void launchShowByDate() {
        Intent i = new Intent(MainActivity.this, ShowByDateActivity.class);
        startActivity(i);
    }


}