package org.example.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Apartament {
    private int codApartament;
    private int etaj;
    private int nrCamere;
    private double pret;
    private double metriPatrati;
    private int codAgent;

    public Apartament(int etaj, int nrCamere, double pret, double metriPatrati, int codAgent) {
        this.etaj = etaj;
        this.nrCamere = nrCamere;
        this.pret = pret;
        this.metriPatrati = metriPatrati;
        this.codAgent = codAgent;
    }

    public Apartament(int etaj, int nrCamere, double pret, double metriPatrati) {
        this.etaj = etaj;
        this.nrCamere = nrCamere;
        this.pret = pret;
        this.metriPatrati = metriPatrati;
    }
}