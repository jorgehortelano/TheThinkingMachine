package com.softwaremagico.tm.character.creation;

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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.cybernetics.ICyberneticDevice;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.log.CostCalculatorLog;

import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

public class CostCalculator {
    public static final int CHARACTERISTIC_EXTRA_POINTS_COST = 3;
    public static final int SKILL_EXTRA_POINTS_COST = 1;

    public static final int PSIQUE_LEVEL_COST = 3;
    public static final int PATH_LEVEL_COST = 1;
    public static final int EXTRA_WYRD_COST = 2;
    public static final int OCCULTISM_POWER_LEVEL_COST = 1;

    public static final int CYBERNETIC_DEVICE_COST = 1;

    private CostCalculatorModificationHandler costCalculatorModificationHandler;

    private AtomicInteger currentCharacteristicPoints;
    private AtomicInteger currentCharacteristicExtraPoints;
    private AtomicInteger currentSkillsPoints;
    private AtomicInteger currentSkillsExtraPoints;
    private AtomicInteger currentTraitsPoints;
    private AtomicInteger currentTraitsExtraPoints;
    private AtomicInteger currentOccultismLevelExtraPoints;
    private AtomicInteger currentOccultismPowersExtraPoints;
    private AtomicInteger currentWyrdExtraPoints;
    private AtomicInteger currentCyberneticsExtraPoints;
    private float fireBirdsExpend;

    public CostCalculator(CharacterPlayer characterPlayer) {
        setCostListeners(characterPlayer);
    }

    public interface ICurrentPointsChanged {
        void updated(int value);
    }

    public interface ICurrentExtraPointsChanged {
        void updated(int value);
    }


    private void setCostListeners(CharacterPlayer characterPlayer) {
        characterPlayer.getCharacterModificationHandler().addCharacteristicUpdatedListener((characteristic, rankModifications) -> {
            updateCost(currentCharacteristicPoints, FreeStyleCharacterCreation.getCharacteristicsPoints(characterPlayer.getInfo().getAge()),
                    currentCharacteristicExtraPoints, rankModifications,
                    value -> getCostCharacterModificationHandler().launchCharacteristicPointsUpdatedListeners(value),
                    value -> getCostCharacterModificationHandler().launchCharacteristicExtraPointsUpdatedListeners(value));
        });
        characterPlayer.getCharacterModificationHandler().addSkillUpdateListener((skill, rankModifications) -> {
            updateCost(currentSkillsPoints, FreeStyleCharacterCreation.getSkillsPoints(characterPlayer.getInfo().getAge()),
                    currentSkillsExtraPoints, rankModifications,
                    value -> getCostCharacterModificationHandler().launchSkillsPointsUpdatedListeners(value),
                    value -> getCostCharacterModificationHandler().launchSkillsExtraPointsUpdatedListeners(value));
        });
        characterPlayer.getCharacterModificationHandler().addBeneficesUpdatedListener((benefice, removed) -> {
            updateCost(currentTraitsPoints, FreeStyleCharacterCreation.getSkillsPoints(characterPlayer.getInfo().getAge()),
                    currentTraitsExtraPoints, removed ? -benefice.getCost() : benefice.getCost(),
                    value -> getCostCharacterModificationHandler().launchTraitsPointsUpdatedListeners(value),
                    value -> getCostCharacterModificationHandler().launchTraitsPointsUpdatedListeners(value));
        });
        characterPlayer.getCharacterModificationHandler().addBlessingUpdatedListener((blessing, removed) -> {
            updateCost(currentTraitsPoints, FreeStyleCharacterCreation.getSkillsPoints(characterPlayer.getInfo().getAge()),
                    currentTraitsExtraPoints, removed ? -blessing.getCost() : blessing.getCost(),
                    value -> getCostCharacterModificationHandler().launchTraitsPointsUpdatedListeners(value),
                    value -> getCostCharacterModificationHandler().launchTraitsPointsUpdatedListeners(value));
        });
        characterPlayer.getCharacterModificationHandler().addOccultismLevelUpdatedListener((occultismType, psyValue) -> {
            updateCost(new AtomicInteger(0), 0,
                    currentOccultismLevelExtraPoints, psyValue,
                    null,
                    value -> getCostCharacterModificationHandler().launchOccultismLevelExtraPointUpdatedListeners(value));
        });
        characterPlayer.getCharacterModificationHandler().addOccultismPowerUpdatedListener((power, removed) -> {
            updateCost(new AtomicInteger(0), 0,
                    currentOccultismPowersExtraPoints, removed ? -power.getCost() : power.getCost(),
                    null,
                    value -> getCostCharacterModificationHandler().launchOccultismPowerExtraPointUpdatedListeners(value));
        });
        characterPlayer.getCharacterModificationHandler().addWyrdUpdatedListener(wyrdValue -> {
            if (currentWyrdExtraPoints.get() != wyrdValue) {
                currentWyrdExtraPoints.set(wyrdValue);
                getCostCharacterModificationHandler().launchWyrdExtraPointUpdatedListeners(wyrdValue);
            }
        });
        characterPlayer.getCharacterModificationHandler().addCyberneticDeviceUpdatedListener((device, removed) -> {
            updateCost(new AtomicInteger(0), 0,
                    currentCyberneticsExtraPoints, removed ? -device.getCost() : device.getCost(),
                    null,
                    value -> getCostCharacterModificationHandler().launchCyberneticExtraPointsListeners(value));
        });
        characterPlayer.getCharacterModificationHandler().addEquipmentUpdatedListener((equipment, removed) -> {
            fireBirdsExpend += (removed ? -equipment.getCost() : equipment.getCost());
            getCostCharacterModificationHandler().launchFirebirdSpendListeners((removed ? -equipment.getCost() : equipment.getCost()));
        });
    }

    /**
     * Calculates the cost variation for a points category (skill, characteristics, traits).
     *
     * @param mainPoints        points by default assigned to a category (skill, characteristics, traits)
     * @param maximumMainPoints total maximum points available to the category
     * @param extraPoints       extra points assigned after all main points are consumed.
     * @param increment         current change on the value.
     */
    private void updateCost(AtomicInteger mainPoints, int maximumMainPoints, AtomicInteger extraPoints, int increment,
                            ICurrentPointsChanged currentPointsChanged, ICurrentExtraPointsChanged currentExtraPointsChanged) {
        if (mainPoints.get() + increment <= maximumMainPoints) {
            if (extraPoints.get() > 0) {
                //increment must be negative.
                mainPoints.addAndGet(extraPoints.get() - increment);
                currentPointsChanged.updated(extraPoints.get() - increment);
                currentExtraPointsChanged.updated(0 - extraPoints.get());
                extraPoints.set(0);
            } else {
                mainPoints.addAndGet(increment);
                currentPointsChanged.updated(increment);
            }
        } else {
            if (extraPoints.get() > 0) {
                extraPoints.addAndGet(increment);
                currentExtraPointsChanged.updated(increment);
            } else {
                // Not extraPoints spent yet.
                extraPoints.addAndGet(increment - (maximumMainPoints - mainPoints.get()));
                currentExtraPointsChanged.updated(increment - (maximumMainPoints - mainPoints.get()));
                if (mainPoints.get() != maximumMainPoints) {
                    mainPoints.set(maximumMainPoints);
                    currentPointsChanged.updated(maximumMainPoints);
                }
            }
        }
    }

    public int getIncrementalCost() {
        return currentCharacteristicExtraPoints.get() * CHARACTERISTIC_EXTRA_POINTS_COST +
                currentSkillsPoints.get() * SKILL_EXTRA_POINTS_COST +
                currentOccultismLevelExtraPoints.get() * PSIQUE_LEVEL_COST +
                currentOccultismPowersExtraPoints.get() * PATH_LEVEL_COST +
                currentCyberneticsExtraPoints.get() * CYBERNETIC_DEVICE_COST +
                currentWyrdExtraPoints.get() * EXTRA_WYRD_COST;
    }

    public static int getCost(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
        return getCost(characterPlayer, 0, 0);
    }

    public static int getCost(CharacterPlayer characterPlayer, int extraSkillPoints, int extraCharacteristicsPoints)
            throws InvalidXmlElementException {
        int cost = 0;
        if (characterPlayer.getRace() != null) {
            cost += characterPlayer.getRace().getCost();
        }
        cost += getCharacteristicsCost(characterPlayer, extraCharacteristicsPoints);
        cost += getSkillCosts(characterPlayer, extraSkillPoints);
        cost += getTraitsCosts(characterPlayer);
        cost += getPsiPowersCosts(characterPlayer);
        cost += getCyberneticsCost(characterPlayer);
        CostCalculatorLog.debug(CostCalculator.class.getName(),
                "Character '{}' total cost '{}'.\n", characterPlayer.getCompleteNameRepresentation(), cost);
        return cost;
    }

    public static int logCost(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
        return logCost(characterPlayer, 0, 0);
    }

    public static int logCost(CharacterPlayer characterPlayer, int extraSkillPoints, int extraCharacteristicsPoints)
            throws InvalidXmlElementException {
        int cost = 0;
        CostCalculatorLog.info(CostCalculator.class.getName(), "####################### ");
        CostCalculatorLog.info(CostCalculator.class.getName(), "\t{}", characterPlayer.getCompleteNameRepresentation());
        CostCalculatorLog.info(CostCalculator.class.getName(), "####################### ");
        if (characterPlayer.getRace() != null) {
            cost += characterPlayer.getRace().getCost();
            CostCalculatorLog.info(CostCalculator.class.getName(), "Race cost '{}'.", characterPlayer.getRace().getCost());
        }
        cost += getCharacteristicsCost(characterPlayer, extraCharacteristicsPoints);
        CostCalculatorLog.info(CostCalculator.class.getName(),
                "Characteristics cost: " + getCharacteristicsCost(characterPlayer, extraCharacteristicsPoints));
        cost += getSkillCosts(characterPlayer, extraSkillPoints);
        CostCalculatorLog.info(CostCalculator.class.getName(),
                "Skills cost: " + getSkillCosts(characterPlayer, extraSkillPoints));
        cost += getTraitsCosts(characterPlayer);
        CostCalculatorLog.info(CostCalculator.class.getName(), "Traits cost '{}'.", getTraitsCosts(characterPlayer));
        cost += getPsiPowersCosts(characterPlayer);
        CostCalculatorLog.info(CostCalculator.class.getName(),
                "Psi powers cost: " + getPsiPowersCosts(characterPlayer));
        cost += getCyberneticsCost(characterPlayer);
        CostCalculatorLog.info(CostCalculator.class.getName(),
                "Cybernetics cost: " + getCyberneticsCost(characterPlayer));
        CostCalculatorLog.info(CostCalculator.class.getName(), "Total cost '{}'.\n", cost);
        return cost;
    }

    private static int getCharacteristicsCost(CharacterPlayer characterPlayer, int extraCharacteristicsPoints) {
        return (characterPlayer.getCharacteristicsTotalPoints() - Math.max(CharacteristicName.values().length,
                (FreeStyleCharacterCreation.getCharacteristicsPoints(characterPlayer.getInfo().getAge()))
                        + extraCharacteristicsPoints))
                * CHARACTERISTIC_EXTRA_POINTS_COST;
    }

    private static int getSkillCosts(CharacterPlayer characterPlayer, int extraSkillPoints)
            throws InvalidXmlElementException {
        return (characterPlayer.getSkillsTotalPoints() - Math.max(0,
                (FreeStyleCharacterCreation.getSkillsPoints(characterPlayer.getInfo().getAge())) + extraSkillPoints))
                * SKILL_EXTRA_POINTS_COST;
    }

    private static int getTraitsCosts(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
        int cost = 0;
        cost += getBlessingCosts(characterPlayer);
        cost += getBeneficesCosts(characterPlayer);
        return cost - FreeStyleCharacterCreation.getTraitsPoints(characterPlayer.getInfo().getAge());
    }

    private static int getBlessingCosts(CharacterPlayer characterPlayer) {
        int cost = 0;
        for (final Blessing blessing : characterPlayer.getAllBlessings()) {
            cost += blessing.getCost();
        }
        return cost;
    }

    public static int getBlessingCosts(List<Blessing> blessings) {
        int cost = 0;
        for (final Blessing blessing : blessings) {
            cost += blessing.getCost();
        }
        return cost;
    }

    public static int getBeneficesCosts(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
        int cost = 0;
        for (final AvailableBenefice benefit : characterPlayer.getAllBenefices()) {
            cost += benefit.getCost();
        }
        for (final AvailableBenefice affliction : characterPlayer.getAfflictions()) {
            cost += affliction.getCost();
        }
        return cost;
    }

    private static int getPsiPowersCosts(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
        int cost = 0;
        for (final Entry<String, List<OccultismPower>> occulstismPathEntry : characterPlayer.getSelectedPowers()
                .entrySet()) {
            for (final OccultismPower occultismPower : occulstismPathEntry.getValue()) {
                cost += occultismPower.getLevel() * OCCULTISM_POWER_LEVEL_COST;
            }
        }
        cost += characterPlayer.getExtraWyrd() * EXTRA_WYRD_COST;
        cost += Math.max(0,
                (characterPlayer.getBasicPsiqueLevel(
                        OccultismTypeFactory.getPsi(characterPlayer.getLanguage(), characterPlayer.getModuleName()))
                        - (characterPlayer.getRace() != null ? characterPlayer.getRace().getPsi() : 0))
                        * PSIQUE_LEVEL_COST);
        cost += Math.max(0,
                (characterPlayer.getBasicPsiqueLevel(
                        OccultismTypeFactory.getTheurgy(characterPlayer.getLanguage(), characterPlayer.getModuleName()))
                        - (characterPlayer.getRace() != null ? characterPlayer.getRace().getTheurgy() : 0))
                        * PSIQUE_LEVEL_COST);
        return cost;
    }

    private static int getCyberneticsCost(CharacterPlayer characterPlayer) {
        int cost = 0;
        for (final ICyberneticDevice device : characterPlayer.getCybernetics()) {
            cost += device.getPoints() * CYBERNETIC_DEVICE_COST;
        }
        return cost;
    }

    public CostCalculatorModificationHandler getCostCharacterModificationHandler() {
        if (costCalculatorModificationHandler == null) {
            costCalculatorModificationHandler = new CostCalculatorModificationHandler();
        }
        return costCalculatorModificationHandler;
    }

}
