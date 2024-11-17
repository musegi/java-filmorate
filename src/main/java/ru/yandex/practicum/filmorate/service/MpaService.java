package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MpaService {
    private final MpaStorage mpaStorage;

    public Optional<Mpa> getMpaById(Long id) {
        mpaValidCheck(id);
        return mpaStorage.getMpaById(id);
    }

    public List<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }

    public void mpaValidCheck(Long id) {
        if (!mpaStorage.containsMpa(id)) {
            throw new NotFoundException("Не существует МПА рейтинг с ID: " + id);
        }
    }
}
