package epam.webtech.utils;

import epam.webtech.exceptions.*;
import epam.webtech.model.user.MySqlUserDao;
import epam.webtech.model.user.User;
import epam.webtech.model.user.UserDao;

public class UserService {

   // private final Logger logger = LogManager.getLogger();

    private HashService hashService = HashService.getInstance();
    private UserDao userDao = MySqlUserDao.getInstance();

    public User registerUser(String name, String password) throws InternalException, AlreadyExistsException {
        try {
            User user = new User();
            user.setName(name);
            user.setAuthorityLvl(1);
            user.setPasswordHash(hashService.getHashAsString(password));
            user.setBank(10000);
            userDao.add(user);
            return user;
        } catch (HashServiceException | DatabaseException e) {
     //       logger.error(e);
            throw new InternalException(e.getMessage());
        }
    }

    public User approveUser(String name, String password) throws AuthorisationException, InternalException {
        try {
            User user = userDao.findByName(name);
            if (user.getPasswordHash().equals(hashService.getHashAsString(password))) {
                return user;
            } else
                throw new AuthorisationException("Wrong password");
        } catch (NotFoundException e) {
            throw new AuthorisationException("User " + name + " doesn't exist.");
        } catch (DatabaseException | HashServiceException e) {
      //      logger.error(e);
            throw new InternalException(e.getMessage());
        }
    }

    public static UserService getInstance() {
        return UserService.SingletonHandler.INSTANCE;
    }

    private static class SingletonHandler {
        static final UserService INSTANCE = new UserService();
    }
}
