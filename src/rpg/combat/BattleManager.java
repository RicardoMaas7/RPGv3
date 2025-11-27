package rpg.combat;

import rpg.core.Character;
import rpg.core.Enemy;
import rpg.core.EnemyGroup;
import rpg.events.GameEventManager;
import rpg.events.EventType;
import rpg.inventory.Item;
import java.util.List;
import java.util.ArrayList;

/**
 * Gestor del sistema de combate por turnos.
 */
public class BattleManager {

    private Character player;
    private Character enemies; // ¡Esto puede ser UN enemigo o un GRUPO! (Composite)
    private boolean isBattleOver = false;

    // Constructor: le pasamos los combatientes
    public BattleManager(Character player, Character enemies) {
        this.player = player;
        this.enemies = enemies;
    }

    // Método principal para iniciar y correr la batalla
    public void startBattle() {
        logMessage("¡Comienza la batalla!");
        logMessage(player.getName() + " se enfrenta a " + enemies.getName() + "!");

        // Loop de batalla (simplificado)
        // En tu GUI real, esto no sería un 'while',
        // sino que esperarías a que el jugador presione un botón.
        
        // Por ahora, solo simulemos un turno
        playerTurn();
        if (isBattleOver) return;
        
        enemyTurn();
    }

    // Lógica para el turno del jugador
    public void playerTurn() {
        if (!player.isAlive()) {
            logMessage("¡El jugador ha sido derrotado!");
            isBattleOver = true;
            return;
        }

        // En un juego real, aquí esperarías el input de la GUI.
        // Por ahora, solo atacamos.
        logMessage(player.getName() + " ataca!");
        player.attack(enemies); // El jugador ataca al grupo

        checkVictory();
    }

    /**
     * Verifica si el jugador ha ganado la batalla y otorga recompensas.
     * @return true si la batalla terminó con victoria, false en caso contrario
     */
    public boolean checkVictory() {
        if (!enemies.isAlive()) {
            logMessage("¡El jugador ha ganado la batalla!");
            
            // Otorgar XP por victoria
            int totalXp = 0;
            int totalGold = 0;
            List<Item> allLoot = new ArrayList<>();
            
            if (enemies instanceof EnemyGroup) {
                EnemyGroup group = (EnemyGroup) enemies;
                for (Character member : group.getMembers()) {
                    if (member instanceof Enemy) {
                        Enemy enemy = (Enemy) member;
                        totalXp += enemy.getExperienceValue();
                        
                        // Generar loot de cada enemigo
                        totalGold += enemy.generateGold();
                        List<Item> enemyLoot = enemy.generateLoot();
                        allLoot.addAll(enemyLoot);
                    }
                }
            } else if (enemies instanceof Enemy) {
                Enemy enemy = (Enemy) enemies;
                totalXp = enemy.getExperienceValue();
                totalGold = enemy.generateGold();
                allLoot = enemy.generateLoot();
            }
            
            // Otorgar recompensas
            if (totalXp > 0) {
                player.gainExperience(totalXp);
                logMessage("¡Ganaste " + totalXp + " XP!");
            }
            
            if (totalGold > 0) {
                player.addGold(totalGold);
                logMessage("¡Conseguiste " + totalGold + " monedas de oro!");
            }
            
            if (!allLoot.isEmpty()) {
                logMessage("=== ITEMS DROPEADOS ===");
                for (Item item : allLoot) {
                    player.getInventory().add(item);
                    logMessage("+ " + item.getName());
                }
                logMessage("¡Los items se añadieron a tu inventario!");
            }
            
            GameEventManager.getInstance().notify(EventType.ENEMY_DEFEATED, enemies);
            isBattleOver = true;
            return true;
        }
        return false;
    }

    // Lógica para el turno del enemigo
    public void enemyTurn() {
        if (!enemies.isAlive()) {
            isBattleOver = true;
            return;
        }
        
        // Si 'enemies' es un EnemyGroup, su 'attack' no hace nada (PassiveStrategy)
        // Necesitamos atacar CON SUS MIEMBROS
        if (enemies instanceof EnemyGroup) {
            EnemyGroup group = (EnemyGroup) enemies;
            for (Character member : group.getMembers()) {
                if (member.isAlive() && player.isAlive()) {
                    logMessage(member.getName() + " ataca!");
                    member.attack(player);
                }
            }
        } else {
            // Es un solo enemigo
            logMessage(enemies.getName() + " ataca!");
            enemies.attack(player);
        }

        if (!player.isAlive()) {
            logMessage("¡El jugador ha sido derrotado!");
            isBattleOver = true;
        }
    }

    // Método de ayuda para enviar mensajes a la consola/GUI
    private void logMessage(String message) {
        System.out.println("BATTLE: " + message);
        // ¡Patrón Observer! Notificamos a la GUI (Consola de Eventos)
        GameEventManager.getInstance().notify(EventType.NEW_MESSAGE_LOGGED, message);
    }
    
    // Getter para la GUI
    public Enemy getCurrentEnemy() {
        if (enemies instanceof Enemy) {
            return (Enemy) enemies;
        } else if (enemies instanceof EnemyGroup) {
            EnemyGroup group = (EnemyGroup) enemies;
            for (Character member : group.getMembers()) {
                if (member.isAlive() && member instanceof Enemy) {
                    return (Enemy) member;
                }
            }
        }
        return null;
    }
}