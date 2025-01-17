# Classes in Beans
```mermaid
classDiagram
    note "Beans store data not represent visually"
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
    Serializable <|-- BlockBean~T~

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
    Serializable <|-- LayerBean~T~

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
        -ArrayList~BlockBean~ blocks;
	    -String color;
	    -String name;
	    -String id;

        +setBlocks(ArrayList~BlockBean~ blocks)
        +getBlocks() ArrayList~BlockBean~
        +getColor() String
        +setColor(String color)
        +getName() String
        +setName(String name)
        +getId() String
        +setId(String id)
    }
    Serializable <|-- BlockPaletteBean

    class BlockElementBean~T~ {
        <<Interface>>
    }
    CloneableBean~T~ <|-- BlockElementBean~T~

    class LabelBlockElementBean {
        -String label

        +setLabel(String label)
        +getLabel() String
    }

```