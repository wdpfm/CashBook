package app.buu.znsz.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import app.buu.znsz.Model.Balance;
import app.buu.znsz.Utils.DBHandler;

public class BalanceController {
    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    public BalanceController(Context context) {
        dbhandler = new DBHandler(context);
    }


    public void open() {
        database = dbhandler.getWritableDatabase();
    }

    public void close() {
        dbhandler.close();
    }


    public void addBalance(Balance balance) {   //添加余额
        open();
        ContentValues values = new ContentValues();  //存储机制  ContentValues对象
        values.put("id", balance.getId());
        values.put("amount", balance.getBalance());
        database.insert("balance", null, values);
        close();
    }

    public void updateBalance(Balance balance) {  //更新余额
        open();
        ContentValues values = new ContentValues();
        values.put("amount", balance.getBalance());
        database.update("balance", values, "id = ?",
                new String[]{String.valueOf(balance.getId())});//整型转换字符串 balance传进来的id
        close();
    }


    public String getBalance(int id) {  //获取余额
        open();
        String newBalance = "";
        String selectQuery = "SELECT * FROM balance where id=" + id;//sql语句原生查询
        Cursor cursor = database.rawQuery(selectQuery, null);  //游标 光标   每一行的数据集合
        if (cursor.getCount() > 0) {  //Cursor 中的行数
            cursor.moveToFirst();
            newBalance = cursor.getString(1);//getString(int columnIndex)返回当前行指定列的值  余额
        }
        cursor.close();
        close();
        return newBalance;
    }
}
