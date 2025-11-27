package rpg.inventory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rpg.core.Character;

public class Inventory implements Serializable {
    
    private static final long serialVersionUID = 1L;

    // ¡Necesita una lista para guardar los items!
    private List<Item> items;
    private Equipment equipment;

    public Inventory() {
        this.items = new ArrayList<>();
        this.equipment = new Equipment();
    }

    // Método para añadir un item (cuando lo recoges o desequipas)
    public void add(Item item) {
        this.items.add(item);
        System.out.println(item.getName() + " añadido al inventario.");
    }

    // Método para quitar un item (cuando lo equipas)
    public void remove(Equippable item) {
        // Revisa si el item equipable es un 'Item' (Poción, Espada)
        if (item instanceof Item) {
            this.items.remove((Item) item);
            System.out.println(((Item) item).getName() + " quitado del inventario.");
        }
    }

    public void use(Item item, Character character) {
        // Lógica para usar un item (ej. poción)
        // Puedes llamar al método del item
        item.use(character);
        
        // Si es un item consumible (como una poción), quítalo después de usarlo
        if (item instanceof HealthPotion) { // (Necesitarás crear esta clase)
             this.items.remove(item);
             System.out.println(item.getName() + " ha sido consumido.");
        }
    }
    
    // (Añade un getItems() para que la GUI pueda mostrar el inventario)
    public List<Item> getItems() {
        return this.items;
    }
    
    // Getter para Equipment
    public Equipment getEquipment() {
        return this.equipment;
    }
}