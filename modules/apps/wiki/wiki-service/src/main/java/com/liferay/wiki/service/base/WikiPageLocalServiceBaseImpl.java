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

package com.liferay.wiki.service.base;

import com.liferay.exportimport.kernel.lar.ExportImportHelperUtil;
import com.liferay.exportimport.kernel.lar.ManifestSummary;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandler;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandlerRegistryUtil;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Conjunction;
import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Disjunction;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
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
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.wiki.model.WikiPage;
import com.liferay.wiki.service.WikiPageLocalService;
import com.liferay.wiki.service.persistence.WikiNodePersistence;
import com.liferay.wiki.service.persistence.WikiPageFinder;
import com.liferay.wiki.service.persistence.WikiPagePersistence;
import com.liferay.wiki.service.persistence.WikiPageResourcePersistence;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Reference;

/**
 * Provides the base implementation for the wiki page local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.wiki.service.impl.WikiPageLocalServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.wiki.service.impl.WikiPageLocalServiceImpl
 * @generated
 */
public abstract class WikiPageLocalServiceBaseImpl
	extends BaseLocalServiceImpl
	implements AopService, IdentifiableOSGiService, WikiPageLocalService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>WikiPageLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.wiki.service.WikiPageLocalServiceUtil</code>.
	 */

	/**
	 * Adds the wiki page to the database. Also notifies the appropriate model listeners.
	 *
	 * @param wikiPage the wiki page
	 * @return the wiki page that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public WikiPage addWikiPage(WikiPage wikiPage) {
		wikiPage.setNew(true);

		return wikiPagePersistence.update(wikiPage);
	}

	/**
	 * Creates a new wiki page with the primary key. Does not add the wiki page to the database.
	 *
	 * @param pageId the primary key for the new wiki page
	 * @return the new wiki page
	 */
	@Override
	@Transactional(enabled = false)
	public WikiPage createWikiPage(long pageId) {
		return wikiPagePersistence.create(pageId);
	}

	/**
	 * Deletes the wiki page with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param pageId the primary key of the wiki page
	 * @return the wiki page that was removed
	 * @throws PortalException if a wiki page with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public WikiPage deleteWikiPage(long pageId) throws PortalException {
		return wikiPagePersistence.remove(pageId);
	}

	/**
	 * Deletes the wiki page from the database. Also notifies the appropriate model listeners.
	 *
	 * @param wikiPage the wiki page
	 * @return the wiki page that was removed
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public WikiPage deleteWikiPage(WikiPage wikiPage) {
		return wikiPagePersistence.remove(wikiPage);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(
			WikiPage.class, clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return wikiPagePersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.wiki.model.impl.WikiPageModelImpl</code>.
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

		return wikiPagePersistence.findWithDynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.wiki.model.impl.WikiPageModelImpl</code>.
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

		return wikiPagePersistence.findWithDynamicQuery(
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
		return wikiPagePersistence.countWithDynamicQuery(dynamicQuery);
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

		return wikiPagePersistence.countWithDynamicQuery(
			dynamicQuery, projection);
	}

	@Override
	public WikiPage fetchWikiPage(long pageId) {
		return wikiPagePersistence.fetchByPrimaryKey(pageId);
	}

	/**
	 * Returns the wiki page matching the UUID and group.
	 *
	 * @param uuid the wiki page's UUID
	 * @param groupId the primary key of the group
	 * @return the matching wiki page, or <code>null</code> if a matching wiki page could not be found
	 */
	@Override
	public WikiPage fetchWikiPageByUuidAndGroupId(String uuid, long groupId) {
		return wikiPagePersistence.fetchByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the wiki page with the primary key.
	 *
	 * @param pageId the primary key of the wiki page
	 * @return the wiki page
	 * @throws PortalException if a wiki page with the primary key could not be found
	 */
	@Override
	public WikiPage getWikiPage(long pageId) throws PortalException {
		return wikiPagePersistence.findByPrimaryKey(pageId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery =
			new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(wikiPageLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(WikiPage.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("pageId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(
			wikiPageLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(WikiPage.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName("pageId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {

		actionableDynamicQuery.setBaseLocalService(wikiPageLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(WikiPage.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("pageId");
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

				@Override
				protected Projection getCountProjection() {
					return ProjectionFactoryUtil.countDistinct(
						"resourcePrimKey");
				}

			};

		initActionableDynamicQuery(exportActionableDynamicQuery);

		exportActionableDynamicQuery.setAddCriteriaMethod(
			new ActionableDynamicQuery.AddCriteriaMethod() {

				@Override
				public void addCriteria(DynamicQuery dynamicQuery) {
					Criterion modifiedDateCriterion =
						portletDataContext.getDateRangeCriteria("modifiedDate");

					if (modifiedDateCriterion != null) {
						Conjunction conjunction =
							RestrictionsFactoryUtil.conjunction();

						conjunction.add(modifiedDateCriterion);

						Disjunction disjunction =
							RestrictionsFactoryUtil.disjunction();

						disjunction.add(
							RestrictionsFactoryUtil.gtProperty(
								"modifiedDate", "lastPublishDate"));

						Property lastPublishDateProperty =
							PropertyFactoryUtil.forName("lastPublishDate");

						disjunction.add(lastPublishDateProperty.isNull());

						conjunction.add(disjunction);

						modifiedDateCriterion = conjunction;
					}

					Criterion statusDateCriterion =
						portletDataContext.getDateRangeCriteria("statusDate");

					if ((modifiedDateCriterion != null) &&
						(statusDateCriterion != null)) {

						Disjunction disjunction =
							RestrictionsFactoryUtil.disjunction();

						disjunction.add(modifiedDateCriterion);
						disjunction.add(statusDateCriterion);

						dynamicQuery.add(disjunction);
					}

					Property workflowStatusProperty =
						PropertyFactoryUtil.forName("status");

					if (portletDataContext.isInitialPublication()) {
						dynamicQuery.add(
							workflowStatusProperty.ne(
								WorkflowConstants.STATUS_IN_TRASH));
					}
					else {
						StagedModelDataHandler<?> stagedModelDataHandler =
							StagedModelDataHandlerRegistryUtil.
								getStagedModelDataHandler(
									WikiPage.class.getName());

						dynamicQuery.add(
							workflowStatusProperty.in(
								stagedModelDataHandler.
									getExportableStatuses()));
					}
				}

			});

		exportActionableDynamicQuery.setCompanyId(
			portletDataContext.getCompanyId());

		exportActionableDynamicQuery.setGroupId(
			portletDataContext.getScopeGroupId());

		exportActionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod<WikiPage>() {

				@Override
				public void performAction(WikiPage wikiPage)
					throws PortalException {

					StagedModelDataHandlerUtil.exportStagedModel(
						portletDataContext, wikiPage);
				}

			});
		exportActionableDynamicQuery.setStagedModelType(
			new StagedModelType(
				PortalUtil.getClassNameId(WikiPage.class.getName())));

		return exportActionableDynamicQuery;
	}

	/**
	 * @throws PortalException
	 */
	public PersistedModel createPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return wikiPagePersistence.create(((Long)primaryKeyObj).longValue());
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {

		return wikiPageLocalService.deleteWikiPage((WikiPage)persistedModel);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return wikiPagePersistence.findByPrimaryKey(primaryKeyObj);
	}

	@Override
	public List<? extends PersistedModel> getPersistedModel(
			long resourcePrimKey)
		throws PortalException {

		return wikiPagePersistence.findByResourcePrimKey(resourcePrimKey);
	}

	/**
	 * Returns all the wiki pages matching the UUID and company.
	 *
	 * @param uuid the UUID of the wiki pages
	 * @param companyId the primary key of the company
	 * @return the matching wiki pages, or an empty list if no matches were found
	 */
	@Override
	public List<WikiPage> getWikiPagesByUuidAndCompanyId(
		String uuid, long companyId) {

		return wikiPagePersistence.findByUuid_C(uuid, companyId);
	}

	/**
	 * Returns a range of wiki pages matching the UUID and company.
	 *
	 * @param uuid the UUID of the wiki pages
	 * @param companyId the primary key of the company
	 * @param start the lower bound of the range of wiki pages
	 * @param end the upper bound of the range of wiki pages (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the range of matching wiki pages, or an empty list if no matches were found
	 */
	@Override
	public List<WikiPage> getWikiPagesByUuidAndCompanyId(
		String uuid, long companyId, int start, int end,
		OrderByComparator<WikiPage> orderByComparator) {

		return wikiPagePersistence.findByUuid_C(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns the wiki page matching the UUID and group.
	 *
	 * @param uuid the wiki page's UUID
	 * @param groupId the primary key of the group
	 * @return the matching wiki page
	 * @throws PortalException if a matching wiki page could not be found
	 */
	@Override
	public WikiPage getWikiPageByUuidAndGroupId(String uuid, long groupId)
		throws PortalException {

		return wikiPagePersistence.findByUUID_G(uuid, groupId);
	}

	/**
	 * Returns a range of all the wiki pages.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.wiki.model.impl.WikiPageModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of wiki pages
	 * @param end the upper bound of the range of wiki pages (not inclusive)
	 * @return the range of wiki pages
	 */
	@Override
	public List<WikiPage> getWikiPages(int start, int end) {
		return wikiPagePersistence.findAll(start, end);
	}

	/**
	 * Returns the number of wiki pages.
	 *
	 * @return the number of wiki pages
	 */
	@Override
	public int getWikiPagesCount() {
		return wikiPagePersistence.countAll();
	}

	/**
	 * Updates the wiki page in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param wikiPage the wiki page
	 * @return the wiki page that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public WikiPage updateWikiPage(WikiPage wikiPage) {
		return wikiPagePersistence.update(wikiPage);
	}

	@Override
	public Class<?>[] getAopInterfaces() {
		return new Class<?>[] {
			WikiPageLocalService.class, IdentifiableOSGiService.class,
			PersistedModelLocalService.class
		};
	}

	@Override
	public void setAopProxy(Object aopProxy) {
		wikiPageLocalService = (WikiPageLocalService)aopProxy;
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return WikiPageLocalService.class.getName();
	}

	protected Class<?> getModelClass() {
		return WikiPage.class;
	}

	protected String getModelClassName() {
		return WikiPage.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = wikiPagePersistence.getDataSource();

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

	protected WikiPageLocalService wikiPageLocalService;

	@Reference
	protected WikiPagePersistence wikiPagePersistence;

	@Reference
	protected WikiPageFinder wikiPageFinder;

	@Reference
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.ResourceLocalService
		resourceLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.SystemEventLocalService
		systemEventLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.UserLocalService
		userLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.WorkflowInstanceLinkLocalService
		workflowInstanceLinkLocalService;

	@Reference
	protected com.liferay.asset.kernel.service.AssetCategoryLocalService
		assetCategoryLocalService;

	@Reference
	protected com.liferay.asset.kernel.service.AssetEntryLocalService
		assetEntryLocalService;

	@Reference
	protected com.liferay.asset.kernel.service.AssetLinkLocalService
		assetLinkLocalService;

	@Reference
	protected com.liferay.asset.kernel.service.AssetTagLocalService
		assetTagLocalService;

	@Reference
	protected com.liferay.expando.kernel.service.ExpandoRowLocalService
		expandoRowLocalService;

	@Reference
	protected com.liferay.ratings.kernel.service.RatingsStatsLocalService
		ratingsStatsLocalService;

	@Reference
	protected WikiNodePersistence wikiNodePersistence;

	@Reference
	protected WikiPageResourcePersistence wikiPageResourcePersistence;

}