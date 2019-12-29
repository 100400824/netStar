package demo.PublicTool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LinYuanPolicySet {

    //生成Redis中的KEY
    public static Map<String, String> generateKey(String policyName, String cpid, String engineid, String billingname, String uid, String query, String date, String policyStatus) {

        String symbol = "-";

        String key = "";

        String value1 = "";

        String value2 = "";

        String value3 = "";

        Map<String, String> map = new HashMap<>();

        switch (policyName) {

            case "policy01"://（用户当天是否有请求记录）policy01-cpid-uid

                key = policyName + symbol + cpid + symbol + uid;

                value1 = billingname;

                value2 = "";

                return getRedisMap(key, value1, value2, value3, policyStatus);

            case "policy02"://（当前合作方当前计费名当天搜索次数大于100）policy02-cpid-billingname

                key = policyName + symbol + cpid + symbol + billingname;

                value1 = "200";

                value2 = "50";

                return getRedisMap(key, value1, value2, value3, policyStatus);

            case "policy03"://（当前引擎计费名过去24H，个人搜索次数大于15）policy03-cpid-engineid-billingname-uid

                key = policyName + symbol + cpid + symbol + engineid + symbol + billingname + symbol + uid;

                value1 = "20";

                value2 = "3";

                return getRedisMap(key, value1, value2, value3, policyStatus);

            case "policy04"://（当前引擎计费名，用户过去两天都有广告结果点击）policy04-cpid-engineid-billingname-uid

                key = policyName + symbol + cpid + symbol + engineid + symbol + billingname + symbol + uid;

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

                Calendar calendar = Calendar.getInstance();

                calendar.add(calendar.DATE, 5);

                date = df.format(calendar.getTime()).toString();

                value1 = date;

                value2 = "";

                return getRedisMap(key, value1, value2, value3, policyStatus);

            case "policy05"://（过去72H当前引擎计费名，个人搜索结果点击大于10，且广告点击率超过50%）policy05-cpid-engineid-billingname-uid

                key = policyName + symbol + cpid + symbol + engineid + symbol + billingname + symbol + uid;

                value1 = "1";

                value2 = "0";

                return getRedisMap(key, value1, value2, value3, policyStatus);

            case "policy06"://（当天，当前引擎计费名下人均搜索次数大于4且个人搜索次数大于等于3）policy06-cpid-engineid-billingname-uid

                key = policyName + symbol + cpid + symbol + engineid + symbol + billingname + symbol + uid;

                value1 = "1";

                value2 = "0";

                return getRedisMap(key, value1, value2, value3, policyStatus);

            case "policy07"://（当天，计费名下整体结果点击率位于45-60%）policy07-cpid-engineid-billingname

                key = policyName + symbol + cpid + symbol + engineid + symbol + billingname;

                value1 = "0.01";

                value2 = "0.03";

                value3 = "0.08";

                return getRedisMap(key, value1, value2, value3, policyStatus);

            case "policy08"://（当天，计费名下广告结果点击率位于2-4%）policy08-cpid-engineid-billingname

                key = policyName + symbol + cpid + symbol + engineid + symbol + billingname;

                value1 = "0.01";

                value2 = "0.03";

                value3 = "0.05";

                return getRedisMap(key, value1, value2, value3, policyStatus);

            case "policy09"://（当天，计费名下结果页人均停留时长大于10s）policy09-cpid-engineid-billingname

                key = policyName + symbol + cpid + symbol + engineid + symbol + billingname;

                value1 = "20000";

                value2 = "1000";

                return getRedisMap(key, value1, value2, value3, policyStatus);

            case "policy10"://（当天，计费名下首条结果（不包含广告）点击率高于50%）policy10-cpid-engineid-billingname

                key = policyName + symbol + cpid + symbol + engineid + symbol + billingname;

                value1 = "0.01";

                value2 = "0.03";

                value3 = "0.07";

                return getRedisMap(key, value1, value2, value3, policyStatus);

            case "policy11"://（是否存在单个词在当前计费名当天搜索超过5%）policy11-cpid-engineid-billingname-query

                key = policyName + symbol + cpid + symbol + engineid + symbol + billingname + symbol + query;

                value1 = "0.2";

                value2 = "0.01";

                return getRedisMap(key, value1, value2, value3, policyStatus);

            case "policy13"://（当天该用户广告点击次数是否大于后台设定值10次）policy04-adclick-cpid-engineid-billingname-uid-date

                key = "policy04" + symbol + "adclick" + symbol + cpid + symbol + engineid + symbol + billingname + symbol + uid + symbol + date;

                value1 = "15";

                value2 = "2";

                return getRedisMap(key, value1, value2, value3, policyStatus);

            case "policy15"://（用户at天内点击广告次数限制3天30次）policy15-cpid-engineid-userId-time

                key = policyName + symbol + cpid + symbol + engineid + symbol + uid + symbol + date;

                value1 = "40";

                value2 = "2";

                return getRedisMap(key, value1, value2, value3, policyStatus);

            default:

                map.put("错误：没有匹配的策略及状态", policyName + ":" + policyStatus);

                return map;
        }
    }

    //对key建赋值
    private static Map<String, String> getRedisMap(String key, String value1, String value2, String value3, String policyStatus) {

        String trueStr = "是", falseStr = "否";

        String minStr = "小于最小", maxStr = "大于最大", middleStr = "区间";

        Map<String, String> map = new HashMap<>();

        if (policyStatus.equals(trueStr)) {

            map.put(key, value1);

        } else if (policyStatus.equals(falseStr)) {

            map.put(key, value2);

        } else if (policyStatus.equals(minStr)) {

            map.put(key, value1);

        } else if (policyStatus.equals(middleStr)) {

            map.put(key, value2);

        } else if (policyStatus.equals(maxStr)) {

            map.put(key, value3);
        }

        return map;
    }

}
