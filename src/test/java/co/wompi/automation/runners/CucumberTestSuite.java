package co.wompi.automation.runners;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * Punto de entrada de la ejecución: corre todos los .feature de src/test/resources/features
 * con el motor de Cucumber sobre JUnit Platform. Glue y plugins en junit-platform.properties.
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
public class CucumberTestSuite {
}
