// this class is a parent classs for the ships and other classes.
class Object {

  float x;
  float y;
  float rotation;

  float velocityX;
  float velocityY;

  float velocity() {
    return sqrt(sq(velocityX) + sq(velocityY));
  }

  Object() {
  }

  Object(float newX, float newY) {

    x = newX;
    y = newY;
  }

  boolean powered = false;
  int mass;

  void setRotation(float newRotation) {
    rotation = newRotation;
  }
}

class Rectangle {

  int w;
  int h;
  float x;
  float y;

  Rectangle(float newX, float newY, int newW, int newH) {

    x = newX;
    y = newY;
    w = newW;
    h = newH;
  }

  boolean intersect(float pointX, float pointY) {

    return (pointX > x && pointX < (x + w) && pointY > y && pointY < (y + h));
  }

  void draw() {

    fill(255, 255, 255, 90);
    rect(x, y, w, h);
  }
}

// The ellipse class handles all of the collision.  Ships have bounding ellipses.
class Ellipse {

  int w;
  int h;
  float x;
  float y;

  Ellipse(float newX, float newY, int newW, int newH) {

    x = newX;
    y = newY;
    w = newW;
    h = newH;
  }

  float x(float rotation) {

    float cosA = cos(rotation * PI /180);
    float sinA = sin(rotation * PI /180);
    //return x * -sin(rotation*PI/180) - x;
    //return (sqrt(sq(x) + sq(y)) * -sin(rotation * PI/180));
    return x * cosA - y * sinA;
  }

  float y(float rotation) {
    float cosA = cos(rotation * PI /180);
    float sinA = sin(rotation * PI /180);
    return y * cosA + x * sinA;
    // return (sqrt(sq(x) + sq(y))* cos(rotation * PI/180));
  }


  boolean inside(float pointX, float pointY, float rotation) { 

    // First half the width and height to get the ellipse parameters


    float cosA = cos(rotation * PI /180);
    float sinA = sin(rotation * PI /180);
    float ww = w / 2 * w / 2;
    float hh = h / 2 * h / 2;

    // Now calculate the deltas:
    float a = sq(cosA * (pointX - x(rotation)) + sinA * (pointY - y(rotation)));
    float b = sq(sinA * (pointX - x(rotation)) - cosA * (pointY - y(rotation)));

    return ((a/ww)+(b/hh) <= 1.0);
  }
}

class Node {

  float x;
  float y;

  Node(float newX, float newY) {

    x = newX;
    y = newY;
  }
}
