import edu.wpi.first.smartdashboard.gui.elements.PIDEditor;
import edu.wpi.first.smartdashboard.types.NamedDataType;

/**
 * @author Jordan Powers
 */
public class MomentumPIDType extends NamedDataType {

  public static final String LABEL = "MomentumPIDController";

  private MomentumPIDType() {
    super(LABEL, PIDEditor.class);
  }

  public static NamedDataType get() {
    if (NamedDataType.get(LABEL) != null) {
      return NamedDataType.get(LABEL);
    } else {
      return new MomentumPIDType();
    }
  }
}