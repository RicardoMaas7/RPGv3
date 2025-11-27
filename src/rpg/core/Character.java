package rpg.core;

import rpg.combat.AttackStrategy;
import rpg.inventory.Equipment;
import rpg.inventory.Equippable;
import rpg.inventory.Inventory;
import rpg.inventory.Item;
import rpg.inventory.Slot;
import rpg.events.GameEventManager;
import rpg.events.EventType;
import java.io.Serializable;

/**
 * Clase base abstracta para todos los personajes del juego.
 * 
 * ARQUITECTURA:
 * - Herencia: Warrior, Mage, Archer, Priest, Enemy heredan de Character
 * - Composición: Contiene Inventory y Equipment (no pueden existir independientemente)
 * - Strategy: Usa AttackStrategy para comportamiento de ataque intercambiable
 * - Observer: Notifica cambios de estado a través de GameEventManager
 * - Serializable: Permite guardar/cargar el estado completo del personaje
 * 
 * RESPONSABILIDADES:
 * - Gestión de stats (HP, maná, ataque, defensa)
 * - Sistema de experiencia y niveles
 * - Combate (atacar, defenderse, recibir daño)
 * - Inventario y equipamiento
 * - Curación (implementa Healable)
 * 
 * MÉTODOS ABSTRACTOS (deben implementar las subclases):
 * - specialAbility(): Habilidad única de cada clase
 * - levelUp(): Cómo aumentan los stats al subir de nivel
 */
public abstract class Character implements Healable, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // --- Atributos (Variables) ---
    protected String name;
    protected int level; // ¡Requisito 2!
    protected int baseAttack; // Para daño físico
    protected int baseMagic;  // Para daño mágico
    
    protected int maxHp;
    protected int currentHp;
    protected int maxMana;
    protected int currentMana;
    
    // Economía
    protected int gold;
    
    // Sistema de experiencia
    protected int currentExp = 0;
    protected int expToNextLevel = 100; // XP necesaria para nivel 2
    
    protected boolean isDefending = false; // Estado para el método defend()
    
    // --- Composición (Requisito 1) ---
    protected Inventory inventory;
    protected Equipment equipment;
    
    // --- Strategy (Requisito 3) ---
    protected AttackStrategy attackStrategy;
    
    protected int attackBonus = 0;
    protected int magicBonus = 0;
    protected int defenseBonus = 0;

    // --- Constructor ---
    public Character(String name, int level, int baseAttack, int baseMagic, int maxHp, int maxMana) {
        this.name = name;
        this.level = level;
        this.baseAttack = baseAttack; // Daño físico base
        this.baseMagic = baseMagic;   // Daño mágico base
        this.maxHp = maxHp;
        this.currentHp = maxHp; // Empieza con vida llena
        this.maxMana = maxMana;
        this.currentMana = maxMana; // Empieza con maná lleno
        
        // ¡Importante! Inicializamos los objetos de composición
        this.inventory = new Inventory();
        this.equipment = new Equipment();
        
        this.gold = GameConstants.STARTING_GOLD;
    }

    // --- Métodos Abstractos (Para los hijos) ---
    
    /**
     * Usa la habilidad única de esta clase (ej. Bola de Fuego, Carga).
     */
    public abstract void specialAbility(Character target);
    
    /**
     * Lógica de cómo esta clase sube sus stats al subir de nivel.
     */
    public abstract void levelUp(); // Sin parámetro, se aplica a 'this'

    // --- Métodos Concretos (Heredados por todos) ---
    
    // Combate
    public void setAttackStrategy(AttackStrategy strategy) {
        this.attackStrategy = strategy;
    }

    /**
     * Ejecuta la AttackStrategy actual contra un objetivo.
     */
    public void attack(Character target) {
        this.isDefending = false; // Atacar rompe la defensa
        // Llama al método execute() DE LA ESTRATEGIA QUE TENGO GUARDADA
        this.attackStrategy.execute(this, target); 
    }

    /**
     * Se prepara para recibir menos daño en el siguiente turno.
     */
    public void defend() {
        this.isDefending = true;
    }

    /**
     * Recibe una cantidad de daño, la reduce si se está defendiendo.
     * @return Un mensaje describiendo el daño recibido
     */
    @Override
    public String receiveDamage(int amount) {
        int originalDamage = amount;
        
        // Restamos la defensa
        int finalDamage = amount - this.defenseBonus;
        if (finalDamage < 1) finalDamage = 1; // Siempre al menos 1 de daño

        String message = "";
        if (this.isDefending) {
            int beforeDefend = finalDamage;
            finalDamage = (int)(finalDamage * 0.5);
            message = this.name + " se defiende y reduce el daño de " + beforeDefend + " a " + finalDamage + "!";
            this.isDefending = false;
        } else {
            message = this.name + " recibe " + finalDamage + " de daño";
            if (this.defenseBonus > 0) {
                message += " (bloqueó " + (originalDamage - amount) + " con armadura)";
            }
            message += "!";
        }
        
        this.currentHp -= finalDamage;
        if (this.currentHp < 0) this.currentHp = 0;
        
        GameEventManager.getInstance().notify(EventType.PLAYER_HP_CHANGED, this);
        
        // Notificar si el jugador ha muerto
        if (this.currentHp == 0 && this == GameFacade.getInstance().getPlayer()) {
            GameEventManager.getInstance().notify(EventType.PLAYER_DIED, this);
        }
        
        return message;
    }
  

    // Gestión (Requisito 2)
    public void useItem(Item item) {
        inventory.use(item, this); 
    }

    public void equip(Equippable item) {
        // Llama a equip en Equipment, pasándole el personaje
        equipment.equip(this, item);
    }
    
    public void unequip(Slot slot) {
        // Llama a unequip en Equipment, pasándole el personaje
        equipment.unequip(this, slot);
    }
    
    /**
     * Intenta consumir una cantidad de maná.
     * @return true si se pudo consumir, false si no había suficiente.
     */
    public boolean consumeMana(int amount) {
        if (this.currentMana >= amount) {
        	
            this.currentMana -= amount;
            GameEventManager.getInstance().notify(EventType.PLAYER_MANA_CHANGED, this);
            return true; // Se pudo consumir
        }
        return false; // No hay suficiente maná
    }

    // Interfaz Healable (Requisito 2)
    @Override
    public void heal(int amount) {
        this.currentHp += amount;
        if (this.currentHp > this.maxHp) {
            this.currentHp = this.maxHp; 
        }
        GameEventManager.getInstance().notify(EventType.PLAYER_HP_CHANGED, this);
    }
    
    // --- Getters (Requisito 4 - Para la GUI y otros sistemas) ---
    
    public Inventory getInventory() {
        return this.inventory;
    }
    
    public String getName() { return this.name; }
    public int getLevel() { return this.level; }
    @Override
    public int getCurrentHp() { return this.currentHp; }
    @Override
    public int getMaxHp() { return this.maxHp; }
    public int getCurrentMana() { return this.currentMana; }
    public int getMaxMana() { return this.maxMana; }
    
    public void setCurrentMana(int mana) {
        this.currentMana = Math.max(0, Math.min(mana, this.maxMana));
        GameEventManager.getInstance().notify(EventType.PLAYER_MANA_CHANGED, this);
    }
    
    public int getBaseAttack() {
        return this.baseAttack + this.attackBonus; 
    }
    
    public int getBaseMagic() {
        return this.baseMagic + this.magicBonus; 
    }
    @Override
    public boolean isAlive() { return this.currentHp > 0; }
    
    public void addAttackBonus(int amount) { this.attackBonus += amount; }
    public void addMagicBonus(int amount) { this.magicBonus += amount; }
    public void addDefenseBonus(int amount) { this.defenseBonus += amount; }
    public void addMaxHp(int amount) { 
        this.maxHp += amount; 
        // Ajusta currentHp si es necesario
        if (this.currentHp > this.maxHp) {
            this.currentHp = this.maxHp;
        }
    }
    
    // --- Getters para GUI ---
    public int getAttack() { return this.baseAttack + this.attackBonus; }
    public int getDefense() { return this.defenseBonus; }
    public int getMagic() { return this.baseMagic + this.magicBonus; }
    public int getGold() { return this.gold; }
    
    public void addGold(int amount) {
        this.gold += amount;
        GameEventManager.getInstance().notify(EventType.NEW_MESSAGE_LOGGED, "Has obtenido " + amount + " de oro.");
    }
    
    public boolean removeGold(int amount) {
        if (this.gold >= amount) {
            this.gold -= amount;
            return true;
        }
        return false;
    }
    
    // --- Sistema de Experiencia ---
    
    public int getCurrentExp() { return this.currentExp; }
    public int getExpToNextLevel() { return this.expToNextLevel; }
    
    /**
     * Añade experiencia al personaje y sube de nivel automáticamente si es necesario.
     * @param amount Cantidad de XP a añadir
     */
    public void gainExperience(int amount) {
        this.currentExp += amount;
        System.out.println(this.name + " gana " + amount + " XP!");
        GameEventManager.getInstance().notify(EventType.NEW_MESSAGE_LOGGED, 
            this.name + " gana " + amount + " XP!");
        
        // Revisa si sube de nivel
        while (this.currentExp >= this.expToNextLevel) {
            this.currentExp -= this.expToNextLevel;
            this.levelUp();
            // La XP necesaria aumenta cada nivel (fórmula: nivel * 100)
            this.expToNextLevel = this.level * 100;
        }
    }
}

