# What are Beans?

**Bean** is a type of class that implements `java.io.Serializable` interface.
**Serializable** in Java is the process of converting an object into a byte stream, which can then be stored in a file, sent over a network, or saved in a database. This allows the object to be reconstructed (deserialized) later whenever required.

Serialization is useful for:  
- Saving object states persistently.  
- Sending objects between different Java processes or over a network.  
- Storing objects efficiently in binary format.  

## Project File System  

This project does not save the user-created projects on a server or database. Instead, it uses device storage to store them locally.  

### Why Not Use JSON or XML?  
To save project data in device storage, formats like **JSON**, **XML**, or **TXT** can be used. However, these formats have drawbacks:  
1. **Complexity** – Converting object data into JSON or XML requires mapping, and retrieving the data involves parsing these formats.  
2. **Performance** – Storing and reading JSON/XML files requires extra processing time. Large files make saving and loading slow.  
3. **Memory Consumption** – JSON/XML parsing consumes more memory as they require intermediate data structures (e.g., `JSONObject`, `Document`).  

### Why Use Beans for Data Storage?  
Instead of using text-based formats like JSON or XML, this project uses **Beans**, which rely on Java’s native serialization mechanism:  
- **Faster Storage and Retrieval** – Since serialization directly converts an object into a byte stream, reading and writing are much faster.  
- **Compact Storage** – Binary serialization is more space-efficient than text formats.  
- **Ease of Implementation** – No need for additional parsing logic; Java handles serialization and deserialization automatically.  

## Basic Principles of Serialization

1. **Serializable Fields**  
   - An object cannot be serialized completely unless all the fields used are also serializable.  
   - If a field is not serializable, you must either make it transient or provide custom serialization logic.  

2. **Serializable Interface**  
   - A class must implement the `Serializable` interface to enable serialization.  
   - This is a marker interface and does not contain any methods.  

3. **Transient Fields**  
   - Fields marked with the `transient` keyword are not serialized.  
   - Use `transient` for sensitive data (e.g., passwords) or fields that should not be stored.  

4. **SerialVersionUID**  
   - It is recommended to define a `serialVersionUID` to ensure version compatibility.  
   - If not defined, Java generates one automatically, which may change if the class structure is modified.  

   ```java
   private static final long serialVersionUID = 1L;
   ```
