/*
 * Copyright (c) 2009-2010, Sergey Karakovskiy and Julian Togelius
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *  Neither the name of the Mario AI nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package ch.idsia.tools;

import ch.idsia.agents.Agent;
import ch.idsia.agents.AgentsPool;
import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.mario.engine.MarioVisualComponent;
import ch.idsia.benchmark.mario.engine.level.LevelGenerator;
import ch.idsia.benchmark.mario.simulation.SimulationOptions;


import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Random;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy
 * Date: Mar 29, 2009
 * Time: 6:27:25 PM
 * Package: .Tools
 */
public class ToolsConfigurator extends JFrame {
    private static final String strPlay = "->  Play! ->";
    private static final String strSimulate = "Simulate! ->";
    public static MarioVisualComponent marioVisualComponent = null;
    private static MarioAIOptions marioAIOptions = null;
    private static JFrame marioComponentFrame = null;
    private static GameViewer gameViewer = null; //new GameViewer(null, null);
    public Checkbox CheckboxShowGameViewer = new Checkbox("Show Game Viewer", true);
    public Label LabelConsole = new Label("Console:");
    public TextArea TextAreaConsole = new TextArea("Console:"/*, 8,40*/);  // Verbose all, keys, events, actions, observations
    public Checkbox CheckboxShowVizualization = new Checkbox("Enable Visualization", GlobalOptions.isVisualization);
    public Checkbox CheckboxMaximizeFPS = new Checkbox("Maximize FPS");
    public Choice ChoiceAgent = new Choice();
    public Choice ChoiceLevelType = new Choice();
    public JSpinner JSpinnerLevelRandomizationSeed = new JSpinner();
    //    public Checkbox CheckboxEnableTimer = new Checkbox("Enable Timer", GlobalOptions.isTimer);
    public JSpinner JSpinnerLevelDifficulty = new JSpinner();
    public Checkbox CheckboxPauseWorld = new Checkbox("Pause World");
    public Checkbox CheckboxPauseMario = new Checkbox("Pause Mario");
    public Checkbox CheckboxPowerRestoration = new Checkbox("Power Restoration");
    public JSpinner JSpinnerLevelLength = new JSpinner();
    public JSpinner JSpinnerMaxAttempts = new JSpinner();
    public Checkbox CheckboxExitOnFinish = new Checkbox("Exit on finish");
    public TextField TextFieldMatLabFileName = new TextField("FileName of output for Matlab");
    public Choice ChoiceVerbose = new Choice();
    //    public Checkbox CheckboxStopSimulationIfWin = new Checkbox("Stop simulation If Win");
    public JButton JButtonPlaySimulate = new JButton(strPlay);
    public JButton JButtonResetEvaluationSummary = new JButton("Reset");
    Dimension defaultSize = new Dimension(330, 100);
    Point defaultLocation = new Point(0, 320);
    private Evaluator evaluator;
    private BasicArrowButton
            upFPS = new BasicArrowButton(BasicArrowButton.NORTH),
            downFPS = new BasicArrowButton(BasicArrowButton.SOUTH);
    private int prevFPS = 24;
    public ToolsConfigurator(Point location, Dimension size) {
        super("Tools Configurator");

        setSize((size == null) ? defaultSize : size);
        setLocation((location == null) ? defaultLocation : location);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Universal Listener
        ToolsConfiguratorActions toolsConfiguratorActions = new ToolsConfiguratorActions();

        //     ToolsConfiguratorOptionsPanel
//        JPanel ToolsConfiguratorOptionsPanel = new JPanel(/*new FlowLayout()*//*GridLayout(0,2)*/);
        Container ToolsConfiguratorOptionsPanel = getContentPane();

        //        CheckboxShowGameViewer
        CheckboxShowGameViewer.addItemListener(toolsConfiguratorActions);

        //              TextFieldConsole
//        TextFieldConsole.addActionListener(toolsConfiguratorActions);

        //          CheckboxShowVizualization
        CheckboxShowVizualization.addItemListener(toolsConfiguratorActions);

        //       CheckboxMaximizeFPS
        CheckboxMaximizeFPS.addItemListener(toolsConfiguratorActions);

        //        ChoiceAgent

        ChoiceAgent.addItemListener(toolsConfiguratorActions);

        Set<String> AgentsNames = AgentsPool.getAgentsNames();
        for (String s : AgentsNames)
            ChoiceAgent.addItem(s);

        //       ChoiceLevelType
        ChoiceLevelType.addItem("Overground");
        ChoiceLevelType.addItem("Underground");
        ChoiceLevelType.addItem("Castle");
        ChoiceLevelType.addItem("Random");
        ChoiceLevelType.addItemListener(toolsConfiguratorActions);

        //      JSpinnerLevelRandomizationSeed
        JSpinnerLevelRandomizationSeed.setToolTipText("Hint: levels with same seed are identical for in observation");
        JSpinnerLevelRandomizationSeed.setValue(1);
        JSpinnerLevelRandomizationSeed.addChangeListener(toolsConfiguratorActions);

        //  CheckboxEnableTimer
//        CheckboxEnableTimer.addItemListener(toolsConfiguratorActions);
        JSpinnerLevelDifficulty.addChangeListener(toolsConfiguratorActions);

        //     CheckboxPauseWorld
        CheckboxPauseWorld.addItemListener(toolsConfiguratorActions);

        //     CheckboxPauseWorld
        CheckboxPauseMario.addItemListener(toolsConfiguratorActions);
        CheckboxPauseMario.setEnabled(false);

        //     CheckboxCheckboxPowerRestoration
        CheckboxPowerRestoration.addItemListener(toolsConfiguratorActions);
        CheckboxPowerRestoration.setEnabled(true);

        //      CheckboxStopSimulationIfWin
//        CheckboxStopSimulationIfWin.addItemListener(toolsConfiguratorActions);

        //      JButtonPlaySimulate
        JButtonPlaySimulate.addActionListener(toolsConfiguratorActions);

        //      JSpinnerLevelLength
        JSpinnerLevelLength.setValue(320);
        JSpinnerLevelLength.addChangeListener(toolsConfiguratorActions);

        //      JSpinnerMaxAttempts
        JSpinnerMaxAttempts.setValue(5);
        JSpinnerMaxAttempts.addChangeListener(toolsConfiguratorActions);

        //      CheckboxExitOnFinish
        CheckboxExitOnFinish.addItemListener(toolsConfiguratorActions);

        //      ChoiceVerbose
        ChoiceVerbose.addItem("Nothing");
        ChoiceVerbose.addItem("All");
        ChoiceVerbose.addItem("Keys pressed");
        ChoiceVerbose.addItem("Selected Actions");


        //      JPanel, ArrowButtons ++FPS, --FPS
        JPanel JPanelFPSFineTune = new JPanel();
        JPanelFPSFineTune.setBorder(new TitledBorder("++FPS/--FPS"));
        JPanelFPSFineTune.setToolTipText("Hint: Use '+' or '=' for ++FPS and '-' for --FPS from your keyboard");
        JPanelFPSFineTune.add(upFPS);
        JPanelFPSFineTune.add(downFPS);
        upFPS.addActionListener(toolsConfiguratorActions);
        downFPS.addActionListener(toolsConfiguratorActions);
        upFPS.setToolTipText("Hint: Use '+' or '=' for ++FPS and '-' for --FPS from your keyboard");
        downFPS.setToolTipText("Hint: Use '+' or '=' for ++FPS and '-' for --FPS from your keyboard");

        //      JPanelLevelOptions
        JPanel JPanelLevelOptions = new JPanel();
        JPanelLevelOptions.setLayout(new BoxLayout(JPanelLevelOptions, BoxLayout.Y_AXIS));
        JPanelLevelOptions.setBorder(new TitledBorder("Level Options"));

        JPanelLevelOptions.add(new Label("Level Type:"));
        JPanelLevelOptions.add(ChoiceLevelType);
        JPanelLevelOptions.add(new Label("Level Randomization Seed:"));
        JPanelLevelOptions.add(JSpinnerLevelRandomizationSeed);

        JPanelLevelOptions.add(new Label("Level Difficulty:"));
        JPanelLevelOptions.add(JSpinnerLevelDifficulty);
        JPanelLevelOptions.add(new Label("Level Length:"));
        JPanelLevelOptions.add(JSpinnerLevelLength);
//        JPanelLevelOptions.add(CheckboxEnableTimer);
        JPanelLevelOptions.add(CheckboxPauseWorld);
        JPanelLevelOptions.add(CheckboxPauseMario);
        JPanelLevelOptions.add(CheckboxPowerRestoration);
        JPanelLevelOptions.add(JButtonPlaySimulate);


        JPanel JPanelMiscellaneousOptions = new JPanel();
        JPanelMiscellaneousOptions.setLayout(new BoxLayout(JPanelMiscellaneousOptions, BoxLayout.Y_AXIS));
        JPanelMiscellaneousOptions.setBorder(new TitledBorder("Miscellaneous Options"));


        JPanelMiscellaneousOptions.add(CheckboxShowGameViewer);

        JPanelMiscellaneousOptions.add(CheckboxShowVizualization);

//        JPanelMiscellaneousOptions.add(TextFieldConsole);
        JPanelMiscellaneousOptions.add(CheckboxMaximizeFPS);
        JPanelMiscellaneousOptions.add(JPanelFPSFineTune);
//        JPanelMiscellaneousOptions.add(JPanelLevelOptions);
        JPanelMiscellaneousOptions.add(new Label("Current Agent:"));
        JPanelMiscellaneousOptions.add(ChoiceAgent);
        JPanelMiscellaneousOptions.add(new Label("Verbose:"));
        JPanelMiscellaneousOptions.add(ChoiceVerbose);
        JPanelMiscellaneousOptions.add(new Label("Evaluation Summary: "));
        JPanelMiscellaneousOptions.add(JButtonResetEvaluationSummary);
        JPanelMiscellaneousOptions.add(new Label("Max # of attemps:"));
        JPanelMiscellaneousOptions.add(JSpinnerMaxAttempts);
//        JPanelMiscellaneousOptions.add(CheckboxStopSimulationIfWin);
        JPanelMiscellaneousOptions.add(CheckboxExitOnFinish);

        JPanel JPanelConsole = new JPanel(new FlowLayout());
        JPanelConsole.setBorder(new TitledBorder("Console"));
        TextAreaConsole.setFont(new Font("Courier New", Font.PLAIN, 12));
        TextAreaConsole.setBackground(Color.BLACK);
        TextAreaConsole.setForeground(Color.GREEN);
        JPanelConsole.add(TextAreaConsole);

        // IF GUI
//        LOGGER.setTextAreaConsole(TextAreaConsole);

        ToolsConfiguratorOptionsPanel.add(BorderLayout.WEST, JPanelLevelOptions);
        ToolsConfiguratorOptionsPanel.add(BorderLayout.CENTER, JPanelMiscellaneousOptions);
        ToolsConfiguratorOptionsPanel.add(BorderLayout.SOUTH, JPanelConsole);

        JPanel borderPanel = new JPanel();
        borderPanel.add(BorderLayout.NORTH, ToolsConfiguratorOptionsPanel);
        setContentPane(borderPanel);
        // autosize:
        this.pack();
    }

    public static void main(String[] args) {
        marioAIOptions = new MarioAIOptions(args);

        ToolsConfigurator toolsConfigurator = new ToolsConfigurator(null, null);
        toolsConfigurator.setVisible(marioAIOptions.isToolsConfigurator());

        toolsConfigurator.ChoiceLevelType.select(marioAIOptions.getLevelType());
        toolsConfigurator.JSpinnerLevelDifficulty.setValue(marioAIOptions.getLevelDifficulty());
        toolsConfigurator.JSpinnerLevelRandomizationSeed.setValue(marioAIOptions.getLevelRandSeed());
        toolsConfigurator.JSpinnerLevelLength.setValue(marioAIOptions.getLevelLength());
        toolsConfigurator.CheckboxShowVizualization.setState(marioAIOptions.isVisualization());
        toolsConfigurator.ChoiceAgent.select(AgentsPool.getCurrentAgent().getName());
        toolsConfigurator.CheckboxMaximizeFPS.setState(marioAIOptions.getFPS() > GlobalOptions.MaxFPS - 1);
        toolsConfigurator.CheckboxPowerRestoration.setState(marioAIOptions.isPowerRestoration());
        toolsConfigurator.CheckboxExitOnFinish.setState(marioAIOptions.isExitProgramWhenFinished());

        gameViewer = new GameViewer(marioAIOptions);

        CreateMarioComponentFrame(marioAIOptions);

        toolsConfigurator.setGameViewer(gameViewer);
        gameViewer.setAlwaysOnTop(false);
        gameViewer.setToolsConfigurator(toolsConfigurator);
        gameViewer.setVisible(marioAIOptions.isGameViewer());

        if (!marioAIOptions.isToolsConfigurator()) {
            toolsConfigurator.simulateOrPlay();
        }
    }


    @Deprecated
    static void CreateMarioComponentFrame(SimulationOptions simulationOptions) {

        if (marioComponentFrame == null) {
            marioComponentFrame = new JFrame( "Mario AI benchmark-tools" + GlobalOptions.getVersionUID());
            marioComponentFrame.pack();
            marioComponentFrame.setResizable(false);

            marioComponentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        marioComponentFrame.setAlwaysOnTop(simulationOptions.isViewAlwaysOnTop());
        marioComponentFrame.setLocation(simulationOptions.getViewLocation());
        marioComponentFrame.setVisible(simulationOptions.isVisualization());
    }

    public void simulateOrPlay() {
        //Simulate or Play!
        SimulationOptions simulationOptions = prepareSimulationOptions();
        assert (simulationOptions != null);
    }

    private SimulationOptions prepareSimulationOptions() {
        SimulationOptions simulationOptions = marioAIOptions;
        Agent agent = AgentsPool.getAgentByName(ChoiceAgent.getSelectedItem());
        simulationOptions.setAgent(agent);
        int type = ChoiceLevelType.getSelectedIndex();
        if (type == 4)
            type = (new Random()).nextInt(4);
        simulationOptions.setLevelType(type);
        simulationOptions.setLevelDifficulty(Integer.parseInt(JSpinnerLevelDifficulty.getValue().toString()));
        simulationOptions.setLevelRandSeed(Integer.parseInt(JSpinnerLevelRandomizationSeed.getValue().toString()));
        simulationOptions.setLevelLength(Integer.parseInt(JSpinnerLevelLength.getValue().toString()));
        simulationOptions.setVisualization(CheckboxShowVizualization.getState());
//        simulationOptions.setEvaluationQuota(Integer.parseInt(JSpinnerMaxAttempts.getValue().toString()));
        simulationOptions.setPowerRestoration(CheckboxPowerRestoration.getState());
        simulationOptions.setExitProgramWhenFinished(CheckboxExitOnFinish.getState());
//        simulationOptions.setMatlabFileName(TextFieldMatLabFileName.getText());

        return simulationOptions;
    }

    public void setGameViewer(GameViewer gameViewer) {
        this.gameViewer = gameViewer;
    }

    public MarioVisualComponent getMarioVisualComponent() {
        return marioVisualComponent;
    }

    public void setConsoleText(String text) {
        LabelConsole.setText("Console got message:");
    }

    enum INTERFACE_TYPE {
        CONSOLE, GUI
    }

    public class ToolsConfiguratorActions implements ActionListener, ItemListener, ChangeListener {
        public void actionPerformed(ActionEvent ae) {
            Object ob = ae.getSource();
            if (ob == JButtonPlaySimulate) {
                simulateOrPlay();
            } else if (ob == upFPS) {
                if (++GlobalOptions.FPS >= GlobalOptions.MaxFPS) {
                    GlobalOptions.FPS = GlobalOptions.MaxFPS;
                    CheckboxMaximizeFPS.setState(true);
                }
//                marioComponent.adjustFPS();
//                LOGGER.println("FPS set to " + (CheckboxMaximizeFPS.getState() ? "infinity" : GlobalOptions.FPS),
//                        LOGGER.VERBOSE_MODE.INFO );
            } else if (ob == downFPS) {
                if (--GlobalOptions.FPS < 1)
                    GlobalOptions.FPS = 1;
                CheckboxMaximizeFPS.setState(false);
//                marioComponent.adjustFPS();
//                LOGGER.println("FPS set to " + (CheckboxMaximizeFPS.getState() ? "infinity" : GlobalOptions.FPS),
//                        LOGGER.VERBOSE_MODE.INFO );
            } else if (ob == JButtonResetEvaluationSummary) {
                evaluator = null;
            }

        }

        public void itemStateChanged(ItemEvent ie) {
            Object ob = ie.getSource();
            if (ob == CheckboxShowGameViewer) {
//                LOGGER.println("Game Viewer " + (CheckboxShowGameViewer.getState() ? "Shown" : "Hidden"),
//                        LOGGER.VERBOSE_MODE.INFO );
                gameViewer.setVisible(CheckboxShowGameViewer.getState());
            } else if (ob == CheckboxShowVizualization) {
//                LOGGER.println("Vizualization " + (CheckboxShowVizualization.getState() ? "On" : "Off"),
//                        LOGGER.VERBOSE_MODE.INFO );
                GlobalOptions.isVisualization = CheckboxShowVizualization.getState();
                marioComponentFrame.setVisible(GlobalOptions.isVisualization);
            } else if (ob == CheckboxMaximizeFPS) {
                prevFPS = (GlobalOptions.FPS == GlobalOptions.MaxFPS) ? prevFPS : GlobalOptions.FPS;
                GlobalOptions.FPS = CheckboxMaximizeFPS.getState() ? 100 : prevFPS;
//                marioComponent.adjustFPS();
//                LOGGER.println("FPS set to " + (CheckboxMaximizeFPS.getState() ? "infinity" : GlobalOptions.FPS),
//                        LOGGER.VERBOSE_MODE.INFO );
/*            } else if (ob == CheckboxEnableTimer)
            {
                GlobalOptions.isTimer = CheckboxEnableTimer.getState();
//                LOGGER.println("Timer " + (GlobalOptions.isTimer ? "enabled" : "disabled"),
//                        LOGGER.VERBOSE_MODE.INFO);
            */
            } else if (ob == CheckboxPauseWorld) {
//            GlobalOptions.isPauseWorld = CheckboxPauseWorld.getState();

//                marioComponent.setPaused(GlobalOptions.isPauseWorld);
//                LOGGER.println("World " + (GlobalOptions.isPauseWorld ? "paused" : "unpaused"),
//                        LOGGER.VERBOSE_MODE.INFO);
            } else if (ob == CheckboxPauseMario) {
                TextAreaConsole.setText("1\n2\n3\n");
            } else if (ob == CheckboxPowerRestoration) {
                GlobalOptions.isPowerRestoration = CheckboxPowerRestoration.getState();
//                LOGGER.println("Mario Power Restoration Turned " + (GlobalOptions.isPowerRestoration ? "on" : "off"),
//                        LOGGER.VERBOSE_MODE.INFO);
            }
//            else if (ob == CheckboxStopSimulationIfWin)
//            {
//                GlobalOptions.StopSimulationIfWin = CheckboxStopSimulationIfWin.getState();
////                LOGGER.println("Stop simulation if Win Criteria Turned " +
////                        (GlobalOptions.StopSimulationIfWin ? "on" : "off"),
////                        LOGGER.VERBOSE_MODE.INFO);
//            }
            else if (ob == ChoiceAgent) {
//                LOGGER.println("Agent chosen: " + (ChoiceAgent.getSelectedItem()), LOGGER.VERBOSE_MODE.INFO);
                JButtonPlaySimulate.setText(strSimulate);
            } else if (ob == ChoiceLevelType) {

            } else if (ob == ChoiceVerbose) {

            }
        }

        public void stateChanged(ChangeEvent changeEvent) {
            Object ob = changeEvent.getSource();
            if (ob == JSpinnerLevelRandomizationSeed) {
                //Change random seed in Evaluator/ Simulator Options
            } else if (ob == JSpinnerLevelDifficulty) {

            } else if (ob == JSpinnerLevelLength) {
                if (Integer.parseInt(JSpinnerLevelLength.getValue().toString()) < LevelGenerator.LevelLengthMinThreshold)
                    JSpinnerLevelLength.setValue(LevelGenerator.LevelLengthMinThreshold);
            }
        }
    }
}
