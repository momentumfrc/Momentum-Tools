package com.momentum4999.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.momentum4999.utils.PIDTuner.PIDGraphValues;
import com.momentum4999.utils.PIDTuner.PIDProperty;
import com.momentum4999.utils.PIDTuner.PIDTunerSettings;

public class PIDTunerBuilder {
    private final String controllerName;
    private PIDTunerSettings settings = new PIDTunerSettings();
    private Optional<PIDGraphValues> graphValues = Optional.empty();
    private final List<PIDProperty> properties = new ArrayList<>();

    public PIDTunerBuilder(String controllerName) {
        this.controllerName = controllerName;
    }

    private PIDTunerBuilder(PIDTunerBuilder toCopy) {
        this.controllerName = toCopy.controllerName;
        this.settings = toCopy.settings;
        this.properties.addAll(toCopy.properties);
        this.graphValues = toCopy.graphValues;
    }

    public class GraphValuesBuilder {
        private Optional<Supplier<Double>> setpointGetter = Optional.empty();
        private Optional<Supplier<Double>> lastMeasurementGetter = Optional.empty();
        private Optional<Supplier<Double>> lastOutputGetter = Optional.empty();

        protected GraphValuesBuilder(){}

        private GraphValuesBuilder(GraphValuesBuilder toCopy) {
            this.setpointGetter = toCopy.setpointGetter;
            this.lastMeasurementGetter = toCopy.lastMeasurementGetter;
            this.lastOutputGetter = toCopy.lastOutputGetter;
        }

        public GraphValuesBuilder withSetpointFrom(Supplier<Double> getter) {
            var copy = new GraphValuesBuilder(this);
            copy.setpointGetter = Optional.of(getter);
            return copy;
        }

        public GraphValuesBuilder withLastMeasurementFrom(Supplier<Double> getter) {
            var copy = new GraphValuesBuilder(this);
            copy.lastMeasurementGetter = Optional.of(getter);
            return copy;
        }

        public GraphValuesBuilder withLastOutputFrom(Supplier<Double> getter) {
            var copy = new GraphValuesBuilder(this);
            copy.lastOutputGetter = Optional.of(getter);
            return copy;
        }

        public PIDTunerBuilder finishGraphValues() {
            if(setpointGetter.isEmpty() || lastMeasurementGetter.isEmpty() || lastOutputGetter.isEmpty()) {
                throw new IllegalStateException("Not all getters were set");
            }
            PIDGraphValues values = new PIDGraphValues() {
                @Override
                public double getSetpoint() {
                    return setpointGetter.get().get();
                }

                @Override
                public double getLastMeasurement() {
                    return lastMeasurementGetter.get().get();
                }

                @Override
                public double getLastOutput() {
                    return lastOutputGetter.get().get();
                }

            };

            var copy = new PIDTunerBuilder(PIDTunerBuilder.this);
            copy.graphValues = Optional.of(values);
            return copy;
        }
    }

    public GraphValuesBuilder buildGraphValues() {
        return new GraphValuesBuilder();
    }

    public PIDTunerBuilder withGraphValues(PIDGraphValues graphValues) {
        PIDTunerBuilder copy = new PIDTunerBuilder(this);
        copy.graphValues = Optional.of(graphValues);
        return copy;
    }

    public PIDTunerBuilder withTunerSettings(PIDTunerSettings settings) {
        PIDTunerBuilder copy = new PIDTunerBuilder(this);
        copy.settings = settings;
        return copy;
    }

    public PIDTunerBuilder withProperty(String propertyName, Consumer<Double> propertySetter) {
        PIDTunerBuilder copy = new PIDTunerBuilder(this);
        PIDProperty property = new PIDProperty(propertyName, propertySetter);
        copy.properties.add(property);
        return copy;
    }

    public PIDTunerBuilder withP(Consumer<Double> setP) {
        return withProperty("kP", setP);
    }

    public PIDTunerBuilder withI(Consumer<Double> setI) {
        return withProperty("kI", setI);
    }

    public PIDTunerBuilder withD(Consumer<Double> setD) {
        return withProperty("kD", setD);
    }

    public PIDTunerBuilder withFF(Consumer<Double> setFF) {
        return withProperty("kFF", setFF);
    }

    public PIDTunerBuilder withIZone(Consumer<Double> setIZone) {
        return withProperty("kIZone", setIZone);
    }

    public PIDTuner build() {
        return new PIDTuner(this.controllerName, this.settings, this.graphValues, this.properties.toArray(PIDProperty[]::new));
    }
}
