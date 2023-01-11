package com.alarmsystem.servlet;

import com.alarmsystem.servlet.model.Activity;
import com.alarmsystem.servlet.model.ActivityQuery;
import com.alarmsystem.servlet.model.Home;
import com.alarmsystem.servlet.model.HomeQuery;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

@WebServlet("/status")
public class AlarmSystemServlet extends HttpServlet {
    public AlarmSystemServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String homeId = request.getParameter("h_id");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\"><html>");
        out.println("<head><title>Alarm System Status</title></head>");
        out.println("<body style=\"font:14px verdana,arial,sans-serif\">");
        out.println("<h1>Alarm System Status</h1>");
        out.println("Enter your home id to see the most recent activity for the system");
        out.println("<p>&nbsp;</p>");
        out.println("<form id=\"form1\" name=\"form1\" method=\"get\" action=\"\">");
        out.println("Home id: ");
        out.println("<input type=\"text\" name=\"h_id\" id=\"h_id\" />");
        out.println("<input type=\"submit\" name=\"submit\" id=\"submit\" value=\"Submit\"/>");
        out.println("</form>");
        out.println("<p>&nbsp;</p>");

        if (homeId != null) {
            HomeQuery homeQuery = new HomeQuery(homeId);
            Iterator<Home> homes = homeQuery.getResultIterator();
            if (homes.hasNext()) {
                Home home = homes.next();
                out.println("<p>");
                out.println("<b>" + home.getContactName() + "</b>, ");
                out.println(home.getAddress() + ", " + home.getCity() + ", " + home.getState() + ", " + home.getZip());
                out.println("</p>");
            }

            ActivityQuery activityQuery = new ActivityQuery(homeId);
            Iterator<Activity> activities = activityQuery.getResultIterator();
            if (activities.hasNext()) {
                out.println("<table style=\"font:14px verdana,arial,sans-serif\" cellpadding=\"4\">");
                while (activities.hasNext()) {
                    Activity activity = activities.next();
                    out.println("<tr>");
                    out.println("<td>" + activity.getDatetime() + "</td>");
                    out.println("<td>" + activity.getEvent() + "</td>");
                    out.println("<td>" + activity.getCodeUsed() + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            } else {
                out.println("<p>&nbsp;</p>");
                out.println("<b>Sorry</b>, no results for home id " + request.getParameter("h_id"));
            }
        }
        out.println("</body></html>");
    }
}
