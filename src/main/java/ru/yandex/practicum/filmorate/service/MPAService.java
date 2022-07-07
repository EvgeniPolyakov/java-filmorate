package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.mpa.MPAStorage;

import java.util.List;

@Service
public class MPAService {
    private final MPAStorage mpaStorage;

    @Autowired
    public MPAService(MPAStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<MPARating> getMPAValues() {
        return mpaStorage.getMPAValues();
    }

    public MPARating getMPAById(Long id) {
        validateMPAId(id);
        return mpaStorage.getMPAById(id);
    }

    private void validateMPAId(Long id) {
        if (mpaStorage.getMPAById(id) == null) {
            throw new NotFoundException(String.format("Рейтинг MPA c id %s не найден.", id));
        }
    }
}
