import processing.sound.*;
import java.util.*;

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

void setup() {

  frameRate(60);
  imageMode(CENTER);
  fullScreen();
  textSize(50);
  background(#000000);
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

void draw() {

  //draw Space
  background(#000000);
  noStroke();
  fill(#ffffff);
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
    fill(#ffffff);

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

void startGame() {

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
  enterprise.weapons.add(new Weapon(enterprise, 0.45, 85f, 35f, 80, 0.9, phaser_blast, 0));
  enterprise.weapons.add(new Weapon(enterprise, 0.6, 25f, 90f, 20, 0.4, quantum_torpedo, 1));
  quantumTorpedo = loadImage("quantum_torpedo.PNG");
  ships.add(enterprise);


  // create warbird
  float warbirdSpeed = 9.5;
  if (difficultyHard) warbirdSpeed += 2.5f;
  warbird = new Ship(400f, 200f, 0f, loadImage("romulan_warbird.PNG"), 0.3f, (int)warbirdSpeed, 0.9f, 1);
  warbird.id = 1;
  warbird.weapons.add(new Weapon(warbird, 0.55, 45f, 35f, 80, 0.7, disruptor_blast, 2));
  ships.add(warbird);

  moving[0] = false;
  moving[1] = false;
  moving[2] = false;
  moving[3] = false;

  // create effects
  effects = new LinkedList<Effect>();

  anchor = enterprise;

  spaceNoise.loop(0.8);
  battleMusic.loop(0.5f);

  // create shield balance rectangles
  shieldBalanceRectangles = new Rectangle[4];
  float imgWidth =  height / 5 * 0.38 * 0.8;
  float imgHeight = height / 5 * 0.8;
  shieldBalanceRectangles[0] = new Rectangle((width / 5 + height / 5 * 0.38 * 0.8 * 2) - imgWidth * 1.1f, (height - height / 7.5) - imgHeight / 2, (int)(imgWidth / 2), (int)imgHeight);
  shieldBalanceRectangles[1] = new Rectangle((width / 5 + height / 5 * 0.38 * 0.8 * 2) + imgWidth * 0.62f, (height - height / 7.5) - imgHeight / 2, (int)(imgWidth / 2), (int)imgHeight);
  shieldBalanceRectangles[2] = new Rectangle((width / 5 + height / 5 * 0.38 * 0.8 * 2) - imgWidth / 2, (height - height / 7.5) - imgHeight * 0.65, (int)imgWidth, (int)imgHeight / 6);
  shieldBalanceRectangles[3] = new Rectangle((width / 5 + height / 5 * 0.38 * 0.8 * 2) - imgWidth / 2, (height - height / 7.5) + imgHeight * 0.5, (int)imgWidth, (int)imgHeight / 6);
}
