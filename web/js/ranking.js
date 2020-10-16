var unagi = document.getElementById("unagi");
var botch = document.getElementById("botch-man");
var bbb = document.getElementById("bbb");

var rank_unagi = document.getElementById("ranking1");
var rank_botch = document.getElementById("ranking2");
var rank_bbb = document.getElementById("ranking3");

const showUnagi = () => {
  unagi.className = "selected-unagi";
  botch.className = "switch-unselected";
  bbb.className = "switch-unselected";

  rank_unagi.style.display = "block";
  rank_botch.style.display = "none";
  rank_bbb.style.display = "none";
};

const showBotch = () => {
  unagi.className = "switch-unselected";
  botch.className = "selected-botch";
  bbb.className = "switch-unselected";
  rank_unagi.style.display = "none";
  rank_botch.style.display = "block";
  rank_bbb.style.display = "none";
};

const showBBB = () => {
  unagi.className = "switch-unselected";
  botch.className = "switch-unselected";
  bbb.className = "selected-bbb";

  rank_unagi.style.display = "none";
  rank_botch.style.display = "none";
  rank_bbb.style.display = "block";
};
