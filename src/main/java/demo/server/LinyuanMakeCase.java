package demo.server;

import demo.PublicTool.ExcelTest;
import demo.PublicTool.FileManage;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LinyuanMakeCase {

    private static String sheetName = "整理分割";

    public static void main(String[] args) throws Exception {


    }

    //true：弹出阻塞广告   false：不弹出阻塞广告；
    //lockNum：阻塞广告数  readNum已经阅读广告数
    private static Boolean getLockAD(Boolean isNewUser,int lockNum, int readNum) {

        if (readNum >= lockNum && isNewUser) {

            return true;
        }

        //清空已经阅读数量
        readNum = 0;

        return false;
    }


    public static Map<String, String> getPolicy(int i) throws Exception {

        int columnNum;

        columnNum = ExcelTest.getColumnNum(FileManage.linyuanADcasePath, sheetName);

        XSSFSheet excelWSheet = initExcel();

        String[] policyName = ExcelTest.getRowValueSoon(excelWSheet, 0, columnNum);

        String[] policyValue = null;

        Map<String, String> map = new HashMap<>();

        //测试先查第一条

        policyValue = ExcelTest.getRowValueSoon(excelWSheet, i, columnNum);

        for (int j = 0; j < columnNum; j++) {

            map.put(policyName[j], policyValue[j]);
        }

        return map;
    }

    //将一行中的数值分成规则与校验存储到map<String , map>中
    public static Map<String, Map<String, String>> splitMap(Map<String, String> map) {

        Map<String, String> policyMap = new HashMap<>();

        Map<String, String> verifyMap = new HashMap<>();

        Map<String, Map<String, String>> splitMap = new HashMap<>();

        for (String mapKey : map.keySet()) {

            //用例反查编号
            if (mapKey.contains("场景编号")) {

                policyMap.put(mapKey, map.get(mapKey));
            }

            if (mapKey.contains("policy")) {

                policyMap.put(mapKey, map.get(mapKey));

            } else if (!mapKey.contains("场景编号")) {

                verifyMap.put(mapKey, map.get(mapKey));
            }
        }

        splitMap.put("policy", policyMap);

        splitMap.put("verify", verifyMap);

        return splitMap;
    }

    private static XSSFSheet initExcel() throws IOException {

        XSSFWorkbook excelWBook;

        XSSFSheet excelWSheet;

        FileInputStream ExcelFile = new FileInputStream(FileManage.linyuanADcasePath);

        excelWBook = new XSSFWorkbook(ExcelFile);

        excelWSheet = excelWBook.getSheet(sheetName);

        return excelWSheet;
    }


}
