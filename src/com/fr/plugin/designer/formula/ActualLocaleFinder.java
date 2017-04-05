package com.fr.plugin.designer.formula;

import com.fr.stable.fun.impl.AbstractLocaleFinder;

/**
 * @author richie
 * @date 2015-07-09
 * @since 8.0
 */
public class ActualLocaleFinder extends AbstractLocaleFinder {

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String find() {
        return "com/fr/plugin/designer/formula/locale/formula";
    }
}