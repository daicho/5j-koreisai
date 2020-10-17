import java.util.HashMap;

public class VirtualInput {
  private HashMap<Integer, Boolean> buttonJustPressed  = new HashMap<Integer, Boolean>();
  private HashMap<Integer, Boolean> buttonPressed      = new HashMap<Integer, Boolean>();
  private HashMap<Integer, Boolean> buttonJustReleased = new HashMap<Integer, Boolean>();

  public void pressButton(int buttonCode) {
    if (buttonPressed(buttonCode)) {
      return;
    }

    buttonJustPressed.put(buttonCode, true);
    buttonPressed.put(buttonCode, true);
  }

  public void releaseButton(int buttonCode) {
    if (!buttonPressed(buttonCode)) {
      return;
    }

    buttonJustReleased.put(buttonCode, true);
    buttonPressed.put(buttonCode, false);
  }

  public void flush() {
    buttonJustPressed.clear();
    buttonJustReleased.clear();
  }

  public boolean buttonPressed() {
    return buttonPressed.containsValue(true);
  }

  public boolean buttonPressed(int buttonCode) {
    return buttonPressed.getOrDefault(buttonCode, false);
  }

  public boolean buttonJustPressed() {
    return buttonJustPressed.containsValue(true);
  }

  public boolean buttonJustPressed(int buttonCode) {
    return buttonJustPressed.getOrDefault(buttonCode, false);
  }

  public boolean buttonJustReleased() {
    return buttonJustReleased.containsValue(true);
  }

  public boolean buttonJustReleased(int buttonCode) {
    return buttonJustReleased.getOrDefault(buttonCode, false);
  }

  public static final int UP    = 0;
  public static final int LEFT  = 1;
  public static final int DOWN  = 2;
  public static final int RIGHT = 3;

  public static final int OK = 4;
  public static final int CANCEL = 5;
}
