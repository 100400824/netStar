package demo.server;

import com.alibaba.fastjson.JSON;
import demo.PublicTool.*;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * 获取各策略属性值
 * 根据策略生成对应的场景
 * 调用后台接口获取策略指标值
 * 调用JS监控返回结果，是否与期望结果一致
 * */

public class LinYuanDoCheck {

    public static void main(String[] args) throws Exception {

        doPolicyCheck();
    }

    public static void doPolicyCheck() throws Exception {

        String cpid = "zpwcpid123";

        String billingname = "zpwBaidu";

        Map<String, String> map;

        int caseNum = ExcelTest.getRowNum(FileManage.linyuanADcasePath, LinyuanMakeCase.sheetName);

        int caseWrongNum = 0;

        for (int i = 1; i <= caseNum-1; i++) {

            //获取Excel中数据，拼装成map
            map = LinyuanMakeCase.getPolicy(i);

            //将整体的map拆分成条件和结果两个MAP
            Map<String, Map<String, String>> splitMap;

            splitMap = LinyuanMakeCase.splitMap(map);

            Map<String, String> policyMap;

            Map<String, String> verifyMap;

            policyMap = splitMap.get("policy");

            verifyMap = splitMap.get("verify");

            String uid = UUID.randomUUID().toString().replace("-", "");

            for (String key : policyMap.keySet()) {

                if (key.contains("1") || key.contains("0")) {

                    Map<String, String> redisMap;

                    //获取redis中需要设置的key-value
                    redisMap = doPolicy(key, policyMap.get(key), uid, cpid, billingname);

                    //设置Redis
                    RedisTool.setRedis(redisMap,"192.168.11.170",30000);
                }
            }

            //请求点击校准接口
            String entityStr = getClieckInfo(uid, cpid, billingname);

            //执行JS
            String[] jsResult = TestJS.doJs(entityStr);

            Boolean checkStatus = LinYuanVerify.checkADResult(jsResult, verifyMap, policyMap);

            if (!checkStatus) {

                caseWrongNum++;
            }

            PrintResult.printSomething(checkStatus, "" + i, jsResult, verifyMap);

        }

        System.out.println("失败case数量" + caseWrongNum);
    }

    private static String getClieckInfo(String uid, String cpid, String billingname) throws Exception {

        String url = FileManage.linyuanURLHost + "/api/v2/get_click_calibration_info";

        Map<String, String> jsonMap = new HashMap<>();

        jsonMap.put("cpId", cpid);

        jsonMap.put("billingName", billingname);

        jsonMap.put("engineId", "0");

        jsonMap.put("userId", uid);

        String entity = DoHttp.toPost(url, jsonMap);

        Map maps = (Map) JSON.parse(entity);

        entity = "" + maps.get("data");

        return entity;

    }

    private static Map<String, String> doPolicy(String policyName, String policyStatus, String uid, String cpid, String billingname) {

        String engineid, query, date;

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式

        engineid = "0";

        query = "奇迹";

        date = df.format(new Date());

        Map<String, String> map;

        map = LinYuanPolicySet.generateKey(policyName, cpid, engineid, billingname, uid, query, date, policyStatus);

        return map;
    }

}
