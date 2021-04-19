package com.risetek.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.inject.Singleton;

import com.risetek.yun74.shared.protobuf.SessionStatus.SessionBrief;
import com.risetek.yun74.shared.protobuf.SessionStatus.SessionBrief.Status;
import com.taosdata.jdbc.TSDBDriver;

@Singleton
public class TDEngine {

	private final String jdbcUrl = "jdbc:TAOS://127.0.0.1";

	private Connection conn = null;

	public TDEngine() {
		try {
			Class.forName("com.taosdata.jdbc.TSDBDriver");
			Properties connProps = new Properties();
			connProps.setProperty(TSDBDriver.PROPERTY_KEY_CONFIG_DIR, "/etc/taos");
			connProps.setProperty(TSDBDriver.PROPERTY_KEY_CHARSET, "UTF-8");
			connProps.setProperty(TSDBDriver.PROPERTY_KEY_LOCALE, "en_US.UTF-8");
			connProps.setProperty(TSDBDriver.PROPERTY_KEY_TIME_ZONE, "UTC-8");
			conn = DriverManager.getConnection(jdbcUrl, connProps);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		try(Statement stmt = conn.createStatement()) {
			// create database
			stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS yun74");
			// use database
			stmt.executeUpdate("USE yun74");
			
			stmt.executeUpdate("CREATE STABLE IF NOT EXISTS meters (ts TIMESTAMP, sysup TIMESTAMP) TAGS (groupdId INT);");			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			sessionStatement = conn.prepareStatement(sessionSql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private PreparedStatement sessionStatement = null;
	private final String sessionSql = "INSERT INTO ? USING meters TAGS (?) VALUES (NOW, ?)";
	public void upInsert(SessionBrief brief) throws SQLException {
		if(null == conn || null == sessionStatement)
			return;

		// 自动建表：在某些特殊场景中，用户在写数据时并不确定某个数据采集点的表是否存在，此时可在写入数据时使用自动建表语法来创建不存在的表，若该表已存在则不会建立新表。比如：
		brief.getStatusList().forEach(status -> upInsertStatus(status));
	}
	
	private void upInsertStatus(Status status) {
		try {
			sessionStatement.setString(1, "T" + status.getId());
			sessionStatement.setInt(2, 2);
			sessionStatement.setLong(3, status.getSysup());
			sessionStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
