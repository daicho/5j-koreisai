// アニメーションの細かさ
var count2resize = 1000;

var gifs_src = new Array(
  "./images/unagi.gif",
  "./images/pacman.gif",
  "./images/bbb.gif"
);
var colors_bg = new Array("#c1c10f", "#c42431", "#1466c6");

var num = -1;
var screen = document.getElementById("gamemovie");
var bg0 = document.getElementById("bg-0");
var bg1 = document.getElementById("bg-1");
var bg2 = document.getElementById("bg-2");
var sizePerCount = 100 / count2resize;

const slideshow_timer = () => {
  if (num == 2) {
    num = 0;
  } else {
    num += 1;
  }
  screen.src = gifs_src[num];
  bg0.style.backgroundColor = colors_bg[0];
  bg1.style.backgroundColor = colors_bg[1];
  bg2.style.backgroundColor = colors_bg[2];

  animation(count2resize);
};

const animation = (count) => {
  if (count < 0) {
    slideshow_timer();
    return;
  }
  bg0.style.width = 100.0 - sizePerCount * count + "%";
  bg1.style.width = 0.0 + sizePerCount * count + "%";
  bg2.style.width = 0.0 + 0 + "%";

  console.log(count);

  setTimeout("animation(" + (count - 1) + ")", 0.1);
};

slideshow_timer();
