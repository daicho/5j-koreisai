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
