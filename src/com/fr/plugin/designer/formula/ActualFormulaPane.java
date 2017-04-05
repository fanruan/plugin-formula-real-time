package com.fr.plugin.designer.formula;

import com.fr.base.io.IOFile;
import com.fr.data.TableDataSource;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.constants.UIConstants;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.formula.FormulaPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.syntax.ui.rsyntaxtextarea.RSyntaxTextArea;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.report.core.namespace.SimpleCellValueNameSpace;
import com.fr.script.Calculator;
import com.fr.stable.UtilEvalError;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author richie
 * @date 2015-06-24
 * @since 8.0
 */
public class ActualFormulaPane extends FormulaPane {

    public ActualFormulaPane() {
        initComponents();
    }

    protected void initComponents() {
        this.setLayout(new BorderLayout(4, 4));

        // text
        JPanel textPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(textPane, BorderLayout.CENTER);

        JPanel checkBoxandbuttonPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();

        UILabel formulaLabel = new UILabel(Inter.getLocText("FormulaD-Input_formula_in_the_text_area_below") + ":"
                + "                         ");
        formulaLabel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        formulaTextArea = new RSyntaxTextArea();
        configFormulaArea();
        formulaTextArea.addKeyListener(this);

        formulaTextArea.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                formulaTextArea.setForeground(Color.black);
                String text = formulaTextArea.getText();
                // 判断在中文输入状态是否还包含提示符 要删掉
                String tip = "\n\n\n" + Inter.getLocText("Tips:You_Can_Input_B1_To_Input_The_Data_Of_The_First_Row_Second_Column");
                if(text.contains(tip)) {
                    text = text.substring(0, text.indexOf(tip));
                    insertPosition = 0;
                    formulaTextArea.setText(text);
                }
            }
        });

        formulaTextArea.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                insertPosition = formulaTextArea.getCaretPosition();
                if (ifHasBeenWriten == 0) {
                    formulaTextArea.setText("");
                    ifHasBeenWriten = 1;
                    formulaTextArea.setForeground(Color.black);
                    insertPosition = 0;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                currentPosition = formulaTextArea.getCaretPosition();
                if (currentPosition == insertPosition) {
                    beginPosition = getBeginPosition();
                    insertPosition = beginPosition;
                    firstStepToFindTips(beginPosition);
                    fixFunctionNameList();
                }
            }
        });
        UIScrollPane formulaTextAreaScrollPane = new UIScrollPane(formulaTextArea);
        formulaTextAreaScrollPane.setBorder(null);
        textPane.add(formulaLabel, BorderLayout.NORTH);
        textPane.add(formulaTextAreaScrollPane, BorderLayout.CENTER);
        textPane.add(checkBoxandbuttonPane, BorderLayout.SOUTH);

        // tipsPane
        JPanel tipsPane = new JPanel(new BorderLayout(4, 4));
        this.add(tipsPane, BorderLayout.EAST);

        JPanel searchPane = new JPanel(new BorderLayout(4, 4));
        searchPane.add(keyWordTextField, BorderLayout.CENTER);
        UIButton searchButton = new UIButton(Inter.getLocText("Search"));
        searchPane.add(searchButton, BorderLayout.EAST);
        tipsPane.add(searchPane, BorderLayout.NORTH);

        keyWordTextField.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String toFind = keyWordTextField.getText();
                    search(toFind, false);

                    fixFunctionNameList();
                    e.consume();
                }
            }
        });

        tipsList = new JList(listModel);
        tipsList.addMouseListener(new DoubleClick());
        UIScrollPane tipsScrollPane = new UIScrollPane(tipsList);
        tipsScrollPane.setPreferredSize(new Dimension(170, 75));
        tipsScrollPane.setBorder(new UIRoundedBorder(UIConstants.LINE_COLOR, 1, UIConstants.ARC));

        tipsPane.add(tipsScrollPane, BorderLayout.CENTER);


        UIButton checkValidButton = new UIButton(Inter.getLocText("FormulaD-Check_Valid"));
        checkValidButton.addActionListener(checkValidActionListener);

        UIButton calculateResultButton = new UIButton(Inter.getLocText("My-Plugin_Formula_Calculate_Now"));
        calculateResultButton.addActionListener(calculateListener);

        JPanel checkBoxPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        checkBoxandbuttonPane.add(checkBoxPane, BorderLayout.WEST);
        checkBoxandbuttonPane.add(GUICoreUtils.createFlowPane(checkValidButton, calculateResultButton), BorderLayout.EAST);

        extendCheckBoxPane(checkBoxPane);

        searchButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String toFind = keyWordTextField.getText();
                search(toFind, false);
                formulaTextArea.requestFocusInWindow();

                fixFunctionNameList();
            }
        });
        variableTreeAndDescriptionArea = new VariableTreeAndDescriptionArea();
        this.add(variableTreeAndDescriptionArea, BorderLayout.SOUTH);
    }

    private ActionListener calculateListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Calculator calculator = Calculator.createCalculator();
            JTemplate<?, ?> template = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
            if (template != null) {
                IOFile file = template.getTarget();
                calculator.setAttribute(TableDataSource.KEY, file);
                calculator.pushNameSpace(SimpleCellValueNameSpace.getInstance());
            }
            Object result;
            try {
                result = calculator.eval(ActualFormulaPane.this.update());
            } catch (UtilEvalError utilEvalError) {
                result = null;
            }
            JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), "公式计算结果为：" + result);

        }
    };

}