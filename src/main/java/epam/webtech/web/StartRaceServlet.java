package epam.webtech.web;

import epam.webtech.exceptions.DatabaseException;
import epam.webtech.exceptions.InternalException;
import epam.webtech.exceptions.NotFoundException;
import epam.webtech.model.enums.RaceStatus;
import epam.webtech.model.race.MySqlRaceDao;
import epam.webtech.model.race.Race;
import epam.webtech.model.race.RaceDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StartRaceServlet extends HttpServlet {

   // private final Logger logger = LogManager.getLogger();

    private RaceDao raceDao = MySqlRaceDao.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int raceId;
        try {
            raceId = Integer.parseInt(req.getParameter("raceid"));
            Race race = raceDao.findById(raceId);
            race.setStatus(RaceStatus.IN_PROGRESS);
            raceDao.update(race);
            resp.sendRedirect("../races");
        } catch (NumberFormatException | DatabaseException | NotFoundException e) {
           // logger.error(e);
            throw new InternalException("Internal error " + e.getMessage());
        }
    }
}
