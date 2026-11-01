public class RemoteControl {
    public static void main(String[] args) {
        Switchable[] devices = { new Fan(), new Light() };

        for (Switchable d : devices) {
            d.toggle();
            System.out.println(d.getClass().getSimpleName() + " is on? " + d.isOn());
        }

        // Anonymous class version
        SwitchRule anonymousRule = new SwitchRule() {
            @Override
            public boolean mayTurnOn(Switchable device, int hour) {
                return hour >= 6 && hour < 22;
            }
        };

        // Lambda version
        SwitchRule lambdaRule = (device, hour) -> hour >= 6 && hour < 22;

        int hour = 23;
        System.out.println("Anonymous rule at " + hour + ":00 -> "
            + anonymousRule.mayTurnOn(devices[0], hour));
        System.out.println("Lambda rule at " + hour + ":00 -> "
            + lambdaRule.mayTurnOn(devices[0], hour));
        hour = 10;
        System.out.println("Anonymous rule at " + hour + ":00 -> "
            + anonymousRule.mayTurnOn(devices[1], hour));
        System.out.println("Lambda rule at " + hour + ":00 -> "
            + lambdaRule.mayTurnOn(devices[1], hour));
    }
}

interface Switchable {
    void on();
    void off();
    boolean isOn();

    default void toggle() {
        if (isOn()) {
            off();
        } else {
            on();
        }
    }
}

@FunctionalInterface
interface SwitchRule {
    boolean mayTurnOn(Switchable device, int hour);
}

class Fan implements Switchable {
    private boolean state;
    public void on()  { state = true; }
    public void off() { state = false; }
    public boolean isOn() { return state; }
}

class Light implements Switchable {
    private boolean state;
    public void on()  { state = true; }
    public void off() { state = false; }
    public boolean isOn() { return state; }
}
