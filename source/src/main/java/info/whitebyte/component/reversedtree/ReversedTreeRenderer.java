/*
 * Copyright 2013 WhiteByte (Nick Russler, Ahmet Yueksektepe).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.whitebyte.component.reversedtree;

import java.io.IOException;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;

import org.primefaces.component.api.UITree;
import org.primefaces.component.tree.Tree;
import org.primefaces.component.tree.TreeRenderer;
import org.primefaces.model.TreeNode;

@FacesRenderer(componentFamily = ReversedTree.COMPONENT_FAMILY, rendererType = ReversedTreeRenderer.RENDERER_TYPE)
public class ReversedTreeRenderer extends TreeRenderer {
	public static final String RENDERER_TYPE = "info.whitebyte.component.ReversedTreeRenderer";

	@Override
	public void encodeTreeNodeChildren(FacesContext context, Tree tree, TreeNode node, String clientId, String rowKey, boolean dynamic, boolean checkbox) throws IOException {
		List<TreeNode> children = node.getChildren();

		if (children.size() > 0) {
			for (int i = children.size() - 1; i >= 0; i--) {
				String childRowKey = rowKey == null ? String.valueOf(i) : rowKey + UITree.SEPARATOR + i;

				encodeTreeNode(context, tree, children.get(i), clientId, childRowKey, dynamic, checkbox);
			}
		}
	}
}