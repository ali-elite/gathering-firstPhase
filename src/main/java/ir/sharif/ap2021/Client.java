package ir.sharif.ap2021;

import ir.sharif.ap2021.Controller.LogicalAgent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Client {
    private static final Logger logger = LogManager.getLogger(Client.class);
    public static void main(String[] args) {

        logger.info("App Started");

        LogicalAgent logicalAgent = new LogicalAgent();
        logicalAgent.run();
    }
}
