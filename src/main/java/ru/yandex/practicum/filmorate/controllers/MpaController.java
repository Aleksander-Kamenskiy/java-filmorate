package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {

    private final MpaService service;

    @GetMapping
    public ResponseEntity<List<Mpa>> getAllMpa() {
        List<Mpa> mpaList = service.getAllMpa();
        return ResponseEntity.ok().body(mpaList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mpa> findMpaById(@PathVariable int id) {
        Mpa mpa = service.findMpaById(id);
        return ResponseEntity.ok().body(mpa);
    }
}