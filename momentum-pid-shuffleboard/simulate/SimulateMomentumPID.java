import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableEntry;

public class SimulateMomentumPID {

    private final NetworkTable sd_table;

    private final NetworkTable sub_table;

    private final NetworkTableEntry p_entry;
    private final NetworkTableEntry i_entry;
    private final NetworkTableEntry d_entry;
    private final NetworkTableEntry f_entry;
    private final NetworkTableEntry errZone_entry;
    private final NetworkTableEntry targetZone_entry;
    private final NetworkTableEntry targetTime_entry;
    private final NetworkTableEntry setpoint_entry;
    private final NetworkTableEntry enabled_entry;

    private int update = 1000;

    private static final int ENTRYFLAGS = EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate;

    private double p, i, d, f, errZone, targetZone, targetTime, setpoint;
    private boolean enabled;

    private final String name = "Test MomentumPID";

    public SimulateMomentumPID() {
        NetworkTableInstance.getDefault().startServer();
        sd_table = NetworkTableInstance.getDefault().getTable("SmartDashboard");
        sub_table = sd_table.getSubTable(name);
        sub_table.getEntry(".name").setString(name);
        sub_table.getEntry(".type").setString("MomentumPIDController");

        p_entry = sub_table.getEntry("p");
        i_entry = sub_table.getEntry("i");
        d_entry = sub_table.getEntry("d");
        f_entry = sub_table.getEntry("f");
        errZone_entry = sub_table.getEntry("errZone");
        targetZone_entry = sub_table.getEntry("targetZone");
        targetTime_entry = sub_table.getEntry("targetTime");
        setpoint_entry = sub_table.getEntry("setpoint");
        enabled_entry = sub_table.getEntry("enabled");

        p_entry.setDouble(p);
        i_entry.setDouble(i);
        d_entry.setDouble(d);
        f_entry.setDouble(f);
        errZone_entry.setDouble(errZone);
        targetZone_entry.setDouble(targetZone);
        targetTime_entry.setDouble(targetTime);
        setpoint_entry.setDouble(setpoint);
        enabled_entry.setBoolean(enabled);
        
    }
    
    public void simulate() {

       
        p_entry.addListener(entry -> {if(entry.value.isDouble()){p = entry.value.getDouble();}}, ENTRYFLAGS);
        i_entry.addListener(entry -> {if(entry.value.isDouble()){i = entry.value.getDouble();}}, ENTRYFLAGS);
        d_entry.addListener(entry -> {if(entry.value.isDouble()){d = entry.value.getDouble();}}, ENTRYFLAGS);
        f_entry.addListener(entry -> {if(entry.value.isDouble()){f = entry.value.getDouble();}}, ENTRYFLAGS);
        errZone_entry.addListener(entry -> {if(entry.value.isDouble()){errZone = entry.value.getDouble();}}, ENTRYFLAGS);
        targetZone_entry.addListener(entry -> {if(entry.value.isDouble()){targetZone = entry.value.getDouble();}}, ENTRYFLAGS);
        targetTime_entry.addListener(entry -> {if(entry.value.isDouble()){targetTime = entry.value.getDouble();}}, ENTRYFLAGS);
        setpoint_entry.addListener(entry -> {if(entry.value.isDouble()){setpoint = entry.value.getDouble();}}, ENTRYFLAGS);
        enabled_entry.addListener(entry -> {if(entry.value.isBoolean()){enabled = entry.value.getBoolean();}}, ENTRYFLAGS);
        

        while(true){

            if(enabled) {
                p = Math.random() * 10;
                i = Math.random() * 10;
                d = Math.random() * 10;
                f = Math.random() * 10;
                errZone = Math.random() * 10;
                targetZone = Math.random() * 10;
                targetTime = Math.random() * 10;
                setpoint = Math.random() * 10;
                p_entry.setDouble(p);
                i_entry.setDouble(i);
                d_entry.setDouble(d);
                f_entry.setDouble(f);
                errZone_entry.setDouble(errZone);
                targetZone_entry.setDouble(targetZone);
                targetTime_entry.setDouble(targetTime);
                setpoint_entry.setDouble(setpoint);
            }

            System.out.format("p:%.3f, i:%.3f, d:%.3f, f:%.3f, errZone:%.3f, targetZone:%.3f, targetTime:%.3f, setpoint:%.3f, enabled:%s\n",
            p, i, d, f, errZone, targetZone, targetTime, setpoint, enabled);

            try {
                Thread.sleep(update);
            } catch (InterruptedException e) {
                break;
            }
            
        }
    }

}