package com.softwaremagico.tm.random;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 - 2018 Softwaremagico
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.benefices.BeneficeClassification;
import com.softwaremagico.tm.character.benefices.BeneficeDefinition;
import com.softwaremagico.tm.character.benefices.BeneficeDefinitionFactory;
import com.softwaremagico.tm.character.benefices.BeneficeGroup;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IGaussianDistribution;
import com.softwaremagico.tm.random.selectors.IRandomPreferences;
import com.softwaremagico.tm.random.selectors.StatusPreferences;
import com.softwaremagico.tm.random.selectors.TraitCostPreferences;

public class RandomBeneficeDefinition extends RandomSelector<BeneficeDefinition> {
	private static final int MAX_AFFLICTIONS = 2;

	protected RandomBeneficeDefinition(CharacterPlayer characterPlayer, Set<IRandomPreferences> preferences) throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	public void assignAvailableBenefices() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
		// First, try status.
		for (BeneficeDefinition benefice : BeneficeDefinitionFactory.getInstance().getBenefices(BeneficeGroup.STATUS, getCharacterPlayer().getLanguage())) {
			if (getWeight(benefice) > 0 && Objects.equals(benefice.getRestricted(), getCharacterPlayer().getFaction().getFactionGroup())) {
				IGaussianDistribution selectedStatus = StatusPreferences.getSelected(getPreferences());
				if (selectedStatus != null) {
					assignBenefice(benefice, selectedStatus.maximum());
				}
			}
		}

		// Later, the others.
		while (CostCalculator.getBeneficesCosts(getCharacterPlayer()) < FreeStyleCharacterCreation.TRAITS_POINTS && !getWeightedElements().isEmpty()) {
			// Select a benefice
			BeneficeDefinition selectedBenefice = selectElementByWeight();
			assignBenefice(selectedBenefice, FreeStyleCharacterCreation.TRAITS_POINTS - CostCalculator.getBeneficesCosts(getCharacterPlayer()));
		}
	}

	private void assignBenefice(BeneficeDefinition selectedBenefice, int maxPoints) throws InvalidXmlElementException {
		// Select the range of the benefice.
		AvailableBenefice selectedBeneficeWithLevel = assignLevelOfBenefice(selectedBenefice, maxPoints);
		if (selectedBeneficeWithLevel != null) {
			// Only a few afflictions.
			if (selectedBeneficeWithLevel.getBeneficeClassification() == BeneficeClassification.AFFLICTION) {
				if (getCharacterPlayer().getAfflictions().size() < MAX_AFFLICTIONS) {
					getCharacterPlayer().addBenefice(selectedBeneficeWithLevel);
				}
			} else {
				getCharacterPlayer().addBenefice(selectedBeneficeWithLevel);
			}
		}
		removeElementWeight(selectedBenefice);
	}

	@Override
	protected TreeMap<Integer, BeneficeDefinition> assignElementsWeight() throws InvalidXmlElementException {
		TreeMap<Integer, BeneficeDefinition> weightedBenefices = new TreeMap<>();
		int count = 1;
		for (BeneficeDefinition benefice : BeneficeDefinitionFactory.getInstance().getElements(getCharacterPlayer().getLanguage())) {
			int weight = getWeight(benefice);
			if (weight > 0) {
				weightedBenefices.put(count, benefice);
				count += weight;
			}
		}
		return weightedBenefices;
	}

	@Override
	protected int getWeight(BeneficeDefinition benefice) {
		// No restricted benefices.
		if (benefice.getRestricted() != null && benefice.getRestricted() != getCharacterPlayer().getFaction().getFactionGroup()) {
			return 0;
		}

		// Status must almost selected.
		if (benefice.getGroup().equals(BeneficeGroup.STATUS)) {
			return 10;
		}

		// No faction preference selected. All benefices has the same
		// probability.
		return 1;
	}

	/**
	 * Returns a cost for a benefice depending on the preferences of the
	 * character.
	 * 
	 * @param benefice
	 * @param maxPoints
	 * @return
	 */
	private AvailableBenefice assignLevelOfBenefice(BeneficeDefinition benefice, int maxPoints) {
		IGaussianDistribution selectedTraitCost = TraitCostPreferences.getSelected(getPreferences());
		if (benefice.getGroup().equals(BeneficeGroup.STATUS)) {
			// Status has also an special preference.
			IGaussianDistribution selectedStatus = StatusPreferences.getSelected(getPreferences());
			if (selectedStatus != null) {
				selectedTraitCost = selectedStatus;
			}
		}
		int maxRangeSelected = selectedTraitCost.randomGaussian();
		if (maxRangeSelected > maxPoints) {
			maxRangeSelected = maxPoints;
		}
		Set<AvailableBenefice> beneficeLevels = AvailableBeneficeFactory.getInstance().getAvailableBeneficesByDefinition(getCharacterPlayer().getLanguage(),
				benefice);
		List<AvailableBenefice> sortedBenefices = new ArrayList<>(beneficeLevels);
		Collections.sort(sortedBenefices, new Comparator<AvailableBenefice>() {

			@Override
			public int compare(AvailableBenefice o1, AvailableBenefice o2) {
				// Sort by cost (descending).
				return new Integer(o2.getCost()).compareTo(new Integer(o1.getCost()));
			}
		});
		for (AvailableBenefice availableBenefice : sortedBenefices) {
			if (Math.abs(availableBenefice.getCost()) <= maxRangeSelected) {
				return availableBenefice;
			}
		}
		return null;
	}
}
