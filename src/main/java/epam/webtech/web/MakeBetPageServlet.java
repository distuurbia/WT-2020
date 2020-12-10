package epam.webtech.web;

import epam.webtech.exceptions.*;
import epam.webtech.model.horse.Horse;
import epam.webtech.model.horse.HorseDao;
import epam.webtech.model.horse.MySqlHorseDao;
import epam.webtech.model.race.MySqlRaceDao;
import epam.webtech.model.race.Race;
import epam.webtech.model.race.RaceDao;
import epam.webtech.model.user.MySqlUserDao;
import epam.webtech.model.user.User;
import epam.webtech.model.user.UserDao;
import epam.webtech.utils.BetService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MakeBetPageServlet extends HttpServlet {

    //private final Logger logger = LogManager.getLogger();

    private RaceDao raceDao = MySqlRaceDao.getInstance();
    private BetService betService = BetService.getInstance();
    private UserDao userDao = MySqlUserDao.getInstance();
    private HorseDao horseDao = MySqlHorseDao.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer authorityLvl = (Integer) req.getSession().getAttribute("authorityLvl");
        Integer currentUserId = (Integer) req.getSession().getAttribute("currentUserId");
        if ((1 == authorityLvl) && (null != currentUserId)) {
            int raceId = 0;
            try {
                raceId = Integer.parseInt(req.getParameter("raceid"));
                Race race = raceDao.findById(raceId);
                req.setAttribute("race", race);
                req.getRequestDispatcher("/WEB-INF/pages/makeBetPage.jsp").forward(req, resp);
            } catch (NumberFormatException | NotFoundException e) {
                req.setAttribute("errorMessage", "Race with id " + raceId + " not found");
                req.getRequestDispatcher("/WEB-INF/pages/notFoundErrorPage.jsp").forward(req, resp);
            } catch (DatabaseException e) {
               // logger.error(e);
                throw new InternalException("Database error");
            }
        } else {
            resp.sendRedirect("login");
        }


    }

    /*
        Make bet
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer currentUserId = (Integer) req.getSession().getAttribute("currentUserId");
        int horseId = 0;
        int amount = 0;
        int raceId = 0;
        try {
            horseId = Integer.parseInt(req.getParameter("horse"));
            Horse horse = horseDao.findById(horseId);
            User user = userDao.findById(currentUserId);
            raceId = Integer.parseInt(req.getParameter("raceid"));
            Race race = raceDao.findById(raceId);
            amount = Integer.parseInt(req.getParameter("amount"));
            betService.makeBet(user, race, horse, amount);
            resp.sendRedirect("profile");
        } catch (NumberFormatException e) {
            req.setAttribute("errorMessage", "Wrong ID");
            req.getRequestDispatcher("/WEB-INF/pages/notFoundErrorPage.jsp").forward(req, resp);
        } catch (NotFoundException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/pages/notFoundErrorPage.jsp").forward(req, resp);
        } catch (DatabaseException | NullPointerException | AlreadyExistsException e) {
           // logger.error(e);
            throw new InternalException("Internal error");
        } catch (NotEnoughMoneyException e) {
            resp.sendRedirect("makebet?errorMessage=Not enough money&raceid=" + raceId);
        }

    }
}
