package BankingApplicationConsoleBased.util;

import BankingApplicationConsoleBased.exception.ValidationException;

@FunctionalInterface
public interface Validation<T> {
    void validate(T value) throws ValidationException;
}
