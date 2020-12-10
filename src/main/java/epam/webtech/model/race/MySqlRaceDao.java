package epam.webtech.model.race;

import epam.webtech.exceptions.AlreadyExistsException;
import epam.webtech.exceptions.DatabaseException;
import epam.webtech.exceptions.NotFoundException;
import epam.webtech.model.enums.RaceStatus;
import epam.webtech.model.horse.Horse;
import epam.webtech.model.horse.HorseDao;
import epam.webtech.model.horse.MySqlHorseDao;
import epam.webtech.utils.JdbcService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlRaceDao implements RaceDao {

    private static final String TABLE = "races";
    private static final String LINK_TABLE = "racehorses";
    private static final String ADD_QUERY = "INSERT INTO " + TABLE
            + "(race_date, race_status, race_winner) VALUES (?, ?, ?);";
    private static final String ADD_HORSES_QUERY = "INSERT INTO " + LINK_TABLE
            + "(racehorse_race_id, racehorse_horse_id) VALUES (?, ?)";
    private static final String LINK_QUERY = "SELECT * FROM " + LINK_TABLE + " WHERE racehorse_horse_id = ?;";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM " + TABLE + " WHERE race_id = ? ;";
    private static final String FIND_ALL_QUERY = "SELECT * FROM " + TABLE + ";";
    private static final String DELETE_QUERY = "DELETE FROM " + TABLE + " WHERE race_id = ?;";
    private static final String UPDATE_QUERY = "UPDATE " + TABLE
            + " SET race_status = ?, race_winner = ? WHERE race_id = ?";

 //   private final Logger logger = LogManager.getLogger();

    private JdbcService jdbcService = JdbcService.getInstance();
    private HorseDao horseDao = MySqlHorseDao.getInstance();

    @Override
    public List<Race> findByHorse(Horse horse) throws DatabaseException, NotFoundException {
        List<Race> races = new ArrayList<>();
        try (PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(LINK_QUERY)) {
            preparedStatement.setInt(1, horse.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                races.add(findById(resultSet.getInt("racehorse_race_id")));
            }
        } catch (SQLException e) {
    //        logger.error(e);
            throw new DatabaseException("Database error.");
        }
        return races;
    }

    @Override
    public void add(Race race) throws DatabaseException, AlreadyExistsException {
        try {
            findById(race.getId());
            throw new AlreadyExistsException("Race with id " + race.getId() + " already exists.");
        } catch (NotFoundException e) {
            try (PreparedStatement preparedStatement = jdbcService.getConnection()
                    .prepareStatement(ADD_QUERY, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setLong(1, race.getDate().getTime());
                preparedStatement.setInt(2, race.getStatus().getPriority());
                preparedStatement.setString(3, "");
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new DatabaseException("Creating race failed, no rows affected.");
                }
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        race.setId(generatedKeys.getInt(1));
                    } else {
                        throw new DatabaseException("Creating race failed, no ID obtained.");
                    }
                }
                try (PreparedStatement preparedStatement1 = jdbcService.getConnection()
                        .prepareStatement(ADD_HORSES_QUERY)) {
                    for (Horse horse : race.getHorses()) {
                        preparedStatement1.setInt(1, race.getId());
                        preparedStatement1.setInt(2, horse.getId());
                        preparedStatement1.executeUpdate();
                    }
                }
            } catch (SQLException ex) {
     //           logger.error(ex);
                throw new DatabaseException("Database error.");
            }
        }
    }

    @Override
    public Race findById(int id) throws DatabaseException, NotFoundException {
        Race race;
        try (PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(FIND_BY_ID_QUERY)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.first()) {
                    race = getRaceFromResultSet(resultSet);
                    race.setHorses(horseDao.findByRace(race));
                } else {
                    throw new NotFoundException("User with id " + id + " not found.");
                }
            }
        } catch (SQLException e) {
      //      logger.error(e);
            throw new DatabaseException("Database error");
        }
        return race;
    }

    @Override
    public List<Race> findAll() throws DatabaseException {
        List<Race> races = new ArrayList<>();
        try (PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(FIND_ALL_QUERY)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Race race = getRaceFromResultSet(resultSet);
                    race.setHorses(horseDao.findByRace(race));
                    races.add(race);
                }
            }
        } catch (SQLException | NotFoundException e) {
       //     logger.error(e);
            throw new DatabaseException("Database error");
        } catch (DatabaseException e) {
       //     logger.error(e);
            throw e;
        }
        return races;
    }

    @Override
    public void update(Race race) throws DatabaseException, NotFoundException {
        findById(race.getId());
        try (PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(UPDATE_QUERY)) {
            preparedStatement.setInt(1, race.getStatus().getPriority());
            if (race.getWinnerHorse() == null) {
                preparedStatement.setString(2, "");

            } else {
                preparedStatement.setString(2, race.getWinnerHorse().getName());

            }
            preparedStatement.setInt(3, race.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
      //      logger.error(e);
            throw new DatabaseException("Database error");
        }
    }

    @Override
    public void delete(Race race) throws DatabaseException, NotFoundException {
        findById(race.getId());
        try (PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(DELETE_QUERY)) {
            preparedStatement.setInt(1, race.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
        //    logger.error(e);
            throw new DatabaseException("Database error");
        }
    }

    private static class SingletonHandler {
        static final MySqlRaceDao INSTANCE = new MySqlRaceDao();
    }

    public static MySqlRaceDao getInstance() {
        return MySqlRaceDao.SingletonHandler.INSTANCE;
    }

    private Race getRaceFromResultSet(ResultSet resultSet) throws SQLException, DatabaseException {
        Race race = new Race();
        race.setId(resultSet.getInt("race_id"));
        try {
            race.setStatus(RaceStatus.getByPriority(resultSet.getInt("race_status")));
        } catch (NotFoundException e) {
        //    logger.error(e);
            throw new DatabaseException("RaceStatus with priority " + resultSet.getInt("race_id") + " not found");
        }
        race.setDate(new Date(resultSet.getLong("race_date")));
        try {
            race.setWinnerHorse(horseDao.findByName(resultSet.getString("race_winner")));
        } catch (NotFoundException e) {
            race.setWinnerHorse(null);
        }
        return race;
    }

}
