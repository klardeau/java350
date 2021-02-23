package com.ipiecoles.java.java350.repository;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import com.ipiecoles.java.java350.service.EmployeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class TestInsertionEmploye {

    @Autowired
    private EmployeService employeService;

    @Autowired
    private EmployeRepository employeRepository;

    //Test si l'insertion de l'employe fonctionne (deja fais en cours)
    @Test
    public void testInsertionEmploye()throws EmployeException {
        //Given
        Employe employe = new Employe("BOB", "billie", "C00001", LocalDate.now(), 10000d, 5, 1d);
        employeRepository.save(employe);

        //When
        Employe emp = employeRepository.findByMatricule("C00001");

        //Then
        Assertions.assertThat(employe.getNom()).isEqualTo(emp.getNom());
        Assertions.assertThat(employe.getPrenom()).isEqualTo(emp.getPrenom());
        Assertions.assertThat(employe.getSalaire()).isEqualTo(emp.getSalaire());
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(emp.getTempsPartiel());
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(emp.getDateEmbauche());
        Assertions.assertThat(employe.getMatricule()).isEqualTo(emp.getMatricule());


    }
}
