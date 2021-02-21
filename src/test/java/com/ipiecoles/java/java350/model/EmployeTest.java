package com.ipiecoles.java.java350.model;

import com.ipiecoles.java.java350.service.EmployeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;

public class EmployeTest {

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
        Double pourc = 1.3;
        Employe employe = new Employe();
        employe.setSalaire(1000d);

        //When
        employe.augmenterSalaire(pourc);

        //Then
        Assertions.assertThat(employe.getSalaire()).isGreaterThan(1000d);

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

//si l'employé n'a pas encore de salaire inscrit
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
            "'2021-01-01', 10",//devrait etre 11
            "'2022-01-01', 10",//(365 - 218 - 105 - 7 - 25 = 10)*1     105 samedi et dimanche
            "'2032-01-01', 11" })//12...
    void testNbRTT(LocalDate date, Integer nbRTT) {
        Employe employe = new Employe("Saint", "Exupery", "M00123", LocalDate.now(), 1600.0, 1, 1.0);

        Integer nbRtt = employe.getNbRtt(date);
        Assertions.assertThat(nbRtt).isEqualTo(nbRTT);
    }

//---------TEST calculPerformanceCommercial----------//

    //caTraite null et negatif (same)
    //BEGIN
    @Test
    public void testCalculPerformanceCommercialNullCaTraite(){
        //Given
        EmployeService employeService = new EmployeService();

        //hasMessage permet de vérifier si augmenterSalaire à Trhow que le salaire était null
        Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial("C12345", null, 2l)).hasMessage("Le chiffre d'affaire traité ne peut être négatif ou null !");

    }

    @Test
    public void testCalculPerformanceCommercialNegaCaTraite(){
        //Given
        EmployeService employeService = new EmployeService();

        Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial("C12345", -4l, 2l)).hasMessage("Le chiffre d'affaire traité ne peut être négatif ou null !");

    }
    //END

    //matricule null et pas commencer par C
    //BEGIN
    @Test
    public void testCalculPerformanceCommercialNullMatricule(){
        //Given
        EmployeService employeService = new EmployeService();

        //hasMessage permet de vérifier si augmenterSalaire à Trhow que le salaire était null
        Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial(null, 2l, 2l)).hasMessage("Le matricule ne peut être null et doit commencer par un C !");

    }

    @Test
    public void testCalculPerformanceCommercialMatriculePasCommenceParC(){
        //Given
        EmployeService employeService = new EmployeService();

        Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial("MATRICULE", 2l, 2l)).hasMessage("Le matricule ne peut être null et doit commencer par un C !");

    }
    //END

    //caTraite null et negatif (same)
    //BEGIN
    @Test
    public void testCalculPerformanceCommercialNullObjectifCa(){
        //Given
        EmployeService employeService = new EmployeService();

        //hasMessage permet de vérifier si augmenterSalaire à Trhow que le salaire était null
        Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial("C12345", 1l, null)).hasMessage("L'objectif de chiffre d'affaire ne peut être négatif ou null !");

    }

    @Test
    public void testCalculPerformanceCommercialNegatifObjectifCa(){
        //Given
        EmployeService employeService = new EmployeService();

        Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial("C12345", 1l, -5l)).hasMessage("L'objectif de chiffre d'affaire ne peut être négatif ou null !");

    }
    //END

    //je donne matricule CCCCCCC
    /*
    @Test
    public void testCalculPerformanceCommercialEmployeNull(){
        //Given
        EmployeService employeService = new EmployeService();
        String matricule = "CCCCCCC";

        Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial(matricule, 1l, 1l)).hasMessage("Le matricule " + matricule + " n'existe pas !");

    }*/
    //END

}
