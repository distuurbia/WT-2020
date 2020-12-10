package epam.webtech.web;

import epam.webtech.exceptions.AlreadyExistsException;
import epam.webtech.model.user.User;
import epam.webtech.utils.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginPageServlet extends HttpServlet {

    //private final Logger logger = LogManager.getLogger();

    private UserService userService = UserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer authorityLvl = (Integer) req.getSession().getAttribute("authorityLvl");
        if (0 != authorityLvl) {
            resp.sendRedirect("profile");
        } else {
            req.getRequestDispatcher("/WEB-INF/pages/loginPage.jsp").forward(req, resp);
        }

    }

    /*
        Registration
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        User user;
        try {
            user = userService.registerUser(name, password);
            req.getSession().setAttribute("currentUserId", user.getId());
            req.getSession().setAttribute("authorityLvl", user.getAuthorityLvl());
            resp.sendRedirect("profile");
        } catch (AlreadyExistsException e) {
            resp.sendRedirect("login?errorMsg=" + e.getMessage());
        }
    }
}
