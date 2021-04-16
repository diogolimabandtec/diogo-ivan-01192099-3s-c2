package com.example.diogoIvan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/lutadores")
public class LutadorController {

    @Autowired
    private LutadorRepository repository;

    @GetMapping
    public ResponseEntity getLutador(){
        List<Lutador> lutadores = repository.findAll();

        if (lutadores.isEmpty()){
            return ResponseEntity.noContent().build();
        } else{
            return ResponseEntity.ok().body(repository.findAllByOrderByForcaGolpeDesc());
        }
    }

    @PostMapping
    public ResponseEntity postLutador(@RequestBody @Valid Lutador novoLutador){
        repository.save(novoLutador);
        return ResponseEntity.status(201).build();
    }

}
