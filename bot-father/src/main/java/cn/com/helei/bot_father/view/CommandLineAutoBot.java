package cn.com.helei.bot_father.view;

import cn.com.helei.bot_father.bot.AccountManageAutoBot;
import cn.com.helei.bot_father.bot.JobInvokeAutoBot;
import cn.com.helei.bot_father.constants.BotStatus;
import cn.com.helei.bot_father.view.commandMenu.CommandMenuNode;
import cn.com.helei.bot_father.config.AutoBotConfig;
import cn.com.helei.common.exception.BotStartException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.CountDownLatch;


/**
 * 命令行交互的depin机器人
 */
@Slf4j
@Getter
public abstract class CommandLineAutoBot {

    private final AutoBotConfig botConfig;

    private final JobInvokeAutoBot bot;

    private final CommandMenuNode mainManu;

    public CommandLineAutoBot(JobInvokeAutoBot bot) {
        this.bot = bot;
        this.botConfig = bot.getAutoBotConfig();

        this.mainManu = new CommandMenuNode(
                "主菜单",
                String.format("欢迎使用[%s]-bot", getBot().getBotInfo().getName()),
                this::printBanner
        );
    }

    /**
     * 构建command菜单
     */
    protected abstract void buildMenuNode(CommandMenuNode mainManu);


    /**
     * 异步启动
     */
    private void asyncExecute(CountDownLatch startLatch) {
        Thread commandInputThread = new Thread(() -> {
            try {
                doExecute();
            } catch (Exception e) {
                log.error("启动bot发生错误", e);
            } finally {
                startLatch.countDown();
            }
        }, "depin-bot-main");
        commandInputThread.setDaemon(true);
        commandInputThread.start();
    }

    /**
     * 启动bot
     *
     * @throws BotStartException DepinBotStartException
     */
    public void start() throws BotStartException {
        bot.updateState(BotStatus.STARTING);
        log.info("{} 正在启动", bot.runtimeBotName());
        try {

            CountDownLatch startLatch = new CountDownLatch(1);
            //启动命令行交互的线程
            asyncExecute(startLatch);

            log.info("{} 启动完毕", bot.runtimeBotName());

            bot.updateState(BotStatus.RUNNING);
            startLatch.await();
        } catch (Exception e) {
            bot.updateState(BotStatus.SHUTDOWN);
            throw new BotStartException("启动 %s 发生错误".formatted(bot.runtimeBotName()), e);
        }
    }


    /**
     * 运行机器人
     *
     * @throws IOException IOException
     */
    public void doExecute() throws IOException {
        //Step 1 获取输入
        CommandMenuNode mainMenuNode = getMenuNode();
        Terminal terminal = TerminalBuilder.builder().system(true).build();
        LineReader reader = LineReaderBuilder.builder().terminal(terminal).parser(new DefaultParser()).build();

        Stack<CommandMenuNode> menuNodeStack = new Stack<>();
        CommandMenuNode currentMenuNode = mainMenuNode;

        //Step 2 不断监听控制台输入
        while (true) {
            boolean inputAccept = true;
            //Step 2.1 获取输入
            String choice;
            try {
                choice = reader.readLine("\n<\n" + getInvokeActionAndMenuNodePrintStr(currentMenuNode) + "请选择>").trim();
            } catch (Exception e) {
                log.error("进入菜单节点[{}]发生异常", currentMenuNode.getTittle(), e);
                currentMenuNode = menuNodeStack.pop();
                continue;
            }

            try {
                //Step 2.2 退出
                if ("exit".equals(choice)) {
                    exitHandler();
                    break;
                }

                //Step 2.3 选择操作
                int option = Integer.parseInt(choice.trim());
                if (option == 0) {
                    //返回上一级菜单
                    if (!menuNodeStack.isEmpty()) {
                        currentMenuNode = menuNodeStack.pop();
                    }
                } else if (option > 0 && option <= currentMenuNode.getSubNodeList().size()) {
                    //进入选择的菜单
                    menuNodeStack.push(currentMenuNode);
                    currentMenuNode = currentMenuNode.getSubNodeList().get(option - 1);
                } else {
                    inputAccept = false;
                }

                //终点节点，不进入，直接返回
                if (currentMenuNode.isEnd()) {
                    System.out.println(getInvokeActionAndMenuNodePrintStr(currentMenuNode));
                    currentMenuNode = menuNodeStack.pop();
                }
            } catch (Exception e) {
                inputAccept = false;
            }

            try {
                if (!inputAccept && currentMenuNode.getResolveInput() != null) {
                    currentMenuNode.getResolveInput().accept(choice);
                }
            } catch (Exception e) {
                System.out.println("系统异常");
            }
        }
    }


    /**
     * 获取菜单， 会放入额外的固定菜单
     *
     * @return CommandMenuNode
     */
    private CommandMenuNode getMenuNode() {

        buildMenuNode(mainManu);

        return mainManu;
    }

    private String printBanner() {

        return "" + bot.printBotRuntimeInfo();
    }

    /**
     * 退出回调
     */
    protected void exitHandler() {
    }

    /**
     * 执行Action回调，获取当前菜单打印的字符串
     *
     * @param currentMenuNode currentMenuNode
     * @return String
     */
    public String getInvokeActionAndMenuNodePrintStr(CommandMenuNode currentMenuNode) {
        StringBuilder sb = new StringBuilder();
        sb.append(currentMenuNode.getDescribe()).append("\n");

        if (currentMenuNode.getAction() != null) {
            sb.append(currentMenuNode.getAction().get()).append("\n");
        }

        if (currentMenuNode.isEnd()) return sb.toString();

        sb.append("选项:\n");
        List<CommandMenuNode> menuNodeList = currentMenuNode.getSubNodeList();
        for (int i = 0; i < menuNodeList.size(); i++) {
            sb.append(i + 1).append(". ").append(menuNodeList.get(i).getTittle()).append("\n");
        }

        sb.append("0. 返回上一级菜单\n");

        return sb.toString();
    }

}
