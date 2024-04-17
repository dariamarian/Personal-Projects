package com.example.gamificationapp.domain.validators;

import com.example.gamificationapp.domain.Quest;
import com.example.gamificationapp.exceptions.ValidationException;

import java.util.Objects;

public class QuestValidator implements Validator<Quest> {
    private static final QuestValidator instance = new QuestValidator();

    public static QuestValidator getInstance() {
        return instance;
    }

    private QuestValidator() {
    }

    @Override
    public void validate(Quest entity) throws ValidationException {
        String errors = "";
        if (Objects.equals(entity.getDescription(), ""))
            errors += "invalid description\n";
        if (entity.getNrOfGamesNeeded()==0)
            errors += "invalid nr of games\n";
        if (errors.length() > 0)
            throw new ValidationException(errors);
    }
}
