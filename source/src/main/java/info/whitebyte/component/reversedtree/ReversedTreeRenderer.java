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