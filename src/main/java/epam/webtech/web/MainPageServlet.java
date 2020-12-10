package epam.webtech.web;

import epam.webtech.exceptions.DatabaseException;
import epam.webtech.exceptions.InternalException;
import epam.webtech.exceptions.NotFoundException;
import epam.webtech.model.horse.Horse;
import epam.webtech.model.horse.HorseDao;
import epam.webtech.model.horse.MySqlHorseDao;
import epam.webtech.model.race.MySqlRaceDao;
import epam.webtech.model.race.Race;
import epam.webtech.model.race.RaceDao;
import epam.webtech.utils.BetService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class MainPageServlet extends HttpServlet {

    //private final Logger logger = LogManager.getLogger();

    private RaceDao raceDao = MySqlRaceDao.getInstance();
    private HorseDao horseDao = MySqlHorseDao.getInstance();
    private BetService betService = BetService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Race> races = raceDao.findAll();
            req.setAttribute("races", races);
        } catch (DatabaseException | NotFoundException e) {
          //  logger.error(e);
            throw new InternalException("Internal error: " + e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/pages/racesPage.jsp").forward(req, resp);
    }

    /*
        Finish race
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int horseId;
        int raceId;
        try {
            horseId = Integer.parseInt(req.getParameter("winnerHorse"));
            raceId = Integer.parseInt(req.getParameter("raceid"));
            Horse horse = horseDao.findById(horseId);
            Race race = raceDao.findById(raceId);
            race.setWinnerHorse(horse);
            betService.finishRace(race);
            resp.sendRedirect("../races");
        } catch (NumberFormatException | DatabaseException | NotFoundException e) {
            //logger.error(e);
            throw new InternalException("Internal error " + e.getMessage());
        }
    }
}
