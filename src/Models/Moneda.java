package Models;

import Interfaces.IClaves;

public class Moneda implements IClaves {
    private String tipo;
    private double cambio;
    public Moneda(String tipo,double cambio){
        this.tipo=tipo;
        this.cambio=cambio;
    }
    public String obtenerClave(String tipo){
        String apiKey = "ab10dd5c36d49cecff7d10df"; // Reemplaza con tu API Key
        String urlString = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/"+tipo;
        return urlString;
    }
}
