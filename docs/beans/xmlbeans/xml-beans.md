# XML Bean Serialization and Code Generation  

## Overview  

The `XmlBean` class is designed to represent an XML structure in Java, making it easier to generate, manipulate, and serialize XML data. It supports attributes, child elements, and XML namespace definitions, making it ideal for use in Android development where XML layouts and configurations are common.  

The `XmlAttributeBean` class represents an individual XML attribute, storing the attribute name and value.  

Both classes support Java serialization (`Serializable` interface) and hence `Bean` in suffix in their class name, allowing them to be saved and loaded efficiently as byte streams rather than text-based formats like JSON or XML.  

---

## `XmlAttributeBean`  

The `XmlAttributeBean` class represents an attribute of an XML element. Each attribute consists of:  
- `attribute`: The name of the attribute (e.g., `android:layout_width`).  
- `attributeValue`: The value of the attribute (e.g., `"match_parent"`).  

### Class Definition  

```java
public class XmlAttributeBean {
    public static final long serialVersionUID = 1L;

    private String attribute;
    private Object attributeValue;

    public String getAttribute() {
        return this.attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public Object getAttributeValue() {
        return this.attributeValue;
    }

    public void setAttributeValue(Object attributeValue) {
        this.attributeValue = attributeValue;
    }
}
```

### Key Features  
- Implements a simple key-value structure for XML attributes.  
- `Serializable` support enables storage as a byte stream.  
- `attributeValue` is of type `Object`, allowing it to store different data types.  

---

## `XmlBean`  

The `XmlBean` class represents an XML element. It contains:  
- **Attributes:** A list of `XmlAttributeBean` objects representing key-value pairs.  
- **Children:** A list of `XmlBean` objects representing nested XML elements.  
- **Root Element Flag:** Determines whether the element is the root of the XML document.  
- **Namespace Flags:** Boolean values indicating whether the Android, App, or Tools namespace should be added.  

### Class Definition  

```java
import java.io.Serializable;
import java.util.ArrayList;

public class XmlBean implements Serializable {
    public static final long serialVersionUID = 1L;

    private boolean addAndroidNameSpace;
    private boolean addAppNameSpace;
    private boolean addToolsNameSpace;
    private boolean isRootElement;
    private ArrayList<XmlAttributeBean> attributes;
    private ArrayList<XmlBean> children;
    private String name;
    private String id;

    public String getCode() {
        // Returns the XML code
    }

    public boolean getAddAndroidNameSpace() {
        return this.addAndroidNameSpace;
    }

    public void setAddAndroidNameSpace(boolean addAndroidNameSpace) {
        this.addAndroidNameSpace = addAndroidNameSpace;
    }

    public boolean getAddAppNameSpace() {
        return this.addAppNameSpace;
    }

    public void setAddAppNameSpace(boolean addAppNameSpace) {
        this.addAppNameSpace = addAppNameSpace;
    }

    public boolean getAddToolsNameSpace() {
        return this.addToolsNameSpace;
    }

    public void setAddToolsNameSpace(boolean addToolsNameSpace) {
        this.addToolsNameSpace = addToolsNameSpace;
    }

    public boolean isRootElement() {
        return this.isRootElement;
    }

    public void setRootElement(boolean isRootElement) {
        this.isRootElement = isRootElement;
    }

    public ArrayList<XmlAttributeBean> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(ArrayList<XmlAttributeBean> attributes) {
        this.attributes = attributes;
    }

    public ArrayList<XmlBean> getChildren() {
        return this.children;
    }

    public void setChildren(ArrayList<XmlBean> children) {
        this.children = children;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

```

### Key Features  
- **XML Generation:** The `getCode(String whitespace)` method generates an XML string from the object structure.  
- **Namespace Support:** Automatically adds Android, App, and Tools namespaces if set.  
- **Hierarchical Structure:** Supports nested elements using a list of `XmlBean` children.  
- **Serialization:** Implements `Serializable` for efficient storage and retrieval.  

---

## Example Usage  

### Creating an XML Structure  

```java
XmlBean root = new XmlBean();
root.setName("LinearLayout");
root.setRootElement(true);
root.setAddAndroidNameSpace(true);

XmlAttributeBean widthAttr = new XmlAttributeBean();
widthAttr.setAttribute("android:layout_width");
widthAttr.setAttributeValue("match_parent");

XmlAttributeBean heightAttr = new XmlAttributeBean();
heightAttr.setAttribute("android:layout_height");
heightAttr.setAttributeValue("wrap_content");

ArrayList<XmlAttributeBean> attributes = new ArrayList<>();
attributes.add(widthAttr);
attributes.add(heightAttr);

root.setAttributes(attributes);

XmlBean child = new XmlBean();
child.setName("TextView");

XmlAttributeBean textAttr = new XmlAttributeBean();
textAttr.setAttribute("android:text");
textAttr.setAttributeValue("Hello World");

ArrayList<XmlAttributeBean> childAttributes = new ArrayList<>();
childAttributes.add(textAttr);
child.setAttributes(childAttributes);

ArrayList<XmlBean> children = new ArrayList<>();
children.add(child);
root.setChildren(children);

System.out.println(root.getCode(""));
```

### Output  

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout 
	xmlns:android="http://schemas.android.com/apk/res/android"
	android:layout_width="match_parent"
	android:layout_height="wrap_content">
	<TextView android:text="Hello World"/>
</LinearLayout>
```

---

## Serialization and Deserialization  

### Saving `XmlBean` to File  

```java
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("xmlbean.dat"))) {
    oos.writeObject(root);
    System.out.println("XmlBean serialized successfully!");
} catch (Exception e) {
    e.printStackTrace();
}
```

### Loading `XmlBean` from File  

```java
import java.io.FileInputStream;
import java.io.ObjectInputStream;

try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("xmlbean.dat"))) {
    XmlBean loadedRoot = (XmlBean) ois.readObject();
    System.out.println("XmlBean deserialized successfully!");
    System.out.println(loadedRoot.getCode(""));
} catch (Exception e) {
    e.printStackTrace();
}
```

---

## Summary  

- **`XmlBean`** represents an XML element and supports attributes, child elements, and namespaces.  
- **`XmlAttributeBean`** stores attribute key-value pairs for XML elements.  
- **Supports serialization** to store and retrieve objects efficiently.  
- **Generates XML** dynamically based on the object hierarchy.  

This approach makes it easier to **manage XML layouts programmatically**, avoiding manual string concatenation while ensuring structured storage.  
 modifications!