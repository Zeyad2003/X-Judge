package com.xjudge;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/people")
public class PersonController {
    private final PersonRepository repository;


    @GetMapping
    public List<Person> all() {
        return repository.findAll();
    }

    @PostMapping
    public Person create(@RequestBody Person person) {
        return repository.save(person);
    }
}
