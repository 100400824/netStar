package demo.PublicTool;

import java.util.Arrays;
import java.util.Map;

public class PrintResult {

    public static String printSomething(Boolean isRight, String caseNum, String[] result, Map<String, String> verifyMap) {

        if (isRight) {

            System.out.println(caseNum + "：测试通过。");

        } else {

            System.out.println(caseNum + "：错误，请检查！！！！");

            System.out.println("期望结果：" + verifyMap.toString());

            System.out.println("实际结果：" + Arrays.asList(result));
        }

        return "";
    }
}
