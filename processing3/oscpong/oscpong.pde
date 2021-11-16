/**
 * OSC Data Pong. Original by Nausk! modified by Heinze Havinga for databending
 * 02-09-2011
 * Nausk!: http://www.openprocessing.org/portal/?userID=5701
 * Heinze Havinga: http://www.leftfootmedia.nl
 *
 *
 *
 *
 **/

// Play the original @ http://www.openprocessing.org/visuals/?visualID=13565

import oscP5.*;
import netP5.*;

NetAddress myBroadcastLocation; 
OscP5 oscP5;
OscMessage myOscMessage;

// Incoming port to listen to
int port = 7002; 

PFont score;

int mult = 1;
float ballX;
float ballY;
float speedX = 2.0;
float speedY = 2.0;
float maxSpeedX = 8.0;
float maxSpeedY = 4.5;
int directionX = 1;
int directionY = 1;
int ballSize = 6;
int RpaddleYPos, LpaddleYPos, RpaddleXPos, LpaddleXPos;
int paddleHeight = 40;
int Rscore = 0;
int Lscore = 0;
int paddleOffset = 15;
int maxScore = 100;

float targetLP;
float targetRP;
float LPeasing = 0.16;

void setup() {
  background(0);
  surface.setSize(400 * mult, 300 * mult);
  //smooth();
  rectMode(CENTER);
  noStroke();
  score = loadFont("04b03-48.vlw");
  textFont(score);
  textSize(48 * mult);
  ballX = width / 2;
  ballY = height / 2;

  oscP5 = new OscP5(this, port);
  myBroadcastLocation = new NetAddress("localhost", 9999);

  myOscMessage = new OscMessage("/pong/out/inputs");
  /* add a value (an integer) to the OscMessage */
  myOscMessage.add("paddle1,paddle2,ballspeedX,ballspeedY");

  /* send the OscMessage to a remote location specified in myNetAddress */
  oscP5.send(myOscMessage, myBroadcastLocation);

  myOscMessage = new OscMessage("/pong/out/outputs");
  /* add a value (an integer) to the OscMessage */
  myOscMessage.add("score,ballx,bally,gameover,bounce,gamestart");

  /* send the OscMessage to a remote location specified in myNetAddress */
  oscP5.send(myOscMessage, myBroadcastLocation);

  myOscMessage = new OscMessage("/pong/out/port");
  /* add a value (an integer) to the OscMessage */
  myOscMessage.add(port);
  println("port " + port);
  oscP5.send(myOscMessage, myBroadcastLocation);
}

void draw() {
  gameStart();
  if (millis() > 4000) {
    inGame();
  }
}

void inGame() {
  background(0);
  LPeasing = random(0.15, 0.2);

  // DRAW NET
  for (int n = 0; n <= height; n += height / 12) {
    rect (width / 2, n, 5 * mult, height / 18);
  }

  //SCORE

  String scoreR = ""+Rscore;
  if (ballX > width - 4) {
    Rscore++;
    myOscMessage = new OscMessage("/pong/out/score");
    myOscMessage.add(1.0);
    oscP5.send(myOscMessage, myBroadcastLocation);
    text(scoreR, width-width/4, 40 * mult);
    delay(10);
    background(0);
    ballX = width/2;
    ballY = height/2;
    speedX = 2.0;
    speedY = 2.0;
  }
  textSize(48);
  text(scoreR, width-width / 4, 40 * mult); // RIGHT SCORE


  String scoreL = ""+Lscore;
  if (ballX < 4) {
    Lscore++;
    myOscMessage = new OscMessage("/pong/out/score");
    myOscMessage.add(1.0);
    oscP5.send(myOscMessage, myBroadcastLocation);
    text(scoreL, width / 4, 40 * mult);
    delay(10);
    ballX = width / 2;
    ballY = height / 2;
    speedX = 2.0;
    speedY = 2.0;
  }
  textSize(48);
  text(scoreL, width / 4, 40 * mult); // LEFT SCORE




  //BALL
  if (ballX < 0 || ballX > width) {
    directionX = -directionX;
  }

  if (ballY < 0 || ballY > height) {
    directionY = -directionY;
  }

  ballX = ballX + speedX * directionX;

  myOscMessage = new OscMessage("/pong/out/ballx");
  myOscMessage.add((float) ballX / width);
  oscP5.send(myOscMessage, myBroadcastLocation);

  ballY = ballY + speedY * directionY;

  myOscMessage = new OscMessage("/pong/out/bally");
  myOscMessage.add((float) ballY / height);
  oscP5.send(myOscMessage, myBroadcastLocation);

  rect (ballX, ballY, ballSize * mult, ballSize * mult);

  // RIGHT PADDLE


  RpaddleYPos += (targetRP - RpaddleYPos) * LPeasing;
  RpaddleXPos = width - paddleOffset * mult;
  rect( RpaddleXPos, RpaddleYPos, 6 * mult, paddleHeight * mult);
  if (ballY >= RpaddleYPos - paddleHeight / 2 && ballY <= RpaddleYPos + paddleHeight / 2 && ballX >= RpaddleXPos-ballSize / 2) {
    directionX = -directionX;
    speedX += 0.5;
    speedY += 0.5;
    myOscMessage = new OscMessage("/pong/out/bounce");
    myOscMessage.add(1.0);
    oscP5.send(myOscMessage, myBroadcastLocation);
  }

  // LEFT PADDLE

  LpaddleYPos += (targetLP - LpaddleYPos) * LPeasing;
  LpaddleXPos = paddleOffset * mult;
  rect (LpaddleXPos, LpaddleYPos, 6 * mult, paddleHeight * mult);
  if (ballY >= LpaddleYPos - paddleHeight / 2 && ballY <= LpaddleYPos + paddleHeight / 2 && ballX <= LpaddleXPos+ballSize / 2) {
    directionX = -directionX;
    speedX += 0.5;
    speedY += 0.5;
    myOscMessage = new OscMessage("/pong/out/bounce");
    myOscMessage.add(1.0);
    oscP5.send(myOscMessage, myBroadcastLocation);
  }

  if (speedX >= maxSpeedX) {
    speedX = maxSpeedX;
  }
  if (speedY >= maxSpeedY) {
    speedY = maxSpeedY;
  }

  if (Rscore >= maxScore || Lscore >= maxScore) {
    gameOver();
  }
}

void gameOver() {
  myOscMessage = new OscMessage("/pong/out/gameover");
  myOscMessage.add(1.0);
  oscP5.send(myOscMessage, myBroadcastLocation);

  Rscore = 0;
  Lscore = 0;
  myOscMessage = new OscMessage("/pong/out/gamestart");
  myOscMessage.add(1.0);
  oscP5.send(myOscMessage, myBroadcastLocation);
}

void gameStart() {
  background(0);
  textSize(32);
  text("AZUKI PONG", width / 2 - 160, height / 2);
  textSize(20);
  text("by: Nausk!", width / 2 - 80, height / 2 + 20);
  text("made patchable by Heinze!", width / 2 - 100, height / 2 + 60);
}

void mousePressed () {
  myOscMessage = new OscMessage("/pong/out/gamestart");
  myOscMessage.add(1.0);
  oscP5.send(myOscMessage, myBroadcastLocation);
  Rscore = 0;
  Lscore = 0;
  loop();
}

void oscEvent(OscMessage theOscMessage) {
  //  println("### received an osc message with addrpattern "+theOscMessage.addrPattern()+" and typetag "+theOscMessage.typetag());
  //  theOscMessage.print(); 

  String addrPattern = theOscMessage.addrPattern();
  //Split up the adress into pieces we can check
  String[] address = split(addrPattern, '/');  
  String installationName= (String) address[1];
  String signalType= (String) address[2];
  String addressName = (String) address[3];
  //  println("adressName "+ adressName);
  if (addressName.equals("paddle1")) {
    if (theOscMessage.checkTypetag("f")) { 
      targetLP = theOscMessage.get(0).floatValue() * height;
    }
  } else if (addressName.equals("paddle2")) {
    if (theOscMessage.checkTypetag("f")) { 
      targetRP = theOscMessage.get(0).floatValue() * height;
    }
  } else if (addressName.equals("ballspeedX")) {
    if (theOscMessage.checkTypetag("f")) { 
      speedX = theOscMessage.get(0).floatValue() * 8.0 - 4.0;
    }
  } else if (addressName.equals("ballspeedY")) {
    if (theOscMessage.checkTypetag("f")) { 
      speedY = theOscMessage.get(0).floatValue() * 8.0 - 4.0;
    }
  }
}
