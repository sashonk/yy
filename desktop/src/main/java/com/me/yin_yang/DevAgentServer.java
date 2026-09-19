package com.me.yin_yang;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import com.badlogic.gdx.Gdx;
import com.me.test.TestGame;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class DevAgentServer {

	public static final int PORT = 8765;
	private static final Charset UTF8 = Charset.forName("UTF-8");

	private final TestGame game;
	private HttpServer server;

	public static DevAgentServer start(TestGame game) {
		DevAgentServer agent = new DevAgentServer(game);
		agent.bind();
		return agent;
	}

	private DevAgentServer(TestGame game) {
		this.game = game;
	}

	private void bind() {
		try {
			server = HttpServer.create(new InetSocketAddress("127.0.0.1", PORT), 0);
			server.createContext("/state", this::handleState);
			server.createContext("/move", this::handleMove);
			server.createContext("/click", this::handleClick);
			server.createContext("/start", this::handleStart);
			server.setExecutor(Executors.newSingleThreadExecutor());
			server.start();
			Gdx.app.log("DevAgentServer", "listening on 127.0.0.1:" + PORT);
		} catch (IOException e) {
			throw new RuntimeException("failed to start DevAgentServer", e);
		}
	}

	public void stop() {
		if (server != null) {
			server.stop(0);
			server = null;
		}
	}

	private void handleState(HttpExchange exchange) throws IOException {
		if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
			send(exchange, 405, "{\"error\":\"GET only\"}");
			return;
		}
		try {
			String json = onGdx(new Callable<String>() {
				@Override
				public String call() {
					return game.agentStateJson();
				}
			});
			send(exchange, 200, json);
		} catch (Exception e) {
			send(exchange, 500, errorJson(e));
		}
	}

	private void handleMove(HttpExchange exchange) throws IOException {
		if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
			send(exchange, 405, "{\"error\":\"POST only\"}");
			return;
		}
		final String dir = query(exchange.getRequestURI()).get("dir");
		if (dir == null || dir.length() == 0) {
			send(exchange, 400, "{\"error\":\"missing dir\"}");
			return;
		}
		try {
			onGdx(new Callable<String>() {
				@Override
				public String call() {
					game.agentMove(dir);
					return "ok";
				}
			});
			send(exchange, 200, "{\"ok\":true}");
		} catch (Exception e) {
			send(exchange, 400, errorJson(e));
		}
	}

	private void handleClick(HttpExchange exchange) throws IOException {
		if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
			send(exchange, 405, "{\"error\":\"POST only\"}");
			return;
		}
		final String name = query(exchange.getRequestURI()).get("name");
		if (name == null || name.length() == 0) {
			send(exchange, 400, "{\"error\":\"missing name\"}");
			return;
		}
		try {
			onGdx(new Callable<String>() {
				@Override
				public String call() {
					game.agentClick(name);
					return "ok";
				}
			});
			send(exchange, 200, "{\"ok\":true}");
		} catch (Exception e) {
			send(exchange, 400, errorJson(e));
		}
	}

	private void handleStart(HttpExchange exchange) throws IOException {
		if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
			send(exchange, 405, "{\"error\":\"POST only\"}");
			return;
		}
		final String levelStr = query(exchange.getRequestURI()).get("level");
		if (levelStr == null || levelStr.length() == 0) {
			send(exchange, 400, "{\"error\":\"missing level\"}");
			return;
		}
		final int level;
		try {
			level = Integer.parseInt(levelStr);
		} catch (NumberFormatException e) {
			send(exchange, 400, "{\"error\":\"bad level\"}");
			return;
		}
		try {
			onGdx(new Callable<String>() {
				@Override
				public String call() {
					game.agentStartLevel(level);
					return "ok";
				}
			});
			send(exchange, 200, "{\"ok\":true}");
		} catch (Exception e) {
			send(exchange, 400, errorJson(e));
		}
	}

	private String onGdx(final Callable<String> task) throws Exception {
		if (Gdx.app == null) {
			throw new IllegalStateException("gdx not ready");
		}
		final AtomicReference<String> result = new AtomicReference<String>();
		final AtomicReference<Throwable> error = new AtomicReference<Throwable>();
		final CountDownLatch latch = new CountDownLatch(1);
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				try {
					result.set(task.call());
				} catch (Throwable t) {
					error.set(t);
				} finally {
					latch.countDown();
				}
			}
		});
		if (!latch.await(5, TimeUnit.SECONDS)) {
			throw new IllegalStateException("gdx timeout");
		}
		if (error.get() != null) {
			Throwable t = error.get();
			if (t instanceof Exception) {
				throw (Exception) t;
			}
			throw new RuntimeException(t);
		}
		return result.get();
	}

	private static Map<String, String> query(URI uri) {
		Map<String, String> map = new HashMap<String, String>();
		String raw = uri.getRawQuery();
		if (raw == null || raw.length() == 0) {
			return map;
		}
		String[] parts = raw.split("&");
		for (String part : parts) {
			int eq = part.indexOf('=');
			try {
				if (eq < 0) {
					map.put(URLDecoder.decode(part, "UTF-8"), "");
				} else {
					map.put(URLDecoder.decode(part.substring(0, eq), "UTF-8"),
							URLDecoder.decode(part.substring(eq + 1), "UTF-8"));
				}
			} catch (Exception ignored) {
			}
		}
		return map;
	}

	private static String errorJson(Throwable e) {
		String msg = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
		return "{\"error\":\"" + msg.replace("\\", "\\\\").replace("\"", "\\\"") + "\"}";
	}

	private static void send(HttpExchange exchange, int code, String body) throws IOException {
		exchange.getRequestBody().close();
		byte[] bytes = body.getBytes(UTF8);
		exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
		exchange.sendResponseHeaders(code, bytes.length);
		OutputStream os = exchange.getResponseBody();
		try {
			os.write(bytes);
		} finally {
			os.close();
		}
	}
}
