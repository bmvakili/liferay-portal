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

package com.liferay.layout.type.controller.asset.display.internal.portlet;

import com.liferay.asset.display.page.portlet.AssetDisplayPageFriendlyURLProvider;
import com.liferay.asset.display.page.util.AssetDisplayPageUtil;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.info.display.contributor.InfoDisplayContributor;
import com.liferay.info.display.contributor.InfoDisplayContributorTracker;
import com.liferay.info.display.contributor.InfoDisplayObjectProvider;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Portal;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Tardín
 */
@Component(service = AssetDisplayPageFriendlyURLProvider.class)
public class AssetDisplayPageFriendlyURLProviderImpl
	implements AssetDisplayPageFriendlyURLProvider {

	@Override
	public String getFriendlyURL(
			String className, long classPK, Locale locale,
			ThemeDisplay themeDisplay)
		throws PortalException {

		InfoDisplayContributor<?> infoDisplayContributor =
			_infoDisplayContributorTracker.getInfoDisplayContributor(className);

		if (infoDisplayContributor == null) {
			return null;
		}

		InfoDisplayObjectProvider<?> infoDisplayObjectProvider =
			infoDisplayContributor.getInfoDisplayObjectProvider(classPK);

		if (infoDisplayObjectProvider == null) {
			return null;
		}

		if (!AssetDisplayPageUtil.hasAssetDisplayPage(
				themeDisplay.getScopeGroupId(),
				infoDisplayObjectProvider.getClassNameId(),
				infoDisplayObjectProvider.getClassPK(),
				infoDisplayObjectProvider.getClassTypeId())) {

			return null;
		}

		StringBundler sb = new StringBundler(3);

		sb.append(
			_getGroupFriendlyURL(
				infoDisplayObjectProvider.getGroupId(), locale, themeDisplay));

		sb.append(infoDisplayContributor.getInfoURLSeparator());
		sb.append(infoDisplayObjectProvider.getURLTitle(locale));

		return sb.toString();
	}

	@Override
	public String getFriendlyURL(
			String className, long classPK, ThemeDisplay themeDisplay)
		throws PortalException {

		return getFriendlyURL(
			className, classPK, themeDisplay.getLocale(), themeDisplay);
	}

	private String _getGroupFriendlyURL(
			long groupId, Locale locale, ThemeDisplay themeDisplay)
		throws PortalException {

		Group group = _groupLocalService.getGroup(groupId);

		if (locale != null) {
			return _portal.getGroupFriendlyURL(
				group.getPublicLayoutSet(), themeDisplay, locale);
		}

		return _portal.getGroupFriendlyURL(
			group.getPublicLayoutSet(), themeDisplay);
	}

	@Reference
	private AssetEntryLocalService _assetEntryLocalService;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private InfoDisplayContributorTracker _infoDisplayContributorTracker;

	@Reference
	private Portal _portal;

}