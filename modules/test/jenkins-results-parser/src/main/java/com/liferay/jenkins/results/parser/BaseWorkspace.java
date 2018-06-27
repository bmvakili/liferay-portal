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

package com.liferay.jenkins.results.parser;

import java.io.File;
import java.io.IOException;

/**
 * @author Michael Hashimoto
 */
public abstract class BaseWorkspace {

	public GitWorkingDirectory getGitWorkingDirectory() {
		return _gitWorkingDirectory;
	}

	public File getRepositoryDir() {
		return _repositoryDir;
	}

	public String getUpstreamBranchName() {
		return _upstreamBranchName;
	}

	public String getUpstreamRepositoryName() {
		return _upstreamRepositoryName;
	}

	public void setupWorkspace() {
		System.out.println("##");
		System.out.println("## " + _repositoryDir);
		System.out.println("##");

		_gitWorkingDirectory.reset("--hard HEAD");

		_gitWorkingDirectory.clean();
	}

	protected BaseWorkspace(
		File repositoryDir, String upstreamBranchName,
		String upstreamRepositoryName) {

		try {
			_repositoryDir = new File(repositoryDir.getCanonicalPath());
		}
		catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}

		_upstreamBranchName = upstreamBranchName;
		_upstreamRepositoryName = upstreamRepositoryName;

		_gitWorkingDirectory =
			GitWorkingDirectoryFactory.newGitWorkingDirectory(
				upstreamBranchName, repositoryDir, upstreamRepositoryName);
	}

	private final GitWorkingDirectory _gitWorkingDirectory;
	private final File _repositoryDir;
	private final String _upstreamBranchName;
	private final String _upstreamRepositoryName;

}