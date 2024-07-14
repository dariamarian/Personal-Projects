package com.example.gamificationapp.domain.validators;

import com.example.gamificationapp.domain.Player;
import com.example.gamificationapp.exceptions.ValidationException;

import java.util.Objects;

public class PlayerValidator implements Validator<Player> {
    private static final PlayerValidator instance = new PlayerValidator();

    public static PlayerValidator getInstance() {
        return instance;
    }

    private PlayerValidator() {
    }

    @Override
    public void validate(Player entity) throws ValidationException {
        String errors = "";
        if (Objects.equals(entity.getUsername(), ""))
            errors += "invalid username\n";
        if (Objects.equals(entity.getPassword(), ""))
            errors += "invalid password\n";
        if (errors.length() > 0)
            throw new ValidationException(errors);
    }
}
