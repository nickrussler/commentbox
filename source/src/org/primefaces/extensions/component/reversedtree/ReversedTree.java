package org.primefaces.extensions.component.reversedtree;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;

import org.primefaces.component.tree.Tree;
/**
 * <code>ReversedTree</code> component.
 *
 * @author  Nick Russler / last modified by $Author$
 * @version $Revision$
 * @since   0.7
 */
@ResourceDependencies({
                          @ResourceDependency(library = "primefaces", name = "primefaces.css"),
                          @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
                          @ResourceDependency(library = "primefaces", name = "primefaces.js")
                      })
@FacesComponent(ReversedTree.COMPONENT_TYPE)
public class ReversedTree extends Tree {    
	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.ReversedTree";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getRendererType() {
        return ReversedTreeRenderer.RENDERER_TYPE;
    }
}