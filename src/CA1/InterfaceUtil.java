/*
 * Name : LIN HTET & NAY HTET AUNG
 * ADMIN NO : p2340304 & p2340391
 * CLASS : DIT/FT/2A/03
 * */

package CA1;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
// Utility class for building tabulated table.
public class InterfaceUtil {

    public InterfaceUtil(DefaultTableModel model,String title){
        getjScrollPane(model,title);
    }

    private static void getjScrollPane(DefaultTableModel model,String title) {
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        // Setting Row Colour
        table.setDefaultRenderer(Object.class, new TableInterfaceUtil());

        // Setting Heading colour
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new HeaderInterfaceUtil());
        JOptionPane.showMessageDialog(null, scrollPane , title, JOptionPane.PLAIN_MESSAGE);
    }
}
class TableInterfaceUtil extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (column == 0) {
            component.setBackground(Color.LIGHT_GRAY);
        } else {
            component.setBackground(Color.WHITE);
        }
        return component;
    }
}

class HeaderInterfaceUtil extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        c.setBackground(Color.BLACK);
        c.setForeground(Color.WHITE);
        c.setFont(new Font("Arial", Font.BOLD, 15));
        return c;
    }
}
