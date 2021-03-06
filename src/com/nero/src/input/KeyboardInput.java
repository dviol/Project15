package com.nero.src.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.nero.src.Game;
import com.nero.src.objects.Player;

public class KeyboardInput extends KeyAdapter {

	Player p;
	Game g;

	// Das der KeyInput ueberhaupt funktioniert

	public KeyboardInput(Player p, Game g) {

		this.p = p;
		this.g = g;
	}

	@Override
	public void keyPressed(KeyEvent e) {

		p.keyPressed(e);
		g.keyPressed(e);

	}

	@Override
	public void keyReleased(KeyEvent e) {

		p.keyReleased(e);

	}

	@Override
	public void keyTyped(KeyEvent e) {

		p.keyTyped(e);

	}

}
