package epam.webtech.web;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LanguageServlet extends HttpServlet {

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String redirectUrl = req.getParameter("source_url");
        final String language = req.getParameter("дтп");
        resp.addCookie(new Cookie("lng", language));
        resp.sendRedirect(redirectUrl);
    }
}
