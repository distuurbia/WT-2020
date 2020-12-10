package epam.webtech.model.bet;

import epam.webtech.exceptions.AlreadyExistsException;
import epam.webtech.exceptions.DatabaseException;
import epam.webtech.exceptions.NotFoundException;
import epam.webtech.model.horse.Horse;
import epam.webtech.model.horse.HorseDao;
import epam.webtech.model.horse.MySqlHorseDao;
import epam.webtech.model.race.MySqlRaceDao;
import epam.webtech.model.race.Race;
import epam.webtech.model.race.RaceDao;
import epam.webtech.model.user.MySqlUserDao;
import epam.webtech.model.user.User;
import epam.webtech.model.user.UserDao;
import epam.webtech.utils.JdbcService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MySqlBetDao implements BetDao {

    private static final String TABLE = "bets";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM " + TABLE + " WHERE bet_id = ? ;";
    private static final String FIND_BY_RACE_ID_QUERY = "SELECT * FROM " + TABLE + " WHERE bet_race_id = ? ;";
    private static final String FIND_BY_USER_ID_QUERY = "SELECT * FROM " + TABLE + " WHERE bet_user_id = ? ;";
    private static final String FIND_BY_HORSE_ID_QUERY = "SELECT * FROM " + TABLE + " WHERE bet_horse_id = ? ;";
    private static final String DELETE_QUERY = "DELETE FROM " + TABLE + " WHERE bet_id = ?;";
    private static final String FIND_ALL_QUERY = "SELECT * FROM " + TABLE + ";";
    private static final String ADD_QUERY = "INSERT INTO " + TABLE
            + " (bet_amount, bet_race_id, bet_horse_id, bet_user_id) VALUES (?, ?, ?, ?)";

  //  private final Logger logger = LogManager.getLogger();

    private JdbcService jdbcService = epam.webtech.utils.JdbcService.getInstance();
    private UserDao userDao = MySqlUserDao.getInstance();
    private HorseDao horseDao = MySqlHorseDao.getInstance();
    private RaceDao raceDao = MySqlRaceDao.getInstance();

    private static class SingletonHandler {
        static final MySqlBetDao INSTANCE = new MySqlBetDao();
    }

    public static MySqlBetDao getInstance() {
        return MySqlBetDao.SingletonHandler.INSTANCE;
    }

    @Override
    public List<Bet> findByUser(User user) throws DatabaseException, NotFoundException {
        List<Bet> bets = new ArrayList<>();
        try (PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(FIND_BY_USER_ID_QUERY)) {
            preparedStatement.setInt(1, user.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next())
                    bets.add(getBetFromResultSet(resultSet));
            }
        } catch (SQLException e) {
    //        logger.error(e);
            throw new DatabaseException("Database error");
        }
        return bets;
    }

    @Override
    public List<Bet> findByRace(Race race) throws DatabaseException, NotFoundException {
        List<Bet> bets = new ArrayList<>();
        try (PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(FIND_BY_RACE_ID_QUERY)) {
            preparedStatement.setInt(1, race.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next())
                    bets.add(getBetFromResultSet(resultSet));
            }
        } catch (SQLException e) {
     //       logger.error(e);
            throw new DatabaseException("Database error");
        }
        return bets;
    }

    @Override
    public List<Bet> findByHorse(Horse horse) throws DatabaseException, NotFoundException {
        List<Bet> bets = new ArrayList<>();
        try (PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(FIND_BY_HORSE_ID_QUERY)) {
            preparedStatement.setInt(1, horse.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next())
                    bets.add(getBetFromResultSet(resultSet));
            }
        } catch (SQLException e) {
      //      logger.error(e);
            throw new DatabaseException("Database error");
        }
        return bets;
    }

    @Override
    public void add(Bet bet) throws AlreadyExistsException, DatabaseException {
        try {
            findById(bet.getId());
            throw new AlreadyExistsException("Bet with id " + bet.getId() + " already exists");
        } catch (NotFoundException e) {
            try (PreparedStatement preparedStatement = jdbcService.getConnection()
                    .prepareStatement(ADD_QUERY, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, bet.getAmount());
                preparedStatement.setInt(2, bet.getRace().getId());
                preparedStatement.setInt(3, bet.getHorse().getId());
                preparedStatement.setInt(4, bet.getUser().getId());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new DatabaseException("Creating bet failed, no rows affected.");
                }
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        bet.setId(generatedKeys.getInt(1));
                    } else {
                        throw new DatabaseException("Creating bet failed, no ID obtained.");
                    }
                }
            } catch (SQLException ex) {
       //         logger.error(ex);
                throw new DatabaseException("Database error");
            }
        }
    }

    @Override
    public Bet findById(int id) throws DatabaseException, NotFoundException {
        Bet bet;
        try (PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(FIND_BY_ID_QUERY)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.first()) {
                    bet = getBetFromResultSet(resultSet);
                } else {
                    throw new NotFoundException("Bet with id " + id + " not found");
                }
            }
        } catch (SQLException e) {
       //     logger.error(e);
            throw new DatabaseException("Database error");
        }
        return bet;
    }

    @Override
    public List<Bet> findAll() throws DatabaseException, NotFoundException {
        List<Bet> bets = new ArrayList<>();
        try (PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(FIND_ALL_QUERY)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next())
                    bets.add(getBetFromResultSet(resultSet));
            }
        } catch (SQLException e) {
      //      logger.error(e);
            throw new DatabaseException("Database error");
        }
        return bets;
    }

    @Override
    public void update(Bet bet) throws DatabaseException, NotFoundException {
        //Bet cannot be updated
    }

    @Override
    public void delete(Bet bet) throws DatabaseException, NotFoundException {
        findById(bet.getId());
        try (PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(DELETE_QUERY)) {
            preparedStatement.setInt(1, bet.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
       //     logger.error(e);
            throw new DatabaseException("Database error");
        }
    }

    private Bet getBetFromResultSet(ResultSet resultSet) throws SQLException, DatabaseException, NotFoundException {
        Bet bet = new Bet();
        bet.setId(resultSet.getInt("bet_id"));
        bet.setAmount(resultSet.getInt("bet_amount"));
        bet.setUser(userDao.findById(resultSet.getInt("bet_user_id")));
        bet.setRace(raceDao.findById(resultSet.getInt("bet_race_id")));
        bet.setHorse(horseDao.findById(resultSet.getInt("bet_horse_id")));
        return bet;
    }
}
