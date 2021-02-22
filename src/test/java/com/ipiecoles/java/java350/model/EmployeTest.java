package com.ipiecoles.java.java350.model;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import com.ipiecoles.java.java350.service.EmployeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmployeTest {
    @InjectMocks
    private EmployeService employeService;

    @Mock
    private EmployeRepository employeRepository;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    //Embauché en 2020. On est en 2020 : 0 an ancienneté
    public void testNombreAnneeAncienneteNow(){
        //Given
        LocalDate now = LocalDate.now();
        Employe employe = new Employe();
        employe.setDateEmbauche(now);

        //When
        Integer nbAnneesAnciennete = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(nbAnneesAnciennete).isEqualTo(0);
    }

    @Test
    //Embauché en 2019. On est en 2020 : 1 an ancienneté
    public void testNombreAnneeAncienneteNMoins1(){
        //Given
        LocalDate nMoins1 = LocalDate.now().minusYears(1);
        Employe employe = new Employe();
        employe.setDateEmbauche(nMoins1);

        //When
        Integer nbAnneesAnciennete = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(nbAnneesAnciennete).isEqualTo(1);
    }

    @Test
    //Embauché en 2021. On est en 2020 : 0 an ancienneté
    public void testNombreAnneeAncienneteNPlus1(){
        //Given
        LocalDate nPlus1 = LocalDate.now().plusYears(1);
        Employe employe = new Employe();
        employe.setDateEmbauche(nPlus1);

        //When
        Integer nbAnneesAnciennete = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(nbAnneesAnciennete).isEqualTo(0);
    }

    @Test
    //Embauché en 2021. On est en 2020 : 0 an ancienneté
    public void testNombreAnneeAncienneteNull(){
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(null);

        //When
        Integer nbAnneesAnciennete = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(nbAnneesAnciennete).isEqualTo(0);
    }


    
    //EVAL--------------------------------------------------- augmenterSalaire
//Test salaire si fonctionne
    @Test
    public void testAugmenterSalaire(){
        //Given
        Double pourc = 1.3; //Augmentation de 1.3 donc 30 pourcents
        Employe employe = new Employe();
        employe.setSalaire(1000d);

        //When
        employe.augmenterSalaire(pourc);

        //Then
        Assertions.assertThat(employe.getSalaire()).isGreaterThan(1000d);

    }

    //Test si le pourcentage est négatif
    @Test
    public void testAugmenterSalaireNegatif(){
        //Given
        Double pourc = -10d;
        Employe employe = new Employe();
        employe.setSalaire(1000d);
        Double res = employe.getSalaire();

        //When
        employe.augmenterSalaire(pourc);

        //Then
        Assertions.assertThat(employe.getSalaire()).isEqualTo(res);
    }

    //Test salaire si pourcentage infèrieur à 1
    @Test
    public void testAugmenterSalaireInfUn(){
        //Given
        Double pourc = 0d;
        Employe employe = new Employe();
        employe.setSalaire(1000d);
        Double res = employe.getSalaire();

        //When
        employe.augmenterSalaire(pourc);

        //Then
        Assertions.assertThat(employe.getSalaire()).isEqualTo(res);

    }


//si l'employé n'a pas encore de salaire inscrit.
    @Test
    public void testAugmenterSalairePasSalaire(){
        //Given
        Double pourc = 1.3d;
        Employe employe = new Employe();
        employe.setSalaire(null);


        //hasMessage permet de vérifier si augmenterSalaire à Trhow que le salaire était null
        Assertions.assertThatThrownBy(() -> employe.augmenterSalaire(pourc)).hasMessage("une erreur est survenue le salaire est null");

    }

    //Test paramétré pour la NbRTT
    @ParameterizedTest
    @CsvSource({
            "'2019-01-01', 8",//(365 - 218 - 104 - 10 - 25 = 8)*1 car travail 100 pourcent du temps
            "'2021-01-01', 10",
            "'2022-01-01', 10",//(365 - 218 - 105 - 7 - 25 = 10)*1     105 samedi et dimanche
            "'2032-01-01', 11" })
    public void testNbRtt(LocalDate date, Integer nbRtt) {
        Employe employe = new Employe("Saint", "Exupery", "M00123", LocalDate.now(), 1600.0, 1, 1.0);

        Integer nb = employe.getNbRtt(date);
        Assertions.assertThat(nb).isEqualTo(nbRtt);
    }

//---------TEST calculPerformanceCommercial----------//

    //caTraite null et negatif (same)
    //BEGIN
    @Test
    public void testCalculPerformanceCommercialNullCaTraite(){

        //hasMessage permet de vérifier si augmenterSalaire à Trhow que le salaire était null
        Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial("C12345", null, 2l)).hasMessage("Le chiffre d'affaire traité ne peut être négatif ou null !");

    }

    @Test
    public void testCalculPerformanceCommercialNegaCaTraite(){

        Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial("C12345", -4l, 2l)).hasMessage("Le chiffre d'affaire traité ne peut être négatif ou null !");

    }
    //END

    //matricule null et pas commencer par C
    //BEGIN
    @Test
    public void testCalculPerformanceCommercialNullMatricule(){

        //hasMessage permet de vérifier si augmenterSalaire à Trhow que le salaire était null
        Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial(null, 2l, 2l)).hasMessage("Le matricule ne peut être null et doit commencer par un C !");

    }

    @Test
    public void testCalculPerformanceCommercialMatriculePasCommenceParC(){
        Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial("T0001", 2l, 2l)).hasMessage("Le matricule ne peut être null et doit commencer par un C !");

    }
    //END

    //caTraite null et negatif (same)
    //BEGIN
    @Test
    public void testCalculPerformanceCommercialNullObjectifCa(){

        //hasMessage permet de vérifier si augmenterSalaire à Trhow que le salaire était null
        Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial("C12345", 1l, null)).hasMessage("L'objectif de chiffre d'affaire ne peut être négatif ou null !");

    }

    @Test
    public void testCalculPerformanceCommercialNegatifObjectifCa(){

        Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial("C12345", 1l, -5l)).hasMessage("L'objectif de chiffre d'affaire ne peut être négatif ou null !");

    }
    //END

    //je donne matricule CCCCCCC

    @Test
    public void testCalculPerformanceCommercialEmployeNull(){
        String matricule = "CCCCCCC";

        Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial(matricule, 1l, 1l)).hasMessage("Le matricule " + matricule + " n'existe pas !");

    }

    @Test
    public void testCalculPerformanceCasUn() throws EmployeException {
        //Employe employe = new Employe("BOB", "Billie", "C00001", LocalDate.now(), 1000d, 1, 1d);
        when(employeRepository.findByMatricule("C00001")).thenReturn(new Employe("BOB", "Billie", "C00001", LocalDate.now(), 1000d, 1, 1d));
        when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(null);

        employeService.calculPerformanceCommercial("C00001", 1l, 2l);
        ArgumentCaptor<Employe> employe = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employe.capture());
        Assertions.assertThat(employe.getValue().getPerformance()).isEqualTo(1);
    }

    @Test
    public void testCalculPerformanceCasDeux() throws EmployeException {

        when(employeRepository.findByMatricule("C00001")).thenReturn(new Employe("BOB", "Billie", "C00001", LocalDate.now(), 1000d, 2, 1d));
        when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(null);

        employeService.calculPerformanceCommercial("C00001", 185l, 200l);
        ArgumentCaptor<Employe> employe = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employe.capture());
        Assertions.assertThat(employe.getValue().getPerformance()).isEqualTo(1);
    }

    @Test
    public void testCalculPerformanceCasTrois() throws EmployeException {
        when(employeRepository.findByMatricule("C00001")).thenReturn(new Employe("BOB", "Billie", "C00001", LocalDate.now(), 1000d, 3, 1d));
        when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(null);

        employeService.calculPerformanceCommercial("C00001", 196l, 200l);
        ArgumentCaptor<Employe> employe = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employe.capture());
        Assertions.assertThat(employe.getValue().getPerformance()).isEqualTo(3);
    }

    @Test
    public void testCalculPerformanceCasQuatre() throws EmployeException {
        when(employeRepository.findByMatricule("C00001")).thenReturn(new Employe("BOB", "Billie", "C00001", LocalDate.now(), 1000d, 4, 1d));
        when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(null);

        employeService.calculPerformanceCommercial("C00001", 215l, 200l);
        ArgumentCaptor<Employe> employe = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employe.capture());
        Assertions.assertThat(employe.getValue().getPerformance()).isEqualTo(5);
    }

    @Test
    public void testCalculPerformanceCasCinq() throws EmployeException {
        when(employeRepository.findByMatricule("C00001")).thenReturn(new Employe("BOB", "Billie", "C00001", LocalDate.now(), 1000d, 5, 1d));
        when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(null);

        employeService.calculPerformanceCommercial("C00001", 260l, 200l);
        ArgumentCaptor<Employe> employe = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employe.capture());
        Assertions.assertThat(employe.getValue().getPerformance()).isEqualTo(9);
    }
}
