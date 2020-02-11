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

package com.liferay.portal.security.wedeploy.auth.service.base;

import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.BaseLocalServiceImpl;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.security.wedeploy.auth.model.WeDeployAuthToken;
import com.liferay.portal.security.wedeploy.auth.service.WeDeployAuthTokenLocalService;
import com.liferay.portal.security.wedeploy.auth.service.persistence.WeDeployAuthAppPersistence;
import com.liferay.portal.security.wedeploy.auth.service.persistence.WeDeployAuthTokenPersistence;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Reference;

/**
 * Provides the base implementation for the we deploy auth token local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.portal.security.wedeploy.auth.service.impl.WeDeployAuthTokenLocalServiceImpl}.
 * </p>
 *
 * @author Supritha Sundaram
 * @see com.liferay.portal.security.wedeploy.auth.service.impl.WeDeployAuthTokenLocalServiceImpl
 * @generated
 */
public abstract class WeDeployAuthTokenLocalServiceBaseImpl
	extends BaseLocalServiceImpl
	implements AopService, IdentifiableOSGiService,
			   WeDeployAuthTokenLocalService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>WeDeployAuthTokenLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.portal.security.wedeploy.auth.service.WeDeployAuthTokenLocalServiceUtil</code>.
	 */

	/**
	 * Adds the we deploy auth token to the database. Also notifies the appropriate model listeners.
	 *
	 * @param weDeployAuthToken the we deploy auth token
	 * @return the we deploy auth token that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public WeDeployAuthToken addWeDeployAuthToken(
		WeDeployAuthToken weDeployAuthToken) {

		weDeployAuthToken.setNew(true);

		return weDeployAuthTokenPersistence.update(weDeployAuthToken);
	}

	/**
	 * Creates a new we deploy auth token with the primary key. Does not add the we deploy auth token to the database.
	 *
	 * @param weDeployAuthTokenId the primary key for the new we deploy auth token
	 * @return the new we deploy auth token
	 */
	@Override
	@Transactional(enabled = false)
	public WeDeployAuthToken createWeDeployAuthToken(long weDeployAuthTokenId) {
		return weDeployAuthTokenPersistence.create(weDeployAuthTokenId);
	}

	/**
	 * Deletes the we deploy auth token with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param weDeployAuthTokenId the primary key of the we deploy auth token
	 * @return the we deploy auth token that was removed
	 * @throws PortalException if a we deploy auth token with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public WeDeployAuthToken deleteWeDeployAuthToken(long weDeployAuthTokenId)
		throws PortalException {

		return weDeployAuthTokenPersistence.remove(weDeployAuthTokenId);
	}

	/**
	 * Deletes the we deploy auth token from the database. Also notifies the appropriate model listeners.
	 *
	 * @param weDeployAuthToken the we deploy auth token
	 * @return the we deploy auth token that was removed
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public WeDeployAuthToken deleteWeDeployAuthToken(
		WeDeployAuthToken weDeployAuthToken) {

		return weDeployAuthTokenPersistence.remove(weDeployAuthToken);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(
			WeDeployAuthToken.class, clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return weDeployAuthTokenPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.security.wedeploy.auth.model.impl.WeDeployAuthTokenModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return weDeployAuthTokenPersistence.findWithDynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.security.wedeploy.auth.model.impl.WeDeployAuthTokenModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<T> orderByComparator) {

		return weDeployAuthTokenPersistence.findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return weDeployAuthTokenPersistence.countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		DynamicQuery dynamicQuery, Projection projection) {

		return weDeployAuthTokenPersistence.countWithDynamicQuery(
			dynamicQuery, projection);
	}

	@Override
	public WeDeployAuthToken fetchWeDeployAuthToken(long weDeployAuthTokenId) {
		return weDeployAuthTokenPersistence.fetchByPrimaryKey(
			weDeployAuthTokenId);
	}

	/**
	 * Returns the we deploy auth token with the primary key.
	 *
	 * @param weDeployAuthTokenId the primary key of the we deploy auth token
	 * @return the we deploy auth token
	 * @throws PortalException if a we deploy auth token with the primary key could not be found
	 */
	@Override
	public WeDeployAuthToken getWeDeployAuthToken(long weDeployAuthTokenId)
		throws PortalException {

		return weDeployAuthTokenPersistence.findByPrimaryKey(
			weDeployAuthTokenId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery =
			new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(
			weDeployAuthTokenLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(WeDeployAuthToken.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("weDeployAuthTokenId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(
			weDeployAuthTokenLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(WeDeployAuthToken.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName(
			"weDeployAuthTokenId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {

		actionableDynamicQuery.setBaseLocalService(
			weDeployAuthTokenLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(WeDeployAuthToken.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("weDeployAuthTokenId");
	}

	/**
	 * @throws PortalException
	 */
	public PersistedModel createPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return weDeployAuthTokenPersistence.create(
			((Long)primaryKeyObj).longValue());
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {

		return weDeployAuthTokenLocalService.deleteWeDeployAuthToken(
			(WeDeployAuthToken)persistedModel);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return weDeployAuthTokenPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns a range of all the we deploy auth tokens.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.security.wedeploy.auth.model.impl.WeDeployAuthTokenModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of we deploy auth tokens
	 * @param end the upper bound of the range of we deploy auth tokens (not inclusive)
	 * @return the range of we deploy auth tokens
	 */
	@Override
	public List<WeDeployAuthToken> getWeDeployAuthTokens(int start, int end) {
		return weDeployAuthTokenPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of we deploy auth tokens.
	 *
	 * @return the number of we deploy auth tokens
	 */
	@Override
	public int getWeDeployAuthTokensCount() {
		return weDeployAuthTokenPersistence.countAll();
	}

	/**
	 * Updates the we deploy auth token in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param weDeployAuthToken the we deploy auth token
	 * @return the we deploy auth token that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public WeDeployAuthToken updateWeDeployAuthToken(
		WeDeployAuthToken weDeployAuthToken) {

		return weDeployAuthTokenPersistence.update(weDeployAuthToken);
	}

	@Override
	public Class<?>[] getAopInterfaces() {
		return new Class<?>[] {
			WeDeployAuthTokenLocalService.class, IdentifiableOSGiService.class,
			PersistedModelLocalService.class
		};
	}

	@Override
	public void setAopProxy(Object aopProxy) {
		weDeployAuthTokenLocalService = (WeDeployAuthTokenLocalService)aopProxy;
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return WeDeployAuthTokenLocalService.class.getName();
	}

	protected Class<?> getModelClass() {
		return WeDeployAuthToken.class;
	}

	protected String getModelClassName() {
		return WeDeployAuthToken.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource =
				weDeployAuthTokenPersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(
				dataSource, sql);

			sqlUpdate.update();
		}
		catch (Exception exception) {
			throw new SystemException(exception);
		}
	}

	@Reference
	protected WeDeployAuthAppPersistence weDeployAuthAppPersistence;

	protected WeDeployAuthTokenLocalService weDeployAuthTokenLocalService;

	@Reference
	protected WeDeployAuthTokenPersistence weDeployAuthTokenPersistence;

	@Reference
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.ClassNameLocalService
		classNameLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.ResourceLocalService
		resourceLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.UserLocalService
		userLocalService;

}