# Copyright (C) 2013 Intel Corporation
#
# Released under the MIT license (see COPYING.MIT)

# Provides a class for setting up ssh connections,
# running commands and copying files to/from a target.
# It's used by testimage.bbclass and tests in lib/oeqa/runtime.

import subprocess
import time
import os

class SSHControl(object):

    def __init__(self, host=None, timeout=300, logfile=None):
        self.host = host
        self.timeout = timeout
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
        command = self.ssh + [self.host, cmd]
        self.log("[Running]$ %s" % " ".join(command))
        # ssh hangs without os.setsid
        proc = subprocess.Popen(command, shell=False, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, preexec_fn=os.setsid)
        return proc

    def run(self, cmd, timeout=None):
        """Run cmd and get it's return code and output.
        Let it run for timeout seconds and then terminate/kill it,
        if time is 0 will let cmd run until it finishes.
        Time can be passed to here or can be set per class instance."""

        if self.host:
            sshconn = self._internal_run(cmd)
        else:
            raise Exception("Remote IP hasn't been set: '%s'" % actualcmd)

        if timeout == 0:
            self._out = sshconn.communicate()[0]
            self._ret = sshconn.poll()
        else:
            if timeout is None:
                tdelta = self.timeout
            else:
                tdelta = timeout
            endtime = time.time() + tdelta
            while sshconn.poll() is None and time.time() < endtime:
                time.sleep(1)
            # process hasn't returned yet
            if sshconn.poll() is None:
                self._ret = 255
                sshconn.terminate()
                sshconn.kill()
                self._out = sshconn.stdout.read()
                sshconn.stdout.close()
                self._out += "\n[!!! SSH command timed out after %d seconds and it was killed]" % tdelta
                self.log("[!!! SSH command timed out after %d seconds and it was killed]" % tdelta)
            else:
                self._out = sshconn.stdout.read()
                self._ret = sshconn.poll()
        # strip the last LF so we can test the output
        self._out = self._out.rstrip()
        self.log("%s" % self._out)
        self.log("[SSH command returned]: %s" % self._ret)
        return (self._ret, self._out)

    def _internal_scp(self, cmd):
        cmd = ['scp'] + self.ssh_options + cmd
        self.log("[Running SCP]$ %s" % " ".join(cmd))
        scpconn = subprocess.Popen(cmd, shell=False, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, preexec_fn=os.setsid)
        out = scpconn.communicate()[0]
        ret = scpconn.poll()
        self.log("%s" % out)
        if ret != 0:
            # we raise an exception so that tests fail in setUp and setUpClass without a need for an assert
            raise Exception("Error running %s, output: %s" % ( " ".join(cmd), out))
        return (ret, out)

    def copy_to(self, localpath, remotepath):
        actualcmd = [localpath, 'root@%s:%s' % (self.host, remotepath)]
        return self._internal_scp(actualcmd)

    def copy_from(self, remotepath, localpath):
        actualcmd = ['root@%s:%s' % (self.host, remotepath), localpath]
        return self._internal_scp(actualcmd)

    def get_status(self):
        return self._ret

    def get_output(self):
        return self._out
