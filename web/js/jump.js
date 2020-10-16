var height = document.body.style.height;
height = height < 800 ? 800 : height;

const jump2ranking = () => {
  scrollTo(0, height + 245);
};
const jump2download = () => {
  scrollTo(0, 2 * height + 300);
};

const jump2preview = () => {
  scrollTo(0, 15000);
};
