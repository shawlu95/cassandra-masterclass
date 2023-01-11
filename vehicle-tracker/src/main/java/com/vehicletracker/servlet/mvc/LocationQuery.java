package com.vehicletracker.servlet.mvc;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.vehicletracker.servlet.Session;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class LocationQuery {
    private CqlSession session = null;
    private ResultSet result = null;
    private LinkedList <Location> resultList;

    public LocationQuery(String vehicleId, String trackDate) {
        getData(vehicleId, trackDate);
    }

    protected void getData(String vehicleId, String trackDate) {
        session = Session.getInstance();
        String cql = "select time, latitude, longitude from location where vehicle_id = '"
                + vehicleId + "' and date = '" + trackDate + "';";
        SimpleStatement statement = SimpleStatement.newInstance(cql);
        ResultSet result = session.execute(statement);
        List<Row> rows = result.all();

        System.out.println(cql);
        System.out.println(String.format("Found %d records", rows.size()));

        resultList = new LinkedList<Location>();

        for (Row row : rows) {
            Location location = new Location();
            location.setTime(row.getInstant("time").toString());
            location.setLatitude(row.getDouble("latitude"));
            location.setLongitude(row.getDouble("longitude"));

            resultList.add(location);
        }
    }

    public Iterator<Location> getResultIterator() {
        return resultList.iterator();
    }
}
