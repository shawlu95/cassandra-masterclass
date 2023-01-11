package com.vehicletracker.servlet;

import com.datastax.oss.driver.api.core.CqlSession;

import java.net.InetSocketAddress;

public class Connection {
    private static CqlSession instance = null;

    public static CqlSession getInstance() {
        if (instance == null) {
            instance = createCqlSession();
        }
        return instance;
    }

    protected static CqlSession createCqlSession() {
        return CqlSession.builder()
                .addContactPoint(InetSocketAddress.createUnresolved("localhost", 9042))
                .withKeyspace("vehicle_tracker")
                .withLocalDatacenter("datacenter1")
                .build();
    }
}
