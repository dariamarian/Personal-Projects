package com.example.gamificationapp.domain.validators;


import com.example.gamificationapp.exceptions.ValidationException;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}