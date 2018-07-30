package org.usfirst.frc.team4999.SmartDashboard;
import edu.wpi.first.smartdashboard.gui.elements.PIDEditor;
import edu.wpi.first.smartdashboard.types.NamedDataType;

/**
 * @author Jordan Powers
 */
public class MomentumPIDType extends NamedDataType {

  public static final String LABEL = "MomentumPIDController";

  private MomentumPIDType() {
    super(LABEL, MomentumPIDEditor.class);
  }

  public static NamedDataType get() {
    if (NamedDataType.get(LABEL) != null) {
      return NamedDataType.get(LABEL);
    } else {
      return new MomentumPIDType();
    }
  }
}
