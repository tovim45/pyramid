package com.pyramid.utils.sshUtils;
import static com.pyramid.infra.logger.AutomationLogger.info;
import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * This class contains convenience methods for working with JSch SSH Connection
 *
 * @Giora Tovim
 */

public class SshCli {


    public String execSshCli(String host, String user, String password , String command, Boolean printCliResult ) throws Exception {

        try {

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, 22);
            session.setPassword(password);
            session.setConfig(config);
            session.connect();
            info("Connected");
            System.out.println("Connected");

            info("print Cli Result is: " + printCliResult);
            System.out.println("Print Cli Result is: " + printCliResult);

            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);

            InputStream in = channel.getInputStream();
            channel.connect();
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    else {
                        String cliResult = new String(tmp, 0, i);
                        if (printCliResult) {
                            info("Exec CLI command:");
                            info(command);
                            info("CLI Results is:");
                            info(cliResult);
                        }

                        info("Done - Disconnect");
                        channel.disconnect();
                        session.disconnect();
                        return cliResult;
                    }
                }
                if (channel.isClosed()) {
                    System.out.println("exit-status: " + channel.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                }
            }
            System.out.println("Done - disconnect");
            channel.disconnect();
            session.disconnect();
            System.out.println("DONE");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
