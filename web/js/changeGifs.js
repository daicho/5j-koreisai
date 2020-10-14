var swiper = new Swiper(".bgcolor", {
  pagination: {
    el: ".swiper-pagination",
    dynamicBullets: true,
  },
  autoplay: {
    delay: 4000,
  },
  loop: true,
  speed: 1000,
  simulateTouch: false,
});
var swiper = new Swiper(".gizasvg", {
  pagination: {
    el: ".swiper-pagination",
    dynamicBullets: true,
  },
  autoplay: {
    delay: 4000,
  },
  loop: true,
  speed: 1000,
  simulateTouch: false,
});

document.onselectstart = function () {
  return false;
};
document.onmousedown = function () {
  return false;
};
document.body.onselectstart = "return false;";
document.body.onmousedown = "return false;";

var gifs_src = new Array(
  "./images/unagi.gif",
  "./images/pacman.gif",
  "./images/bbb.gif"
);
var colors_bg = new Array("#c1c10f", "#c42431", "#1466c6");

var bg0 = document.getElementById("bg-0");
var bg1 = document.getElementById("bg-1");
var bg2 = document.getElementById("bg-2");
