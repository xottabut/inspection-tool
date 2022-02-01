package com.intellias.tooling;

import com.sun.tools.attach.VirtualMachine;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import sun.jvmstat.monitor.MonitorException;
import sun.jvmstat.monitor.MonitoredHost;
import sun.jvmstat.monitor.MonitoredVm;
import sun.jvmstat.monitor.MonitoredVmUtil;
import sun.jvmstat.monitor.VmIdentifier;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Log4j2
public class InspectionApplication {

    private static final Scanner INPUT = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                log.error("Path to the agent not provided.");
                return;
            }
            String agentPath = args[0];
            if (Files.notExists(Paths.get(agentPath))) {
                log.error("There is no file by provided path.");
                return;
            }
            attachAgentToVirtualMachine(agentPath);
        } catch (Exception e) {
            log.error("Something went wrong. Closing the application.", e);
        }
    }

    private static void attachAgentToVirtualMachine(String agentPath) throws Exception {
        List<MonitoredVm> monitoredVms = getLocalMonitoredVMs();
        if (!monitoredVms.isEmpty()) {
            int vmPid = selectVirtualMachineToAttach(monitoredVms);
            VirtualMachine virtualMachine = VirtualMachine.attach(String.valueOf(vmPid));
            try {
                log.info("Attached successfully to {}", vmPid);
                System.out.print("Class to transform>");
                String className = INPUT.next();
                System.out.print("Method to transform>");
                String methodName = INPUT.next();
                virtualMachine.loadAgent(agentPath, className + ":" + methodName);
            } finally {
                virtualMachine.detach();
                log.info("Detached successfully from {}", vmPid);
            }
        } else {
            System.out.println("No VMs found.");
        }
    }

    private static int selectVirtualMachineToAttach(List<MonitoredVm> monitoredVms) throws MonitorException {
        System.out.println("Select VM to attach:");
        for (int i = 0; i < monitoredVms.size(); i++) {
            MonitoredVm monitoredVm = monitoredVms.get(i);
            System.out.printf("%d: [%d] %s%n", i, monitoredVm.getVmIdentifier().getLocalVmId(), MonitoredVmUtil.mainClass(monitoredVm, true));
        }
        System.out.print("Process index>");
        int selectedProcessIdx = INPUT.nextInt();
        while (selectedProcessIdx < 0 || selectedProcessIdx >= monitoredVms.size()) {
            System.out.println("Wrong selection. Please enter between 0 and " + (monitoredVms.size() - 1));
            System.out.print("Process index>");
            selectedProcessIdx = INPUT.nextInt();
        }
        MonitoredVm selectedVm = monitoredVms.get(selectedProcessIdx);
        return selectedVm.getVmIdentifier().getLocalVmId();
    }

    private static List<MonitoredVm> getLocalMonitoredVMs() throws MonitorException, URISyntaxException {
        MonitoredHost local = MonitoredHost.getMonitoredHost("localhost");
        return local.activeVms().stream().map(vmId -> String.format("//%d", vmId)).map(InspectionApplication::newVmIdentifier).map(vmIdentifier -> {
            try {
                return local.getMonitoredVm(vmIdentifier);
            } catch (MonitorException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    @SneakyThrows
    private static VmIdentifier newVmIdentifier(String id) {
        return new VmIdentifier(id);
    }

}
