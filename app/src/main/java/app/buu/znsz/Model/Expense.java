package app.buu.znsz.Model;
//定义expense实体类
public class Expense {
    int id;
    int idk;              //类别对应的id
    String description;   //描述
    int amount;         //数量
    String date;     //日期

    public Expense() {
    }

    public Expense(int id, int idk, String description, int amount, String date) {
        this.id = id;
        this.idk = idk;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdk() {
        return idk;
    }

    public void setIdk(int idk) {
        this.idk = idk;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
