package com.elfilibustero.uidesigner.beans;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import com.google.errorprone.annotations.Immutable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** Represents an item that can be drag and dropped to the editor */
@Immutable
public class ViewBean {

	@NonNull public static Builder builder() {
		return new Builder();
	}

	private final String className;
	private final String name;
	private final int icon;
	private final Map<String, Object> attributes;

	private ViewBean(
			String className, String name, @DrawableRes int icon, Map<String, Object> attributes) {
		this.className = className;
		this.name = name;
		this.icon = icon;
		this.attributes = attributes;
	}

	/**
	 * @return The class name that will be used to inflate the view
	 */
	public String getClassName() {
		return className;
	}

	public String getName() {
		return name;
	}

	@DrawableRes
	public int getIcon() {
		return icon;
	}

	/**
	 * @return The default values that can be used when this palette is inflated
	 */
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ViewBean))
			return false;
		ViewBean that = (ViewBean) o;
		return icon == that.icon && className.equals(that.className);
	}

	@Override
	public int hashCode() {
		return Objects.hash(className, icon);
	}

	public static class Builder {
		private String className;
		private String name;
		private int icon;
		private final Map<String, Object> attributes = new HashMap<>();

		public Builder setClassName(String name) {
			this.className = name;
			return this;
		}

		public Builder setName(String name) {
			this.name = name;
			return this;
		}

		public Builder setIcon(@DrawableRes int icon) {
			this.icon = icon;
			return this;
		}

		public Builder addDefaultValue(@NonNull String name, @NonNull Object value) {
			this.attributes.put(name, value);
			return this;
		}

		public ViewBean build() {
			return new ViewBean(className, name, icon, attributes);
		}
	}
}
