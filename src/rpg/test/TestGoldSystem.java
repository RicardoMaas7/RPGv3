package rpg.test;

import rpg.core.Character;
import rpg.factory.CharacterFactory;
import rpg.factory.CharacterType;
import rpg.core.GameConstants;

public class TestGoldSystem {
    public static void main(String[] args) {
        System.out.println("=== VERIFICACIÓN DEL SISTEMA DE ORO ===");
        
        // 1. Verificar Oro Inicial
        System.out.println("\n1. Verificando Oro Inicial...");
        CharacterFactory factory = new CharacterFactory();
        Character hero = factory.createCharacter(CharacterType.WARRIOR, "TestHero");
        
        if (hero.getGold() == GameConstants.STARTING_GOLD) {
            System.out.println("[OK] El oro inicial es correcto: " + hero.getGold());
        } else {
            System.err.println("[ERROR] El oro inicial es incorrecto. Esperado: " + GameConstants.STARTING_GOLD + ", Actual: " + hero.getGold());
        }
        
        // 2. Verificar Añadir Oro
        System.out.println("\n2. Verificando Añadir Oro...");
        int amountToAdd = 100;
        hero.addGold(amountToAdd);
        
        if (hero.getGold() == GameConstants.STARTING_GOLD + amountToAdd) {
            System.out.println("[OK] Se añadió " + amountToAdd + " de oro correctamente. Total: " + hero.getGold());
        } else {
            System.err.println("[ERROR] Fallo al añadir oro. Total: " + hero.getGold());
        }
        
        // 3. Verificar Remover Oro (Éxito)
        System.out.println("\n3. Verificando Gastar Oro (Suficiente)...");
        int amountToRemove = 50;
        boolean success = hero.removeGold(amountToRemove);
        
        if (success && hero.getGold() == (GameConstants.STARTING_GOLD + amountToAdd - amountToRemove)) {
            System.out.println("[OK] Se gastó " + amountToRemove + " de oro correctamente. Total: " + hero.getGold());
        } else {
            System.err.println("[ERROR] Fallo al gastar oro. Success: " + success + ", Total: " + hero.getGold());
        }
        
        // 4. Verificar Remover Oro (Fallo)
        System.out.println("\n4. Verificando Gastar Oro (Insuficiente)...");
        int amountTooMuch = 1000;
        boolean failure = hero.removeGold(amountTooMuch);
        
        if (!failure && hero.getGold() == (GameConstants.STARTING_GOLD + amountToAdd - amountToRemove)) {
            System.out.println("[OK] El sistema impidió gastar más oro del disponible.");
        } else {
            System.err.println("[ERROR] Se permitió gastar oro insuficiente o el saldo cambió incorrectamente.");
        }
        
        System.out.println("\n=== VERIFICACIÓN COMPLETADA ===");
    }
}
