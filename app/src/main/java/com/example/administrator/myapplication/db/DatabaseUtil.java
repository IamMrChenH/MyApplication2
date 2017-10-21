package com.example.administrator.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.administrator.myapplication.CarMessage;
import com.example.administrator.myapplication.MoneyLog;
import com.example.administrator.myapplication.ParkLog;
import com.example.administrator.myapplication.TrafficR;
import com.example.administrator.myapplication.Violation;
import com.example.administrator.myapplication.util.AffMarkSMS;

import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {
    static SQLiteDatabase sqliteDatabase;

    // 获取数据库单实例
    public static SQLiteDatabase getDatabase(Context context) {
        if (sqliteDatabase == null) {
            DatabaseHelper dbHelper = new DatabaseHelper(context, "userDB");
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            sqliteDatabase = dbHelper.getReadableDatabase();
        }
        return sqliteDatabase;
    }

    // 获取数据库同步的单实例
    public synchronized static SQLiteDatabase getDatabaseSync(Context context) {
        if (sqliteDatabase == null) {
            DatabaseHelper dbHelper = new DatabaseHelper(context, "userDB");
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            sqliteDatabase = dbHelper.getReadableDatabase();
        }
        return sqliteDatabase;
    }

    /**
     * 获得停车场费用
     */
    public static String getParkMoney(Context context) {
        String chargeType = null;
        String chargeStandard = null;
        SQLiteDatabase sqliteDatabase = getDatabase(context);
        Cursor cursor = sqliteDatabase.query("Park", null, null,
                null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            chargeType = cursor.getString(cursor.getColumnIndex("chargeType"));
            chargeStandard = cursor.getString(cursor.getColumnIndex("chargeStandard"));
        }
        return chargeType + "," + chargeStandard;
    }

    /**
     * 更新收费模式
     */
    public static void updateParkMoney(Context context, String chargeType, String chargeStandard) {
        SQLiteDatabase sqliteDatabase = getDatabase(context);
        ContentValues values = new ContentValues();
        values.put("chargeType", chargeType);
        values.put("chargeStandard", chargeStandard);
        sqliteDatabase.update("Park", values, null, null);
    }

    /**
     * 获得停车场空位
     */
    public static int getParkFree(Context context) {
        int count = 0;
        String chargeStandard = null;
        SQLiteDatabase sqliteDatabase = getDatabase(context);
        Cursor cursor = sqliteDatabase.query("ParkNow", null, "parkCar=?",
                new String[]{"0"}, null, null, null);
        count = cursor.getCount();
        return count;
    }

    /**
     * 获得当前是否停车
     */
    public static int getParkNow(Context context, String id) {
        int count = 0;
        String chargeStandard = null;
        SQLiteDatabase sqliteDatabase = getDatabase(context);
        Cursor cursor = sqliteDatabase.query("ParkNow", null, "parkCar=?",
                new String[]{id}, null, null, null);
        return count;
    }

    /**
     * 获得小车所有信息
     */
    public static CarMessage getCarMessage(Context context, String id) {
        CarMessage carMessage = new CarMessage();
        String chargeStandard = null;
        SQLiteDatabase sqliteDatabase = getDatabase(context);
        Cursor cursor = sqliteDatabase.query("Car", null, "id=?",
                new String[]{id}, null, null, null);
        if (cursor.moveToFirst()) {
            carMessage.setCardscore(cursor.getString(cursor.getColumnIndex("cardScore")));
            carMessage.setCarNum(cursor.getString(cursor.getColumnIndex("number")));
            carMessage.setCarType(cursor.getString(cursor.getColumnIndex("numberType")));
            carMessage.setCarMoney(cursor.getDouble(cursor.getColumnIndex("carMoney")));
            carMessage.setCarState(cursor.getString(cursor.getColumnIndex("carState")));
            carMessage.setCarName(cursor.getString(cursor.getColumnIndex("carName")));
            carMessage.setCarPhone(cursor.getString(cursor.getColumnIndex("carPhone")));
        }
        return carMessage;
    }

    /**
     * 充值
     */
    public static void updateCarMoney(Context context, String id, double money, int type) {
        SQLiteDatabase sqliteDatabase = getDatabase(context);
        Cursor cursor = sqliteDatabase.query("Car", null, "id=?",
                new String[]{id}, null, null, null);
        double lastMoney = 0.0;
        if (cursor.moveToFirst()) {
            lastMoney = cursor.getDouble(cursor.getColumnIndex("carMoney")) + money;
            ContentValues values = new ContentValues();
            values.put("carMoney", lastMoney);
            //修改条件
            String whereClause = "id=?";
            //修改添加参数
            String[] whereArgs = {id};
            //修改
            sqliteDatabase.update("Car", values, whereClause, whereArgs);
            //插入一条充值记录
            // 创建ContentValues对象
            ContentValues values2 = new ContentValues();
            values2.put("cid", id);
            values2.put("money", money);
            values2.put("time", System.currentTimeMillis());
            if (type == 1) {
                values2.put("mark", "支付宝");
            } else if (type == 2) {
                values2.put("mark", "微信");
            } else {
                values2.put("mark", "其他");
            }
            values2.put("type", 1);
            sqliteDatabase.insert("MoneyLog", null, values2);
        }


    }

    /**
     * 添加停车记录
     */
    public static void addPartLog(Context context, int id, long parkTime, long longTime, String type) {
        SQLiteDatabase sqliteDatabase = getDatabase(context);
        ContentValues values2 = new ContentValues();
        values2.put("parkTime", parkTime);
        values2.put("parkCar", id);
        values2.put("longTime", longTime);
        String costType = type.substring(0, 1);
        double money = Double.valueOf(type.substring(2));
        double num;
        values2.put("type", costType);
        values2.put("cost", money);
        if (type.charAt(0) == '2') {
            num = money;
            values2.put("moneyCast", money);
        } else {
            num = money * (((int) longTime / 3600000) + 1);
            values2.put("moneyCast", num);
        }
        sqliteDatabase.insert("ParkNow", null, values2);

        Cursor cursor = sqliteDatabase.query("Car", null, "id=?",
                new String[]{id + ""}, null, null, null);
        double lastMoney = 0.0;
        boolean flagM = false;//判断用户余额够不够
        if (cursor.moveToFirst()) {
            lastMoney = cursor.getDouble(cursor.getColumnIndex("carMoney"));
            if (lastMoney > money) {
                flagM = true;
                ContentValues values = new ContentValues();
                values.put("carMoney", lastMoney - money);
                //修改条件
                String whereClause = "id=?";
                //修改添加参数
                String[] whereArgs = {id + ""};
                //修改
                sqliteDatabase.update("Car", values, whereClause, whereArgs);
            }

        }


        ContentValues values = new ContentValues();
        values.put("money", num);
        values.put("cid", id);
        values.put("time", System.currentTimeMillis());
        if (flagM) {
            values.put("type", "2");
        } else {
            values.put("type", "3");
        }
        values.put("mark", type);
        sqliteDatabase.insert("MoneyLog", null, values);


    }

    /**
     * 获得违规记录
     */
    public static List<Violation> getV(Context context, String carID) {
        Violation violation;
        List<Violation> violations = new ArrayList<Violation>();
        String chargeType = null;
        String chargeStandard = null;
        SQLiteDatabase sqliteDatabase = getDatabase(context);
        Cursor cursor = sqliteDatabase.query("Rule", null, "carId=?",
                new String[]{carID}, null, null, null);
        while (cursor.moveToNext()) {
            violation = new Violation();
            violation.setCarId(cursor.getString(cursor.getColumnIndex("carId")));
            violation.setId(cursor.getInt(cursor.getColumnIndex("id")));
            violation.setFlag(cursor.getString(cursor.getColumnIndex("flag")));
            violation.setMoney(cursor.getString(cursor.getColumnIndex("money")));
            violation.setOverTime(cursor.getString(cursor.getColumnIndex("overTime")));
            violation.setvTime(cursor.getString(cursor.getColumnIndex("vTime")));
            violation.setScore(cursor.getString(cursor.getColumnIndex("score")));
            violation.setvMsg(cursor.getString(cursor.getColumnIndex("vMsg")));
            violation.setvXY(cursor.getString(cursor.getColumnIndex("vXY")));
            violation.setvOffice(cursor.getString(cursor.getColumnIndex("vOffice")));
            violation.setvID(cursor.getString(cursor.getColumnIndex("vID")));
            violation.setMoneyFlag(cursor.getString(cursor.getColumnIndex("moneyFlag")));
            violation.setPhoto(cursor.getString(cursor.getColumnIndex("photo")));
            violations.add(violation);
        }
        return violations;
    }

    /**
     * 获得所有违规记录
     */
    public static List<Violation> getAllV(Context context) {
        Violation violation;
        List<Violation> violations = new ArrayList<Violation>();
        String chargeType = null;
        String chargeStandard = null;
        SQLiteDatabase sqliteDatabase = getDatabase(context);
        Cursor cursor = sqliteDatabase.query("Rule", null, null,
                null, null, null, null);
        while (cursor.moveToNext()) {
            violation = new Violation();
            violation.setCarId(cursor.getString(cursor.getColumnIndex("carId")));
            violation.setId(cursor.getInt(cursor.getColumnIndex("id")));
            violation.setFlag(cursor.getString(cursor.getColumnIndex("flag")));
            violation.setMoney(cursor.getString(cursor.getColumnIndex("money")));
            violation.setOverTime(cursor.getString(cursor.getColumnIndex("overTime")));
            violation.setvTime(cursor.getString(cursor.getColumnIndex("vTime")));
            violation.setScore(cursor.getString(cursor.getColumnIndex("score")));
            violation.setvMsg(cursor.getString(cursor.getColumnIndex("vMsg")));
            violation.setvXY(cursor.getString(cursor.getColumnIndex("vXY")));
            violation.setvOffice(cursor.getString(cursor.getColumnIndex("vOffice")));
            violation.setvID(cursor.getString(cursor.getColumnIndex("vID")));

            violation.setPhoto(cursor.getString(cursor.getColumnIndex("photo")));
            violation.setMoneyFlag(cursor.getString(cursor.getColumnIndex("moneyFlag")));
            violations.add(violation);
        }
        return violations;
    }

    /**
     * 获得数据字段
     */
    public static List<TrafficR> getTrafficR(Context context) {
        TrafficR trafficR;
        List<TrafficR> trafficrs = new ArrayList<TrafficR>();
        SQLiteDatabase sqliteDatabase = getDatabase(context);
        Cursor cursor = sqliteDatabase.query("TrafficR", null, null,
                null, null, null, null);
        while (cursor.moveToNext()) {
            trafficR = new TrafficR();
            trafficR.setId(cursor.getInt(cursor.getColumnIndex("id")));
            trafficR.setRule(cursor.getString(cursor.getColumnIndex("rule")));
            trafficR.setScore(cursor.getString(cursor.getColumnIndex("score")));
            trafficR.setMoney(cursor.getString(cursor.getColumnIndex("money")));
            trafficrs.add(trafficR);
        }
        return trafficrs;
    }

    /**
     * 获得用户停车记录
     */
    public static List<ParkLog> getParkLog(Context context, String carID) {
        ParkLog parkLog;
        List<ParkLog> parkLogs = new ArrayList<ParkLog>();
        SQLiteDatabase sqliteDatabase = getDatabase(context);
        Cursor cursor = sqliteDatabase.query("ParkNow", null, "parkCar=?",
                new String[]{carID}, null, null, null);
        while (cursor.moveToNext()) {
            parkLog = new ParkLog();
            parkLog.setLongTime(cursor.getInt(cursor.getColumnIndex("longTime")));
            parkLog.setMoneyCast(cursor.getInt(cursor.getColumnIndex("moneyCast")));
            parkLog.setParkCar(cursor.getInt(cursor.getColumnIndex("parkCar")));
            parkLog.setParkTime(cursor.getLong(cursor.getColumnIndex("parkTime")));
            parkLog.setType(cursor.getString(cursor.getColumnIndex("type")));
            parkLog.setCost(cursor.getString(cursor.getColumnIndex("cost")));
            parkLogs.add(parkLog);
        }
        return parkLogs;
    }

    /**
     * 获得所有用户停车记录
     */
    public static List<ParkLog> getAllParkLog(Context context) {
        ParkLog parkLog;
        List<ParkLog> parkLogs = new ArrayList<ParkLog>();
        SQLiteDatabase sqliteDatabase = getDatabase(context);
        Cursor cursor = sqliteDatabase.query("ParkNow", null, null,
                null, null, null, null);
        while (cursor.moveToNext()) {
            parkLog = new ParkLog();
            parkLog.setLongTime(cursor.getInt(cursor.getColumnIndex("longTime")));
            parkLog.setMoneyCast(cursor.getInt(cursor.getColumnIndex("moneyCast")));
            parkLog.setParkCar(cursor.getInt(cursor.getColumnIndex("parkCar")));
            parkLog.setParkTime(cursor.getLong(cursor.getColumnIndex("parkTime")));
            parkLog.setType(cursor.getString(cursor.getColumnIndex("type")));
            parkLog.setCost(cursor.getString(cursor.getColumnIndex("cost")));
            parkLogs.add(parkLog);
        }
        return parkLogs;
    }

    /**
     * 获得用户消费记录
     */
    public static List<MoneyLog> getMoneyLog(Context context, String carID) {
        MoneyLog moneyLog;
        List<MoneyLog> moneyLogs = new ArrayList<MoneyLog>();
        SQLiteDatabase sqliteDatabase = getDatabase(context);
        Cursor cursor = sqliteDatabase.query("MoneyLog", null, "cid=?",
                new String[]{carID}, null, null, null);
        while (cursor.moveToNext()) {
            moneyLog = new MoneyLog();
            moneyLog.setCid(cursor.getInt(cursor.getColumnIndex("cid")));
            moneyLog.setMoney(cursor.getInt(cursor.getColumnIndex("money")));
            moneyLog.setTime(cursor.getLong(cursor.getColumnIndex("time")));
            moneyLog.setType(cursor.getInt(cursor.getColumnIndex("type")));
            moneyLog.setMark(cursor.getString(cursor.getColumnIndex("mark")));
            moneyLogs.add(moneyLog);
        }
        return moneyLogs;
    }

    /**
     * 获得所有用户消费记录
     */
    public static List<MoneyLog> getAllMoneyLog(Context context) {
        MoneyLog moneyLog;
        List<MoneyLog> moneyLogs = new ArrayList<MoneyLog>();
        SQLiteDatabase sqliteDatabase = getDatabase(context);
        Cursor cursor = sqliteDatabase.query("MoneyLog", null, null,
                null, null, null, null);
        while (cursor.moveToNext()) {
            moneyLog = new MoneyLog();
            moneyLog.setCid(cursor.getInt(cursor.getColumnIndex("cid")));
            moneyLog.setMoney(cursor.getInt(cursor.getColumnIndex("money")));
            moneyLog.setTime(cursor.getLong(cursor.getColumnIndex("time")));
            moneyLog.setType(cursor.getInt(cursor.getColumnIndex("type")));
            moneyLog.setMark(cursor.getString(cursor.getColumnIndex("mark")));
            moneyLogs.add(moneyLog);
        }
        return moneyLogs;
    }

    public static String getUCar(Context context, String phone) {
        String id = "";
        SQLiteDatabase sqliteDatabase = getDatabase(context);
        Cursor cursor = sqliteDatabase.query("Car", null, "carPhone=?",
                new String[]{phone}, null, null, null);
        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex("id"));
        }
        return id;
    }


    /**
     * 登录
     */
    public static String getU(Context context, String username, String userpwd) {
        String phone = "";
        String type = "";
        SQLiteDatabase sqliteDatabase = getDatabase(context);
        Cursor cursor = sqliteDatabase.query("User", null, "username=? and userpwd=?",
                new String[]{username, userpwd}, null, null, null);
        if (cursor.moveToNext()) {
            phone = cursor.getString(cursor.getColumnIndex("phone"));
            type = cursor.getString(cursor.getColumnIndex("type"));

        }
        return phone + "," + type;
    }

    /**
     * 插入違規記錄
     */
    public static long insertV(String carId, String vTime, String vMsg, String score, String money, String vXY, String vID, String vOffice,
                               String photo, Context context) {
        // 创建ContentValues对象
        ContentValues values = new ContentValues();
        // 向该对象中插入键值对，其中键是列名，值是希望插入到这一列的值，值必须和数据库当中的数据类型一致
        values.put("carId", carId);
        values.put("vTime", vTime);
        values.put("vMsg", vMsg);
        values.put("vXY", vXY);
        values.put("vID", vID);
        values.put("vOffice", vOffice);
        values.put("score", score);
        values.put("money", money);
        values.put("overTime", "0");
        values.put("photo", photo);
        values.put("flag", "0");
        values.put("moneyFlag", "0");
        SQLiteDatabase sqliteDatabase = getDatabase(context);
        // 调用insert方法，就可以将数据插入到数据库当中
        // 第一个参数:表名称
        // 第二个参数：SQl不允许一个空列，如果ContentValues是空的，那么这一列被明确的指明为NULL值
        // 第三个参数：ContentValues对象                                           v                                           v                                                                                                                                                                                             1
        Log.e("233", "2");
        if (sqliteDatabase.insert("Rule", null, values) != -1) {
            Log.e("233", "1");
            Cursor cursor = sqliteDatabase.query("Car", null, "id=?",
                    new String[]{carId}, null, null, null);
            Log.e("233", "" + carId);
            if (cursor.moveToFirst()) {
                AffMarkSMS sms = new AffMarkSMS();
                sms.execute(cursor.getString(cursor.getColumnIndex("carPhone")), cursor.getString(cursor.getColumnIndex("carName")));
            }

            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 增加用户
     */
    public static long insertU(String username, String userpwd, String phone,
                               Context context) {
        // 创建ContentValues对象
        ContentValues values = new ContentValues();
        // 向该对象中插入键值对，其中键是列名，值是希望插入到这一列的值，值必须和数据库当中的数据类型一致
        values.put("username", username);
        values.put("userpwd", userpwd);
        values.put("phone", phone);
        values.put("type", "2");
        SQLiteDatabase sqliteDatabase = getDatabase(context);
        // 调用insert方法，就可以将数据插入到数据库当中
        // 第一个参数:表名称
        // 第二个参数：SQl不允许一个空列，如果ContentValues是空的，那么这一列被明确的指明为NULL值
        // 第三个参数：ContentValues对象
        return sqliteDatabase.insert("User", null, values);
    }


    /**
     * 找回密码
     */
    public static long forgetU(String username, String newPwd, String phone,
                               Context context) {

        SQLiteDatabase sqliteDatabase = getDatabase(context);
        ContentValues values = new ContentValues();
        values.put("userpwd", newPwd);
        //修改条件
        String whereClause = "username=? and phone=?";
        //修改添加参数
        String[] whereArgs = {username, phone};
        //修改
        return sqliteDatabase.update("User", values, whereClause, whereArgs);

    }

    /**
     * 更新收费标准
     */
    public static long updateFees(String type, String money,
                                  Context context) {

        SQLiteDatabase sqliteDatabase = getDatabase(context);
        ContentValues values = new ContentValues();
        values.put("chargeType", type);
        values.put("chargeStandard", money);
        //修改条件
        String whereClause = "id=?";
        //修改添加参数
        String[] whereArgs = {"1"};
        //修改
        return sqliteDatabase.update("Park", values, whereClause, whereArgs);

    }

    /**
     * 更新违规记录
     */
    public static long updateV(int score, double money, String overTime, int flag, int moneyFlag, String cid,
                               Context context) {

        SQLiteDatabase sqliteDatabase = getDatabase(context);

        Cursor cursor = sqliteDatabase.query("Car", null, "id=?",
                new String[]{cid}, null, null, null);
        double lastMoney = 0.0;
        int myscore = 0;
        boolean flagM = false;//判断用户余额够不够
        if (cursor.moveToFirst()) {
            lastMoney = cursor.getDouble(cursor.getColumnIndex("carMoney"));
            myscore = cursor.getInt(cursor.getColumnIndex("cardScore"));
            if (myscore < score) {
                return 0;
            }
            if (lastMoney > money) {
                flagM = true;
                ContentValues values = new ContentValues();
                values.put("carMoney", lastMoney - money);
                //修改条件
                String whereClause = "id=?";
                //修改添加参数
                String[] whereArgs = {cid};
                //修改
                sqliteDatabase.update("Car", values, whereClause, whereArgs);
            }

        }

        ContentValues values2 = new ContentValues();
        values2.put("money", money);
        values2.put("cid", cid);
        values2.put("time", System.currentTimeMillis());
        if (flagM) {
            values2.put("type", "2");//余额支付
        } else {
            values2.put("type", "3");//现金支付
        }
        values2.put("mark", "");
        sqliteDatabase.insert("MoneyLog", null, values2);

        ContentValues values = new ContentValues();
        values.put("overTime", overTime);
        values.put("flag", flag);
        values.put("moneyFlag", moneyFlag);
        //修改条件
        String whereClause = "id=?";
        //修改添加参数
        String[] whereArgs = {cid};
        //修改


        return sqliteDatabase.update("Rule", values, whereClause, whereArgs);

    }


    /**
     * 通过用户名查找该用户的注册邮箱
     *
     * @param username 用户名
     * @param context  数据库上下文
     * @return 如果找到则返回该用户的注册邮箱，如果没找到则返回null
     */
    public static String queryUserEmail(String username, Context context) {
        String email = null;
        SQLiteDatabase sqliteDatabase = getDatabase(context);
        Cursor cursor = sqliteDatabase.query("user", null, "username=?",
                new String[]{username}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            email = cursor.getString(cursor.getColumnIndex("email"));
        }
        return email;
    }

    /**
     * 通过用户名查找该用户的密码
     *
     * @param username 用户名
     * @param context  数据库上下文
     * @return 如果找到则返回该用户的密码，如果没找到则返回null
     */
    public static String queryUserPassword(String username, Context context) {
        String password = null;
        SQLiteDatabase sqliteDatabase = getDatabase(context);
        Cursor cursor = sqliteDatabase.query("user", null, "username=?",
                new String[]{username}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            password = cursor.getString(cursor.getColumnIndex("userpwd"));
        }
        return password;
    }

    /**
     * 增加停车记录
     */
    public static long insertParking(int carID, Context context) {
        // 创建ContentValues对象
        ContentValues values = new ContentValues();
        // 向该对象中插入键值对，其中键是列名，值是希望插入到这一列的值，值必须和数据库当中的数据类型一致
        values.put("parkCar", carID);
        values.put("parkTime", System.currentTimeMillis());
        SQLiteDatabase sqliteDatabase = getDatabase(context);
        // 调用insert方法，就可以将数据插入到数据库当中
        // 第一个参数:表名称
        // 第二个参数：SQl不允许一个空列，如果ContentValues是空的，那么这一列被明确的指明为NULL值
        // 第三个参数：ContentValues对象
        return sqliteDatabase.insert("ParkNow", null, values);
    }
}
