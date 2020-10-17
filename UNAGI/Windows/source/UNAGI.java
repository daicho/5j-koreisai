import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 
import java.io.*; 
import java.net.*; 
import java.time.*; 
import java.time.format.*; 
import http.requests.*; 
import java.util.ArrayList; 
import java.util.Arrays; 
import java.util.LinkedList; 
import java.util.List; 
import java.util.Random; 
import java.util.Map; 
import java.util.HashMap; 
import java.util.HashMap; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class UNAGI extends PApplet {



private Minim minim;
private VirtualInput vi;
private Game g;

public void setup() {
  
  
  this.surface.setTitle("UNAGI");
  this.surface.setIcon(loadImage("sprite/feed.png"));
  
  noCursor();
  frameRate(15);
  textFont(createFont("PixelMplus10-Regular.ttf", 10, false));
  
  minim = new Minim(this);
  vi = new VirtualInput();
  g  = new Game(vi, new STitle());
}

public void draw() {
  if (g.frame()) {
    exit();
  }
  
  vi.flush();
}

public void keyPressed(KeyEvent e) {
  switch (e.getKey()) {
    case 'w': vi.pressButton(VirtualInput.UP); break;
    case 'a': vi.pressButton(VirtualInput.LEFT); break;
    case 's': vi.pressButton(VirtualInput.DOWN); break;
    case 'd': vi.pressButton(VirtualInput.RIGHT); break;
    case 'e': vi.pressButton(VirtualInput.OK); break;
    case 'q': vi.pressButton(VirtualInput.CANCEL); break;
  }
}

public void keyReleased(KeyEvent e) {
  switch (e.getKey()) {
    case 'w': vi.releaseButton(VirtualInput.UP); break;
    case 'a': vi.releaseButton(VirtualInput.LEFT); break;
    case 's': vi.releaseButton(VirtualInput.DOWN); break;
    case 'd': vi.releaseButton(VirtualInput.RIGHT); break;
    case 'e': vi.releaseButton(VirtualInput.OK); break;
    case 'q': vi.releaseButton(VirtualInput.CANCEL); break;
  }
}

public void stop() {
  minim.stop();
}

public AudioPlayer loadAudio(String s) {
  return minim.loadFile(s);
}






private static int rank(int quality) {
  return quality > 14 ? 14 : quality;
}

private static String rankName(int quality) {
  switch (rank(quality)) {
    case 0:  return "草・並";
    case 1:  return "草・上";
    case 2:  return "草・特上";
    case 3:  return "梅・並";
    case 4:  return "梅・上";
    case 5:  return "梅・特上";
    case 6:  return "竹・並";
    case 7:  return "竹・上";
    case 8:  return "竹・特上";
    case 9:  return "松・並";
    case 10: return "松・上";
    case 11: return "松・特上";
    case 12: return "桜・並";
    case 13: return "桜・上";
    case 14: return "桜・特上";
    default: return "虚無";
  }
}

private LinkedList<Record> databaseLoad() {
  final String data = databaseQuery("SELECT name, score FROM ranking ORDER BY score DESC LIMIT 10;");
  if (data == null) {
    return null;
  }
  
  final LinkedList<Record> result = new LinkedList<Record>();
  if (data.isEmpty()) {
    return result;
  }
  
  final String[] records = data.split("\n");
  for (String record : records) {
    final String[] cols = record.split(",");
    result.addLast(new Record(cols[0], Integer.parseInt(cols[1])));
  }
  
  return result;
}

private void databaseAdd(String name, int score) {
  LocalDateTime ldt = LocalDateTime.now();
  String datetime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(ldt);
  databaseQuery("INSERT INTO ranking VALUES" + "('" + name + "', " + score + ", '" + datetime + "');");
}

private String databaseQuery(String sql) {
  if (!databaseCanConnect()) {
    return null;
  }
  
  try {
    PostRequest post = new PostRequest(DB_URL);
    post.addData("query", URLEncoder.encode(sql, "UTF-8"));
    post.send();
    return post.getContent();
  } catch (Exception e) {
    return null;
  }
}

private boolean databaseCanConnect() {
  try {
    new URL(DB_URL).openConnection().getInputStream();
  } catch (Exception e) {
    return false;
  }
  
  return true;
}

private static final int CM_PER_UNIT = 10;
private static final String DB_NAME = "unagi";
private static final String DB_URL = "https://nitnc5j.sakura.ne.jp/" + DB_NAME + "/mysql/query.php";
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
class Record {
  public final String name;
  public final int score;
  
  Record(String name, int score) {
    this.name = name;
    this.score = score;
  }
}
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
    background(color(0xff89c3eb));
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
          fill((x + y) % 2 == 0 ? color(0xff89c3eb) : color(0xffbcd8eb));
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
          fill((x + y) % 2 == 0 ? color(0xff89c3eb) : color(0xffbcd8eb));
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
class SHowto extends Scene {
  // LOAD IMAGE LIKE THIS
  private final PImage imgBack  = loadImage("sprite/back.png");
  private final PImage imgFeed  = loadImage("sprite/feed.png");
  private final PImage imgSFeed = loadImage("sprite/sfeed.png");
  private final PImage imgHook  = loadImage("sprite/hook.png");
  private final PImage imgNet   = loadImage("sprite/net.png");
  
  @Override
  public Scene update(VirtualInput vi) {
    if (vi.buttonJustPressed(VirtualInput.OK)) {
      return new STitle();
    }
    return this;
  }
  
  @Override
  public void render() {
    background(0);
    fill(255);
    
    final int imgX  = 60;
    final int textX = 90;
    
    textAlign(CENTER, TOP);
    textSize(40);
    text("あそびかた", width / 2, 48);
    
    textAlign(LEFT, TOP);
    textSize(20);
    text(
      "ようしょくじょうへようこそ。\nあなたのしごとはかんたん、ウナギの\nすすむほうこうをかえるだけ。\nエサをてきとうにまくので、おいしく\nながくそだててやってください。\nじぶんのからだにあたると、ウナギは\nからまってしまうのできをつけて。\nあみとつりばりにもごちゅういを。",
      imgX,
      118
    );
    
    textAlign(CENTER, TOP);
    textSize(40);
    text("アイテム", width / 2, 380);
    
    textAlign(LEFT, TOP);
    textSize(20);
    
    image(imgBack, imgX, 450);
    image(imgFeed, imgX, 450);
    text("エビをたべるなんてぜいたくだ\nたくさんたべてよくそだつ", textX, 448);
    
    image(imgBack, imgX, 520);
    image(imgSFeed, imgX, 520);
    text("めずらしいきんいろのエビ\nおいしさのヒケツ", textX, 518);
    
    image(imgBack, imgX, 590);
    image(imgNet, imgX, 590);
    text("ようしょくじょうのあみ\nけっしてにがさない", textX, 588);
    
    image(imgBack, imgX, 660);
    image(imgHook, imgX, 660);
    text("だれかさんのつりばり\nようしょくじょうなのに？", textX, 658);
    
    textAlign(CENTER, BOTTOM);
    textSize(20);
    text("Eキーでタイトルへ", width / 2, height - 30);
  }
}



class SName extends Scene {
  private final int score;
  
  private int cursorX = 0;
  private int cursorY = 0;
  
  private final char[] name = new char[NAME_LENGTH];
  private int nextNameIndex = 0;
  
  SName(int score) {
    this.score = score;
  }
  
  @Override
  public Scene update(VirtualInput vi) {
    if (vi.buttonJustPressed(VirtualInput.OK) && !vi.buttonJustPressed(VirtualInput.CANCEL)) {
      switch (KEYBOARD[cursorY][cursorX]) {
        case '゛':
          if (nextNameIndex > 0) name[nextNameIndex - 1] = DAKUTEN.getOrDefault(name[nextNameIndex - 1], name[nextNameIndex - 1]);
          break;
        case '゜':
          if (nextNameIndex > 0) name[nextNameIndex - 1] = HANDAKUTEN.getOrDefault(name[nextNameIndex - 1], name[nextNameIndex - 1]);
          break;
        case '小':
          if (nextNameIndex > 0) name[nextNameIndex - 1] = SMALL.getOrDefault(name[nextNameIndex - 1], name[nextNameIndex - 1]);
          break;
        case '完':
          if (nextNameIndex == 0) {
            databaseAdd(NAME_DEFAULT, score);
          }
          else {
            final StringBuilder sb = new StringBuilder();
            for (char c : name) {
              if (c == '\0') break;
              sb.append(c);
            }
            databaseAdd(sb.toString(), score);
          }
          return new SRanking();
        case '　':
          break;
        default:
          name[nextNameIndex > NAME_LENGTH - 1 ? NAME_LENGTH - 1 : nextNameIndex] = KEYBOARD[cursorY][cursorX];
          if (++nextNameIndex > NAME_LENGTH) nextNameIndex = NAME_LENGTH;
          break;
      }
    }
    else if (!vi.buttonJustPressed(VirtualInput.OK) && vi.buttonJustPressed(VirtualInput.CANCEL)) {
      if (nextNameIndex > 0) {
        name[nextNameIndex - 1] = '\0';
        nextNameIndex--;
      }
    }
    
    if (vi.buttonJustPressed(VirtualInput.UP) && !vi.buttonJustPressed(VirtualInput.DOWN)) {
      if (--cursorY < 0) cursorY = KEYBOARD_H - 1;
    }
    else if (!vi.buttonJustPressed(VirtualInput.UP) && vi.buttonJustPressed(VirtualInput.DOWN)) {
      if (++cursorY > KEYBOARD_H - 1) cursorY = 0;
    }
    
    if (vi.buttonJustPressed(VirtualInput.LEFT) && !vi.buttonJustPressed(VirtualInput.RIGHT)) {
      if (--cursorX < 0) cursorX = KEYBOARD_W - 1;
    }
    else if (!vi.buttonJustPressed(VirtualInput.LEFT) && vi.buttonJustPressed(VirtualInput.RIGHT)) {
      if (++cursorX > KEYBOARD_W - 1) cursorX = 0;
    }
    
    return this;
  }
  
  public void render() {
    final int textSize = 30;
    final int space = 8;
    final int nameX = (width - ((textSize + space) * NAME_LENGTH - space)) / 2;
    final int keyboardX = (width - ((textSize + space) * KEYBOARD_W - space)) / 2;
    
    fill(255);
    background(0);
    
    textAlign(CENTER, TOP);
    textSize(20);
    text("Eキーでにゅうりょく　Qキーでさくじょ", width / 2, 10);
    
    textAlign(CENTER, TOP);
    textSize(textSize);
    text("せいさんしゃのなまえ", width / 2, 60);
    
    textAlign(LEFT, TOP);
    textSize(textSize);
    for (int i = 0; i < NAME_LENGTH; i++) {
      if (name[i] != '\0') {
        text(name[i], nameX + (textSize + space) * i, 120);
      }
      rect(nameX + (textSize + space) * i, 160, textSize, 4);
    }
    
    rect(
      keyboardX + (textSize + space) * cursorX,
      190 + (textSize + space + 2) * cursorY + textSize + 2,
      textSize,
      4
    );
    
    for (int ix = 0; ix < KEYBOARD_W; ix++) {
      for (int iy = 0; iy < KEYBOARD_H; iy++) {
        if (ix == KEYBOARD_W - 1 && iy == KEYBOARD_H - 1) {
          fill(255, 255 ,0);
        }
        
        text(KEYBOARD[iy][ix], keyboardX + (textSize + space) * ix, 190 + (textSize + space + 2) * iy);
      }
    }
  }
  
  private static final int    NAME_LENGTH  = 10;
  private static final String NAME_DEFAULT = "ななしのせいさんしゃ";
  
  private final char[][] KEYBOARD = {
    { '゛', 'わ', 'ら', 'や', 'ま', 'は', 'な', 'た', 'さ', 'か', 'あ' },
    { '゜', 'を', 'り', '　', 'み', 'ひ', 'に', 'ち', 'し', 'き', 'い' },
    { '小', 'ん', 'る', 'ゆ', 'む', 'ふ', 'ぬ', 'つ', 'す', 'く', 'う' },
    { 'ー', '　', 'れ', '　', 'め', 'へ', 'ね', 'て', 'せ', 'け', 'え' },
    { '！', '　', 'ろ', 'よ', 'も', 'ほ', 'の', 'と', 'そ', 'こ', 'お' },
    { '？', 'ワ', 'ラ', 'ヤ', 'マ', 'ハ', 'ナ', 'タ', 'サ', 'カ', 'ア' },
    { '０', 'ヲ', 'リ', '　', 'ミ', 'ヒ', 'ニ', 'チ', 'シ', 'キ', 'イ' },
    { '１', 'ン', 'ル', 'ユ', 'む', 'フ', 'ヌ', 'ツ', 'ス', 'ク', 'ウ' },
    { '２', '　', 'レ', '　', 'め', 'ヘ', 'ネ', 'テ', 'セ', 'ケ', 'エ' },
    { '３', '　', 'ロ', 'ヨ', 'も', 'ホ', 'ノ', 'ト', 'ソ', 'コ', 'オ' },
    { '４', 'ｑ', 'ｗ', 'ｅ', 'ｒ', 'ｔ', 'ｙ', 'ｕ', 'ｉ', 'ｏ', 'ｐ' },
    { '５', 'ａ', 'ｓ', 'ｄ', 'ｆ', 'ｇ', 'ｈ', 'ｊ', 'ｋ', 'ｌ', '　' },
    { '６', 'ｚ', 'ｘ', 'ｃ', 'ｖ', 'ｂ', 'ｎ', 'ｍ', '　', '　', '　' },
    { '７', 'Ｑ', 'Ｗ', 'Ｅ', 'Ｒ', 'Ｔ', 'Ｙ', 'Ｕ', 'Ｉ', 'Ｏ', 'Ｐ' },
    { '８', 'Ａ', 'Ｓ', 'Ｄ', 'Ｆ', 'Ｇ', 'Ｈ', 'Ｊ', 'Ｋ', 'Ｌ', '　' },
    { '９', 'Ｚ', 'Ⅹ', 'Ｃ', 'Ｖ', 'Ｂ', 'Ｎ', 'Ｍ', '　', '　', '完' }
  };
  private static final int KEYBOARD_W = 11;
  private static final int KEYBOARD_H = 16;
  
  private final Map<Character, Character> DAKUTEN = new HashMap<Character, Character>() {{
    put('か', 'が'); put('き', 'ぎ'); put('く', 'ぐ'); put('け', 'げ'); put('こ', 'ご');
    put('さ', 'ざ'); put('し', 'じ'); put('す', 'ず'); put('せ', 'ぜ'); put('そ', 'ぞ');
    put('た', 'だ'); put('ち', 'ぢ'); put('つ', 'づ'); put('て', 'で'); put('と', 'ど');
    put('は', 'ば'); put('ひ', 'び'); put('ふ', 'ぶ'); put('へ', 'べ'); put('ほ', 'ぼ');
    put('ぱ', 'ば'); put('ぴ', 'び'); put('ぷ', 'ぶ'); put('ぺ', 'べ'); put('ぽ', 'ぼ');
    put('が', 'か'); put('ぎ', 'き'); put('ぐ', 'く'); put('げ', 'け'); put('ご', 'こ');
    put('ざ', 'さ'); put('じ', 'し'); put('ず', 'す'); put('ぜ', 'せ'); put('ぞ', 'そ');
    put('だ', 'た'); put('ぢ', 'ち'); put('づ', 'つ'); put('で', 'て'); put('ど', 'と');
    put('ば', 'は'); put('び', 'ひ'); put('ぶ', 'ふ'); put('べ', 'へ'); put('ぼ', 'ほ');
    put('ウ', 'ヴ');
    put('カ', 'ガ'); put('キ', 'ギ'); put('ク', 'グ'); put('ケ', 'ゲ'); put('コ', 'ゴ');
    put('サ', 'ザ'); put('シ', 'ジ'); put('ス', 'ズ'); put('セ', 'ゼ'); put('ソ', 'ゾ');
    put('タ', 'ダ'); put('チ', 'ヂ'); put('ツ', 'ヅ'); put('テ', 'デ'); put('ト', 'ド');
    put('ハ', 'バ'); put('ヒ', 'ビ'); put('フ', 'ブ'); put('ヘ', 'ベ'); put('ホ', 'ボ');
    put('パ', 'バ'); put('ピ', 'ビ'); put('プ', 'ブ'); put('ペ', 'ベ'); put('ポ', 'ポ');
    put('ガ', 'カ'); put('ギ', 'キ'); put('グ', 'ク'); put('ゲ', 'ケ'); put('ゴ', 'コ');
    put('ザ', 'サ'); put('ジ', 'シ'); put('ズ', 'ス'); put('ゼ', 'セ'); put('ゾ', 'ソ');
    put('ダ', 'タ'); put('ヂ', 'チ'); put('ヅ', 'ツ'); put('デ', 'テ'); put('ド', 'ト');
    put('バ', 'ハ'); put('ビ', 'ヒ'); put('ブ', 'フ'); put('ベ', 'ヘ'); put('ボ', 'ホ');
    put('ヴ', 'ウ');
  }};
  
  private final Map<Character, Character> HANDAKUTEN = new HashMap<Character, Character>() {{
    put('は', 'ぱ'); put('ひ', 'ぴ'); put('ふ', 'ぷ'); put('へ', 'ぺ'); put('ほ', 'ぽ');
    put('ば', 'ぱ'); put('び', 'ぴ'); put('ぶ', 'ぷ'); put('べ', 'ぺ'); put('ぼ', 'ぽ');
    put('ぱ', 'は'); put('ぴ', 'ひ'); put('ぷ', 'ふ'); put('ぺ', 'へ'); put('ぽ', 'ほ');
    put('ハ', 'パ'); put('ヒ', 'ピ'); put('フ', 'プ'); put('ヘ', 'ペ'); put('ホ', 'ポ');
    put('バ', 'パ'); put('ビ', 'ピ'); put('ブ', 'プ'); put('ベ', 'ペ'); put('ボ', 'ポ');
    put('パ', 'ハ'); put('ヒ', 'ヒ'); put('プ', 'フ'); put('ペ', 'ヘ'); put('ポ', 'ホ');
  }};
  
  private final Map<Character, Character> SMALL = new HashMap<Character, Character>() {{
    put('あ', 'ぁ'); put('い', 'ぃ'); put('う', 'ぅ'); put('え', 'ぇ'); put('お', 'ぉ');
    put('つ', 'っ');
    put('や', 'ゃ'); put('ゆ', 'ゅ'); put('よ', 'ょ');
    put('わ', 'ゎ');
    put('ぁ', 'あ'); put('ぃ', 'い'); put('ぅ', 'う'); put('ぇ', 'え'); put('ぉ', 'お');
    put('っ', 'つ');
    put('ゃ', 'や'); put('ゅ', 'ゆ'); put('ょ', 'よ');
    put('ゎ', 'わ');
    put('ア', 'ァ'); put('イ', 'ィ'); put('ウ', 'ゥ'); put('エ', 'ェ'); put('オ', 'ォ');
    put('カ', 'ヵ'); put('ケ', 'ヶ');
    put('ツ', 'ッ');
    put('ヤ', 'ャ'); put('ユ', 'ュ'); put('ヨ', 'ョ');
    put('ワ', 'ヮ');
    put('ァ', 'ア'); put('ィ', 'イ'); put('ゥ', 'ウ'); put('ェ', 'エ'); put('ォ', 'オ');
    put('ヵ', 'カ'); put('ヶ', 'ケ');
    put('ッ', 'ツ');
    put('ャ', 'ヤ'); put('ュ', 'ユ'); put('ョ', 'ヨ');
    put('ヮ', 'ワ');
  }};
}
class SRanking extends Scene {
  private LinkedList<Record> records; /* = new LinkedList<Record>(Arrays.asList(
    new Record("ななしのせいさんしゃ", 400000),
    new Record("a", 0),
    new Record("a", 0),
    new Record("a", 0),
    new Record("a", 0),
    new Record("a", 0),
    new Record("a", 0),
    new Record("a", 0),
    new Record("a", 0),
    new Record("a", 0)
  )); */
  
  private final PImage logo = loadImage("logo/logo-250.png");
  
  @Override
  public Scene update(VirtualInput vi) {
    if (vi.buttonJustPressed(VirtualInput.OK)) {
      return new STitle();
    }
    
    if (records == null) {
      records = databaseLoad();
    }
    
    return this;
  }
  
  @Override
  public void render() {
    fill(255);
    background(0);
    
    image(logo, (width - logo.width) / 2, 30);
    
    textAlign(CENTER, BOTTOM);
    textSize(20);
    text("Eキーでタイトルへ", width / 2, height - 30);
    
    if (records == null) {
      textAlign(CENTER, CENTER);
      textSize(20);
      text("ランキングシステムに\nせつぞくできません", width / 2, height / 2);
      return;
    }
    
    if (records.isEmpty()) {
      textAlign(CENTER, CENTER);
      textSize(20);
      text("ざんねんながらまだだれも\nウナギをそだてていません", width / 2, height / 2);
      return;
    }
    
    final int textSize = 20;
    final int x = 48;
    final int y = 170;
    
    textAlign(LEFT, TOP);
    textSize(textSize);
    int i = 0;
    for (Record record : records) {
      textAlign(LEFT, TOP);
      text((i + 1 < 10 ? "0" : "") + (i + 1) + ".", x, y + (textSize + 10 + textSize + 10) * i);
      text("せいさんしゃ: " + record.name, x + 32, y + (textSize + 10 + textSize + 10) * i);
      text("　　おねだん:", x + 32, y + textSize + 10 + (textSize + 10 + textSize + 10) * i);
      textAlign(RIGHT, TOP);
      text(record.score + " 円", width - x,  y + textSize + 10 + (textSize + 10 + textSize + 10) * i);
      i++;
    }
  }
}
class SResult extends Scene {
  private final int length;
  private final int quality;
  private final int score;
  
  private int count = -1;
  private boolean finish = false;
  
  private final PImage img_bone     = loadImage("sprite/bone.png");
  private final PImage img_kabayaki = loadImage("sprite/kabayaki.png");
  
  private final AudioPlayer sound_drum     = loadAudio("sound/drum-japanese1.mp3");
  private final AudioPlayer sound_register = loadAudio("sound/clearing1.mp3");
  
  SResult(int length, int quality) {
    this.length = length;
    this.quality = quality;
    score = score(length, quality);
  }
  
  @Override
  public Scene update(VirtualInput vi) {
    count += finish ? 0 : 1;
    
    if (finish && vi.buttonJustPressed(VirtualInput.OK)) {
      sound_drum.close();
      sound_register.close();
      
      final LinkedList<Record> records = databaseLoad();
      if (records == null) {
        return new STitle();
      }
      if (records.size() < 10 || score > records.getLast().score) {
        return new SName(score);
      }
      return new STitle();
    }
    
    return this;
  }
  
  @Override
  public void render() {
    if (count == 0) {
      background(0);
      return;
    }
    
    if (count == 15) {
      image(loadImage("sprite/manaita.png"), 48, 48);
      image(loadImage("sprite/sumibi.png"), 112, 48);
      
      sound_drum.play();
      sound_drum.rewind();
      return;
    }
    
    if (count == 30) {
      final int SIZE = 16;
      image(loadImage("sprite/unagi/head-1-x-0.png"), 48 + SIZE / 2, 48 + SIZE / 2 + SIZE * 0);
      image(loadImage("sprite/unagi/neck-1-0-0.png"), 48 + SIZE / 2, 48 + SIZE / 2 + SIZE * 1);
      
      final PImage img_body = loadImage("sprite/unagi/body-x-0-0.png");
      for (int i = 2; i < length - 1; i++) {
        image(img_body, 48 + SIZE / 2, 48 + SIZE / 2 + SIZE * i);
      }
      
      image(loadImage("sprite/unagi/tail-1-0-0.png"), 48 + SIZE / 2, 48 + SIZE / 2 + SIZE * (length - 1));
      
      fill(255);
      textAlign(LEFT, BOTTOM);
      textSize(20);
      
      fill(255);
      final int left = 180;
    
      textAlign(LEFT, BOTTOM);
      textSize(20);
      text("ながさ：", left + 0, 280); text("cm", left + 200, 280);
      text("ランク：", left + 0, 350);
      
      textSize(40);
      text(rankName(quality), left + 100, 354);
      
      textAlign(RIGHT, BOTTOM);
      text(length * CM_PER_UNIT, left + 180, 284);
      
      sound_drum.play();
      sound_drum.rewind();
    }
    
    if (45 <= count && count <= 45 + (length - 3) * 2) {
      if ((count - 45) % 2 == 0) {
        final int SIZE = 16;
        final int i = (count - 45) / 2 + 1;
        image(img_bone, 48 + SIZE / 2, 48 + SIZE / 2 + SIZE * i);
        image(img_kabayaki, 48 + 64, 48 + SIZE / 2 + SIZE * i);
        
        sound_drum.play();
        sound_drum.rewind();
      }
    }
    
    if (count == 45 + (length - 3) * 2 + 15) {
      fill(255);
      textAlign(RIGHT, BOTTOM);
      
      textSize(20);
      text("円", width - 48, 500);
      
      textSize(60);
      text(score, width - 80, 510);
      
      sound_register.play();
      sound_register.rewind();
    }
    
    if (count == 45 + (length - 3) * 2 + 30) {
      fill(255);
      textSize(20);
      textAlign(CENTER, BOTTOM);
      text("Eキーをおしてすすむ", 300, height - 16);
      finish = true;
    }
  }
  
  private int score(int length, int quality) {
    return (length - 2) * (1000 + 50 * quality);
  }
}
public class STitle extends Scene {
  private int     cursor = 0;
  private boolean move = false;
  
  private final PImage logo = loadImage("logo/logo-250.png");
  private final AudioPlayer sound = loadAudio("sound/drum-japanese1.mp3");
  
  @Override
    public Scene update(VirtualInput vi) {
    if (vi.buttonJustPressed(VirtualInput.OK)) {
      sound.close();
      switch (cursor) {
      case CHOICE_GAME: 
        return new SGame();
      case CHOICE_HOWTO: 
        return new SHowto();
      case CHOICE_RANKING: 
        return new SRanking();
      case CHOICE_CREDIT: 
        return new SCredit();
      default: 
        return null;
      }
    }

    final boolean up   = vi.buttonJustPressed(VirtualInput.UP);
    final boolean down = vi.buttonJustPressed(VirtualInput.DOWN);
    if (!(up ^ down)) {
      move = false;
      return this;
    }
    
    move = true;
    
    if (up && --cursor < 0) {
     
      cursor = CHOICES - 1;
    } else if (down && ++cursor > CHOICES - 1) {
     
      cursor = 0;
    }
    return this;
  }
  
  @Override
    public void render() {
    // CODE HERE
    // USE VARIABLE "cursor".
    
    background(0);
    image(logo, (width - logo.width) / 2, 80);
    textSize(TEXTSIZE);
    textAlign(CENTER, CENTER);
    text("ようしょく", width / 2, 420 + (TEXTSIZE + TEXT_SPACE) * CHOICE_GAME);
    text("あそびかた", width / 2, 420 + (TEXTSIZE + TEXT_SPACE) * CHOICE_HOWTO);
    text("ランキング", width / 2, 420 + (TEXTSIZE + TEXT_SPACE) * CHOICE_RANKING);
    text("クレジット", width / 2, 420 + (TEXTSIZE + TEXT_SPACE) * CHOICE_CREDIT);
    
    final int cursorX = 140;
    final int cursorY = 420 + (TEXTSIZE + TEXT_SPACE) * cursor;
    
    triangle(
      cursorX,  cursorY,
      cursorX - TEXTSIZE, cursorY - TEXTSIZE / 2,
      cursorX - TEXTSIZE, cursorY + TEXTSIZE / 2
    );
    
    if (move) {
      sound.play();
      sound.rewind();
    }
  }
  
  private static final int CHOICES = 4;
  private static final int CHOICE_GAME = 0;
  private static final int CHOICE_HOWTO = 1;
  private static final int CHOICE_RANKING = 2;
  private static final int CHOICE_CREDIT = 3;
  
  private static final int TEXTSIZE = 30;
  private static final int TEXT_SPACE = TEXTSIZE;
}
abstract class Scene {
  public abstract Scene update(VirtualInput vi);
  public abstract void  render();
}


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
  public void settings() {  size(480, 848); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "UNAGI" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
