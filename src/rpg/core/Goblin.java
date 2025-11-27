package rpg.core;

import rpg.combat.MeleeAttack;
import rpg.ai.EvasiveAI;
import rpg.events.EventType;
import rpg.events.GameEventManager;
import rpg.inventory.*;

/**
 * Goblin - Criatura pequeña pero astuta.
 * 
 * CARACTERÍSTICAS ESPECIALES:
 * - Muy ágil y difícil de golpear
 * - Puede llamar refuerzos (futuro)
 * - Ataques sucios y tramposos
 * 
 * HABILIDAD ESPECIAL: [ATAQUE SUCIO]
 * - Daño: Ataque × 1.5
 * - 40% probabilidad de reducir defensa enemiga 1 turno
 */
public class Goblin extends Enemy {
    
    private static final long serialVersionUID = 1L;
    private static final double DIRTY_TRICK_CHANCE = 0.40;
    
    public Goblin() {
        super("Goblin", 3, 5, 0, 35, 
              new MeleeAttack(), new EvasiveAI(), 30);
        
        configureLoot();
    }
    
    private void configureLoot() {
        LootTable loot = new LootTable();
        
        // Oro (los goblins aman el oro)
        loot.setGoldReward(20, 30); // 20-50 oro
        
        // Items
        loot.addDrop(new HealthPotion(), 0.60); // 60% chance (AUMENTADO)
        loot.addDrop(new Sword("Daga Oxidada", "Arma goblin oxidada pero afilada", 6), 0.30); // 30% chance (AUMENTADO)
        
        this.setLootTable(loot);
    }
    
    @Override
    public void specialAbility(Character target) {
        String sneakMessage = "[TRAMPA] " + this.name + " se prepara para hacer trampa...";
        System.out.println(sneakMessage);
        GameEventManager.getInstance().notify(EventType.NEW_MESSAGE_LOGGED, sneakMessage);
        
        int damage = (int) (this.baseAttack * 1.5);
        
        String dirtyMessage = ">>> ¡ATAQUE SUCIO! <<<";
        System.out.println(dirtyMessage);
        GameEventManager.getInstance().notify(EventType.NEW_MESSAGE_LOGGED, dirtyMessage);
        
        String damageMessage = target.receiveDamage(damage);
        if (!damageMessage.isEmpty()) {
            System.out.println(damageMessage);
            GameEventManager.getInstance().notify(EventType.NEW_MESSAGE_LOGGED, damageMessage);
        }
        
        // Truco sucio: arena en los ojos
        if (Math.random() < DIRTY_TRICK_CHANCE) {
            String trickMsg = "[CIEGO] " + this.name + " lanza arena a los ojos!\n" +
                            "¡Tu vision esta borrosa!";
            System.out.println(trickMsg);
            GameEventManager.getInstance().notify(EventType.NEW_MESSAGE_LOGGED, trickMsg);
        }
    }
}
