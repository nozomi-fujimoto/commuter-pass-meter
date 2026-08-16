package com.example.commuterpassmeter.commuterpass;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/commuter-pass")
public class CommuterPassController {

    private final CommuterPassService service;

    public CommuterPassController(CommuterPassService service) {
        this.service = service;
    }

    @GetMapping
    public CommuterPass getActive() {
        return service.getActive();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommuterPass create(@RequestBody CommuterPassRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public CommuterPass update(@PathVariable Long id, @RequestBody CommuterPassRequest request) {
        return service.update(id, request);
    }
}
