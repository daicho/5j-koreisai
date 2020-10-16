const downloadUnagi = () => {
  let xhr = new XMLHttpRequest();
  xhr.open("POST", "./counter.php");
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
  xhr.send("game=unagi");

  gtag("event", "download", {"event_label": "UNAGI.zip"});
  location.href = "./unagi/UNAGI.zip";
};

const downloadBotch = () => {
  let xhr = new XMLHttpRequest();
  xhr.open("POST", "./counter.php");
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
  xhr.send("game=pacman");

  gtag("event", "download", {"event_label": "BOTCH-MAN.zip"});
  location.href = "./pacman/BOTCH-MAN.zip";
};

const downloadBBB = () => {
  let xhr = new XMLHttpRequest();
  xhr.open("POST", "./counter.php");
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
  xhr.send("game=tetris");

  gtag("event", "download", {"event_label": "BBB.zip"});
  location.href = "./tetris/BREAK BRIGHT BLOCK.zip";
};
