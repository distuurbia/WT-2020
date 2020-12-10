package epam.webtech.model.race;

import epam.webtech.exceptions.DatabaseException;
import epam.webtech.exceptions.NotFoundException;
import epam.webtech.model.Dao;
import epam.webtech.model.horse.Horse;

import java.util.List;

public interface RaceDao extends Dao<Race> {

    List<Race> findByHorse(Horse horse) throws DatabaseException, NotFoundException;
}
