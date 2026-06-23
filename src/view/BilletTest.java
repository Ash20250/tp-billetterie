package view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class BilletTest {

    @Test
    public void testCalculerTotal() {
        // 1. Arrange (Préparation)
        // On instancie le contrôleur pour tester sa logique
        AchatBilletViewController controller = new AchatBilletViewController();
        int nombrePlaces = 3;
        double prixAttendu = 135.00; // 3 * 45.00

        // 2. Act (Action)
        // On appelle la méthode de calcul
        double resultat = controller.calculerTotal(nombrePlaces);

        // 3. Assert (Vérification)
        // On vérifie que le résultat correspond à ce qui est attendu
        assertEquals(prixAttendu, resultat, 0.001, "Le calcul du prix total est incorrect");
    }

    @Test
    public void testCalculerTotalZeroPlace() {
        AchatBilletViewController controller = new AchatBilletViewController();
        assertEquals(0.0, controller.calculerTotal(0), "Le prix pour 0 place doit être 0");
    }
}