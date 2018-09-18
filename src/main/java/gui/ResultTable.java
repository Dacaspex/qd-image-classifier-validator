package gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Class to show the results from the network
 */
public class ResultTable extends JFrame {

    /**
     * Column headers
     */
    private String[] headers;
    /**
     * Actual table
     */
    private JTable table;

    public ResultTable() {

        // Create table
        headers = new String[]{"Object", "Value"};
        table = new JTable(new String[][]{}, headers) {
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                return new DefaultTableCellRenderer() {
                    Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);

                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table,
                            Object value,
                            boolean isSelected,
                            boolean hasFocus,
                            int row,
                            int column
                    ) {
                        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        setBorder(BorderFactory.createCompoundBorder(getBorder(), padding));
                        return this;
                    }
                };
            }
        };
        table.setRowHeight(25);
        table.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

        // Create frame
        this.setPreferredSize(new Dimension(500, 900));
        this.add(table);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setVisible(false);
    }

    public void open() {
        this.setVisible(true);
    }

    public void close() {
        this.setVisible(false);
    }

    public void setData(String[][] data) {
        table.setModel(new DefaultTableModel(data, headers));
    }
}
