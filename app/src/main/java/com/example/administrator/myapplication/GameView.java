package com.example.administrator.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.administrator.myapplication.config.GameViewConfig;
import com.example.administrator.myapplication.db.DatabaseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.administrator.myapplication.config.GameViewConfig.CAR_SPEED_TEXT_SIZE;

public class GameView extends View implements View.OnClickListener {
    private Matrix matrix;
    Random rand = new Random();

    public Bitmap car_blue;
    public Bitmap car_red;
    public Bitmap car_yellow;
    Point A1, A2, B1, B2, B3, C1, C2, C3;
//wb

    private Canvas mCanvas;
    private int mWidth, mHeigh;

    //LZH
    SharedPreferences sp;
    /**
     * 小车数量
     */
    private int carNum;

    public int getCarNumber() {
        return carNum;
    }

    public List<Car> getCars() {
        return cars;
    }

    // 0 480  900
    private int LightOne = 0;
    private int Luxian1 = 0;//大圈
    private int Luxian2 = 413 + 70;//中圈  luxian3
    private int Luxian3 = 725;//小圈
    int[] LUXIANS = {Luxian1, Luxian2, Luxian3};
    private Paint paint = new Paint();//ETC路口灯
    //     Paint paintH1 = new Paint();//横向路口灯
    Paint paintH2 = new Paint();//横向路口灯
    Paint paintZ = new Paint();//纵向路口灯
    List<Car> cars = new ArrayList<Car>();
    List<Light> lights = new ArrayList<Light>();
    List<Car> partCars = new ArrayList<Car>();
    String chaocheFlag = "";

    //陈浩修改
    Paint paint_land;
    Paint paint_ground;
    Paint paint_text;
    Paint textRed;
    int width;
    int height;

    public List<Car> getPartCars() {
        return partCars;
    }

    public List<Light> getLights() {
        return lights;
    }

    public static volatile boolean isShowCarSpeed = false;

    public static boolean isShowCarSpeed() {
        return isShowCarSpeed;
    }

    public static void setIsShowCarSpeed(boolean isShowCarSpeed) {
        GameView.isShowCarSpeed = isShowCarSpeed;
    }

    /**
     * 必须要覆盖View中一个构造方法
     *
     * @param context
     */
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        matrix = new Matrix();
        sp = getContext().getSharedPreferences("config", context.MODE_APPEND);
        carNum = sp.getInt("carNum", 10);

        Light l1 = new Light(100, 100, 0);
        Light l2 = new Light(100, 100, 100);
        Light B1A = new Light(100, 100, 0);
        Light B1B = new Light(100, 100, 100);
        Light DA = new Light(100, 100, 0);
        Light DB = new Light(100, 100, 100);

        lights.add(l1);
        lights.add(l2);
        lights.add(B1A);
        lights.add(B1B);
        lights.add(DA);
        lights.add(DB);


        A1 = new Point(0, 0);
        A2 = new Point(0, 0);
//        A3 = new Point(0, 0);
        // 加载小车
        car_red = LzhTool.reBitmap(BitmapFactory.decodeResource(getResources(), R.drawable
                .car_red), 100 / 2, 50 / 2);
        car_blue = LzhTool.reBitmap(BitmapFactory.decodeResource(getResources(), R.drawable
                .car_blue), 100 / 2, 50 / 2);
        car_yellow = LzhTool.reBitmap(BitmapFactory.decodeResource(getResources(), R.drawable
                .car_yellow), 100 / 2, 50 / 2);
        List<Bitmap> carBitmaps = new ArrayList<Bitmap>();

        for (int i = 0; i < carNum; i++) {
            if (i % 3 == 0) {
                carBitmaps.add(LzhTool.reBitmap(BitmapFactory.decodeResource(getResources(), R
                        .drawable.car_red), 100 / 2, 50 / 2));
            }
            if (i % 3 == 1) {
                carBitmaps.add(LzhTool.reBitmap(BitmapFactory.decodeResource(getResources(), R
                        .drawable.car_blue), 100 / 2, 50 / 2));
            }
            if (i % 3 == 2) {
                carBitmaps.add(LzhTool.reBitmap(BitmapFactory.decodeResource(getResources(), R
                        .drawable.car_yellow), 100 / 2, 50 / 2));
            }
        }

        Paint text = new Paint();
        text.setColor(Color.WHITE);
        text.setTextSize(25);

        Car car;
        MyThread myThread;
        Canvas canvas;
        for (int i = 1; i < carNum + 1; i++) {
            canvas = new Canvas();
            canvas.setBitmap(carBitmaps.get(i - 1));
            canvas.rotate(90, 20, 20);
            canvas.drawText("" + i, 20, 20, text);
            canvas.rotate(-90, 20, 20);
            car = new Car(i * 100, 20, carBitmaps.get(i - 1), LUXIANS[i % 3], i);
            car.parkAdd = i - 1;
            cars.add(car);

            myThread = new MyThread(car, 1);
            myThread.start();
        }
        setOnClickListener(this);

        //陈浩修改
        paint_land = new Paint();
        paint_ground = new Paint();
        paint_text = new Paint();
        textRed = new Paint();
    }

    @Override
    protected void onMeasure(int width, int height) {
        super.onMeasure(width, height);
        int w = this.getMeasuredHeight();
        int h = this.getMeasuredWidth();
        A1 = new Point(w / 5, 0);
        A2 = new Point(w / 3 * 2, 0);
        // A3=new Point(w,0);
        B1 = new Point();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取屏幕大小
        initWidthHeight(canvas);

        B1.x = width / 4;
        B1.y = (int) (height * 0.85);
        // 画笔对象，可以控制颜色和文字大小


        getParkLed(10 - partCars.size());
        //画路灯
        getStreetLight(LzhTool.colors);
        // 给画笔设置系统内置的颜色：蓝色
        paint_land.setColor(Color.GRAY);
        paint_ground.setColor(Color.GREEN);
        paint_text.setColor(Color.YELLOW);
        paintZ.setColor(Color.GREEN);
        // 文字大小
        paint_text.setTextSize(50);
        //将画笔颜色调成红色
        for (Light light : lights) {
            if (light.nowTime < light.greedTime) {
                light.light.setColor(Color.GREEN);
            } else if (light.nowTime < light.greedTime + light.redTime) {
                light.light.setColor(Color.RED);
            } else {
                light.nowTime = 0;
            }
            light.nowTime++;
        }
        if (LightOne >= 100) {
            paint.setColor(Color.BLACK);
//            paintH1.setColor(Color.RED);
            LightOne++;
        }
        if (LightOne < 100) {
            paint.setColor(Color.TRANSPARENT);
//            paintH1.setColor(Color.GREEN);
            LightOne++;
        }
        if (LightOne >= 200) {
            LightOne = 0;
        }

        //ECT通道
        canvas.drawRect(width / 5 * 4 + 80, 0, width / 5 * 4 + 100, height / 8 - 30, paint);
        canvas.drawRect(width / 5 * 4 + 80, height - height / 8 + 30, width / 5 * 4 + 100,
                height, paint);

        //绘画红绿灯
        drawTrafficLight(canvas);

//        canvas.drawCircle(width / 5 * 1 , 10, 20, paintH1);

//        canvas.drawCircle(width / 5 * 1 +170, height / 8 - 30, 20, paintZ);

        textRed.setColor(Color.RED);
        textRed.setTextSize(50);
        canvas.drawText(chaocheFlag, width / 5 * 1 + width / 20 + 50, height / 2, textRed);
        //canvas.drawText("停车场", width / 5 * 3 + 120, height / 2, paint_text);
        //画车S
        for (Car car : cars) {
            for (Car carTemp : cars) {
                if ((!car.equals(carTemp)) && car.getPointLimit(carTemp) < car.image.getWidth() -
                        50) {
//                    Log.e("chenhao", "撞车" + car.image.getWidth());
//                    car.isRun = false;
                }
                if (!carTemp.isRun)
                    continue;
                if (car.round == 270 && car.x > carTemp.x)
                    continue;
                if (car.round == 90 && car.x < carTemp.x)
                    continue;
                if (car.round == 0 && car.y > carTemp.y)
                    continue;
                if (car.round == 180 && car.y < carTemp.y)
                    continue;
                if (car.equals(carTemp) || car.round == (carTemp.round + 180) % 360 ||
                        car.round == (carTemp.round + 90) % 360)
                    continue;
                if (LzhTool.isLimit(car.x, carTemp.x, car.y, carTemp.y, 55)) {
                    car.targerX = car.x;
                    car.targerY = car.y;
                }
            }
            //270左 西 0下 南 90 右 东 180 上 北
            canvas.drawBitmap(car.image, (int) width - (car.targerX), (int) car.targerY, null);

            if (isShowCarSpeed) {
                paint_text.setTextSize(CAR_SPEED_TEXT_SIZE);
                canvas.drawText(car.getSpeed() + "", (int) width - (car.targerX), (int) car.targerY,
                        paint_text);
            }
            car.x = car.targerX;
            car.y = car.targerY;


        }


    }

    private void initWidthHeight(Canvas canvas) {
        width = canvas.getWidth();
        height = canvas.getHeight();
        //wb
        //画LED的方法
        mCanvas = canvas;
        mWidth = width;
        mHeigh = height;

//        Luxian1 = 0;
//        Luxian2 = mWidth*2/3;
//        Luxian3 = mWidth-50;

        // Luxian1 = Luxian2 = Luxian3 = 678;


//        Log.e("233", "mWidth: " + mWidth + "mHeigh: " + mHeigh);

    }


    /**
     * 绘画 红绿灯 和停车场
     *
     * @param canvas
     */
    private void drawTrafficLight(Canvas canvas) {
        //灯和停车场
        canvas.drawCircle(width / 5 * 2, 10, GameViewConfig.TRAFFIC_LIGHT_SIZE, lights.get(0).light);
        canvas.drawCircle(width / 5 * 2 + 210, height / 8 - 30, GameViewConfig.TRAFFIC_LIGHT_SIZE, lights.get(1).light);
        //ch 左下角
        //左边
        canvas.drawCircle(width / 4, height - 10, GameViewConfig.TRAFFIC_LIGHT_SIZE, lights.get(2).light);
        //乡下
        canvas.drawCircle((float) (width / 4 * 2 * 0.5 + 100), (float) (height * 0.85), GameViewConfig.TRAFFIC_LIGHT_SIZE,
                lights.get(3).light);

        //ch 停车场上方
        canvas.drawCircle((float) (width / 4 * 3), 10, GameViewConfig.TRAFFIC_LIGHT_SIZE, lights.get(4).light);
        canvas.drawCircle((float) (width / 2 + width / 2 * 0.55), height / 8 - 20, GameViewConfig.TRAFFIC_LIGHT_SIZE, lights.get
                (5).light);

    }


    //这是画停车场提示牌和提示牌上的字体的方法
    private void getParkLed(int text) {
        Paint ledPain = new Paint();
        ledPain.setColor(Color.BLUE);
        ledPain.setStyle(Paint.Style.FILL);
        //Led的停车场提示牌
        mCanvas.drawRect(
                mWidth / 2 + 50,//左
                mHeigh / 8 * 6,//上
                mWidth / 8 * 5,//右
                mHeigh - 150,//下
                ledPain);

        Paint ledPainText = new Paint();
        ledPainText.setColor(Color.WHITE);
        ledPainText.setStyle(Paint.Style.FILL);
        ledPainText.setTextSize(25);
        mCanvas.drawText("空闲车位", mWidth / 2 + 60, mHeigh / 8 * 6 + 40, ledPainText);
        ledPainText.setTextSize(50);
        mCanvas.drawText(String.valueOf(text), mWidth / 2 + 80, mHeigh / 8 * 6 + 90, ledPainText);
    }

    //这是画四周的路灯的方法


    public void getStreetLight(String mColor) {
        Paint streetLight = new Paint();
        streetLight.setColor(Color.parseColor(mColor));
        streetLight.setStyle(Paint.Style.FILL);
        //上排的五个
        mCanvas.drawCircle(5, 5, 10, streetLight);
        mCanvas.drawCircle(mWidth / 4, 5, 10, streetLight);
        mCanvas.drawCircle(mWidth / 2, 5, 10, streetLight);
        mCanvas.drawCircle(mWidth / 4 * 3, 5, 10, streetLight);
        mCanvas.drawCircle(mWidth - 5, 5, 10, streetLight);

        //下排的五个
        mCanvas.drawCircle(5, mHeigh - 5, 10, streetLight);
        mCanvas.drawCircle(mWidth / 4, mHeigh - 5, 10, streetLight);
        mCanvas.drawCircle(mWidth / 2, mHeigh - 5, 10, streetLight);
        mCanvas.drawCircle(mWidth / 4 * 3, mHeigh - 5, 10, streetLight);
        mCanvas.drawCircle(mWidth - 5, mHeigh - 5, 10, streetLight);

        //左边
        mCanvas.drawCircle(5, mHeigh / 4, 10, streetLight);
        mCanvas.drawCircle(5, mHeigh / 2, 10, streetLight);
        mCanvas.drawCircle(5, mHeigh / 4 * 3, 10, streetLight);
        //右边
        mCanvas.drawCircle(mWidth - 5, mHeigh / 4, 10, streetLight);
        mCanvas.drawCircle(mWidth - 5, mHeigh / 2, 10, streetLight);
        mCanvas.drawCircle(mWidth - 5, mHeigh / 4 * 3, 10, streetLight);

    }

    //图片旋转
    static Bitmap setRotate(Car car, int orientationDegree) {
        Bitmap bm = car.image;
        car.round = (car.round - orientationDegree + 360) % 360;
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);

        try {
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);

            return bm1;

        } catch (OutOfMemoryError ex) {


        }
        return bm;

    }

    //线程刷新重绘
    public void setsleep() {
        // 不断的调用View中的postInvalidate方法，让界面重新绘制
        this.postInvalidate();
        //this.invalidate(); 此方法要求在UI主线程调用
        try {
            // 暂停0.5秒继续
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //左行至红绿灯至结尾
    public void Car_GoLeft(Car car) {
        car.targerY = 20;
        while (true) {
            car.targerX = car.x + car.speed;
            if (car.targerX >= (A1.x) && car.targerX <= (A1.x + car.speed) && paint.getColor() ==
                    Color.BLACK) {
                car.targerX = (A1.x);
                setsleep();
            } else if (car.targerX >= (A2.x) && car.targerX <= (A2.x + car.speed) && lights.get
                    (0).light.getColor() == Color.RED) {
                car.targerX = (A2.x);
                setsleep();
            }
//            else if (car.targerX >= (A3.x) && car.targerX <= (A3.x + car.speed) && paintH2
// .getColor()  != Color.GREEN) {
//                car.targerX = (A3.x);
//                setsleep();
//            }

            if (car.targerX >= (car.luxian == Luxian3 ? Luxian3 : getWidth())) {
                car.targerX = (car.luxian == Luxian3 ? Luxian3 : getWidth()) - 30;
                break;
            }
            //ch
//            if ((car.y <= B1.y - 10 || car.y >= B1.y + 10) && car.x >= B1.x - 10) {
//                if (lights.get(2).light.getColor() == Color.RED) {
//                    car.x = B1.x - 30;
//                }
//            }


            setsleep();

        }

    }

    //到底部

    public void Car_GoBootom(Car car) {
        while (true) {
            car.targerY = car.speed + car.y;
            if (car.luxian == Luxian3 && lights.get(3).light.getColor() == Color.RED && car
                    .targerY >= getHeight() - 200 && car.targerY <= getHeight() - 200 + car.speed) {
                car.targerY = car.y;
            }

            if (car.targerY >= getHeight() - 100) {
                car.targerY = getHeight() - 100;
                return;
            }
            setsleep();

        }

    }

    //到右边至红绿灯至结束
    public void Car_GoRight(Car car) {
        car.targerY = getHeight() - 50;
        while (true) {
            if (car.targerX >= (getWidth() / 4 - 20) && car.targerX <= (getWidth() / 4 + 80) &&
                    paint.getColor() == Color.BLACK) {
                car.targerX = (getWidth() / 4 - 20);
            } else if (car.targerX >= (getWidth() - 215 - car.speed) && car.targerX <= (getWidth
                    () - 215) && lights.get(2).light.getColor() == Color.RED) {
                car.targerX = car.x;
            } else {
                car.targerX = car.x - car.speed;
            }

            if (car.isPark() && car.targerX <= (getWidth() / 3)) {
                car.targerX = (300);
                return;
            }

            if ((!car.isPark()) && car.targerX <= car.image.getWidth() - 20 + (car.luxian == 0 ?
                    0 : Luxian2)) {
                if (car.luxian == Luxian2 || car.luxian == Luxian3)
                    car.targerX = (car.luxian == 0 ? 0 : Luxian2);
                else car.targerX = (car.luxian == 0 ? 0 : Luxian2) + 50;
                return;
            }

            setsleep();
        }

    }

    //到顶部
    public void Car_GoTop(Car car) {
        while (true) {
            car.targerY = car.y - car.speed;
            if (car.targerX == (car.luxian == 0 ? 0 : Luxian2) + 70 && car.targerY < 100 && car
                    .targerY >= 100 - car.speed && lights.get(1).light.getColor() == Color.RED &&
                    car.luxian != Luxian1) {
                car.targerY = 100;
            }
            if (car.isPark()) {
                if (car.targerY < 100 + car.parkAdd * 81 && car.targerY >= 100 + car.parkAdd * 81
                        - car.speed) {
                    car.image = setRotate(car, 270);
                    car.targerX = 200;
                    Log.e("233", "Car_GoTop: " + car.getName());
                    car.targerY = 75 + car.parkAdd * 55;
                    car.isRun = false;
                    setsleep();
                    car.setBeginPartTime(System.currentTimeMillis());
                    car.setPartType(DatabaseUtil.getParkMoney(getContext()));
                    long time = System.currentTimeMillis() + car.parkTime;
                    partCars.add(car);
                    while (System.currentTimeMillis() < time && car.isPark) {
                        setsleep();

                    }
                    DatabaseUtil.addPartLog(getContext(), car.getName(), car.getBeginPartTime(),
                            car.getParkTime(), car.getPartType());
                    car.setParkTime(0);
                    car.setPartType("");
                    car.setPark(false);
                    partCars.remove(partCars.indexOf(car));
                    car.isRun = true;
                    car.image = setRotate(car, 90);
                    car.targerX = 300;
                    if (car.targerY < 10) {
                        car.targerY = car.image.getWidth() + 10;
                        return;
                    }
                }
            }


            if (car.targerY < 10) {
                car.targerY = car.image.getWidth() + 10;
                return;
            }
            setsleep();
        }

    }

    @Override
    public void onClick(View v) {

    }


    class MyThread extends Thread {
        private Car car; //定义需要传值进来的参数
        private int time;

        public MyThread(Car car, int time) {
            this.car = car;
            this.time = time;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(time * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (true) {
                Car_GoLeft(car);
                //旋转车头
                car.image = setRotate(car, -90);
                Car_GoBootom(car);
                car.image = setRotate(car, -90);
                Car_GoRight(car);
                car.image = setRotate(car, -90);
                Car_GoTop(car);
                car.image = setRotate(car, -90);
                setsleep();
            }
        }
    }
}