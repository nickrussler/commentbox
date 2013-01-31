package info.whitebyte.component.reversedtree;

import javax.faces.component.FacesComponent;

import org.primefaces.component.tree.Tree;

@FacesComponent(ReversedTree.COMPONENT_TYPE)
public class ReversedTree extends Tree {    
	public static final String COMPONENT_TYPE = "info.whitebyte.component.ReversedTree";
	public static final String COMPONENT_FAMILY = "info.whitebyte.component";
    
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getRendererType() {
        return ReversedTreeRenderer.RENDERER_TYPE;
    }
}