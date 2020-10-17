public class Game {
  private final VirtualInput vi;
  private Scene s;

  Game(VirtualInput vi, Scene s) {
    this.vi = vi;
    this.s = s;
  }

  public boolean frame() {
    s = s.update(vi);

    if (s == null) {
      return true;
    }

    s.render();
    return false;
  }
}
