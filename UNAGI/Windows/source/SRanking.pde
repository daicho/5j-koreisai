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
