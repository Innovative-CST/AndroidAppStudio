package com.tscodeeditor.android.appstudio.utils;

import com.tscodeeditor.android.appstudio.R;
import com.tscodeeditor.android.appstudio.models.EventHolder;
import com.tscodeeditor.android.appstudio.utils.serialization.DeserializerUtils;
import java.io.File;
import java.util.ArrayList;

public class EventsHolderUtils {
  public static ArrayList<EventHolder> getEventHolder(File eventDir) {
    ArrayList<EventHolder> events = new ArrayList<EventHolder>();

    if (!eventDir.exists()) return events;

    for (File path : eventDir.listFiles()) {
      DeserializerUtils.deserialize(
          new File(path, EnvironmentUtils.EVENTS_DIR),
          new DeserializerUtils.DeserializerListener() {

            @Override
            public void onSuccessfullyDeserialized(Object object) {
              if (object instanceof EventHolder) {
                ((EventHolder) object).setFilePath(path);
                events.add((EventHolder) object);
              }
            }

            @Override
            public void onFailed(int errorCode, Exception e) {}
          });
    }
    return events;
  }

  public static EventHolder getGradleEventHolder() {
    EventHolder eventHolder = new EventHolder();
    eventHolder.setBuiltInEvents(true);
    eventHolder.setHolderName("Config");
    eventHolder.setIcon(R.drawable.ic_gradle);
    return eventHolder;
  }
}
