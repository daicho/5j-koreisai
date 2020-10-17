import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

class SGame extends Scene {
  private final Stage s;
  private final List<StageObject> stageObjects;
  private final SOUnagi unagi;
  private boolean start = false;
  
  private final PImage logo = loadImage("logo/logo-250.png");

  SGame() {
    s = new Stage();

    stageObjects = new LinkedList<StageObject>();
    
    stageObjects.add(unagi = new SOUnagi(s));    
    s.attachUnagi(unagi);

    stageObjects.add(new SOFeed(s));
    stageObjects.add(new SOSFeed(s));
    stageObjects.add(new SOHook(s));
    
    final SONet net = new SONet(s);
    s.attachNet(net);
    stageObjects.add(net);
  }

  @Override
  public Scene update(VirtualInput vi) {
    if (vi.buttonJustPressed()) {
      start = true;
    }

    if (!start) {
      return this;
    }

    for (StageObject so : stageObjects) {
      so.update(vi);
    }
    
    if (unagi.isAlive()) {
      return this;
    }

    unagi.close();
    return new SResult(unagi.length(), unagi.quality());
  }

  @Override
  public void render() {
    background(0);
    
    image(logo, (width - logo.width) / 2, 50);
    
    pushMatrix();
    translate((width - s.w * Stage.GRID_SIZE) / 2, 170);
    s.render();
    for (StageObject so : stageObjects) {
      so.render();
    }
    
    fill(255);
    
    textAlign(LEFT, BOTTOM);
    textSize(20);
    text("ながさ：", 0, 480); text("cm", 200, 480);
    text("ランク：", 0, 600);
    
    textSize(40);
    text(rankName(unagi.quality()), 100, 600);
    
    textAlign(RIGHT, BOTTOM);
    text(unagi.length() * CM_PER_UNIT, 180, 484);
    
    popMatrix();
  }

  class Vector {
    public final int x;
    public final int y;

    Vector(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }

  private class Stage {
    public final int w = 23;
    public final int h = 23;
    private final StageObject[][] stage;
    private SOUnagi unagi;

    Stage() {
      stage = new StageObject[h][w];
    }

    public void attachUnagi(SOUnagi unagi) {
      this.unagi = unagi;
    }
    
    public void attachNet(SONet net) {
      for (int x = 0; x < w; x++) {
        stage[0][x] = net;
        stage[w - 1][x] = net;
      }
      
      for (int y = 1; y < h - 1; y++) {
        stage[y][0] = net;
        stage[y][h - 1] = net;
      }
    }
    
    public void render() {
      noStroke();
      for (int x = 0; x < w; x++) {
        for (int y = 0; y < h; y++) {
          fill((x + y) % 2 == 0 ? color(#89c3eb) : color(#bcd8eb));
          rect(GRID_SIZE * x, GRID_SIZE * y, GRID_SIZE, GRID_SIZE);
        }
      }
    }

    public StageObject get(int x, int y) {
      return stage[y][x];
    }

    public void put(StageObject so, int x, int y) {
      stage[y][x] = so;
    }

    public int freeSpaces() {
      return (w - 2) * (h - 2) - unagi.length();
    }
    
    public void sprite(PImage img, int x, int y) {
      image(img, GRID_SIZE * x, GRID_SIZE * y);
    }

    public Vector vacant() {
      if (freeSpaces() == 0) {
        return null;
      }

      while (true) {
        final Random random = new Random();
        final int rx = random.nextInt(w - 2) + 1;
        final int ry = random.nextInt(h - 2) + 1;

        if (get(rx, ry) != null) {
          continue;
        }

        return new Vector(rx, ry);
      }
    }

    public Vector vacant(int dist) {
      final Vector v = unagi.headPosition();
      final List<Vector> candidates = new ArrayList<Vector>();
      for (int x = -dist; x <= dist; x++) {
        for (int pm = -1; pm < 2; pm += 2) {
          final Vector candidate = new Vector(
            v.x + x,
            v.y + (dist - Math.abs(x)) * (0 - pm)
          );

          if (candidate.x < 1 || candidate.x > w - 2) {
            continue;
          }

          if (candidate.y < 1 || candidate.y > h - 2) {
            continue;
          }

          if (get(candidate.x , candidate.y) == null) {
            candidates.add(candidate);
          }
        }
      }

      if (candidates.isEmpty()) {
        return null;
      }

      return candidates.get(new Random().nextInt(candidates.size()));
    }

    public static final int D_UP = 0;
    public static final int D_LEFT = 1;
    public static final int D_DOWN = 2;
    public static final int D_RIGHT = 3;
    
    private static final int GRID_SIZE = 16;
  }

  private abstract class StageObject {
    protected final Stage stage;

    StageObject(Stage stage) {
      this.stage = stage;
    }

    public abstract void update(VirtualInput vi);
    public abstract void render();
  }

  private class SOUnagi extends StageObject {
    private final LinkedList<Unit> units = new LinkedList<Unit>();
    private int quality = 0;
    
    private Unit pass = null;

    private int animation = ANIMATION;
    private int to = Stage.D_UP;
    private boolean alive = true;
    
    private AudioPlayer eatingSound = loadAudio("sound/egg-break1.mp3");
    private boolean ate = false;
    
    SOUnagi(Stage stage) {
      super(stage);
      
      units.add(new Unit(new Vector(stage.w / 2, stage.h / 2 + 0), Stage.D_UP, Stage.D_UP));
      units.add(new Unit(new Vector(stage.w / 2, stage.h / 2 + 1), Stage.D_UP, Stage.D_UP));
      units.add(new Unit(new Vector(stage.w / 2, stage.h / 2 + 2), Stage.D_UP, Stage.D_UP));
      units.add(new Unit(new Vector(stage.w / 2, stage.h / 2 + 3), Stage.D_UP, Stage.D_UP));
      
      stage.put(this, stage.w / 2, stage.h / 2 + 0);
      stage.put(this, stage.w / 2, stage.h / 2 + 1);
      stage.put(this, stage.w / 2, stage.h / 2 + 2);
      stage.put(this, stage.w / 2, stage.h / 2 + 3);
    }

    @Override
    public void update(VirtualInput vi) {
      ate = false;
      int headDCrnt = units.getFirst().dCrnt;

      switch (headDCrnt) {
        case Stage.D_UP:
        case Stage.D_DOWN:
          if      (vi.buttonJustPressed(VirtualInput.LEFT) && !vi.buttonJustPressed(VirtualInput.RIGHT)) to = Stage.D_LEFT;
          else if (!vi.buttonJustPressed(VirtualInput.LEFT) && vi.buttonJustPressed(VirtualInput.RIGHT)) to = Stage.D_RIGHT;
          break;
        case Stage.D_LEFT:
        case Stage.D_RIGHT:
          if      (vi.buttonJustPressed(VirtualInput.UP) && !vi.buttonJustPressed(VirtualInput.DOWN)) to = Stage.D_UP;
          else if (!vi.buttonJustPressed(VirtualInput.UP) && vi.buttonJustPressed(VirtualInput.DOWN)) to = Stage.D_DOWN;
          break;
      }

      if (animation++ < ANIMATION) {
        return;
      }
      animation = 0;
      
      final Unit headInvalid = units.removeFirst();
      final Unit head = new Unit(headInvalid.position, to, headInvalid.dPrev);
      units.addFirst(head);

      final Unit newHead = new Unit(
        new Vector(
          head.position.x + ((to == Stage.D_LEFT) ? -1 : (to == Stage.D_RIGHT) ? 1 : 0),
          head.position.y + ((to == Stage.D_UP) ? -1 : (to == Stage.D_DOWN ? 1 : 0))
        ),
        to,
        head.dCrnt
      );

      pass = units.removeLast();
      stage.put(null, pass.position.x, pass.position.y);
      
      final StageObject so = stage.get(newHead.position.x, newHead.position.y);
      if (so instanceof SOFeed) {
        ((SOFeed)so).eaten();
        ate = true;
        units.addLast(pass);
        stage.put(this, pass.position.x, pass.position.y);
        pass = null;
      }
      else if (so instanceof SOSFeed) {
        ((SOSFeed)so).eaten();
        ate = true;
        quality++;
      }
      else if (so != null) {
        alive = false;
      }

      units.addFirst(newHead);
      stage.put(this, newHead.position.x, newHead.position.y);
    }

    @Override
    public void render() {
      int i = -1;
      ellipseMode(CORNER);
      fill(0, 0, 255);
      for (Unit u : units) {
        i++;
        
        if (i == 0) {
          stage.sprite(img_head[animation][DONTCARE][u.dCrnt], u.position.x, u.position.y);
          continue;
        }
        
        if (i == 1) {
          stage.sprite(img_neck[animation][u.dPrev][u.dCrnt], u.position.x, u.position.y);
          continue;
        }
        
        if (i == units.size() - 1) {
          stage.sprite(img_tail[pass == null ? ANIMATION : animation][animation == ANIMATION ? u.dCrnt : u.dPrev][u.dCrnt], u.position.x, u.position.y);
          continue;
        }
        
        stage.sprite(img_body[DONTCARE][u.dPrev][u.dCrnt], u.position.x, u.position.y);
      }
      
      if (pass != null && animation < ANIMATION) {
        stage.sprite(img_pass[DONTCARE][DONTCARE][pass.dCrnt], pass.position.x, pass.position.y);
      }
      
      if (ate) {
        eatingSound.play();
        eatingSound.rewind();
      }
    }

    public int length() {
      return units.size();
    }

    public int quality() {
      return quality;
    }

    public boolean isAlive() {
      return alive;
    }
    
    public void close() {
      eatingSound.close();
    }

    public Vector headPosition() {
      return units.getFirst().position;
    }

    private class Unit {
      final Vector position;
      final int dCrnt;
      final int dPrev;

      Unit(Vector position, int dCrnt, int dPrev) {
        this.position = position;
        this.dCrnt = dCrnt;
        this.dPrev = dPrev;
      }
    }
    
    private final PImage[][][] img_head;
    private final PImage[][][] img_neck;
    private final PImage[][][] img_body;
    private final PImage[][][] img_tail;
    private final PImage[][][] img_pass;

    private static final int ANIMATION = 1;
    private static final int DONTCARE  = 0;
    
    {
      img_head = new PImage[2][1][4];
      img_neck = new PImage[2][4][4];
      img_body = new PImage[1][4][4];
      img_tail = new PImage[2][4][4];
      img_pass = new PImage[1][1][4];
      
      
      for (int i = 0; i < ANIMATION + 1; i++) {
        for (int from = 0; from < 4; from++) {
          for (int to = 0; to < 4; to++) {
            if (from == DONTCARE) {
              img_head[i][0][to] = loadImage("sprite/unagi/head-" + i + "-x-" + to + ".png");
            }
            
            if (i == DONTCARE && from == DONTCARE) {
              img_pass[DONTCARE][DONTCARE][to] = loadImage("sprite/unagi/pass-x-x-" + to + ".png");
            }
            
            if (from != to && (from + to) % 2 == 0) {
              continue;
            }
            
            img_neck[i][from][to] = loadImage("sprite/unagi/neck-" + i + "-" + from + "-" + to + ".png");
            
            if (i == DONTCARE) {
              img_body[DONTCARE][from][to] = loadImage("sprite/unagi/body-x-" + from + "-" + to + ".png");
            }
            
            img_tail[i][from][to] = loadImage("sprite/unagi/tail-" + i + "-" + (i == ANIMATION ? to : from) + "-" + to + ".png");
          }
        }
      }
    }
  }

  private class SOFeed extends StageObject {
    private Vector position = null;

    SOFeed(Stage stage) {
      super(stage);
      
      update(null);
    }

    @Override
    public void update(VirtualInput vi) {
      if (position != null) {
        return;
      }

      position = stage.vacant();
      if (position == null) {
        return;
      }

      stage.put(this, position.x, position.y);
    }

    @Override
    public void render() {
      if (position == null) {
        return;
      }
      
      stage.sprite(img, position.x, position.y);
    }

    public void eaten() {
      position = null;
    }
    
    private final PImage img = loadImage("sprite/feed.png");
  }

  private class SOSFeed extends StageObject {
    private Vector position = null;
    private int count = STEP_APPEAR;

    SOSFeed(Stage stage) {
      super(stage);
    }

    @Override
    public void update(VirtualInput vi) {
      if (count-- != 0) {
        return;
      }

      if (position == null) {
        if (stage.freeSpaces() > 10) {
          position = stage.vacant();
          count = STEP_DISAPPEAR * 2;
          
          if (position != null) {
            stage.put(this, position.x, position.y);
          }
        }

        return;
      }

      stage.put(null, position.x, position.y);
      position = null;
      count = STEP_APPEAR * 2;
    }

    @Override
    public void render() {
      if (position == null) {
        return;
      }
      
      stage.sprite(img, position.x, position.y);
    }

    public void eaten() {
      position = null;
      count = STEP_APPEAR * 2;
    }

    private static final int STEP_APPEAR    = 70;
    private static final int STEP_DISAPPEAR = 30;
    
    private final PImage img = loadImage("sprite/sfeed.png");
  }

  private class SOHook extends StageObject {
    private Vector position;
    private int count = STEP_APPEAR_BASE + new Random().nextInt(STEP_APPEAR_RANGE + 1);

    SOHook(Stage stage) {
      super(stage);
    }

    @Override
    public void update(VirtualInput vi) {
      if (count-- != 0) {
        return;
      }

      if (position == null) {
        if (stage.freeSpaces() > 10) {
          position = stage.vacant(5);
          count = (STEP_DISAPPEAR_BASE + new Random().nextInt(STEP_DISAPPEAR_RANGE + 1)) * 2;
          if (position != null) {
            stage.put(this, position.x, position.y);
          }
        }

        return;
      }

      stage.put(null, position.x, position.y);
      position = null;
      count = (STEP_APPEAR_BASE + new Random().nextInt(STEP_APPEAR_RANGE + 1)) * 2;
    }

    @Override
    public void render() {
      if (position == null) {
        return;
      }
      
      stage.sprite(img, position.x, position.y);
    }

    private static final int STEP_APPEAR_BASE     = 5;
    private static final int STEP_APPEAR_RANGE    = 10;
    private static final int STEP_DISAPPEAR_BASE  = 15;
    private static final int STEP_DISAPPEAR_RANGE = 15;
    
    private final PImage img = loadImage("sprite/hook.png");
  }

  private class SONet extends StageObject {
    SONet(Stage stage) {
      super(stage);
    }

    @Override
    public void update(VirtualInput vi) {}

    @Override
    public void render() {
      for (int x = 0; x < stage.w; x++) {
        stage.sprite(img, x, 0);
        stage.sprite(img, x, stage.h - 1);
      }
      
      for (int y = 1; y < stage.h - 1; y++) {
        stage.sprite(img, 0, y);
        stage.sprite(img, stage.w - 1, y);
      }
    }
    
    private final PImage img = loadImage("sprite/net.png");
  }
}
