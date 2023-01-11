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

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");

        String vehicleId = "CA6AFL218";
        String trackDate = "2014-05-19";
        String cql = "select time, latitude, longitude from location where vehicle_id = '"
                + vehicleId + "' and date = '" + trackDate + "';";
        SimpleStatement statement = SimpleStatement.newInstance(cql);
        ResultSet result = session.execute(statement);

        PrintWriter out = response.getWriter();
        out.println("<html><head></head><body>");
        out.println("<table>");
        for (Row row : result) {
            out.println("<tr>");
            out.println("<td>" + row.getInstant("time") + "</td>");
            out.println("<td>" + row.getDouble("latitude") + "</td>");
            out.println("<td>" + row.getDouble("longitude") + "</td>");
            out.println("</tr>");
        }
        out.println("</table>");
        out.println("</body></html>");
    }
}
