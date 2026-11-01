package minibank.repo;

import java.util.Collection;
import java.util.Optional;

public interface Repository<T> {
    void save(T item);
    Optional<T> findById(int id);
    Collection<T> findAll();
}
