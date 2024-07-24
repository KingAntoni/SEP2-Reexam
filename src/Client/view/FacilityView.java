package Client.view;

import Client.Networking.Client;
import Client.Networking.RMIClient;
import Client.viewModel.FacilityViewModel;
import Client.viewModel.FacilityViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class FacilityView {
    private FacilityViewModel viewModel;
    private JFrame frame;
    private JPanel panel;
    private JTextField nameField;
    private JTextField descriptionField;
    private JButton addButton;

    public FacilityView(FacilityViewModel viewModel) {
        this.viewModel = viewModel;
        createView();
    }

    private void createView() {
        frame = new JFrame("Sports Facilities Booking System");
        panel = new JPanel();
        nameField = new JTextField(20);
        descriptionField = new JTextField(20);
        addButton = new JButton("Add Facility");

        panel.setLayout(new GridLayout(3, 2));
        panel.add(new JLabel("Facility Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(addButton);

        frame.add(panel);
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        addListeners();
    }

    private void addListeners() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String description = descriptionField.getText();
                try {
                    viewModel.addFacility(name, description);
                    JOptionPane.showMessageDialog(frame, "Facility added.");
                } catch (IOException | SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Failed to add facility.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        try {
            Client client = new RMIClient();
            FacilityViewModel viewModel = new FacilityViewModel(client);
            new FacilityView(viewModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}