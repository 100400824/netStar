package demo.PublicTool;

import java.io.*;


public class FileManage {

    public static String homePath = System.getProperty("user.dir");

    public static String xx = File.separator;

    public static String dspCasePath = homePath + xx + "TargetPolicy.xlsx";

    public static String linyuanADcasePath = homePath + xx + "临渊测试拆分.xlsx";

    public static String linyuanURLHost = "http://linyuan-asw.testing.wannianli.mobi";

    public static void main(String[] args) throws Exception {

    }


    public static void writeFile(String textName, String strValue) throws Exception {

        String filePath = homePath + xx + textName + ".txt";

        File fp = new File(filePath);

        FileWriter pfp = new FileWriter(fp, true);

        pfp.write(strValue + "\r\n");

        pfp.close();

    }

}