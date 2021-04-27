package io.quarkiverse.univocityparsers.deployment;

import org.jboss.jandex.DotName;
import org.jboss.logging.Logger;

import com.univocity.parsers.conversions.Conversion;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.IndexDependencyBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;

class UnivocityParsersProcessor {

    private static final Logger log = Logger.getLogger("io.quarkiverse.univocityparsers");

    private static final String FEATURE = "univocity-parsers";
    private static final DotName CONVERSION_INTERFACE = DotName.createSimple(Conversion.class.getName());

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    IndexDependencyBuildItem indexUnivocityParserJar() {
        return new IndexDependencyBuildItem("com.univocity", FEATURE);
    }

    @BuildStep
    /**
     * Allowing to register all {@link Conversion#execute(Object)} method
     * which {@link com.univocity.parsers.common.processor.core.BeanConversionProcessor#getConversionMethod(Conversion, String)}
     * call dynamically
     */
    void registerForReflection(CombinedIndexBuildItem combinedIndex,
            BuildProducer<ReflectiveClassBuildItem> reflectiveClasses) {
        // Produce one build item for Conversion interface
        reflectiveClasses.produce(new ReflectiveClassBuildItem(true, true, CONVERSION_INTERFACE.toString()));

        // Produce one build item for all Conversion interface implementors
        reflectiveClasses.produce(new ReflectiveClassBuildItem(true, true,
                combinedIndex.getIndex().getAllKnownImplementors(CONVERSION_INTERFACE).stream()
                        .map(ci -> {
                            String combinedIndexName = ci.name().toString();
                            log.debug("Conversion class implementation '" + combinedIndexName + "' registered for reflection");
                            return combinedIndexName;
                        }).toArray(String[]::new)));
    }
}
