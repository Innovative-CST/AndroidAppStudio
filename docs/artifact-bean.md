# ArtifactBean Documentation

## Overview
The `ArtifactBean` class represents an artifact in a repository within the Android AppStudio project. It is used to encapsulate the metadata associated with an artifact, such as its ID, version, repository details, and file extension. This class implements the `Serializable` interface, allowing its instances to be serialized for storage or transfer.

## Class Details

### ArtifactBean
This class contains several attributes that describe the artifact and provides getter and setter methods for each attribute.

## Attributes

### `groupId`
The Group ID of the artifact, typically in reverse domain format (e.g., `com.example`).

### `artifactId`
The Artifact ID, which is the name of the artifact (e.g., `my-library`).

### `version`
The version of the artifact, following semantic versioning (e.g., `1.0.0`).

### `repositoryName`
The name of the repository where the artifact is hosted.

### `repositoryUrl`
The URL of the repository where the artifact can be accessed.

### `extension`
The file extension of the artifact (e.g., `jar`, `aar`).

## Methods

### Getter and Setter Methods
The class provides the following methods to access and modify its attributes:

- **`getGroupId()`**: Returns the Group ID of the artifact.
- **`setGroupId(String groupId)`**: Sets the Group ID of the artifact.
- **`getArtifactId()`**: Returns the Artifact ID.
- **`setArtifactId(String artifactId)`**: Sets the Artifact ID.
- **`getVersion()`**: Returns the version of the artifact.
- **`setVersion(String version)`**: Sets the version of the artifact.
- **`getRepositoryName()`**: Returns the name of the repository.
- **`setRepositoryName(String repositoryName)`**: Sets the name of the repository.
- **`getRepositoryUrl()`**: Returns the URL of the repository.
- **`setRepositoryUrl(String repositoryUrl)`**: Sets the URL of the repository.
- **`getExtension()`**: Returns the file extension of the artifact.
- **`setExtension(String extension)`**: Sets the file extension of the artifact.

## Example Usage
Here's a quick example of how to use the `ArtifactBean` class:

```java
ArtifactBean artifact = new ArtifactBean();
artifact.setGroupId("com.example");
artifact.setArtifactId("my-library");
artifact.setVersion("1.0.0");
artifact.setRepositoryName("Central Repository");
artifact.setRepositoryUrl("https://repo.maven.apache.org/maven2");
artifact.setExtension("jar");

// Now you can attach it to blocks or events, AndroidAppStudio will process it itself.
```