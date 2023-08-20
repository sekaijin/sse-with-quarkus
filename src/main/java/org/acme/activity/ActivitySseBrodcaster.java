package org.acme.activity;

import org.acme.sse.SseRessource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.sse.Sse;

@Path(Activity.NAME)
@ApplicationScoped
public class ActivitySseBrodcaster extends SseRessource<Activity> {

	public ActivitySseBrodcaster(@Context Sse sse) {
		super(sse, 5);
	}
}
