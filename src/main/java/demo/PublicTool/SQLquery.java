package demo.PublicTool;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SQLquery {

    //DSP
/*    private static final String DB_URL = "jdbc:mysql://39.105.116.176:3306/dsp_v1_info";
    private static final String USER = "daijun_root";
    private static final String PASS = "G4UD1nq31@Z4d9EG";*/

    //临渊
    private static final String DB_URL = "jdbc:mysql://mysql.testing.star-media.cn:32008/linyuan";
    private static final String USER = "linyuan_user";
    private static final String PASS = "oeK2yOYewluU";

    public static void main(String[] args) throws Exception {

        String sql = "select cp_id, engine_billing_name from search_log_20191213 where engine_billing_name != '0' group by cp_id, engine_billing_name having count(*) > 100;";//用sql接收数据库语句

        System.out.println(getSQLRowValua(sql));

    }

    public static Map<String, Object> getSQLRowValua(String sql) throws Exception {

        if (sql.contains("drop") || sql.contains("delete") || sql.contains("update") || sql.contains("truncate")) {

            sql = "SELECT * FROM agent_info WHERE agentid = '1063'";
        }

        Connection conn = null;//设置conn全局变量

        Statement stmt = null;//设置stmt全局变量

        // 注册 JDBC 驱动
        Class.forName("com.mysql.jdbc.Driver");

        // 打开链接
        conn = DriverManager.getConnection(DB_URL, USER, PASS);

        // 执行查询
        stmt = conn.createStatement();//实例化Statement对象，用stmt接收

        ResultSet rs = getSql(sql, stmt);

        Map<String, Object> map = new HashMap<>();

        map = convertMap(rs);

        rs.close();

        stmt.close();

        conn.close();

        return map;
    }


    private static ResultSet getSql(String sql, Statement stmt) throws Exception {
        //执行SQL
        return stmt.executeQuery(sql);
    }

    private static Map<String, Object> convertMap(ResultSet rs) {
        Map<String, Object> map = new TreeMap<>();
        try {
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    map.put(md.getColumnName(i), rs.getObject(i));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                rs = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return map;
        }
    }

}
