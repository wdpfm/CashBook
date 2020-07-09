package app.buu.znsz.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import app.buu.znsz.Model.Expense;
import app.buu.znsz.Utils.DBHandler;

public class ExpenseController {

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    public ExpenseController(Context context) {
        dbhandler = new DBHandler(context);
    }


    public void open() {
        database = dbhandler.getWritableDatabase();
    }

    public void close() {
        dbhandler.close();
    }


    public void addExpense(Expense expense) {           //添加支出
        open();
        ContentValues values = new ContentValues();     //存储机制  ContentValues对象
        values.put("idk", expense.getIdk());
        values.put("description", expense.getDescription());
        values.put("amount", expense.getAmount());
        values.put("date", expense.getDate());
        database.insert("expense", null, values);
        close();
    }

    public void updateExpense(Expense expense) {    //更新支出
        open();
        ContentValues values = new ContentValues();
        values.put("idk", expense.getIdk());
        values.put("description", expense.getDescription());
        values.put("amount", expense.getAmount());
        values.put("date", expense.getDate());
        database.update("expense", values, "id = ?",
                new String[]{String.valueOf(expense.getId())});
        //整型转换字符串 id传进来的expense的id
        close();
    }

    public void deleteExpenseById(int id) {     //删除支出
        open();
        database.delete("expense", "id = ?",
                new String[]{String.valueOf(id)});  //传入的id 转换为字符串
        close();
    }

    public List<Expense> getExpenseByDate(String date) {   //通过日期查询支出（存放在list）
        List<Expense> expenseList = new ArrayList();//定义一个expense列表
        String selectQuery = "SELECT  * FROM expense where date = '" + date + "'";
        open();
        Cursor cursor = database.rawQuery(selectQuery, null);//sql语句原生查询
        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense();
                expense.setId(Integer.parseInt(cursor.getString(0)));
                //返回当前行指定列的值  强制类型转换为整型 存入expense对象
                expense.setIdk(Integer.parseInt(cursor.getString(1)));
                expense.setDescription(cursor.getString(2));
                expense.setAmount(Integer.parseInt(cursor.getString(3)));
                expense.setDate(cursor.getString(4));
                expenseList.add(expense);
                //将expense对象 加入到List
            } while (cursor.moveToNext());
        }
        close();
        return expenseList;
    }

    public String getTotal(String date) {   //返回支出总额
        open();
        String total = "0";
        Cursor cursor = database.rawQuery("SELECT sum(amount) FROM expense where date = '" + date + "'", null);
        if (cursor.getCount() > 0) {   //Cursor 中的行数
            cursor.moveToFirst();//游标移动到首位
            total = cursor.getString(0);
        }
        cursor.close();
        close();
        return total;
    }
}