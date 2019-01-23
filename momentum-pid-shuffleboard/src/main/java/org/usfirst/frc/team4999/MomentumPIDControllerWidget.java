package org.usfirst.frc.team4999;

import edu.wpi.first.shuffleboard.api.components.NumberField;
import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;

import org.controlsfx.control.ToggleSwitch;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;

@Description(name="Momentum PID Controller", dataTypes=MomentumPIDControllerData.class)
@ParametrizedController("MomentumPIDControllerWidget.fxml")
public class MomentumPIDControllerWidget extends SimpleAnnotatedWidget<MomentumPIDControllerData> {
    @FXML
    private Pane root;
    @FXML
    private NumberField pField;
    @FXML
    private NumberField iField;
    @FXML
    private NumberField dField;
    @FXML
    private NumberField fField;
    @FXML
    private NumberField errZoneField;
    @FXML
    private NumberField targetZoneField;
    @FXML
    private NumberField targetTimeField;
    @FXML
    private NumberField setpointField;
    @FXML
    private ToggleSwitch enableToggle;

    @FXML
    private Label currentLabel;
    @FXML
    private Label outputLabel;

    private boolean togglingFromDataChange = true;

    @FXML
    private void initialize() {
        root.setStyle("-fx-font-size: 10pt;");
        dataProperty().addListener((__, old, newData) -> {
            pField.setNumber(newData.getP());
            iField.setNumber(newData.getI());
            dField.setNumber(newData.getD());
            fField.setNumber(newData.getF());
            errZoneField.setNumber(newData.getErrZone());
            targetZoneField.setNumber(newData.getTargetZone());
            targetTimeField.setNumber(newData.getTargetTime());
            setpointField.setNumber(newData.getSetpoint());
            togglingFromDataChange = true;
            enableToggle.setSelected(newData.isEnabled());
            togglingFromDataChange = false;
            currentLabel.setText(Double.toString(newData.getCurrent()));
            outputLabel.setText(Double.toString(newData.getOutput()));
        });

        actOnFocusLost(pField);
        actOnFocusLost(iField);
        actOnFocusLost(dField);
        actOnFocusLost(fField);
        actOnFocusLost(errZoneField);
        actOnFocusLost(targetZoneField);
        actOnFocusLost(targetTimeField);
        actOnFocusLost(setpointField);

        enableToggle.selectedProperty().addListener((__, prev, cur) -> {
            if (!togglingFromDataChange) {
            MomentumPIDControllerData data = getData();
            setData(data.withEnabled(!data.isEnabled()));
            }
        });
    }

    private void actOnFocusLost(TextField field) {
        field.focusedProperty().addListener((__, was, is) -> {
            if (!is) {
                field.getOnAction().handle(new ActionEvent(this, field));
            }
        });
    }

    @Override
    public Pane getView() {
        return root;
    }

    @FXML
    private void setP() {
        setData(getData().withP(pField.getNumber()));
    }

    @FXML
    private void setI() {
        setData(getData().withI(iField.getNumber()));
    }

    @FXML
    private void setD() {
        setData(getData().withD(dField.getNumber()));
    }

    @FXML
    private void setF() {
        setData(getData().withF(fField.getNumber()));
    }

    @FXML
    private void setErrZone() {
        setData(getData().withErrZone(errZoneField.getNumber()));
    }

    @FXML
    private void setTargetZone() {
        setData(getData().withTargetZone(targetZoneField.getNumber()));
    }

    @FXML
    private void setTargetTime() {
        setData(getData().withTargetTime(targetTimeField.getNumber()));
    }

    @FXML
    private void setSetpoint() {
        setData(getData().withSetpoint(setpointField.getNumber()));
    }

}