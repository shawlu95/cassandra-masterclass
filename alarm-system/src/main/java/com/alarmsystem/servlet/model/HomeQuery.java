package com.alarmsystem.servlet.model;

import com.alarmsystem.servlet.Connection;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class HomeQuery {
    private LinkedList<Home> resultList;

    public HomeQuery(String homeId) {
        getData(homeId);
    }

    protected void getData(String homeId) {
        CqlSession session = Connection.getInstance();
        String cql =  "SELECT contact_name, address, city, state, zip FROM home_security.home WHERE home_id = '" + homeId + "'";
        SimpleStatement statement = SimpleStatement.newInstance(cql);
        ResultSet result = session.execute(statement);
        List<Row> rows = result.all();

        resultList = new LinkedList<Home>();

        for (Row row : rows) {
            Home home = new Home();
            home.setContactName(row.getString("contact_name"));
            home.setAddress(row.getString("address"));
            home.setCity(row.getString("city"));
            home.setState(row.getString("state"));
            home.setZip(row.getString("zip"));
            resultList.add(home);
        }
    }

    public Iterator<Home> getResultIterator() {
        return resultList.iterator();
    }
}
