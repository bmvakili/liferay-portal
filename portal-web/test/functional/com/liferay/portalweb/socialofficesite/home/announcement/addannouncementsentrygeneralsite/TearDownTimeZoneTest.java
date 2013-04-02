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

package com.liferay.portalweb.socialofficesite.home.announcement.addannouncementsentrygeneralsite;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * @author Brian Wing Shun Chan
 */
public class TearDownTimeZoneTest extends BaseTestCase {
	public void testTearDownTimeZone() throws Exception {
		int label = 1;

		while (label >= 1) {
			switch (label) {
			case 1:
				selenium.selectWindow("null");
				selenium.selectFrame("relative=top");
				selenium.open("/web/guest/home/");

				boolean socialOfficeSignOutPresent = selenium.isElementPresent(
						"//li[@id='_145_userMenu']");

				if (!socialOfficeSignOutPresent) {
					label = 2;

					continue;
				}

				selenium.clickAt("//div[@id='dockbar']",
					RuntimeVariables.replace("Dockbar"));
				selenium.waitForElementPresent(
					"//script[contains(@src,'/aui/aui-editable/aui-editable-min.js')]");
				assertTrue(selenium.isVisible("//li[@id='_145_userMenu']"));
				selenium.mouseOver("//li[@id='_145_userMenu']");

			case 2:
				selenium.waitForVisible("link=My Account");
				selenium.clickAt("link=My Account",
					RuntimeVariables.replace("My Account"));
				selenium.waitForVisible(
					"//iframe[contains(@class,'aui-dialog-iframe-node')]");
				selenium.selectFrame(
					"//iframe[contains(@class,'aui-dialog-iframe-node')]");
				selenium.waitForVisible("//a[@id='_2_displaySettingsLink']");
				selenium.clickAt("//a[@id='_2_displaySettingsLink']",
					RuntimeVariables.replace("Display Settings"));
				Thread.sleep(1000);
				selenium.waitForElementPresent(
					"//li[@class='selected']/a[@id='_2_displaySettingsLink']");
				selenium.waitForVisible("//select[@name='_2_timeZoneId']");
				selenium.select("//select[@name='_2_timeZoneId']",
					RuntimeVariables.replace(
						"(UTC ) Coordinated Universal Time"));
				assertEquals("(UTC ) Coordinated Universal Time",
					selenium.getSelectedLabel("//select[@name='_2_timeZoneId']"));
				selenium.clickAt("//input[@value='Save']",
					RuntimeVariables.replace("Save"));
				selenium.waitForPageToLoad("30000");
				assertEquals(RuntimeVariables.replace(
						"Your request completed successfully."),
					selenium.getText("//div[@class='portlet-msg-success']"));
				assertEquals("(UTC ) Coordinated Universal Time",
					selenium.getSelectedLabel("//select[@name='_2_timeZoneId']"));
				selenium.selectFrame("relative=top");

			case 100:
				label = -1;
			}
		}
	}
}