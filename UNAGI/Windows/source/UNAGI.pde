import ddf.minim.*;

private Minim minim;
private VirtualInput vi;
private Game g;

void setup() {
  size(480, 848);
  
  this.surface.setTitle("UNAGI");
  this.surface.setIcon(loadImage("sprite/feed.png"));
  
  noCursor();
  frameRate(15);
  textFont(createFont("PixelMplus10-Regular.ttf", 10, false));
  
  minim = new Minim(this);
  vi = new VirtualInput();
  g  = new Game(vi, new STitle());
}

void draw() {
  if (g.frame()) {
    exit();
  }
  
  vi.flush();
}

void keyPressed(KeyEvent e) {
  switch (e.getKey()) {
    case 'w': vi.pressButton(VirtualInput.UP); break;
    case 'a': vi.pressButton(VirtualInput.LEFT); break;
    case 's': vi.pressButton(VirtualInput.DOWN); break;
    case 'd': vi.pressButton(VirtualInput.RIGHT); break;
    case 'e': vi.pressButton(VirtualInput.OK); break;
    case 'q': vi.pressButton(VirtualInput.CANCEL); break;
  }
}

void keyReleased(KeyEvent e) {
  switch (e.getKey()) {
    case 'w': vi.releaseButton(VirtualInput.UP); break;
    case 'a': vi.releaseButton(VirtualInput.LEFT); break;
    case 's': vi.releaseButton(VirtualInput.DOWN); break;
    case 'd': vi.releaseButton(VirtualInput.RIGHT); break;
    case 'e': vi.releaseButton(VirtualInput.OK); break;
    case 'q': vi.releaseButton(VirtualInput.CANCEL); break;
  }
}

void stop() {
  minim.stop();
}

AudioPlayer loadAudio(String s) {
  return minim.loadFile(s);
}
