package com.risetek.storage;

import java.sql.SQLException;

import com.risetek.yun74.shared.protobuf.SessionStatus.SessionBrief;

public interface IEngine {
	void upInsert(SessionBrief brief) throws SQLException;
}
