package com.me.yin_yang;

import com.me.test.IActivityRequestHandler;
import com.me.test.TestGame;

public class DesktopTestGame extends TestGame {

	private DevAgentServer agentServer;

	public DesktopTestGame(IActivityRequestHandler platform) {
		super(platform);
	}

	@Override
	public void create() {
		super.create();
		if (getNative() instanceof Main && ((Main) getNative()).agent()) {
			agentServer = DevAgentServer.start(this);
		}
	}

	@Override
	public void dispose() {
		if (agentServer != null) {
			agentServer.stop();
			agentServer = null;
		}
		super.dispose();
	}
}
