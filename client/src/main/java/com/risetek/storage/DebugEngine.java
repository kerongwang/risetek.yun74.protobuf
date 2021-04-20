package com.risetek.storage;

import java.sql.SQLException;

import javax.inject.Singleton;

import com.risetek.yun74.shared.protobuf.SessionStatus.SessionBrief;
import com.risetek.yun74.shared.protobuf.SessionStatus.SessionBrief.Fire;
import com.risetek.yun74.shared.protobuf.SessionStatus.SessionBrief.Modem;
import com.risetek.yun74.shared.protobuf.SessionStatus.SessionBrief.Status;

@Singleton
public class DebugEngine implements IEngine {
	private String makeTableName(Status status) {
		String name = new StringBuffer().append("T").append(status.getId()).append(status.getModel()).toString();
		return name.trim().replace(" ", "_").replace(".", "_");
	}

	private String makeFireTableName(Status status) {
		String name = new StringBuffer().append("V0F").append(status.getId()).append(status.getModel()).toString();
		return name.trim().replace(" ", "_").replace(".", "_");
	}

	@Override
	public void upInsert(SessionBrief brief) throws SQLException {
		brief.getStatusList().forEach(status -> {
			upInsertStatus(status);
			status.getModemsList().forEach(modem -> upInsertModem(modem));
			status.getFiresList().forEach(fire -> upInsertFire(status, fire));
		});
	}
	

	private void upInsertStatus(Status status) {
		System.out.println("--: " + makeTableName(status) + " TTL: " + status.getTTL() + " RTT: "
				+ status.getRtt() + " LOST: " + status.getLost() + " PACKET: " + status.getPacket());

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

	private void upInsertModem(Modem modem) {
		if(!modem.hasModemImei())
			return;
		System.out.println(" ->: IMEI: " + modem.getModemImei() + " IMSI: " + modem.getModemImsi());
		// System.out.println("     PPP-in: " + modem.getPppIn() + " out: " + modem.getPppOut());
		System.out.println("   remote address: " + modem.getRemoteAddress());
	}
	
	private void upInsertFire(Status status, Fire fire) {
		System.out.println(makeFireTableName(status) + "Fire at: " + fire.getFireTime());
	}
}
