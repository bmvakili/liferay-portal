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

package com.liferay.portal.vulcan.internal.jaxrs.context.provider;

import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.odata.entity.CollectionEntityField;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.aggregation.Aggregation;
import com.liferay.portal.vulcan.internal.accept.language.AcceptLanguageImpl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.ServerErrorException;
import javax.ws.rs.ext.Provider;

import org.apache.cxf.jaxrs.ext.ContextProvider;
import org.apache.cxf.message.Message;

/**
 * @author Javier Gamarra
 */
@Provider
public class AggregationContextProvider
	implements ContextProvider<Aggregation> {

	public AggregationContextProvider(Language language, Portal portal) {
		_language = language;
		_portal = portal;
	}

	@Override
	public Aggregation createContext(Message message) {
		try {
			HttpServletRequest httpServletRequest =
				ContextProviderUtil.getHttpServletRequest(message);

			return _createContext(
				new AcceptLanguageImpl(httpServletRequest, _language, _portal),
				ParamUtil.getStringValues(
					httpServletRequest, "aggregationTerms"),
				ContextProviderUtil.getEntityModel(message));
		}
		catch (Exception exception) {
			throw new ServerErrorException(500, exception);
		}
	}

	private Aggregation _createContext(
		AcceptLanguage acceptLanguage, String[] aggregationTermsArray,
		EntityModel entityModel) {

		if ((aggregationTermsArray == null) || (entityModel == null)) {
			return null;
		}

		Map<String, EntityField> entityFieldsMap =
			entityModel.getEntityFieldsMap();

		Aggregation aggregation = new Aggregation();

		Map<String, String> aggregationTerms =
			aggregation.getAggregationTerms();

		for (String aggregationTerm : aggregationTermsArray) {
			if (entityFieldsMap.containsKey(aggregationTerm)) {
				EntityField entityField = entityFieldsMap.get(aggregationTerm);

				if (EntityField.Type.COLLECTION.equals(entityField.getType())) {
					CollectionEntityField collectionEntityField =
						(CollectionEntityField)entityField;

					entityField = collectionEntityField.getEntityField();
				}

				aggregationTerms.put(
					aggregationTerm,
					entityField.getFilterableName(
						acceptLanguage.getPreferredLocale()));
			}
			else {
				aggregationTerms.put(aggregationTerm, aggregationTerm);
			}
		}

		return aggregation;
	}

	private final Language _language;
	private final Portal _portal;

}