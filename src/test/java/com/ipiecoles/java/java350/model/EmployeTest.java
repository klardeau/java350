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
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;

import static org.mockito.Mockito.when;


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


    
    //EVAL---------------------------------------------------
//----------------------------------------------------- augmenterSalaire--------------------------------------------------------------------//
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
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1300d);

    }

    //Test si le pourcentage est négatif
    @Test
    public void testAugmenterSalaireNegatif(){
        //Given
        Double pourc = -10d;
        Employe employe = new Employe();
        employe.setSalaire(1000d);

        //When
        //Then
        Assertions.assertThatThrownBy(() -> employe.augmenterSalaire(pourc)).hasMessage("Erreur le salaire ne peut etre negatif");
    }

    //Test salaire si pourcentage infèrieur à 1 mais pas negatif
    @Test
    public void testAugmenterSalaireInfUn(){
        //Given
        Double pourc = 0.3d;
        Employe employe = new Employe();
        employe.setSalaire(1000d);

        //When
        //Then
        Assertions.assertThatThrownBy(() -> employe.augmenterSalaire(pourc)).hasMessage("Erreur le salaire ne peut diminuer");

    }


//si l'employé n'a pas encore de salaire inscrit.
    @Test
    public void testAugmenterSalairePasSalaire(){
        //Given
        Double pourc = 1.3d;
        Employe employe = new Employe();
        employe.setSalaire(null);


        //hasMessage permet de vérifier si augmenterSalaire à Trhow que le salaire était null
        Assertions.assertThatThrownBy(() -> employe.augmenterSalaire(pourc)).hasMessage("une erreur est survenue le salaire est null ou égale à zéro");

    }

    @Test
    public void testAugmenterSalaireSalAZero(){
        //Given
        Double pourc = 1.3d;
        Employe employe = new Employe();
        employe.setSalaire(0d);


        //hasMessage permet de vérifier si augmenterSalaire à Trhow que le salaire était null
        Assertions.assertThatThrownBy(() -> employe.augmenterSalaire(pourc)).hasMessage("une erreur est survenue le salaire est null ou égale à zéro");

    }

    //Test paramétré pour la NbRTT
    @ParameterizedTest
    @CsvSource({
            "'2016-03-19', 9",//pour vendredi validé
            "'2019-01-01', 8",//(365 - 218 - 104 - 10 - 25 = 8)*1 car travail 100 pourcent du temps
            "'2021-01-01', 10",
            "'2022-01-01', 10",//(365 - 218 - 105 - 7 - 25 = 10)*1     105 samedi et dimanche
            "'2032-01-01', 11" })
    public void testNbRtt(LocalDate date, Integer nbRtt) {
        Employe employe = new Employe("Saint", "Exupery", "M00123", LocalDate.now(), 1600.0, 1, 1.0);

        Integer nb = employe.getNbRtt(date);
        Assertions.assertThat(nb).isEqualTo(nbRtt);
    }



}
