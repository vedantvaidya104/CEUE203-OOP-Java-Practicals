package minibank.repo;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import minibank.model.Account;

// Thread-safe store (Practical 12): backed by a ConcurrentHashMap
public class AccountRepository implements Repository<Account> {
    private final Map<Integer, Account> store = new ConcurrentHashMap<>();

    @Override
    public void save(Account account) {
        store.put(account.getAccountNumber(), account);
    }

    @Override
    public Optional<Account> findById(int id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Collection<Account> findAll() {
        return store.values();
    }
}
