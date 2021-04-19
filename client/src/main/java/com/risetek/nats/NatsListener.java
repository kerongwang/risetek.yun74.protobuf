package com.risetek.nats;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.protobuf.InvalidProtocolBufferException;
import com.risetek.storage.TDEngine;
import com.risetek.yun74.shared.protobuf.SessionStatus.SessionBrief;

import io.nats.client.ConnectionListener.Events;

import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.Options;

@Singleton
public class NatsListener extends Thread {
	@Inject TDEngine storage;

	@Override
	public void start() {
		Options options = new Options.Builder().server("nats://:4222").connectionListener((conn, type) -> {
			if(Events.CONNECTED == type) {
				// query (topic) 实时数据
				conn.createDispatcher(msg -> QueryRealMessageListener(msg)).subscribe("yun74.drop.query.real");
			} else
				System.out.println("Nats connecte type:" + type.toString());
		}).build();

		try {
			Nats.connectAsynchronously(options, true);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void QueryRealMessageListener(Message msg) {
		try {
			SessionBrief brief = SessionBrief.parseFrom(msg.getData());
			storage.upInsert(brief);
		} catch (InvalidProtocolBufferException | SQLException e) {
			e.printStackTrace();
		}
	}
}
