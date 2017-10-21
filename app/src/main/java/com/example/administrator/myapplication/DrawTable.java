package com.example.administrator.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;

public class DrawTable extends Activity {
    LinearLayout li1;
    private double[] d1;//收益
    private double[] d2;//车辆数

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw_table);

        //得到资源
        li1 = (LinearLayout) findViewById(R.id.lli);
        //初始化柱状图
        initView();
    }

    private void initView() {
        //柱状图的两个序列的名字
        String[] titles = new String[]{"车辆 （单位：万辆）", "收益  （单位：万）"};
        //存放柱状图两个序列的值
        ArrayList<double[]> value = new ArrayList<double[]>();
        //停车场数量和收益
        d1 = new double[]{0.1, 0.3, 0.7, 0.8, 0.5, 0.3, 0.7, 0.8, 0.5, 0.7, 0.8, 0.5};
        d2 = new double[]{0.3, 0.4, 0.8, 0.6, 0.1, 0.3, 0.7, 0.8, 0.5, 0.3, 0.7, 0.8};
        value.add(d1);
        value.add(d2);
        //两个状的颜色
        int[] colors = {Color.BLUE, Color.GREEN};

        //为li1添加柱状图
        li1.addView(
                xychar(getApplication(), titles, value, colors, 6, 1, new double[]{0,
                        12, 0, 1}, new int[]{1, 2, 3, 4,
                        5, 6, 7, 8, 9, 10, 11, 12}, "停车场收益（月报表）", true),
                new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT));
    }

    public static GraphicalView xychar(Context contexts, String[] titles, ArrayList<double[]> value,
                                       int[] colors, int x, int y, double[] range, int[] xLable, String xtitle, boolean f) {
        //多个渲染
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        //多个序列的数据集
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        //构建数据集以及渲染
        for (int i = 0; i < titles.length; i++) {

            XYSeries series = new XYSeries(titles[i]);
            double[] yLable = value.get(i);
            for (int j = 0; j < yLable.length; j++) {
                series.add(xLable[j], yLable[j]);
            }
            dataset.addSeries(series);
            XYSeriesRenderer xyRenderer = new XYSeriesRenderer();
            // 设置颜色
            xyRenderer.setColor(colors[i]);
            // 设置点的样式 //
            xyRenderer.setPointStyle(PointStyle.SQUARE);
            // 将要绘制的点添加到坐标绘制中

            renderer.addSeriesRenderer(xyRenderer);
        }
        renderer.setZoomEnabled(true, false);

        //设置x轴标签数
        renderer.setXLabels(x);
        //设置Y轴标签数
        renderer.setYLabels(y);
        //设置x轴的最大值
        renderer.setXAxisMax(x - 0.5);
        //设置轴的颜色
        renderer.setAxesColor(Color.BLACK);
        //设置x轴和y轴的标签对齐方式
        renderer.setXLabelsAlign(Align.CENTER);
        renderer.setYLabelsAlign(Align.RIGHT);
        // 设置现实网格
        renderer.setShowGrid(true);

        renderer.setShowAxes(true);
        // 设置条形图之间的距离
        renderer.setBarSpacing(0.2);
        ///renderer.setInScroll(false);
        renderer.setPanEnabled(true, false);
//		renderer.setClickEnabled(false);
        //设置x轴和y轴标签的颜色
        renderer.setXLabelsColor(Color.RED);
        renderer.setYLabelsColor(0, Color.YELLOW);

        int length = renderer.getSeriesRendererCount();
        //设置图标的标题
        renderer.setChartTitle(xtitle);
        renderer.setLabelsColor(Color.RED);

        //设置图例的字体大小
        renderer.setLegendTextSize(18);
        //设置x轴和y轴的最大最小值
        renderer.setRange(range);
        renderer.setMarginsColor(0x00888888);
        for (int i = 0; i < length; i++) {
            SimpleSeriesRenderer ssr = renderer.getSeriesRendererAt(i);
//			ssr.setChartValuesTextAlign(Align.RIGHT);
//			ssr.setChartValuesTextSize(12);
//			ssr.setDisplayChartValues(f);
        }
        GraphicalView mChartView = ChartFactory.getBarChartView(contexts,
                dataset, renderer, Type.DEFAULT);

        return mChartView;

    }
}