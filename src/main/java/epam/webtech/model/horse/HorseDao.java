package epam.webtech.model.horse;

import epam.webtech.exceptions.DatabaseException;
import epam.webtech.exceptions.NotFoundException;
import epam.webtech.model.Dao;
import epam.webtech.model.race.Race;

import java.util.List;

public interface HorseDao extends Dao<Horse> {

    Horse findByName(String name) throws NotFoundException, DatabaseException;

    List<Horse> findByRace(Race race) throws DatabaseException, NotFoundException;
}
