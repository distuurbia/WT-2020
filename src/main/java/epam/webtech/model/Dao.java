package epam.webtech.model;

import epam.webtech.exceptions.AlreadyExistsException;
import epam.webtech.exceptions.DatabaseException;
import epam.webtech.exceptions.NotFoundException;

import java.util.List;

public interface Dao<T> {

    void add(T object) throws DatabaseException, AlreadyExistsException;

    T findById(int id) throws DatabaseException, NotFoundException;

    List<T> findAll() throws DatabaseException, NotFoundException;

    void update(T object) throws DatabaseException, NotFoundException;

    void delete(T object) throws DatabaseException, NotFoundException;
}
