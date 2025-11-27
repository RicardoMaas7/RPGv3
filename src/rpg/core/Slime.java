package rpg.core;

import rpg.combat.MeleeAttack;
import rpg.ai.AggressiveAI;
import rpg.events.EventType;
import rpg.events.GameEventManager;
import rpg.inventory.*;

/**
 * Slime - Criatura gelatinosa básica.
 * 
 * CARACTERÍSTICAS ESPECIALES:
 * - Puede dividirse (futuro)
 * - Resistente a daño contundente
 * - Ataque ácido
 * 
 * HABILIDAD ESPECIAL: [SALPICADURA ÁCIDA]
 * - Daño: Ataque × 1.3
 * - Daño reducido pero garantizado
 */
public class Slime extends Enemy {
    
    private static final long serialVersionUID = 1L;
    
    public Slime() {
        super("Slime", 1, 3, 0, 20, 
              new MeleeAttack(), new AggressiveAI(), 15);
        
        // Bonus de defensa por cuerpo gelatinoso
        this.defenseBonus = 2;
        
        // Configurar loot
        configureLoot();
    }
    
    private void configureLoot() {
        LootTable loot = new LootTable();
        
        // Oro
        loot.setGoldReward(5, 10); // 5-15 oro
        
        // Items (baja probabilidad)
        loot.addDrop(new HealthPotion(), 0.50); // 50% de chance (AUMENTADO)
        
        this.setLootTable(loot);
    }
    
    @Override
    public void specialAbility(Character target) {
        String bounceMessage = "[REBOTE] " + this.name + " rebota amenazadoramente...";
        System.out.println(bounceMessage);
        GameEventManager.getInstance().notify(EventType.NEW_MESSAGE_LOGGED, bounceMessage);
        
        int damage = (int) (this.baseAttack * 1.3);
        
        String acidMessage = ">>> ¡SALPICADURA ACIDA! <<<";
        System.out.println(acidMessage);
        GameEventManager.getInstance().notify(EventType.NEW_MESSAGE_LOGGED, acidMessage);
        
        String damageMessage = target.receiveDamage(damage);
        if (!damageMessage.isEmpty()) {
            System.out.println(damageMessage);
            GameEventManager.getInstance().notify(EventType.NEW_MESSAGE_LOGGED, damageMessage);
        }
        
        String sizzleMsg = "[ACIDO] ¡El acido quema!";
        System.out.println(sizzleMsg);
        GameEventManager.getInstance().notify(EventType.NEW_MESSAGE_LOGGED, sizzleMsg);
    }
    
    @Override
    public String receiveDamage(int amount) {
        // Cuerpo gelatinoso: mensaje especial
        String result = super.receiveDamage(amount);
        
        if (this.isAlive()) {
            String wobbleMsg = "[GELATINA] El slime tiembla pero se mantiene cohesionado...";
            System.out.println(wobbleMsg);
        }
        
        return result;
    }
}
