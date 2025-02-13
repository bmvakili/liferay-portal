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

import classNames from 'classnames';
import PropTypes from 'prop-types';
import React, {useEffect, useState} from 'react';

import {NotDraggableArea} from '../../../app/utils/useDragAndDrop';

export default function PageStructureSidebarSection({
	children,
	resizable = false,
}) {
	const [handlerElement, setHandlerElement] = useState(null);
	const [panelElement, setPanelElement] = useState(null);
	const [panelHeight, setPanelHeight] = useState();
	const [resizing, setResizing] = useState(false);

	useEffect(() => {
		if (!handlerElement || !panelElement) {
			return;
		}

		let initialHeight = 0;
		let initialY = 0;
		let maxHeight = 0;
		const minHeight = 200;

		const handleResizeStart = (event) => {
			initialHeight = panelElement.getBoundingClientRect().height;
			initialY = event.clientY;
			maxHeight = window.innerHeight * 0.8;

			document.body.addEventListener('mousemove', handleResize);
			document.body.addEventListener('mouseleave', handleResizeEnd);
			document.body.addEventListener('mouseup', handleResizeEnd);

			setResizing(true);
		};

		const handleResize = (event) => {
			const delta = event.clientY - initialY;

			setPanelHeight(
				Math.max(Math.min(maxHeight, initialHeight - delta), minHeight)
			);
		};

		const handleResizeEnd = () => {
			document.body.removeEventListener('mousemove', handleResize);
			document.body.removeEventListener('mouseleave', handleResizeEnd);
			document.body.removeEventListener('mouseup', handleResizeEnd);

			setResizing(false);
		};

		handlerElement.addEventListener('mousedown', handleResizeStart);

		return () => {
			handlerElement.removeEventListener('mousedown', handleResizeStart);
			handleResizeEnd();
		};
	}, [handlerElement, panelElement]);

	return (
		<>
			{resizable && (
				<NotDraggableArea>
					<div
						className={classNames(
							'page-editor__page-structure__section__resize-handler',
							{
								active: resizing,
							}
						)}
						ref={setHandlerElement}
					/>
				</NotDraggableArea>
			)}

			<div
				className={classNames('page-editor__page-structure__section', {
					resized: !!panelHeight,
				})}
				ref={setPanelElement}
				style={{height: panelHeight}}
			>
				{children}
			</div>
		</>
	);
}

PageStructureSidebarSection.propTypes = {
	resizable: PropTypes.bool,
};
