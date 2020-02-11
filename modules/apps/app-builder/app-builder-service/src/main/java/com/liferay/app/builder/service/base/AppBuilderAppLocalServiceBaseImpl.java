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

package com.liferay.app.builder.service.base;

import com.liferay.app.builder.model.AppBuilderApp;
import com.liferay.app.builder.service.AppBuilderAppLocalService;
import com.liferay.app.builder.service.persistence.AppBuilderAppFinder;
import com.liferay.app.builder.service.persistence.AppBuilderAppPersistence;
import com.liferay.exportimport.kernel.lar.ExportImportHelperUtil;
import com.liferay.exportimport.kernel.lar.ManifestSummary;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery;
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

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Reference;

/**
 * Provides the base implementation for the app builder app local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.app.builder.service.impl.AppBuilderAppLocalServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.app.builder.service.impl.AppBuilderAppLocalServiceImpl
 * @generated
 */
public abstract class AppBuilderAppLocalServiceBaseImpl
	extends BaseLocalServiceImpl
	implements AopService, AppBuilderAppLocalService, IdentifiableOSGiService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>AppBuilderAppLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.app.builder.service.AppBuilderAppLocalServiceUtil</code>.
	 */

	/**
	 * Adds the app builder app to the database. Also notifies the appropriate model listeners.
	 *
	 * @param appBuilderApp the app builder app
	 * @return the app builder app that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public AppBuilderApp addAppBuilderApp(AppBuilderApp appBuilderApp) {
		appBuilderApp.setNew(true);

		return appBuilderAppPersistence.update(appBuilderApp);
	}

	/**
	 * Creates a new app builder app with the primary key. Does not add the app builder app to the database.
	 *
	 * @param appBuilderAppId the primary key for the new app builder app
	 * @return the new app builder app
	 */
	@Override
	@Transactional(enabled = false)
	public AppBuilderApp createAppBuilderApp(long appBuilderAppId) {
		return appBuilderAppPersistence.create(appBuilderAppId);
	}

	/**
	 * Deletes the app builder app with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param appBuilderAppId the primary key of the app builder app
	 * @return the app builder app that was removed
	 * @throws PortalException if a app builder app with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public AppBuilderApp deleteAppBuilderApp(long appBuilderAppId)
		throws PortalException {

		return appBuilderAppPersistence.remove(appBuilderAppId);
	}

	/**
	 * Deletes the app builder app from the database. Also notifies the appropriate model listeners.
	 *
	 * @param appBuilderApp the app builder app
	 * @return the app builder app that was removed
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public AppBuilderApp deleteAppBuilderApp(AppBuilderApp appBuilderApp) {
		return appBuilderAppPersistence.remove(appBuilderApp);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(
			AppBuilderApp.class, clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return appBuilderAppPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.app.builder.model.impl.AppBuilderAppModelImpl</code>.
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

		return appBuilderAppPersistence.findWithDynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.app.builder.model.impl.AppBuilderAppModelImpl</code>.
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

		return appBuilderAppPersistence.findWithDynamicQuery(
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
		return appBuilderAppPersistence.countWithDynamicQuery(dynamicQuery);
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

		return appBuilderAppPersistence.countWithDynamicQuery(
			dynamicQuery, projection);
	}

	@Override
	public AppBuilderApp fetchAppBuilderApp(long appBuilderAppId) {
		return appBuilderAppPersistence.fetchByPrimaryKey(appBuilderAppId);
	}

	/**
	 * Returns the app builder app matching the UUID and group.
	 *
	 * @param uuid the app builder app's UUID
	 * @param groupId the primary key of the group
	 * @return the matching app builder app, or <code>null</code> if a matching app builder app could not be found
	 */
	@Override
	public AppBuilderApp fetchAppBuilderAppByUuidAndGroupId(
		String uuid, long groupId) {

		return appBuilderAppPersistence.fetchByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the app builder app with the primary key.
	 *
	 * @param appBuilderAppId the primary key of the app builder app
	 * @return the app builder app
	 * @throws PortalException if a app builder app with the primary key could not be found
	 */
	@Override
	public AppBuilderApp getAppBuilderApp(long appBuilderAppId)
		throws PortalException {

		return appBuilderAppPersistence.findByPrimaryKey(appBuilderAppId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery =
			new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(appBuilderAppLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(AppBuilderApp.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("appBuilderAppId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(
			appBuilderAppLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(AppBuilderApp.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName(
			"appBuilderAppId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {

		actionableDynamicQuery.setBaseLocalService(appBuilderAppLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(AppBuilderApp.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("appBuilderAppId");
	}

	@Override
	public ExportActionableDynamicQuery getExportActionableDynamicQuery(
		final PortletDataContext portletDataContext) {

		final ExportActionableDynamicQuery exportActionableDynamicQuery =
			new ExportActionableDynamicQuery() {

				@Override
				public long performCount() throws PortalException {
					ManifestSummary manifestSummary =
						portletDataContext.getManifestSummary();

					StagedModelType stagedModelType = getStagedModelType();

					long modelAdditionCount = super.performCount();

					manifestSummary.addModelAdditionCount(
						stagedModelType, modelAdditionCount);

					long modelDeletionCount =
						ExportImportHelperUtil.getModelDeletionCount(
							portletDataContext, stagedModelType);

					manifestSummary.addModelDeletionCount(
						stagedModelType, modelDeletionCount);

					return modelAdditionCount;
				}

			};

		initActionableDynamicQuery(exportActionableDynamicQuery);

		exportActionableDynamicQuery.setAddCriteriaMethod(
			new ActionableDynamicQuery.AddCriteriaMethod() {

				@Override
				public void addCriteria(DynamicQuery dynamicQuery) {
					portletDataContext.addDateRangeCriteria(
						dynamicQuery, "modifiedDate");
				}

			});

		exportActionableDynamicQuery.setCompanyId(
			portletDataContext.getCompanyId());

		exportActionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod<AppBuilderApp>() {

				@Override
				public void performAction(AppBuilderApp appBuilderApp)
					throws PortalException {

					StagedModelDataHandlerUtil.exportStagedModel(
						portletDataContext, appBuilderApp);
				}

			});
		exportActionableDynamicQuery.setStagedModelType(
			new StagedModelType(
				PortalUtil.getClassNameId(AppBuilderApp.class.getName())));

		return exportActionableDynamicQuery;
	}

	/**
	 * @throws PortalException
	 */
	public PersistedModel createPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return appBuilderAppPersistence.create(
			((Long)primaryKeyObj).longValue());
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {

		return appBuilderAppLocalService.deleteAppBuilderApp(
			(AppBuilderApp)persistedModel);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return appBuilderAppPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns all the app builder apps matching the UUID and company.
	 *
	 * @param uuid the UUID of the app builder apps
	 * @param companyId the primary key of the company
	 * @return the matching app builder apps, or an empty list if no matches were found
	 */
	@Override
	public List<AppBuilderApp> getAppBuilderAppsByUuidAndCompanyId(
		String uuid, long companyId) {

		return appBuilderAppPersistence.findByUuid_C(uuid, companyId);
	}

	/**
	 * Returns a range of app builder apps matching the UUID and company.
	 *
	 * @param uuid the UUID of the app builder apps
	 * @param companyId the primary key of the company
	 * @param start the lower bound of the range of app builder apps
	 * @param end the upper bound of the range of app builder apps (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the range of matching app builder apps, or an empty list if no matches were found
	 */
	@Override
	public List<AppBuilderApp> getAppBuilderAppsByUuidAndCompanyId(
		String uuid, long companyId, int start, int end,
		OrderByComparator<AppBuilderApp> orderByComparator) {

		return appBuilderAppPersistence.findByUuid_C(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns the app builder app matching the UUID and group.
	 *
	 * @param uuid the app builder app's UUID
	 * @param groupId the primary key of the group
	 * @return the matching app builder app
	 * @throws PortalException if a matching app builder app could not be found
	 */
	@Override
	public AppBuilderApp getAppBuilderAppByUuidAndGroupId(
			String uuid, long groupId)
		throws PortalException {

		return appBuilderAppPersistence.findByUUID_G(uuid, groupId);
	}

	/**
	 * Returns a range of all the app builder apps.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.app.builder.model.impl.AppBuilderAppModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of app builder apps
	 * @param end the upper bound of the range of app builder apps (not inclusive)
	 * @return the range of app builder apps
	 */
	@Override
	public List<AppBuilderApp> getAppBuilderApps(int start, int end) {
		return appBuilderAppPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of app builder apps.
	 *
	 * @return the number of app builder apps
	 */
	@Override
	public int getAppBuilderAppsCount() {
		return appBuilderAppPersistence.countAll();
	}

	/**
	 * Updates the app builder app in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param appBuilderApp the app builder app
	 * @return the app builder app that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public AppBuilderApp updateAppBuilderApp(AppBuilderApp appBuilderApp) {
		return appBuilderAppPersistence.update(appBuilderApp);
	}

	@Override
	public Class<?>[] getAopInterfaces() {
		return new Class<?>[] {
			AppBuilderAppLocalService.class, IdentifiableOSGiService.class,
			PersistedModelLocalService.class
		};
	}

	@Override
	public void setAopProxy(Object aopProxy) {
		appBuilderAppLocalService = (AppBuilderAppLocalService)aopProxy;
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return AppBuilderAppLocalService.class.getName();
	}

	protected Class<?> getModelClass() {
		return AppBuilderApp.class;
	}

	protected String getModelClassName() {
		return AppBuilderApp.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = appBuilderAppPersistence.getDataSource();

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

	protected AppBuilderAppLocalService appBuilderAppLocalService;

	@Reference
	protected AppBuilderAppPersistence appBuilderAppPersistence;

	@Reference
	protected AppBuilderAppFinder appBuilderAppFinder;

	@Reference
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.UserLocalService
		userLocalService;

}