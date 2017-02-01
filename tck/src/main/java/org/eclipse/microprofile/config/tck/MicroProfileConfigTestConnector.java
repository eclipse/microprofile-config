package org.eclipse.microprofile.config.tck;

import org.jboss.shrinkwrap.api.Archive;

/**
 * An implementation of this interface can be provided by the config implementation using {@link java.util.ServiceLoader} 
 * in order to provide additional configuration for the tests (e.g. dependencies for a tested ShrinkWrap archive)
 * @author Ondrej Mihalyi
 */
public interface MicroProfileConfigTestConnector {
    Archive modifyDeployment(Archive deployment); 
}
