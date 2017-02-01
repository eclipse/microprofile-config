package org.eclipse.microprofile.config.tck;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Iterator;
import java.util.ServiceLoader;
import org.jboss.shrinkwrap.api.Archive;

/**
 *
 * @author Ondrej Mihalyi
 */
public final class CDITestsFunctions {
    
    private CDITestsFunctions() {
        // utility class
    }

    public static Archive add_implementation_resources(Archive deployment) {
        Iterator<MicroProfileConfigTestConnector> itConnectors = ServiceLoader.load(MicroProfileConfigTestConnector.class).iterator();
        if (itConnectors.hasNext()) {
            return itConnectors.next().modifyDeployment(deployment);
        } 
        else {
            return deployment;
        }
    }

    public static Date toDate(String isoDateTime) {
        return Date.from(ZonedDateTime.parse(isoDateTime).toInstant());
    }

    public static LocalDate toLocalDate(String isoDateTime) {
        return LocalDate.parse(isoDateTime);
    }

    public static LocalDateTime toLocalDateTime(String isoDateTime) {
        return LocalDateTime.parse(isoDateTime);
    }

}
