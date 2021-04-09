package io.quarkiverse.univocityparsers.univocity.parsers.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class UnivocityParsersProcessor {

    private static final String FEATURE = "univocity-parsers";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
