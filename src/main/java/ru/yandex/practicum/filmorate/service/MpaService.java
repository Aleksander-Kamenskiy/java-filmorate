package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public List<Mpa> getAllMpa() {
        return mpaStorage.findAllMpa();
    }

    public Mpa findMpaById(int id) {
        Optional<Mpa> mpa = mpaStorage.findMpaById(id);
        return mpa.orElseThrow(() -> new NotFoundException("Рейтинг MPA с id " + id + " не найден"));
    }
}
