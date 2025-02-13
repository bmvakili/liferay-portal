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

import {CollectionConfigurationPanel} from '../../../app/components/floating-toolbar/CollectionConfigurationPanel';
import ContainerLinkPanel from '../../../app/components/floating-toolbar/ContainerLinkPanel';
import {ContainerStylesPanel} from '../../../app/components/floating-toolbar/ContainerStylesPanel';
import EditableLinkPanel from '../../../app/components/floating-toolbar/EditableLinkPanel';
import {FragmentConfigurationPanel} from '../../../app/components/floating-toolbar/FragmentConfigurationPanel';
import {FragmentStylesPanel} from '../../../app/components/floating-toolbar/FragmentStylesPanel';
import {ImagePropertiesPanel} from '../../../app/components/floating-toolbar/ImagePropertiesPanel';
import {MappingPanel} from '../../../app/components/floating-toolbar/MappingPanel';
import {RowConfigurationPanel} from '../../../app/components/floating-toolbar/RowConfigurationPanel';
import {RowStylesPanel} from '../../../app/components/floating-toolbar/RowStylesPanel';
import isMapped from '../../../app/components/fragment-content/isMapped';
import {EDITABLE_TYPES} from '../../../app/config/constants/editableTypes';
import {FRAGMENT_CONFIGURATION_ROLES} from '../../../app/config/constants/fragmentConfigurationRoles';
import {ITEM_TYPES} from '../../../app/config/constants/itemTypes';
import {LAYOUT_DATA_ITEM_TYPES} from '../../../app/config/constants/layoutDataItemTypes';
import {VIEWPORT_SIZES} from '../../../app/config/constants/viewportSizes';
import selectCanUpdateEditables from '../../../app/selectors/selectCanUpdateEditables';
import selectCanUpdateItemConfiguration from '../../../app/selectors/selectCanUpdateItemConfiguration';
import selectEditableValue from '../../../app/selectors/selectEditableValue';

export const PANEL_IDS = {
	collectionConfiguration: 'collectionConfiguration',
	containerLink: 'containerLink',
	containerStyles: 'containerStyles',
	editableLink: 'editableLink',
	editableMapping: 'editableMapping',
	fragmentConfiguration: 'fragmentConfiguration',
	fragmentStyles: 'fragmentStyles',
	imageProperties: 'imageProperties',
	rowConfiguration: 'rowConfiguration',
	rowStyles: 'rowStyles',
};

export const PANELS = {
	[PANEL_IDS.collectionConfiguration]: {
		component: CollectionConfigurationPanel,
		label: Liferay.Language.get('configuration'),
	},
	[PANEL_IDS.containerLink]: {
		component: ContainerLinkPanel,
		label: Liferay.Language.get('link'),
	},
	[PANEL_IDS.containerStyles]: {
		component: ContainerStylesPanel,
		label: Liferay.Language.get('styles'),
	},
	[PANEL_IDS.editableLink]: {
		component: EditableLinkPanel,
		label: Liferay.Language.get('link'),
	},
	[PANEL_IDS.editableMapping]: {
		component: MappingPanel,
		label: Liferay.Language.get('mapping'),
	},
	[PANEL_IDS.fragmentConfiguration]: {
		component: FragmentConfigurationPanel,
		label: Liferay.Language.get('configuration'),
	},
	[PANEL_IDS.fragmentStyles]: {
		component: FragmentStylesPanel,
		label: Liferay.Language.get('styles'),
	},
	[PANEL_IDS.imageProperties]: {
		component: ImagePropertiesPanel,
		label: Liferay.Language.get('image'),
	},
	[PANEL_IDS.rowConfiguration]: {
		component: RowConfigurationPanel,
		label: Liferay.Language.get('configuration'),
	},
	[PANEL_IDS.rowStyles]: {
		component: RowStylesPanel,
		label: Liferay.Language.get('styles'),
	},
};

export const selectPanels = (activeItemId, activeItemType, state) => {
	let activeItem = null;
	let panelsIds = {};

	if (activeItemType === ITEM_TYPES.layoutDataItem) {
		activeItem = state.layoutData.items[activeItemId];
	}
	else if (activeItemType === ITEM_TYPES.editable) {
		const [fragmentEntryLinkId] = activeItemId.split('-');
		activeItem = state.editables[fragmentEntryLinkId]?.[activeItemId];
	}

	if (!activeItem) {
		return {activeItem, panelsIds};
	}

	const canUpdateEditables = selectCanUpdateEditables(state);
	const canUpdateItemConfiguration = selectCanUpdateItemConfiguration(state);

	if (canUpdateEditables && activeItem.editableId) {
		const editableValue = selectEditableValue(
			state,
			activeItem.fragmentEntryLinkId,
			activeItem.editableId,
			activeItem.editableValueNamespace
		);
		const editableIsMapped = isMapped(editableValue);

		panelsIds = {
			[PANEL_IDS.editableLink]: [
				EDITABLE_TYPES.text,
				EDITABLE_TYPES.image,
				EDITABLE_TYPES.link,
			].includes(activeItem.type),
			[PANEL_IDS.imageProperties]:
				!editableIsMapped &&
				[EDITABLE_TYPES.image, EDITABLE_TYPES.backgroundImage].includes(
					activeItem.type
				),
			[PANEL_IDS.editableMapping]: true,
		};
	}
	else if (activeItem.type === LAYOUT_DATA_ITEM_TYPES.collection) {
		panelsIds = {
			[PANEL_IDS.collectionConfiguration]: canUpdateItemConfiguration,
		};
	}
	else if (activeItem.type === LAYOUT_DATA_ITEM_TYPES.container) {
		panelsIds = {
			[PANEL_IDS.containerStyles]: canUpdateItemConfiguration,
			[PANEL_IDS.containerLink]: canUpdateItemConfiguration,
		};
	}
	else if (activeItem.type === LAYOUT_DATA_ITEM_TYPES.fragment) {
		const fieldSets =
			state.fragmentEntryLinks[activeItem.config.fragmentEntryLinkId]
				?.configuration?.fieldSets ?? [];

		panelsIds = {
			[PANEL_IDS.fragmentStyles]:
				canUpdateItemConfiguration &&
				fieldSets.some(
					(fieldSet) =>
						fieldSet.configurationRole ===
						FRAGMENT_CONFIGURATION_ROLES.style
				),
			[PANEL_IDS.fragmentConfiguration]:
				canUpdateItemConfiguration &&
				fieldSets.some(
					(fieldSet) =>
						fieldSet.configurationRole !==
						FRAGMENT_CONFIGURATION_ROLES.style
				),
		};
	}
	else if (activeItem.type === LAYOUT_DATA_ITEM_TYPES.row) {
		panelsIds = {
			[PANEL_IDS.rowStyles]: canUpdateItemConfiguration,
			[PANEL_IDS.rowConfiguration]:
				canUpdateItemConfiguration &&
				state.selectedViewportSize === VIEWPORT_SIZES.desktop,
		};
	}

	return {activeItem, panelsIds};
};
