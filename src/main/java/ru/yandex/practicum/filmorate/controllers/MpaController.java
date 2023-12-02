package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MpaController {

    MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping("/mpa")
    public List<Mpa> getAllMpa() {
        log.info("Получение всех MPA");
        List<Mpa> mpaList = mpaService.getAllMpa();
        log.info("Все MPA получены");
        return mpaList;
    }

    @GetMapping("/mpa/{id}")
    public Mpa findMpaById(@PathVariable int id) {
        log.info("Получение MPA");
        Mpa mpa = mpaService.findMpaById(id);
        log.info("MPA получен");
        return mpa;
    }
}
