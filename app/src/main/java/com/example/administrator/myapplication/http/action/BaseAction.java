package com.example.administrator.myapplication.http.action;

import android.content.Context;
import android.util.Log;

import com.example.administrator.myapplication.util.Util;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Element;

/**
 * 基础action类，能根据协议类型分别调用jason和soap相关的解析函数， 具体的jason和soap解析函数要求子类实现
 *
 * @author asus
 */
public abstract class BaseAction {
    // jason和soap协议类型
    public static final String TYPE_JASON = "jason";
    public static final String TYPE_SOAP = "soap";

    // 通信协议类型
    private String type = "";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    protected String username = "";

    public String getUsername() {
        return username;
    }

    protected String logMsg = "";

    public String getLogMsg() {
        return logMsg;
    }

    protected String logObj = "";

    public String getLogObj() {
        return logObj;
    }

    /**
     * 根据协议类型分别调用jason和soap相关的解析函数
     *
     * @param param 待解析的数据
     * @return 服务器返回的Jason数据
     */
    public String porcessAction(String param) {
        String resultStr = null;
        if (type.equals(TYPE_JASON)) {
            try {
                Log.e("123", param);
                JSONObject jsonRequest = new JSONObject(param);
                // 解析用户名
                if (jsonRequest.has("username")) {
                    username = jsonRequest.getString("username");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 处理jason类型的请求
            resultStr = jasonPorcess(param);
            if (resultStr == null) {
                resultStr = "{'result':'failed'}";
            }
        } else if (type.equals(TYPE_SOAP)) {
            // get username
            Element soapBodyEle = Util.getSoapBodyElement(param);
            if (soapBodyEle != null) {
                // 解析用户名
                username = Util
                        .getChildElementValueStr(soapBodyEle, "username");
            }

            // 处理soap类型的请求
            resultStr = soapPorcess(param);
            if (resultStr == null) {
                resultStr = Util.getSoapXml(Util.getXmlElementStr("result",
                        "failed"));
            }
        } else {
            // 如果是jason和soap以外的协议类型，则返回协议错误，目前只支持jason和soap协议类型
            resultStr = "Protocol error";
        }
        return resultStr;
    }

    /**
     * jason协议的解析函数，要求子类实现
     *
     * @param param 待解析的数据
     * @return 要发送的数据
     */
    protected abstract String jasonPorcess(String param);

    /**
     * soap协议的解析函数，要求子类实现
     *
     * @param param 待解析的数据
     * @return 要发送的数据
     */
    protected abstract String soapPorcess(String param);

    /**
     * 根据协议类型和action，解析协议内容并返回相关结果
     *
     * @param actionType 协议类型，目前支持jason和soap
     * @param actionName 动作名称
     * @param strBody    客户端请求body
     * @param context    app上下文
     * @return 返回相关请求的处理结果
     */
    public static String disposeAction(String communType, String actionType,
                                       String actionName, String strBody, Context context, String clientIp) {
        String httpResp = "Can't find action";
        if (strBody.equals(""))
            Log.e("123", "kong");
        if (!actionName.equals("") && !actionType.equals("")) {
            BaseAction action = null;
            if (actionName.equals(GetCarMessage.TAG)) {
                action = new GetCarMessage(context); // 获得车主信息请求处理类
            } else if (actionName.equals(GetParkCastAciton.TAG)) {
                action = new GetParkCastAciton(context);// 获得停车场信息请求处理类
            } else if (actionName.equals(Register.TAG)) {
                action = new Register(context);// 获得停车场信息请求处理类
            } else if (actionName.equals(Login.TAG)) {
                action = new Login(context);// 获得停车场信息请求处理类
            } else if (actionName.equals(AddViolation.TAG)) {
                action = new AddViolation(context);// 添加违规记录请求处理类
            } else if (actionName.equals(GetAllCarData.TAG)) {
                action = new GetAllCarData(context);// 获得所有车辆数据请求处理类
            } else if (actionName.equals(GetCarData.TAG)) {
                action = new GetCarData(context);// 获得指定车辆数据请求处理类
            } else if (actionName.equals(GetViolation.TAG)) {
                action = new GetViolation(context);// 获得指定车辆违规记录请求处理类
            } else if (actionName.equals(GetAllViolation.TAG)) {
                action = new GetAllViolation(context);// 获得指定车辆违规记录请求处理类
            } else if (actionName.equals(AddMoney.TAG)) {
                action = new AddMoney(context);// 获得指定车辆违规记录请求处理类
            } else if (actionName.equals(UpdatePwd.TAG)) {
                action = new UpdatePwd(context);// 获得指定车辆违规记录请求处理类
            } else if (actionName.equals(Parking.TAG)) {
                action = new Parking(context);// 获得指定车辆违规记录请求处理类
            } else if (actionName.equals(GetPartLog.TAG)) {
                action = new GetPartLog(context);// 获得指定车辆违规记录请求处理类
            } else if (actionName.equals(GetMoneyLog.TAG)) {
                action = new GetMoneyLog(context);// 获得指定车辆违规记录请求处理类
            } else if (actionName.equals(UpdateV.TAG)) {
                action = new UpdateV(context);// 获得指定车辆违规记录请求处理类
            } else if (actionName.equals(GetAllPartLog.TAG)) {
                action = new GetAllPartLog(context);// 获得指定车辆违规记录请求处理类
            } else if (actionName.equals(GetAllMoneyLog.TAG)) {
                action = new GetAllMoneyLog(context);// 获得指定车辆违规记录请求处理类
            }else if (actionName.equals(UpdateFees.TAG)) {
                action = new UpdateFees(context);// 获得指定车辆违规记录请求处理类
            }else if (actionName.equals(GetTrafficR.TAG)) {
                action = new GetTrafficR(context);// 获得指定车辆违规记录请求处理类
            }

            if (action != null) {
                action.setType(actionType);
                // 处理相关请求，并返回结果
                httpResp = action.porcessAction(strBody);
            }
        }
        return httpResp;
    }
}
