package BankingApplicationConsoleBased.repository;

import BankingApplicationConsoleBased.domain.Customer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CustomerRepository {
    private final Map<String, Customer> customerById = new HashMap<>();

    public void save(Customer customer) {
        customerById.put(customer.getId(), customer);
    }

    public Optional<Customer> findById(String customerId) {
        return Optional.ofNullable(customerById.get(customerId));
    }
}
