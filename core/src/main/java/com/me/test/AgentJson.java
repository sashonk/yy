package com.me.test;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

public final class AgentJson {

	private AgentJson() {
	}

	public static void appendQuoted(StringBuilder sb, String s) {
		sb.append('"');
		if (s != null) {
			for (int i = 0; i < s.length(); i++) {
				char c = s.charAt(i);
				switch (c) {
				case '"':
					sb.append("\\\"");
					break;
				case '\\':
					sb.append("\\\\");
					break;
				case '\n':
					sb.append("\\n");
					break;
				case '\r':
					sb.append("\\r");
					break;
				case '\t':
					sb.append("\\t");
					break;
				default:
					sb.append(c);
				}
			}
		}
		sb.append('"');
	}

	public static void appendNamedActors(StringBuilder sb, Actor root) {
		List<Actor> found = new ArrayList<Actor>();
		collectNamed(root, found);
		sb.append('[');
		for (int i = 0; i < found.size(); i++) {
			if (i > 0) {
				sb.append(',');
			}
			Actor a = found.get(i);
			sb.append("{\"name\":");
			appendQuoted(sb, a.getName());
			sb.append(",\"visible\":").append(a.isVisible());
			if (a instanceof Button) {
				sb.append(",\"disabled\":").append(((Button) a).isDisabled());
			}
			sb.append('}');
		}
		sb.append(']');
	}

	private static void collectNamed(Actor a, List<Actor> out) {
		if (a == null) {
			return;
		}
		if (a.getName() != null) {
			out.add(a);
		}
		if (a instanceof Group) {
			for (Actor c : ((Group) a).getChildren()) {
				collectNamed(c, out);
			}
		}
	}

	public static void clickActor(Actor actor) {
		Stage stage = actor.getStage();
		float cx = actor.getWidth() / 2f;
		float cy = actor.getHeight() / 2f;
		Vector2 stageCoords = actor.localToStageCoordinates(new Vector2(cx, cy));

		InputEvent down = new InputEvent();
		down.setType(InputEvent.Type.touchDown);
		down.setStage(stage);
		down.setTarget(actor);
		down.setListenerActor(actor);
		down.setPointer(0);
		down.setButton(0);
		down.setStageX(stageCoords.x);
		down.setStageY(stageCoords.y);
		actor.fire(down);

		InputEvent up = new InputEvent();
		up.setType(InputEvent.Type.touchUp);
		up.setStage(stage);
		up.setTarget(actor);
		up.setListenerActor(actor);
		up.setPointer(0);
		up.setButton(0);
		up.setStageX(stageCoords.x);
		up.setStageY(stageCoords.y);
		actor.fire(up);
	}
}
