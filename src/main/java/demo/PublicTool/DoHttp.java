package demo.PublicTool;


import com.alibaba.fastjson.JSON;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.Map;

public class DoHttp {

    private static CloseableHttpClient httpClient = HttpClients.createDefault();

    private static CloseableHttpResponse response;

    private static String entity;

    public static void main(String[] args) {

    }

    public static String doGet(String url) throws Exception {

        HttpGet httpGet = new HttpGet(url);

        response = httpClient.execute(httpGet);

        System.out.println(response);

        entity = EntityUtils.toString(response.getEntity(), "UTF-8");

        return entity;
    }

    public static String toPost(String url, Map<String, String> map) throws Exception {

        HttpPost httpPost = new HttpPost(url);

        //Map 转成  JSONObject 字符串
        JSONObject jsonObject = JSONObject.fromObject(map);

        StringEntity myEntity = new StringEntity(jsonObject.toString());

        myEntity.setContentType("application/json");

        httpPost.setEntity(myEntity);

        response = httpClient.execute(httpPost);

        entity = EntityUtils.toString(response.getEntity(), "UTF-8");

        return entity;
    }

}
