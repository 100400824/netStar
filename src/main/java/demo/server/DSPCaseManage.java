package demo.server;

import demo.PublicTool.ExcelTest;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DSPCaseManage {

    private static CloseableHttpClient httpClient = HttpClients.createDefault();

    private static String filePath = "C:\\Users\\ninge\\Desktop\\star\\dsp\\dsp接口测试.xlsx";

    public static void main(String[] args) throws Exception {

        getAPI();
    }

    //管理执行用例
    private static void getAPI() throws Exception {

        String manageSheetName = "manage";

        int apiNum = ExcelTest.getRowNum(filePath, manageSheetName);

        String[] hostValue = ExcelTest.getRowValue(filePath, manageSheetName, 1);

        String[] urlValue;

        //遍历需要测试的接口内容
        for (int aNum = 2; aNum < apiNum; aNum++) {

            urlValue = ExcelTest.getRowValue(filePath, manageSheetName, aNum);

            if (urlValue[4].equals("open")) {

                doAPI(hostValue[3],urlValue);
            }
        }
    }

    //执行接口中所有的case
    private static void doAPI(String hostStr,String[] urlValue) throws Exception {

        String url = hostStr + urlValue[3];

        String sheetName = urlValue[2];

        //get caseNum
        int caseNum = ExcelTest.getRowNum(filePath, sheetName) - 1;

        int parameterNum = ExcelTest.getColumnNum(filePath, sheetName);

        String[] apiKey, apiValue;

        apiKey = ExcelTest.getRowValue(filePath, sheetName, 0);

        System.out.println(apiKey[0]);

        //do post
        for (int i = 1; i <= caseNum; i++) {

            apiValue = ExcelTest.getRowValue(filePath, sheetName, i);

            doPost(url, parameterNum, apiKey, apiValue);
        }
    }

    //进行接口请求
    private static void doPost(String url, int parameterNum, String[] apiKey, String[] apiValue) throws Exception {

        HttpPost httpPost = new HttpPost(url);

        Map<String, String> map = new HashMap<String, String>();

        String paraValue,uuidStr;

        for (int i = 2; i < parameterNum-1; i++) {

            paraValue = apiValue[i];

            if (paraValue.contains("UUID")) {

                uuidStr = UUID.randomUUID().toString().replace("-","");

                paraValue = paraValue.replace("UUID",uuidStr);

            }else if (paraValue.equals("null")) {

                paraValue = "";
            }

            map.put(apiKey[i], paraValue);
        }

        JSONObject jsonObject = JSONObject.fromObject(map);

        StringEntity myEntity = new StringEntity(jsonObject.toString(), "UTF-8");

        myEntity.setContentType("application/json");

        httpPost.setEntity(myEntity);

        CloseableHttpResponse response = httpClient.execute(httpPost);

        String entity = EntityUtils.toString(response.getEntity(), "UTF-8");

//        System.out.println(entity);

        doVerify(entity, apiValue,jsonObject.toString());
    }

    //校验接口返回数据
    private static void doVerify(String entity, String[] apiValue,String jsonStr) {

        String verifyValue = apiValue[apiValue.length - 1];

        if (entity.contains(verifyValue)) {

            System.out.println(apiValue[0] + "、" + apiValue[1] + ":" + "测试通过。");

/*            System.out.println("请求参数：" + jsonStr);

            System.out.println("实际返回：" + entity);*/

        } else {

            System.out.println(apiValue[0] + "、" + apiValue[1] + ":" + "测试未通过!!!");

            System.out.println("请求参数：" + jsonStr);

            System.out.println("期望返回：" + verifyValue);

            System.out.println("实际返回：" + entity);
        }
    }

}
