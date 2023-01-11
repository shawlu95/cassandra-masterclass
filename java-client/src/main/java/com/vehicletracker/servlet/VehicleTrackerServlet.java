package com.vehicletracker.servlet;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;

@WebServlet("/track")
public class VehicleTrackerServlet extends HttpServlet {
    private static final  long serialVersionUID = 1L;
    CqlSession session;

    public VehicleTrackerServlet () {
        super();
        session = CqlSession.builder()
                .addContactPoint(InetSocketAddress.createUnresolved("localhost", 9042))
                .withKeyspace("vehicle_tracker")
                .withLocalDatacenter("datacenter1")
                .build();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        SimpleStatement statement = SimpleStatement.newInstance("select * from location");
        ResultSet resultSet = session.execute(statement);
        Row row = resultSet.one();
        System.out.println(row.getString("vehicle_id"));

        PrintWriter out = response.getWriter();
        out.println("<h3>Hello World!</h3>");
    }
}
