import java.util.Map;
import java.util.HashMap;

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
