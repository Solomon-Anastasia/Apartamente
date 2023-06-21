package org.example.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Agent {
    private int codAgent;
    private String nume;
    private String prenume;
    private int virsta;
    private String telefon;

    public Agent(String nume, String prenume, int virsta, String telefon) {
        this.nume = nume;
        this.prenume = prenume;
        this.virsta = virsta;
        this.telefon = telefon;
    }
}