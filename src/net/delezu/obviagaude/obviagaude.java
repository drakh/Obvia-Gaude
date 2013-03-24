package net.delezu.obviagaude;

import processing.core.*;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;

import controlP5.*;
import ketai.ui.*;
import ketai.sensors.*;
import ketai.data.*;
import processing.opengl.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class obviagaude extends PApplet {

	String[] texts = { "Obvia", "Gaude" };
	String[] names = { "Evula", "Paulo" };
	String t;
	String n;
	String ft;
	PFont f;
	int fs = 42;
	int tl = 0;
	int nl = 0;
	int fl = 0;
	int c = 0;
	int tc = 0;
	int tcc = 0;
	int nc = 0;
	int r = 0;
	float rot = 0;
	float rot2 = 0;
	int sze;
	ControlP5 cp5;
	PGraphics pg;
	Textfield hername_i;
	Textfield hisname_i;

	float accM;

	KetaiSensor sensor;
	float accelerometerX, accelerometerY, accelerometerZ;

	public void reinit() {
		c = 0;
		t = new String(join(texts, ""));
		n = new String(join(names, ""));
		String[] tmpt = { t, n };
		ft = new String(join(tmpt, ""));
		tl = t.length();
		nl = n.length();
		fl = tl + nl;
		int wdth = displayWidth;
		int hgth = displayHeight;
		sze = wdth;
		if (wdth > hgth) {
			sze = hgth;
		}
		fs = sze / ((nl) * 2) + 2;
	}

	@Override
	public void setup() {
		sensor = new KetaiSensor(this);
		sensor.start();
		orientation(LANDSCAPE);

		if (displayWidth > displayHeight) {
			pg = createGraphics(displayHeight, displayHeight, OPENGL);
		} else {
			pg = createGraphics(displayWidth, displayWidth, OPENGL);
		}
		f = loadFont("CourierNewPSMT-40.vlw");
		reinit();
		frameRate(10);
		smooth();
		rectMode(CORNER);
		buildGUI();
	}

	public void draw() {
		textFont(f);
		textAlign(CENTER, CENTER);
		textSize(fs);
		background(255);

		pg.beginDraw();
		pg.textFont(f);
		pg.textAlign(CENTER, CENTER);
		pg.textSize(fs);
		pg.background(255);
		int l = c;
		if (c > fl) {
			l = fl;
			if (r >= tl)
				r = 0;
		}
		for (int k = 0; k < l; k++) {
			for (int i = 0; i <= k; i++) {
				int j = k - i;
				String ca = ft.substring(k, k + 1);
				if (k == r || k == r + tl || k == r + fl - 1
						|| k == r + tl + names[0].length() || k == r + tl - 1) {
					ca = ca.toUpperCase();
					pg.fill(200, 0, 0);
					pg.stroke(200, 0, 0);
				} else {
					pg.fill(0);
					pg.stroke(0);
				}
				pg.pushMatrix();
				pg.translate(sze / 2 + j * fs, sze / 2 + i * fs, sin(rot) * 15
						* i * j + accM);
				if (j != 0 && i != 0) {
					pg.rotateZ(rot2);
					pg.rotateX(rot);
				} else {
					pg.rotateZ((rot + rot2) / 2);
				}
				pg.text(ca, 0, 0);
				pg.popMatrix();
				if (i > 0) {
					pg.pushMatrix();
					pg.translate(sze / 2 + j * fs, sze / 2 - i * fs, sin(rot)
							* 15 * i * j + accM);
					if (j != 0 && i != 0) {
						pg.rotateZ(rot);
						pg.rotateX(rot2);
					} else {
						pg.rotateZ((rot + rot2) / 2);
					}
					pg.text(ca, 0, 0);
					pg.popMatrix();
				}

				if (j > 0 && i < k) {
					pg.pushMatrix();
					pg.translate(sze / 2 - j * fs, sze / 2 + i * fs, sin(rot)
							* 15 * i * j + accM);
					if (i != 0) {
						pg.rotateY(rot);
						pg.rotateX(rot2);
					} else {
						pg.rotateZ((rot + rot2) / 2);
					}
					pg.text(ca, 0, 0);
					pg.popMatrix();
					if (i > 0) {
						pg.pushMatrix();
						pg.translate(sze / 2 - j * fs, sze / 2 - i * fs,
								sin(rot) * 15 * i * j + accM);
						if (j != 0) {
							pg.rotateY(rot2);
							pg.rotateX(rot);
						} else {
							pg.rotateZ((rot + rot2) / 2);
						}
						pg.text(ca, 0, 0);
						pg.popMatrix();
					}
				}
			}
		}
		if (c <= ft.length() - 1)
			c++;
		pg.endDraw();
		image(pg, (displayWidth - sze) / 2, 0);
	}

	public void set() {
		names[0] = hername_i.getText();
		names[1] = hisname_i.getText();
		reinit();
	}

	public void buildGUI() {
		cp5 = new ControlP5(this, f);
		hername_i = cp5.addTextfield("hername").setPosition(20, 60)
				.setSize(fs * 5, fs).setFont(f).setColorBackground(color(255))
				.setColorCursor(color(0)).setColorForeground(color(0))
				.setColorActive(color(0)).setText(names[0])
				.setLabelVisible(false).setColor(color(0));
		cp5.addTextlabel("hernamelabel").setText("Her name: ")
				.setPosition(20, 20).setColorValue(color(0)).setFont(f);

		hisname_i = cp5.addTextfield("hisname").setPosition(20, 160)
				.setSize(fs * 5, fs).setFont(f).setLabelVisible(false)
				.setColorBackground(color(255)).setColorCursor(color(0))
				.setColorForeground(color(0)).setText(names[1])
				.setColorActive(color(0)).setColor(color(0));
		cp5.addTextlabel("hisnamelabel").setText("His name: ")
				.setPosition(20, 120).setColorValue(color(0)).setFont(f);

		cp5.addBang("set").setPosition(20, 220).setSize(80, 40)
				.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
	}

	public void onAccelerometerEvent(float x, float y, float z) {
		accelerometerX = x;
		accelerometerY = y;
		accelerometerZ = z;
		accM = (accelerometerX * accelerometerX + accelerometerY
				* accelerometerY + accelerometerZ * accelerometerZ) * 2;
	}

	public void mouseDragged() {
		rot = map(mouseX, width / 2, width - 50, 0, PI);
		rot2 = map(mouseY, height / 2, height - 50, 0, PI);
	}

	public int sketchWidth() {
		return displayWidth;
	}

	public int sketchHeight() {
		return displayHeight;
	}

	public String sketchRenderer() {
		return OPENGL;
	}
}