<?php
// クエリ文を取得
if (!array_key_exists("game", $_POST))
    exit();

$game = urldecode($_POST["game"]);

// データベースに接続
$dbh = new PDO(
    "mysql:host=mysql2014.db.sakura.ne.jp; dbname=nitnc5j_management; charset=utf8",
    "nitnc5j",
    "5jclassarai"
);

// クエリ文の実行
$stmt = $dbh->query("INSERT INTO counter_" . $game . " VALUES('". date("Y-m-d H:i:s") . "')");;
