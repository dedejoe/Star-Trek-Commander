import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.sound.*; 
import java.util.*; 
import java.util.LinkedList; 
import java.util.LinkedList; 
import java.util.Iterator; 
import java.util.LinkedList; 
import java.util.Iterator; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Main extends PApplet {




// Sound Effects
SoundEffect disruptor_blast;
SoundEffect phaser_blast;
SoundEffect shield_impact;
SoundEffect large_explosion;
SoundEffect quantum_torpedo;
SoundEffect hull_hit;

// Music Sounds
SoundFile spaceNoise;
SoundFile menuMusic;
SoundFile battleMusic;

// LinkedLists
LinkedList<Ship> ships;
LinkedList<Effect> effects;
LinkedList<SoundEffect> soundEffects;

// PImages
PImage explosionImage;
PImage hullStatus;
PImage logo;
PImage quantumTorpedo;
PImage tutorial;

// booleans
boolean menuUp = true;
boolean gameOver = false;
boolean won = false;
boolean startedGame;
boolean[] moving = new boolean[4];
boolean difficultyHard = false;
boolean test = false;

// Misc
Enterprise enterprise;
Ship warbird;
Star[] stars;
Object anchor;
Projectile laser;
Rectangle[] shieldBalanceRectangles;
PFont myFont;
Menu menu;

public void setup() {

  frameRate(60);
  imageMode(CENTER);
  
  textSize(50);
  background(0xff000000);
  text("Loading...", width - 280, height - 50);

  // load sounds
  menuMusic = new SoundFile(this, "menu_theme.wav");
  spaceNoise = new SoundFile(this, "tng_engine_1.wav");
  battleMusic = new SoundFile(this, "battle_music.wav");
  disruptor_blast = new SoundEffect(new SoundFile(this, "disruptor_blast.wav"), 10);
  phaser_blast = new SoundEffect(new SoundFile(this, "phaser_blast.wav"), 10);
  shield_impact = new SoundEffect(new SoundFile(this, "shield_impact.wav"), 10);
  hull_hit = new SoundEffect(new SoundFile(this, "hull_hit.wav"), 10);
  large_explosion = new SoundEffect(new SoundFile(this, "large_explosion.wav"), 5);
  quantum_torpedo = new SoundEffect(new SoundFile(this, "quantum_torpedo.wav"), 10);

  // load images
  tutorial = loadImage("Tutorial.PNG");

  soundEffects = new LinkedList<SoundEffect>();

  soundEffects.add(disruptor_blast);
  soundEffects.add(phaser_blast);
  soundEffects.add(shield_impact);
  soundEffects.add(hull_hit);
  soundEffects.add(large_explosion);
  soundEffects.add(quantum_torpedo);
  menuMusic.loop();

  startedGame = false;
  logo = loadImage("logo.PNG");

  Object myAnchor = new Object(0, 0);
  anchor = myAnchor;

  // create menu
  menu = new Menu();
  myFont = createFont("font.ttf", 128);
  Container container = new Container();
  Button button = new Button("Play", 1);
  container.addButton(button);
  button = new Button("Tutorial", 5);
  container.addButton(button);
  button = new Button("Exit Game", 0);
  container.addButton(button);
  menu.containers.add(container);
  container = new Container();
  button = new Button("Easy", 3);
  container.addButton(button);
  button = new Button("Hard", 3);
  container.addButton(button);
  button = new Button("Back", 2, 32);
  container.addButton(button);
  menu.containers.add(container);
  container = new Container(tutorial);
  button = new Button("Back", 2, 32);
  container.addButton(button);
  menu.containers.add(container);

  // create stars
  stars = new Star[750];
  for (int i = 0; i < 50; i++) {
    stars[i] = new Star();
  }
  for (int i = 50; i < 100; i++) {
    stars[i] = new Star(stars[i - 50].x + width, stars[i - 50].y, stars[i - 50].radius);
  }
  for (int i = 100; i < 150; i++) {
    stars[i] = new Star(stars[i - 50].x + width, stars[i - 50].y, stars[i - 50].radius);
  }
  for (int i = 150; i < 200; i++) {
    stars[i] = new Star(stars[i - 150].x, stars[i - 150].y + height, stars[i - 50].radius);
  }
  for (int i = 200; i < 250; i++) {
    stars[i] = new Star(stars[i - 50].x + width, stars[i - 50].y, stars[i - 50].radius);
  }
  for (int i = 250; i < 300; i++) {
    stars[i] = new Star(stars[i - 50].x + width, stars[i - 50].y, stars[i - 50].radius);
  }
  for (int i = 300; i < 350; i++) {
    stars[i] = new Star(stars[i - 150].x, stars[i - 50].y + height, stars[i - 50].radius);
  }
  for (int i = 350; i < 400; i++) {
    stars[i] = new Star(stars[i - 50].x + width, stars[i - 50].y, stars[i - 50].radius);
  }
  for (int i = 400; i < 450; i++) {
    stars[i] = new Star(stars[i - 50].x + width, stars[i - 50].y, stars[i - 50].radius);
  }
  for (int i = 450; i < 500; i++) {
    stars[i] = new Star(stars[i - 150].x, stars[i - 50].y + height, stars[i - 50].radius);
  }
  for (int i = 500; i < 550; i++) {
    stars[i] = new Star(stars[i - 50].x + width, stars[i - 50].y, stars[i - 50].radius);
  }
  for (int i = 550; i < 600; i++) {
    stars[i] = new Star(stars[i - 50].x + width, stars[i - 50].y, stars[i - 50].radius);
  }
  for (int i = 600; i < 650; i++) {
    stars[i] = new Star(stars[i - 150].x, stars[i - 50].y + height, stars[i - 50].radius);
  }
  for (int i = 650; i < 700; i++) {
    stars[i] = new Star(stars[i - 50].x + width, stars[i - 50].y, stars[i - 50].radius);
  }
  for (int i = 700; i < 750; i++) {
    stars[i] = new Star(stars[i - 50].x + width, stars[i - 50].y, stars[i - 50].radius);
  }
}

public void draw() {

  //draw Space
  background(0xff000000);
  noStroke();
  fill(0xffffffff);
  for (int i = 0; i < 750; i++) {
    stars[i].draw();
  }

  //handle keypresses
  runKeyboard();

  // do Menu things
  if (menuUp) {
    cursor();
    menu.update_draw();
    return;
  }

  //update all ships
  int numShips = 0;
  Iterator<Ship> iterator = ships.iterator();
  while (iterator.hasNext()) {
    Ship tempShip = iterator.next();
    tempShip.update();
    tempShip.draw();
    numShips++;
  }

  if (gameOver == false && numShips == 1) {

    if (enterprise.destroyed()) {
      won = false;
    } else won = true;
    gameOver = true;
    createReplayMenu();
  }

  Iterator<Effect> effectIterator = effects.iterator();
  while (effectIterator.hasNext()) {
    Effect tempEffect = effectIterator.next();
    if (tempEffect.draw() == -1) {
      effectIterator.remove();
    }
  }

  //draw HUD
  drawMiniMap();
  drawShipStatus();

  //update sounds
  Iterator<SoundEffect> soundEffectIterator = soundEffects.iterator();
  while (soundEffectIterator.hasNext()) {
    soundEffectIterator.next().update();
  }

  if (gameOver) {
    String message = "";
    cursor();

    if (won) {
      message = "VICTORY";
      fill(0, 255, 0);
    } else {
      message = "DEFEAT";
      fill(255, 0, 0);
    }

    // draw victory message
    textSize(128);
    textFont(myFont);
    text(message, width / 2, height / 2);

    pushMatrix();

    translate(width / 2, height / 4 + height / 10);
    textAlign(CENTER);

    //draw menu
    fill(0xffffffff);

    // draw all contained containers
    int counter = 0;
    Iterator<Container> citerator = menu.containers.iterator();
    while (citerator.hasNext()) {
      Container tmpContainer = citerator.next();
      if (counter == menu.currentContainer) {
        tmpContainer.update();
        tmpContainer.draw();
        break;
      }
      counter++;
    }

    textFont(createFont("Arial Bold", 16));

    popMatrix();
    return;
  } else {  
    noCursor();
  }

  // draw cursor
  resetMatrix();
  noFill();
  stroke(0, 255, 0, 155);
  strokeWeight(1);
  line(mouseX, mouseY - 20, mouseX, mouseY - 10);
  line(mouseX, mouseY + 20, mouseX, mouseY + 10);
  line(mouseX - 20, mouseY, mouseX - 10, mouseY);
  line(mouseX + 20, mouseY, mouseX + 10, mouseY);

  anchor = new Object(enterprise.x, enterprise.y);

  // Lock camera at boundary
  if (enterprise.x > width * 1 / 2) {
    anchor.x = width * 1 / 2;
  } else if (enterprise.x < -width * 1 / 2) {
    anchor.x = -width * 1 / 2;
  }
  // top and bottom edge
  if (enterprise.y > height * 3 / 2) {
    anchor.y = height * 3 /2;
  } else if (enterprise.y < -height * 3 / 2) {
    anchor.y = height * -3/ 2;
  }
  
}

public void startGame() {

  startedGame = true;
  gameOver = false;

  menuMusic.pause();

  textFont(createFont("Arial Bold", 20));

  noCursor();

  hullStatus = loadImage("enterprise_hull.PNG");

  // load Images
  explosionImage = loadImage("explosion_sprite_sheet.png");

  // create ships list
  ships = new LinkedList<Ship>();

  // create ship
  enterprise = new Enterprise(0f, 0f, 180f, loadImage("enterprise.PNG"), 0.3f, 6.5f, 1.85f, 0);
  enterprise.id = 0;
  enterprise.weapons.add(new Weapon(enterprise, 0.45f, 85f, 35f, 80, 0.9f, phaser_blast, 0));
  enterprise.weapons.add(new Weapon(enterprise, 0.6f, 25f, 90f, 20, 0.4f, quantum_torpedo, 1));
  quantumTorpedo = loadImage("quantum_torpedo.PNG");
  ships.add(enterprise);


  // create warbird
  float warbirdSpeed = 9.5f;
  if (difficultyHard) warbirdSpeed += 2.5f;
  warbird = new Ship(400f, 200f, 0f, loadImage("romulan_warbird.PNG"), 0.3f, (int)warbirdSpeed, 0.9f, 1);
  warbird.id = 1;
  warbird.weapons.add(new Weapon(warbird, 0.55f, 45f, 35f, 80, 0.7f, disruptor_blast, 2));
  ships.add(warbird);

  moving[0] = false;
  moving[1] = false;
  moving[2] = false;
  moving[3] = false;

  // create effects
  effects = new LinkedList<Effect>();

  anchor = enterprise;

  spaceNoise.loop(0.8f);
  battleMusic.loop(0.5f);

  // create shield balance rectangles
  shieldBalanceRectangles = new Rectangle[4];
  float imgWidth =  height / 5 * 0.38f * 0.8f;
  float imgHeight = height / 5 * 0.8f;
  shieldBalanceRectangles[0] = new Rectangle((width / 5 + height / 5 * 0.38f * 0.8f * 2) - imgWidth * 1.1f, (height - height / 7.5f) - imgHeight / 2, (int)(imgWidth / 2), (int)imgHeight);
  shieldBalanceRectangles[1] = new Rectangle((width / 5 + height / 5 * 0.38f * 0.8f * 2) + imgWidth * 0.62f, (height - height / 7.5f) - imgHeight / 2, (int)(imgWidth / 2), (int)imgHeight);
  shieldBalanceRectangles[2] = new Rectangle((width / 5 + height / 5 * 0.38f * 0.8f * 2) - imgWidth / 2, (height - height / 7.5f) - imgHeight * 0.65f, (int)imgWidth, (int)imgHeight / 6);
  shieldBalanceRectangles[3] = new Rectangle((width / 5 + height / 5 * 0.38f * 0.8f * 2) - imgWidth / 2, (height - height / 7.5f) + imgHeight * 0.5f, (int)imgWidth, (int)imgHeight / 6);
}
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

  public int draw() {

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
 //<>// //<>// //<>//

//This class handles drawing the minimap in the bottomleft
public void drawMiniMap() {

  pushMatrix();

  stroke(0xffffffff);
  strokeWeight(1);
  fill(0, 0, 0, 50);
  ellipseMode(CENTER);
  translate(5, height - height / 2.5f - 5);

  rect(0, 0, width / 5, height / 2.5f);

  translate(width / 10, height / 5);

  fill(0, 255, 0);

  circle(enterprise.x / 10, enterprise.y / 10, 8);

  fill(255, 0, 0);

  circle(warbird.x / 10, warbird.y  / 10, 8);


  popMatrix();
}

//this method draws the shield and hull integrity visual
public void drawShipStatus() {

  pushMatrix();

  float translationX = width / 5 + height / 5 * 0.38f * 0.8f * 2;
  float translationY = height - height / 7.5f;
  textAlign(CENTER);
  stroke(0xffffffff);
  strokeWeight(1);
  fill(0xff000000);
  ellipseMode(CENTER);
  float hullIntegrity = (float)(enterprise.hullIntegrity) / 100;
  tint(255 * (1f - hullIntegrity), 255 * hullIntegrity, 0);
  translate(translationX, translationY);

  // draw ship model
  float imgWidth =  height / 5 * 0.38f * 0.8f;
  float imgHeight = height / 5 * 0.8f;
  image(hullStatus, 0, 0, imgWidth, imgHeight);

  // draw hull integrity text
  textSize(18);
  fill(0xffffffff);
  text((int)enterprise.hullIntegrity + "%", 0, imgHeight / 0.8f * 0.23f);


  // draw shield
  noFill();
  strokeWeight(5);

  // draw top
  float average = enterprise.shieldAverage(0, 90);
  float constant = 0f;
  if (shieldBalanceRectangles[2].intersect(mouseX, mouseY)) constant = 100f;
  stroke(constant, constant, average - 40f);
  constant = 0f;
  curve(0 - imgWidth / 3, 0 - imgHeight / 8, 0 - imgWidth / 2, 0 - imgHeight / 1.8f, 0 + imgWidth / 2, 0 - imgHeight /  1.8f, 0 + imgWidth / 3, 0 - imgHeight / 8);


  // draw bottom
  average = enterprise.shieldAverage(180, 90);
  if (shieldBalanceRectangles[3].intersect(mouseX, mouseY)) constant = 100f;
  stroke(constant, constant, average - 40f);
  constant = 0f;
  curve(0 - imgWidth / 3, 0 + imgHeight / 8, 0 - imgWidth / 2, 0 + imgHeight / 1.8f, 0 + imgWidth / 2, 0 + imgHeight /  1.8f, 0 + imgWidth / 3, 0 + imgHeight / 8);

  // draw left
  average = enterprise.shieldAverage(90, 90);
  if (shieldBalanceRectangles[0].intersect(mouseX, mouseY)) constant = 100f;
  stroke(constant, constant, average - 40f);
  constant = 0f;
  curve(0 + imgWidth / 2, 0 + imgHeight / 1.8f, 0 - imgWidth / 1.4f, 0 + imgHeight / 2.2f, 0 - imgWidth / 1.4f, 0 - imgHeight / 2.2f, 0 + imgWidth / 2, 0 - imgHeight / 1.8f);

  // draw right
  average = enterprise.shieldAverage(270, 90);
  if (shieldBalanceRectangles[1].intersect(mouseX, mouseY)) constant = 100f;
  stroke(constant, constant, average - 40f);
  constant = 0f;
  curve(0 - imgWidth / 2, 0 + imgHeight / 1.8f, 0 + imgWidth / 1.4f, 0 + imgHeight / 2.2f, 0 + imgWidth / 1.4f, 0 - imgHeight / 2.2f, 0 - imgWidth / 2, 0 - imgHeight / 1.8f);

  fill(0xffffffff);
  // draw shield text

  text((int)(enterprise.shieldAverage(0, 90) * 100 / 255) + "%", 0, 0 - imgHeight * 0.7f);
  text((int)(enterprise.shieldAverage(180, 90) * 100 / 255) + "%", 0, 0 + imgHeight * 0.75f);
  text((int)(enterprise.shieldAverage(90, 90) * 100 / 255) + "%", 0 - imgWidth * 1.25f, 0 );
  text((int)(enterprise.shieldAverage(270, 90) * 100 / 255) + "%", 0 + imgWidth * 1.3f, 0);



  noTint();
  popMatrix();
}

// This class handles all of the menus
class Menu {

  LinkedList<Container> containers;
  int currentContainer;

  Menu() {
    containers = new LinkedList<Container>();
    currentContainer = 0;
  }  

  public void update_draw() {

    textFont(myFont);
    pushMatrix();
    translate(width / 2, height / 4 + height / 10);
    textAlign(CENTER);

    image(logo, 0, 0);


    // draw all contained containers
    Iterator<Container> iterator = containers.iterator();

    int counter = 0;
    while (iterator.hasNext()) {
      Container tmpContainer = iterator.next();
      if (counter == currentContainer) {
        tmpContainer.update();
        tmpContainer.draw();
        break;
      }
      counter++;
    }

    textFont(createFont("Arial Bold", 16));

    popMatrix();
  }
}

// This method creates the menu for victory/defeat
public void createReplayMenu() {

  menu = new Menu();

  Container container = new Container();

  Button button = new Button("Play Again", 1);
  container.addButton(button);

  button = new Button("Exit", 0);
  container.addButton(button);

  menu.containers.add(container);

  container = new Container();

  button = new Button("Easy", 3);
  container.addButton(button);

  button = new Button("Hard", 3);
  container.addButton(button);

  button = new Button("Back", 2, 32);
  container.addButton(button);

  menu.containers.add(container);
}

// This class handles all of the button clicking in the menus
class Button {

  float timer;
  int timerDirection;
  float x, y;
  int textSize;
  String text;
  int type;
  //type 0 = exit, type 1 = difficulty, type 2 = go Back

  Button(String newText, int newType) {
    x = -width / 150;
    y = 0;
    text = newText;
    type = newType;
    textSize = 64;
  }

  Button(String newText, int newType, int size) {
    x = -width / 150;
    y = 0;
    text = newText;
    type = newType;
    textSize = size;
  }

  public void draw() {
    textSize(textSize * timer);
    text(text, x, y);
  }

  public boolean mouseOver() {

    int mX = mouseX - width / 2;
    int mY = (mouseY - height / 4 - height / 10);
    float textTop = y - (textSize / 2);
    float textBottom = y + (textSize / 2);
    float textLeft = x - (text.length() * 15);
    float textRight = x + (text.length() * 15);
    return (mY > textTop && mY < textBottom && mX > textLeft && mX < textRight);
  }

  // this method determines what happens when a button is clicked
  public void execute() {

    if (type == 0) {
      exit();
    } else if (type == 1) {
      menu.currentContainer = 1;
    } else if (type == 2) {
      menu.currentContainer = 0;
    } else if (type == 5) {
      menu.currentContainer = 2;
    } else if (type == 3) {
      menu.currentContainer = 0;
      if (text == "Easy") {
        difficultyHard = false;
      } else {
        difficultyHard = true;
      }
      menuUp = false;

      // create New Menu

      menu = new Menu();

      Container container = new Container();

      Button button = new Button("Resume", 4);
      container.addButton(button);

      button = new Button("Tutorial", 5);
      container.addButton(button);

      button = new Button("New Game", 1);
      container.addButton(button);

      button = new Button("Exit", 0);
      container.addButton(button);

      menu.containers.add(container);

      container = new Container();

      button = new Button("Easy", 3);
      container.addButton(button);

      button = new Button("Hard", 3);
      container.addButton(button);

      button = new Button("Back", 2, 32);
      container.addButton(button);

      menu.containers.add(container);

      container = new Container(tutorial);

      button = new Button("Back", 2, 32);
      container.addButton(button);

      menu.containers.add(container);

      startGame();
    } else if (type == 4) {
      menuUp = false;
      menuMusic.pause();
      spaceNoise.loop(0.8f);
      battleMusic.loop(0.5f);
    }
  }

  // this makes the buttons visually responsive
  public void update() {

    if (mouseOver() == true) {

      if (timer <= 0.5f) {

        timerDirection = 1;
      } else if (timer >= 1.0f) {
        timerDirection = -1;
      }

      timer += 0.02f * timerDirection;
    } else {
      timer = 1f;
    }
  }
}

// this class contains all of the buttons on a given menu
class Container {

  private PImage image = null;
  private LinkedList<Button> buttons;

  Container() {
    buttons = new LinkedList<Button>();
  }

  Container(PImage newImage) {
    buttons = new LinkedList<Button>();
    image = newImage;
  }

  public void draw() {

    // draw all contained controls
    Iterator<Button> iterator = buttons.iterator();

    // this is for the tutorial
    if (image != null) {
      image(image, 0, height / 2.8f);
      while (iterator.hasNext()) {
        Button button = iterator.next();
        button.y = height / 2 + 75;
        button.draw();
      }
      return;
    }

    while (iterator.hasNext()) {
      iterator.next().draw();
    }
  }

  public void update() {

    // draw all contained controls
    Iterator<Button> iterator = buttons.iterator();

    while (iterator.hasNext()) {
      iterator.next().update();
    }
  }

  public void addButton(Button button) {
    button.y = height / 3 + count() * 64;
    buttons.add(button);
  }

  public int count() {
    int i = 0;
    Iterator<Button> iterator = buttons.iterator();

    while (iterator.hasNext()) {
      iterator.next();
      i++;
    }

    return i;
  }
}
// this two methods just bring a rotation back down to 0 - 360
public float normalizeAngle(float angle) {

  while (angle >= 360 || angle < 0) {

    if (angle >= 360) {
      angle -= 360;
    } else if (angle < 0) {
      angle += 360;
    }
  }
  return angle;
}

public int normalizeAngle(int angle) {

  while (angle >= 360 || angle < 0) {

    if (angle >= 360) {
      angle -= 360;
    } else if (angle < 0) {
      angle += 360;
    }
  }
  return angle;
}

// returns the distance of two points
public float distance(float x, float y) {

  return sqrt(sq(x) + sq(y));
}

public float distance(float x1, float y1, float x2, float y2) {

  return sqrt(sq(x2 - x1) + sq(y2 - y1));
}

// this class handles the stars
class Star {

  float x;
  float y;
  float radius;

  Star() {
    x = random(width) - width * 3 / 2;
    y = random(height) - height * 5 / 2;
    radius = random(5);
  }

  Star(float newX, float newY, float newRadius) {
    x = newX;
    y = newY;
    radius = newRadius;
  }

  public void draw() {
    float transX = x - anchor.x + (width / 2);
    float transY = y - anchor.y + (height / 2);
    if (transX > 0 && transX < width && transY > 0 && transY < height)
      circle(transX, transY, radius);
  }
}
// this class is a parent classs for the ships and other classes.
class Object {

  float x;
  float y;
  float rotation;

  float velocityX;
  float velocityY;

  public float velocity() {
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

  public void setRotation(float newRotation) {
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

  public boolean intersect(float pointX, float pointY) {

    return (pointX > x && pointX < (x + w) && pointY > y && pointY < (y + h));
  }

  public void draw() {

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

  public float x(float rotation) {

    float cosA = cos(rotation * PI /180);
    float sinA = sin(rotation * PI /180);
    //return x * -sin(rotation*PI/180) - x;
    //return (sqrt(sq(x) + sq(y)) * -sin(rotation * PI/180));
    return x * cosA - y * sinA;
  }

  public float y(float rotation) {
    float cosA = cos(rotation * PI /180);
    float sinA = sin(rotation * PI /180);
    return y * cosA + x * sinA;
    // return (sqrt(sq(x) + sq(y))* cos(rotation * PI/180));
  }


  public boolean inside(float pointX, float pointY, float rotation) { 

    // First half the width and height to get the ellipse parameters


    float cosA = cos(rotation * PI /180);
    float sinA = sin(rotation * PI /180);
    float ww = w / 2 * w / 2;
    float hh = h / 2 * h / 2;

    // Now calculate the deltas:
    float a = sq(cosA * (pointX - x(rotation)) + sinA * (pointY - y(rotation)));
    float b = sq(sinA * (pointX - x(rotation)) - cosA * (pointY - y(rotation)));

    return ((a/ww)+(b/hh) <= 1.0f);
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
// This class handles lasers and other energy blasts //<>// //<>// //<>//
class Projectile {

  float x;
  float y;
  float speed;
  float radius;
  float strength;
  int spread;
  boolean alreadyHit = false;
  boolean noCloseShip = true;
  float rotation;
  float startingX;
  float startingY;
  float length;
  boolean passed = false;
  float split;
  int shipID;
  float impactLevel = 40f;

  Projectile() {
  }

  Projectile(float startX, float startY, float startSpeed, float startRotation, float startRadius, float startStrength, int startSpread, float stSplit, int startShipID) {
    startingX = x = startX;
    startingY = y = startY;
    speed = startSpeed;
    rotation = startRotation;
    radius = startRadius;
    strength = startStrength;
    spread = startSpread;
    shipID = startShipID;
    split = stSplit;
    length = 0;
  }

  public int update() {

    // check collision with Ship

    Iterator<Ship> iterator = ships.iterator();
    while (iterator.hasNext()) {
      Ship tempShip = iterator.next();
      if (passed == false && collideWithShield(tempShip)) {
        if (tempShip.shield[tempShip.closestShieldPoint(x, y)] >= impactLevel) {
          tempShip.shieldImpact(strength, spread, split, x, y);
          return -1;
        } else {
          passed = true;
        }
      } else if (collideWithShip(tempShip)) {
        tempShip.shipImpact(strength, split, x, y);
        return -1;
      }
    }

    return move(speed);
  }

  // this method determines if a projectile has collided with a ships shield
  public boolean collideWithShield(Ship ship) {

    if (ship.id == shipID)
      return false;    

    if (ship.shieldEllipse.inside(x - ship.x, y - ship.y, ship.rotation)) {
      draw();
      return true;
    }

    return false;
  }

  // this method determines if a projectile has collided with the ships hull
  public boolean collideWithShip(Ship ship) {

    if (ship.id == shipID)
      return false;    

    Iterator<Ellipse> eiterator = ship.boundingEllipses.iterator();
    while (eiterator.hasNext()) {
      Ellipse ell = eiterator.next();
      if (ell.inside(x - ship.x, y - ship.y, enterprise.rotation)) {
        return true;
      }
    }

    return false;
  }

  // This method moves the ship and returns -1 if its out of range and should be terminated
  public int move(float a) {

    x += a * cos((rotation - 90) * PI / 180);
    y += a * sin((rotation - 90) * PI / 180);

    if (x < (anchor.x - width / 1.2f) || x > (anchor.x + width / 1.2f) )
      return -1;
    else if (y < (anchor.y - height / 1.2f) || y > (anchor.y + height / 1.2f) )
      return -1;


    return 0;
  }

  public float dirX() {
    return cos((rotation - 90) * PI / 180);
  }

  public float dirY() {
    return sin((rotation - 90) * PI / 180);
  }

  public void draw() {
    pushMatrix();
    stroke(0, 255, 100, 155);
    strokeWeight(2);
    fill(255, 255, 255, 155);
    translate(x - anchor.x + width / 2, y - anchor.y + height / 2);

    rotate(rotation * PI / 180);

    triangle(-5, 0, 5, 0, 0, 20);

    arc(0, 2, 10, 10, PI, 2 * PI);

    popMatrix();
  }
}

// an inherited class that determines the yellow energy blast of the enterprise
class Phaser extends Projectile {
  float finalLength = 600;
  boolean firstContact = true;
  boolean hullFirstContact = true;
  boolean latchedOn;
  float latchedX;
  float latchedY;
  Ship latchedShip;

  Phaser(float startX, float startY, float startSpeed, float startRotation, float startRadius, float startStrength, int startSpread, float stSplit, int startShipID) {
    startingX = x = startX;
    startingY = y = startY;
    speed = startSpeed;
    rotation = startRotation;
    radius = startRadius;
    strength = startStrength;
    spread = startSpread;
    shipID = startShipID;
    split = stSplit;
    radius = finalLength;
    length = 5;
    radius = 0;
  }

  public int update() {

    if (!latchedOn) {
      for (int i = 0; i < speed / 10; i++) {
        move(speed / 10f);
        if (length < finalLength)
          length += speed / 10f;
        else {
          length -= speed / 10f;
          x -= speed * cos((rotation - 90) * PI / 180) / 10f;
          y -= speed * sin((rotation - 90) * PI / 180) / 10f;
        }

        Iterator<Ship> iterator = ships.iterator();
        while (iterator.hasNext()) {
          Ship tempShip = iterator.next();

          if (collideWithShip(tempShip)) {
            latchedOn = true;
            latchedX = x;
            latchedY = y;
            tempShip.shipImpact(strength, split, x, y);
            return -1;
          } else if (passed == false && collideWithShield(tempShip)) {
            if (tempShip.shield[tempShip.closestShieldPoint(x, y)] >= 20f) {
              tempShip.shieldImpact(strength, spread, split, x, y);
              latchedOn = true;
              latchedX = x;
              latchedY = y;
              return -1;
            } else {
              passed = true;
            }
          }
        }
      }
    } else {
      length -= speed;
      x = latchedX -= speed * cos((rotation - 90) * PI / 180);
      y = latchedY -= speed * sin((rotation - 90) * PI / 180);
    }

    return move(0f);
  }

  public void draw() {
    pushMatrix();
    strokeWeight(2);


    translate(x - anchor.x + width / 2, y - anchor.y + height / 2);

    rotate(rotation * PI / 180);

    noFill();
    stroke(255, 198, 10, 30);
    rect(-4.5f, 0, 9, length, 8, 8, 8, 8);
    stroke(255, 198, 10, 70);
    rect(-3.5f, 0, 7, length, 8, 8, 8, 8);
    stroke(255, 198, 10, 110);
    fill(255, 255, 255, 255);
    rect(-2.5f, 0, 5, length, 3, 3, 3, 3);

    popMatrix();
  }
}

// This is the blue torpedo of the enterprise
class QuantumTorpedo extends Projectile {

  QuantumTorpedo(float startX, float startY, float startSpeed, float startRotation, float startRadius, float startStrength, int startSpread, float stSplit, int startShipID) {
    startingX = x = startX;
    startingY = y = startY;
    speed = startSpeed;
    rotation = startRotation;
    radius = startRadius;
    strength = startStrength;
    spread = startSpread;
    shipID = startShipID;
    split = stSplit;
    radius = 0;
    length = 0;
    impactLevel = 30f;
  }

  public void draw() {

    pushMatrix();

    translate(x - anchor.x + width / 2, y - anchor.y + height / 2);

    rotate(rotation * PI / 180);

    image(quantumTorpedo, 0, 0);

    popMatrix();
  }
}
 //<>//


//This is the class for the main ship
class Enterprise extends Ship {

  Enterprise(float startX, float startY, float startRot, PImage startImage, float startScale, float startSpeed, float startTurningSpeed, int newTeam) {

    // initiate all shield values to full 100
    shield = new float[360];
    shieldFlash = new float[360];
    for (int i = 0; i < 360; i++) {
      shield[i] = 255f;
      shieldFlash[i] = 1f;
    }
    x = startX;
    y = startY;
    rotation = startRot;
    image = startImage;
    scale = startScale;
    mass = 1000;
    speed = startSpeed;
    turningSpeed = startTurningSpeed;
    powered = true;
    projectiles = new LinkedList<Projectile>();
    weapons = new LinkedList<Weapon>();
    boundingEllipses = new LinkedList<Ellipse>();
    shieldDisplacementX = 0.95f;
    shieldDisplacementY = 1f;
    team = newTeam;
    boundingEllipses.add(new Ellipse(0, 55, 80, 110));
    boundingEllipses.add(new Ellipse(0, -20, 25, 60));
    boundingEllipses.add(new Ellipse(-30, -50, 15, 105));
    boundingEllipses.add(new Ellipse(28, -50, 15, 105));
    shieldEllipse = new Ellipse(0, 0, (int)(shipWidth() * 1.75f * shieldDisplacementX), (int)(shipHeight() * 1.25f * shieldDisplacementY));
  }
}

class Ship extends Object {

  boolean targetingEnterprise = false;

  int id;
  int shieldBalanceTimer = 60;
  int shieldBalanceQuadrant = -1;
  float[] shield;
  float[] shieldFlash;
  float previousRotation = 0f;
  float myLastRotation;
  boolean changedDirection = false;
  float scale;
  int rotationTimer = 5;
  float shieldDisplacementX;
  float shieldDisplacementY;
  float speed;
  float turningSpeed;
  float radius = 50f;
  PImage image;
  int team;
  LinkedList<Ellipse> boundingEllipses;
  float hullIntegrity = 100;
  Ellipse shieldEllipse = new Ellipse(0, 0, 0, 0);
  LinkedList<Node> nodes;
  LinkedList<Weapon> weapons;
  LinkedList<Projectile> projectiles;

  Ship() {
    projectiles = new LinkedList<Projectile>();
  }

  Ship(float startX, float startY, float startRot, PImage startImage, float startScale, float startSpeed, float startTurningSpeed, int newTeam) {

    // initiate all shield values to full 100 
    shield = new float[360];
    shieldFlash = new float[360];
    for (int i = 0; i < 360; i++) {
      shield[i] = 255f;
      shieldFlash[i] = 1f;
    }
    x = startX;
    y = startY;
    rotation = startRot;
    image = startImage;
    scale = startScale;
    mass = 1000;
    speed = startSpeed;
    turningSpeed = startTurningSpeed;
    powered = true;
    projectiles = new LinkedList<Projectile>();
    weapons = new LinkedList<Weapon>();
    nodes = new LinkedList<Node>();
    boundingEllipses = new LinkedList<Ellipse>();
    shieldDisplacementX = 0.8f;
    shieldDisplacementY = 1;
    team = newTeam;

    boundingEllipses.add(new Ellipse(-3, -50, 20, 60));
    boundingEllipses.add(new Ellipse(-55, 10, 15, 60));
    boundingEllipses.add(new Ellipse(0, -10, 110, 80));
    boundingEllipses.add(new Ellipse(55, 10, 15, 60));
    boundingEllipses.add(new Ellipse(2, 50, 23, 60));
    shieldEllipse = new Ellipse(0, 0, (int)(shieldRadiusX() * 2), (int)(shieldRadiusY() * 2));
  }

  public void runAI() {


    float desiredAngle = 0f;
    float newSpeed = speed;
    float angle = normalizeAngle(atan2(enterprise.y - y, enterprise.x - x) * 180 / PI - 90) + 360;    

    float rot = rotation + 360;

    // fire weapons and slow down if in range to fire
    Iterator<Ship> iterator = ships.iterator();
    while (iterator.hasNext()) {
      Ship tempShip = iterator.next();
      if (tempShip.team == team || team == 0)
        continue;
      //text((int)abs((rotation + 360) - (angle)) + ", " + (int)rotation + ", " + (int)angle, 50, 50);
      if (abs((rotation + 360) - (angle)) < 30f && sqrt(sq(enterprise.x - x) + sq(enterprise.y - y)) < 900f) {
        if (difficultyHard) {
          newSpeed *= 0.75f;
        }
        Iterator<Weapon> weaponIterator = weapons.iterator();
        while (weaponIterator.hasNext()) {
          Weapon tmpWeapon = weaponIterator.next();
          if (tmpWeapon.canFire(true)) {
            tmpWeapon.fire();
          }
        }
      }
    }

    // move unit according to rotation
    x += newSpeed * cos((rotation + 90) * PI / 180);
    y += newSpeed * sin((rotation + 90)* PI / 180);
    //rotation += turningSpeed;

    // determine if we are on attack run or not
    if (!targetingEnterprise) {
      if (sqrt(sq(enterprise.x - x) + sq(enterprise.y - y)) > 800f) {
        targetingEnterprise = true;
      }
    } else {
      if (sqrt(sq(enterprise.x - x) + sq(enterprise.y - y)) < 200f) {
        targetingEnterprise = false;
      }
    }

    // This code block is to deal with the awkward 0 to 360 jump
    if (abs(previousRotation - angle + desiredAngle) > 180f) {
      changedDirection = true;
    }

    //adjust angle towards the enterprise if on an attack run
    if (targetingEnterprise) {
      if (!changedDirection) {
        if (rot > angle + desiredAngle) {
          if (rot - turningSpeed < angle) rot = angle;
          else rotation -= turningSpeed;
          myLastRotation = -turningSpeed;
        } else if (rot < angle + desiredAngle) {

          if (rot + turningSpeed > angle) rot = angle;
          else rotation += turningSpeed;
          myLastRotation = turningSpeed;
        }
      } else {
        rotation += myLastRotation;
        if (rotationTimer == 0) {      
          changedDirection = false;
          rotationTimer = 5;
        } else rotationTimer--;
      }
    } else {
      rotation += previousRotation * 0.001f;
    }

    // auto balance shields on hard

    if (difficultyHard) {
      if (shieldAverage(180, 90) < 80f && shieldBalanceQuadrant == -1) {
        shieldBalanceQuadrant = 3;
        shieldBalanceTimer = 240;
      } else if (shieldAverage(0, 90) < 80f && shieldBalanceQuadrant == -1) {
        shieldBalanceQuadrant = 2;
        shieldBalanceTimer = 240;
      } else if (shieldAverage(270, 90) < 80f && shieldBalanceQuadrant == -1) {
        shieldBalanceQuadrant = 1;
        shieldBalanceTimer = 240;
      } else if (shieldAverage(90, 90) < 80f && shieldBalanceQuadrant == -1) {
        shieldBalanceQuadrant = 0;
        shieldBalanceTimer = 240;
      }
    }

    previousRotation = angle;
  }

  public void update() {

    if (gameOver) return;

    rotation = normalizeAngle(rotation);

    if (team != 0) 
      runAI();

    normalizeShields(shieldBalanceQuadrant, 0.02f, 0.02f);

    Iterator<Projectile> iterator = projectiles.iterator();
    while (iterator.hasNext()) {
      if (iterator.next().update() == -1) {
        iterator.remove();
      }
    }

    Iterator<Weapon> weaponIterator = weapons.iterator();
    while (weaponIterator.hasNext()) {
      Weapon tmpWeapon = weaponIterator.next();
      tmpWeapon.update();
      if (tmpWeapon.buffered == true && tmpWeapon.canFire(false) ) {
        tmpWeapon.fire();
      }
    }

    if (x < -width) {
      x = width;
    }
    if (x > width) {
      x = -width;
    }
    if (y < -height * 2) {
      y = height * 2;
    }
    if (y > height * 2) {
      y = -height * 2;
    }
  }

  public void fire(int whichOne) {

    int counter = 0;
    Iterator<Weapon> iterator = weapons.iterator();
    while (iterator.hasNext()) {
      Weapon tmpWeapon = iterator.next();
      if (counter == whichOne) {
        if (tmpWeapon.canFire(true)) {
          tmpWeapon.fire();
        }
      }
      counter++;
    }
  }

  public int closestShieldPoint(float px, float py) {

    int temp = (int)(atan2(px - x, py - y) * 180 / PI + (rotation + 180));

    return normalizeAngle(temp);
  }

  public void shieldImpact(float force, int spread, float split, float hitX, float hitY) {
    // force = most damage at center of shot, spread = how much of the shield it affects

    shield_impact.play(1);

    int tmp = closestShieldPoint(hitX, hitY);
    float counter = -spread /2;
    for (int i = tmp - (spread / 2); i < tmp + (spread / 2); i++) {

      int y = i;
      if (i > 359)
        y -= 360;
      else if (i < 0)
        y += 360;

      shieldFlash[y] = 2f;
      shield[y] -= abs(force - (force / 2) * abs(counter)/spread * 0.5f) * split;

      if (shield[y] < (255f * (1f - split))) {
        hullIntegrity -= ((force * (1f - split)) / (float)spread) * split * 0.15f;
      }

      if (shield[y] < 0) {
        shield[y] = 0;
      }

      counter += 1;
    }

    //int centerShot =
  }

  public void shipImpact(float force, float split, float hitX, float hitY) {
    // force = most damage at center of shot, spread = how much of the shield it affects
    hullIntegrity -= (force * (1f - split));

    if (hullIntegrity <= 0f) {
      large_explosion.play(1f);
      Effect effect = new Effect(x + 25, y + 25, explosionImage, 40, 256, 248, 1.5f);
      effects.add(effect);

      hullIntegrity = 0f;

      Iterator<Ship> iterator = ships.iterator();
      while (iterator.hasNext()) {
        Ship tempShip = iterator.next();
        if (tempShip.id == id) {
          iterator.remove();
        }
      }
    } else {
      hull_hit.play(1);

      if (force >= 20) {
        Effect effect = new Effect(hitX, hitY, explosionImage, 40, 256, 248, force / 200f * (1f - split) * 2);
        effects.add(effect);
      }
    }
  }

  //int centerShot =


  public float shieldRadiusX() {

    return shipWidth() / 2 * 1.75f * shieldDisplacementX;
  }

  public float shieldRadiusY() {

    return shipHeight() / 2 * 1.25f * shieldDisplacementY;
  }

  public float getShieldX(int i) {
    float myX = PApplet.parseInt(shieldRadiusX() * -sin(i * PI/180));
    float myY = PApplet.parseInt(shieldRadiusY() * cos(i * PI/180));

    return (myX * cos(rotation * PI / 180) + myY * sin(rotation * PI / 180)) + x;
  }

  public float getShieldY(int i) {

    float myX = PApplet.parseInt(shieldRadiusX() * -sin(i * PI/180));
    float myY = PApplet.parseInt(shieldRadiusY() * cos(i * PI/180));

    return (myX * sin(rotation * PI / 180) - (myY * cos(rotation * PI / 180))) + y;
  }

  public float getShieldX(int i, float scalar) {

    float myX = PApplet.parseInt(scalar * shieldRadiusX() * -sin(i * PI/180));
    float myY = PApplet.parseInt(scalar * shieldRadiusY() * cos(i * PI/180));

    return (myX * cos(rotation * PI / 180) + myY * sin(rotation * PI / 180)) + x;
  }

  public float getShieldY(int i, float scalar) {
    float myX = PApplet.parseInt(scalar * shieldRadiusX() * -sin(i * PI/180));
    float myY = PApplet.parseInt(scalar * shieldRadiusY() * cos(i * PI/180));

    return (myX * sin(rotation * PI / 180) - (myY * cos(rotation * PI / 180))) + y;
  }

  public void draw() {

    Iterator<Projectile> iterator = projectiles.iterator();
    while (iterator.hasNext()) {
      iterator.next().draw();
    }

    noFill();
    strokeWeight(5);
    smooth();

    pushMatrix();
    translate(x - anchor.x + width / 2, y - anchor.y + height / 2);

    //ellipse(aimEllipse.x, aimEllipse.y, aimEllipse.w, aimEllipse.h);

    for (int i = 0; i < 36; i++) {
      //Change stroke of curve to shield strength
      if (shieldFlash[i * 10] > 1f) {
        stroke(shieldAverage((i * 10), 10) * 0.2f, shieldAverage((i * 10), 10) * 0.2f, 255, shieldAverage((i * 10), 10) * shieldFlash[i * 10]);
        shieldFlash[i * 10] -= 0.1f;
      } else
        stroke(0, 0, shieldAverage((i * 10), 10) * 0.5f);

      // draw first layer of shield
      curve(getShieldX(i * 10 - 10) - x, getShieldY(i * 10 - 10) - y, getShieldX(i * 10) - x, getShieldY(i * 10) - y, 
        getShieldX(i * 10 + 10) - x, getShieldY(i * 10 + 10) - y, getShieldX(i * 10 + 20) - x, getShieldY(i * 10 + 20) - y);
    }

    // draw ship image
    rotate(rotation * PI / 180);

    image(image, 0, 0, shipWidth(), shipHeight());

    /*  draw shields and hull boundingEllipse
     fill(255, 0, 0, 50);
     ellipse(shieldEllipse.x, shieldEllipse.y, shieldEllipse.w, shieldEllipse.h);
     
     fill(#ffffff);
     Iterator<Ellipse> biterator = boundingEllipses.iterator();
     while (biterator.hasNext()) {
     Ellipse t = biterator.next();
     ellipse(t.x, t.y, t.w, t.h);
     }
     */

    popMatrix();
  }

  public int shipWidth() {

    return PApplet.parseInt(image.width * scale);
  }

  public int shipHeight() {

    return PApplet.parseInt(image.height * scale);
  }

  public boolean destroyed() {

    return (hullIntegrity <= 0);
  }

  public float shieldAverage(int index, int range) {

    float average = 0f;

    for (int l = index - range / 2; l < index + range /  2; l++) {
      int i = l;
      if (l >= 360) {
        i -= 360;
      } else if (l < 0) {
        i += 360;
      }

      average += shield[i];
    }

    return (average / (range));
  }

  public void normalizeShields(int quadrant, float rate, float recharge) {

    if (quadrant == -1) {
      float average = 0f;

      for (int i = 0; i < 360; i++) {
        average += shield[i];
        if (shield[i] < 255) {
          if (shieldFlash[i] >= 1f)
            shield[i] += recharge;
        }
      }

      average /= 360f;

      for (int i = 0; i < 360; i++) {
        if (shieldFlash[i] <= 1f) {
          if (shield[i] < average && shield[i] < 255) {
            if (shield[i] + rate > average) {
              shield[i] = average;
            } else
              shield[i] += rate;
          } else if (shield[i] > average) {
            if (shield[i] - rate < average) {
              shield[i] = average;
            } else if (shield[i] > 255f) {
              shield[i] -= rate * 3;
            } else {
              shield[i] -= rate;
            }
          }
        }
      }
    } else {

      shieldBalanceTimer--;

      float power = 0f;
      float factor = 0.2f;

      for (int i = 0; i < 360; i++) {
        if (shield[i] - factor >= 0) {
          power += factor;
          shield[i] -= factor;
        } else {
          power += shield[i];
          shield[i] = 0;
        }
      }

      int start = 0;
      int range = 0;
      if (quadrant == 0) {
        start = 45;  
        range = start + 90;
      } else if (quadrant == 1) {
        start = 225;  
        range = start + 90;
      } else if (quadrant == 3) {
        start = 135;  
        range = start + 90;
      } else {
        start = 315;
        range = 45;
      }


      for (int i = 0; i < 360; i++) {
        if (quadrant == 2) {
          if (i > start || i < range) {
            shield[i] += power / 90f;
          }
        } else {
          if (i > start && i < range) {
            shield[i] += power / 90f;
          }
        }
      }

      if (shieldBalanceTimer <= 0) {
        shieldBalanceQuadrant = -1;
        shieldBalanceTimer = 120;
      }
    }
  }
}
class SoundEffect {

  int counter;
  int rate;
  SoundFile sound;

  SoundEffect(SoundFile newSound, int newRate) {
    sound = newSound;
    rate = newRate;
  }

  public void play(float volume) {
    if (counter >= rate) {
      sound.play(1, volume * 0.6f);
      counter = 0;
    }
  }
  
  public void update(){
  
    counter++;
    
  }
}
public void keyPressed() { //<>// //<>//

  if (gameOver) {
    key = 0;
    return;
  }

  if (key == ESC) {

    if (startedGame == false) {
      menuUp = true;
      return;
    } else {
      menuUp = !menuUp;
      if (menuUp) {
        if (battleMusic != null) {
          spaceNoise.pause();
          battleMusic.pause();
        }
        menuMusic.play();
      } else {
        menuMusic.pause();
        spaceNoise.loop(0.8f);
        battleMusic.loop(0.5f);
      }
    }

    key = 0;
  }



  if (enterprise != null && enterprise.destroyed()) return;

  if (menuUp) return;

  if (key == 'w') {
    moving[0] = true;
  } else if (key == 's') {
    moving[1] = true;
  }

  if (key == 'a') {
    moving[2] = true;
  } else if (key == 'd') {
    moving[3] = true;
  }
}

public void keyReleased() {
  if (enterprise == null || enterprise.destroyed()) return;
  if (key == 'w') {
    moving[0] = false;
  } else if (key == 's') {
    moving[1] = false;
  }

  if (key == 'a') {
    moving[2] = false;
  } else if (key == 'd') {
    moving[3] = false;
  }

  if (key == ' ') {
    enterprise.fire(1);
  }
}

public void runKeyboard() {

  if (gameOver) {
    key = 0;
    return;
  }

  if (enterprise != null && enterprise.destroyed()) return;

  if (menuUp) return;

  if (moving[0]) {
    enterprise.x += enterprise.speed * cos((enterprise.rotation + 90) * PI / 180);
    enterprise.y += enterprise.speed * sin((enterprise.rotation + 90)* PI / 180);
  } else if (moving[1]) {
    enterprise.x -= cos((enterprise.rotation + 90) * PI / 180) * enterprise.speed / 1.7f;
    enterprise.y -= sin((enterprise.rotation + 90)* PI / 180) * enterprise.speed / 1.7f;
  }

  if (moving[2]) {
    enterprise.setRotation(enterprise.rotation - enterprise.turningSpeed);
  } else if (moving[3]) {
    enterprise.setRotation(enterprise.rotation + enterprise.turningSpeed);
  }
}

public void mousePressed () {

  if (!menuUp && gameOver == false) {
    for (int i = 0; i < 4; i++) {
      if ( shieldBalanceRectangles[i].intersect(mouseX, mouseY)) {
        enterprise.shieldBalanceQuadrant = i;
        enterprise.shieldBalanceTimer = 120;
        return;
      }
    }
    enterprise.fire(0);
  } else {

    Iterator<Container> iterator = menu.containers.iterator();

    int counter = 0;
    while (iterator.hasNext()) {
      Container tmpContainer = iterator.next();

      if (menu.currentContainer == counter) {
        // found our container, check if we're over a button and click it.
        Iterator<Button> buttonIterator = tmpContainer.buttons.iterator();
        while (buttonIterator.hasNext()) {
          Button tmpButton = buttonIterator.next();
          if (tmpButton.mouseOver()) {
            tmpButton.execute();
            break;
          }
        }
        break;
      }
      counter++;
    }
  }
}



class Weapon {

  SoundEffect sound;
  Ship ship;
  float fireRate;
  int counter;
  float damage;
  int spread;
  int type;
  float split;
  float speed;
  boolean buffered;

  Weapon(Ship myShip, float rate, float spe, float dam, int sp, float spl, SoundEffect sou, int myType) {
    ship = myShip;
    fireRate = rate;
    damage = dam;
    spread = sp;
    split = spl;
    speed = spe;
    sound = sou;
    type = myType;
  }

  public void update() {

    counter++;
  }

  public boolean canFire(boolean buffer) {

    if (buffer == true)
      if (counter >= 0.8f * fireRate * 60f)
        buffered = buffer;

    return ( counter >= fireRate * 60f);
  }

  public void fire() {

    // So you can't fire on top of each other
    if (abs(warbird.x - enterprise.x) < enterprise.shieldRadiusX() && abs(warbird.y - enterprise.y) < enterprise.shieldRadiusY()) return;
    
    float vol = 0f;
    float newDamage = damage;

    if (ship.id != 0) {

      if (difficultyHard == true) {
        newDamage *= 1.5f;
      }
    } else {

      if (difficultyHard == false) {
        newDamage *= 1.5f;
      }
    }

    if (type == 2)
    {
      ship.projectiles.add(new Projectile(ship.getShieldX(160), ship.getShieldY(160), speed, ship.rotation + 180, 20f, newDamage, spread, split, ship.id));
      ship.projectiles.add(new Projectile(ship.getShieldX(200), ship.getShieldY(200), speed, ship.rotation + 180, 20f, newDamage, spread, split, ship.id));
      vol = 0.3f;
    } else if (type == 1) {

      ship.projectiles.add(new QuantumTorpedo(ship.getShieldX(180), ship.getShieldY(180), speed, ship.rotation + 180, 20f, newDamage, spread, split, ship.id));
      vol = 0.9f;
      
    } else if (type == 0) {


      float angle = atan2(mouseY - ((ship.shipWidth() * 0.7f) * sin((ship.rotation + 90)* PI / 180)) - height/2 + (anchor.y - enterprise.y), mouseX - ((ship.shipWidth() * 0.7f) * cos((ship.rotation + 90) * PI / 180)) - width/2 + (anchor.x - enterprise.x)) * 180 / PI + 90;

      ship.projectiles.add(new Phaser(ship.x + (ship.shipWidth() * 0.7f) * cos((ship.rotation + 90) * PI / 180), 
        ship.y + (ship.shipWidth() * 0.7f) * sin((ship.rotation + 90)* PI / 180), 
        speed, angle, 25f, newDamage, spread, split, ship.id));
      vol = 0.12f;
    }

    sound.play(vol);
    counter = 0;
    buffered = false;
  }
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Main" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
