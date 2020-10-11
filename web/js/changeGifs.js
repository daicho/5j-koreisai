var gifs_src = new Array(
  "./images/unagi.gif",
  "./images/pacman.gif",
  "./images/bbb.gif"
);
var colors_bg = new Array("#c1c10f", "c42431", "1466c6");

var num = -1;
var screen = document.getElementById("gamemovie");
var bg0 = document.getElementById("bg-0");
var bg1 = document.getElementById("bg-1");
var bg2 = document.getElementById("bg-0");

slideshow_timer();

function slideshow_timer() {
  if (num == 2) {
    num = 0;
  } else {
    num += 1;
  }
  screen.src = gifs_src[num];
  bg0.style.backgroundColor = colors_bg[num];
  bg1.style.backgroundColor = "black"; //colors_bg[num];
  bg2.style.backgroundColor = colors_bg[num];

  setTimeout("slideshow_timer()", 1000);
}
