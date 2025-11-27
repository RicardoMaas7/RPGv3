package rpg.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import rpg.core.Character;
import rpg.core.GameFacade;
import rpg.inventory.Item;
import rpg.inventory.Equippable;
import rpg.ui.theme.UITheme;
import rpg.ui.components.ModernButton;

/**
 * Panel de inventario mejorado con UI moderna.
 */
public class InventoryPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JTable itemTable;
    private DefaultTableModel tableModel;
    
    public InventoryPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.SECONDARY_DARK);
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        // Título
        JLabel titleLabel = new JLabel("INVENTARIO");
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(UITheme.ACCENT_BLUE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Tabla de items
        String[] columns = {"Nombre", "Descripción", "Tipo"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        itemTable = new JTable(tableModel);
        itemTable.setFont(UITheme.FONT_BODY);
        itemTable.setRowHeight(40);
        itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemTable.setSelectionBackground(UITheme.ACCENT_BLUE);
        itemTable.setSelectionForeground(UITheme.TEXT_PRIMARY);
        itemTable.setBackground(UITheme.PRIMARY_DARK);
        itemTable.setForeground(UITheme.TEXT_PRIMARY);
        itemTable.setGridColor(new Color(60, 63, 65));
        itemTable.setFillsViewportHeight(true);
        itemTable.setOpaque(true);
        
        // Header de la tabla
        itemTable.getTableHeader().setFont(UITheme.FONT_BODY_BOLD);
        itemTable.getTableHeader().setBackground(new Color(52, 73, 94));
        itemTable.getTableHeader().setForeground(UITheme.TEXT_PRIMARY);
        itemTable.getTableHeader().setBorder(BorderFactory.createEmptyBorder());
        
        JScrollPane scrollPane = new JScrollPane(itemTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 63, 65), 1));
        scrollPane.getViewport().setBackground(UITheme.PRIMARY_DARK);
        scrollPane.setBackground(UITheme.PRIMARY_DARK);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        
        ModernButton equipBtn = new ModernButton("Equipar", UITheme.ACCENT_BLUE);
        equipBtn.addActionListener(e -> equipSelectedItem());
        
        ModernButton useBtn = new ModernButton("Usar", UITheme.ACCENT_GREEN);
        useBtn.addActionListener(e -> useSelectedItem());
        
        buttonPanel.add(equipBtn);
        buttonPanel.add(useBtn);
        
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        refresh();
    }
    
    public void refresh() {
        tableModel.setRowCount(0);
        
        Character player = GameFacade.getInstance().getPlayer();
        if (player != null) {
            for (Item item : player.getInventory().getItems()) {
                String type = item instanceof Equippable ? "Equipable" : "Consumible";
                tableModel.addRow(new Object[]{
                    item.getName(),
                    item.getDescription(),
                    type
                });
            }
        }
    }
    
    private void equipSelectedItem() {
        int selectedRow = itemTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un item primero");
            return;
        }
        
        Character player = GameFacade.getInstance().getPlayer();
        Item item = player.getInventory().getItems().get(selectedRow);
        
        if (item instanceof Equippable) {
            player.equip((Equippable) item);
            JOptionPane.showMessageDialog(this, "Item equipado: " + item.getName());
            refresh();
        } else {
            JOptionPane.showMessageDialog(this, "Este item no se puede equipar");
        }
    }
    
    private void useSelectedItem() {
        int selectedRow = itemTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un item primero");
            return;
        }
        
        Character player = GameFacade.getInstance().getPlayer();
        Item item = player.getInventory().getItems().get(selectedRow);
        
        item.use(player);
        JOptionPane.showMessageDialog(this, "Usaste: " + item.getName());
        refresh();
    }
}
