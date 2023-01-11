package com.vehicletracker.servlet;

import com.datastax.oss.driver.api.core.CqlSession;
import com.vehicletracker.servlet.mvc.Location;
import com.vehicletracker.servlet.mvc.VehicleTracker;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.Iterator;

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

        String vehicleId = request.getParameter("veh_id");
        String trackDate = request.getParameter("date_val");

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\"><html>");
        out.println("<head><title>Track a Vehicle</title></head>");
        out.println("<body>");
        out.println("<h1>Track a Vehicle</h1>");
        out.println("Enter the track date and id of the vehicle you want to track");
        out.println("<p>&nbsp;</p>");
        out.println("<form id=\"form1\" name=\"form1\" method=\"get\" action=\"\">");
        out.println("<table>");
        out.println("<tr><td>Date (e.g. 2014-05-19):</td>");
        out.println("<td><input type=\"text\" name=\"date_val\" id=\"date_val\"/></td></tr>");
        out.println("<tr><td>Vehicle id (e.g. FLN78197):</td>");
        out.println("<td><input type=\"text\" name=\"veh_id\" id=\"veh_id\" /></td></tr>");
        out.println("<tr><td></td><td><input type=\"submit\" name=\"submit\" id=\"submit\" value=\"Submit\"/></td></tr>");
        out.println("</table>");
        out.println("</form>");
        out.println("<p>&nbsp;</p>");

        if (vehicleId != null) {
            VehicleTracker vtd = new VehicleTracker(vehicleId, trackDate);
            Iterator<Location> igs = vtd.getResultIterator();
            if (igs.hasNext()) {
                out.println("<hr/>");
                out.println("<table cellpadding=\"4\">");
                out.println("<tr><td colspan=\"3\"><h2>" + request.getParameter("veh_id") + "</h2></td></tr>");
                out.println("<tr><td><b>Date and Time</b></td><td><b>Latitude</b></td><td><b>Longitude</b></td></tr>");
                while (igs.hasNext()) {
                    Location location = igs.next();
                    out.println("<tr>");
                    out.println("<td>" + location.getTime() + "</td>");
                    out.println("<td>" + location.getLatitude() + "</td>");
                    out.println("<td>" + location.getLongitude()+ "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            } else {
                out.println("<hr/>");
                out.println("<p>&nbsp;</p>");
                out.println("Sorry, no results for vehicle id " + request.getParameter("veh_id") + " for " + request.getParameter("date_val"));
            }
        }
        out.println("</body></html>");
    }
}
