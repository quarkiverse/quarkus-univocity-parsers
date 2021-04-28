package io.quarkiverse.univocityparsers.deployment;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.jboss.jandex.ClassInfo;
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

    private static final Logger log = Logger.getLogger(UnivocityParsersProcessor.class);

    private static final String FEATURE = "univocity";
    private static final String INDEX_DEPENDENCY_GROUPE_ID = "com.univocity";
    private static final String INDEX_DEPENDENCY_ARTIFACT_ID = "univocity-parsers";
    private static final DotName CONVERSION_INTERFACE = DotName.createSimple(Conversion.class.getName());

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    IndexDependencyBuildItem indexUnivocityParserJar() {
        return new IndexDependencyBuildItem(INDEX_DEPENDENCY_GROUPE_ID, INDEX_DEPENDENCY_ARTIFACT_ID);
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

        // Get all class implementation of Conversion
        Collection<ClassInfo> implementorClassInfos = combinedIndex.getIndex().getAllKnownImplementors(CONVERSION_INTERFACE);
        List<String> implementorClassList = new LinkedList<>();
        for (ClassInfo implClassInfo : implementorClassInfos) {
            String combinedIndexName = implClassInfo.name().toString();
            log.debugf("Conversion class implementation '[%s]' registered for reflection", combinedIndex);
            implementorClassList.add(combinedIndexName);
        }

        // Produce one build item for all Conversion interface implementors
        reflectiveClasses.produce(new ReflectiveClassBuildItem(true, true,
                implementorClassList.toArray(new String[implementorClassList.size()])));
    }
}
