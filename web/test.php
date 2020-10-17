<?php
ini_set("display_errors", 1);

$game_name = ["unagi", "pacman", "tetris"];

$dbh = [];
$stmt = [];
$ranking = [];

for ($i = 0; $i < 3; $i++) {
  // データベースに接続
  $dbh[] = new PDO(
      "mysql:host=mysql2014.db.sakura.ne.jp; dbname=nitnc5j_" . $game_name[$i] . "; charset=utf8",
      "nitnc5j",
      "5jclassarai"
  );

  // ランキングを取得
  $stmt[] = $dbh[$i]->query("SELECT * FROM ranking ORDER BY score DESC");
  $ranking[] = $stmt[$i]->fetchAll();
}
?>

<!DOCTYPE html>
<html lang="ja">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <!-- <link rel="stylesheet" href="./css/Ranking.css" /> -->
    <link rel="stylesheet" href="./css/style_test.css" />
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

    <!-- Global site tag (gtag.js) - Google Analytics -->
    <script async src="https://www.googletagmanager.com/gtag/js?id=UA-180728016-1"></script>
    <script>
      window.dataLayer = window.dataLayer || [];
      function gtag(){dataLayer.push(arguments);}
      gtag('js', new Date());

      gtag('config', 'UA-180728016-1');
    </script>
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

        <!-- 一つ目 -->
        <div class="swiper-slide">
          <div id="bg-1">
            <div class="movieBox">
              <img
              class="welcome-video"
              src="./images/unagi.gif"
              alt="ボッチマンムービー"
              />
              <div class="gameTitle">UNAGI</div>
            </div>
          </div>
          <div class="gameGiza">
            <img  src="./images/red-block.svg" alt="うなギザギザ" style="background-color: white; z-index: 5;">
          </div>
        </div>
        
        <!-- 二つ目 -->
        <div class="swiper-slide">
          <div id="bg-0">
            <div class="movieBox">
              <img
              class="welcome-video"
              src="./images/botchman.gif"
              alt="ウナギムービー"
              />
              <div class="gameTitle">BOTCH-MAN</div>
            </div>
          </div>
          <div class="gameGiza">
            <img  src="./images/slide-block.svg" alt="うなギザギザ" style="background-color: white; z-index: 5;">
          </div>
        </div>

        <!-- 三つ目 -->
        <div class="swiper-slide">
          <div id="bg-2">
            <div class="movieBox">
              <img
              class="welcome-video"
              src="./images/bbb.gif"
              alt="ブレイクブライトブロックムービー"
              />
              <div class="gameTitle">BREAK BRIGHT BLOCK</div>
            </div>
          </div>
          <div class="gameGiza">
            <img  src="./images/blue-block.svg" alt="うなギザギザ" style="background-color: white; z-index: 5;">
          </div>
        </div>

      </div>
    </div>
    <!-- ランキングの画面 -->
    <div id="ranking">
      <div name="ranking" class="smallTitle">ランキングを見る</div>
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
      <?php for ($i = 0; $i < 3; $i++) { ?>
        <div id="ranking<?= $i + 1 ?>" class="rankingBox" style="display: <?= $i == 0 ? "block" : "none" ?>">
          <?php $rank = 1; ?>
          <?php foreach ($ranking[$i] as $row) { ?>
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
      <?php } ?>
    </div>

    <!-- ダウンロードの画面 -->
    <div id="download">
      <div name="download" class="smallTitle">ゲームをダウンロードする</div>
      <label for="trigger" class="help" >
        <img
          class="helpMark"
          src="./images/question.svg"
          alt="クエスチョンマーク"
        />
        <div class="helpName">ダウンロードの仕方を知る</div>
      </div>
      <!-- ヘルプのポップアップ -->
      <div class="popup_wrap">
        <input id="trigger" type="checkbox" />
        <div class="popup_overlay">
          <label for="trigger" class="popup_trigger"></label>
          <div class="popup_content">
            <div id="popupTop">
              <div class="popupTitle">ゲームをダウンロードする</div>
              <label for="trigger" class="close_btn">
                <img src="./images/close-btn.svg" alt="閉じるボタン">
              </label>
            </div>
            <div id="popupBottom">
              <div class="setpBox">
                <div class="stepNum">STEP1</div>
                <img class="stepImg" src="./images/dl_1.png" alt="ステップ1">
                <div class="stepTitle">ダウンロードする</div>
                <div class="stepExp">
                  ホームページからお好きなゲームを選んで、ダウンロードボタンをクリックします。
                </div>
              </div>
              <div>
                <img class="arrowImg" src="./images/arrow.svg" alt="右">
              </div>
              <div class="setpBox">
                <div class="stepNum">STEP2</div>
                <img class="stepImg" src="./images/dl_2.png" alt="ステップ1">
                <div class="stepTitle">解凍する</div>
                <div class="stepExp">
                  ダウンロードしたファイルを右クリックから解凍し、フォルダを開きます。
                </div>
              </div>
              <div>
                <img class="arrowImg" src="./images/arrow.svg" alt="右">
              </div>
              <div class="setpBox">
                <div class="stepNum">STEP3</div>
                <img class="stepImg" src="./images/dl_3.png" alt="ステップ1">
                <div class="stepTitle">.exeファイルをクリック</div>
                <div class="stepExp">
                  〇〇.exeファイルをクリックするとゲームで遊べます。必ず「README.txt」を読んでから遊んでください。
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- ここまで -->
      <div id="gameDownload">
        <div class="download-game">
          <div class="download-gameTitle" id="download-unagiTitle">UNAGI</div>
          <div class="download-gameExplain">
            壁や障害物をよけつつ、餌を食べさせてうなぎを成長させましょう。成長するほどウナギが伸び難易度が上がります。
          </div>
          <button class="download-button" onclick="downloadUnagi()">
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
          <button class="download-button" onclick="downloadBotch()">
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
          <button class="download-button" onclick="downloadBBB()">
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
      <div name="preview" class="smallTitle">プレビュー</div>
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
    <script src="./js/jump.js"></script>
    <script src="./js/download.js"></script>
  </body>
</html>
