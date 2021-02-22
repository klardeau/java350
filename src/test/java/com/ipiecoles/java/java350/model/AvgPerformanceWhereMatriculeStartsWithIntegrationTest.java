package com.ipiecoles.java.java350.model;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import com.ipiecoles.java.java350.service.EmployeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class AvgPerformanceWhereMatriculeStartsWithIntegrationTest {

    @Autowired
    private EmployeService employeService;

    @Autowired
    private EmployeRepository employeRepository;

    //Test de la moyenne des performances des matricules commençant par K = kinesitherapeute 
    //res devrait etre égale à 5 car 7 et 3 s'annule et 6 et 4 de meme
    @Test
    public void testAvgPerformanceWhereMatriculeStartsWith() throws EmployeException {
        //Given
        List<Employe> lstEmp = new ArrayList<Employe>();
        Employe employe = new Employe("BOB", "Kevin", "K00001", LocalDate.now(), 10000d, 7, 1d);
        Employe employe1 = new Employe("BOB", "Killian", "K00002", LocalDate.now(), 10000d, 5, 1d);
        Employe employe2 = new Employe("BOB", "Kylian", "K00003", LocalDate.now(), 10000d, 3, 1d);
        Employe employe3 = new Employe("BOB", "Kyllian", "K00004", LocalDate.now(), 10000d, 4, 1d);
        Employe employe4 = new Employe("BOB", "Kilian", "K00005", LocalDate.now(), 10000d, 6, 1d);
        lstEmp.add(employe);
        lstEmp.add(employe1);
        lstEmp.add(employe2);
        lstEmp.add(employe3);
        lstEmp.add(employe4);

        for (Employe emp: lstEmp) {
            employeRepository.save(emp);
        }

        //When
        Double res = employeRepository.avgPerformanceWhereMatriculeStartsWith("K");

        //Then
        Assertions.assertThat(res).isEqualTo(5);
    }


}
