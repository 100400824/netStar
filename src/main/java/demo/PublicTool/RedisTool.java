package demo.PublicTool;

import redis.clients.jedis.Jedis;

import java.util.Map;

public class RedisTool {


    //设置redis
    public static void setRedis(Map<String, String> map,String host,int port) {

        if (map.size() != 1) {

            System.out.println("map存储错误。");

            return;
        }

        String key = "", value = "";

        for (String keyStr : map.keySet()) {

            key = keyStr;

            value = map.get(key);
        }

        Jedis jedis = new Jedis(host, port);

        jedis.set(key, value);

        jedis.close();
    }
}
