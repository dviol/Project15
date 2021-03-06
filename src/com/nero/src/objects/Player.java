package com.nero.src.objects;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import com.nero.helper.Exit;
import com.nero.helper.ScrollerDown;
import com.nero.helper.ScrollerLeft;
import com.nero.helper.ScrollerRight;
import com.nero.helper.ScrollerUp;
import com.nero.helper.Sound;
import com.nero.src.Game;
import com.nero.src.input.Controller;
import com.nero.src.input.EnviromentallyMoved;
import com.nero.src.input.InternerLvLcreator;
import com.nero.src.objects.doors.DoorBlau;
import com.nero.src.objects.doors.DoorGelb;
import com.nero.src.objects.doors.DoorGruen;
import com.nero.src.objects.doors.DoorOrange;
import com.nero.src.objects.doors.DoorPink;
import com.nero.src.objects.doors.DoorSilber;
import com.nero.src.objects.doors.keys.KeyBlau;
import com.nero.src.objects.doors.keys.KeyGelb;
import com.nero.src.objects.doors.keys.KeyGruen;
import com.nero.src.objects.doors.keys.KeyOrange;
import com.nero.src.objects.doors.keys.KeyPink;
import com.nero.src.objects.doors.keys.KeySilber;

public class Player extends MoveablePaintable {
	// TODO
	// // Malfunktion
	// public void paint(Graphics2D g2d) {
	//
	// g2d.drawImage(getImage(),pos.x,pos.y - 13, null); Warum -13?
	//
	// }

	// Ausmasse + woher kriegt er sein Bildchen her

	public static final int playerWidth = 33;
	public static final int playerHeight = 50;

	// Attribute wie schnell der Typ ist
	private int bewegungsgeschwindigkeit = 5;
	private int sprungkraft = 0;
	private int sprunkonstante = 15;
	private int springer = sprunkonstante;
	private boolean allowSpace = true;
	public static boolean darfRechts, darfLinks = true;

	public static boolean SchluesselBlau = false;
	public static boolean SchluesselGelb = false;
	public static boolean SchluesselGruen = false;
	public static boolean SchluesselOrange = false;
	public static boolean SchluesselPink = false;
	public static boolean SchluesselSilber = false;

	private int maxFallingSpeed = 6;

	// Faellt der oder nicht?

	private boolean falling = true;
	private boolean init = true;
	private boolean boden = true;

	Sound w;
	Sound s;

	private final int gravity = 1;
	private int fallingSpeed = 0;
	private boolean nachRechtsGucken = true;

	// private String springenLinksBild
	// ="/images/spielfigur/spielfigurSchuss.gif";
	private final static String imagePathStehen = "/images/spielfigur/spielfigurStehen.gif";
	private final static String fallenLinksBild = "/images/spielfigur/Spieler_links_springen.png";
	private final static String stehenLinksBild = "/images/spielfigur/Spielerlinks.png";
	private final static String laufenRechtsBild = "/images/spielfigur/Spieler_rechts_laufen_animiert.gif";

	// private String springenRechtsBild=springenLinksBild;
	private final static String fallenRechtsBild = "/images/spielfigur/Spieler_rechts_springen.png";
	private final static String stehenRechtsBild = "/images/spielfigur/Spielerrechts.png";
	private final static String laufenLinksBild = "/images/spielfigur/Spieler_links_laufen_animiert.gif";

	// Jetzt wird die BlockListe eingefuegt

	public Player(int x, int y) {
		super(x, y, playerWidth, playerHeight, imagePathStehen);
		w = new Sound("/Sound/laufen.wav");
		s = new Sound("/Sound/Sprung.wav");
	}

	public Player(Point p) {
		this(p.x, p.y);
	}

	public void update(LinkedList<Block> b, LinkedList<Ghostly> e, LinkedList<Coin> co, Exit ex, ScrollerUp su,
			ScrollerRight sr, ScrollerDown sd, ScrollerLeft sl, DoorGelb dg, DoorBlau db, DoorGruen dgr, DoorOrange dor,
			DoorPink dp, DoorSilber ds, KeyBlau kb, KeyGelb kg, KeyGruen kgr, KeyOrange kor, KeyPink kp, KeySilber ks) {

		// Das er sich ueberhaupt mit der Geschwindigkeit bewegt + Collission
		Collission(b, e, ex, su, sr, sd, sl, co, dg, db, dgr, dor, dp, ds, kb, kg, kgr, kor, kp, ks);
		pos.x += velX;
		pos.y += velY;
		pos.y += fallingSpeed;
		pos.y -= sprungkraft;

		// Falle ich?
		if (falling) {

			if (nachRechtsGucken) {
				imagePath = fallenRechtsBild;
			} else {
				imagePath = fallenLinksBild;
			}

			fallingSpeed += gravity;

			if (fallingSpeed > sprungkraft) {

				fallingSpeed = sprungkraft + maxFallingSpeed;

			}

		} else {
			if (velX == 0) {
				if (nachRechtsGucken) {
					imagePath = stehenRechtsBild;
				} else {
					imagePath = stehenLinksBild;
				}
			}

		}

	}

	// Kollissionfunktion mit Gegner ,BlICHWAREINUMLAUTcken und Exit und was dann
	// passiert
	public void Collission(LinkedList<Block> b, LinkedList<Ghostly> e, Exit ex, ScrollerUp su, ScrollerRight sr,
			ScrollerDown sd, ScrollerLeft sl, LinkedList<Coin> co, DoorGelb dg, DoorBlau db, DoorGruen dgr,
			DoorOrange dor, DoorPink dp, DoorSilber ds, KeyBlau kb, KeyGelb kg, KeyGruen kgr, KeyOrange kor, KeyPink kp,
			KeySilber ks) {

		for (int i = 0; i < co.size(); i++) {

			if (getBounds().intersects(co.get(i).getBounds())) {

				Controller.removeCoin(co.get(i));
				i = co.size();
				Game.Score++;

			} else {

			}

		}

		for (int i = 0; i < b.size(); i++) {

			if (getBounds().intersects(b.get(i).getObstacleBoundsOben())) {

				i = b.size();
				falling = false;
				fallingSpeed = 0;
				boden = true;

			}

			else {
				falling = true;
				boden = false;

			}

		}
		// Kollission Unten

		for (int i = 0; i < b.size(); i++) {

			if (getBounds().intersects(b.get(i).getObstacleBoundsUnten())) {

				i = b.size();
				falling = true;
				fallingSpeed = sprungkraft + maxFallingSpeed;

			}

		}

		// Kollission Rechts und Links

		for (int i = 0; i < b.size(); i++) {

			if (getBounds().intersects(b.get(i).getObstacleBoundsRechts())) {

				pos.x += bewegungsgeschwindigkeit;

			} else if (getBounds().intersects(b.get(i).getObstacleBoundsLinks())) {

				pos.x -= bewegungsgeschwindigkeit;

			}

		}

		// Wenn ich getroffen wered, kill das spiel
		for (int i = 0; i < e.size(); i++) {

			if (getBounds().intersects(e.get(i).getBounds())) {
				Game.Score = 0;
				Controller.removeAllExceptNotList();
				InternerLvLcreator.internerLvlControl();
				break;
			}

		}
		// Wenn Exit getroffen erh�he den Level
		if (getBounds().intersects(ex.getBounds())) {

			++Game.level;
			Controller.removeAllExceptNotList();
			InternerLvLcreator.internerLvlControl();

		}

		if (!getBounds().intersects(sd.getBounds()) ^ !getBounds().intersects(su.getBounds())) {

			EnviromentallyMoved.environmentVelY = 0;
			springer = sprunkonstante;
			allowSpace = true;

		}
		if (!getBounds().intersects(su.getBounds())) {

			EnviromentallyMoved.environmentVelY = 0;
			springer = sprunkonstante;
			allowSpace = true;
		}

		if (getBounds().intersects(su.getBounds())) {

			allowSpace = false;

			if (springer > 0) {
				springer -= fallingSpeed + 2;
			}

			EnviromentallyMoved.environmentVelY = springer;

		}

		if (getBounds().intersects(sd.getBounds())) {

			if (falling) {

				fallingSpeed = 0;
				EnviromentallyMoved.environmentVelY = -maxFallingSpeed;

			}

		}

		if (!getBounds().intersects(sl.getBounds()) && !getBounds().intersects(sr.getBounds())) {
			EnviromentallyMoved.environmentVelX = 0;

		}

		if (getBounds().intersects(sr.getBounds())) {

			EnviromentallyMoved.environmentVelX = -bewegungsgeschwindigkeit;

			pos.x -= bewegungsgeschwindigkeit;

		}

		if (getBounds().intersects(sl.getBounds())) {

			EnviromentallyMoved.environmentVelX = bewegungsgeschwindigkeit;

			pos.x += bewegungsgeschwindigkeit;

		}

		// ----------------------
		if (!SchluesselBlau) {
			if (getBounds().intersects(db.getDoorBoundsRechts())) {

				pos.x += bewegungsgeschwindigkeit;
				System.out.println("Collission");
			} else if (getBounds().intersects(db.getDoorBoundsLinks())) {

				pos.x -= bewegungsgeschwindigkeit;

			}
		} else if (SchluesselBlau) {
			if (getBounds().intersects(db.getDoorBoundsRechts())) {
				Controller.returnDoorBlau().switchPictures();
			}
		}

		// ----------------------
		// ----------------------
		if (!SchluesselGelb) {
			if (getBounds().intersects(dg.getDoorBoundsRechts())) {

				pos.x += bewegungsgeschwindigkeit;

			} else if (getBounds().intersects(dg.getDoorBoundsLinks())) {

				pos.x -= bewegungsgeschwindigkeit;

			}
		} else if (SchluesselGelb) {
			if (getBounds().intersects(dg.getDoorBoundsRechts())) {
				Controller.returnDoorGelb().switchPictures();
			}
		}

		// ----------------------//----------------------
		if (!SchluesselGruen) {
			if (getBounds().intersects(dgr.getDoorBoundsRechts())) {

				pos.x += bewegungsgeschwindigkeit;

			} else if (getBounds().intersects(dgr.getDoorBoundsLinks())) {

				pos.x -= bewegungsgeschwindigkeit;

			}
		} else if (SchluesselGruen) {
			if (getBounds().intersects(dgr.getDoorBoundsRechts())) {
				Controller.returnDoorGruen().switchPictures();
			}
		}

		// ----------------------//----------------------
		if (!SchluesselOrange) {
			if (getBounds().intersects(dor.getDoorBoundsRechts())) {

				pos.x += bewegungsgeschwindigkeit;

			} else if (getBounds().intersects(dor.getDoorBoundsLinks())) {

				pos.x -= bewegungsgeschwindigkeit;

			}
		} else if (SchluesselOrange) {
			if (getBounds().intersects(dor.getDoorBoundsRechts())) {
				Controller.returnDoorOrange().switchPictures();
			}
		}

		// ----------------------//----------------------
		if (!SchluesselPink) {
			if (getBounds().intersects(dp.getDoorBoundsRechts())) {

				pos.x += bewegungsgeschwindigkeit;

			} else if (getBounds().intersects(dp.getDoorBoundsLinks())) {

				pos.x -= bewegungsgeschwindigkeit;

			}
		} else if (SchluesselPink) {
			if (getBounds().intersects(dp.getDoorBoundsRechts())) {
				Controller.returnDoorPink().switchPictures();
			}
		}

		// ----------------------//----------------------
		if (!SchluesselSilber) {
			if (getBounds().intersects(ds.getDoorBoundsRechts())) {

				pos.x += bewegungsgeschwindigkeit;

			} else if (getBounds().intersects(ds.getDoorBoundsLinks())) {

				pos.x -= bewegungsgeschwindigkeit;

			}
		} else if (SchluesselSilber) {
			if (getBounds().intersects(ds.getDoorBoundsRechts())) {
				Controller.returnDoorSilber().switchPictures();
			}
		}

		// ----------------------
		if (getBounds().intersects(kb.getBounds())) {
			SchluesselBlau = true;
			Controller.returnKeyBlau().hideKey();
		}
		if (getBounds().intersects(kg.getBounds())) {

			SchluesselGelb = true;
			Controller.returnKeyGelb().hideKey();
		}

		if (getBounds().intersects(kgr.getBounds())) {
			SchluesselGruen = true;
			Controller.returnKeyGruen().hideKey();
		}

		if (getBounds().intersects(kor.getBounds())) {
			SchluesselOrange = true;
			Controller.returnKeyOrange().hideKey();
		}

		if (getBounds().intersects(kp.getBounds())) {
			SchluesselPink = true;
			Controller.returnKeyPink().hideKey();
		}

		if (getBounds().intersects(ks.getBounds())) {
			SchluesselSilber = true;
			Controller.returnKeySilber().hideKey();
		}

	}

	// Das man sich ueberhaupt bewegen kann
	public void keyPressed(KeyEvent e) {

		switch (e.getKeyCode()) {

		case KeyEvent.VK_RIGHT:
			nachRechtsGucken = true;
			velX = bewegungsgeschwindigkeit;

			if (init) {
				if (boden) {
					w.playSound();
				} else {
					w.stopSound();
				}
				init = false;
			}

			if (!falling) {

				imagePath = laufenRechtsBild;

			}
			break;
		case KeyEvent.VK_LEFT:
			nachRechtsGucken = false;

			imagePath = laufenLinksBild;
			if (init) {
				if (boden) {
					w.playSound();
				} else {
					w.stopSound();
				}
				init = false;
			}
			velX = -bewegungsgeschwindigkeit;

			break;
		case KeyEvent.VK_SPACE:

			if (allowSpace) {

				if (!falling) {
					if (boden) {
						// s.playSoundOnce();
						// boden = false;
					}
					sprungkraft = sprunkonstante;

				} else {

					sprungkraft = 0;

				}
			}

			break;
		case KeyEvent.VK_U:
			Controller.removeAllExceptNotList();
			++Game.level;
			InternerLvLcreator.internerLvlControl();
			break;
		case KeyEvent.VK_D:
			Controller.removeAllExceptNotList();
			--Game.level;
			InternerLvLcreator.internerLvlControl();
			break;

		}

	}

	public void keyTyped(KeyEvent e) {

		switch (e.getKeyCode()) {

		}

	}

	public void keyReleased(KeyEvent e) {

		switch (e.getKeyCode()) {

		case KeyEvent.VK_R:
			Controller.removeAllExceptNotList();
			InternerLvLcreator.internerLvlControl();
			break;

		case KeyEvent.VK_RIGHT:
			imagePath = stehenRechtsBild;
			velX = 0;
			init = true;
			w.stopSound();

			break;
		case KeyEvent.VK_LEFT:
			imagePath = stehenLinksBild;
			velX = 0;
			init = true;
			w.stopSound();

			break;
		case KeyEvent.VK_SPACE:

			if (!nachRechtsGucken) {
				imagePath = fallenLinksBild;
			} else {
				imagePath = fallenRechtsBild;
			}

			sprungkraft = 0;

			break;

		}

	}

	// Kollissionsviereck
	@Override
	public Rectangle getBounds() {

		return new Rectangle(pos.x, pos.y, playerWidth, playerHeight + 1);
	}

	// Hol das bild her!

	public int returnPlayerX() {

		return pos.x;
	}

	public int returnPlayerY() {

		return pos.y;

	}

	public void deleteInventory() {
		SchluesselBlau = false;
		SchluesselGelb = false;
		SchluesselGruen = false;
		SchluesselOrange = false;
		SchluesselPink = false;
		SchluesselSilber = false;
	}

}
