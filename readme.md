## 自定义公式编辑器

### 实现相关概述

公式编辑器面板需要实现接口

```java
com.fr.design.formula.UIFormula
```

注册新的公式编辑器需要实现接口

```java
com.fr.design.fun.UIFormulaProcessor
```

plugin.xml注册节点

```xml
 <extra-designer>
      <UIFormulaProcessor class="com.fr.plugin.designer.formula.ActualFormulaUI"/>
 </extra-designer>
```

### 效果图(对于部分公式可以直接查看计算结果)
![t](screenshots/result.png)

### 接口内容

com.fr.design.formula.UIFormula

```java
package com.fr.design.formula;

import com.fr.base.Formula;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionListener;

import java.awt.*;

/**
 * @author richie
 * @date 2015-06-24
 * @since 8.0
 */
public interface UIFormula {

    /**
     * 从已有的公式还原公式面板
     * @param formula 公式
     */
    void populate(Formula formula);

    /**
     * 根据指定的变量处理和公式还原公式面板
     * @param formula 公式
     * @param variableResolver 变量处理器
     */
    void populate(Formula formula, VariableResolver variableResolver);

    /**
     * 获取公式面板的参数
     * @return 公式
     */
    Formula update();

    /**
     * 显示窗口
     * @param window 窗口
     * @param l 对话框监听器
     * @return 对话框
     */
    BasicDialog showLargeWindow(Window window, DialogActionListener l);
}
```
_____
com.fr.design.fun.UIFormulaProcessor

```java
package com.fr.design.fun;

import com.fr.design.formula.UIFormula;

/**
 * @author richie
 * @date 2015-04-17
 * @since 8.0
 * 公式编辑器界面处理接口
 */
public interface UIFormulaProcessor {
    String MARK_STRING = "UIFormulaProcessor";

    /**
     * 普通的公式编辑器界面类
     * @return 公式编辑器界面类
     */
    UIFormula appearanceFormula();

    /**
     * 当需要显示“保留公式”项时的公式编辑器界面类
     * @return 公式编辑器界面类
     */
    UIFormula appearanceWhenReserveFormula();
}
```