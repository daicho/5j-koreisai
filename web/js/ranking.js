var unagi = document.getElementById("unagi");
var botch = document.getElementById("botch-man");
var bbb = document.getElementById("bbb");

const showUnagi = () => {
  unagi.className = "selected-unagi";
  botch.className = "switch-unselected";
  bbb.className = "switch-unselected";
};

const showBotch = () => {
  unagi.className = "switch-unselected";
  botch.className = "selected-botch";
  bbb.className = "switch-unselected";
};

const showBBB = () => {
  unagi.className = "switch-unselected";
  botch.className = "switch-unselected";
  bbb.className = "selected-bbb";
};
