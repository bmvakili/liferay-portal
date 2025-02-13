/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

import PropTypes from 'prop-types';
import React, {useCallback} from 'react';

import {getLayoutDataItemPropTypes} from '../../../prop-types/index';
import {FRAGMENT_CONFIGURATION_ROLES} from '../../config/constants/fragmentConfigurationRoles';
import {FREEMARKER_FRAGMENT_ENTRY_PROCESSOR} from '../../config/constants/freemarkerFragmentEntryProcessor';
import selectSegmentsExperienceId from '../../selectors/selectSegmentsExperienceId';
import {useDispatch, useSelector} from '../../store/index';
import updateFragmentConfiguration from '../../thunks/updateFragmentConfiguration';
import {FieldSet} from './FieldSet';

export const FragmentConfigurationPanel = ({item}) => {
	const dispatch = useDispatch();

	const fragmentEntryLink = useSelector(
		(state) => state.fragmentEntryLinks[item.config.fragmentEntryLinkId]
	);

	const segmentsExperienceId = useSelector(selectSegmentsExperienceId);

	const fieldSets = fragmentEntryLink.configuration?.fieldSets.filter(
		(fieldSet) =>
			fieldSet.configurationRole !== FRAGMENT_CONFIGURATION_ROLES.style
	);

	const defaultConfigurationValues =
		fragmentEntryLink.defaultConfigurationValues;

	const onValueSelect = useCallback(
		(name, value) => {
			const configurationValues = getConfigurationValues(
				defaultConfigurationValues,
				fragmentEntryLink
			);

			const nextConfigurationValues = {
				...configurationValues,
				[name]: value,
			};

			dispatch(
				updateFragmentConfiguration({
					configurationValues: nextConfigurationValues,
					fragmentEntryLink,
					segmentsExperienceId,
				})
			);
		},
		[
			defaultConfigurationValues,
			dispatch,
			fragmentEntryLink,
			segmentsExperienceId,
		]
	);

	return (
		<>
			{fieldSets.map((fieldSet, index) => {
				return (
					<FieldSet
						configurationValues={getConfigurationValues(
							defaultConfigurationValues,
							fragmentEntryLink
						)}
						fields={fieldSet.fields}
						key={index}
						label={fieldSet.label}
						onValueSelect={onValueSelect}
					/>
				);
			})}
		</>
	);
};

FragmentConfigurationPanel.propTypes = {
	item: getLayoutDataItemPropTypes({
		config: PropTypes.shape({
			fragmentEntryLinkId: PropTypes.string.isRequired,
		}).isRequired,
	}),
};

function getConfigurationValues(defaultConfigurationValues, fragmentEntryLink) {
	return {
		...defaultConfigurationValues,
		...fragmentEntryLink.editableValues[
			FREEMARKER_FRAGMENT_ENTRY_PROCESSOR
		],
	};
}
