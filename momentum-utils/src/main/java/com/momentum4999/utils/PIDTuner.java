package com.momentum4999.utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.ini4j.Ini;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.NetworkTableEvent;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.SuppliedValueWidget;

/**
 * A tuner for PID controllers. This class helps tune PID controllers by publishing the
 * PID constants to the shuffleboard and updating the controllers whenever the constants
 * are changed. It also saves and loads tuned constants from a file.
 */
public class PIDTuner {
    public static interface PIDGraphValues {
        public abstract double getSetpoint();
        public abstract double getLastMeasurement();
        public abstract double getLastOutput();
    }

    public static class PIDTunerSettings {
        public String shuffleboardTab = "PID Tuning";
        public File saveValuesLocation = new File("pid_constants.ini");
        public boolean showOnShuffleboard = true;

        public PIDTunerSettings(){}
        public PIDTunerSettings(PIDTunerSettings toCopy) {
            this.shuffleboardTab = toCopy.shuffleboardTab;
            this.saveValuesLocation = toCopy.saveValuesLocation;
            this.showOnShuffleboard = toCopy.showOnShuffleboard;
        }

        public PIDTunerSettings withShowOnShuffleboard(boolean showOnShuffleboard) {
            PIDTunerSettings copy = new PIDTunerSettings(this);
            copy.showOnShuffleboard = showOnShuffleboard;
            return copy;
        }

        public PIDTunerSettings withSaveValuesLocation(File saveValuesLocation) {
            PIDTunerSettings copy = new PIDTunerSettings(this);
            copy.saveValuesLocation = saveValuesLocation;
            return copy;
        }

        public PIDTunerSettings withShuffleboardTab(String shuffleboardTab) {
            PIDTunerSettings copy = new PIDTunerSettings(this);
            copy.shuffleboardTab = shuffleboardTab;
            return copy;
        }

    }

    public static class PIDProperty {
        public final String propertyName;
        public final Consumer<Double> setter;

        public PIDProperty(String propertyName, Consumer<Double> setter) {
            this.propertyName = propertyName;
            this.setter = setter;
        }
    }

    private static class DataStore {
        private static HashMap<File, DataStore> instances = new HashMap<>();
        public static DataStore getInstance(File file) {
            if(!instances.containsKey(file)) {
                instances.put(file, new DataStore(file));
            }
            return instances.get(file);
        }

        private final File file;
        private final Ini ini;

        public DataStore(File file) {
            this.file = file;
            ini = new Ini();
            if(file.isFile()) {
                try {
                    ini.load(file);
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void putValue(String section, String property, double value) {
            ini.put(section, property, value);
        }

        public Optional<Double> getValue(String section, String property) {
            if(ini.containsKey(section) && ini.get(section).containsKey(property)) {
                return Optional.of(ini.get(section, property, Double.class));
            } else {
                return Optional.empty();
            }
        }

        public void save() {
            try {
                ini.store(file);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class PIDPropertyImpl implements Consumer<NetworkTableEvent> {
        private final PIDProperty property;

        private static class ShuffleboardValues {
            public final GenericEntry entry;
            public final int listenerHandle;

            public ShuffleboardValues(GenericEntry entry, int listenerHandle) {
                this.entry = entry;
                this.listenerHandle = listenerHandle;
            }
        }

        private static class DatastoreValues {
            public final DataStore store;
            public final String controllerName;

            public DatastoreValues(DataStore store, String controllerName) {
                this.store = store;
                this.controllerName = controllerName;
            }
        }

        private Optional<ShuffleboardValues> shuffleboardValues = Optional.empty();
        private Optional<DatastoreValues> datastoreValues = Optional.empty();

        public PIDPropertyImpl(PIDProperty property) {
            this.property = property;
        }

        public void setupShuffleboard(ShuffleboardLayout layoutGroup) {
            var entry = layoutGroup.add(property.propertyName, 0).withWidget(BuiltInWidgets.kTextView).getEntry();
            var listenerHandle = entry.getTopic().getInstance().addListener(entry.getTopic(), EnumSet.of(NetworkTableEvent.Kind.kValueAll), this);

            this.shuffleboardValues = Optional.of(new ShuffleboardValues(entry, listenerHandle));

            // Need to propagate the stored value to the shuffleboard if setupDatastore() was
            // called before setupShuffleboard()
            if(datastoreValues.isPresent()) {
                var values = datastoreValues.get();
                var maybeValue = values.store.getValue(values.controllerName, property.propertyName);
                if(maybeValue.isPresent()) {
                    entry.setDouble(maybeValue.get());
                }
            }
        }

        public void setupDatastore(DataStore store, String controllerName) {
            this.datastoreValues = Optional.of(new DatastoreValues(store, controllerName));

            // Need to propagate the stored value to the controller, and to the shuffleboard
            // if setupShuffleboard() was called before setupDatastore()
            var maybeValue = store.getValue(controllerName, property.propertyName);
            if(maybeValue.isPresent()) {
                double value = maybeValue.get();
                if(shuffleboardValues.isPresent()) {
                    shuffleboardValues.get().entry.setDouble(value);
                } else {
                    property.setter.accept(value);
                }
            }
        }

        @Override
        public void accept(NetworkTableEvent e) {
            double value = e.valueData.value.getDouble();
            property.setter.accept(value);
            if(datastoreValues.isPresent()) {
                var values = datastoreValues.get();
                values.store.putValue(values.controllerName, property.propertyName, value);
                values.store.save();
            }
        }

        protected void removeListener() {
            if(shuffleboardValues.isPresent()) {
                var values = shuffleboardValues.get();
                values.entry.getTopic().getInstance().removeListener(values.listenerHandle);
            }
        }
    }

    private final String controllerName;

    private final DataStore store;

    private final List<PIDPropertyImpl> properties;

    private SuppliedValueWidget<Double> setpointWidget;
    private SuppliedValueWidget<Double> lastMeasurementWidget;
    private SuppliedValueWidget<Double> lastOutputWidget;

    public PIDTuner(String controllerName, PIDTunerSettings settings, Optional<PIDGraphValues> graphValues, PIDProperty... properties) {
        this.controllerName = controllerName;

        this.store = DataStore.getInstance(settings.saveValuesLocation);
        this.properties = Arrays.stream(properties).map(PIDPropertyImpl::new).toList();

        for(PIDPropertyImpl prop : this.properties) {
            prop.setupDatastore(this.store, this.controllerName);
        }

        if(settings.showOnShuffleboard) {
            setupShuffleboard(settings.shuffleboardTab, graphValues);
        }
    }

    private void setupShuffleboard(String tabName, Optional<PIDGraphValues> graphValues) {
        ShuffleboardTab tab = Shuffleboard.getTab(tabName);
        ShuffleboardLayout layoutGroup = tab.getLayout(controllerName, BuiltInLayouts.kList).withSize(2, 3).withProperties(Map.of("Label position", "RIGHT"));

        for(PIDPropertyImpl prop : this.properties) {
            prop.setupShuffleboard(layoutGroup);
        }

        if(graphValues.isPresent()) {
            var values = graphValues.get();
            setpointWidget = layoutGroup.addDouble("Setpoint", values::getSetpoint).withProperties(Map.of("Visible time", 10));
            lastMeasurementWidget = layoutGroup.addDouble("Measurement", values::getLastMeasurement).withProperties(Map.of("Visible time", 10));
            lastOutputWidget = layoutGroup.addDouble("Output", values::getLastOutput).withProperties(Map.of("Visible time", 10));
        }
    }

    /**
     * De-registers the listeners for updates to PID constants on the shuffleboard. You shouldn't
     * call this method unless you know what you're doing.
     */
    public void cleanup() {
        for(PIDPropertyImpl prop : properties) {
            prop.removeListener();
        }
    }
}
