package org.usfirst.frc.team4999;

import edu.wpi.first.shuffleboard.api.plugin.Plugin;
import edu.wpi.first.shuffleboard.api.tab.TabInfo;
import edu.wpi.first.shuffleboard.api.plugin.Description;
import edu.wpi.first.shuffleboard.api.data.DataType;
import edu.wpi.first.shuffleboard.api.widget.ComponentType;
import edu.wpi.first.shuffleboard.api.widget.WidgetType;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

@Description(
    group = "org.usfirst.frc.team4999",
    name="Momentum PID Editor",
    version="1.0.0",
    summary="A plugin to configure MomentumPID Controllers"
)
public class MomentumPIDPlugin extends Plugin {
    public MomentumPIDPlugin() {
        super();
    }

    @Override
    public List<DataType> getDataTypes() {
        return ImmutableList.of(
            MomentumPIDControllerType.Instance
        );
    }

    @Override
    public List<ComponentType> getComponents() {
        return ImmutableList.of(
            WidgetType.forAnnotatedWidget(MomentumPIDControllerWidget.class)
        );
    }

    @Override
    public Map<DataType, ComponentType> getDefaultComponents() {
        return ImmutableMap.<DataType, ComponentType>builder()
        .put(MomentumPIDControllerType.Instance, WidgetType.forAnnotatedWidget(MomentumPIDControllerWidget.class))
        .build();
    }

    /*@Override
    public List<TabInfo> getDefaultTabInfo() {
        return ImmutableList.of(
            TabInfo.builder().name("SmartDashboard").autoPopulate().sourcePrefix("SmartDashboard/").build(),
            TabInfo.builder().name("LiveWindow").autoPopulate().sourcePrefix("LiveWindow/").build()
        );
    }*/

}