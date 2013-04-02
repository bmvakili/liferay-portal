/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.security.membershippolicy.samples;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.UserGroupRole;
import com.liferay.portal.security.membershippolicy.BaseOrganizationMembershipPolicy;
import com.liferay.portal.security.membershippolicy.BaseOrganizationMembershipPolicyTestCase;
import com.liferay.portal.security.membershippolicy.MembershipPolicyException;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetTag;

import java.io.Serializable;

import java.util.List;
import java.util.Map;

import org.junit.Assert;

/**
 * @author Roberto Díaz
 */
public class TestOrganizationMembershipPolicy
	extends BaseOrganizationMembershipPolicy {

	public void checkMembership(
			long[] userIds, long[] addOrganizationIds,
			long[] removeOrganizationIds)
		throws PortalException {

		for (long forbiddenOrganizationId :
				BaseOrganizationMembershipPolicyTestCase.
					getForbiddenOrganizationIds()) {

			if (forbiddenOrganizationId == 0) {
				continue;
			}

			if (ArrayUtil.contains(
					addOrganizationIds, forbiddenOrganizationId)) {

				throw new MembershipPolicyException(
					MembershipPolicyException.
						ORGANIZATION_MEMBERSHIP_NOT_ALLOWED);
			}
		}

		for (long requiredOrganizationId :
				BaseOrganizationMembershipPolicyTestCase.
					getRequiredOrganizationIds()) {

			if (requiredOrganizationId == 0) {
				continue;
			}

			if (ArrayUtil.contains(
					removeOrganizationIds, requiredOrganizationId)) {

				throw new MembershipPolicyException(
					MembershipPolicyException.ORGANIZATION_MEMBERSHIP_REQUIRED);
			}
		}
	}

	public void checkRoles(
			List<UserGroupRole> addUserGroupRoles,
			List<UserGroupRole> removeUserGroupRoles)
		throws PortalException {

		long[] addUserGroupRoleIds = new long[2];
		long[] removeUserGroupRoleIds = new long[2];

		if ((addUserGroupRoles != null) && !addUserGroupRoles.isEmpty()) {
			for (UserGroupRole addUserGroupRole : addUserGroupRoles) {
				addUserGroupRoleIds[0] = addUserGroupRole.getRoleId();
			}
		}

		if ((removeUserGroupRoles != null) && !removeUserGroupRoles.isEmpty()) {
			for (UserGroupRole removeUserGroupRole : removeUserGroupRoles) {
				removeUserGroupRoleIds[0] = removeUserGroupRole.getRoleId();
			}
		}

		for (long forbiddenGroupRoleId :
				BaseOrganizationMembershipPolicyTestCase.
					getForbiddenRoleIds()) {

			if (forbiddenGroupRoleId == 0) {
				continue;
			}

			if (ArrayUtil.contains(addUserGroupRoleIds, forbiddenGroupRoleId)) {
				throw new MembershipPolicyException(
					MembershipPolicyException.ROLE_MEMBERSHIP_NOT_ALLOWED);
			}
		}

		for (long requiredGroupRoleId :
				BaseOrganizationMembershipPolicyTestCase.getRequiredRoleIds()) {

			if (requiredGroupRoleId == 0) {
				continue;
			}

			if (ArrayUtil.contains(
					removeUserGroupRoleIds, requiredGroupRoleId)) {

				throw new MembershipPolicyException(
					MembershipPolicyException.ROLE_MEMBERSHIP_REQUIRED);
			}
		}
	}

	public void propagateMembership(
			long[] userIds, long[] addOrganizationIds,
			long[] removeOrganizationIds) {

		BaseOrganizationMembershipPolicyTestCase.setPropagateMembership(true);
	}

	public void propagateRoles(
			List<UserGroupRole> adduserGroupRoles,
			List<UserGroupRole> removeUserGroupRoles) {

		BaseOrganizationMembershipPolicyTestCase.setPropagateRoles(true);
	}

	@Override
	public void verifyPolicy() {
		BaseOrganizationMembershipPolicyTestCase.setVerify(true);
	}

	@Override
	public void verifyPolicy(Organization organization) {
		verifyPolicy();
	}

	public void verifyPolicy(
			Organization organization, Organization oldOrganization,
			List<AssetCategory> oldAssetCategories, List<AssetTag> oldAssetTags,
			Map<String, Serializable> oldExpandoAttributes) {

			Assert.assertNotNull(organization);
			Assert.assertNotNull(oldOrganization);
			Assert.assertNotNull(oldAssetCategories);
			Assert.assertNotNull(oldAssetTags);
			Assert.assertNotNull(oldExpandoAttributes);

			verifyPolicy(organization);
	}

	public void verifyPolicy(Role role) {
		verifyPolicy();
	}

	public void verifyPolicy(
			Role role, Role oldRole,
			Map<String, Serializable> oldExpandoAttributes) {

		Assert.assertNotNull(role);
		Assert.assertNotNull(oldRole);
		Assert.assertNotNull(oldExpandoAttributes);

		verifyPolicy(role);
	}

}