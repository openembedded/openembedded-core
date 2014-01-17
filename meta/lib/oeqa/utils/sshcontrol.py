# Copyright (C) 2013 Intel Corporation
#
# Released under the MIT license (see COPYING.MIT)

# Provides a class for setting up ssh connections,
# running commands and copying files to/from a target.
# It's used by testimage.bbclass and tests in lib/oeqa/runtime.

import subprocess
import time
import os
import select

class SSHControl(object):

    def __init__(self, ip=None, timeout=300, logfile=None):
        self.ip = ip
        self.timeout = timeout
        self._starttime = None
        self._out = ''
        self._ret = 126
        self.logfile = logfile
        self.ssh_options = [
                '-o', 'UserKnownHostsFile=/dev/null',
                '-o', 'StrictHostKeyChecking=no',
                '-o', 'LogLevel=ERROR'
                ]
        self.ssh = ['ssh', '-l', 'root'] + self.ssh_options

    def log(self, msg):
        if self.logfile:
            with open(self.logfile, "a") as f:
                f.write("%s\n" % msg)

    def _internal_run(self, cmd):
        # We need this for a proper PATH
        cmd = ". /etc/profile; " + cmd
        command = self.ssh + [self.ip, cmd]
        self.log("[Running]$ %s" % " ".join(command))
        self._starttime = time.time()
        # ssh hangs without os.setsid
        proc = subprocess.Popen(command, shell=False, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, preexec_fn=os.setsid)
        return proc

    def run(self, cmd, timeout=None):
        """Run cmd and get it's return code and output.
        Let it run for timeout seconds and then terminate/kill it,
        if time is 0 will let cmd run until it finishes.
        Time can be passed to here or can be set per class instance."""

        if self.ip:
            sshconn = self._internal_run(cmd)
        else:
            raise Exception("Remote IP hasn't been set, I can't run ssh without one.")

        # run the command forever
        if timeout == 0:
            output = sshconn.communicate()[0]
        else:
            # use the default timeout
            if timeout is None:
                tdelta = self.timeout
            # use the specified timeout
            else:
                tdelta = timeout
            endtime = self._starttime + tdelta
            output = ''
            eof = False
            while time.time() < endtime and not eof:
                if select.select([sshconn.stdout], [], [], 5)[0] != []:
                    data = os.read(sshconn.stdout.fileno(), 1024)
                    if not data:
                        sshconn.stdout.close()
                        eof = True
                    else:
                        output += data
                        endtime = time.time() + tdelta

            # process hasn't returned yet
            if not eof:
                sshconn.terminate()
                time.sleep(3)
                try:
                    sshconn.kill()
                except OSError:
                    pass
                output += "\n[!!! SSH command killed - no output for %d seconds. Total running time: %d seconds." % (tdelta, time.time() - self._starttime)

        self._ret = sshconn.wait()
        # strip the last LF so we can test the output
        self._out = output.rstrip()
        self.log("%s" % self._out)
        self.log("[SSH command returned after %d seconds]: %s" % (time.time() - self._starttime, self._ret))
        return (self._ret, self._out)

    def _internal_scp(self, cmd):
        cmd = ['scp'] + self.ssh_options + cmd
        self.log("[Running SCP]$ %s" % " ".join(cmd))
        self._starttime = time.time()
        scpconn = subprocess.Popen(cmd, shell=False, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, preexec_fn=os.setsid)
        out = scpconn.communicate()[0]
        ret = scpconn.poll()
        self.log("%s" % out)
        self.log("[SCP command returned after %d seconds]: %s" % (time.time() - self._starttime, ret))
        if ret != 0:
            # we raise an exception so that tests fail in setUp and setUpClass without a need for an assert
            raise Exception("Error running %s, output: %s" % ( " ".join(cmd), out))
        return (ret, out)

    def copy_to(self, localpath, remotepath):
        actualcmd = [localpath, 'root@%s:%s' % (self.ip, remotepath)]
        return self._internal_scp(actualcmd)

    def copy_from(self, remotepath, localpath):
        actualcmd = ['root@%s:%s' % (self.ip, remotepath), localpath]
        return self._internal_scp(actualcmd)
