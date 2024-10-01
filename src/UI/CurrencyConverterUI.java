package UI;

import Controllers.tipocambioController;
import Models.RMoneda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CurrencyConverterUI extends JFrame {
    private JComboBox<String> fromCurrency;
    private JComboBox<String> toCurrency;
    private JTextField amountField;
    private JLabel resultLabel;
    private tipocambioController controller;

    public CurrencyConverterUI() {
        controller = new tipocambioController();

        setTitle("Currency Converter");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        try {
            List<RMoneda> rates = controller.obtenerTasasDeCambio("USD");
            List<String> currencies = new ArrayList<>();
            rates.forEach(rate -> currencies.add(rate.tipo()));
            fromCurrency = new JComboBox<>(currencies.toArray(new String[0]));
            toCurrency = new JComboBox<>(currencies.toArray(new String[0]));
        } catch (IOException e) {
            e.printStackTrace();
            fromCurrency = new JComboBox<>(new String[]{"USD", "EUR", "PEN"});
            toCurrency = new JComboBox<>(new String[]{"USD", "EUR", "PEN"});
        }

        amountField = new JTextField();
        resultLabel = new JLabel("Resultado: ");

        JButton convertButton = new JButton("Convertir");
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    convertCurrency();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        add(new JLabel("Moneda elegida:"));
        add(fromCurrency);
        add(new JLabel("Cantidad:"));
        add(amountField);
        add(new JLabel("Moneda a cambiar:"));
        add(toCurrency);
        add(convertButton);
        add(resultLabel);
    }

    private void convertCurrency() throws IOException {
        String from = (String) fromCurrency.getSelectedItem();
        String to = (String) toCurrency.getSelectedItem();
        double amount = Double.parseDouble(amountField.getText());

        List<RMoneda> rates = controller.obtenerTasasDeCambio(from);
        double rate = rates.stream()
                .filter(r -> r.tipo().equals(to))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Currency not found"))
                .cambio();
        double result = amount * rate;

        resultLabel.setText("Result: " + result);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CurrencyConverterUI().setVisible(true);
            }
        });
    }
}