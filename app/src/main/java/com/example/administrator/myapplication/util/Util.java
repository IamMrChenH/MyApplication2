package com.example.administrator.myapplication.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Util {
    /**
     * 判断当前系统中指定的service是否在运行
     *
     * @param context   APP上下文
     * @param className service类的名称
     * @return 如果存在，返回true，否则返回false
     */
    public static boolean isServiceExisted(Context context, String className) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> serviceList = activityManager
                .getRunningServices(Integer.MAX_VALUE);

        if (serviceList.size() <= 0) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            RunningServiceInfo serviceInfo = serviceList.get(i);
            ComponentName serviceName = serviceInfo.service;

            if (serviceName.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 快速封装一个soap协议
     *
     * @param bodyContent soap协议中的body内容
     * @return 完整的soap协议
     */
    public static String getSoapXml(String bodyContent) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
                + "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                + "<soap:Body>" + bodyContent + "</soap:Body></soap:Envelope>");
        return sb.toString();
    }

    public static String getXmlElementStr(String name, String value) {
        return "<" + name + ">" + value + "</" + name + ">";
    }

    /*

     */
    public static GraphicalView xychar(Context context, String[] titles, ArrayList<double[]> value,
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
        renderer.setXLabelsAlign(Paint.Align.CENTER);
        renderer.setYLabelsAlign(Paint.Align.RIGHT);
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
        GraphicalView mChartView = ChartFactory.getBarChartView(context,
                dataset, renderer, BarChart.Type.DEFAULT);

        return mChartView;

    }

    /**
     * 从一个完整的soap协议内容中解析出soap body内容
     *
     * @param soapXml 完整的soap协议
     * @return soap body内容
     */
    public static Element getSoapBodyElement(String soapXml) {
        Element soapBodyEle = null;

        // 采用doc的方式解析xml内容
        DocumentBuilderFactory factory = null;
        DocumentBuilder builder = null;
        Document document = null;
        StringReader stringReader = new StringReader(soapXml);
        InputSource inputSource = new InputSource(stringReader);

        factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(inputSource);

            Element root = document.getDocumentElement();
            if (root != null) {
                // 从soap协议中找到soap:Body标签并提取该标签里的内容
                soapBodyEle = (Element) root.getElementsByTagName("soap:Body")
                        .item(0);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return soapBodyEle;
    }

    // 从xml的父标签中提取子标签的内容
    public static String getChildElementValueStr(Element element,
                                                 String childName) {
        Element childEle = (Element) element.getElementsByTagName(childName)
                .item(0);
        if (childEle != null) {
            return childEle.getFirstChild().getNodeValue();
        }
        return "";
    }

    // 从xml的父标签中提取子标签的内容，并把内容转成int类型返回
    public static int getChildElementValueInt(Element element, String childName) {
        int ret = 0;
        String strValue = getChildElementValueStr(element, childName);
        if (strValue.equals("")) {
            try {
                ret = Integer.parseInt(strValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }


}
