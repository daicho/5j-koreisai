var swiper = new Swiper(".swiper-container", {
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
