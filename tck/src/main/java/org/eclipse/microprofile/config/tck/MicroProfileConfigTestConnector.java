package org.eclipse.microprofile.config.tck;

import org.jboss.shrinkwrap.api.Archive;

/**
 *
 * @author Ondrej Mihalyi
 */
public interface MicroProfileConfigTestConnector {
    Archive modifyDeployment(Archive deployment); 
}
