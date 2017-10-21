package com.example.administrator.myapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLiteOpenHelper是一个辅助类，用来管理数据库的创建和版本他，它提供两个方面的功能
 * 第一，getReadableDatabase()、getWritableDatabase
 * ()可以获得SQLiteDatabase对象，通过该对象可以对数据库进行操作
 * 第二，提供了onCreate()、onUpgrade()两个回调函数，允许我们再创建和升级数据库时，进行自己的操作
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 6;
    //路灯表
    public static final String CREATE_Light = "create table Light("
            + "id integer primary key autoincrement,"
            + "state text,"
            + "rLight integer,"
            + "gLignt integer，"
            + "yLight integer)";

    //停车价位表
    public static final String CREATE_Park = "create table Park("
            + "id integer primary key autoincrement,"
            + "chargeType text,"
            + "chargeStandard text)";

    //消费记录表
    public static final String CREATE_MoneyLog = "create table MoneyLog("
            + "id integer primary key autoincrement,"
            + "cid text,"
            + "money text," +
            "type text," +
            "mark text," +
            "time text)";

    //用户表
    public static final String CREATE_User = "create table User("
            + "id integer primary key autoincrement,"
            + "username text,"
            + "userpwd text," +
            "type text," +
            "phone text)";


    //停车记录表

    public static final String CREATE_ParkNow = "create table ParkNow("
            + "id integer primary key autoincrement,"
            + "parkTime text,"
            + "longTime INTEGER," +
            "type INTEGER," +
            "cost double,"
            + "moneyCast double,"
            + "parkCar int)";
    //小车信息表
    public static final String CREATE_Car = "create table Car("
            + "id integer primary key autoincrement,"
            + "number text,"
            + "numberType text," +
            "carMoney double," +
            "carState text," +
            "carName text," +
            "carPhone text,"
            + " cardScore text)";

    //违规记录表

    public static final String CREATE_Rules = "create table Rule("
            + "id integer primary key autoincrement,"
            + "vTime text,"
            + "vMsg text,"
            + "overTime text,"
            + "flag text,"
            + "carId text,"
            + "vXY text,"
            + "vID text,"
            + "vOffice text,"
            + "moneyFlag text,"
            + "score text,"
            + "money text,"
            + "photo text)";
    //环境记录表

    public static final String CREATE_Envir = "create table Envir(" +
            "co2 int ,airTemperature int ," +
            "airHumidity int,soilTemperature int ,soilHumidity int ," +
            "light int ,time bigint)";
    public static final String CREATE_TrafficR = "create table TrafficR(" +
            "id integer primary key autoincrement , money text," +
            " rule text, score text)";

    /**
     * 在SQLiteOpenHelper的子类当中，必须有该构造函数
     *
     * @param context 上下文对象
     * @param name    数据库名称
     * @param factory
     * @param version 当前数据库的版本，值必须是整数并且是递增的状态
     */
    public DatabaseHelper(Context context, String name, CursorFactory factory,
                          int version) {
        // 必须通过super调用父类当中的构造函数
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    public DatabaseHelper(Context context, String name) {
        this(context, name, VERSION);
    }

    // 该函数是在第一次创建的时候执行，实际上是第一次得到SQLiteDatabase对象的时候才会调用这个方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("create a database");
        // execSQL用于执行SQL语句
        db.execSQL(CREATE_Light);
        db.execSQL(CREATE_Park);
        db.execSQL(CREATE_ParkNow);
        db.execSQL(CREATE_Car);
        db.execSQL(CREATE_Rules);
        db.execSQL(CREATE_Envir);
        db.execSQL(CREATE_User);
        db.execSQL(CREATE_MoneyLog);
        db.execSQL(CREATE_TrafficR);


        db.execSQL("CREATE TABLE IF NOT EXISTS user (username string primary key, userpwd string, email string);");


        //规则表初始化
        db.execSQL("insert into TrafficR(id,money ,rule ,score) values(1,'50','未携带行驶证、驾驶证','1')");
        db.execSQL("insert into TrafficR(id,money ,rule ,score) values(2,'50','车辆未张贴交织险标志','1')");
        db.execSQL("insert into TrafficR(id,money ,rule ,score) values(3,'50','副驾不系安全带','1')");
        db.execSQL("insert into TrafficR(id,money ,rule ,score) values(4,'50','超载小于20%','2')");
        db.execSQL("insert into TrafficR(id,money ,rule ,score) values(5,'50','超速驾驶但未超过限速50%','3')");
        db.execSQL("insert into TrafficR(id,money ,rule ,score) values(6,'50','超速驾驶高于限速50%','12')");
        db.execSQL("insert into TrafficR(id,money ,rule ,score) values(7,'50','高速公路倒车、逆行、穿越中央隔离带调头','12')");
        db.execSQL("insert into TrafficR(id,money ,rule ,score) values(8,'50','未悬挂、伪造、变造、不按规定安装号牌、故意遮挡污损号牌','12')");


        //环境表初始化
        db.execSQL("insert into Envir(co2,airTemperature,airHumidity,soilTemperature,soilHumidity,light,time) values(10,10,10,10,10,10,1)");
        db.execSQL("insert into Envir(co2,airTemperature,airHumidity,soilTemperature,soilHumidity,light,time) values(20,20,20,20,20,20,2)");
        db.execSQL("insert into Envir(co2,airTemperature,airHumidity,soilTemperature,soilHumidity,light,time) values(30,30,30,30,30,30,3)");
        db.execSQL("insert into Envir(co2,airTemperature,airHumidity,soilTemperature,soilHumidity,light,time) values(40,40,40,40,40,40,4)");
        db.execSQL("insert into Envir(co2,airTemperature,airHumidity,soilTemperature,soilHumidity,light,time) values(50,50,50,50,50,50,5)");
        db.execSQL("insert into Envir(co2,airTemperature,airHumidity,soilTemperature,soilHumidity,light,time) values(60,60,60,60,60,60,6)");
        db.execSQL("insert into Envir(co2,airTemperature,airHumidity,soilTemperature,soilHumidity,light,time) values(70,70,70,70,70,70,7)");

        //停车场价格表初始化
        db.execSQL("insert into Park(chargeType,chargeStandard) values('1','50')");

        //小车信息表初始化
        db.execSQL("insert into Car(number,numberType,cardScore,carMoney,carState,carName,carPhone) values('MA000001','c1','12',0.0,'1','LZH1','18659127035')");
        db.execSQL("insert into Car(number,numberType,cardScore,carMoney,carState,carName,carPhone) values('MA000002','b1','12',0.0,'1','LZH2','17720798392')");
        db.execSQL("insert into Car(number,numberType,cardScore,carMoney,carState,carName,carPhone) values('MA000003','a1','12',0.0,'1','LZH3','15160594831')");
        db.execSQL("insert into Car(number,numberType,cardScore,carMoney,carState,carName,carPhone) values('MA000004','a1','12',0.0,'1','LZH4','13705970196')");
        db.execSQL("insert into Car(number,numberType,cardScore,carMoney,carState,carName,carPhone) values('MA000005','a1','12',0.0,'1','LZH5','13055710973')");
        db.execSQL("insert into Car(number,numberType,cardScore,carMoney,carState,carName,carPhone) values('MA000006','c1','12',0.0,'1','LZH6','2533666')");
        db.execSQL("insert into Car(number,numberType,cardScore,carMoney,carState,carName,carPhone) values('MA000007','a1','12',0.0,'1','LZH7','2336666')");
        db.execSQL("insert into Car(number,numberType,cardScore,carMoney,carState,carName,carPhone) values('MA000008','b1','12',0.0,'1','LZH8','2336766')");
        db.execSQL("insert into Car(number,numberType,cardScore,carMoney,carState,carName,carPhone) values('MA000009','a1','12',0.0,'1','LZH9','2336866')");
        db.execSQL("insert into Car(number,numberType,cardScore,carMoney,carState,carName,carPhone) values('MA000010','c1','12',0.0,'1','LZH10','2339666')");
        db.execSQL("insert into Car(number,numberType,cardScore,carMoney,carState,carName,carPhone) values('MA000011','a1','12',0.0,'1','LZH11','10233666')");
        db.execSQL("insert into Car(number,numberType,cardScore,carMoney,carState,carName,carPhone) values('MA000012','b1','12',0.0,'1','LZH12','11233666')");

        //用户表初始化
        db.execSQL("insert into User(username,userpwd,type,phone) values('admin','admin','0','110')");
        db.execSQL("insert into User(username,userpwd,type,phone) values('police','police','1','110')");
        db.execSQL("insert into User(username,userpwd,type,phone) values('123','123','2','17720798392')");
        db.execSQL("insert into User(username,userpwd,type,phone) values('1234','1234','2','15160594831')");
        db.execSQL("insert into User(username,userpwd,type,phone) values('12345','12345','2','13705970196')");

        //违规表初始化
        db.execSQL("insert into Rule(vTime,vMsg,overTime,flag,carId,vXY,vID,vOffice,moneyFlag,score,money,photo,overTime) " +
                "values('1490235397504','副驾不系安全带','','0','2','2,2','3','福建交警大队','0','1','50','a1.jpg,a2.jpg,a3.jpg','0')");
        db.execSQL("insert into Rule(vTime,vMsg,overTime,flag,carId,vXY,vID,vOffice,moneyFlag,score,money,photo,overTime) " +
                "values('1490235397504','副驾不系安全带','','0','3','2,2','3','福建交警大队','0','1','50','a1.jpg,a2.jpg,a3.jpg','0')");
        db.execSQL("insert into Rule(vTime,vMsg,overTime,flag,carId,vXY,vID,vOffice,moneyFlag,score,money,photo,overTime) " +
                "values('1490235397504','副驾不系安全带','','0','4','2,2','3','福建交警大队','0','1','50','a1.jpg,a2.jpg,a3.jpg','0')");

        //停车表初始化
        db.execSQL("insert into ParkNow(parkTime,longTime,moneyCast,parkCar,type,cost) values('1490235397504',1,20,2,1,20)");
        db.execSQL("insert into ParkNow(parkTime,longTime,moneyCast,parkCar,type,cost) values('1490235397504',1,20,3,1,20)");
        db.execSQL("insert into ParkNow(parkTime,longTime,moneyCast,parkCar,type,cost) values('1490235397504',1,20,4,1,20)");

        //消费记录表初始化
        //1为违规缴款 2为停车场结账
        db.execSQL("insert into MoneyLog(cid ,money,type,mark,time) values('2','20','1','','1490235397504')");
        db.execSQL("insert into MoneyLog(cid ,money,type,mark,time) values('3','20','2','1,20','1490235397504')");
        db.execSQL("insert into MoneyLog(cid ,money,type,mark,time) values('4','20','2','2,20','1490235397504')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db_db, int oldVersion, int newVersion) {
        // 数据库升级以后，删除老数据
        if (oldVersion != VERSION) {
            System.out.println("DROP TABLE : user");
            // 清除老数据库
            db_db.execSQL("DROP TABLE IF EXISTS user");
            // 重新创建数据库
            onCreate(db_db);
        }
    }
}
