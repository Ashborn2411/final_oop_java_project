package Gui;

import Entity.Publisher;
import Manager.LibraryManager;
import util.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PublisherPanel extends JPanel {
    private JTable table;
    private JPanel formPanel;
    private JTextField nameField, locationField, contactField, registrationDateField, searchField;
    private JButton addButton;
    private LibraryManager manager;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;


    public PublisherPanel(LibraryManager manager) {
        this.manager = manager;
        this.setLayout(new BorderLayout());
        initFormPanel();
        initTable();
        initBottomPanel();
    }

    private void initFormPanel() {
        formPanel = new ImagePanel(ImagePathUtil.bookbackgorund, new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        nameField = TextFieldUtil.styledField();
        locationField = TextFieldUtil.styledField();
        contactField = TextFieldUtil.styledField();
        registrationDateField = TextFieldUtil.styledField();
       JTextField[] fields = {
                nameField,
                locationField,
                contactField,
                registrationDateField
        };
        for (int i = 0; i < fields.length; i++) {
            addLabeledField(Utilstring.labels[i], fields[i]);
        }


        this.add(formPanel, BorderLayout.NORTH);
    }

    private void initTable() {

        tableModel = new DefaultTableModel(Utilstring.publishColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        styleTable();
        updateTableData();

        // Double-click to select ID and copy to clipboard
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        int modelRow = table.convertRowIndexToModel(selectedRow);
                        Publisher publisher = manager.getPublishers().get(modelRow);

                        // Copy Publisher ID to clipboard
                        String publisherId = publisher.getPublisherId();
                        StringSelection stringSelection = new StringSelection(publisherId);
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(stringSelection, null);

                        // Show confirmation dialog
                        JOptionPane.showMessageDialog(
                                PublisherPanel.this,
                                "Publisher ID '" + publisherId + "' copied to clipboard!",
                                Utilstring.copied,
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        // Populate form fields
                        nameField.setText(publisher.getName());
                        locationField.setText(publisher.getAddress());
                        contactField.setText(publisher.getContact());
                        registrationDateField.setText(publisher.getRegistrationDate());
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);
        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);
    }
    private void initBottomPanel() {
        addButton = createAddButton(Utilstring.addpublisher, ImagePathUtil.plusIcon);
        addButton.addActionListener(e -> addPublisher());
        searchField = createSearchField(Utilstring.searchbyNameorLocation);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.add(addButton);
        bottomPanel.add(new JLabel(Utilstring.search));
        bottomPanel.add(searchField);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addLabeledField(String labelText, Component field) {
        JLabel label = new JLabel(labelText);
        label.setFont(FontUtil.POPPINS_SEMIBOLD_16);
        label.setForeground(Color.GRAY);
        formPanel.add(label);
        formPanel.add(field);
    }

    private void styleTable() {
        table.setFont(FontUtil.POPPINS_REGULAR_14);
        table.setRowHeight(28);
        JTableHeader header = table.getTableHeader();
        header.setFont(FontUtil.POPPINS_SEMIBOLD_16);
        header.setBackground(new Color(220, 220, 220));
        header.setForeground(new Color(40, 40, 40));
    }

    private JButton createAddButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(FontUtil.COURIER_BOLD_15);
        button.setBackground(new Color(60, 120, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(15));
        button.setBorder(BorderFactory.createCompoundBorder(
                button.getBorder(),
                BorderFactory.createEmptyBorder(8, 16, 8, 16) // Padding
        ));
        ImageIcon icon = new ImageIcon(iconPath);
        button.setIcon(new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        return button;
    }

    private JTextField createSearchField(String tooltip) {
        JTextField field = TextFieldUtil.styledField();
        field.setPreferredSize(new Dimension(200, 30));
        field.setToolTipText(tooltip);
        return field;
    }

    private void addPublisher() {
        String name = nameField.getText().trim();
        String location = locationField.getText().trim();
        String contact = contactField.getText().trim();
        String registrationDate = registrationDateField.getText().trim();

        if (name.isEmpty() || location.isEmpty() || contact.isEmpty() || registrationDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, Utilstring.allfiedlcopied, Utilstring.error, JOptionPane.ERROR_MESSAGE);
            return;
        }

        manager.addPublisher(new Publisher(name, location, contact, registrationDate));
        updateTableData();
        clearFormFields();
        JOptionPane.showMessageDialog(this, Utilstring.publisherAddedSuccess, Utilstring.successTitle, JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateTableData() {
        tableModel.setRowCount(0);
        manager.getPublishers().forEach(publisher -> {
            tableModel.addRow(new Object[]{
                    publisher.getPublisherId(),
                    publisher.getName(),
                    publisher.getAddress(),
                    publisher.getContact(),
                    publisher.getRegistrationDate()
            });
        });
    }

    private void clearFormFields() {
        nameField.setText("");
        locationField.setText("");
        contactField.setText("");
        registrationDateField.setText("");
    }

    private void filterTable() {
        String text = searchField.getText().trim();
        if (text.isEmpty()) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    static class RoundedBorder implements javax.swing.border.Border {
        private final int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(0, 0, 0, 0);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }
}
