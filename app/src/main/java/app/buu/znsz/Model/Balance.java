package app.buu.znsz.Model;


//定义Balance实体类
public class Balance {

    int id;
    int balance;      //余额

    public Balance() {
    }

    public Balance(int id, int balance) {
        this.id = id;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
