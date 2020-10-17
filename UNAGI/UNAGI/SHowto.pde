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
