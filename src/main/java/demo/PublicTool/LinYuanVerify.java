package demo.PublicTool;

import java.util.Map;

public class LinYuanVerify {

    public static Boolean checkADResult(String[] result, Map<String, String> verifyMap, Map<String, String> policyMap) {

        String deleteFirst, deleteAD, refrenceStr;

        deleteFirst = verifyMap.get("删除首条");

        deleteAD = verifyMap.get("删除广告");

        refrenceStr = verifyMap.get("引导");

        switch (deleteFirst) {

            case "是":

                if (!result[2].equals("删除首条")) {

                    return false;
                }

                break;

            case "否":

                if (!result[2].equals("不删除首条")) {

                    return false;
                }

                break;

            default:

                return false;

        }

        switch (deleteAD) {

            case "是":

                if (!result[1].equals("删除广告")) {

                    return false;

                }

                break;

            case "否":

                if (!result[1].equals("不删除广告")) {

                    return false;

                }

                break;

            default:

                return false;

        }

        switch (refrenceStr) {

            case "无引导":

                if (!result[0].equals("无引导")) {

                    return false;

                }

                break;

            case "不可点击":

                if (!result[0].equals("不可点击")) {

                    return false;

                }
                break;

            case "点击广告":

                if (!result[0].equals("点击广告")) {

                    return false;
                }
                break;

            case "计时圈":

                if (!result[0].equals("计时圈")) {

                    return false;

                }
                break;

            case "首条结果":

                if (!result[0].equals("首条结果")) {

                    return false;

                }
                break;

            case "正常结果":

                if (!result[0].equals("正常结果")) {

                    return false;
                }
                break;

            default:

                return false;

        }

        return true;

    }
}
