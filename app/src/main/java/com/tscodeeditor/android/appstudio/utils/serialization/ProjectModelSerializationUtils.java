package com.tscodeeditor.android.appstudio.utils.serialization;

import com.tscodeeditor.android.appstudio.models.ProjectModel;
import java.io.File;

public class ProjectModelSerializationUtils {
  /*
   * Constants for the error code.
   */
  public static final int FILE_NOT_FOUND = 0;
  public static final int PATH_IS_DIRECTORY = 1;
  public static final int ERROR_OCCURED_WHILE_DESERIALIZING = 2;
  public static final int FILE_IS_NOT_PROJECT_MODEL = 3;

  public static void deserialize(File path, DeserializerListener listener) {
    DeserializerUtils.deserialize(
        path,
        new DeserializerUtils.DeserializerListener() {

          @Override
          public void onSuccessfullyDeserialized(Object object) {
            /*
             * Check that tge object which is deserialozed is a ProjectModel.
             * If it is ProjectModel then pass to the listener with onSuccessfullyDeserialized.
             * If it is not a ProjectModel then call the onFailed with error FILE_IS_NOT_PROJECT_MODEL.
             */
            if (object instanceof ProjectModel) {
              /*
               * Requested file is deserialized successfully without any errors.
               * Now pass the Deserialized object to the listener.
               */
              listener.onSuccessfullyDeserialized((ProjectModel) object);
              return;
            }
            /*
             * Requested file was not a ProjectModel.
             * Pass the error code and Exception to the listener.
             */
            listener.onFailed(
                FILE_IS_NOT_PROJECT_MODEL, new Exception("Provided file was not a ProjectModel"));
          }

          @Override
          public void onFailed(int errorCode, Exception e) {
            listener.onFailed(errorCode, e);
          }
        });
  }

  public interface DeserializerListener {
    void onSuccessfullyDeserialized(ProjectModel object);

    void onFailed(int errorCode, Exception e);
  }
}
