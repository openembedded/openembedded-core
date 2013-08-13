# Copyright (C) 2013 Intel Corporation
#
# Released under the MIT license (see COPYING.MIT)

# This module provides a class for starting qemu images using runqemu.
# It's used by testimage.bbclass.

import subprocess
import os
import time
import signal
import re
import socket
import select
import bb

class QemuRunner:

    def __init__(self, machine, rootfs, display = None, tmpdir = None, logfile = None, boottime = 400, runqemutime = 60):
        # Popen object
        self.runqemu = None

        self.machine = machine
        self.rootfs = rootfs

        self.qemupid = None
        self.ip = None

        self.display = display
        self.tmpdir = tmpdir
        self.logfile = logfile
        self.boottime = boottime
        self.runqemutime = runqemutime

        self.bootlog = ''
        self.qemusock = None

        try:
            self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            self.server_socket.setblocking(0)
            self.server_socket.bind(("127.0.0.1",0))
            self.server_socket.listen(2)
            self.serverport = self.server_socket.getsockname()[1]
            bb.note("Created listening socket for qemu serial console on: 127.0.0.1:%s" % self.serverport)
        except socket.error, msg:
            self.server_socket.close()
            bb.fatal("Failed to create listening socket: %s" %msg[1])


    def log(self, msg):
        if self.logfile:
            with open(self.logfile, "a") as f:
                f.write("%s" % msg)

    def launch(self, qemuparams = None):


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

        self.qemuparams = 'bootparams="console=tty1 console=ttyS0,115200n8" qemuparams="-serial tcp:127.0.0.1:%s"' % self.serverport
        if qemuparams:
            self.qemuparams = self.qemuparams[:-1] + " " + qemuparams + " " + '\"'

        launch_cmd = 'runqemu %s %s %s' % (self.machine, self.rootfs, self.qemuparams)
        self.runqemu = subprocess.Popen(launch_cmd,shell=True,stdout=subprocess.PIPE,stderr=subprocess.STDOUT,preexec_fn=os.setpgrp)

        bb.note("runqemu started, pid is %s" % self.runqemu.pid)
        bb.note("waiting at most %s seconds for qemu pid" % self.runqemutime)
        endtime = time.time() + self.runqemutime
        while not self.is_alive() and time.time() < endtime:
            time.sleep(1)

        if self.is_alive():
            bb.note("qemu started - qemu procces pid is %s" % self.qemupid)
            pscmd = 'ps -p %s -fww | grep -o "192\.168\.7\.[0-9]*::" | awk -F":" \'{print $1}\'' % self.qemupid
            self.ip = subprocess.Popen(pscmd,shell=True,stdout=subprocess.PIPE).communicate()[0].strip()
            if not re.search("^((?:[0-9]{1,3}\.){3}[0-9]{1,3})$", self.ip):
                bb.note("Couldn't get ip from qemu process arguments, I got '%s'" % self.ip)
                bb.note("Here is the ps output:\n%s" % \
                        subprocess.Popen("ps -p %s -fww" % self.qemupid,shell=True,stdout=subprocess.PIPE).communicate()[0])
                self.kill()
                return False
            bb.note("IP found: %s" % self.ip)
            bb.note("Waiting at most %d seconds for login banner" % self.boottime )
            endtime = time.time() + self.boottime
            socklist = [self.server_socket]
            reachedlogin = False
            stopread = False
            while time.time() < endtime and not stopread:
                sread, swrite, serror = select.select(socklist, [], [], 0)
                for sock in sread:
                    if sock is self.server_socket:
                        self.qemusock, addr = self.server_socket.accept()
                        self.qemusock.setblocking(0)
                        socklist.append(self.qemusock)
                        socklist.remove(self.server_socket)
                        bb.note("Connection from %s:%s" % addr)
                    else:
                        data = sock.recv(1024)
                        if data:
                            self.log(data)
                            self.bootlog += data
                            lastlines = "\n".join(self.bootlog.splitlines()[-2:])
                            if re.search("login:", lastlines):
                                stopread = True
                                reachedlogin = True
                                bb.note("Reached login banner")
                        else:
                            socklist.remove(sock)
                            sock.close()
                            stopread = True


            if not reachedlogin:
                bb.note("Target didn't reached login boot in %d seconds" % self.boottime)
                lines = "\n".join(self.bootlog.splitlines()[-5:])
                bb.note("Last 5 lines of text:\n%s" % lines)
                bb.note("Check full boot log: %s" % self.logfile)
                self.kill()
                return False
        else:
            bb.note("Qemu pid didn't appeared in %s seconds" % self.runqemutime)
            self.kill()
            bb.note("Output from runqemu: %s " % self.runqemu.stdout.read())
            self.runqemu.stdout.close()
            return False

        return self.is_alive()


    def kill(self):
        if self.server_socket:
            self.server_socket.close()
            self.server_socket = None
        if self.runqemu.pid:
            os.kill(-self.runqemu.pid,signal.SIGTERM)
            os.kill(-self.runqemu.pid,signal.SIGKILL)
            self.runqemu.pid = None
        self.qemupid = None
        self.ip = None

    def restart(self, qemuparams = None):
        if self.is_alive():
            self.kill()
        bb.note("Qemu Restart required...")
        return self.launch(qemuparams)

    def is_alive(self):
        qemu_child = self.find_child(str(self.runqemu.pid))
        if qemu_child:
            self.qemupid = qemu_child[0]
            if os.path.exists("/proc/" + str(self.qemupid)):
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
            if "qemu-system" in basecmd and "-serial tcp" in commands[p]:
                return [int(p),commands[p]]

