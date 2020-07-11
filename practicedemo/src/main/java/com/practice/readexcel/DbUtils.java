package com.practice.readexcel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * @author 小白i
 * @date 2020/4/15
 */
public class DbUtils {

    static String url = "jdbc:mysql://localhost:3306/posp?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
    static String user = "root";
    static String password = "root";

    public static PreparedStatement getStm() throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        //获取数据库连接        
        Connection conn = null;
        PreparedStatement stmt = null;
        conn = DriverManager.getConnection(url, user, password);
        stmt = conn.prepareStatement("");
        return stmt;
    }
}
