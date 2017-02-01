package org.eclipse.microprofile.config.tck;

import io.microprofile.config.inject.ConfigProperty;
import io.microprofile.config.inject.ConfigRegistry;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.inject.Provider;
import static org.eclipse.microprofile.config.tck.matchers.AdditionalMatchers.floatCloseTo;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Ondrej Mihalyi
 */
@RunWith(Arquillian.class)
public class CDIPlainInjectionTest {

    @Deployment
    public static Archive deployment() {
        return CDITestsFunctions.add_implementation_resources(ShrinkWrap.create(JavaArchive.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml"));
    }

    @Test
    public void can_inject_simple_values_when_defined() {
        ensure_all_property_values_are_defined();

        SimpleValuesBean bean = get_bean_of_type(SimpleValuesBean.class);

        assertThat(bean.stringProperty, is(equalTo("text")));
        assertThat(bean.boolProperty, is(true));
        assertThat(bean.intProperty, is(equalTo(5)));
        assertThat(bean.longProperty, is(equalTo(10L)));
        assertThat(bean.floatProperty, is(floatCloseTo(10.5f, 0.1f)));
        assertThat(bean.doubleProperty, is(closeTo(11.5, 0.1)));
        assertThat(bean.dateProperty, is(equalTo(CDITestsFunctions.toDate("2017-01-30"))));
        assertThat(bean.localDateProperty, is(CDITestsFunctions.toDate("2016-01-30")));
        assertThat(bean.dateTimeProperty, is(CDITestsFunctions.toDate("2015-01-30T10:00")));
    }

    @Test
    public void can_inject_optional_values() {
        ensure_property_undefined("non-existent.string.property");
        ensure_property_defined("my.string.property", "text");

        OptionalValuesBean bean = get_bean_of_type(OptionalValuesBean.class);

        assertThat(bean.noStringProperty.isPresent(), is(false));
        assertThat(bean.stringProperty.isPresent(), is(true));
        assertThat(bean.stringProperty.get(), is(equalTo("text")));
    }
        
    @Test
    public void can_inject_dynamic_values_via_CDI_provider() {
        clear_all_property_values();

        DynamicValuesBean bean = get_bean_of_type(DynamicValuesBean.class);

        assertThat(bean.getIntProperty(), is(nullValue()));

        ensure_all_property_values_are_defined();

        assertThat(bean.getIntProperty(), is(equalTo(5)));
    }

    @Test
    public void can_inject_dynamic_values_via_custom_interface() {
        clear_all_property_values();

        CustomConfigBean bean = get_bean_of_type(CustomConfigBean.class);

        assertThat(bean.getIntProperty(), is(nullValue()));
        assertThat(bean.getStringProperty(), is(nullValue()));

        ensure_all_property_values_are_defined();

        assertThat(bean.getIntProperty(), is(equalTo(5)));
        assertThat(bean.getStringProperty(), is(equalTo("text")));
    }

    private void ensure_all_property_values_are_defined() {
        ensure_property_defined("my.string.property", "text");
        ensure_property_defined("my.boolean.property", "true");
        ensure_property_defined("my.int.property", "5");
        ensure_property_defined("my.long.property", "10");
        ensure_property_defined("my.float.property", "10.5");
        ensure_property_defined("my.double.property", "11.5");
        ensure_property_defined("my.date.property", "2017-01-30");
        ensure_property_defined("my.localdate.property", "2016-01-30");
        ensure_property_defined("my.datetime.property", "2015-01-30T10:00");
    }

    private void ensure_property_defined(String key, String value) {
        // setting configuration via system properties
        System.setProperty(key, value);
    }

    private void ensure_property_undefined(String key) {
        // clearing configuration in system properties if previously set
        System.getProperties().remove(key);
    }
    
    private static <T> T get_bean_of_type(Class<T> beanClass) {
        return CDI.current().select(beanClass).get();
    }

    private void clear_all_property_values() {
        ensure_property_undefined("my.string.property");
        ensure_property_undefined("my.boolean.property");
        ensure_property_undefined("my.int.property");
        ensure_property_undefined("my.long.property");
        ensure_property_undefined("my.float.property");
        ensure_property_undefined("my.double.property");
        ensure_property_undefined("my.date.property");
        ensure_property_undefined("my.localdate.property");
        ensure_property_undefined("my.datetime.property");
    }

    @Dependent
    public static class SimpleValuesBean {

        @Inject
        @ConfigProperty("my.string.property")
        String stringProperty;

        @Inject
        @ConfigProperty("my.boolean.property")
        Boolean boolProperty;

        @Inject
        @ConfigProperty("my.int.property")
        Integer intProperty;

        @Inject
        @ConfigProperty("my.long.property")
        Long longProperty;

        @Inject
        @ConfigProperty("my.float.property")
        Float floatProperty;

        @Inject
        @ConfigProperty("my.double.property")
        Double doubleProperty;

        @Inject
        @ConfigProperty("my.date.property")
        Date dateProperty;

        @Inject
        @ConfigProperty("my.localdate.property")
        LocalDate localDateProperty;

        @Inject
        @ConfigProperty("my.datetime.property")
        LocalDateTime dateTimeProperty;

    }

    @Dependent
    public static class OptionalValuesBean {

        @Inject
        @ConfigProperty("my.string.property")
        Optional<String> stringProperty;

        @Inject
        @ConfigProperty("non-existent.string.property")
        Optional<String> noStringProperty;

    }
    
    @Dependent
    public static class DynamicValuesBean {

        @Inject
        @ConfigProperty("my.int.property")
        Provider<Integer> intPropertyProvider;

        public Integer getIntProperty() {
            try {
                return intPropertyProvider.get();
            } 
            catch (Exception e) {
                return null;
            }
        }

    }

    @Dependent
    public static class CustomConfigBean {

        @Inject
        CustomConfig config;

        public Integer getIntProperty() {
            return config.getIntProperty();
        }

        public String getStringProperty() {
            return config.getStringProperty().orElse(null);
        }

    }

    @ConfigRegistry
    public interface CustomConfig {

        @ConfigProperty("my.integer.property")
        Integer getIntProperty();

        @ConfigProperty("my.string.property")
        Optional<String> getStringProperty();

    }

}
