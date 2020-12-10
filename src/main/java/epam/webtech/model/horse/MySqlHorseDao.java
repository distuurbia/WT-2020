package epam.webtech.model.horse;

import epam.webtech.exceptions.AlreadyExistsException;
import epam.webtech.exceptions.DatabaseException;
import epam.webtech.exceptions.NotFoundException;
import epam.webtech.model.race.Race;
import epam.webtech.utils.JdbcService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MySqlHorseDao implements HorseDao {

    private static final String TABLE = "horses";
    private static final String LINK_TABLE = "racehorses";
    private static final String LINK_QUERY = "SELECT * FROM " + LINK_TABLE + " WHERE racehorse_race_id = ?;";
    private static final String ADD_QUERY = "INSERT INTO " + TABLE
            + "(horse_name, horse_wins_counter) VALUES (?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM " + TABLE + " WHERE horse_id = ? ;";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM " + TABLE + " WHERE horse_name = ? ;";
    private static final String FIND_ALL_QUERY = "SELECT * FROM " + TABLE + ";";
    private static final String DELETE_QUERY = "DELETE FROM " + TABLE + " WHERE horse_id = ?;";
    private static final String UPDATE_QUERY = "UPDATE " + TABLE
            + " SET horse_name = ?, horse_wins_counter = ? WHERE horse_id = ?";

  //  private final Logger logger = LogManager.getLogger();

    private JdbcService jdbcService = epam.webtech.utils.JdbcService.getInstance();

    private static class SingletonHandler {
        static final MySqlHorseDao INSTANCE = new MySqlHorseDao();
    }

    public static MySqlHorseDao getInstance() {
        return MySqlHorseDao.SingletonHandler.INSTANCE;
    }

    @Override
    public Horse findByName(String name) throws NotFoundException, DatabaseException {
        Horse horse;
        try (PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(FIND_BY_NAME_QUERY)) {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.first()) {
                    horse = getHorseFromResultSet(resultSet);
                } else {
                    throw new NotFoundException("Horse with name " + name + " not found");
                }
            }
        } catch (SQLException e) {
    //        logger.error(e);
            throw new DatabaseException("Database error");
        }
        return horse;
    }

    @Override
    public List<Horse> findByRace(Race race) throws DatabaseException, NotFoundException {
        List<Horse> horses = new ArrayList<>();
        try (PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(LINK_QUERY)) {
            preparedStatement.setInt(1, race.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                horses.add(findById(resultSet.getInt("racehorse_horse_id")));
            }
        } catch (SQLException e) {
     //       logger.error(e);
            throw new DatabaseException("Database error");
        }
        return horses;
    }

    @Override
    public void add(Horse horse) throws DatabaseException, AlreadyExistsException {
        try {
            findByName(horse.getName());
            throw new AlreadyExistsException("Horse with name " + horse.getName() + " already exists");
        } catch (NotFoundException e) {
            try (PreparedStatement preparedStatement = jdbcService.getConnection()
                    .prepareStatement(ADD_QUERY, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, horse.getName());
                preparedStatement.setInt(2, horse.getWinsCounter());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new DatabaseException("Creating horse failed, no rows affected.");
                }
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        horse.setId(generatedKeys.getInt(1));
                    } else {
                        throw new DatabaseException("Creating horse failed, no ID obtained.");
                    }
                }
            } catch (SQLException ex) {
        //        logger.error(ex);
                throw new DatabaseException("Database error");
            }
        }
    }

    @Override
    public Horse findById(int id) throws DatabaseException, NotFoundException {
        Horse horse;
        try (PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(FIND_BY_ID_QUERY)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.first()) {
                    horse = getHorseFromResultSet(resultSet);
                } else {
                    throw new NotFoundException("Horse with id " + id + " not found");
                }
            }
        } catch (SQLException e) {
       //     logger.error(e);
            throw new DatabaseException("Database error");
        }
        return horse;
    }

    @Override
    public List<Horse> findAll() throws DatabaseException {
        List<Horse> horses = new ArrayList<>();
        try (PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(FIND_ALL_QUERY)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    horses.add(getHorseFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
       //     logger.error(e);
            throw new DatabaseException("Database error");
        } catch (DatabaseException e) {
        //    logger.error(e);
            throw e;
        }
        return horses;
    }

    @Override
    public void update(Horse horse) throws DatabaseException, NotFoundException {
        findById(horse.getId());
        try (PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(UPDATE_QUERY)) {
            preparedStatement.setString(1, horse.getName());
            preparedStatement.setInt(2, horse.getWinsCounter());
            preparedStatement.setInt(3, horse.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
       //     logger.error(e);
            throw new DatabaseException("Database error");
        }
    }

    @Override
    public void delete(Horse horse) throws DatabaseException, NotFoundException {
        findById(horse.getId());
        try (PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(DELETE_QUERY)) {
            preparedStatement.setInt(1, horse.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Database error");
        }
    }

    private Horse getHorseFromResultSet(ResultSet resultSet) throws SQLException {
        Horse horse = new Horse();
        horse.setId(resultSet.getInt("horse_id"));
        horse.setName(resultSet.getString("horse_name"));
        horse.setWinsCounter(resultSet.getInt("horse_wins_counter"));
        return horse;
    }
}
