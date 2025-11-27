package rpg.ui.theme;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * Centraliza todos los estilos de la interfaz gráfica.
 */
public class UITheme {
    
    // --- Colores Principales ---
    public static final Color PRIMARY_DARK = new Color(33, 37, 41);    // Fondo oscuro principal
    public static final Color SECONDARY_DARK = new Color(44, 48, 52);  // Fondo de paneles
    public static final Color ACCENT_BLUE = new Color(52, 152, 219);   // Azul brillante
    public static final Color ACCENT_GREEN = new Color(46, 204, 113);  // Verde éxito
    public static final Color ACCENT_RED = new Color(231, 76, 60);     // Rojo peligro/acción
    public static final Color ACCENT_GOLD = new Color(241, 196, 15);   // Oro/Advertencia
    
    // --- Texto ---
    public static final Color TEXT_PRIMARY = new Color(236, 240, 241); // Blanco humo
    public static final Color TEXT_SECONDARY = new Color(189, 195, 199); // Gris claro
    
    // --- Fuentes ---
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_MONOSPACE = new Font("Consolas", Font.PLAIN, 13);
    
    // --- Bordes ---
    public static Border createStandardBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 63, 65), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
    }
    
    public static Border createRoundedBorder(Color color, int radius) {
        return BorderFactory.createLineBorder(color, 1); // Simplificado para Swing básico
    }
}
