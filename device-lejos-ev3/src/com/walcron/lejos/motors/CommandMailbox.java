package com.walcron.lejos.motors;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public enum CommandMailbox {
	INSTANCE;
	
	private final Queue<String> commands = new LinkedList<>();
	
	public void addCommand(String command) {
		commands.add(command);
	}
	
	public Optional<String> getCommand() {
		if(!commands.isEmpty()) {
			return Optional.of(commands.remove());
		}
		return Optional.empty();
	}
}
