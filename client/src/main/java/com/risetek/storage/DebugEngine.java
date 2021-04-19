package com.risetek.storage;

import java.sql.SQLException;

import javax.inject.Singleton;

import com.risetek.yun74.shared.protobuf.SessionStatus.SessionBrief;
import com.risetek.yun74.shared.protobuf.SessionStatus.SessionBrief.Status;

@Singleton
public class DebugEngine implements IEngine {
	private String makeTableName(Status status) {
		String name = new StringBuffer().append("T").append(status.getId()).append(status.getModel()).toString();
		return name.trim().replace(" ", "_").replace(".", "_");
	}

	@Override
	public void upInsert(SessionBrief brief) throws SQLException {
		for(Status status : brief.getStatusList()) {
			System.out.println("--: " + makeTableName(status));
/*			
			System.out.println("ID: " + status.getId());
			if(!isEmpty(status.getModel()))
				System.out.println(">>Module: " + status.getModel().trim());
			if(!isEmpty(status.getVersion()))
				System.out.println(">>Version: " + status.getVersion().trim());
			if(!isEmpty(status.getDeviceID()))
				System.out.println(">>DeviceID: " + status.getDeviceID().trim());
*/
		}
	}
}
