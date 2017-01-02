package com.echo.servlet;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import net.sf.json.JSONArray;

public class DBUtils {
	
	
	public static String getTargetInfoFromMysql(String targetName) throws Exception{
        Connection conn = null;
        String sql1,sql2;
        String url = "jdbc:mysql://localhost:3306/wibupt?"
                + "user=root&password=root&useUnicode=true&characterEncoding=UTF8";
        
        int id = 0;
        String result = null;
        JSONArray jsonArray = new JSONArray();
        try {
        	 Class.forName("com.mysql.jdbc.Driver");
        	 System.out.println("成功加载MySQL驱动程序");
        	 conn = (Connection) DriverManager.getConnection(url);
        	 Statement stmt = (Statement) conn.createStatement();
        	 
        	 sql1 ="select groupid from arTarget where targetName=\'"+ targetName +"\'";
        	 ResultSet rs1 = stmt.executeQuery(sql1);
        	 
	         while (rs1.next()){     	
	            id = rs1.getInt(1);
	            if (id > 0) {
					break;
				}
	         }
	         
	         sql2 = "select traffic from realtimedata_in where groupid ="+ id +" order by monTime desc limit 13";
	         ResultSet rs2 = stmt.executeQuery(sql2);
	         
	         ArrayList<Integer> traffics = new ArrayList<Integer>();
	         while (rs2.next()) {
				int traffic =  rs2.getInt(1);
				traffics.add(traffic);				
			}
	         
	        jsonArray = JSONArray.fromObject(traffics);
	        System.out.println(jsonArray);
	        
        }catch (SQLException e) {
            System.out.println("MySQL操作错误");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	 
            conn.close();
        }
        return jsonArray.toString();
	}
}  
