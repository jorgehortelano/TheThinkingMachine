package com.softwaremagico.tm.character.skills;

import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.values.IValue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

public class SkillDefinition extends Skill<SkillDefinition> implements ISkillRandomDefintions, IValue {
    public static final int MAX_RANKS_TO_SKILLS_FACTION_LIMITED = 5;
    public static final String FACTION_LORE_ID = "factionLore";
    public static final String PLANETARY_LORE_ID = "planetaryLore";

    private final Set<Faction> factions = new HashSet<>();
    private boolean natural = false;
    private Set<Specialization> specializations = new HashSet<>();
    private final Set<String> requiredSkills = new HashSet<>();
    private SkillGroup skillGroup;
    // Number of times that a skill (generalizable) is shown in the PDF.
    private int numberToShow = 1;

    public SkillDefinition(String id, String name, String description, String language, String moduleName) {
        super(id, name, description, language, moduleName);
    }

    public boolean isNatural() {
        return natural;
    }

    public boolean isSpecializable() {
        return !specializations.isEmpty();
    }

    public SkillGroup getSkillGroup() {
        return skillGroup;
    }

    public void setSkillGroup(SkillGroup skillGroup) {
        if (skillGroup == null) {
            throw new RuntimeException("Skill group cannot be null in skill '" + this + "'");
        }
        this.skillGroup = skillGroup;
    }

    public void setNatural(boolean natural) {
        this.natural = natural;
    }

    public Set<Specialization> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(Set<Specialization> specializations) {
        this.specializations = specializations;
    }

    public int getNumberToShow() {
        return numberToShow;
    }

    public void setNumberToShow(int numberToShow) {
        this.numberToShow = numberToShow;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + skillGroup + ") " + getSpecializations();
    }

    public Set<Faction> getFactions() {
        return factions;
    }

    public void addFaction(Faction faction) {
        factions.add(faction);
    }

    public void addFactions(Collection<Faction> factions) {
        this.factions.addAll(factions);
    }

    public void addRequiredSkill(String requiredSkillId) {
        this.requiredSkills.add(requiredSkillId);
    }

    public void addRequiredSkills(Collection<String> requiredSkills) {
        this.requiredSkills.addAll(requiredSkills);
    }

    public Set<String> getRequiredSkills() {
        return requiredSkills;
    }

    public boolean isLimitedToFaction() {
        return !factions.isEmpty();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
