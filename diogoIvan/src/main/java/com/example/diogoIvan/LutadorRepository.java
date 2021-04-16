package com.example.diogoIvan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface LutadorRepository extends JpaRepository<Lutador,Integer> {


    List<Lutador> findAllByOrderByForcaGolpeDesc();

    int countByVivoIsTrue();

    List<Lutador> findByIdAndId(Integer bate, Integer apanha);

}
