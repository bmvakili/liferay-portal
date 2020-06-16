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

package com.liferay.dynamic.data.mapping.internal.upgrade.v3_7_3;

import com.liferay.dynamic.data.mapping.constants.DDMFormInstanceReportConstants;
import com.liferay.dynamic.data.mapping.internal.report.CheckboxMultipleDDMFormFieldTypeReportProcessor;
import com.liferay.dynamic.data.mapping.internal.report.NumericDDMFormFieldTypeReportProcessor;
import com.liferay.dynamic.data.mapping.internal.report.RadioDDMFormFieldTypeReportProcessor;
import com.liferay.dynamic.data.mapping.internal.report.TextDDMFormFieldTypeReportProcessor;
import com.liferay.dynamic.data.mapping.io.DDMFormDeserializer;
import com.liferay.dynamic.data.mapping.io.DDMFormDeserializerDeserializeRequest;
import com.liferay.dynamic.data.mapping.io.DDMFormDeserializerDeserializeResponse;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.report.DDMFormFieldTypeReportProcessor;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.jdbc.AutoBatchPreparedStatementUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.upgrade.UpgradeException;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Marcos Martins
 */
public class UpgradeDDMFormInstanceReport extends UpgradeProcess {

	public UpgradeDDMFormInstanceReport(
		DDMFormDeserializer ddmFormDeserializer, JSONFactory jsonFactory) {

		_ddmFormDeserializer = ddmFormDeserializer;
		_jsonFactory = jsonFactory;
	}

	@Override
	protected void doUpgrade() throws Exception {
		runSQL("delete from DDMFormInstanceReport;");

		StringBundler sb1 = new StringBundler(12);

		sb1.append("select DDMContent.data_, ");
		sb1.append("DDMFormInstanceRecord.formInstanceRecordId from ");
		sb1.append("DDMContent inner join DDMFormInstanceRecordVersion on ");
		sb1.append("DDMContent.contentId = ");
		sb1.append("DDMFormInstanceRecordVersion.storageId inner join ");
		sb1.append("DDMFormInstanceRecord on ");
		sb1.append("DDMFormInstanceRecordVersion.formInstanceRecordId = ");
		sb1.append("DDMFormInstanceRecord.formInstanceRecordId where ");
		sb1.append("DDMFormInstanceRecord.version = ");
		sb1.append("DDMFormInstanceRecordVersion.version and ");
		sb1.append("DDMFormInstanceRecordVersion.status = ? and ");
		sb1.append("DDMFormInstanceRecord.formInstanceId = ?");

		StringBundler sb2 = new StringBundler(3);

		sb2.append("insert into DDMFormInstanceReport (formInstanceReportId, ");
		sb2.append("groupId, companyId, createDate, modifiedDate, ");
		sb2.append("formInstanceId, data_) values (?, ?, ?, ?, ?, ?, ?)");

		try (PreparedStatement ps1 = connection.prepareStatement(
				"select formInstanceId, companyid, createDate, groupId, " +
					"structureId from DDMFormInstance")) {

			ResultSet rs1 = ps1.executeQuery();

			while (rs1.next()) {
				long formInstanceId = rs1.getLong("formInstanceId");

				long structureId = rs1.getLong("structureId");

				DDMForm ddmForm = _getDDMForm(structureId);

				JSONObject dataJSONObject = _jsonFactory.createJSONObject();

				try (PreparedStatement ps2 = connection.prepareStatement(
						sb1.toString())) {

					ps2.setInt(1, WorkflowConstants.STATUS_APPROVED);
					ps2.setLong(2, formInstanceId);

					ResultSet rs2 = ps2.executeQuery();

					while (rs2.next()) {
						String data = rs2.getString("data_");
						long formInstanceRecordId = rs2.getLong(
							"formInstanceRecordId");

						DDMFormValues ddmFormValues = _getDDMFormValues(
							data, ddmForm);

						dataJSONObject = _processDDMFormValues(
							dataJSONObject, ddmFormValues,
							formInstanceRecordId);

						dataJSONObject.put(
							"totalItems",
							dataJSONObject.getInt("totalItems") + 1);
					}
				}

				long groupId = rs1.getLong("groupId");

				long companyId = rs1.getLong("companyid");

				Timestamp createDate = rs1.getTimestamp("createDate");

				try (PreparedStatement ps3 =
						AutoBatchPreparedStatementUtil.concurrentAutoBatch(
							connection, sb2.toString())) {

					ps3.setLong(1, increment());
					ps3.setLong(2, groupId);
					ps3.setLong(3, companyId);
					ps3.setTimestamp(4, createDate);
					ps3.setTimestamp(5, createDate);
					ps3.setLong(6, formInstanceId);
					ps3.setString(7, dataJSONObject.toString());

					ps3.execute();
				}
			}
		}
	}

	private DDMForm _getDDMForm(long structureId) throws Exception {
		try (PreparedStatement ps = connection.prepareStatement(
				"select parentStructureId, definition, storageType from " +
					"DDMStructure where structureId = ?")) {

			ps.setLong(1, structureId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					String definition = rs.getString("definition");

					DDMFormDeserializerDeserializeRequest.Builder builder =
						DDMFormDeserializerDeserializeRequest.Builder.
							newBuilder(definition);

					DDMFormDeserializerDeserializeResponse
						ddmFormDeserializerDeserializeResponse =
							_ddmFormDeserializer.deserialize(builder.build());

					return ddmFormDeserializerDeserializeResponse.getDDMForm();
				}
			}

			throw new UpgradeException(
				"Unable to find dynamic data mapping structure with ID " +
					structureId);
		}
	}

	private DDMFormValues _getDDMFormValues(String data, DDMForm ddmForm)
		throws Exception {

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		JSONObject dataJSONObject = _jsonFactory.createJSONObject(data);

		JSONArray fieldValuesJSONArray = dataJSONObject.getJSONArray(
			"fieldValues");

		Iterator<JSONObject> iterator = fieldValuesJSONArray.iterator();

		while (iterator.hasNext()) {
			JSONObject jsonObject = iterator.next();

			String name = jsonObject.getString("name");

			DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

			ddmFormFieldValue.setDDMFormValues(ddmFormValues);
			ddmFormFieldValue.setInstanceId(jsonObject.getString("instanceId"));
			ddmFormFieldValue.setName(name);

			DDMFormField ddmFormField = ddmFormFieldValue.getDDMFormField();

			Value value = null;

			if (ddmFormField.isLocalizable()) {
				value = new LocalizedValue();

				JSONObject valueJSONObject = jsonObject.getJSONObject("value");

				for (String key : valueJSONObject.keySet()) {
					value.addString(
						LocaleUtil.fromLanguageId(key),
						valueJSONObject.getString(key));
				}
			}
			else {
				value = new UnlocalizedValue(
					Optional.of(
						jsonObject.get("value")
					).orElse(
						StringPool.BLANK
					).toString());
			}

			ddmFormFieldValue.setValue(value);

			ddmFormFieldValues.add(ddmFormFieldValue);
		}

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		return ddmFormValues;
	}

	private JSONObject _processDDMFormValues(
			JSONObject dataJSONObject, DDMFormValues ddmFormValues,
			long formInstanceRecordId)
		throws Exception, JSONException {

		for (DDMFormFieldValue ddmFormFieldValue :
				ddmFormValues.getDDMFormFieldValues()) {

			DDMFormFieldTypeReportProcessor ddmFormFieldTypeReportProcessor =
				_ddmFormFieldTypeReportProcessorTracker.
					getDDMFormFieldTypeReportProcessor(
						ddmFormFieldValue.getDDMFormField(),
						ddmFormFieldValue.getType());

			if (ddmFormFieldTypeReportProcessor != null) {
				String fieldName = ddmFormFieldValue.getName();

				JSONObject fieldJSONObject = dataJSONObject.getJSONObject(
					fieldName);

				if (fieldJSONObject == null) {
					fieldJSONObject = JSONUtil.put(
						"type", ddmFormFieldValue.getType()
					).put(
						"values", _jsonFactory.createJSONObject()
					);
				}

				JSONObject processedFieldJSONObject =
					ddmFormFieldTypeReportProcessor.process(
						ddmFormFieldValue,
						_jsonFactory.createJSONObject(
							fieldJSONObject.toJSONString()),
						formInstanceRecordId,
						DDMFormInstanceReportConstants.
							EVENT_ADD_RECORD_VERSION);

				dataJSONObject.put(fieldName, processedFieldJSONObject);
			}
		}

		return dataJSONObject;
	}

	private final DDMFormDeserializer _ddmFormDeserializer;
	private DDMFormFieldTypeReportProcessorTracker
		_ddmFormFieldTypeReportProcessorTracker =
			new DDMFormFieldTypeReportProcessorTracker();
	private final JSONFactory _jsonFactory;

	private class DDMFormFieldTypeReportProcessorTracker {

		public DDMFormFieldTypeReportProcessor
			getDDMFormFieldTypeReportProcessor(
				DDMFormField ddmFormField, String type) {

			if (StringUtil.equals(type, "checkbox_multiple") ||
				StringUtil.equals(type, "select")) {

				return new CheckboxMultipleDDMFormFieldTypeReportProcessor();
			}
			else if (StringUtil.equals(type, "color") ||
					 StringUtil.equals(type, "date") ||
					 StringUtil.equals(type, "text")) {

				return new TextDDMFormFieldTypeReportProcessor();
			}
			else if (StringUtil.equals(type, "grid")) {
				return new UpgradeGridDDMFormFieldTypeReportProcessor(
					ddmFormField);
			}
			else if (StringUtil.equals(type, "numeric")) {
				return new NumericDDMFormFieldTypeReportProcessor();
			}
			else if (StringUtil.equals(type, "radio")) {
				return new RadioDDMFormFieldTypeReportProcessor();
			}

			return null;
		}

	}

	private class UpgradeGridDDMFormFieldTypeReportProcessor
		implements DDMFormFieldTypeReportProcessor {

		public UpgradeGridDDMFormFieldTypeReportProcessor(
			DDMFormField ddmFormField) {

			_ddmFormField = ddmFormField;
		}

		@Override
		public JSONObject process(
				DDMFormFieldValue ddmFormFieldValue, JSONObject fieldJSONObject,
				long formInstanceRecordId, String ddmFormInstanceReportEvent)
			throws Exception {

			JSONObject valuesJSONObject = fieldJSONObject.getJSONObject(
				"values");

			Value value = ddmFormFieldValue.getValue();

			JSONObject valueJSONObject = JSONFactoryUtil.createJSONObject(
				value.getString(value.getDefaultLocale()));

			Iterator<String> iterator = valueJSONObject.keys();

			while (iterator.hasNext()) {
				String rowName = iterator.next();

				JSONObject rowJSONObject = valuesJSONObject.getJSONObject(
					rowName);

				if (rowJSONObject == null) {
					rowJSONObject = JSONFactoryUtil.createJSONObject();
				}

				String columnName = valueJSONObject.getString(rowName);

				rowJSONObject.put(
					columnName, rowJSONObject.getInt(columnName) + 1);

				valuesJSONObject.put(rowName, rowJSONObject);
			}

			int totalEntries = fieldJSONObject.getInt("totalEntries");

			if (valueJSONObject.length() != 0) {
				totalEntries++;
			}

			fieldJSONObject.put(
				"structure",
				JSONUtil.put(
					"columns", _getOptionValuesJSONArray("columns")
				).put(
					"rows", _getOptionValuesJSONArray("rows")
				)
			).put(
				"totalEntries", totalEntries
			);

			return fieldJSONObject;
		}

		private JSONArray _getOptionValuesJSONArray(String propertyName) {
			JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

			DDMFormFieldOptions ddmFormFieldOptions =
				(DDMFormFieldOptions)_ddmFormField.getProperty(propertyName);

			if (ddmFormFieldOptions != null) {
				Set<String> optionsValues =
					ddmFormFieldOptions.getOptionsValues();

				optionsValues.forEach(
					optionValue -> jsonArray.put(optionValue));
			}

			return jsonArray;
		}

		private final DDMFormField _ddmFormField;

	}

}