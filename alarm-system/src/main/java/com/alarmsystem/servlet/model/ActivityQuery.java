package com.alarmsystem.servlet.model;

import com.alarmsystem.servlet.Session;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ActivityQuery {
    private LinkedList<Activity> resultList;

    public ActivityQuery(String homeId) {
        getData(homeId);
    }

    protected void getData(String homeId) {
        CqlSession session = Session.getInstance();
        String cql = "SELECT datetime, event, code_used FROM home_security.activity WHERE home_id = '" + homeId + "'";
        SimpleStatement statement = SimpleStatement.newInstance(cql);
        ResultSet result = session.execute(statement);
        List<Row> rows = result.all();

        System.out.println(cql);
        System.out.println(String.format("Found %d records", rows.size()));

        resultList = new LinkedList<Activity>();

        for (Row row : rows) {
            Activity activity = new Activity();
            activity.setDatetime(row.getInstant("datetime").toString());
            activity.setEvent(row.getString("event"));
            activity.setCodeUsed(row.getString("code_used"));
            resultList.add(activity);
        }
    }

    public Iterator<Activity> getResultIterator() {
        return resultList.iterator();
    }
}
