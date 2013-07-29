# Copyright (C) 2013 Intel Corporation
#
# Released under the MIT license (see COPYING.MIT)

# This module provides a class for starting qemu images using runqemu.
# It's used by testimage.bbclass.

import subprocess
import optparse
import sys
import os
import time
import signal
import re
import bb
from oeqa.utils.oeqemuconsole import oeQemuConsole

class QemuRunner:

    def __init__(self, machine, rootfs, display = None, tmpdir = None, logfile = None, boottime = 400):
        # Popen object
        self.runqemu = None

        self.machine = machine
        self.rootfs = rootfs

        self.streampath = '/tmp/qemuconnection.%s' % os.getpid()
        self.qemuparams = 'bootparams="console=tty1 console=ttyS0,115200n8" qemuparams="-serial unix:%s,server,nowait"' % self.streampath
        self.qemupid = None
        self.ip = None

        self.display = display
        self.tmpdir = tmpdir
        self.logfile = logfile
        self.boottime = boottime

    def launch(self, qemuparams = None):

        if qemuparams:
            self.qemuparams = self.qemuparams[:-1] + " " + qemuparams + " " + '\"'

        if self.display:
            os.environ["DISPLAY"] = self.display
        else:
            bb.error("To start qemu I need a X desktop, please set DISPLAY correctly (e.g. DISPLAY=:1)")
            return False
        if not os.path.exists(self.rootfs):
            bb.error("Invalid rootfs %s" % self.rootfs)
            return False
        if not os.path.exists(self.tmpdir):
            bb.error("Invalid TMPDIR path %s" % self.tmpdir)
            return False
        else:
            os.environ["OE_TMPDIR"] = self.tmpdir

        launch_cmd = 'runqemu %s %s %s' % (self.machine, self.rootfs, self.qemuparams)
        self.runqemu = subprocess.Popen(launch_cmd,shell=True,stdout=subprocess.PIPE,stderr=subprocess.STDOUT,preexec_fn=os.setpgrp)

        bb.note("runqemu started, pid is %s" % self.runqemu.pid)
        bb.note("waiting at most 60 seconds for qemu pid")
        endtime = time.time() + 60
        while not self.is_alive() and time.time() < endtime:
            time.sleep(1)

        if self.is_alive():
            bb.note("qemu started - qemu procces pid is %s" % self.qemupid)

            console = oeQemuConsole(self.streampath, self.logfile)
            bb.note("Waiting at most %d seconds for login banner" % self.boottime )
            (match, text) = console.read_all_timeout("login:", self.boottime)

            if match:
                bb.note("Reached login banner")
                console.write("root\n")
                (index, match, text) = console.expect([r"(root@[\w-]+:~#)"],10)
                if not match:
                    bb.note("Couldn't get prompt, all I got was:\n%s" % match.group(0))
                    return False
                console.write("ip addr show `ip route list | sed -n '1p' | awk '{print $5}'` | sed -n '3p' | awk '{ print $2 }' | cut -f 1 -d \"/\"\n")
                (index, match, text) = console.expect([r"((?:[0-9]{1,3}\.){3}[0-9]{1,3})"],10)
                console.close()
                if match:
                    self.ip = match.group(0)
                    bb.note("Ip found: %s" % self.ip)
                else:
                    bb.note("Couldn't determine ip, all I got was:\n%s" % text)
                    return False
            else:
                console.close()
                bb.note("Target didn't reached login boot in %d seconds" % self.boottime)
                lines = "\n".join(text.splitlines()[-5:])
                bb.note("Last 5 lines of text:\n%s" % lines)
                bb.note("Check full boot log: %s" % self.logfile)
                return False
        else:
            bb.note("Qemu pid didn't appeared in 30 seconds")
            self.runqemu.terminate()
            self.runqemu.kill()
            bb.note("Output from runqemu: %s " % self.runqemu.stdout.read())
            self.runqemu.stdout.close()
            return False

        return self.is_alive()


    def kill(self):
        if self.runqemu:
            os.kill(-self.runqemu.pid,signal.SIGTERM)
        self.qemupid = None
        self.ip = None
        if os.path.exists(self.streampath):
            os.remove(self.streampath)

    def restart(self, qemuparams = None):
        if self.is_alive():
            self.kill()
        bb.note("Qemu Restart required...")
        return self.launch(qemuparams)

    def is_alive(self):
        qemu_child = self.find_child(str(self.runqemu.pid))
        if qemu_child:
            self.qemupid = qemu_child[0]
            if os.path.exists("/proc/" + str(self.qemupid)) and os.path.exists(self.streampath):
                return True
        return False

    def find_child(self,parent_pid):
        #
        # Walk the process tree from the process specified looking for a qemu-system. Return its [pid'cmd]
        #
        ps = subprocess.Popen(['ps', 'axww', '-o', 'pid,ppid,command'], stdout=subprocess.PIPE).communicate()[0]
        processes = ps.split('\n')
        nfields = len(processes[0].split()) - 1
        pids = {}
        commands = {}
        for row in processes[1:]:
            data = row.split(None, nfields)
            if len(data) != 3:
                continue
            if data[1] not in pids:
                pids[data[1]] = []

            pids[data[1]].append(data[0])
            commands[data[0]] = data[2]

        if parent_pid not in pids:
            sys.stderr.write("No children found matching %s\n" % parent_pid)
            return []

        parents = []
        newparents = pids[parent_pid]
        while newparents:
            next = []
            for p in newparents:
                if p in pids:
                    for n in pids[p]:
                        if n not in parents and n not in next:
                            next.append(n)
                if p not in parents:
                    parents.append(p)
                    newparents = next
        #print "Children matching %s:" % str(parents)
        for p in parents:
            # Need to be careful here since runqemu-internal runs "ldd qemu-system-xxxx"
            # Also, old versions of ldd (2.11) run "LD_XXXX qemu-system-xxxx"
            basecmd = commands[p].split()[0]
            basecmd = os.path.basename(basecmd)
            if "qemu-system" in basecmd and "-serial unix" in commands[p]:
                return [int(p),commands[p]]

