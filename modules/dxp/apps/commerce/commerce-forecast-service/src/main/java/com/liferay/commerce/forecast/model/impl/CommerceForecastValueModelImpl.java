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

package com.liferay.commerce.forecast.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.commerce.forecast.model.CommerceForecastValue;
import com.liferay.commerce.forecast.model.CommerceForecastValueModel;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Types;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The base model implementation for the CommerceForecastValue service. Represents a row in the &quot;CommerceForecastValue&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link CommerceForecastValueModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link CommerceForecastValueImpl}.
 * </p>
 *
 * @author Andrea Di Giorgi
 * @see CommerceForecastValueImpl
 * @see CommerceForecastValue
 * @see CommerceForecastValueModel
 * @generated
 */
@ProviderType
public class CommerceForecastValueModelImpl extends BaseModelImpl<CommerceForecastValue>
	implements CommerceForecastValueModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a commerce forecast value model instance should use the {@link CommerceForecastValue} interface instead.
	 */
	public static final String TABLE_NAME = "CommerceForecastValue";
	public static final Object[][] TABLE_COLUMNS = {
			{ "commerceForecastValueId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "userId", Types.BIGINT },
			{ "userName", Types.VARCHAR },
			{ "createDate", Types.TIMESTAMP },
			{ "modifiedDate", Types.TIMESTAMP },
			{ "commerceForecastEntryId", Types.BIGINT },
			{ "date_", Types.TIMESTAMP },
			{ "lowerValue", Types.DECIMAL },
			{ "value", Types.DECIMAL },
			{ "upperValue", Types.DECIMAL }
		};
	public static final Map<String, Integer> TABLE_COLUMNS_MAP = new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("commerceForecastValueId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("commerceForecastEntryId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("date_", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("lowerValue", Types.DECIMAL);
		TABLE_COLUMNS_MAP.put("value", Types.DECIMAL);
		TABLE_COLUMNS_MAP.put("upperValue", Types.DECIMAL);
	}

	public static final String TABLE_SQL_CREATE = "create table CommerceForecastValue (commerceForecastValueId LONG not null primary key,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,commerceForecastEntryId LONG,date_ DATE null,lowerValue DECIMAL(30, 16) null,value DECIMAL(30, 16) null,upperValue DECIMAL(30, 16) null)";
	public static final String TABLE_SQL_DROP = "drop table CommerceForecastValue";
	public static final String ORDER_BY_JPQL = " ORDER BY commerceForecastValue.date ASC";
	public static final String ORDER_BY_SQL = " ORDER BY CommerceForecastValue.date_ ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.commerce.forecast.service.util.ServiceProps.get(
				"value.object.entity.cache.enabled.com.liferay.commerce.forecast.model.CommerceForecastValue"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.commerce.forecast.service.util.ServiceProps.get(
				"value.object.finder.cache.enabled.com.liferay.commerce.forecast.model.CommerceForecastValue"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.commerce.forecast.service.util.ServiceProps.get(
				"value.object.column.bitmask.enabled.com.liferay.commerce.forecast.model.CommerceForecastValue"),
			true);
	public static final long COMMERCEFORECASTENTRYID_COLUMN_BITMASK = 1L;
	public static final long DATE_COLUMN_BITMASK = 2L;
	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.commerce.forecast.service.util.ServiceProps.get(
				"lock.expiration.time.com.liferay.commerce.forecast.model.CommerceForecastValue"));

	public CommerceForecastValueModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _commerceForecastValueId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setCommerceForecastValueId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _commerceForecastValueId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return CommerceForecastValue.class;
	}

	@Override
	public String getModelClassName() {
		return CommerceForecastValue.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("commerceForecastValueId", getCommerceForecastValueId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("commerceForecastEntryId", getCommerceForecastEntryId());
		attributes.put("date", getDate());
		attributes.put("lowerValue", getLowerValue());
		attributes.put("value", getValue());
		attributes.put("upperValue", getUpperValue());

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long commerceForecastValueId = (Long)attributes.get(
				"commerceForecastValueId");

		if (commerceForecastValueId != null) {
			setCommerceForecastValueId(commerceForecastValueId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		Long commerceForecastEntryId = (Long)attributes.get(
				"commerceForecastEntryId");

		if (commerceForecastEntryId != null) {
			setCommerceForecastEntryId(commerceForecastEntryId);
		}

		Date date = (Date)attributes.get("date");

		if (date != null) {
			setDate(date);
		}

		BigDecimal lowerValue = (BigDecimal)attributes.get("lowerValue");

		if (lowerValue != null) {
			setLowerValue(lowerValue);
		}

		BigDecimal value = (BigDecimal)attributes.get("value");

		if (value != null) {
			setValue(value);
		}

		BigDecimal upperValue = (BigDecimal)attributes.get("upperValue");

		if (upperValue != null) {
			setUpperValue(upperValue);
		}
	}

	@Override
	public long getCommerceForecastValueId() {
		return _commerceForecastValueId;
	}

	@Override
	public void setCommerceForecastValueId(long commerceForecastValueId) {
		_commerceForecastValueId = commerceForecastValueId;
	}

	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	@Override
	public long getUserId() {
		return _userId;
	}

	@Override
	public void setUserId(long userId) {
		_userId = userId;
	}

	@Override
	public String getUserUuid() {
		try {
			User user = UserLocalServiceUtil.getUserById(getUserId());

			return user.getUuid();
		}
		catch (PortalException pe) {
			return "";
		}
	}

	@Override
	public void setUserUuid(String userUuid) {
	}

	@Override
	public String getUserName() {
		if (_userName == null) {
			return "";
		}
		else {
			return _userName;
		}
	}

	@Override
	public void setUserName(String userName) {
		_userName = userName;
	}

	@Override
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	@Override
	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public boolean hasSetModifiedDate() {
		return _setModifiedDate;
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		_setModifiedDate = true;

		_modifiedDate = modifiedDate;
	}

	@Override
	public long getCommerceForecastEntryId() {
		return _commerceForecastEntryId;
	}

	@Override
	public void setCommerceForecastEntryId(long commerceForecastEntryId) {
		_columnBitmask |= COMMERCEFORECASTENTRYID_COLUMN_BITMASK;

		if (!_setOriginalCommerceForecastEntryId) {
			_setOriginalCommerceForecastEntryId = true;

			_originalCommerceForecastEntryId = _commerceForecastEntryId;
		}

		_commerceForecastEntryId = commerceForecastEntryId;
	}

	public long getOriginalCommerceForecastEntryId() {
		return _originalCommerceForecastEntryId;
	}

	@Override
	public Date getDate() {
		return _date;
	}

	@Override
	public void setDate(Date date) {
		_columnBitmask = -1L;

		if (_originalDate == null) {
			_originalDate = _date;
		}

		_date = date;
	}

	public Date getOriginalDate() {
		return _originalDate;
	}

	@Override
	public BigDecimal getLowerValue() {
		return _lowerValue;
	}

	@Override
	public void setLowerValue(BigDecimal lowerValue) {
		_lowerValue = lowerValue;
	}

	@Override
	public BigDecimal getValue() {
		return _value;
	}

	@Override
	public void setValue(BigDecimal value) {
		_value = value;
	}

	@Override
	public BigDecimal getUpperValue() {
		return _upperValue;
	}

	@Override
	public void setUpperValue(BigDecimal upperValue) {
		_upperValue = upperValue;
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
			CommerceForecastValue.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public CommerceForecastValue toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (CommerceForecastValue)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		CommerceForecastValueImpl commerceForecastValueImpl = new CommerceForecastValueImpl();

		commerceForecastValueImpl.setCommerceForecastValueId(getCommerceForecastValueId());
		commerceForecastValueImpl.setCompanyId(getCompanyId());
		commerceForecastValueImpl.setUserId(getUserId());
		commerceForecastValueImpl.setUserName(getUserName());
		commerceForecastValueImpl.setCreateDate(getCreateDate());
		commerceForecastValueImpl.setModifiedDate(getModifiedDate());
		commerceForecastValueImpl.setCommerceForecastEntryId(getCommerceForecastEntryId());
		commerceForecastValueImpl.setDate(getDate());
		commerceForecastValueImpl.setLowerValue(getLowerValue());
		commerceForecastValueImpl.setValue(getValue());
		commerceForecastValueImpl.setUpperValue(getUpperValue());

		commerceForecastValueImpl.resetOriginalValues();

		return commerceForecastValueImpl;
	}

	@Override
	public int compareTo(CommerceForecastValue commerceForecastValue) {
		int value = 0;

		value = DateUtil.compareTo(getDate(), commerceForecastValue.getDate());

		if (value != 0) {
			return value;
		}

		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof CommerceForecastValue)) {
			return false;
		}

		CommerceForecastValue commerceForecastValue = (CommerceForecastValue)obj;

		long primaryKey = commerceForecastValue.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return ENTITY_CACHE_ENABLED;
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return FINDER_CACHE_ENABLED;
	}

	@Override
	public void resetOriginalValues() {
		CommerceForecastValueModelImpl commerceForecastValueModelImpl = this;

		commerceForecastValueModelImpl._setModifiedDate = false;

		commerceForecastValueModelImpl._originalCommerceForecastEntryId = commerceForecastValueModelImpl._commerceForecastEntryId;

		commerceForecastValueModelImpl._setOriginalCommerceForecastEntryId = false;

		commerceForecastValueModelImpl._originalDate = commerceForecastValueModelImpl._date;

		commerceForecastValueModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<CommerceForecastValue> toCacheModel() {
		CommerceForecastValueCacheModel commerceForecastValueCacheModel = new CommerceForecastValueCacheModel();

		commerceForecastValueCacheModel.commerceForecastValueId = getCommerceForecastValueId();

		commerceForecastValueCacheModel.companyId = getCompanyId();

		commerceForecastValueCacheModel.userId = getUserId();

		commerceForecastValueCacheModel.userName = getUserName();

		String userName = commerceForecastValueCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			commerceForecastValueCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			commerceForecastValueCacheModel.createDate = createDate.getTime();
		}
		else {
			commerceForecastValueCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			commerceForecastValueCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			commerceForecastValueCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		commerceForecastValueCacheModel.commerceForecastEntryId = getCommerceForecastEntryId();

		Date date = getDate();

		if (date != null) {
			commerceForecastValueCacheModel.date = date.getTime();
		}
		else {
			commerceForecastValueCacheModel.date = Long.MIN_VALUE;
		}

		commerceForecastValueCacheModel.lowerValue = getLowerValue();

		commerceForecastValueCacheModel.value = getValue();

		commerceForecastValueCacheModel.upperValue = getUpperValue();

		return commerceForecastValueCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(23);

		sb.append("{commerceForecastValueId=");
		sb.append(getCommerceForecastValueId());
		sb.append(", companyId=");
		sb.append(getCompanyId());
		sb.append(", userId=");
		sb.append(getUserId());
		sb.append(", userName=");
		sb.append(getUserName());
		sb.append(", createDate=");
		sb.append(getCreateDate());
		sb.append(", modifiedDate=");
		sb.append(getModifiedDate());
		sb.append(", commerceForecastEntryId=");
		sb.append(getCommerceForecastEntryId());
		sb.append(", date=");
		sb.append(getDate());
		sb.append(", lowerValue=");
		sb.append(getLowerValue());
		sb.append(", value=");
		sb.append(getValue());
		sb.append(", upperValue=");
		sb.append(getUpperValue());
		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		StringBundler sb = new StringBundler(37);

		sb.append("<model><model-name>");
		sb.append("com.liferay.commerce.forecast.model.CommerceForecastValue");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>commerceForecastValueId</column-name><column-value><![CDATA[");
		sb.append(getCommerceForecastValueId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>companyId</column-name><column-value><![CDATA[");
		sb.append(getCompanyId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>userId</column-name><column-value><![CDATA[");
		sb.append(getUserId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>userName</column-name><column-value><![CDATA[");
		sb.append(getUserName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>createDate</column-name><column-value><![CDATA[");
		sb.append(getCreateDate());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>modifiedDate</column-name><column-value><![CDATA[");
		sb.append(getModifiedDate());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>commerceForecastEntryId</column-name><column-value><![CDATA[");
		sb.append(getCommerceForecastEntryId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>date</column-name><column-value><![CDATA[");
		sb.append(getDate());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>lowerValue</column-name><column-value><![CDATA[");
		sb.append(getLowerValue());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>value</column-name><column-value><![CDATA[");
		sb.append(getValue());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>upperValue</column-name><column-value><![CDATA[");
		sb.append(getUpperValue());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static final ClassLoader _classLoader = CommerceForecastValue.class.getClassLoader();
	private static final Class<?>[] _escapedModelInterfaces = new Class[] {
			CommerceForecastValue.class, ModelWrapper.class
		};
	private long _commerceForecastValueId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _setModifiedDate;
	private long _commerceForecastEntryId;
	private long _originalCommerceForecastEntryId;
	private boolean _setOriginalCommerceForecastEntryId;
	private Date _date;
	private Date _originalDate;
	private BigDecimal _lowerValue;
	private BigDecimal _value;
	private BigDecimal _upperValue;
	private long _columnBitmask;
	private CommerceForecastValue _escapedModel;
}