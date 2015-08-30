import os
import sys
import errno
import datetime
import itertools
from commands import runCmd

def get_host_dumper(d):
    return HostDumper(d)


class BaseDumper(object):

    def __init__(self, d, cmds):
        self.cmds = []
        self.parent_dir = d.getVar("TESTIMAGE_DUMP_DIR", True)
        if not cmds:
            return
        for cmd in cmds.split('\n'):
            cmd = cmd.lstrip()
            if not cmd or cmd[0] == '#':
                continue
            # Replae variables from the datastore
            while True:
                index_start = cmd.find("${")
                if index_start == -1:
                    break
                index_start += 2
                index_end = cmd.find("}", index_start)
                var = cmd[index_start:index_end]
                value = d.getVar(var, True)
                cmd = cmd.replace("${%s}" % var, value)
            self.cmds.append(cmd)

    def create_dir(self, dir_suffix):
        dump_subdir = ("%s_%s" % (
                datetime.datetime.now().strftime('%Y%m%d%H%M'),
                dir_suffix))
        dump_dir = os.path.join(self.parent_dir, dump_subdir)
        try:
            os.makedirs(dump_dir)
        except OSError as err:
            if err.errno != errno.EEXIST:
                raise err
        self.dump_dir = dump_dir

    def _write_dump(self, command, output):
        if isinstance(self, HostDumper):
            prefix = "host"
        elif isinstance(self, TargetDumper):
            prefix = "target"
        else:
            prefix = "unknown"
        for i in itertools.count():
            filename = "%s_%02d_%s" % (prefix, i, command)
            fullname = os.path.join(self.dump_dir, filename)
            if not os.path.exists(fullname):
                break
        with open(fullname, 'w') as dump_file:
            dump_file.write(output)


class HostDumper(BaseDumper):

    def __init__(self, d):
        host_cmds = d.getVar("testimage_dump_host", True)
        super(HostDumper, self).__init__(d, host_cmds)

    def dump_host(self, dump_dir=""):
        if dump_dir:
            self.dump_dir = dump_dir
        for cmd in self.cmds:
            result = runCmd(cmd, ignore_status=True)
            self._write_dump(cmd.split()[0], result.output)


class TargetDumper(BaseDumper):

    def __init__(self, d, qemurunner):
        target_cmds = d.getVar("testimage_dump_target", True)
        super(TargetDumper, self).__init__(d, target_cmds)
        self.runner = qemurunner

    def dump_target(self, dump_dir=""):
        if dump_dir:
            self.dump_dir = dump_dir
        for cmd in self.cmds:
            (status, output) = self.runner.run_serial(cmd)
            self._write_dump(cmd.split()[0], output)
