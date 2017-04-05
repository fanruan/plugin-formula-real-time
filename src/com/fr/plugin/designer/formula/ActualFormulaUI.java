package com.fr.plugin.designer.formula;

import com.fr.design.formula.UIFormula;
import com.fr.design.fun.impl.AbstractUIFormulaProcessor;

/**
 * @author richie
 * @date 2015-06-24
 * @since 8.0
 */
public class ActualFormulaUI extends AbstractUIFormulaProcessor {

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public UIFormula appearanceFormula() {
        return new ActualFormulaPane();
    }

    @Override
    public UIFormula appearanceWhenReserveFormula() {
        return new ActualFormulaPaneWhenReserveFormula();
    }
}