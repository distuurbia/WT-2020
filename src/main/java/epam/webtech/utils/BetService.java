package epam.webtech.utils;

import epam.webtech.exceptions.*;
import epam.webtech.model.bet.Bet;
import epam.webtech.model.bet.BetDao;
import epam.webtech.model.bet.MySqlBetDao;
import epam.webtech.model.enums.RaceStatus;
import epam.webtech.model.horse.Horse;
import epam.webtech.model.race.MySqlRaceDao;
import epam.webtech.model.race.Race;
import epam.webtech.model.race.RaceDao;
import epam.webtech.model.user.MySqlUserDao;
import epam.webtech.model.user.User;
import epam.webtech.model.user.UserDao;

import java.util.List;
import java.util.stream.Collectors;

public class BetService {

    //private final Logger logger = LogManager.getLogger();

    private BetDao betDao = MySqlBetDao.getInstance();
    private UserDao userDao = MySqlUserDao.getInstance();
    private RaceDao raceDao = MySqlRaceDao.getInstance();

    public void makeBet(User user, Race race, Horse horse, int amount) throws NotEnoughMoneyException, DatabaseException, AlreadyExistsException, NotFoundException {
        if (user.getBank() < amount) {
            throw new NotEnoughMoneyException();
        } else {
            Bet bet = new Bet();
            bet.setUser(user);
            bet.setHorse(horse);
            bet.setRace(race);
            bet.setAmount(amount);
            user.setBank(user.getBank() - amount);
            userDao.update(user);
            betDao.add(bet);
        }
    }

    public void finishRace(Race race) throws InternalException {
        if (!race.getStatus().equals(RaceStatus.IN_PROGRESS)) {
            throw new InternalException("Race isn't in progress");
        } else {
            try {
                race.setStatus(RaceStatus.FINISHED);
                raceDao.update(race);
                List<Bet> allBets = betDao.findAll();
                List<Bet> winningBets = allBets.stream()
                        .filter(bet -> bet.getRace().equals(race))
                        .filter(bet -> bet.getHorse().equals(race.getWinnerHorse()))
                        .collect(Collectors.toList());
                if (winningBets.size() > 0) {
                    int multiplier = allBets.size() / winningBets.size();
                    for (Bet bet : winningBets) {
                        bet.getUser().setBank(bet.getUser().getBank() + Math.round(bet.getAmount() * multiplier * 0.7f));
                        userDao.update(bet.getUser());

                    }
                }
                for (Bet bet : allBets) {
                    betDao.delete(bet);
                }
            } catch (DatabaseException | NotFoundException e) {
        //        logger.error(e);
                throw new InternalException("Internal error: " + e.getMessage());
            }
        }
    }

    public static BetService getInstance() {
        return BetService.SingletonHandler.INSTANCE;
    }

    private static class SingletonHandler {
        static final BetService INSTANCE = new BetService();
    }
}
