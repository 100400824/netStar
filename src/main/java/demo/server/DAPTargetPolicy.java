package demo.server;

import demo.PublicTool.DoHttp;
import demo.PublicTool.ExcelTest;
import demo.PublicTool.FileManage;
import demo.PublicTool.SQLquery;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//定向策略校验接口
public class DAPTargetPolicy {

    public static void main(String[] args) throws Exception {

        checkPolicy();
    }

    public static String checkPolicy() throws Exception {

        String filePath = FileManage.dspCasePath;

        System.out.println(filePath);

        String sheetNamePolicy = "策略表";

        String sheetDictionary = "字典表";

        int policyValueNum = ExcelTest.getRowNum(filePath, sheetNamePolicy);

        String[] policyKey = ExcelTest.getRowValue(filePath, sheetNamePolicy, 0);

        String[] policyValue;

        String[] dictionaryKey = ExcelTest.getRowValue(filePath, sheetDictionary, 0);

        String[] dictionaryValue = ExcelTest.getRowValue(filePath, sheetDictionary, 1);

        Map<String, String> dictionaryMap;

        dictionaryMap = arrayToMap(dictionaryKey, dictionaryValue);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH_mm_ss");//设置日期格式

        String timeStr = df.format(new Date());

        String textName = "定向策略" + timeStr;

        for (int i = 1; i < policyValueNum; i++) {

            policyValue = ExcelTest.getRowValue(filePath, sheetNamePolicy, i);

            //将策略表中的值转换成对应的map
            Map<String, String> urlMap = arrayToMap(policyKey, policyValue);

            //获取检查列表
            Map<String, String> checkMap = convertMap(urlMap, dictionaryMap);

            //请求URL，通过接口返回的ID，
//            Map<String, Object> resultMap = createURL(urlMap);
            String[] idArray = createURL(urlMap);

            //查询order_info表，封装成resultMap
            for (String id : idArray) {

                Map<String, Object> resultMap = getSQLResult(id);

                //校验检查结果
                System.out.println("checkMap::" + checkMap);

                System.out.println("resultMap::" + resultMap);

                if (!resultMap.isEmpty()) {

                    Boolean isVerify = checkMap(checkMap, resultMap);

                    //记录执行结果
                    writeSomething(isVerify, i, textName, checkMap, resultMap);

                } else {

                    //记录oid为空的结果
                    writeSomething(true, i, textName, checkMap, resultMap);
                }
            }
        }

        return "测试报告地址 -> " + FileManage.homePath + FileManage.xx + textName;
    }


    //拼接URL
    private static String[] createURL(Map<String, String> map) throws Exception {

        String url = "http://39.105.116.176/wjxc?";

        String queryStr = map.toString().replace("{", "").
                replace("}", "").
                replace(",", "&").
                replace(" ", "");

        url = url + queryStr;

        System.out.println(url);

        String entity = DoHttp.doGet(url);

        System.out.println(entity);

        //返回响应内容
        return getID(entity);
    }

    //获取SQL中的数据
    private static Map<String, Object> getSQLResult(String id) throws Exception {

        //oid 为空时，直接返空map，不查询SQL
        if (id.equals("ID is null")) {

            return new HashMap<>();
        }

        System.out.println("id:" + id);

        String sql = "SELECT * FROM order_info where orderid = 'idValue'";

        sql = sql.replace("idValue", id);

        System.out.println(sql);

        //sql查询结果
        return SQLquery.getSQLRowValua(sql);

    }


    private static String[] getID(String entity) {

        if (entity.contains("\"oid\":")) {

            String[] entityArray = entity.split("\"3rd\":");

            String[] idArray = new String[entityArray.length - 1];

            for (int i = 1; i < entityArray.length; i++) {

                System.out.println("截取的第" + i + "段字符串：" + entityArray[i]);

                idArray[i - 1] = entityArray[i].substring(entityArray[i].indexOf("\"oid\":\"") + "\"oid\":\"".length(), entityArray[i].indexOf("\",\"uid\""));
            }

            System.out.println("aaaaaaaaa:" + Arrays.asList(idArray));

            return idArray;

        } else {

            System.out.println("无法找到oid");

            System.out.println("entity ->" + entity);

            return new String[]{"ID is null"};
        }

    }

    private static Map<String, String> arrayToMap(String[] policyKey, String[] policyValue) {

        Map<String, String> map = new HashMap<>();

        int policyNum = policyKey.length;

        for (int i = 1; i < policyNum; i++) {

            map.put(policyKey[i], policyValue[i]);
        }

        return map;
    }

    //按照字典表中的字段，重新生成与SQL字段一致的MAP
    private static Map<String, String> convertMap(Map<String, String> policyMap, Map<String, String> dictionaryMap) {

        Map<String, String> checkMap = new HashMap<>();

        for (String key : dictionaryMap.keySet()) {

            checkMap.put(dictionaryMap.get(key), policyMap.get(key));
        }

        return checkMap;
    }

    //比对两个map中的值是否一致
    private static Boolean checkMap(Map<String, String> checkMap, Map<String, Object> resultMap) {

        String checkValue, resultValue;

        for (String key : checkMap.keySet()) {

            checkValue = checkMap.get(key);

            resultValue = resultMap.get(key).toString();

            //当检查的值不一致，并且不能为空，判断为错误
            if (!resultValue.contains(checkValue) && !resultValue.equals("")) {

                System.out.println("比对不一致。。。。。。");

                System.out.println("期望值：" + checkValue);

                System.out.println("实际值：" + resultValue);

                return false;
            }
        }
        return true;
    }

    //写入文件内容
    private static void writeSomething(Boolean isVerify, int i, String textName, Map<String, String> checkMap, Map<String, Object> resultMap) throws Exception {

        if (resultMap.isEmpty()) {

            String strEmpty = "case" + i + "：OID为空，请核对。";

            FileManage.writeFile(textName, strEmpty);

            System.out.println(strEmpty);

        } else {

            if (isVerify) {

                String str1 = "case" + i + "：测试通过。";

                FileManage.writeFile(textName, str1);

                System.out.println(str1);

            } else {

                String str2 = "case" + i + "：测试不通过，请检查。";

                FileManage.writeFile(textName, str2);

                FileManage.writeFile(textName, "校验内容：" + checkMap.toString());

                FileManage.writeFile(textName, "数据库查得内容：" + resultMap.toString());

                System.out.println(str2);
            }
        }
    }

}
