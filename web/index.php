<?php
ini_set("display_errors", 1);

// データベースに接続
$dbh_unagi = new PDO(
    "mysql:host=mysql2014.db.sakura.ne.jp; dbname=nitnc5j_unagi; charset=utf8",
    "nitnc5j",
    "5jclassarai"
);

$dbh_pacman = new PDO(
    "mysql:host=mysql2014.db.sakura.ne.jp; dbname=nitnc5j_pacman; charset=utf8",
    "nitnc5j",
    "5jclassarai"
);

$dbh_tetris = new PDO(
    "mysql:host=mysql2014.db.sakura.ne.jp; dbname=nitnc5j_tetris; charset=utf8",
    "nitnc5j",
    "5jclassarai"
);

// ランキングを取得
$stmt_unagi = $dbh_unagi->query("SELECT * FROM ranking");
$ranking_unagi = $stmt_unagi->fetchAll();

$stmt_pacman = $dbh_pacman->query("SELECT * FROM ranking");
$ranking_pacman = $stmt_pacman->fetchAll();

$stmt_tetris = $dbh_tetris->query("SELECT * FROM ranking ORDER BY score DESC");
$ranking_tetris = $stmt_tetris->fetchAll();

var_dump($ranking_unagi);
var_dump($ranking_pacman);
var_dump($ranking_tetris);
?>

<!DOCTYPE html>
<html lang="ja">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <!-- <link rel="stylesheet" href="./css/Ranking.css" /> -->
    <link rel="stylesheet" href="./css/style.css" />
    <link
      rel="stylesheet"
      href="./library/swiper-6.3.3/swiper-bundle.min.css"
    />
    <title>5Jのレトロゲーム</title>

    <!-- フォントの読み込み -->
    <link rel="stylesheet" href="https://use.typekit.net/rtr7aed.css" />
    <link
      href="https://fonts.googleapis.com/css2?family=Noto+Sans+JP:wght@400;700&display=swap"
      rel="stylesheet"
    />
  </head>
  <body>
    <!-- トップの画面 -->
    <div class="welcome" id="unagiWelcome">
      <div class="jumpMenu">
        <button class="welcome-jump" onclick="jump2ranking()">
          ランキング
        </button>
        <button class="welcome-jump" onclick="jump2download()">
          ゲームをダウンロードする
        </button>
        <button class="welcome-jump" onclick="jump2preview()">
          プレビュー
        </button>
      </div>
      <div class="welcomeBox">
        <div class="welcome-title">
          <img src="./images/catch-copy.svg" alt="キャッチコピー" />
        </div>
        <div class="welcome-explain">
          長野工業高等専門学校、電子情報工学科の5学年は昨年、レトロゲームを作りました。今年、オンラインで競い会えるようになって帰ってきました。ゲームをダウンロードして存分に楽しんでください。
        </div>
      </div>
    </div>

    <div class="swiper-container bgcolor">
      <div class="swiper-wrapper">
        <div class="swiper-slide" id="bg-0">
          <div class="movieBox">
            <img
              class="welcome-video"
              src="./images/botchman.gif"
              alt="ボッチマンムービー"
            />
            <div class="gameTitle">BOTCH-MAN</div>
          </div>
        </div>
        <div class="swiper-slide" id="bg-1">
          <div class="movieBox">
            <img
              class="welcome-video"
              src="./images/unagi.gif"
              alt="ウナギムービー"
            />
            <div class="gameTitle">UNAGI</div>
          </div>
        </div>
        <div class="swiper-slide" id="bg-2">
          <div class="movieBox">
            <img
              class="welcome-video"
              src="./images/bbb.gif"
              alt="ブレイクブライトブロックムービー"
            />
            <div class="gameTitle">BREAK BRIGHT BLOCK</div>
          </div>
        </div>
        <!-- <div class="swiper-slide" id="bg-1">botch</div> -->
      </div>
    </div>
    <div class="swiper-container gizasvg" id="gizagiza">
      <div class="swiper-wrapper">
        <div class="swiper-slide">
          <img
            src="./images/slide-block.svg"
            alt="ギザギザ"
            class="donotDownload"
          />
        </div>
        <div class="swiper-slide">
          <img
            src="./images/red-block.svg"
            alt="ギザギザ"
            class="donotDownload"
          />
        </div>
        <div class="swiper-slide">
          <img
            src="./images/blue-block.svg"
            alt="ギザギザ"
            class="donotDownload"
          />
        </div>
      </div>
    </div>
    <!-- ランキングの画面 -->
    <div id="ranking">
      <div class="smallTitle">ランキングを見る</div>
      <!-- ランキングの切り替え -->
      <div class="switch_menu">
        <div class="selected-unagi" id="unagi" onclick="showUnagi()">UNAGI</div>
        <div class="switch-unselected" id="botch-man" onclick="showBotch()">
          BOTCH-MAN
        </div>
        <div class="switch-unselected" id="bbb" onclick="showBBB()">
          BREAK BRIGHT BLOCK
        </div>
      </div>

      <!-- ランキングの表示 -->
      <div class="rankingBox">
        <!-- 実際の中身スコア一つ分 -->
        <?php $rank = 1; ?>
        <?php foreach ($ranking_pacman as $row) { ?>
          <?php if ($row["datetime"] == "0000-00-00 00:00:00") continue; ?>
          <?php if ($rank == 1) { ?>
            <div class="topScore">
              <div class="topNum"><?= $rank ?></div>
              <img class="topCrown" src="./images/crown.svg" alt="王冠" />
              <div class="topName"><?= $row["name"] ?></div>
              <div class="topPoint"><?= $row["score"] ?></div>
              <div class="topDate"><?= str_replace("-", "/", substr($row["datetime"], 0, 10)) ?></div>
            </div>
          <?php } else { ?>
            <div class="score">
              <div class="num"><?= $rank ?></div>
              <div class="name"><?= $row["name"] ?></div>
              <div class="point"><?= $row["score"] ?></div>
              <div class="date"><?= str_replace("-", "/", substr($row["datetime"], 0, 10)) ?></div>
            </div>
          <?php } ?>
          <?php $rank++; ?>
        <?php } ?>
      </div>
    </div>

    <!-- ダウンロードの画面 -->
    <div id="download">
      <div class="smallTitle">ゲームをダウンロードする</div>
      <div class="help" onclick="openHelp()">
        <img
          class="helpMark"
          src="./images/question.svg"
          alt="クエスチョンマーク"
        />
        <div class="helpName">ダウンロードの仕方を知る</div>
      </div>
      <div id="gameDownload">
        <div class="download-game">
          <div class="download-gameTitle" id="download-unagiTitle">UNAGI</div>
          <div class="download-gameExplain">
            壁や障害物をよけつつ、餌を食べさせてうなぎを成長させましょう。成長するほどウナギが伸び難易度が上がります。
          </div>
          <button class="download-button" onclick="donwloadUnagi()">
            ダウンロード
          </button>
        </div>
        <div class="download-game">
          <div class="download-gameTitle" id="download-botchmanTitle">
            BOTCH-MAN
          </div>
          <div class="download-gameExplain">
            高専の教員に捕まらず、ステージ上に散らばる単位を全てゲットしましょう。アイテムを取るといいことがあるかも...?
          </div>
          <button class="download-button" onclick="donwloadBotch()">
            ダウンロード
          </button>
        </div>
        <div class="download-game">
          <div class="download-gameTitle" id="download-breakbrightblockTitle">
            BREAK BRIGHT BLOCK
          </div>
          <div class="download-gameExplain">
            制限時間内にどれだけ多く華麗にブロックを消せるかを競うパズルゲームです。特殊な消し方で得点アップ!
          </div>
          <button class="download-button" onclick="donwloadBBB()">
            ダウンロード
          </button>
        </div>
      </div>
    </div>
    <div id="previewsvg">
      <img src="./images/preview-block.svg" alt="ギザ" />
    </div>
    <!-- プレビュー -->

    <div id="preview">
      <div class="smallTitle">プレビュー</div>
      <div class="previewBox">
        <img
          src="./images/unagi.gif"
          class="preview-game"
          alt="ウナギプレビュー"
        />
        <img
          src="./images/botchman.gif"
          class="preview-game"
          alt="ボッチマンプレビュー"
        />
        <img
          src="./images/bbb.gif"
          class="preview-game"
          alt="ブレイクブライトブロックプレビュー"
        />
      </div>
    </div>

    <div class="copyright">©︎ 2020 長野高専電子情報工学科5年</div>

    <script src="./library/swiper-6.3.3/swiper-bundle.min.js"></script>
    <script src="./js/changeGifs.js"></script>
    <script src="./js/ranking.js"></script>
    <script src="./js/help.js"></script>
    <script src="./js/jump.js"></script>
    <script src="./js/download.js"></script>
  </body>
</html>