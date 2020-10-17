import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.io.*; 
import java.net.*; 
import http.requests.*; 
import processing.io.*; 
import java.time.*; 
import java.time.format.*; 
import javax.swing.*; 
import java.io.IOException; 
import ddf.minim.*; 
import ddf.minim.analysis.*; 
import ddf.minim.effects.*; 
import ddf.minim.signals.*; 
import ddf.minim.spi.*; 
import ddf.minim.ugens.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Main extends PApplet {

Scene nowScene;
Sound sound;
static int[] result;

public void setup() {
  
  this.surface.setTitle("BBB");
  this.surface.setIcon(loadImage("title_resources/icon.png"));
  setupFonts();
  sound = new Sound();
  nowScene = new TitleScene(sound);
  result = new int [14];
  frameRate(30);
}   

public void draw() {  
  nowScene.update();
  if (nowScene.isFinish()) goNextScene();
}

private void setupFonts() {
  PFont font;  
  font = loadFont("ModiThorson-48.vlw");  
  textFont(font, 48);
}

public void keyPressed() {
  nowScene.keyPressed();
}

public void keyReleased() {
  nowScene.keyReleased();
}

public void goNextScene() {
  switch (nowScene.getClass().getName()) {
    case "Main$TitleScene": nowScene = new CountdownScene(); break;
    case "Main$CountdownScene": nowScene = new GameScene(sound); break;
    case "Main$GameScene": nowScene = new OneCushionScene(); break;
    case "Main$OneCushionScene": nowScene = new ResultScene(sound); break;
    case "Main$ResultScene": nowScene = new TitleScene(sound);  break; 
    default :
      println(""+nowScene.getClass().getName());
    break;	
  }
}
class CountdownScene extends Scene {
  final private int count;

  private int text_size;
  private PImage backImage;
  private PImage backTitle;

  private int backImageAlpha;

  public CountdownScene() {
    super();
    backImage = loadImage("resources/MAIN.png");
    backTitle = loadImage("title_resources/select.png");
    background(0, 255);
    count = 3;
    text_size = 70;
    backImageAlpha = 255;
    noTint();
    image(backImage, 0, 0, width, height);
    tint(255, backImageAlpha);
    image(backTitle, 0, 0, width, height);
  }
  public void update() {
    super.update();
    countdown();
  }

  public void countdown() {
    noTint();
    image(backImage, 0, 0, width, height);
    tint(255, backImageAlpha);
    image(backTitle, 0, 0, width, height);
    textSize(text_size);
    fill(0xffD0FFFF);
    textAlign(CENTER);
    text(count - elapsedTimeS, width/2, height/2);
    if (backImageAlpha >= 3) backImageAlpha -= 3;
    if (elapsedTimeS == count) {
      finishFlag = true;
      return;
    }
  }

  public void keyPressed() {}
  public void keyReleased() {}

  public boolean isFinish() { return finishFlag; }
}




// データベース操作用クラス
public class DataBase {
  protected String dbname;
  protected String url;
  
  /*
  gameに指定する文字列
  test  : テスト用データベース
  pacman: パックマン
  tetris: テトリス
  unagi : UNAGI
  */
  public DataBase(String dbname) {
    this.dbname = dbname;
    this.url = "https://nitnc5j.sakura.ne.jp/" + dbname + "/mysql/query.php";
  }
  
  // クエリ
  public String query(String sql) {
    // 接続チェック
    if (!canConnect()) {
      return null;
    }
    
    // クエリを実行
    PostRequest post = new PostRequest(url);
    
    try {
      post.addData("query", URLEncoder.encode(sql, "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      
    }
    
    post.send();
    return post.getContent();
  }
  
  // 接続できるかチェック
  public boolean canConnect() {
    try {
      URL obj = new URL(url);
      obj.openConnection().getInputStream();
    } catch (Exception e) {
      return false;
    }
    
    return true;
  }
}
class GameScene extends Scene {
  Display disp;   
  Stage stage;
  Input input;
  Sound sound;

  int pre_time;
  int delta_time;  // 前フレームからの経過時間を持つ
  AudioPlayer bgm;  //テトリスBGM

  public GameScene(Sound sound) {   
    super();
    noTint();
    this.sound = sound;
    stage = new Stage(sound);
    disp = new Display(stage);
    input = new InputKey();
    pre_time = 0;
    sound.stopBGM();
    sound.playBGM(0);
  }   

  public void update() {
    super.update();
    sound.stopCheck();
    sound.bgmRoop(0);
    // 時間計測
    delta_time = elapsedTimeMS - pre_time;
    pre_time = elapsedTimeMS;
    //画面の状態
    int screenNum = 1;
    //update 
    input.update(delta_time); 
    disp.update();

      //ゲーム表示
      if (screenNum == 0) {  //スタート画面

      if (disp.startScreen(stage)) screenNum++;
    } else if (screenNum == 1) {  //ゲーム画面
      if (stage.update(input, delta_time)) finishFlag = true;
      if (finishFlag) {
        stage.getResultScore();
      }
      
      disp.drawBackground();
      disp.drawgame(stage);
      disp.showNext();
      disp.showHold();
      disp.drawFallingMino(stage.mino);
      disp.drawScore(stage);
      disp.dispTime(stage);
      disp.dispText(stage);
      disp.dispLevel(stage);
    }
    //キーリセット
    input.clean();
  }

  public void keyPressed() {
    input.checkInput();
  }

  public void keyReleased() {
    input.checkRelease();
  }

  public boolean isFinish() { return finishFlag; }
}
public class Imageview{

    private PImage images[];
    private boolean runSwitch;
    private int scene ;

    Imageview(){
        images = new PImage[5];
        images[0] = loadImage("title_resources/0.png");
        images[1] = loadImage("title_resources/1.png");
        images[2] = loadImage("title_resources/2.png");
        images[3] = loadImage("title_resources/3.png");
        images[4] = loadImage("title_resources/4.png");
        runSwitch = false;
        scene = 0;
    }

    public void goFrontPage() {
        scene += 1;
        if (scene >= images.length) scene = 0;
    }

    public void goBackPage() {
        scene -= 1;
        if (scene < 0) scene = 4;
    }

    public void HowToPlay(){
        if (!runSwitch) return;
        switch (scene) {
            case 0: image(images[0], width/2, 424); break;
            case 1: image(images[1], width/2, 424); break;
            case 2: image(images[2], width/2, 424); break;
            case 3: image(images[3], width/2, 424); break;
            case 4: image(images[4], width/2, 424); break;
        }
    }

    public void pushSwitch() {
        runSwitch = !runSwitch;
    }

    public boolean isRunnning() {
        return runSwitch;
    }
}


// 入力のインターフェイス
public interface InputInterface {
  public abstract boolean right();   // →
  public abstract boolean up();      // ↑
  public abstract boolean left();    // ←
  public abstract boolean down();    // ↓
  public abstract boolean buttonA(); // A
  public abstract boolean buttonB(); // B
  public abstract boolean buttonC(); // C
}

// キーボードからの入力
public class KeyboardInput implements InputInterface {
  public boolean right() {
    return keyPressed && (keyCode == RIGHT || key == 'd');
  }

  public boolean up() {
    return keyPressed && (keyCode == UP || key == 'w');
  }

  public boolean left() {
    return keyPressed && (keyCode == LEFT || key == 'a');
  }

  public boolean down() {
    return keyPressed && (keyCode == DOWN || key == 's');
  }

  public boolean buttonA() {
    return keyPressed && (key == 'z' || keyCode == ENTER || key == ' ' );
  }

  public boolean buttonB() {
    return keyPressed && key == 'x';
  }

  public boolean buttonC() {
    return keyPressed && key == 'c';
  }
}

// アーケードからの入力
public class ArcadeInput implements InputInterface {
  public static final int RIGHT = 18;
  public static final int UP = 4;
  public static final int LEFT = 27;
  public static final int DOWN = 17;
  public static final int ROUND_UP = 22;
  public static final int ROUND_LEFT = 24;
  public static final int ROUND_RIGHT = 23;

  public ArcadeInput() {
    GPIO.pinMode(RIGHT, GPIO.INPUT_PULLUP);
    GPIO.pinMode(UP, GPIO.INPUT_PULLUP);
    GPIO.pinMode(LEFT, GPIO.INPUT_PULLUP);
    GPIO.pinMode(DOWN, GPIO.INPUT_PULLUP);
    GPIO.pinMode(ROUND_UP, GPIO.INPUT_PULLUP);
    GPIO.pinMode(ROUND_LEFT, GPIO.INPUT_PULLUP);
    GPIO.pinMode(ROUND_RIGHT, GPIO.INPUT_PULLUP);
  }

  public boolean right() {
    return GPIO.digitalRead(RIGHT) == GPIO.LOW;
  }

  public boolean up() {
    return GPIO.digitalRead(UP) == GPIO.LOW;
  }

  public boolean left() {
    return GPIO.digitalRead(LEFT) == GPIO.LOW;
  }

  public boolean down() {
    return GPIO.digitalRead(DOWN) == GPIO.LOW;
  }

  public boolean buttonA() {
    return GPIO.digitalRead(ROUND_UP) == GPIO.LOW;
  }

  public boolean buttonB() {
    return GPIO.digitalRead(ROUND_LEFT) == GPIO.LOW;
  }

  public boolean buttonC() {
    return GPIO.digitalRead(ROUND_RIGHT) == GPIO.LOW;
  }
}

// キーボード・アーケード同時対応
public class MixInput implements InputInterface {
  private KeyboardInput keyboardInput = new KeyboardInput();
  private ArcadeInput arcadeInput = new ArcadeInput();

  public boolean right() {
    return keyboardInput.right() || arcadeInput.right();
  }

  public boolean up() {
    return keyboardInput.up() || arcadeInput.up();
  }

  public boolean left() {
    return keyboardInput.left() || arcadeInput.left();
  }

  public boolean down() {
    return keyboardInput.down() || arcadeInput.down();
  }

  public boolean buttonA() {
    return keyboardInput.buttonA() || arcadeInput.buttonA();
  }

  public boolean buttonB() {
    return keyboardInput.buttonB() || arcadeInput.buttonB();
  }

  public boolean buttonC() {
    return keyboardInput.buttonC() || arcadeInput.buttonC();
  }
}

// 入力
public static class Input_title {
  protected static InputInterface inputInterface;

  // 前回の状態
  protected static boolean prevRight = false;
  protected static boolean prevUp = false;
  protected static boolean prevLeft = false;
  protected static boolean prevDown = false;
  protected static boolean prevButtonA = false;
  protected static boolean prevButtonB = false;
  protected static boolean prevButtonC = false;

  public static void setInputInterface(InputInterface inputInterface) {
    Input_title.inputInterface = inputInterface;
    prevRight = false;
    prevUp = false;
    prevLeft = false;
    prevDown = false;
    prevButtonA = false;
    prevButtonB = false;
    prevButtonC = false;
  }

  public static boolean right() {
    return inputInterface.right();
  }

  public static boolean up() {
    return inputInterface.up();
  }

  public static boolean left() {
    return inputInterface.left();
  }

  public static boolean down() {
    return inputInterface.down();
  }

  public static boolean buttonA() {
    return inputInterface.buttonA();
  }

  public static boolean buttonB() {
    return inputInterface.buttonB();
  }

  public static boolean buttonC() {
    return inputInterface.buttonC();
  }

  public static boolean anyButton() {
    return right() || up() || left() || down() || buttonA() || buttonB() || buttonC();
  }

  public static boolean rightPress() {
    if (right()) {
      if (prevRight) {
        return false;
      } else {
        prevRight = true;
        return true;
      }
    } else {
      prevRight = false;
      return false;
    }
  }

  public static boolean upPress() {
    if (up()) {
      if (prevUp) {
        return false;
      } else {
        prevUp = true;
        return true;
      }
    } else {
      prevUp = false;
      return false;
    }
  }

  public static boolean leftPress() {
    if (left()) {
      if (prevLeft) {
        return false;
      } else {
        prevLeft = true;
        return true;
      }
    } else {
      prevLeft = false;
      return false;
    }
  }

  public static boolean downPress() {
    if (down()) {
      if (prevDown) {
        return false;
      } else {
        prevDown = true;
        return true;
      }
    } else {
      prevDown = false;
      return false;
    }
  }

  public static boolean buttonAPress() {
    if (buttonA()) {
      if (prevButtonA) {
        return false;
      } else {
        prevButtonA = true;
        return true;
      }
    } else {
      prevButtonA = false;
      return false;
    }
  }

  public static boolean buttonBPress() {
    if (buttonB()) {
      if (prevButtonB) {
        return false;
      } else {
        prevButtonB = true;
        return true;
      }
    } else {
      prevButtonB = false;
      return false;
    }
  }

  public static boolean buttonCPress() {
    if (buttonC()) {
      if (prevButtonC) {
        return false;
      } else {
        prevButtonC = true;
        return true;
      }
    } else {
      prevButtonC = false;
      return false;
    }
  }

  public static boolean anyButtonPress() {
    return rightPress() || upPress() || leftPress() || downPress() || buttonAPress() || buttonBPress() || buttonCPress();
  }

  public static boolean rightRelease() {
    if (right()) {
      prevRight = true;
      return false;
    } else {
      if (prevRight) {
        prevRight = false;
        return true;
      } else {
        return false;
      }
    }
  }

  public static boolean upRelease() {
    if (up()) {
      prevUp = true;
      return false;
    } else {
      if (prevUp) {
        prevUp = false;
        return true;
      } else {
        return false;
      }
    }
  }

  public static boolean leftRelease() {
    if (left()) {
      prevLeft = true;
      return false;
    } else {
      if (prevLeft) {
        prevLeft = false;
        return true;
      } else {
        return false;
      }
    }
  }

  public static boolean downRelease() {
    if (down()) {
      prevDown = true;
      return false;
    } else {
      if (prevDown) {
        prevDown = false;
        return true;
      } else {
        return false;
      }
    }
  }

  public static boolean buttonARelease() {
    if (buttonA()) {
      prevButtonA = true;
      return false;
    } else {
      if (prevButtonA) {
        prevButtonA = false;
        return true;
      } else {
        return false;
      }
    }
  }

  public static boolean buttonBRelease() {
    if (buttonB()) {
      prevButtonB = true;
      return false;
    } else {
      if (prevButtonB) {
        prevButtonB = false;
        return true;
      } else {
        return false;
      }
    }
  }

  public static boolean buttonCRelease() {
    if (buttonC()) {
      prevButtonC = true;
      return false;
    } else {
      if (prevButtonC) {
        prevButtonC = false;
        return true;
      } else {
        return false;
      }
    }
  }

  public static boolean anyButtonRelease() {
    return rightRelease() || upRelease() || leftRelease() || downRelease() || buttonARelease() || buttonBRelease() || buttonCRelease();
  }
}
public abstract class Mino {
  private int shape[][];      //ブロックの形
  public float nextPointX;  //ネクストのブロック座標
  public float nextPointY;  
  public float holdPointX;  //ホールドのブロック座標
  public float holdPointY;
  public float holdSize;
  public float nextBlockSize; //ネクストのブロックサイズ
  private PImage texture;   //ブロックのテクスチャ
  private int turnImino[][];

  // ミノの左上の座標 stageの配列にそのまま入る
  private int posx, posy;
  private int ghost_y;

  private int id;  //ブロックID

  public abstract void showTexture();

  public Mino(int x, int y) {
    posx = x;
    posy = y;
    ghost_y = 0;
  }

  /*
  回転はstageとミノの状況からcheckMino(), rotateRight(), rotateLeft()をうまく使って実装する
  回転後のshapeは上書きしてもらって構わない
  各種ミノで実装すること
  posxとposyの変更も忘れずに
   */

  //ブロックの回転
  public boolean turnRight(int[][] stage) {
  
    int rotate_shape[][] = rotateRight();
    boolean SspinFlag = false;
    
      if (checkMino(stage, rotate_shape, 0, 0)) {
        shape = rotate_shape;
        return true;
      } else
      {
        if(id == 1){
        SspinFlag = superTSpin(stage,rotate_shape,posx,posy,false);
        }
        else if(id ==2){
        chengeImino(checkImino(rotate_shape));
        posy+=1;
       if(turnCheck(stage,turnImino)==true)
        return true;
        posy-=1;
        }
        
        if(SspinFlag == true)return true;
        return turnCheck(stage,rotate_shape);
      }
    
  }

  public boolean turnLeft(int[][] stage) {
    int rotate_shape[][] = rotateLeft();
    boolean SspinFlag = false;
    
    
    if (checkMino(stage, rotate_shape, 0, 0)) {
      shape = rotate_shape;
      
      return true;
    } else {
      if(id == 1){
        SspinFlag = superTSpin(stage,rotate_shape,posx,posy,true);
      } 
      else if(id ==2){
        chengeImino(checkImino(rotate_shape));
        posy +=1;
        if(turnCheck(stage,turnImino)==true)
        return true;
        posy -=1;
      }
      if(SspinFlag == true)return true;
      return turnCheck(stage,rotate_shape);
    }

  }
  //回転判定部分(まとめただけ)
  public boolean turnCheck(int[][] stage,int[][] rotate_shape)
  {
    
    for (int mpos = 1; mpos < 3; mpos += 1)
        {
          if (checkMino(stage, rotate_shape, -mpos, mpos)) {
            posx -= mpos;
            posy += mpos;
            shape = rotate_shape;
            return true;
          }
          else if (checkMino(stage, rotate_shape, mpos, mpos)) {
            posx += mpos;
            posy += mpos;
            shape = rotate_shape;
            return true;
          }
          else if (checkMino(stage, rotate_shape, mpos, 0)) {
            posx += mpos;
            shape = rotate_shape;
            return true;
          }
          else if (checkMino(stage, rotate_shape, -mpos, 0)) {
            posx -= mpos;
            shape = rotate_shape;
            return true;
          }
          else if (checkMino(stage, rotate_shape, 0, -mpos)) {
            posy -= mpos;
            shape = rotate_shape;
            return true;
          }
          else if (checkMino(stage, rotate_shape, mpos, -mpos)) {
            posx += mpos;
            posy -= mpos;
            shape = rotate_shape;
            return true;
          }
          else if (checkMino(stage, rotate_shape, -mpos, -mpos)) {
            posx -= mpos;
            posy -= mpos;
            shape = rotate_shape;
            return true;
          }
          else if (checkMino(stage, rotate_shape, -mpos, mpos)) {
            posx -= mpos;
            posy += mpos;
            shape = rotate_shape;
            return true;
          }
          else if (checkMino(stage, rotate_shape, mpos, mpos)) {
            posx += mpos;
            posy += mpos;
            shape = rotate_shape;
            return true;
          }
        }
        return false;
  }
  // 現在の位置から+(dx, dy)ずれた位置にミノが存在できるかを判定する
  public boolean checkMino(int[][] stage, int[][] shape, int dx, int dy) {
    for (int y = 0; y < 5; y++) {
      for (int x = 0; x < 5; x++) {
        if (shape[y][x] != 0) { 
          int check_x = x + posx + dx;
          int check_y = y + posy + dy;
          // インデックスがstage[][]からはみ出さないか監視
          if (check_x < 0 || check_x >= stage[0].length || check_y < 0 || check_y >= stage.length) {
            return false;
          }
          if (stage[check_y][check_x] != 0) {
            return false;
          }
        }
      }
    }
    return true;
  }

  public boolean checkMino(int[][] stage, int dx, int dy) {
    return checkMino(stage, shape, dx, dy);
  }

  // 落ちられたらtrue、落ちれなかったらfalse
  public boolean fall(int[][] stage) {
    if (checkMino(stage, 0, 1)) {
      posy++; 
      return true;
    }
    return false;
  }

  //ブロック移動
  // 移動出来たらtrue, だめならfalse
  public boolean moveRight(int[][] stage) {
    if (checkMino(stage, 1, 0)) {
      posx++;
      return true;
    }
    return false;
  }

  
  public boolean moveLeft(int[][] stage) {
    if (checkMino(stage, -1, 0)) {
      posx--; 
      return true;
    }
    return false;
  }

  // ゴーストの位置を作成
  public void setGhost(int[][] stage) {
    for (int y = posy; checkMino(stage, 0, y - posy); y++) {
      ghost_y = y;
    }
  }

  /*
   ミノを回転させる
   この関数を使ってturnLeft、turnRightをつくる
   回転軸が異なるミノはオーバーライドすること
   */
  public int[][] rotateRight() { 
    int[][] rotation = new int[5][5];
        // 回転行列(Iminoだけ特殊にする)
    if(id == 2){
       for (int y = 1; y < 5; y++) {
          for (int x = 1; x < 5; x++) {
            rotation[y][x] = shape[-(x - 2) + 3][y];
          }
       }
       if(rotation[3][1] == 2&&rotation[3][2] == 2&&rotation[3][3] == 2&&rotation[3][4] == 0)rotation[3][4]=2;
    }else{
      for (int y = 0; y < 5; y++) {
        for (int x = 0; x < 5; x++) {
          rotation[y][x] = shape[-(x - 2) + 2][y];
        }
      }
    }

    return rotation;
  }

  public int[][] rotateLeft() { 
    int[][] rotation = new int[5][5];
    
    // 回転行列
    
    if(id == 2){
      for (int y = 1; y < 5; y++) {
        for (int x = 1; x < 5; x++) {
          rotation[y][x] = shape[x][-(y - 2) + 3];
        }
      }
      if(rotation[3][1] == 2&&rotation[3][2] == 2&&rotation[3][3] == 2&&rotation[3][4] == 0)rotation[3][4]=2;
    }else{
      for (int y = 0; y < 5; y++) {
        for (int x = 0; x < 5; x++) {
          rotation[y][x] = shape[x][-(y - 2) + 2];
        }
      }
    }

    return rotation;
  }
  
  public boolean superTSpin(int[][] stage,int[][] rotate_shape,int posx,int posy,boolean RLFlag){
    return false;
  }
  private int checkImino(int[][] rotate_shape)
  {
    if(rotate_shape[1][3]==2)
    {
      
      return 2;
    }
    if(rotate_shape[4][3]==2)
    {
      return 3;
    }
    if(rotate_shape[1][2]==2)
    {
      return 4;
    }
    if(rotate_shape[2][4]==2)
    {
      return 1;
    }
    return 0;
  }
  private void chengeImino(int num)
  {
    int moveI[][];
    if(num==2)
    {
      moveI = new int[][] {
      {0, 0, 0, 0, 0}, 
      {0, 0, 2, 0, 0}, 
      {0, 0, 2, 0, 0}, 
      {0, 0, 2, 0, 0}, 
      {0, 0, 2, 0, 0}};
    }
    else if(num==3)
    {
      moveI = new int[][] {
      {0, 0, 0, 0, 0}, 
      {0, 0, 0, 0, 0}, 
      {2, 2, 2, 2, 0}, 
      {0, 0, 0, 0, 0}, 
      {0, 0, 0, 0, 0}};
    }
    else if(num==4)
    {
      moveI = new int[][] {
      {0, 0, 2, 0, 0}, 
      {0, 0, 2, 0, 0}, 
      {0, 0, 2, 0, 0}, 
      {0, 0, 2, 0, 0}, 
      {0, 0, 0, 0, 0}};
    }
    else
    {
      moveI = new int[][] {
      {0, 0, 0, 0, 0}, 
      {0, 0, 0, 0, 0}, 
      {0, 2, 2, 2, 2}, 
      {0, 0, 0, 0, 0}, 
      {0, 0, 0, 0, 0}};
    }
    turnImino=moveI;
  }
  
}
class OneCushionScene extends Scene {
  final private int count;

  
  private PImage backImage;
  private float backAlpha;

  public OneCushionScene() {
    super();
    fill(0xffD0FFFF);
    textAlign(CENTER);
    text("Game Over", width/2, height/2);
    count = 2;
    backAlpha = 0.0f;
    backImage = loadImage("resources/MAIN.jpg");
    
  }
  public void update() {
    super.update();
    countdown();
    
  }

  public void countdown() {
    if (backAlpha <= 255) backAlpha += 0.5f;
   tint(255, (int)backAlpha);
   image(backImage, 0, 0, width, height);
   fill(0xffD0FFFF);
    textAlign(CENTER);
    text("Game Over", width/2, height/2);
    if (elapsedTimeS == count) {
      finishFlag = true;
      return;
    }
  }

  public void keyPressed() {}
  public void keyReleased() {}

  public boolean isFinish() { return finishFlag; }
}




class ResultScene extends Scene {
  PImage ui_img;       // 画面背景
  PImage a;
  PImage text_img[];
  
  float textLineX;
  float textLineY;
  final int TIME =12;
  final float INTERVAL = 13 + 23; 
  final float TEXTLINEX = 40; 
  final float TEXTLINEY = 120; 
  String dispResult[];
  int textAlpha[];
  int textAlphaNum;
  boolean dispFinishFlag = false;

  private Sound sound;
  private int startTime;

  public ResultScene(Sound sound) {
    text_img = new PImage[13];
    dispResult = new String[result.length];
    textAlpha = new int[result.length];
    Input_title.setInputInterface(new KeyboardInput()); // キーボード
    textAlphaNum = 0;
    this.sound = sound;
    sound.stopBGM();
    sound.playBGM(1);
    ui_img = loadImage("score/back.png");
    text_img[0] = loadImage("score/lenman.png");
    text_img[1] = loadImage("score/single.png");
    text_img[2] = loadImage("score/double.png");
    text_img[3] = loadImage("score/triple.png");
    text_img[4] = loadImage("score/tetris.png");
    text_img[5] = loadImage("score/tspin.png");
    text_img[6] = loadImage("score/tspinSingle.png");
    text_img[7] = loadImage("score/tspinDouble.png");
    text_img[8] = loadImage("score/tspinTriple.png");
    text_img[9] = loadImage("score/allclear.png");
    text_img[10] = loadImage("score/level.png");
    text_img[11] = loadImage("score/lines.png");
    text_img[12] = loadImage("score/playtime.png");
    textLineX = TEXTLINEX;
    textLineY = TEXTLINEY;
    changeTimeFormat();
    for(int i = 0;i < result.length;i++) {
      if(i != TIME)  dispResult[i] = String.valueOf(result[i]);
    }
    
    startTime = millis();
    
  }
  public void update() {
    //スコア登録、update()の初めにないとおかしい挙動になることがある
    if(Input_title.buttonA()){
      if(dispFinishFlag){ 
        if(millis() - startTime >= 300) settingScoreDB();
      }
    }
    
    drawBackground();
    dispText();
    sound.stopCheck();
    sound.bgmRoop(1);
    
    if (millis() - startTime >= 30000)
      exit();
  }

  private void drawBackground() {  
    image(ui_img, 0, 0, width, height);
  }
  
  private void dispText(){
    if(textAlpha[textAlphaNum] >= 255 && textAlphaNum < result.length -1) textAlphaNum++;
    
    if(textAlphaNum < result.length && textAlpha[textAlphaNum] <= 255)  textAlpha[textAlphaNum] +=21;
    
    if(Input_title.buttonA()){
      for(int i = 0;i < textAlpha.length;i++){
        textAlpha[i] = 255;
        dispFinishFlag = true;
      }
    }

    //描画
    textLineX = TEXTLINEX;
    textLineY = TEXTLINEY;
    for(int i = 0;i < text_img.length;i++){
      if(i == 10) textLineY += INTERVAL - 10;
      dispAText(i,textLineX,textLineY,text_img[i].width,text_img[i].height);
      dispBText(i,width -textLineX,textLineY+13,text_img[i].width,text_img[i].height,255);
      textLineY += INTERVAL;
    }
    dispScore();
  }
  
  
  private void dispAText(int img,float pointX,float pointY,float sizeWidth,float sizeHeight){
    tint(255,255,255,textAlpha[img]);
    image(text_img[img], pointX, pointY, sizeWidth, sizeHeight);
    noTint();
  }
  
  private void dispBText(int img,float pointX,float pointY,float sizeWidth,float sizeHeight,float alpha){
    textAlign(RIGHT);
    fill(0xff00ffff,textAlpha[img]);
    textSize(25);
    text(dispResult[img],pointX,pointY);
  }
  
  private void dispScore(){
    textAlign(CENTER);
    fill(0xff00ffff,textAlpha[result.length-1]);
    textSize(30);
    text(dispResult[result.length-1],width / 2,height - 142);
  }
  
  private void changeTimeFormat(){
    int time = result[TIME];    //time
    int minute = time / 60;
    int seconds = time % 60;
    String cMinute = String.valueOf(minute);
    String cSeconds = String.valueOf(seconds);
    if (cSeconds.length() == 1) cSeconds = "0"+ cSeconds;
    dispResult[TIME] = cMinute + ":" + cSeconds;
  }

  public void keyPressed() {}
  public void keyReleased() {
  }
  
  public boolean isFinish() { return finishFlag; }
  
  public void settingScoreDB(){
  int score = Integer.parseInt(dispResult[result.length-1]);
  
  // 接続先DBは"test", "pacman", "tetris", "unagi"から指定
  DataBase db = new DataBase("tetris");
  
    // 接続できるかチェック 
  if (!db.canConnect()) {
    //finishFlag = true; // タイトルへ戻る処理など
      int goTitle = JOptionPane.showConfirmDialog(null, 
      "ネットワークに繋がっていません。タイトル画面へ戻りますか？",
      "確認", 
      JOptionPane.YES_NO_OPTION, 
      JOptionPane.QUESTION_MESSAGE);
      
      if(goTitle == JOptionPane.YES_NO_OPTION){
        finishFlag = true;
        return;
      } else if (goTitle == JOptionPane.NO_OPTION){
        return;
    }
  }
  
  // Yes/Noダイアログを表示
  int regist = JOptionPane.showConfirmDialog(null, "ランキングに登録しますか？", "確認", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
  
  // Yesが選択されたら
  if (regist == JOptionPane.YES_OPTION) {
    // 入力ダイアログを表示
    String name = JOptionPane.showInputDialog(null, "名前を入力してください");
    
    // 取り消しが押されたら
    if (name == null) {
      finishFlag = true; // タイトルへ戻る処理など
    }
    
    // 日時を取得
    LocalDateTime ldt = LocalDateTime.now();
    String datetime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(ldt);
    
    if("".equals(name)) name = "名無しのBBBer";
    
    if(name != null) db.query("INSERT INTO ranking VALUES ('" + name + "', " + score + ", '" + datetime + "')");
    
    JOptionPane.showMessageDialog(frame, "スコアが送信されました。Webサイトで順位が確認できます。(Readme参照)", "送信メッセージ", JOptionPane.PLAIN_MESSAGE);
    
  // Noが選択されたら
  } else if (regist == JOptionPane.NO_OPTION) {
    finishFlag = true; // タイトルへ戻る処理など
  }
  
  finishFlag = true;
  }
  
}
class Scene {
  Sound sound;
  boolean finishFlag;
  private int startTimeMS;
  protected int elapsedTimeS;
  protected int elapsedTimeMS;
  Scene() {
    finishFlag = false;
    elapsedTimeMS = 0;
    startTimeMS = millis();
    imageMode(CORNER);
    rectMode(CORNER);
  }
  public void update() {
    elapsedTimeMS = millis() - startTimeMS;
    elapsedTimeS = elapsedTimeMS / 1000;
  }
  public void keyPressed() {}
  public void keyReleased() {}
  public boolean isFinish() { return finishFlag; }
}


class TitleScene extends Scene {
  int select = -1;                   // 選択しているゲーム
  int state = -1;                    // how to playの切り替え用
  int count = 0;
  boolean selectFlag = false;
  boolean keyReleasedFlag = false;

  PImage back;   // 背景
  PImage start; 
  PImage how;
  PImage test;

  //Imageview view;

  final private int ENTER_GAME = 1;
  final private int HOW_TO_PLAY = 2;
  
  Imageview view;

  private Sound sound;

  public TitleScene(Sound sound) {
    super();
    view = new Imageview();
    this.sound = sound;
    Input_title.setInputInterface(new KeyboardInput()); // キーボード
    back = loadImage("title_resources/select.png");
    start = loadImage("title_resources/start.png");
    how = loadImage("title_resources/how.png");
    test = loadImage("title_resources/test.png");
    this.sound.playBGM(1);
  }

  public void update() {
    super.update();
    sound.bgmRoop(1);
    if (Input_title.upPress()) {
      if (!view.isRunnning()) {
        if (select == -1)
          select = HOW_TO_PLAY;
        else
          select = 3 - select;
      }
    }

    if (Input_title.downPress()) {
      if (!view.isRunnning()) {
        if (select == -1)
          select = ENTER_GAME;
        else
          select = 3 - select;
      }
    }

    //HOW TO PLAYが押された場合
    if(select == HOW_TO_PLAY && Input_title.buttonA()) {
      selectFlag = true;
    }

    if(selectFlag){
      state = 0;
      if(keyReleasedFlag && Input_title.buttonA()){
        state = -1;
        selectFlag = false;
        keyReleasedFlag = false;
      }
    }

    imageMode(CENTER);
    image(back, width/2,height/2);

    if (select == ENTER_GAME) {
      fill(41,171,226,80);
    } else {
      noFill();
    }
    oval(width/2, 393, 210, 30);

    if (select == HOW_TO_PLAY) {
      fill(41,171,226, 80);
    } else {
      noFill();
    }
    oval(width/2, 492, 210, 30);
  
    image(start, width / 2, 394);
    image(how, width / 2, 493);  

    view.HowToPlay();
  }
  
  public void keyPressed() {
    super.keyPressed();

    switch (select) {
      case ENTER_GAME:
        if (Input_title.buttonA()) {
          finishFlag = true;
          sound.endingBgm();
        }
        break;
      case HOW_TO_PLAY:
        if (Input_title.buttonA()) view.pushSwitch();
        if (Input_title.right()) view.goFrontPage();
        else if (Input_title.left()) view.goBackPage();
        break;
    }
  }

  public void keyReleased() {
    super.keyReleased();
  }

  public void oval(float x, float y, float w, float h) {
    rectMode(CENTER);
    stroke(41,171,226, 80);
    strokeWeight(1.5f);

    rect(x, y, w, h, 15);
  }

  public boolean isFinish() { return finishFlag; }
}
class ViewedText {
  private int startTime;
  private String text;
  ViewedText(String text) {
    startTime = millis();
    this.text = text;
  }
  public boolean isFinish() {
    if (millis() - startTime < 3000) return false;
    else return true;
  }
  public String getText() {
    return this.text;
  }
}
class Display {

  int sSarray_x;       // 横配列
  int sSarray_y;       // 縦配列
  float arst_y;
  PImage ui_img;       // 画面背景
  PImage minoTex[];    // ステージに設置されたミノ描画用のテクスチャ
  String score;//スコア
  
  private final int STAGESIZE_Y=19;  // 縦ブロック数(ゲーム高さ) //*
  private final int STAGESIZE_X=10;  // 横ブロック数(ゲーム幅) //*
  private final float BLOCKSIZE=25;  // ブロックの大きさ (変更する際はMinoclassのも変更)
  private final float BLOCKRADIUS = 5;
  private final float STAGEPOSITION_X = (229)/2;  //プレイ画面の位置
  private final float STAGEPOSITION_Y = 373 / 2;
  private final float NEXTPOINTINREVAL = 45; //次のブロックの表示位置の差
  private final float COLLECTNEXT_X = 20;          //ネクストブロック座標補正(次のブロック以外のネクストの位置を変えるため)
  private final float COLLECTNEXT_Y = 20;          
  private final int MINO_COLOR = 0xffD6FFFC;
  private final int TEXT_COLOR = 0xff00FFFF;
  private ArrayList<ViewedText> t_a_s_text = new ArrayList();

  boolean tetris_flag = false;
  int tetris_disp_start_time = 0;
  
  boolean allClearFlag = false;
  int allClear_disp_start_time = 0;
  
  boolean tSpinFlag = false;
  int tSpin_disp_start_time = 0;
  
  int tLine = 0;
  
  boolean lenFlag = false;
  int len_disp_start_time = 0;
  int len = 0;

  Stage stage;
  Mino dispNextMino[];
  Mino holdMino;
  Mino minos[];

  public Display(Stage stage) {    
    //テクスチャ設定
    this.stage = stage;
    ui_img = loadImage("resources/MAIN.jpg");
    minoTex = new PImage[7];
    
    minos = new Mino[7];
    minos[0] = new TMino(0, 0);
    minos[1] = new IMino(0, 0);
    minos[2] = new JMino(0, 0);
    minos[3] = new LMino(0, 0);
    minos[4] = new SMino(0, 0);
    minos[5] = new ZMino(0, 0);
    minos[6] = new OMino(0, 0);
    
    for(int i = 0; i < 7; i++){
      minoTex[i] = minos[i].texture;
    }

    sSarray_x = stage.stage[0].length;    //横配列
    sSarray_y = stage.stage.length;  //縦配列

    arst_y = sSarray_y-STAGESIZE_Y-1;   //配列とブロック数の差
    dispNextMino =new Mino[4];
    score = String.valueOf(stage.getScore());
  }

  public void update() {
    stage.getNext(dispNextMino);
    holdMino = stage.getHoldMino(holdMino);
    score = String.valueOf(stage.score);
  }
  //ゴースト絵画
  public void showGhost(int x,int y,Mino mino) {
    if (mino.posy < mino.ghost_y) {
      fill(210, 100);
      noStroke();
      rect(STAGEPOSITION_X + BLOCKSIZE * (x + mino.posx - 1), STAGEPOSITION_Y + BLOCKSIZE * (y + mino.ghost_y - arst_y), BLOCKSIZE, BLOCKSIZE);
    }
  }


  
  //ネクスト表示
  public void showNext() {
    float collectRadius = 1;
    for (int next = 0; next < 4; next++) {
      translate(0, NEXTPOINTINREVAL);

      translate(dispNextMino[next].nextPointX, dispNextMino[next].nextPointY);
      if (next >= 1) { //2個前
        collectRadius = 1.5f;
        dispNextMino[next].nextBlockSize -=5;       
        translate(COLLECTNEXT_X, COLLECTNEXT_Y);
      } else {  //1個前
      }
      for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 5; j++) {
          if (dispNextMino[next].shape[i][j] >= 1) {
            stroke(MINO_COLOR);
            fill(255,100);
            rect(dispNextMino[next].nextBlockSize* j, dispNextMino[next].nextBlockSize * i, dispNextMino[next].nextBlockSize, dispNextMino[next].nextBlockSize, BLOCKRADIUS / collectRadius);
            //image(minoTex[dispNextMino[next].id - 1], dispNextMino[next].nextBlockSize* j, dispNextMino[next].nextBlockSize * i, dispNextMino[next].nextBlockSize, dispNextMino[next].nextBlockSize);
          }
        }
      }
      if (next >= 1) {
        translate(-COLLECTNEXT_X, -COLLECTNEXT_Y);
        dispNextMino[next].nextBlockSize +=5;
      }

      translate(-dispNextMino[next].nextPointX, -dispNextMino[next].nextPointY);
    }
    //元に戻す

    translate(0, NEXTPOINTINREVAL*(-4));
  }
  
  //ホールド表示
  public void showHold() {
    if (holdMino != null) {
      translate(holdMino.holdPointX, holdMino.holdPointY);
      for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 5; j++) {
          if (minos[holdMino.id - 1].shape[i][j] >= 1) {
            stroke(MINO_COLOR);
            fill(255,100);
            rect(holdMino.holdSize* j, holdMino.holdSize * i, holdMino.holdSize, holdMino.holdSize, BLOCKRADIUS);
            //image(minoTex[minos[holdMino.id - 1].id - 1], holdMino.holdSize* j, holdMino.holdSize * i, holdMino.holdSize, holdMino.holdSize);
          }
        }
      }
      //元に戻す
      translate(-holdMino.holdPointX, -holdMino.holdPointY);
    }
  }

  //背景
  public void drawBackground() {  
    image(ui_img, 0, 0, width, height);
  }

    // ステージに設置されているブロックを描画
  public void drawgame(Stage stage) {  //ゲームプレイ画面
    fill(200,200,255,50);  
    for (int i = (int)arst_y; i < sSarray_y; i++) {
      for (int j = 0; j < sSarray_x; j++) {
        if (stage.stage[i][j] == 0) {
          fill(200,200,255,50);
          noStroke();
        } else if (stage.stage[i][j] > 0) {
          stroke(MINO_COLOR);
          rect(STAGEPOSITION_X + BLOCKSIZE * (j - 1), STAGEPOSITION_Y + BLOCKSIZE * (i-arst_y), BLOCKSIZE, BLOCKSIZE, BLOCKRADIUS);
        }
      }
    }
  }

  // 落下中のミノ描画
  public void drawFallingMino(Mino mino) {
    for (int y = 0; y < 5; y++) {
      for (int x = 0; x < 5; x++) {
        if (mino.shape[y][x] != 0) {
          // ミノの影
          showGhost(x,y,mino);
          // ミノの本体
          stroke(MINO_COLOR);
          fill(0,50);
          rect(STAGEPOSITION_X + BLOCKSIZE * (x + mino.posx - 1), STAGEPOSITION_Y + BLOCKSIZE * (y + mino.posy - arst_y), BLOCKSIZE, BLOCKSIZE, BLOCKRADIUS);
        }
      }
    }
  }

  //スタート画面の表示
  public boolean startScreen(Stage stage){
    //ここにスタート画面を表示するコードを書く終了したらtrueにする
    return false;
  }
  
  //scoreを描画
  public void drawScore(Stage stage){
    textSize(35);
    fill(TEXT_COLOR);
    textAlign(RIGHT);
    text(score, 350, 139);
  }
  
    //Tetrisなどの文字を描画
  public void dispText(Stage stage) {
    int tempTLine = 0;
    int tempLen = 0;
    
    if (stage.checkTetris()) {
      tetris_disp_start_time = millis();
      tetris_flag = true;
    }
    
    if (stage.getAllClearFlag()) {
      allClear_disp_start_time = millis();
      allClearFlag = true;
    }
    
    if (stage.checkTSpinFlag()){
      tSpin_disp_start_time = millis();
      tSpinFlag = true;
    }
    
    if ((tempLen = stage.getLenCount()) != len){
      len = tempLen;
      if (len != 0 || len != 1) len_disp_start_time = millis();
    }
    
    if((tempTLine = stage.getClearLine()) != 0){
      tLine = tempTLine;    
    } else tLine = 0;
    
    if (tetris_flag) {
      tetris_flag = false;
      t_a_s_text.add(0, new ViewedText("TETRIS"));
    }
    
    if (allClearFlag) {
      allClearFlag = false;
      t_a_s_text.add(0, new ViewedText("ALL CLEAR"));
    }

    if ((millis() - len_disp_start_time <= 3000)) {
      textSize(25);
      fill(255);
      if (!(len >= 0 && len <= 1))  text("Ren", 80, 500);
      if (!(len >= 0 && len <= 1))  text(len - 1, 80, 530);
    }

       
    if(tSpinFlag){
      t_a_s_text.add(0, new ViewedText("Tspin")); 
      switch(tLine){
        case 1:
          t_a_s_text.add(1, new ViewedText("Single"));
          break;
        case 2:
          t_a_s_text.add(1, new ViewedText("Double"));
          break;
        case 3:
          t_a_s_text.add(1, new ViewedText("Triple"));
          break;
        default :
          
        break;	
      }
      tSpinFlag = false;
    }

    for (int i = 0; i < t_a_s_text.size(); ++i) {
      if (t_a_s_text.get(i).isFinish()) {
        if (t_a_s_text.get(i).getText() == "Tspin" && tLine != 0) {
          t_a_s_text.remove(i+1);
        }
        t_a_s_text.remove(i);
      }
    }

    textView();
  }

  private void textView() {
    for (int i = 0; i < t_a_s_text.size(); i++) {
      textSize(25);
      fill(255);
      textAlign(CENTER);
      text(t_a_s_text.get(i).getText(), width * 0.5f, 250 + 30 * i);
    }
  }
  
  public void dispLevel(Stage stage){
    int level = stage.getLevel();
    textSize(55);
    textAlign(RIGHT);
    fill(TEXT_COLOR);
    text(level, 325, 755);
  }
  
  public void dispTime(Stage stage){
    int time = stage.getTime();
    String min = String.valueOf(time / 60);
    String sec = String.valueOf(time % 60);
    if(sec.length() == 1) sec = "0" + sec; 
    String gametime = min + " : " + sec;
    textSize(16);
    textAlign(RIGHT);
    fill(TEXT_COLOR);
    text(gametime, 79, 365);
  }
}
public class IMino extends Mino {

  public IMino(int x, int y) {
    super(x, y);
    super.nextPointX = 385;
    super.nextPointY = 140;
    super.holdPointX = 6;                //ホールド座標X
    super.holdPointY = 185;               //ホールド座標Y
    super.holdSize = 15;
    super.nextBlockSize = 15;
    super.shape = new int[][] {
      {0, 0, 0, 0, 0}, 
      {0, 0, 0 , 0, 0}, 
      {0, 2, 2, 2, 2}, 
      {0, 0, 0, 0, 0}, 
      {0, 0, 0, 0, 0}};
    super.id = 2;
  }

  public void showTexture() {
  }

  public void turnMino(int turn) {
  }
  
  public void superSpin(){}
  
}
abstract class Input {

  public final int R_MOVE = 0; 
  public final int L_MOVE = 1;
  public final int R_TURN = 2;
  public final int L_TURN = 3;
  public final int HOLD   = 4;
  public final int S_DROP = 5;
  public final int H_DROP = 6;

  private final int INPUT_DELAY = 200;            // 次の入力を受け付けるまでの時間(ms)
  private final int MOVE_FAST_INPUT_DELAY = 20;   // 左右移動の速いほうの待ち時間

  public boolean state[];                          // 7種類のキーの状態 ON or OFF
  public boolean preStateMoveR;
  public boolean preStateMoveL;

  public int moveCount;                        // 連続で動いた回数

  protected  boolean keyState[];                   // 押されているキー
  private int wait[];                              // 7種類のキーの待ち時間
  private int waitMove;                            // moveとturnは左右でセットなので同じ時間を参照する
  private int waitTurn;

  // この２つのメソッドをコントローラーに合わせてオーバーライドすればいい
  public abstract void checkInput();
  public abstract void checkRelease();

  public Input() {
    state =  new boolean[7];
    keyState = new boolean[7];
    wait = new int[7];
    waitMove = 0;
    waitTurn = 0;
    preStateMoveR = false;
    preStateMoveL = false;
    moveCount = 0;
  }

  // draw()の最初で呼ぶ
  public void update(int delta_time) {

    // ・　・・・の処理
    int moveDelay;

    if (moveCount >= 2) {
      moveDelay = MOVE_FAST_INPUT_DELAY;
    } else {
      moveDelay = INPUT_DELAY;
    }

    if (!keyState[R_MOVE] && preStateMoveR) {
      moveCount = 0;
    }
    if (!keyState[L_MOVE] && preStateMoveL) {
      moveCount = 0;
    }

    preStateMoveR = keyState[R_MOVE];
    preStateMoveL = keyState[L_MOVE];

    if (waitMove >= moveDelay) {
      if (!keyState[R_MOVE] || !keyState[L_MOVE]) {
        // 右移動
        if (keyState[R_MOVE]) {
          state[L_MOVE] = false;
          state[R_MOVE] = true;
          moveCount++;
          waitMove = 0;
        }

        // 左移動
        if (keyState[L_MOVE]) {
          state[R_MOVE] = false;
          state[L_MOVE] = true;
          moveCount++;
          waitMove = 0;
        }
      }
    }

    // 右回転
    // 連続入力なし
    if (keyState[R_TURN]) {
      if (wait[R_TURN] >= INPUT_DELAY && waitTurn >= INPUT_DELAY) {
        state[R_TURN] = true;
        wait[R_TURN] = 0;
        waitTurn = 0;
      }
    } else {
      wait[R_TURN] = INPUT_DELAY;
    }

    // 左回転
    // 連続入力なし
    if (keyState[L_TURN]) {
      if (wait[L_TURN] >= INPUT_DELAY && waitTurn >= INPUT_DELAY) {
        state[L_TURN] = true;
        wait[L_TURN] = 0;
        waitTurn = 0;
      }
    } else {
      wait[L_TURN] = INPUT_DELAY;
    }

    // ホールド
    if (keyState[HOLD]) {
      if (wait[HOLD] >= INPUT_DELAY) {
        state[HOLD] = true;
        wait[HOLD] = 0;
      }
    }

    // ソフトドロップ
    if (keyState[S_DROP]) {
      state[S_DROP] = true;
    }

    // ハードドロップ
    // 連続入力なし
    if (keyState[H_DROP]) {
      if (wait[H_DROP] >= INPUT_DELAY) {
        state[H_DROP] = true;
        wait[H_DROP] = 0;
      }
    } else {
      wait[H_DROP] = INPUT_DELAY;
    }

    // 待ち時間を加算する
    for (int i = 0; i < 7; i++) {
      if (i == H_DROP || i == R_TURN || i == L_TURN) { // 連続入力をしないキーはここで飛ばす
        continue;
      }
      wait[i] += delta_time;
    }
    waitTurn += delta_time;
    waitMove += delta_time;
  }

  // draw()の最後で呼ぶ
  public void clean() {
    for (int i = 0; i < 7; i++) {
      state[i] = false;
    }
  }
}
class InputButton extends Input {
 
  public InputButton()
  {
   GPIO.pinMode(ArcadeInput.UP, GPIO.INPUT_PULLUP);  // UP
   GPIO.pinMode(ArcadeInput.DOWN, GPIO.INPUT_PULLUP); // Down
   GPIO.pinMode(ArcadeInput.RIGHT, GPIO.INPUT_PULLUP); // Right
   GPIO.pinMode(ArcadeInput.LEFT, GPIO.INPUT_PULLUP); // Left
   GPIO.pinMode(ArcadeInput.ROUND_UP, GPIO.INPUT_PULLUP); // RoundButton Up
   GPIO.pinMode(ArcadeInput.ROUND_RIGHT, GPIO.INPUT_PULLUP); // RoundButton Right
   GPIO.pinMode(ArcadeInput.ROUND_LEFT, GPIO.INPUT_PULLUP); // RoundButton Left
  }
  
  public void update(int delta_time)
  {
    // ---------- GPIO ----------------
    if (GPIO.digitalRead(ArcadeInput.UP) == GPIO.LOW) {
      keyState[H_DROP] = true;
    } else {
      keyState[H_DROP] = false;
    }
    if (GPIO.digitalRead(ArcadeInput.DOWN) == GPIO.LOW) {
      keyState[S_DROP] = true;
    } else {
      keyState[S_DROP] = false;
    }
    if (GPIO.digitalRead(ArcadeInput.RIGHT) == GPIO.LOW) {
      keyState[R_MOVE] = true;
    } else {
      keyState[R_MOVE] = false;
    }
    if (GPIO.digitalRead(ArcadeInput.LEFT) == GPIO.LOW) {
      keyState[L_MOVE] = true;
    } else {
      keyState[L_MOVE] = false;
    }
    if (GPIO.digitalRead(ArcadeInput.ROUND_UP) == GPIO.LOW) {
      keyState[HOLD] = true;
    } else {
      keyState[HOLD] = false;
    }
    if (GPIO.digitalRead(ArcadeInput.ROUND_RIGHT) == GPIO.LOW) {
      keyState[R_TURN] = true;
    } else {
      keyState[R_TURN] = false;
    }
    if (GPIO.digitalRead(ArcadeInput.ROUND_LEFT) == GPIO.LOW) {
      keyState[L_TURN] = true;
    } else {
      keyState[L_TURN] = false;
    }
    
    super.update(delta_time);
  }
  
  public void checkInput() {
    if (key == 'd') {
      keyState[R_MOVE] = true;
    }
    if (key == 'a') {
      keyState[L_MOVE] = true;
    }
    if (key == 'w') {
      keyState[H_DROP] = true;
    }
    if (key == 's') {
      keyState[S_DROP] = true;
    }
    if (key == ' ') {
      keyState[HOLD] = true;
    }
    if (key == 'l') {
      keyState[R_TURN] = true;
    }
    if (key == 'j') {
      keyState[L_TURN] = true;
    }
  }
  
  public void checkRelease() {
    if (key == 'd') {
      keyState[R_MOVE] = false;
    }
    if (key == 'a') {
      keyState[L_MOVE] = false;
    }
    if (key == 'w') {
      keyState[H_DROP] = false;
    }
    if (key == 's') {
      keyState[S_DROP] = false;
    }
    if (key == ' ') {
      keyState[HOLD] = false;
    }
    if (key == 'l') {
      keyState[R_TURN] = false;
    }
    if (key == 'j') {
      keyState[L_TURN] = false;
    }
  }
  
}
class InputKey extends Input {

  InputKey() {
   super();
 }
  
  public void update(int delta_time)
  { 
    super.update(delta_time);
  }
  
  public void checkInput() {
    if (key == 'd') {
      keyState[R_MOVE] = true;
    }
    if (key == 'a') {
      keyState[L_MOVE] = true;
    }
    if (key == 'w') {
      keyState[H_DROP] = true;
    }
    if (key == 's') {
      keyState[S_DROP] = true;
    }
    if (key == ' ') {
      keyState[HOLD] = true;
    }
    if (key == 'k') {
      keyState[R_TURN] = true;
    }
    if (key == 'j') {
      keyState[L_TURN] = true;
    }
  }

  public void checkRelease() {
    if (key == 'd') {
      keyState[R_MOVE] = false;
    }
    if (key == 'a') {
      keyState[L_MOVE] = false;
    }
    if (key == 'w') {
      keyState[H_DROP] = false;
    }
    if (key == 's') {
      keyState[S_DROP] = false;
    }
    if (key == ' ') {
      keyState[HOLD] = false;
    }
    if (key == 'k') {
      keyState[R_TURN] = false;
    }
    if (key == 'j') {
      keyState[L_TURN] = false;
    }
  }
}
public class JMino extends Mino {

  public JMino(int x, int y) {
    super(x, y);
    super.nextPointX = 390;
    super.nextPointY = 140;
    super.holdPointX = 15;                //ホールド座標X 
    super.holdPointY = 190;               //ホールド座標Y
    super.holdSize = 15;
    super.nextBlockSize = 15;
    super.shape = new int[][] {
      {0, 0, 0, 0, 0}, 
      {0, 3, 0, 0, 0}, 
      {0, 3, 3, 3, 0}, 
      {0, 0, 0, 0, 0}, 
      {0, 0, 0, 0, 0}};

    super.id = 3;
  }

  public void showTexture() {
  }
  public void turnMino(int turn) {
  }
  
  public void superSpin(){}
}
public class LMino extends Mino {

  public LMino(int x, int y) {
    super(x, y);
    super.nextPointX = 390;
    super.nextPointY = 140;
    super.holdPointX = 15;                //ホールド座標X 
    super.holdPointY = 190;               //ホールド座標Y
    super.holdSize = 15;
    super.nextBlockSize = 15;
    super.shape = new int[][] {
      {0, 0, 0, 0, 0}, 
      {0, 0, 0, 4, 0}, 
      {0, 4, 4, 4, 0}, 
      {0, 0, 0, 0, 0}, 
      {0, 0, 0, 0, 0}};

    super.id = 4;
  }

  public void showTexture() {
  }

  public void turnMino(int turn) {
  }
  
  public void superSpin(){}
}
public class OMino extends Mino {

  public OMino(int x, int y) {
    super(x, y);
    super.nextPointX = 385;
    super.nextPointY = 140;
    super.holdPointX = 8;                //ホールド座標X 
    super.holdPointY = 193;               //ホールド座標Y
    super.holdSize = 14.5f;
    super.nextBlockSize = 15;
    super.shape = new int[][] {
      {0, 0, 0, 0, 0}, 
      {0, 0, 7, 7, 0}, 
      {0, 0, 7, 7, 0}, 
      {0, 0, 0, 0, 0}, 
      {0, 0, 0, 0, 0}};

    super.id = 7;
  }

  public void showTexture() {
  }
  
  public void superSpin(){}

  public void turnMino(int turn) {
  }
  //oは回転させない
  public int[][] rotateRight() { 
    return super.shape;
  }

  public int[][] rotateLeft() { 
    return super.shape;
  }
}
public class RandomMino {

  private int nextMino1[] = new int[7];
  private int nextMino2[] = new int[14];
  private boolean firstFlag = false;
  private int q_point = 0;

  private void randomMino() {  //  次にくるブロックの生成
    int flag[] = new int[7];
    int rand;
    for (int i = 0; i < 7; i++) {
      while (true) {
        rand = (int)random(1, 8);
        if (flag[rand - 1] != 1) {
          flag[rand - 1] = 1;
          break;
        }
      }
      nextMino1[i] = rand;
    }
  }

  private void nextMino() {  //生成したブロックを格納
    if (firstFlag == false) {
      randomMino();
      for (int i = 0; i < 7; i++) {
        nextMino2[i] = nextMino1[i];
      }
      randomMino();
      for (int i = 0; i < 7; i++) {
        nextMino2[i+7] = nextMino1[i];
      }
      firstFlag = true;
    } else {

      if (q_point == 8) {
        randomMino();
        for (int i = 0; i < 7; i++) {
          nextMino2[i] = nextMino1[i];
        }
      } else {
        if (q_point == 0);
        randomMino();
        for (int i = 0; i < 7; i++) {
          nextMino2[i+7] = nextMino1[i];
        }
      }
    }
  }

  public int getNextMino() {  //格納したブロックを取り出す(取り出すと次に変わる)
    int next;
    if (q_point == 14)  q_point = 0;
    if (q_point == 0 || q_point == 8) nextMino();
    next = nextMino2[q_point];

    q_point++;
    return (next);
  }
}
public class SMino extends Mino {

  public SMino(int x, int y) {
    super(x, y);
    super.nextPointX = 390;
    super.nextPointY = 140;
    super.holdPointX = 15;                //ホールド座標X 
    super.holdPointY = 190;               //ホールド座標Y
    super.holdSize = 15;
    super.nextBlockSize = 15;
    super.shape = new int[][] {
      {0, 0, 0, 0, 0}, 
      {0, 0, 5, 5, 0}, 
      {0, 5, 5, 0, 0}, 
      {0, 0, 0, 0, 0}, 
      {0, 0, 0, 0, 0}};

    super.id = 5;
  }

  public void superSpin(){}
  
  public void showTexture() {
  }

  public void turnMino(int turn) {
  }
}






Minim minim = new Minim(this);

final int TITLESCENE = 0;
final int GAMESCENE  = 1;
final int RESULTSCENE = 2;
  
public class Sound{
  private AudioPlayer bgm,titleResultBGM;  //テトリスBGM
  private AudioPlayer tetris, aline, twoLine, drop, soft, tSpin1, tSpin2, tSpin3, hold;
  
  private AudioPlayer preSE, nowSE;
  
  private AudioPlayer sounds[];
  
  Sound(/*int sceneNum*/){
    setupSounds(/*sceneNum*/);
  }
  
  private void setupSounds(/*int secneNum*/){
     //<>//
    bgm    = minim.loadFile("sounds/bgm2.mp3");
    titleResultBGM = minim.loadFile("sounds/title_result_BGM.mp3"); //<>//
    
    tetris = minim.loadFile("sounds/tetris1.mp3"); //<>//
    aline  = minim.loadFile("sounds/aLine.mp3");
    twoLine  = minim.loadFile("sounds/twoLine.mp3");
    drop   = minim.loadFile("sounds/drop.mp3");
    soft   = minim.loadFile("sounds/soft.mp3");
    tSpin1 = minim.loadFile("sounds/tSpin1.mp3");
    tSpin2 = minim.loadFile("sounds/tSpin2.mp3");
    tSpin3 = minim.loadFile("sounds/tSpin3.mp3");
    hold   = minim.loadFile("sounds/hold.mp3");

    
    sounds = new AudioPlayer[] {
      tetris,
      aline,
      twoLine,
      drop,
      soft,
      tSpin1,
      tSpin2,
      tSpin3,
      hold
    };
    
    preSE = tetris;
  }

  public void playBGM(int a) { //t
    if(a == 0){
      if(!bgm.isPlaying()) bgm.rewind();
      bgm.play();
    }else if(a == 1){
      if(!titleResultBGM.isPlaying()) titleResultBGM.rewind();
      titleResultBGM.play();
    }
    
  }
  
  public void stopAllSounds() {
    bgm.close();
    tetris.close();
    aline.close();
    twoLine.close();
    drop.close();
    soft.close();
    titleResultBGM.close();
    hold.close();
  }
  
  public void playSE(String soundName) {
    switch(soundName) {
      case "tetris": tetris.play(); nowSE = tetris; break;
      case "aline" : aline.play();  nowSE = aline;  break;
      case "twoLine" : twoLine.play();  nowSE = twoLine;  break;
      case "drop"  : drop.play();   nowSE = drop;   break;
      case "soft"  : soft.play();   nowSE = soft;   break;
      case "tSpin1": tSpin1.play(); nowSE = tSpin1; break;
      case "tSpin2": tSpin2.play(); nowSE = tSpin2; break;
      case "tSpin3": tSpin3.play(); nowSE = tSpin3; break;
      case "hold"  : hold.play();   nowSE = hold;   break;
    }
    for (AudioPlayer se: sounds) {
      if(se == nowSE) se.rewind();
    }
  }
  
  public void stopBGM(){
    bgm.pause();
    titleResultBGM.pause();
  }
  
  public void stopBgm(){
  }
  
  public void endingBgm() {
  }
  
  public void stopCheck() {
  }
  
  public void bgmRoop() {
  }
  
  public void bgmRoop(int bgmNum){
    if(bgmNum == 0){
      if(!bgm.isPlaying()) bgm.loop();
    }
    if(bgmNum == 1){
      if(!titleResultBGM.isPlaying()) titleResultBGM.loop();
    }
  }
}
class Stage {

  private int score;

  private int waitFall;       
  private int lastInputTime;  // 最後の入力からの経過時間
  private int gameTime;    //ゲームの残り時間(秒)
  private int gameLimitTime; //リミットタイム
  private int startTime;     // ゲーム開始時の時間
  private int level;
  private boolean gameFinishFlag;

  private boolean isGround;   // ミノが接地しているか
  private int minoFreeTime;   // 地面に接している間にミノが自由に動ける時間
  private boolean doneHold;   // ホールドを使ったか
  private int fall_time;     //落下間隔時間
  private boolean fallMinoFlag;
  private int clearLineNum;
  private int lenCount;
  private int lastline;
  private boolean allClearFlag;
  private boolean dispAllClearFlag;
  private boolean firstGroundFlag;
  
  private boolean line1;  //スコア関連フラグ
  private boolean line2;
  private boolean line3;
  private boolean line4;
  private boolean tetrisFlag;
  private boolean tSpinFlag;
  private int dispClearLine;
  
  private int oneLineScore = 100; //加算するスコア(変えてください)

  private Mino nextMino[];  

  private final int FIRST_X = 3;  // ミノの生成位置
  private final int FIRST_Y = 3;
  private final int NORMAL_FALL_TIME = 1000; //自然落下間隔時間
  private final int SOFT_FALL_TIME = 40;  //強制落下間隔時間
  private final int FREE_TIME = 4000;   // 接地後に最大何ms動かせるか
  private final int INPUT_WAIT = 1000;  // 最後の入力から何ms待つか(カサカサ)
  private final int CLEAR_LINE_NUM = 10000; //issue #6

  //result変数
  private int lines;
  private int lineSingle;
  private int lineDouble;
  private int lineTriple;
  private int tetris;
  private int maxLen;
  private int tSpin;
  private int tSpinS;
  private int tSpinD;
  private int tSpinT;
  private int allClear;


  RandomMino next;
  private Mino mino;
  private Mino holdMino;
  int[][] stage;
  private Sound sound;

  public Stage(Sound sound) {
    this.sound = sound;

    next = new RandomMino();  // ミノ生成
    mino = getNewMino(next.getNextMino());  // 最初のミノを生成
    nextMino =new Mino[4];
    for (int i = 0; i < 4; i++) nextMino[i]=getNewMino(next.getNextMino());  // Nextの4つのミノを生成
    allClearFlag = false;
    dispAllClearFlag = false;
    firstGroundFlag = false;
    holdMino = null;
    isGround = false;
    waitFall = 0;
    fall_time = NORMAL_FALL_TIME;
    minoFreeTime = 0;
    lastInputTime = 0;
    clearLineNum = 0;
    gameLimitTime = 300;
    gameTime = 0;
    startTime = millis();
    level = 1;
    gameFinishFlag = false;
    doneHold = false;
    fallMinoFlag = false;
    
    line1 = false;
    line2 = false;
    line3 = false;
    line4 = false;
    tetrisFlag = false;
    tSpinFlag = false;
    lenCount = 0;
    lastline = 0;
    dispClearLine = 0;

    lines = 0;
    lineSingle = 0;
    lineDouble = 0;
    lineTriple = 0;
    tetris = 0;
    maxLen = 0;
    tSpin = 0;
    tSpinS = 0;
    tSpinD = 0;
    tSpinT = 0;
    allClear = 0;
    score = 0;
    
    stage = new int[][] { 
      {-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1}, 
      {-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1}, 
      {-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1}, 
      {-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1}, //3,3
      {-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1}, //<=ここからミノ生成 (6,5)を回転軸に ここから下が表示される
      {-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1}, 
      {-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1}, 
      {-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1}, 
      {-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1}, 
      {-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1}, 
      {-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1}, 
      {-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1}, 
      {-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1}, 
      {-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1}, 
      {-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1}, 
      {-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1}, 
      {-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1}, 
      {-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1},
      {-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1}, 
      {-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1}, 
      {-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1}, 
      {-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1}, 
      {-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1},
      {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1}};
  }

  // このメソッドをdraw()で毎フレーム呼ぶ
  public boolean update(Input input, int delta_time) {

    decrementTime();//時間を減らす
    
    // 操作されたか（カサカサ用）
    boolean wasOperate = false;

    wasOperate = move(input,wasOperate);  
    
    wasOperate = rotation(input, wasOperate);

    if (input.state[input.HOLD]) {          // ホールド
      hold();
    }

    mino.setGhost(stage);    // ゴーストの位置設定

    // Down Mino //
    waitFall += delta_time;
    
    if (waitFall >= fall_time) {
      isGround = !mino.fall(stage);  // 落下と接地判定
      waitFall = 0;
      if (!isGround) {
        fallMinoFlag = false;
      }
    }
    //

    // "kasakasa" and check ground Mino //
    if (isGround) {
      minoFreeTime += delta_time;

      if (wasOperate) {
        lastInputTime = 0;
      } else {
        lastInputTime += delta_time;
      }
    //

      // ミノの位置が決まった
      if (minoFreeTime >= FREE_TIME || lastInputTime >= INPUT_WAIT) {
        ground();
      }
    }

    if (gameFinishFlag) sound.stopBgm();
    return gameFinishFlag;
  }

  private boolean move(Input input,boolean wasOp){
    boolean wasOperate = wasOp;
    // キーと操作の対応はclass InputKeyを参照されたし
    if (input.state[input.R_MOVE]) {        // 右移動
      wasOperate = mino.moveRight(stage);
    }

    if (input.state[input.L_MOVE]) {        // 左移動
      wasOperate = mino.moveLeft(stage);
    }
    
    if (input.state[input.H_DROP]) {        // ハードドロップ
      mino.posy = mino.ghost_y;
      minoFreeTime = FREE_TIME;
      isGround = true;
    }
    
    if (input.state[input.S_DROP]) {        // ソフトドロップ
      fall_time = SOFT_FALL_TIME - ((level - 1) * 100);
      fallMinoFlag = true;
    } else {
      fall_time = NORMAL_FALL_TIME - ((level - 1) * 100);
    }
    return wasOperate;
  }
  
  private boolean rotation(Input input,boolean wasOp){
    boolean wasOperate = wasOp;
      if (input.state[input.R_TURN]) {        // 右回転
        wasOperate = mino.turnRight(stage);
        // 浮かび上がったときの処理
        boolean preIsGround = isGround;
        isGround = !mino.checkMino(stage, 0, 1);
        if (preIsGround && isGround) {
          waitFall = 0;
        }
      }

    if (input.state[input.L_TURN]) {        // 左回転
      wasOperate = mino.turnLeft(stage);
      // 浮かび上がったときの処理
      boolean preIsGround = isGround;
      isGround = !mino.checkMino(stage, 0, 1);
      if (preIsGround && isGround) {
        waitFall = 0;
      }
    }
    return wasOperate;
  }
  
  private void ground(){
    tSpinFlag = checkTSpin(stage,mino.posx,mino.posy,mino.shape);
    // ラインチェックと次のミノの処理
    stageSetMino(mino);      // stage[][]にミノのブロックを反映
    gameFinishFlag = gameOver();
    clearLineNum += checkline(mino.posy);    // ラインチェック
    clearLineNum = gameClear(clearLineNum);//issue #6
    firstGroundFlag = true;
    if(!gameFinishFlag)allClearFlag = checkAllClear();
    if(allClearFlag == true) dispAllClearFlag = true;
    
    if(!gameFinishFlag)addScore(lenCount);            // 得点か三
    lenCount(clearLineNum);        // れん
    setNextMino();         // 次のミノを取り出す
    levelUp();
    
    if (allClearFlag) sound.playSE("allclear");
    else {
      if(line4) sound.playSE("tetris");
      else if (line3) {
        if (tSpinFlag == true) sound.playSE("tSpin3");
        else sound.playSE("twoLine");
      }
      else if (line2) {
        if (tSpinFlag == true) sound.playSE("tSpin2");
        else sound.playSE("twoLine");
      }
      else if (line1) {
        if (tSpinFlag == true) sound.playSE("tSpin1");
        else sound.playSE("aline");
      }
      else sound.playSE("drop");
    }

    doneHold = false; 
    isGround = false;
    minoFreeTime = 0;
    lastInputTime = 0;
    waitFall = 0;
    countResultScore();
    downFlag();
  }
  
  // 新しいミノのインスタンスを返す
  // idは17の間
  private Mino getNewMino(int id) {
    Mino nmino = null;

    switch(id) {
    case 1 : 
      nmino = new TMino(FIRST_X, FIRST_Y);
      break;
    case 2 :
      nmino = new IMino(FIRST_X, FIRST_Y);
      break;
    case 3 :
      nmino = new JMino(FIRST_X, FIRST_Y);
      break;
    case 4 :
      nmino = new LMino(FIRST_X, FIRST_Y);
      break;
    case 5 :
      nmino = new SMino(FIRST_X, FIRST_Y);
      break;
    case 6 :
      nmino = new ZMino(FIRST_X, FIRST_Y);
      break;
    case 7 :
      nmino = new OMino(FIRST_X, FIRST_Y);
      break;
    }
    return nmino;
  }

  // 次のミノをminoに代入する
  public void setNextMino() {
    mino = nextMino[0];
    for (int i = 0; i < 3; i++) {
      nextMino[i] = nextMino[i + 1];
    }
    nextMino[3] = getNewMino(next.getNextMino());
  }

  // ホールドの処理
  public void hold() {
    if (!doneHold) {           // 既にホールドを使っていないかのチェック
      sound.playSE("hold");
      minoFreeTime = 0;        // 各種変数の再設定
      lastInputTime = 0;
      waitFall = 0;
      doneHold = true;
      if (holdMino == null) {  // ホールドにミノがないとき
        holdMino = mino;
        setNextMino();
      } else {                 // ホールドにミノがあるときr
        Mino tmp = mino;
        mino = getNewMino(holdMino.id);
        holdMino = tmp;
      }
    }
  }

  public Mino getHoldMino(Mino holdMino2) {
    holdMino2 = holdMino;
    return holdMino2;
  }

  // ミノをstage[][]にセットする
  public void stageSetMino(Mino mino) {
    for (int y = 0; y < 5; y++) {
      for (int x = 0; x < 5; x++) {
        if (mino.shape[y][x] != 0) {
          stage[y + mino.posy][x + mino.posx] = mino.shape[y][x];
        }
      }
    }
  }

  // int cy : ミノの位置  
  // cyを基準にしてブロックを走査
  public int checkline(int cy) {
    int flag = 0;
    int blockCount = 0;    // 1行にあるブロックの数のバッファ
    int clearY = 0;
    int checknum = 4;    
    int clear = 0;

    for (int i = checknum; i >= 0; i -= 1) {    // 最大4行消えるから?
      flag = 0;
      blockCount = 0;
      for (int j = 1; j <= 10 && flag == 0; j += 1) {
        blockCount += 1;
        if ((cy - i + checknum)>=23)break;      // stage[][]を縦にはみ出さないように
        if (stage[cy - i+4][j] == -1)
          break;
        else if (stage[cy - i+ checknum ][j] == 0)
          flag = 1;
        if (blockCount == 10) {
          clearY = cy - i + checknum;
        }
      }
      if (flag == 0 && blockCount == 10) {
        clear += 1;
        for (int j = 1; j <= 10; j += 1) {
          stage[clearY][j] = 0;
        }
        for (int ci = clearY; ci > 0; ci -= 1) {
          if (stage[ci][1] == -1) break;
          for (int cj = 1; cj <= 10; cj += 1) {
            stage[ci][cj] = stage[ci - 1][cj];
          }
        }
      }

      blockCount = 0;
    }
    if(clear == 1)
      line1 = true;
    else if(clear == 2)
      line2 = true;
    else if(clear == 3)
      line3 = true;
    else if(clear == 4)
      line4 = true;
      
      onDispFlag();
      
    return clear;
  }

  public void addScore(int lenNum) {//得点加算 値は適当に決めたので変更してください
    int len = 0;
    int lenBonus = 0;
    byte tSpinBonus = 1;
    if(lenNum>=2)
    {
      len = lenNum-1;
    }
    
    if(tSpinFlag)  tSpinBonus = 2;
    if (len == 0)          lenBonus = 0;
    else if(len < 4)       lenBonus = 50;
    else if(len < 8)       lenBonus = 100;
    else if(len < 12)      lenBonus = 150;
    else if(len < 16)      lenBonus = 200;
    else                   lenBonus = 250;
    if(line1 == true)
    {
      score += (int)(oneLineScore * tSpinBonus + lenBonus); 
    }
    else if(line2 == true)
    {
      score += (int)(oneLineScore * tSpinBonus* 2 +lenBonus); 
    }
    else if(line3 == true)
    {
      score += (int)(oneLineScore * tSpinBonus * 3 + lenBonus); 
    }
    else if(line4 == true)
    {
      score += (int)(oneLineScore* 4 + lenBonus); 
    }
    
    if(allClearFlag) score+=1000;
  }

  public int gameClear(int clear) {//issue #6//issue #6 発生原因箇所
    if (clear >= CLEAR_LINE_NUM)
    {
      for (int y = 0; y < 23; y += 1)
      {
        for (int x = 1; x <= 10; x+= 1)
        {  
          stage[y][x] = 0;
        }
      }
      holdMino = null;
      doneHold = false;
      lenCount = 0;
      lastline = 0;
      score = 0;
      return 0;
    }
    return clear;
  }

  public boolean gameOver() {
    boolean gameOverFlag = false;
    //画面外にミノがあるか探す
    for (int y = 0; y < 4; y += 1)
    {
      for (int x = 4; x <= 7; x += 1)
      {  
        if (stage[y][x] != 0)
        {
          gameOverFlag = true;
          break;
        }
      }
    }
    //ミノが生成される場所にミノがあるか探す
    if (gameOverFlag == false)
    {
      for (int x = 4; x <= 6; x+= 1)
      {  
        if (stage[5][x] != 0&&nextMino[0].id == 1)
        {
          gameOverFlag = true;
          break;
        }
        if (stage[5][x] != 0||stage[4][x] != 0)
        {
          gameOverFlag = true;
          break;
        }
      }
      if (stage[5][7] != 0 && nextMino[0].id == 2)
      {
        gameOverFlag = true;
      }
    }
    //処理内容　盤面削除､ホールド初期化
    if (gameOverFlag == true)
    {      
      holdMino = null;
      doneHold = false;
      lenCount = 0;
      lastline = 0;
    }
    return gameOverFlag;
  }
  


  public void getNext(Mino dispNextMino[]) {
    for (int i = 0; i < 4; i++) {
      dispNextMino[i] = nextMino[i];
    }
  }

  public void lenCount(int count) {// れん加算
    if(lastline != count )
    {
      lenCount += 1;
    }
    else
    {
      lenCount = 0;
      
    }
    lastline = count;
  }

  /**
   * 
   * せり上がりs 
   *
   **/
  public void addLine() {
  }
  public void downFlag(){
    line1 = false;
    line2 = false;
    line3 = false;
    line4 = false;
    allClearFlag = false;
  }
  
  public boolean checkAllClear(){  
    if (!firstGroundFlag) return false; 
    for(int i = 1;i < 11; i++){
      if(stage[22][i] >= 1) return false;
    }
    return true;
  }

  public int getScore() {
    return score;
  }
  
  public int getTime(){
    return gameTime;
  }
  
  private void decrementTime(){
    int ms = (millis() - startTime) / 1000;
    gameTime = gameLimitTime - ms;
    if(gameTime <= 0) gameFinishFlag = true;
  }
  
  public boolean checkTetris(){
    if(tetrisFlag == true) {
      tetrisFlag = false;
      return true;
    }
    return false;
  }
  
  public boolean checkTSpin(int[][] stage,int posx,int posy,int[][] mino){
    if(this.mino.id != 1) return false;
    
    boolean CP1 = false;
    boolean CP2 = false; 
    boolean CP3 = false;
    boolean CP4 = false;
    if(stage[posy+1][posx+1] != 0) CP1 = true;//LEFTUP
    if(stage[posy+1][posx+3] != 0) CP2 = true;//RIGHTUP
    if(stage[posy+3][posx+1] != 0) CP3 = true;//LEFTDOWN
    if(stage[posy+3][posx+3] != 0) CP4 = true;//RIGHTDOWN
    
    int tRo = 0;
    //Tmino direction
    if(mino[3][2] == 0 )tRo = 1; //UP
    else if(mino[2][1] == 0 )tRo = 2; //RIGHT
    else if(mino[2][3] == 0 )tRo = 3; //LEFT
    else if(mino[1][2] == 0 )tRo = 4;  //DOWN
    if(CP3 &&CP4){
      if(tRo==1) 
      {
        if(CP1 || CP2) { 
          if((stage[posy+2][posx+0] != 0) && (stage[posy+2][posx+4] != 0)){
             return true; 
          }
        }
      }
      else if(tRo==2) 
      { 
        if(CP2) {  
          return true; 
        } 
      } 
      else if(tRo==3) 
      { 
        if(CP1) {  
          return true; 
        } 
      } 
      else if(tRo==4) 
      { 
        if(CP1 || CP2) {  
          return true; 
        } 
      } 
    }
    
    if(CP1 && CP2){
      if(tRo == 2){
        if(CP3){
          return true; 
        }
      }
      else if(tRo == 3){
        if(CP4){
          return true; 
        }
      }
    }
    return false; 
  } 
   
  private void levelUp(){
    if (score < 500) level = 1;
    else if (score < 800) level = 2;
    else if (score < 1500) level = 3;
    else if (score < 2300) level = 4;
    else if (score < 3200) level = 5;
    else if (score < 4500) level = 6;
    else if (score < 7500) level = 7;
    else if (score < 10000) level = 8;
    else level = 9;
  }
  
  public int getLevel(){
    return level;
  }
  
  public boolean checkTSpinFlag(){
    if(tSpinFlag == true) {
      tSpinFlag = false;
      return true;
    }
    return false;
  }
  
  public boolean getAllClearFlag(){
    if(dispAllClearFlag){
      dispAllClearFlag = false;
      return true;
    }
    return false;
  }
  
  
  public int getClearLine(){
    int line;
    line = dispClearLine;
    dispClearLine = 0;
    return line;
  }
  
  public int getLenCount(){
    return lenCount;
  }
  
  private void onDispFlag(){
    if(line1 == true) dispClearLine = 1;
    if(line2 == true) dispClearLine = 2;
    if(line3 == true) dispClearLine = 3;
    if(line4 == true) tetrisFlag = true;
  }

  public void countResultScore(){
    if(!tSpinFlag){
      if(line1) {
        lineSingle += 1;
        lines += 1;
      }else
      if(line2) {
        lineDouble += 1;
        lines += 2;
      }else
      if(line3) {
        lineTriple += 1;
        lines += 3;
      }else
      if(line4) {
      tetris += 1;
      lines += 4;
      }
    }else{
      if(line1) {
        tSpinS += 1;
        lines += 1;
      }else
      if(line2) {
        tSpinD += 1;
        lines += 2;
      } else
      if(line3) {
        tSpinT += 1;
        lines += 3;
      }else{
        tSpin += 1;
      }
    }

    if(allClearFlag)  allClear += 1;
    if(maxLen < (lenCount - 1)) maxLen = lenCount - 1;
  }
  public void getResultScore(){
    int[] tempResult;
    tempResult = new int[]{maxLen,lineSingle,lineDouble,lineTriple,tetris
    ,tSpin,tSpinS,tSpinD,tSpinT,allClear,level,lines,gameLimitTime-gameTime,score};
    System.arraycopy(tempResult,0,result,0,tempResult.length);
  }
  
}
public class TMino extends Mino {

  private int tSpinTriple[][] = {
    {0, 0, 1, -2, -2}, 
    {0, 0, 0, -2, -2}, 
    {-2, 1, 0, -2, -2}, 
    {-2, 0, 0, -2, -2}, 
    {-2, -2, 0, -2, -2}};

  public TMino(int x, int y) {
    super(x, y);
    super.nextPointX = 390;
    super.nextPointY = 140;
    super.holdPointX = 15;                //ホールド座標X 
    super.holdPointY = 190;               //ホールド座標Y
    super.holdSize = 15;
    super.nextBlockSize = 15;
    super.shape = new int[][] {
      {0, 0, 0, 0, 0}, 
      {0, 0, 1, 0, 0}, 
      {0, 1, 1, 1, 0}, 
      {0, 0, 0, 0, 0}, 
      {0, 0, 0, 0, 0}
    };

    super.id = 1;
  }

  public boolean tSpinFlag(int[][] stage, int[][] rotate_shape, int posx, int posy)
  {
    return true;
  }

  public boolean checkBlockR(int[][] stage, int [][] rotateShape, int posx, int posy)
  {
    return true;
  }

  public boolean superTSpin(int[][] stage, int[][] rotate_shape, int posx, int posy, boolean RLFlag) {
    /*
    stage : stage 
     rotate_shape : rotate_shape
     posx : ミノのX座標
     posy : ミノのY座標
     RLFlag : true -> L
     false-> R
     */
    int sx = posx; //この左上の座標と比較
    int sy = posy + 1;
    int rex, rey, spx, spy;
    boolean rFlag = true, sFlag = true;
    boolean conR = true, conS = true;
    //入るかチェック
    if (RLFlag == true)
    {
      sx += 1;
      spx = sx;
      spy = sy;
      sx -= 1;
      sy -= 2;
      rex = sx;
      rey = sy;
      for (int j = 0; j < 5; j += 1)
      {
        if (j+spy > 23)conS = false;
        if (j+rey > 23)conR = false;
        for (int i = 0; i < 5; i += 1)
        {
          if (i+spx >= 12)conS = false;
          if (i+rex >= 12)conR = false;


          if (conS == true && sFlag == true)
            sFlag = chechkSpin_L(stage, i, j, spx, spy);
          if (conR == true && rFlag == true)
            rFlag = chechkSpin_R(stage, i, j, rex, rey);
          if (rFlag == false && sFlag == false)return false;
        }
        conS = true;
        conR = true;
      }
    } else if (RLFlag == false)
    {
      sx -= 1;
      spx = sx;
      spy = sy;
      sx += 1;
      sy -= 2;
      rex = sx;
      rey = sy;

      if (rex == -1)rex = 0;
      for (int j = 0; j < 5; j += 1)
      {
        if (j+spy > 23)conS = false;
        if (j+rey > 23)conR = false;
        for (int i = 0; i < 5; i += 1)
        {
          if (i+spx >= 12)conS = false;
          if (i+rex >= 12)conR = false;
          if (conS == true && sFlag == true)
            sFlag = chechkSpin_R(stage, i, j, spx, spy);
          if (conR == true && rFlag == true)
            rFlag = chechkSpin_L(stage, i, j, rex, rey);
          if (rFlag == false && sFlag == false)return false;
        }
        conS = true;
        conR = true;
      }
    }
    //はめ込みをする
    //左回転させ下に2右に1ずらすなどをして
    if (checkMino(stage, rotate_shape, 1, 2) && RLFlag == true) {
      super.posx += 1;
      super.posy += 2;
      super.shape = rotate_shape;
      return true;
    } else if (checkMino(stage, rotate_shape, -1, 2) && RLFlag == false) {
      super.posx -= 1;
      super.posy += 2;
      super.shape = rotate_shape;
      return true;
    } else if (rFlag == true && RLFlag == true)
    {
      super.posx += 1;
      super.posy -= 2;
      super.shape = rotate_shape;
      return true;
    } else if (rFlag == true && RLFlag == false)
    {
      super.posx -= 1;
      super.posy -= 2;
      super.shape = rotate_shape;
      return true;
    }

    //戻りや向きが逆のパターンはのちにする
    return false;
  }

  public void showTexture() {
  }

  public void turnMino(int turn) {
  }

  public boolean chechkSpin_R(int[][] stage, int i, int j, int sx, int sy)
  {

    if (tSpinTriple[j][4-i] != -2) { //-2の時はどちらでもいいので判定しない
      if (stage[j+sy][i+sx] == -1) return true;

      if (tSpinTriple[j][4-i] == 1) {

        //ブロック1以上かをチェック
        if (stage[j+sy][i+sx] <= 0) return false;
      } else if (tSpinTriple[j][4-i] == 0) {

        //ブロックがないかをチェック
        if (stage[j+sy][i+sx] != 0) return false;
      }
    }
    return true;
  }
  public boolean chechkSpin_L(int[][] stage, int i, int j, int sx, int sy)
  {

    if (tSpinTriple[j][i] != -2) { //-2の時はどちらでもいいので判定しない
      if (stage[j+sy][i+sx] == -1)return true;

      if (tSpinTriple[j][i] == 1) {

        //ブロック1以上かをチェック
        if (stage[j+sy][i+sx] <= 0) return false;
      } else if (tSpinTriple[j][i] == 0) {

        //ブロックがないかをチェック
        if (stage[j+sy][i+sx] != 0) return false;
      }
    }
    return true;
  }
}
public class ZMino extends Mino {

  public ZMino(int x, int y) {
    super(x, y);
    super.nextPointX = 390;
    super.nextPointY = 140;
    super.holdPointX = 15;                //ホールド座標X 
    super.holdPointY = 190;               //ホールド座標Y
    super.holdSize = 15;
    super.nextBlockSize = 15;
    super.shape = new int[][] {
      {0, 0, 0, 0, 0}, 
      {0, 6, 6, 0, 0}, 
      {0, 0, 6, 6, 0}, 
      {0, 0, 0, 0, 0}, 
      {0, 0, 0, 0, 0}};

    super.id = 6;
  }
  
  public void superSpin(){}

  public void showTexture() {
  }

  public void turnMino(int turn) {
  }
}
  public void settings() {  size(480, 848); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Main" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
