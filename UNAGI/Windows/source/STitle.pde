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
