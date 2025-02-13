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

package com.liferay.frontend.taglib.clay.data.set.view.cards;

import com.liferay.frontend.taglib.clay.data.set.ClayDataSetDisplayView;
import com.liferay.frontend.taglib.clay.data.set.constants.ClayDataSetConstants;
import com.liferay.petra.string.StringPool;

/**
 * @author Bruno Basto
 */
public abstract class BaseCardsClayDataSetDisplayView
	implements ClayDataSetDisplayView {

	@Override
	public String getContentRenderer() {
		return ClayDataSetConstants.CARDS;
	}

	public abstract String getDescription();

	public String getImage() {
		return StringPool.BLANK;
	}

	@Override
	public String getLabel() {
		return ClayDataSetConstants.CARDS;
	}

	public String getLink() {
		return StringPool.BLANK;
	}

	public String getName() {
		return ClayDataSetConstants.CARDS;
	}

	public String getSticker() {
		return StringPool.BLANK;
	}

	public String getSymbol() {
		return StringPool.BLANK;
	}

	@Override
	public String getThumbnail() {
		return "cards2";
	}

	public abstract String getTitle();

}