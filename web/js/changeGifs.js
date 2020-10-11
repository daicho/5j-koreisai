var gifs_src = new Array(
  "./images/unagi.gif",
  "./images/pacman.gif",
  "./images/bbb.gif"
);
var num = -1;
var screen = document.getElementById("gamemovie");

slideshow_timer();

function slideshow_timer() {
  if (num == 2) {
    num = 0;
  } else {
    num += 1;
  }
  // screen.src = gifs_src[num];
  // document.getElementById("gamemovie").src = gifs_src[num];
  // setTimeout("slideshow_timer()", 4200);
}
