package org.usfirst.frc.team4999;

import edu.wpi.first.shuffleboard.api.data.ComplexDataType;

import java.util.Map;
import java.util.function.Function;

public final class MomentumPIDControllerType extends ComplexDataType<MomentumPIDControllerData> {

    public static final MomentumPIDControllerType Instance = new MomentumPIDControllerType();

    private MomentumPIDControllerType() {
        super("MomentumPIDController", MomentumPIDControllerData.class);
    }

    @Override
    public Function<Map<String, Object>, MomentumPIDControllerData> fromMap() {
        return MomentumPIDControllerData::new;
    }

    @Override 
    public MomentumPIDControllerData getDefaultValue() {
        return new MomentumPIDControllerData(0, 0, 0, 0, 0, 0, 0, 0, false, 0, 0);
    }
    
}