package rpg.core;

/**
 * Clase de constantes globales del juego.
 * Centraliza valores numéricos y configuraciones para facilitar el balanceo.
 * 
 * PROPÓSITO:
 * - Eliminar "magic numbers" del código
 * - Facilitar ajustes de balanceo
 * - Mejorar legibilidad del código
 * 
 * @author RPGv3 Team
 * @version 1.0
 */
public final class GameConstants {
    
    // Constructor privado para prevenir instanciación
    private GameConstants() {
        throw new AssertionError("Esta clase no debe ser instanciada");
    }
    
    // ==================== COMBATE ====================
    
    /** Multiplicador de daño cuando se defiende (0.5 = 50% reducción) */
    public static final double DEFENSE_DAMAGE_MULTIPLIER = 0.5;
    
    /** Probabilidad de huir exitosamente de una batalla (0.5 = 50%) */
    public static final double FLEE_SUCCESS_RATE = 0.5;
    
    /** Daño base mínimo garantizado en ataques */
    public static final int MINIMUM_DAMAGE = 1;
    
    // ==================== EXPERIENCIA ====================
    
    /** XP base requerida para nivel 2 */
    public static final int BASE_EXP_TO_LEVEL = 100;
    
    /** Multiplicador de XP por nivel (1.5 = 150% incremento por nivel) */
    public static final double EXP_GROWTH_MULTIPLIER = 1.5;
    
    /** XP otorgada por Slime */
    public static final int SLIME_XP_REWARD = 15;
    
    /** XP otorgada por Goblin */
    public static final int GOBLIN_XP_REWARD = 30;
    
    /** XP otorgada por Skeleton */
    public static final int SKELETON_XP_REWARD = 40;
    
    /** XP otorgada por Orc */
    public static final int ORC_XP_REWARD = 60;
    
    /** XP otorgada por Dark Mage */
    public static final int DARK_MAGE_XP_REWARD = 80;
    
    /** XP otorgada por Wolf */
    public static final int WOLF_XP_REWARD = 35;
    
    /** XP otorgada por Dragon Whelp */
    public static final int DRAGON_WHELP_XP_REWARD = 150;
    
    /** XP otorgada por Bandit */
    public static final int BANDIT_XP_REWARD = 45;
    
    // ==================== STATS DE PERSONAJES ====================
    
    /** HP base del Warrior */
    public static final int WARRIOR_BASE_HP = 120;
    
    /** HP base del Mage */
    public static final int MAGE_BASE_HP = 80;
    
    /** HP base del Archer */
    public static final int ARCHER_BASE_HP = 100;
    
    /** HP base del Priest */
    public static final int PRIEST_BASE_HP = 90;
    
    /** Incremento de HP por nivel */
    public static final int HP_PER_LEVEL = 10;
    
    /** Incremento de ataque por nivel */
    public static final int ATTACK_PER_LEVEL = 2;
    
    /** Incremento de defensa por nivel */
    public static final int DEFENSE_PER_LEVEL = 1;
    
    // ==================== CURACION ====================
    
    /** Cantidad de HP recuperada por descanso */
    public static final int REST_HP_RESTORE = 30;
    
    /** Cantidad de HP recuperada por poción de salud */
    public static final int HEALTH_POTION_RESTORE = 50;
    
    /** Cantidad de MP recuperada por poción de maná */
    public static final int MANA_POTION_RESTORE = 30;
    
    /** Costo de maná para habilidades especiales */
    public static final int SPECIAL_ABILITY_MANA_COST = 20;
    
    // ==================== ENCUENTROS ====================
    
    /** Probabilidad de encuentro individual (0.4 = 40%) */
    public static final double SINGLE_ENEMY_PROBABILITY = 0.4;
    
    /** Probabilidad de encuentro pequeño (0.6 = 60% hasta este punto) */
    public static final double SMALL_GROUP_PROBABILITY = 0.6;
    
    /** Probabilidad de encuentro mediano (0.85 = 85% hasta este punto) */
    public static final double MEDIUM_GROUP_PROBABILITY = 0.85;
    
    // ==================== INTERFAZ ====================
    
    /** Ancho de ventana predeterminado */
    public static final int WINDOW_WIDTH = 1200;
    
    /** Alto de ventana predeterminado */
    public static final int WINDOW_HEIGHT = 800;
    
    /** Altura del panel superior */
    public static final int TOP_PANEL_HEIGHT = 80;
    
    /** Ancho del panel izquierdo (menú) */
    public static final int LEFT_PANEL_WIDTH = 200;
    
    /** Ancho del panel derecho (stats) */
    public static final int RIGHT_PANEL_WIDTH = 250;
    
    /** Altura del panel inferior (log) */
    public static final int BOTTOM_PANEL_HEIGHT = 150;
    
    // ==================== COLORES (RGB) ====================
    
    /** Color primario oscuro */
    public static final int[] COLOR_DARK_PRIMARY = {44, 62, 80};
    
    /** Color secundario oscuro */
    public static final int[] COLOR_DARK_SECONDARY = {52, 73, 94};
    
    /** Color de acento azul */
    public static final int[] COLOR_ACCENT_BLUE = {52, 152, 219};
    
    /** Color de HP (rojo) */
    public static final int[] COLOR_HP = {231, 76, 60};
    
    /** Color de MP (azul) */
    public static final int[] COLOR_MP = {52, 152, 219};
    
    /** Color de éxito (verde) */
    public static final int[] COLOR_SUCCESS = {39, 174, 96};
    
    /** Color de peligro (rojo) */
    public static final int[] COLOR_DANGER = {192, 57, 43};
    
    /** Color de oro */
    public static final int[] COLOR_GOLD = {255, 215, 0};
    
    // ==================== ECONOMÍA ====================
    
    /** Oro inicial del jugador */
    public static final int STARTING_GOLD = 0;
}
