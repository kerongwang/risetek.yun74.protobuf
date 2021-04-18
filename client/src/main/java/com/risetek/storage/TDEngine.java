package com.risetek.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.inject.Singleton;

import com.risetek.yun74.shared.protobuf.SessionStatus.SessionBrief;
import com.taosdata.jdbc.TSDBDriver;

@Singleton
public class TDEngine {

//	private final String jdbcUrl = "jdbc:TAOS://127.0.0.1/";
	private final String jdbcUrl = "jdbc:TAOS://localhost:6030/";

	private Connection conn = null;

	public TDEngine() {
		try {
			System.out.println("loading driver");
			Class.forName("com.taosdata.jdbc.TSDBDriver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		
		try {
			System.out.println("loading connection");
			Properties connProps = new Properties();
			connProps.setProperty(TSDBDriver.PROPERTY_KEY_CONFIG_DIR, "/etc/taos");
			connProps.setProperty(TSDBDriver.PROPERTY_KEY_CHARSET, "UTF-8");
			connProps.setProperty(TSDBDriver.PROPERTY_KEY_LOCALE, "en_US.UTF-8");
			connProps.setProperty(TSDBDriver.PROPERTY_KEY_TIME_ZONE, "UTC-8");
			System.out.println("connect");

			conn = DriverManager.getConnection(jdbcUrl, connProps);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("connect2");
		try(Statement stmt = conn.createStatement()) {
			// create database
			stmt.executeUpdate("create database if not exists yun74");
			// use database
			stmt.executeUpdate("use yun74");
			
			stmt.executeUpdate("CREATE STABLE meters (ts timestamp, sysup int) TAGS (groupdId int);");			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			sessionStatement = conn.prepareStatement(sessionSql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private PreparedStatement sessionStatement = null;
	private final String sessionSql = "INSERT INTO ? USING METERS TAGS (?) VALUES (now, ?)";
	public void upInsert(SessionBrief brief) throws SQLException {
		brief.getStatusList().forEach(status -> {
			System.out.println(status.getId());
			System.out.println(status.getSysup());
		});
		System.out.println("STEP 2");
		if(null == conn || null == sessionStatement)
			return;

		// 自动建表：在某些特殊场景中，用户在写数据时并不确定某个数据采集点的表是否存在，此时可在写入数据时使用自动建表语法来创建不存在的表，若该表已存在则不会建立新表。比如：
		sessionStatement.setString(1, "T00001");
		sessionStatement.setInt(2, 2);
		sessionStatement.setInt(3, 2);
		sessionStatement.execute();
		System.out.println("STEP 1");
	}
}
