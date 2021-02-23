package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;

import com.ipiecoles.java.java350.repository.EmployeRepository;
import com.ipiecoles.java.java350.service.EmployeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CalculPerformanceCommercialIntegrationTest {

    @Autowired
    private EmployeService employeService;

    @Autowired
    private EmployeRepository employeRepository;

    //Cas 3 donc le résultat doit etre celui de la performance
    @Test
    public void testCalculPerformanceCommercial() throws EmployeException {
        //Given
        Employe employe = new Employe("BOB", "billie", "C00001", LocalDate.now(), 10000d, 5, 1d);
        employeRepository.save(employe);

        //When
        employeService.calculPerformanceCommercial(employe.getMatricule(), 5l, 5l);

        //Then
        Assertions.assertThat(employe.getPerformance()).isEqualTo(5);
    }



}
