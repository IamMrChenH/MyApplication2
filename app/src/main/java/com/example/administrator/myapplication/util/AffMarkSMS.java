package com.example.administrator.myapplication.util;

/**
 * 会员营销短信接口
 *
 * @ClassName: AffMarkSMS
 * @Description: 会员营销短信接口
 */
public class AffMarkSMS {
    private static String operation = "/affMarkSMS/sendSMS";

    private static String accountSid = Config.ACCOUNT_SID;
    private static String to = "18659127035";
    private static String smsContent = "【煌煌交通智能平台】尊敬的LZH，您有一条新信息，请进入APP查看，回复N退订。";

    /**
     * 会员营销短信
     */
    public static void execute(String number, String name) {
        String url = Config.BASE_URL + operation;
        String body = "accountSid=" + accountSid + "&to=" + number + "&smsContent=" + "【煌煌交通智能平台】尊敬的" + name + "，您有一条新信息，请进入APP查看，回复N退订。"
                + HttpUtil.createCommonParam();

        // 提交请求
        String result = HttpUtil.post(url, body);
        System.out.println("result:" + "\n" + result);
    }
}
