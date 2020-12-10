package epam.webtech.web;

import epam.webtech.exceptions.AuthorisationException;
import epam.webtech.exceptions.DatabaseException;
import epam.webtech.exceptions.InternalException;
import epam.webtech.exceptions.NotFoundException;
import epam.webtech.model.bet.BetDao;
import epam.webtech.model.bet.MySqlBetDao;
import epam.webtech.model.user.MySqlUserDao;
import epam.webtech.model.user.User;
import epam.webtech.model.user.UserDao;
import epam.webtech.utils.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserProfilePageServlet extends HttpServlet {

    //private final Logger logger = LogManager.getLogger();

    private UserService userService = UserService.getInstance();
    private UserDao userDao = MySqlUserDao.getInstance();
    private BetDao betDao = MySqlBetDao.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer authorityLvl = (Integer) req.getSession().getAttribute("authorityLvl");
        Integer currentUserId = (Integer) req.getSession().getAttribute("currentUserId");
        if ((0 != authorityLvl) && (null != currentUserId)) {
            try {
                User user = userDao.findById(currentUserId);
                req.setAttribute("user", user);
                req.setAttribute("bets", betDao.findByUser(user));
                req.getRequestDispatcher("/WEB-INF/pages/userProfilePage.jsp").forward(req, resp);
            } catch (DatabaseException | NotFoundException e) {
             //   logger.error(e);
                throw new InternalException(e.getMessage());
            }
        } else {
            resp.sendRedirect("login");
        }
    }

    /*
        Login
    */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        try {
            User user = userService.approveUser(name, password);
            req.getSession().setAttribute("currentUserId", user.getId());
            req.getSession().setAttribute("authorityLvl", user.getAuthorityLvl());
            resp.sendRedirect("profile");
        } catch (AuthorisationException e) {
            resp.sendRedirect("login?errorMsg=" + e.getMessage());
        }
    }
}
