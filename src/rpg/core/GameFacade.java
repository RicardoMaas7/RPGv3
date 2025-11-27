package rpg.core;

// Importa todos tus subsistemas
import rpg.factory.*;
import rpg.combat.*;
import rpg.events.*;
import rpg.quest.Quest;
import rpg.quest.QuestManager;
import rpg.persistence.SaveManager;
import java.util.List;

/**
 * Fachada principal del juego - Patrones Facade + Singleton (GoF).
 */
public class GameFacade {

    // --- Singleton ---
    private static GameFacade instance;
    public static GameFacade getInstance() {
        if (instance == null) {
            instance = new GameFacade();
        }
        return instance;
    }
    // --- Fin Singleton ---

    // --- Subsistemas ---
    private CharacterFactory charFactory;
    private EnemyFactory enemyFactory;
    private QuestManager questManager;
    private SaveManager saveManager;
    
    // --- Estado del Juego ---
    private Character player;
    private CharacterType currentCharacterType; // Tipo de clase actual
    
    // El constructor privado inicializa todos los subsistemas
    private GameFacade() {
        this.charFactory = new CharacterFactory();
        this.enemyFactory = new EnemyFactory();
        this.questManager = new QuestManager();
        this.saveManager = new SaveManager();
    }

    // --- API Pública (Métodos que la GUI llamará) ---

    /**
     * Inicia un juego nuevo, creando al jugador.
     */
    public void startNewGame(CharacterType type, String name) {
        this.player = this.charFactory.createCharacter(type, name);
        this.currentCharacterType = type;
        log("Un nuevo héroe, " + player.getName() + ", ha aparecido!");
    }
    
    // --- API DE MISIONES ---

    public void startQuest(String questId) {
        questManager.startQuest(questId);
    }
    
    public void completeQuest(String questId) {
        questManager.completeQuest(questId);
    }
    
    public List<Quest> getActiveQuests() {
        return questManager.getActiveQuests();
    }

    /**
     * Inicia una batalla contra un encuentro aleatorio.
     */
    public void startBattle() {
        if (player == null || !player.isAlive()) {
            log("No se puede iniciar una batalla sin un jugador vivo.");
            return;
        }
        
        // 1. Crea el encuentro (¡usando el Composite!)
        Character encounter = this.enemyFactory.createEncounter();
        
        // 2. Crea el BattleManager y guárdalo
        currentBattle = new BattleManager(player, encounter);
        
        // Notifica a la GUI que la batalla comenzó
        GameEventManager.getInstance().notify(EventType.NEW_MESSAGE_LOGGED, "¡Batalla iniciada!");
    }
    
    /**
     * Devuelve el objeto del jugador para que la GUI muestre sus stats.
     */
    public Character getPlayer() {
        return this.player;
    }

    // --- Método de Ayuda para Log ---
    
    /**
     * Envía un mensaje tanto a la consola como al sistema de eventos (Observer).
     */
    private void log(String message) {
        System.out.println("FACADE: " + message);
        GameEventManager.getInstance().notify(EventType.NEW_MESSAGE_LOGGED, message);
    }
    
    /**
     * Guarda el estado actual del jugador en un archivo.
     */
    public boolean saveGame(String filename) {
        if (player != null && currentCharacterType != null) {
            return saveManager.saveGame(player, currentCharacterType, filename);
        } else {
            log("No hay jugador que guardar.");
            return false;
        }
    }
    
    /**
     * Guarda en un slot específico (1-5).
     */
    public boolean saveGameToSlot(int slotNumber, String saveName) {
        if (player != null && currentCharacterType != null) {
            return saveManager.saveGame(player, currentCharacterType, slotNumber, saveName);
        } else {
            log("No hay jugador que guardar.");
            return false;
        }
    }
    
    /**
     * Carga el estado del jugador desde un archivo.
     */
    public boolean loadGame(String filename) {
        rpg.persistence.GameState gameState = saveManager.loadGame(filename);
        if (gameState != null) {
            this.player = gameState.getPlayer();
            this.currentCharacterType = gameState.getCharacterType();
            log("¡Partida cargada! Bienvenido de nuevo, " + this.player.getName());
            // ¡Notifica a la GUI que todo cambió!
            GameEventManager.getInstance().notify(EventType.PLAYER_HP_CHANGED, player);
            GameEventManager.getInstance().notify(EventType.PLAYER_MANA_CHANGED, player);
            GameEventManager.getInstance().notify(EventType.PLAYER_LEVELED_UP, player);
            return true;
        } else {
            log("Falló la carga del archivo: " + filename);
            return false;
        }
    }
    
    /**
     * Carga desde un slot específico.
     */
    public boolean loadGameFromSlot(int slotNumber) {
        rpg.persistence.GameState gameState = saveManager.loadGameFromSlot(slotNumber);
        if (gameState != null) {
            this.player = gameState.getPlayer();
            this.currentCharacterType = gameState.getCharacterType();
            log("¡Partida cargada! Bienvenido de nuevo, " + this.player.getName());
            GameEventManager.getInstance().notify(EventType.PLAYER_HP_CHANGED, player);
            GameEventManager.getInstance().notify(EventType.PLAYER_MANA_CHANGED, player);
            GameEventManager.getInstance().notify(EventType.PLAYER_LEVELED_UP, player);
            return true;
        } else {
            log("No hay partida guardada en el slot " + slotNumber);
            return false;
        }
    }
    
    /**
     * Obtiene el SaveManager para acceso directo a funciones avanzadas.
     */
    public SaveManager getSaveManager() {
        return saveManager;
    }
    
    // --- API para Combate (GUI) ---
    
    private BattleManager currentBattle;
    
    public BattleManager getBattleManager() {
        return currentBattle;
    }
    
    public String playerAttack() {
        if (currentBattle != null && player != null) {
            Enemy enemy = currentBattle.getCurrentEnemy();
            if (enemy != null) {
                int initialHp = enemy.getCurrentHp();
                player.attack(enemy);
                int damage = initialHp - enemy.getCurrentHp();
                
                String message = player.getName() + " ataca a " + enemy.getName() + " por " + damage + " de daño!";
                GameEventManager.getInstance().notify(EventType.NEW_MESSAGE_LOGGED, message);
                
                // Verificar victoria inmediatamente
                currentBattle.checkVictory();
                
                return message;
            }
        }
        return "No hay batalla activa";
    }
    
    public String playerSpecialAbility() {
        if (currentBattle != null && player != null) {
            Enemy enemy = currentBattle.getCurrentEnemy();
            if (enemy != null) {
                int initialHp = enemy.getCurrentHp();
                player.specialAbility(enemy);
                int damage = initialHp - enemy.getCurrentHp();
                
                String message = player.getName() + " usa [Habilidad Especial] contra " + enemy.getName() + " por " + damage + " de daño!";
                GameEventManager.getInstance().notify(EventType.NEW_MESSAGE_LOGGED, message);
                
                // Verificar victoria inmediatamente
                currentBattle.checkVictory();
                
                return message;
            }
        }
        return "No se puede usar habilidad";
    }
    
    public String enemyTurn() {
        if (currentBattle != null) {
            Enemy enemy = currentBattle.getCurrentEnemy();
            if (enemy != null && enemy.isAlive()) {
                // Usa la IA del enemigo para decidir la acción
                enemy.performAIAction(player);
                return "Turno del enemigo completado";
            }
        }
        return "";
    }
    
    public QuestManager getQuestManager() {
        return questManager;
    }

}