package app.buu.znsz.Activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import app.buu.znsz.Controller.ExpenseController;
import app.buu.znsz.Model.Expense;
import app.buu.znsz.R;
import app.buu.znsz.Utils.DatePicker;
import app.buu.znsz.Utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowByDateActivity extends AppCompatActivity {
    @BindView(R.id.tgl)
    Button tgl;
    @BindView(R.id.outList)
    ListView outList;
    @BindView(R.id.todayOutByDate)
    TextView todayOut;
    @BindView(R.id.scategory)
    Spinner category;
    List<Expense> Expenses = new ArrayList<Expense>();//支出列表
    List<Expense> ExpensesByCategory;//分类后的支出列表
    ArrayAdapter<Expense> adapter;
    private ExpenseController expense;//定义expense对象 作为控制器控制支出

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_by_date);
        ButterKnife.bind(this);//自动生成 绑定视图
        tgl.setText(Utils.getDateNow());   //utils工具类  显示时间
        expense = new ExpenseController(this);
        Expenses = expense.getExpenseByDate(tgl.getText().toString());//按日期查询支出记录
        setListAdapter(Expenses);//设置适配器
        changeTodayOut(Expenses);  //修改今日支出
        outList.setAdapter(adapter);  //绑定适配器
        LinearLayout lineout = (LinearLayout) findViewById(R.id.emptyview);
        outList.setEmptyView(lineout);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {   //spinner点击事件
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeListByCategory(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Expenses = expense.getExpenseByDate(tgl.getText().toString());//按日期查询支出记录
        setListAdapter(Expenses);//设置适配器
        changeTodayOut(Expenses);  //修改今日支出
        outList.setAdapter(adapter);  //绑定适配器
    }

    @OnClick(R.id.tgl)            //日历
    public void onViewClicked() {
        showDatePicker();
    }
    private void showDatePicker() {
        DatePicker date = new DatePicker();     //时间选择器
        Calendar calender = Calendar.getInstance();//获取指定时间
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));  //bundle传值
        date.setArguments(args);       //
        date.setCallBack(onGetDate);
        date.show(getFragmentManager(), "Date Picker");   //?????????????????
    }
    DatePickerDialog.OnDateSetListener onGetDate = new DatePickerDialog.OnDateSetListener() {   //回调
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

    public void setListAdapter(List<Expense> expenses) {
        adapter = new ArrayAdapter<Expense>(this, android.R.layout.simple_dropdown_item_1line, expenses) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Expense current = getItem(position);//获取列表下标位置  current当前位置的一个对象
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.item_expense, null, false);//填充布局
                }
                TextView txtDes = (TextView) convertView.findViewById(R.id.deskrip); //标题
                TextView txtKat = (TextView) convertView.findViewById(R.id.namakat);//金额
                TextView txtJml = (TextView) convertView.findViewById(R.id.jumlah);//类别
                String[] ExpClass = getResources().getStringArray(R.array.expClass);//获取字符串数组---spanner
                txtDes.setText(current.getDescription());
                txtKat.setText(ExpClass[current.getIdk()]);//分类id 转换为分类名
                txtJml.setText(current.getAmount()+"￥");
                return convertView;
            }
        };
    }

    public void changeListByCategory(int id) {   //这个id是 用来判断分类id
        if (id>0) {
            id--;//为了和数据库对应
            ExpensesByCategory= new ArrayList<Expense>();
            for (Expense pe : Expenses) {//增强型for循环遍历列表
                if (pe.getIdk() == id) {   //判断分类idk
                    ExpensesByCategory.add(pe);
                }
            }
            setListAdapter(ExpensesByCategory);
            changeTodayOut(ExpensesByCategory);
        } else {   //选择的是“全部”
            setListAdapter(Expenses);
            changeTodayOut(Expenses);
        }
        outList.setAdapter(adapter);
        LinearLayout lineout = (LinearLayout) findViewById(R.id.emptyview);
        outList.setEmptyView(lineout);
    }

    public void changeTodayOut(List<Expense> expenses) {     //左下角的  今日支出
        if (expenses.size() != 0) {
            int total = 0;
            for (Expense pe : expenses) {
                total += pe.getAmount();
            }
            todayOut.setText(total+"￥");//隐式转换字符串
        }else{
            todayOut.setText("0￥");//隐式转换字符串
        }

    }
}