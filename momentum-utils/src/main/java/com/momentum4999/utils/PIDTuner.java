package com.momentum4999.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

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

    public static interface PIDAdapter {
        public abstract void setP(double kP);
        public abstract void setI(double kI);
        public abstract void setD(double kD);
        public abstract void setFF(double kFF);
        public abstract void setIZone(double kIZone);
        public abstract double getSetpoint();
        public abstract double getCurrentValue();
    }

    public static class PIDTunerSettings {
        public String shuffleboardTab = "PID Tuning";
        public File saveValuesLocation = new File("pid_constants.ini");
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

    private class PIDProperty implements Consumer<NetworkTableEvent> {
        public final String propertyName;
        public final GenericEntry entry;
        public final int listenerHandle;
        public Consumer<Double> setter;

        public PIDProperty(String propertyName, Consumer<Double> setter) {
            this.propertyName = propertyName;
            this.setter = setter;

            entry = layoutGroup.add(propertyName, 0).withWidget(BuiltInWidgets.kTextView).getEntry();

            listenerHandle = entry.getTopic().getInstance().addListener(entry.getTopic(), EnumSet.of(NetworkTableEvent.Kind.kValueAll), this);

            var value = store.getValue(controllerName, propertyName);
            if(value.isPresent()) {
                entry.setDouble(value.get());
            }
        }

        @Override
        public void accept(NetworkTableEvent e) {
            double value = e.valueData.value.getDouble();
            store.putValue(controllerName, propertyName, value);
            setter.accept(value);
            store.save();
        }

        protected void removeListener() {
            entry.getTopic().getInstance().removeListener(listenerHandle);
        }
    }

    private final PIDAdapter adapter;
    private final String controllerName;
    private final ShuffleboardLayout layoutGroup;

    private final DataStore store;

    private final List<PIDProperty> properties = new ArrayList<>();

    private final SuppliedValueWidget<Double> setpointWidget;
    private final SuppliedValueWidget<Double> currentValueWidget;

    public PIDTuner(String controllerName, PIDAdapter adapter) {
        this(controllerName, adapter, new PIDTunerSettings());
    }

    public PIDTuner(String controllerName, PIDAdapter adapter, PIDTunerSettings settings) {
        this.adapter = adapter;
        this.controllerName = controllerName;
        this.store = DataStore.getInstance(settings.saveValuesLocation);

        ShuffleboardTab tab = Shuffleboard.getTab(settings.shuffleboardTab);

        layoutGroup = tab.getLayout(controllerName, BuiltInLayouts.kList).withSize(2, 3).withProperties(Map.of("Label position", "RIGHT"));

        properties.add(new PIDProperty("kP", adapter::setP));
        properties.add(new PIDProperty("kI", adapter::setI));
        properties.add(new PIDProperty("kD", adapter::setD));
        properties.add(new PIDProperty("kFF", adapter::setFF));
        properties.add(new PIDProperty("kIZone", adapter::setIZone));

        setpointWidget = layoutGroup.addDouble("Setpoint", adapter::getSetpoint).withProperties(Map.of("Visible time", 10));
        currentValueWidget = layoutGroup.addDouble("Current", adapter::getCurrentValue).withProperties(Map.of("Visible time", 10));
    }

    /**
     * De-registers the listeners for updates to PID constants on the shuffleboard. You shouldn't
     * call this method unless you know what you're doing.
     */
    public void cleanup() {
        for(PIDProperty prop : properties) {
            prop.removeListener();
        }
    }
}
