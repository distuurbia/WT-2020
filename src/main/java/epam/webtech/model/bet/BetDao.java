package epam.webtech.model.bet;

import epam.webtech.exceptions.DatabaseException;
import epam.webtech.exceptions.NotFoundException;
import epam.webtech.model.Dao;
import epam.webtech.model.horse.Horse;
import epam.webtech.model.race.Race;
import epam.webtech.model.user.User;

import java.util.List;

public interface BetDao extends Dao<Bet> {

    List<Bet> findByUser(User user) throws DatabaseException, NotFoundException;

    List<Bet> findByRace(Race race) throws DatabaseException, NotFoundException;

    List<Bet> findByHorse(Horse horse) throws DatabaseException, NotFoundException;
}
