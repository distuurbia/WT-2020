package epam.webtech.model.user;

import epam.webtech.exceptions.AlreadyExistsException;
import epam.webtech.exceptions.DatabaseException;
import epam.webtech.exceptions.NotFoundException;
import epam.webtech.utils.JdbcService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MySqlUserDao implements UserDao {

    private static final String TABLE = "users";
    private static final String ADD_QUERY = "INSERT INTO " + TABLE
            + "(user_name, user_password_hash, user_bank, user_authority_lvl) VALUES (?, ?, ?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM " + TABLE + " WHERE user_id = ? ;";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM " + TABLE + " WHERE user_name = ? ;";
    private static final String FIND_ALL_QUERY = "SELECT * FROM " + TABLE + ";";
    private static final String DELETE_QUERY = "DELETE FROM " + TABLE + " WHERE user_id = ?;";
    private static final String UPDATE_QUERY = "UPDATE " + TABLE
            + " SET user_name = ?, user_password_hash = ?, user_bank = ?, user_authority_lvl = ? WHERE user_id = ?";

  //  private final Logger logger = LogManager.getLogger();

    private JdbcService jdbcService = JdbcService.getInstance();

    private static class SingletonHandler {
        static final MySqlUserDao INSTANCE = new MySqlUserDao();
    }

    public static MySqlUserDao getInstance() {
        return MySqlUserDao.SingletonHandler.INSTANCE;
    }

    @Override
    public void add(User user) throws DatabaseException, AlreadyExistsException {
        try {
            findByName(user.getName());
            throw new AlreadyExistsException("User with name " + user.getName() + " already exists");
        } catch (NotFoundException e) {
            try (PreparedStatement preparedStatement = jdbcService.getConnection()
                    .prepareStatement(ADD_QUERY, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getPasswordHash());
                preparedStatement.setInt(3, user.getBank());
                preparedStatement.setInt(4, user.getAuthorityLvl());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new DatabaseException("Creating user failed, no rows affected.");
                }
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1));
                    } else {
                        throw new DatabaseException("Creating user failed, no ID obtained.");
                    }
                }
            } catch (SQLException ex) {
      //          logger.error(ex);
                throw new DatabaseException("Database error");
            }
        }
    }

    @Override
    public User findById(int id) throws DatabaseException, NotFoundException {
        User user;
        try (PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(FIND_BY_ID_QUERY)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.first()) {
                    user = getUserFromResultSet(resultSet);
                } else {
                    throw new NotFoundException("User with id " + id + " not found");
                }
            }
        } catch (SQLException e) {
     //       logger.error(e);
            throw new DatabaseException("Database error");
        }
        return user;
    }

    @Override
    public User findByName(String name) throws NotFoundException, DatabaseException {
        User user;
        try (PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(FIND_BY_NAME_QUERY)) {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.first()) {
                    user = getUserFromResultSet(resultSet);
                } else {
                    throw new NotFoundException("User with name " + name + " not found");
                }
            }
        } catch (SQLException e) {
     //       logger.error(e);
            throw new DatabaseException("Database error");
        }
        return user;
    }

    @Override
    public List<User> findAll() throws DatabaseException {
        List<User> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(FIND_ALL_QUERY)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(getUserFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
     //       logger.error(e);
            throw new DatabaseException("Database error");
        } catch (DatabaseException e) {
     //       logger.error(e);
            throw e;
        }
        return users;
    }

    @Override
    public void update(User user) throws DatabaseException, NotFoundException {
        findById(user.getId());
        try (PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(UPDATE_QUERY)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPasswordHash());
            preparedStatement.setInt(3, user.getBank());
            preparedStatement.setInt(4, user.getAuthorityLvl());
            preparedStatement.setInt(5, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
      //      logger.error(e);
            throw new DatabaseException("Database error");
        }
    }

    @Override
    public void delete(User user) throws DatabaseException, NotFoundException {
        findById(user.getId());
        try (PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(DELETE_QUERY)) {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
     //       logger.error(e);
            throw new DatabaseException("Database error");
        }
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("user_id"));
        user.setName(resultSet.getString("user_name"));
        user.setPasswordHash(resultSet.getString("user_password_hash"));
        user.setBank(resultSet.getInt("user_bank"));
        user.setAuthorityLvl(resultSet.getInt("user_authority_lvl"));
        return user;
    }
}
