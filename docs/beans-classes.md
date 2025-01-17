# Classes in Beans
```mermaid
---
title: Beans
---
classDiagram
    class CloneableBean~T~ {
        +cloneBean() T
    }

    class BlockBean~T~ {
        <<Abstract>>

      -String blockBeanKey
      -String color
	    -boolean dragAllowed
	    -boolean valueReadOnly

        +getBlockBeanKey() String
        +getColor() String
        +isDragAllowed() boolean
        +isValueReadOnly() boolean
        +setValueReadOnly(boolean valueReadOnly)
        +setBlockBeanKey(String blockBeanKey)
        +setColor(String color)
        +setDragAllowed(boolean dragAllowed)
    }
    CloneableBean~T~ <|-- BlockBean~T~

    class BaseBlockBean~T~ {
        <<Abstract>>

        -elementsLayers ArrayList~BlockElementLayerBean~

        +getElementsLayers() ArrayList~BlockElementLayerBean~
        +setElementsLayers(ArrayList~BlockElementLayerBean~)
    }
    BlockBean~T~ <|-- BaseBlockBean~T~

    class EventBlockBean {
        +getValueFromKey(String key)
    }
    BaseBlockBean~T~ <|-- EventBlockBean

    class ExpressionBlockBean {
        -DatatypeBean[] returnDatatypes

        +getReturnDatatypes() DatatypeBean[]
        +setReturnDatatypes(DatatypeBean[] returnDatatypes)
    }
    BaseBlockBean~T~ <|-- ExpressionBlockBean

    class ActionBlockBean~T~ {
        <<Abstract>>

        -ArrayList~LayerBean~ layers

        +getLayers() ArrayList~LayerBean~
        +setLayers(ArrayList~LayerBean~ layers)
    }
    BlockBean~T~ <|-- ActionBlockBean~T~

    class RegularBlockBean {
        -String codeSyntax

        +getCodeSyntax() String
        +setCodeSyntax(String codeSyntax)
    }

    ActionBlockBean~RegularBlockBean~ <|-- RegularBlockBean
    class TerminatorBlockBean {
        -String codeSyntax

        +getCodeSyntax() String
        +setCodeSyntax(String codeSyntax)
    }
    ActionBlockBean~TerminatorBlockBean~ <|-- TerminatorBlockBean

    class LayerBean~T~ {
        <<Abstract>>
    }
    CloneableBean~T~ <|-- LayerBean~T~

    class ActionBlockLayerBean {
        -ArrayList<ActionBlockBean> actionBlockBean

        +setActionBlockBean(ArrayList~ActionBlockBean~ actionBlockBean)
        +getActionBlockBean() ArrayList~ActionBlockBean~
    }
    LayerBean~T~ <|-- ActionBlockLayerBean

    class BlockElementLayerBean {
        -ArrayList<BlockElementBean> blockElementBeans

        +getBlockElementBeans() ArrayList~BlockElementBean~
        +setBlockElementBeans(ArrayList~BlockElementBean~)
    }
    LayerBean~T~ <|-- BlockElementLayerBean

    class BlockPaletteBean {
        -ArrayList~BlockBean~ blocks
	    -String color
	    -String name
	    -String id

        +setBlocks(ArrayList~BlockBean~ blocks)
        +getBlocks() ArrayList~BlockBean~
        +getColor() String
        +setColor(String color)
        +getName() String
        +setName(String name)
        +getId() String
        +setId(String id)
    }

    class BlockElementBean~T~ {
        <<Interface>>
    }
    CloneableBean~T~ <|-- BlockElementBean~T~

    class LabelBlockElementBean {
        -String label

        +setLabel(String label)
        +getLabel() String
    }
    BlockElementBean~T~ <|-- LabelBlockElementBean

    class ArtifactBean {
        -String groupId
        -String artifactId
        -String version
        -String repositoryName
        -String repositoryUrl
        -String extension
        +String getGroupId()
        +void setGroupId(String groupId)
        +String getArtifactId()
        +void setArtifactId(String artifactId)
        +String getVersion()
        +void setVersion(String version)
        +String getRepositoryName()
        +void setRepositoryName(String repositoryName)
        +String getRepositoryUrl()
        +void setRepositoryUrl(String repositoryUrl)
        +String getExtension()
        +void setExtension(String extension)
    }

    class DatatypeBean {
        +static long serialVersionUID
        -String className
        -String classImport
        -boolean isImportNecessary
        +boolean equals(DatatypeBean mDatatypeBean)
        +String getClassName()
        +void setClassName(String className)
        +String getClassImport()
        +void setClassImport(String classImport)
        +boolean isImportNecessary()
        +void setImportNecessary(boolean isImportNecessary)
    }

```