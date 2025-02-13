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

package com.liferay.source.formatter.checkstyle.util;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.source.formatter.checks.util.JavaSourceUtil;

import java.io.IOException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Peter Shin
 * @author Hugo Huijser
 */
public class JSPCheckstyleUtil {

	public static String getJavaContent(String absolutePath, String content)
		throws IOException {

		if (absolutePath.contains("src/main/resources/alloy_mvc/jsp/")) {
			return _getAlloyMVCJavaContent(content);
		}

		if (StringUtil.startsWith(StringUtil.trim(content), "<%\n")) {
			return null;
		}

		Matcher matcher = _javaSourceTag.matcher(content);

		if (matcher.find()) {
			return _getJavaContent(absolutePath, content);
		}

		return null;
	}

	private static String _getAlloyMVCJavaContent(String content) {
		if (!content.matches("(?s)<%--.*--%>(\\s*<%@[^\\n]*)*\\s*<%!\\s.*") ||
			(StringUtil.count(content, "<%!") != 1) ||
			!content.endsWith("\n%>")) {

			return null;
		}

		String javaContent = StringUtil.replace(
			content, new String[] {"<%--", "--%>", "<%@", "<%!"},
			new String[] {"//<%--", "//--%>", "//<%@", "//<%!"});

		return StringUtil.replaceLast(javaContent, "\n%>", "");
	}

	private static String _getJavaContent(String fileName, String content)
		throws IOException {

		StringBundler sb = new StringBundler();

		List<String> lines = CheckstyleUtil.getLines(content);

		boolean javaSource = false;

		sb.append("public class ");
		sb.append(
			StringUtil.removeChar(
				JavaSourceUtil.getClassName(fileName), CharPool.DASH));
		sb.append(" {\n");

		for (int i = 1; i < lines.size(); i++) {
			String line = lines.get(i);

			String trimmedLine = StringUtil.trimLeading(line);

			if (javaSource) {
				if (trimmedLine.matches("%>")) {
					sb.append("\t\t// PLACEHOLDER");

					javaSource = false;
				}
				else if (Validator.isNotNull(trimmedLine)) {
					sb.append("\t\t");
					sb.append(line);
				}

				sb.append("\n");

				continue;
			}

			if (i == 1) {
				sb.append("\tpublic void method() {");
			}
			else {
				sb.append("\t\t// PLACEHOLDER");
			}

			sb.append("\n");

			if (trimmedLine.matches("<%")) {
				javaSource = true;
			}
		}

		sb.append("\t}\n");
		sb.append("}\n");

		return sb.toString();
	}

	private static final Pattern _javaSourceTag = Pattern.compile("\n\t*<%\n");

}