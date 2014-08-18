import os
import unittest
from oeqa.oetest import oeRuntimeTest
from oeqa.utils.decorators import *

#in the future these lists could be moved outside of module
errors = ["error", "cannot", "can\'t", "failed"]

ignore_errors = { 'genericx86-64': ['mmci-pl18x', 'error changing net interface name', 'dma timeout'], \
                'genericx86': ['mmci-pl18x', 'error changing net interface name', 'dma timeout', 'AE_ALREADY_EXISTS'], \
                'emenlow': ['mmci-pl18x', 'error changing net interface name', 'dma timeout', 'AE_ALREADY_EXISTS', '\[drm:psb_do_init\] \*ERROR\* Debug is'], \
                'crownbay': ['mmci-pl18x', 'error changing net interface name', 'dma timeout', 'AE_ALREADY_EXISTS', 'Could not enable PowerButton event', 'probe of LNXPWRBN:00 failed with error -22'], \
                'qemuarm': ['mmci-pl18x', 'error changing net interface name', 'dma timeout', 'mmci-pl18x: probe of fpga:[0-f][0-f] failed with error -38', 'wrong ELF class', 'Fast TSC calibration', 'AE_NOT_FOUND', 'Open ACPI failed', 'Failed to load module "glx"', '\(EE\) error', 'perfctr msr \(MSR c1 is 0\)', 'MMCONFIG information'], \
                'qemux86-64': ['mmci-pl18x', 'error changing net interface name', 'dma timeout', 'wrong ELF class', 'Fast TSC calibration', 'AE_NOT_FOUND', 'Open ACPI failed', 'Failed to load module "glx"', '\(EE\) error', 'perfctr msr \(MSR c1 is 0\)', 'MMCONFIG information'] }

log_locations = ["/var/log/","/var/log/dmesg", "/tmp/dmesg_output.log"]

class ParseLogsTest(oeRuntimeTest):

    @classmethod
    def setUpClass(self):
        self.errors = errors
        self.ignore_errors = ignore_errors
        self.log_locations = log_locations
        self.msg = ""

    def getMachine(self):
        (status, output) = self.target.run("uname -n")
        return output

    #get some information on the CPU of the machine to display at the beginning of the output. This info might be useful in some cases.
    def getHardwareInfo(self):
        hwi = ""
        (status, cpu_name) = self.target.run("cat /proc/cpuinfo | grep \"model name\" | head -n1 | awk 'BEGIN{FS=\":\"}{print $2}'")
        (status, cpu_physical_cores) = self.target.run("cat /proc/cpuinfo | grep \"cpu cores\" | head -n1 | awk {'print $4'}")
        (status, cpu_logical_cores) = self.target.run("cat /proc/cpuinfo | grep \"processor\" | wc -l")
        (status, cpu_arch) = self.target.run("uname -m")
        hwi += "Machine information: \n"
        hwi += "*******************************\n"
        hwi += "Machine name: "+self.getMachine()+"\n"
        hwi += "CPU: "+str(cpu_name)+"\n"
        hwi += "Arch: "+str(cpu_arch)+"\n"
        hwi += "Physical cores: "+str(cpu_physical_cores)+"\n"
        hwi += "Logical cores: "+str(cpu_logical_cores)+"\n"
        hwi += "*******************************\n"
        return hwi

    #go through the log locations provided and if it's a folder create a list with all the .log files in it, if it's a file just add 
    #it to that list
    def getLogList(self, log_locations):
        logs = []
        for location in log_locations:
            (status, output) = self.target.run("test -f "+str(location))
            if (status == 0):
                logs.append(str(location))
            else:
                (status, output) = self.target.run("test -d "+str(location))
                if (status == 0):
                    (status, output) = self.target.run("find "+str(location)+"/*.log -maxdepth 1 -type f")
                    output = output.splitlines()
                    for logfile in output:
                        logs.append(os.path.join(location,str(logfile)))
        return logs

    #build the grep command to be used with filters and exclusions
    def build_grepcmd(self, errors, ignore_errors, log):
        grepcmd = "grep "
        grepcmd +="-Ei \""
        for error in errors:
            grepcmd += error+"|"
        grepcmd = grepcmd[:-1]
        grepcmd += "\" "+str(log)+" | grep -Eiv \'"
        try:
            errorlist = ignore_errors[self.getMachine()]
        except KeyError:
            self.msg += "No ignore list found for this machine, using generic\n"
            errorlist = ignore_errors['genericx86']
        for ignore_error in errorlist:
            grepcmd += ignore_error+"|"
        grepcmd = grepcmd[:-1]
        grepcmd += "\'"
        return grepcmd

    #grep only the errors so that their context could be collected. Default context is 10 lines before and after the error itself
    def parse_logs(self, errors, ignore_errors, logs, lines_before = 10, lines_after = 10):
        results = {}
        rez = []
        for log in logs:
            thegrep = self.build_grepcmd(errors, ignore_errors, log)
            try:
                (status, result) = self.target.run(thegrep)
            except:
                pass
            if result:
                results[log] = {}
                rez = result.splitlines()
                for xrez in rez:
                    command = "grep \"\\"+str(xrez)+"\" -B "+str(lines_before)+" -A "+str(lines_after)+" "+str(log)
                    try:
                        (status, yrez) = self.target.run(command)
                    except:
                        pass
                    results[log][xrez]=yrez
        return results

    #get the output of dmesg and write it in a file. This file is added to log_locations.
    def write_dmesg(self):
        (status, dmesg) = self.target.run("dmesg")
        (status, dmesg2) = self.target.run("echo \""+str(dmesg)+"\" > /tmp/dmesg_output.log")

    @skipUnlessPassed('test_ssh')
    def test_parselogs(self):
        self.write_dmesg()
        log_list = self.getLogList(self.log_locations)
        result = self.parse_logs(self.errors, self.ignore_errors, log_list)
        print self.getHardwareInfo()
        errcount = 0
        for log in result:
            self.msg += "Log: "+log+"\n"
            self.msg += "-----------------------\n"
            for error in result[log]:
                errcount += 1
                self.msg += "Central error: "+str(error)+"\n"
                self.msg +=  "***********************\n"
                self.msg +=  result[str(log)][str(error)]+"\n"
                self.msg +=  "***********************\n"
        self.msg += "%s errors found in logs." % errcount
        self.assertEqual(errcount, 0, msg=self.msg)