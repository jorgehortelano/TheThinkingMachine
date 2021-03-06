package com.softwaremagico.tm.character.races;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 Softwaremagico
 * %%
 * This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero
 * <softwaremagico@gmail.com> Valencia (Spain).
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.planets.Planet;
import com.softwaremagico.tm.log.MachineLog;

import java.lang.reflect.Field;
import java.util.Set;

@SuppressWarnings("unused")
public class Race extends Element<Race> {
    private final RaceCharacteristic strength = new RaceCharacteristic(CharacteristicName.STRENGTH);
    private final RaceCharacteristic dexterity = new RaceCharacteristic(CharacteristicName.DEXTERITY);
    private final RaceCharacteristic endurance = new RaceCharacteristic(CharacteristicName.ENDURANCE);
    private final RaceCharacteristic wits = new RaceCharacteristic(CharacteristicName.WITS);
    private final RaceCharacteristic perception = new RaceCharacteristic(CharacteristicName.PERCEPTION);
    private final RaceCharacteristic tech = new RaceCharacteristic(CharacteristicName.TECH);
    private final RaceCharacteristic presence = new RaceCharacteristic(CharacteristicName.PRESENCE);
    private final RaceCharacteristic will = new RaceCharacteristic(CharacteristicName.WILL);
    private final RaceCharacteristic faith = new RaceCharacteristic(CharacteristicName.FAITH);

    private final RaceCharacteristic movement = new RaceCharacteristic(CharacteristicName.MOVEMENT);
    private final RaceCharacteristic initiative = new RaceCharacteristic(CharacteristicName.INITIATIVE);
    private final RaceCharacteristic defense = new RaceCharacteristic(CharacteristicName.DEFENSE);

    private Set<Blessing> blessings = null;
    private Set<AvailableBenefice> benefices = null;
    private Set<Planet> planets = null;

    private int psi;
    private int theurgy;
    private int urge;
    private int hubris;

    private int cost;

    public Race(String id, String name, String description, String language, String moduleName) {
        super(id, name, description, language, moduleName);
    }

    public Race(String name, String description, String language, String moduleName, int strength, int dexterity, int endurance, int wits,
                int perception, int tech, int presence, int will, int faith, int movement, int psi, int teurgy, int urge,
                int hubris, int cost) {
        this(null, name, description, language, moduleName);
        setValue(CharacteristicName.STRENGTH, strength);
        setValue(CharacteristicName.DEXTERITY, dexterity);
        setValue(CharacteristicName.ENDURANCE, endurance);
        setValue(CharacteristicName.WITS, wits);
        setValue(CharacteristicName.PERCEPTION, perception);
        setValue(CharacteristicName.TECH, tech);
        setValue(CharacteristicName.PRESENCE, presence);
        setValue(CharacteristicName.WILL, will);
        setValue(CharacteristicName.FAITH, faith);
        setValue(CharacteristicName.MOVEMENT, movement);
        this.psi = psi;
        this.theurgy = teurgy;
        this.urge = urge;
        this.hubris = hubris;
        this.cost = cost;
    }

    public RaceCharacteristic getParameter(CharacteristicName characteristicName) {
        for (final Field field : this.getClass().getDeclaredFields()) {
            if (field.getType().isAssignableFrom(RaceCharacteristic.class)) {
                final RaceCharacteristic parameter;
                try {
                    parameter = (RaceCharacteristic) field.get(this);
                    if (parameter != null) {
                        if (parameter.getCharacteristic().equals(characteristicName)) {
                            return parameter;
                        }
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // Not valid field.
                }
            }
        }
        return null;
    }

    public void setMaximumValue(CharacteristicName characteristicName, int maxValue) {
        try {
            getParameter(characteristicName).setMaximumValue(maxValue);
        } catch (NullPointerException npe) {
            MachineLog.severe(this.getClass().getName(), "Invalid maximum parameter '{}'.", characteristicName);
        }
    }

    public void setMaximumInitialValue(CharacteristicName characteristicName, int maxValue) {
        try {
            getParameter(characteristicName).setMaximumInitialValue(maxValue);
        } catch (NullPointerException npe) {
            MachineLog.severe(this.getClass().getName(), "Invalid maximum initial parameter '{}'.", characteristicName);
        }
    }

    public void setValue(CharacteristicName characteristicName, int value) {
        try {
            getParameter(characteristicName).setInitialValue(value);
        } catch (NullPointerException npe) {
            MachineLog.severe(this.getClass().getName(), "Invalid value parameter '{}'.", characteristicName);
        }
    }

    public RaceCharacteristic get(CharacteristicName characteristicName) {
        return getParameter(characteristicName);
    }

    public int getPsi() {
        return psi;
    }

    public int getTheurgy() {
        return theurgy;
    }

    public int getUrge() {
        return urge;
    }

    public int getHubris() {
        return hubris;
    }

    public int getCost() {
        return cost;
    }

    public void setPsi(int psi) {
        this.psi = psi;
    }

    public void setTheurgy(int teurgy) {
        this.theurgy = teurgy;
    }

    public void setUrge(int urge) {
        this.urge = urge;
    }

    public void setHubris(int hubris) {
        this.hubris = hubris;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public boolean isXeno() {
        return !getId().equals("human");
    }

    public Set<Blessing> getBlessings() {
        if (blessings == null) {
            // Blessings are not read with factions due to a loop
            // factions->blessings->skills->factions
            try {
                RaceFactory.getInstance().setBlessings(this);
            } catch (InvalidRaceException e) {
                MachineLog.errorMessage(this.getClass().getName(), e);
            }
        }
        return blessings;
    }

    public Set<AvailableBenefice> getBenefices() {
        if (benefices == null) {
            // Benefices are not read with factions due to a loop
            // factions->benefices->skills->factions
            try {
                RaceFactory.getInstance().setBenefices(this);
            } catch (InvalidRaceException e) {
                MachineLog.errorMessage(this.getClass().getName(), e);
            }
        }
        return benefices;
    }

    public void setBlessings(Set<Blessing> blessings) {
        this.blessings = blessings;
    }

    public void setBenefices(Set<AvailableBenefice> benefices) {
        this.benefices = benefices;
    }

    public Set<Planet> getPlanets() {
        if (planets == null) {
            // Blessings are not read with factions due to a loop
            // factions->blessings->skills->factions
            try {
                RaceFactory.getInstance().setPlanets(this);
            } catch (InvalidRaceException e) {
                MachineLog.errorMessage(this.getClass().getName(), e);
            }
        }
        return planets;
    }

    public void setPlanets(Set<Planet> planets) {
        this.planets = planets;
    }
}
