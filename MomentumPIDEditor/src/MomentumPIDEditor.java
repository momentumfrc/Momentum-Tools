import edu.wpi.first.smartdashboard.gui.elements.bindings.AbstractTableWidget;
import edu.wpi.first.smartdashboard.livewindow.elements.Controller;
import edu.wpi.first.smartdashboard.livewindow.elements.NameTag;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;

/**
 * @author Jordan Powers
 */
public class MomentumPIDEditor extends AbstractTableWidget implements Controller {

  public static final DataType[] TYPES = {MomentumPIDType.get()};

  private NumberTableField pField;
  private NumberTableField iField;
  private NumberTableField dField;
  private NumberTableField fField;
  private NumberTableField errZoneField;
  private NumberTableField targetZoneField;
  private NumberTableField targetTimeField;
  private NumberTableField setpointField;
  private BooleanTableCheckBox enabledBox;
  private JLabel pLabel;
  private JLabel iLabel;
  private JLabel dLabel;
  private JLabel fLabel;
  private JLabel errZoneLabel;
  private JLabel targetZoneLabel;
  private JLabel targetTimeLabel;
  private JLabel setpointLabel;
  private JLabel enabledLabel;


  @Override
  public void init() {
    setLayout(new GridBagLayout());

    pLabel = new JLabel("P:");
    iLabel = new JLabel("I:");
    dLabel = new JLabel("D:");
    fLabel = new JLabel("F:");
    errZoneLabel = new JLabel("Windup:");
    targetZoneLabel = new JLabel("Target:");
    targetTimeLabel = new JLabel("Time:");
    setpointLabel = new JLabel("Setpoint:");
    enabledLabel = new JLabel("Enabled:");
    pLabel.setHorizontalAlignment(JLabel.RIGHT);
    iLabel.setHorizontalAlignment(JLabel.RIGHT);
    dLabel.setHorizontalAlignment(JLabel.RIGHT);
    fLabel.setHorizontalAlignment(JLabel.RIGHT);
    errZoneLabel.setHorizontalAlignment(JLabel.RIGHT);
    targetZoneLabel.setHorizontalAlignment(JLabel.RIGHT);
    targetTimeLabel.setHorizontalAlignment(JLabel.RIGHT);
    setpointLabel.setHorizontalAlignment(JLabel.RIGHT);
    enabledLabel.setHorizontalAlignment(JLabel.RIGHT);
    pField = new NumberTableField("p");
    iField = new NumberTableField("i");
    dField = new NumberTableField("d");
    fField = new NumberTableField("f");
    errZoneField = new NumberTableField("errZone");
    targetZoneField = new NumberTableField("targetZone");
    targetTimeField = new NumberTableField("targetTime");
    setpointField = new NumberTableField("setpoint");
    enabledBox = new BooleanTableCheckBox("enabled");

    int columns = 10;
    pField.setColumns(columns);
    iField.setColumns(columns);
    dField.setColumns(columns);
    fField.setColumns(columns);
    errZoneField.setColumns(columns);
    targetZoneField.setColumns(columns);
    targetTimeField.setColumns(columns);
    setpointField.setColumns(columns);
    
    GridBagConstraints c = new GridBagConstraints();
    
    /*   |        1        |        2        |
     * 1 |      pLabel     |     pField      |
     * 2 |      iLabel     |     iField      |
     * 3 |      dLabel     |     dField      |
     * 4 |      fLabel     |     fField      |
     * 5 |   errZoneLabel  |   errZoneField  |
     * 6 | targetZoneLabel | targetZoneField |
     * 7 | targetTimeLabel | targetTimeField |
     * 8 |  setpointLabel  |  setpointField  |
     * 9 |   enabledLabel  |  enabledLabel   |
     * 
     */
    
    c.gridx = 0;
    c.gridy = 1;
    add(pLabel, c);
    c.gridy = 2;
    add(iLabel, c);
    c.gridy = 3;
    add(dLabel, c);
    c.gridy = 4;
    add(fLabel, c);
    c.gridy = 5;
    add(errZoneLabel, c);
    c.gridy = 6;
    add(targetZoneLabel, c);
    c.gridy = 7;
    add(targetTimeLabel, c);
    c.gridy = 8;
    add(setpointLabel, c);
    c.gridy = 9;
    add(enabledLabel, c);

    c.gridx = 1;
    c.weightx = 1.0;
    c.gridy = 0;
    c.fill = GridBagConstraints.HORIZONTAL;
    add(nameTag = new NameTag(""), c);
    nameTag.setHorizontalAlignment(JLabel.LEFT);
    nameTag.setText(getFieldName());
    c.gridy = 1;
    add(pField, c);
    c.gridy = 2;
    add(iField, c);
    c.gridy = 3;
    add(dField, c);
    c.gridy = 4;
    add(fField, c);
    c.gridy = 5;
    add(errZoneField, c);
    c.gridy = 6;
    add(targetZoneField, c);
    c.gridy = 7;
    add(targetTimeField, c);
    c.gridy = 8;
    add(setpointField, c);
    c.gridy = 9;
    add(enabledBox, c);

    setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));

    revalidate();
    repaint();
  }

  @Override
  public void propertyChanged(Property property) {
  }

  @Override
  public void reset() {
    enabledBox.setBindableValue(false);
  }
}