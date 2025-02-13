<%--
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
--%>

<%@ include file="/init.jsp" %>

<%
SearchContainer<?> searchContainer = (SearchContainer<?>)request.getAttribute("liferay-ui:search:searchContainer");

PortletURL iteratorURL = searchContainer.getIteratorURL();

String redirect = iteratorURL.toString();

ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

CustomAttributesDisplay customAttributesDisplay = (CustomAttributesDisplay)row.getParameter("customAttributesDisplay");
%>

<c:if test="<%= permissionChecker.isCompanyAdmin() %>">
	<portlet:renderURL var="editURL">
		<portlet:param name="mvcPath" value="/view_attributes.jsp" />
		<portlet:param name="redirect" value="<%= redirect %>" />
		<portlet:param name="modelResource" value="<%= customAttributesDisplay.getClassName() %>" />
	</portlet:renderURL>

	<liferay-ui:icon
		icon="edit"
		markupView="lexicon"
		message="edit"
		url="<%= editURL %>"
	/>
</c:if>