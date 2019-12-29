package demo.PublicTool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.StringReader;

public class TestJS {


    public static void main(String args[]) throws Exception {

        String jsonStr = "{\"perStayTimeFlag\":1,\"adClickRateFlag\":2,\"firstResultClickRateFlag\":2,\"allResultClickRateFlag\":2,\"userAdsAtLimitFlag\":0,\"userClickAdsFlag\":0}";

        doJs(jsonStr);
    }

    public static String[] doJs(String jsonStr) throws Exception {

        JSONObject jsonObject = JSON.parseObject(jsonStr);

        String[] routeScript = {"function judge(judgeInfo){" +
                "if (judgeInfo.userAdsAtLimitFlag != 1 && judgeInfo.adClickRateFlag == 2 && judgeInfo.userClickAdsFlag != 1 ) { " +
                "return '点击广告'; " +
                "} else if (judgeInfo.allResultClickRateFlag == 2) { " +
                "if (judgeInfo.firstResultClickRateFlag == 2) {  " +
                "return '首条结果';" +
                "} else {   " +
                "return '正常结果';" +
                "}" +
                "} else if (judgeInfo.allResultClickRateFlag == 1) { " +
                "return '不可点击';" +
                "} else if (judgeInfo.perStayTimeFlag == 1) { " +
                "return '计时圈';" +
                "}" +
                "else {return '无引导';}" +//新加
                "}", "function judge(judgeInfo){" +
                "if(judgeInfo.userAdsAtLimitFlag == 1||judgeInfo.adClickRateFlag == 1||judgeInfo.userClickAdsFlag == 1){" +
                "return '删除广告';" +
                "}" +
                "else {return '不删除广告';}" +
                "}", "function judge(judgeInfo){" +
                "if(judgeInfo.firstResultClickRateFlag == 1){" +
                "return '删除首条';" +
                "}" +
                "else {return '不删除首条';}" +
                "}"};

        String[] scriptResult = new String[3];//脚本的执行结果

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");//1.得到脚本引擎

        for (int index = 0; index < routeScript.length; index++) {

            engine.eval(new StringReader(routeScript[index]));

            //3.将引擎转换为Invocable，这样才可以掉用js的方法
            Invocable invocable = (Invocable) engine;

            //4.使用 invocable.invokeFunction掉用js脚本里的方法，第一個参数为方法名，后面的参数为被调用的js方法的入参
            scriptResult[index] = (String) invocable.invokeFunction("judge", jsonObject);

        }

        return scriptResult;
    }
}



