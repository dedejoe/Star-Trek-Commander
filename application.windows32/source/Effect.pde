//This class handles sprite effects like explosions
class Effect extends Object {

  PImage image;
  int frameCount;
  int frame;
  int w;
  int h;
  float scale;
   
  Effect(float stX, float stY, PImage stImage, int stFrameCount, int stWidth, int stHeight, float stScale) {

    x = stX;
    y = stY;
    frameCount = stFrameCount;
    image = createImage(stImage.width / stWidth, stImage.height / stHeight, ARGB);
    image.copy(stImage, 0, 0, image.width, image.height, 0, 0, image.width, image.height);
    w = stWidth;
    h = stHeight;
    image = stImage;
    frame = 0;
    scale = stScale;
  }

  int draw() {

    pushMatrix();

    translate(x - anchor.x + (width / 2), y - anchor.y + (height / 2));
    imageMode(CENTER);


    int yCoord = frame / (image.width / (w - 20));
    PImage tmpImage = createImage(256, 256, ARGB);
    tmpImage.copy(image, 20 + (frame - (yCoord * 8)) * w, yCoord * (h + 8), w - 20, h, 0, 0, w - 20, h);

    image(tmpImage, 0, 0, (w - 20) * scale, h * scale);
    popMatrix();

    frame++;

    if (frame == frameCount) {
      return -1;
    }

    return 0;
  }
}
