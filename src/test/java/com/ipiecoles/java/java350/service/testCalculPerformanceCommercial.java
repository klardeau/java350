package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import com.ipiecoles.java.java350.service.EmployeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class testCalculPerformanceCommercial {

    @InjectMocks
    private EmployeService employeService;

    @Mock
    private EmployeRepository employeRepository;

    //Problème, les services ont des dépendances extérieurs,
    // on fait donc des mocks pour simuler le fonctionnement des dépendances

    //Le temps d'un test pour la compréhension
    //injection du service et du repo pour faire un exemple de test d'intégration

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    //----------------------------------------------------------------------TEST calculPerformanceCommercial-------------------------------------------------//

    //caTraite null et negatif (same)
    //BEGIN--
    @Test
    public void testCalculPerformanceCommercialNullCaTraite(){

        //hasMessage permet de vérifier si augmenterSalaire à Trhow que le salaire était null
        Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial("C12345", null, 2l)).hasMessage("Le chiffre d'affaire traité ne peut être négatif ou null !");

    }

    @Test
    public void testCalculPerformanceCommercialNegaCaTraite(){

        Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial("C12345", -4l, 2l)).hasMessage("Le chiffre d'affaire traité ne peut être négatif ou null !");

    }
    //END--

    //matricule null et pas commencer par C
    //BEGIN--
    @Test
    public void testCalculPerformanceCommercialNullMatricule(){

        //hasMessage permet de vérifier si augmenterSalaire à Trhow que le salaire était null
        Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial(null, 2l, 2l)).hasMessage("Le matricule ne peut être null et doit commencer par un C !");

    }

    @Test
    public void testCalculPerformanceCommercialMatriculePasCommenceParC(){
        Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial("T0001", 2l, 2l)).hasMessage("Le matricule ne peut être null et doit commencer par un C !");

    }
    //END--

    //caTraite null et negatif (same)
    //BEGIN--
    @Test
    public void testCalculPerformanceCommercialNullObjectifCa(){

        //hasMessage permet de vérifier si augmenterSalaire à Trhow que le salaire était null
        Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial("C12345", 1l, null)).hasMessage("L'objectif de chiffre d'affaire ne peut être négatif ou null !");

    }

    @Test
    public void testCalculPerformanceCommercialNegatifObjectifCa(){

        Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial("C12345", 1l, -5l)).hasMessage("L'objectif de chiffre d'affaire ne peut être négatif ou null !");

    }
    //END--

    //je donne matricule CCCCCCC

    @Test
    public void testCalculPerformanceCommercialEmployeNull(){
        String matricule = "CCCCCCC";

        Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial(matricule, 1l, 1l)).hasMessage("Le matricule " + matricule + " n'existe pas !");

    }

    //Cas 1 il passe à travers et tombe sur 1
    @Test
    public void testCalculPerformanceCasUn() throws EmployeException {
        when(employeRepository.findByMatricule("C00001")).thenReturn(new Employe("BOB", "Billie", "C00001", LocalDate.now(), 1000d, 1, 1d));
        when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(null);//On empèche l'ajout afin de voir plus facilement si le test fonctionnent pour performance

        employeService.calculPerformanceCommercial("C00001", 1l, 2l);
        ArgumentCaptor<Employe> employe = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employe.capture());
        Assertions.assertThat(employe.getValue().getPerformance()).isEqualTo(1);
    }

    //Cas 2 inferieur entre 20 et 5 pourcent donc il trouve 2 -2 = 0 il choisira 1 car 1 sup à 0
    @Test
    public void testCalculPerformanceCasDeux() throws EmployeException {

        when(employeRepository.findByMatricule("C00001")).thenReturn(new Employe("BOB", "Billie", "C00001", LocalDate.now(), 1000d, 2, 1d));
        when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(null);

        employeService.calculPerformanceCommercial("C00001", 85l, 100l);
        ArgumentCaptor<Employe> employe = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employe.capture());
        Assertions.assertThat(employe.getValue().getPerformance()).isEqualTo(1);
    }

    //Cas 3 entre -5 et 5 pourcent donc 3(performance) + rien = 3
    @Test
    public void testCalculPerformanceCasTrois() throws EmployeException {
        when(employeRepository.findByMatricule("C00001")).thenReturn(new Employe("BOB", "Billie", "C00001", LocalDate.now(), 1000d, 3, 1d));
        when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(null);

        employeService.calculPerformanceCommercial("C00001", 96l, 100l);
        ArgumentCaptor<Employe> employe = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employe.capture());
        Assertions.assertThat(employe.getValue().getPerformance()).isEqualTo(3);
    }

    //Cas 4 sup de 5 à 20 pourcent donc 4(performance) + 1 = 5
    @Test
    public void testCalculPerformanceCasQuatre() throws EmployeException {
        when(employeRepository.findByMatricule("C00001")).thenReturn(new Employe("BOB", "Billie", "C00001", LocalDate.now(), 1000d, 4, 1d));
        when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(null);

        employeService.calculPerformanceCommercial("C00001", 115l, 100l);
        ArgumentCaptor<Employe> employe = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employe.capture());
        Assertions.assertThat(employe.getValue().getPerformance()).isEqualTo(5);
    }

    //Cas 5 sup à 20 pourcent donc 5(performance) + 4(au dessus de 20) = 9
    @Test
    public void testCalculPerformanceCasCinq() throws EmployeException {
        when(employeRepository.findByMatricule("C00001")).thenReturn(new Employe("BOB", "Billie", "C00001", LocalDate.now(), 1000d, 5, 1d));
        when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(null);

        employeService.calculPerformanceCommercial("C00001", 160l, 100l);
        ArgumentCaptor<Employe> employe = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employe.capture());
        Assertions.assertThat(employe.getValue().getPerformance()).isEqualTo(9);
    }

}
