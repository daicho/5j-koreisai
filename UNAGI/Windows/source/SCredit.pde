class SCredit extends Scene {
  // LOAD IMAGE LIKE THIS
  
  
  private class TextMaster extends StageObject {
    private List<Text> text_level0;
    private List<Text> text_level1;
    private int nextCounter;
    
    TextMaster(Stage stage) {
      super(stage);
      text_level0 = new LinkedList<Text>();
      text_level1 = new LinkedList<Text>();
      
      ///////-- ここに追記する --/////
      /* text_level0.add(new Text(s, new Vector(x座標, y座標), サイズ, 文字列, 全角かどうか)) */
      // 始めに表示する文字
      text_level0.add(new Text(s, new Vector(4, 5), 4, "クレジット", true));
      text_level0.add(new Text(s, new Vector(3, 14), 2, "スプライト", true));
      text_level0.add(new Text(s, new Vector(3 + 13, 14), 2, "塚田陽大", true));
      text_level0.add(new Text(s, new Vector(3, 20), 2, "プログラム", true));
      text_level0.add(new Text(s, new Vector(3 + 13, 20), 2, "芳賀七海", true));
      text_level0.add(new Text(s, new Vector(3, 26), 2, "画面描画", true));
      text_level0.add(new Text(s, new Vector(3 + 13, 26), 2, "島田拓人", true));
      text_level0.add(new Text(s, new Vector(3 + 13, 29), 2, "島田佳祐", true));
      text_level0.add(new Text(s, new Vector(3, 34), 2, "デバッグ", true));
      text_level0.add(new Text(s, new Vector(3 + 13, 34), 2, "宮坂大晟", true));
      text_level0.add(new Text(s, new Vector(3, 40), 2, "ロゴ提供", true));
      text_level0.add(new Text(s, new Vector(3 + 13, 40), 2, "角田　創", true));
      // 後で表示する文字
      text_level1.add(new Text(s, new Vector(1, 15), 4, "SPECIAL THANKS", false));
      text_level1.add(new Text(s, new Vector(10, 20), 3, "あなた", true));
      text_level1.add(new Text(s, new Vector(4, 32), 2, "THANK YOU FOR PLAYING!", false));
      //////-- ここまで --///////////
      nextCounter = 0;
      for (Text te : text_level0) {
        nextCounter += te.getLength();
      }
    }

    @Override
    public void update(VirtualInput vi) {
      if (unagi.eatCount() < nextCounter) {
        for (Text te : text_level0) {
          te.update(vi);
        }
      } else {
        for (Text te : text_level1) {
          te.update(vi);
        }
      }
    }

    @Override
    public void render() {
      if (unagi.eatCount() < nextCounter) {
        for (Text te : text_level0) {
          te.render();
        }
      } else {
        for (Text te : text_level1) {
          te.render();
        }
      }
    }
  }
  
  private class TextMinimum extends StageObject {
    private Vector position;
    private Vector size;
    private String text;
    private static final int GRID_SIZE = 16;
    
    TextMinimum(Stage stage, Vector position, int size, String text, boolean multiByte) {
      super(stage);
      this.position = position;
      this.text = text;
      if (multiByte == false) {
        this.size = new Vector(size / 2, size);
      } else if (multiByte == true) {
        this.size = new Vector(size, size);
      }
    }
    
    @Override
    public void update(VirtualInput vi) {
      if (position == null) return;
      for (int x = 0; x < this.size.x; x++) {
        for (int y = 0; y < this.size.y; y++) {
          stage.put(this, this.position.x + x, this.position.y + y);
        }
      }
    }

    @Override
    public void render() {
      if (position == null) return;
      pushMatrix();
      textAlign(LEFT, TOP);
      fill(0);
      textSize(GRID_SIZE * this.size.y);
      text(text, GRID_SIZE * this.position.x, GRID_SIZE * this.position.y);
      popMatrix();
    }
    
    public void eaten() {
      for (int x = 0; x < this.size.x; x++) {
        for (int y = 0; y < this.size.y; y++) {
          stage.put(null, this.position.x + x, this.position.y + y);
        }
      }
      position = null;
    }
  }
    
      
  
  private class Text extends StageObject {
    private List<TextMinimum> texts;
    private Vector size;
    private String text;
    
    Text(Stage stage, Vector position, int size, String text, boolean multiByte) {
      super(stage);
      this.text = text;
      texts = new LinkedList<TextMinimum>();
      if (multiByte == false) {
        this.size = new Vector(size / 2, size);
      } else if (multiByte == true) {
        this.size = new Vector(size, size);
      }
      for (int i = 0; i < text.length(); i++) {
        if (text.charAt(i) == ' ' || text.charAt(i) == '　') continue;
        texts.add(new TextMinimum(stage, new Vector(position.x + this.size.x * i, position.y), size, String.valueOf(text.charAt(i)), multiByte));
      }
    }

    @Override
    public void update(VirtualInput vi) {
      for (TextMinimum te : texts) {
          te.update(vi);
      }
    }

    @Override
    public void render() {
      for (TextMinimum te : texts) {
          te.render();
      }
    }
    
    public int getLength() {
      int len = 0;
      for (int i = 0; i < text.length(); i++) {
        if (text.charAt(i) == ' ' || text.charAt(i) == '　') continue;
        len++;
      }
      return len;
    }
  }
  
  ////////////////////
  private final Stage s;
  private final List<StageObject> stageObjects;
  private final SOUnagi unagi;
  private boolean start = false;
  

  SCredit() {
    s = new Stage();

    stageObjects = new LinkedList<StageObject>();
    
    stageObjects.add(unagi = new SOUnagi(s));    
    s.attachUnagi(unagi);    
    stageObjects.add(new TextMaster(s));
    
    
    final SONet net = new SONet(s);
    s.attachNet(net);
    stageObjects.add(net);
  }

  @Override
  public Scene update(VirtualInput vi) {
    if (vi.buttonJustPressed(VirtualInput.OK)) {
      return new STitle();
    }
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
    return new STitle();
  }

  @Override
  public void render() {
    background(color(#89c3eb));
    pushMatrix();
    for (StageObject so : stageObjects) {
      so.render();
    }
    fill(255);
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
    public final int w = 30;
    public final int h = 53;
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
        stage[h - 1][x] = net;
      }
      
      for (int y = 1; y < h - 1; y++) {
        stage[y][0] = net;
        stage[y][w - 1] = net;
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
    private int eatcount = 0;
    
    private Unit pass = null;

    private int animation = ANIMATION;
    private int to = Stage.D_UP;
    private boolean alive = true;
    
    private AudioPlayer eatingSound = loadAudio("sound/egg-break1.mp3");
    private boolean ate = false;
    
    SOUnagi(Stage stage) {
      super(stage);
      
      units.add(new Unit(new Vector(stage.w / 2 - 1, stage.h - 5), Stage.D_UP, Stage.D_UP));
      units.add(new Unit(new Vector(stage.w / 2 - 1, stage.h - 4), Stage.D_UP, Stage.D_UP));
      units.add(new Unit(new Vector(stage.w / 2 - 1, stage.h - 3), Stage.D_UP, Stage.D_UP));
      units.add(new Unit(new Vector(stage.w / 2 - 1, stage.h - 2), Stage.D_UP, Stage.D_UP));
      
      stage.put(this, stage.w / 2 - 1, stage.h - 5);
      stage.put(this, stage.w / 2 - 1, stage.h - 4);
      stage.put(this, stage.w / 2 - 1, stage.h - 3);
      stage.put(this, stage.w / 2 - 1, stage.h - 2);
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
      if (so instanceof TextMinimum) {
        ((TextMinimum)so).eaten();
        eatcount++;
        ate = true;
        units.addLast(pass);
        stage.put(this, pass.position.x, pass.position.y);
        pass = null;
      } else if (so != null) {
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
    
    public int eatCount() {
      return eatcount;
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
