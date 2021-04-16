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

    @GetMapping("/contagem-vivos")
    public ResponseEntity getTotalLutadorVivos(){
        return ResponseEntity.ok().body(repository.countByVivoIsTrue());
    }

    @PostMapping("{Id}/concentrar")
    public ResponseEntity postConcentrar(@RequestBody @RequestAttribute Integer id){

        if (repository.existsById(id)){
            Lutador lutador = repository.findById(id).get();
            if (lutador.getConcentracoesRealizadas() == 3){
                return ResponseEntity.status(401).body("Concentração já no máximo");
            } else {
                double vida = lutador.getVida() * 1.15;
                lutador.setVida(vida);
                Integer contagem = lutador.getConcentracoesRealizadas();
                contagem++;
                lutador.setConcentracoesRealizadas(contagem);
                return ResponseEntity.status(201).body("Concentração realizada");
            }
        }
            return ResponseEntity.status(404).build();
    }

    @PostMapping("/golpe")
    public ResponseEntity postGolpear(@RequestAttribute int idBate, @RequestAttribute int idApanha){
        if(idBate > 0 && idApanha > 0){
            if (repository.existsById(idBate) && repository.existsById(idApanha)){
                Lutador bate = repository.findById(idBate).get();
                Lutador apanha = repository.findById(idApanha).get();
                if(bate.isVivo() && apanha.isVivo()){
                    if(bate.getForcaGolpe() >= apanha.getVida()){
                        apanha.setVida(0);
                        apanha.setVivo(false);
                    } else {
                        double dano = bate.getForcaGolpe();
                        double sobraVida = apanha.getVida();
                        apanha.setVida(sobraVida - dano);
                    }
                    return ResponseEntity.status(200).body(repository.findByIdAndId(idBate,idApanha));
                }
            }
        }
        return ResponseEntity.status(400).build();
    }
}
