#
# Copyright (C) 2023-2024 Siemens AG
#
# SPDX-License-Identifier: GPL-2.0-only
#
"""Devtool ide-sdk IDE plugin interface definition and helper functions"""

import errno
import json
import logging
import os
from enum import Enum, auto
from devtool import DevtoolError

logger = logging.getLogger('devtool')


class BuildTool(Enum):
    UNDEFINED = auto()
    CMAKE = auto()
    MESON = auto()
    KERNEL_MODULE = auto()

    @property
    def is_c_cpp(self):
        if self is BuildTool.CMAKE:
            return True
        if self is BuildTool.MESON:
            return True
        return False

    @property
    def is_c_cpp_kernel(self):
        if self.is_c_cpp or self is BuildTool.KERNEL_MODULE:
            return True
        return False


class DebuggerServerModes(Enum):
    ONCE = auto()
    ATTACH = auto()
    MULTI = auto()


class DebuggerCrossConfig:
    """Base class defining the cross-debugger configuration generator interface.

    Manages the per-binary port assignment, script paths, and SSH argument
    construction that are common to all debugger back-ends (GDB, LLDB).
    Concrete subclasses provide the back-end-specific remote start/kill commands.
    """
    _port_next = 1234
    _configs = {}

    def __init__(self, image_recipe, modified_recipe, binary, default_mode):
        self.image_recipe = image_recipe
        self.modified_recipe = modified_recipe
        self.debugger_cross = modified_recipe.debugger_cross
        self.binary = binary
        self.default_mode = default_mode
        self.binary_pretty = self.binary.binary_path.replace(os.sep, '-').lstrip('-')
        self.debug_server_port = DebuggerCrossConfig._port_next
        DebuggerCrossConfig._port_next += 1
        self.id_pretty = "%d_%s" % (self.debug_server_port, self.binary_pretty)

        if self.id_pretty in DebuggerCrossConfig._configs:
            raise DevtoolError(
                "debugger config for binary %s is already generated" % binary)
        DebuggerCrossConfig._configs[self.id_pretty] = self

    def id_pretty_mode(self, mode):
        return "%s_%s" % (self.id_pretty, mode.name.lower())

    # Host-side script paths
    @property
    def script_dir(self):
        return self.modified_recipe.ide_sdk_scripts_dir

    def server_script(self, mode):
        raise NotImplementedError

    # SSH argument helpers
    def _target_ssh_args(self):
        ssh_args = []
        if self.debugger_cross.target_device.ssh_port:
            ssh_args += self.debugger_cross.target_device.ssh_port
        if self.debugger_cross.target_device.extraoptions:
            ssh_args.extend(self.debugger_cross.target_device.extraoptions)
        if self.debugger_cross.target_device.target:
            ssh_args.append(self.debugger_cross.target_device.target)
        return ssh_args

    def server_modes(self):
        """List of debug-server modes for which scripts are generated."""
        modes = [self.default_mode]
        if self.binary.runs_as_service and self.default_mode != DebuggerServerModes.ATTACH:
            modes.append(DebuggerServerModes.ATTACH)
        return modes

    def initialize(self):
        """Called after construction to generate any required config files."""
        pass

    # Abstract — subclasses must implement
    def _target_start_cmd(self, mode):
        raise NotImplementedError

    def _target_kill_cmd(self):
        raise NotImplementedError


class GdbCrossConfig(DebuggerCrossConfig):
    """GDB-specific cross-debugging configuration.

    Manages gdbserver on the target and gdb-cross on the host.  Provides
    gdbinit / gdb wrapper scripts used by ide=none as well as the
    target-side tmp/pid/log paths consumed by the gdbserver start command.
    """

    def __init__(self, image_recipe, modified_recipe, binary,
                 default_mode=DebuggerServerModes.MULTI):
        super().__init__(image_recipe, modified_recipe, binary,
                         default_mode)

    # GDB-specific host paths
    @property
    def gdbinit_dir(self):
        return os.path.join(self.script_dir, 'gdbinit')

    @property
    def gdbinit(self):
        return os.path.join(self.gdbinit_dir, 'gdbinit_' + self.id_pretty)

    @property
    def gdb_script(self):
        return os.path.join(self.script_dir, 'gdb_' + self.id_pretty)

    def server_script_file(self, mode):
        return 'gdbserver_' + self.id_pretty_mode(mode)

    def server_script(self, mode):
        return os.path.join(self.script_dir, self.server_script_file(mode))

    # gdbserver files on the target
    def _gdbserver_tmp_dir(self, mode):
        return os.path.join('/tmp', 'gdbserver_%s' % self.id_pretty_mode(mode))

    def _gdbserver_pid_file(self, mode):
        return os.path.join(self._gdbserver_tmp_dir(mode), 'gdbserver.pid')

    def _gdbserver_log_file(self, mode):
        return os.path.join(self._gdbserver_tmp_dir(mode), 'gdbserver.log')

    def _target_start_cmd(self, server_mode):
        """SSH command to start gdbserver on the target device.

        Returns something like:
          "\"/bin/sh -c '/usr/bin/gdbserver --once :1234 /usr/bin/cmake-example'\""
        """
        if server_mode == DebuggerServerModes.ONCE:
            gdbserver_cmd_start = "%s --once :%s %s" % (
                self.debugger_cross.debug_server_path, self.debug_server_port, self.binary.binary_path)
        elif server_mode == DebuggerServerModes.ATTACH:
            pid_command = self.binary.pid_command
            if pid_command:
                gdbserver_cmd_start = "%s --attach :%s \\$(%s)" % (
                    self.debugger_cross.debug_server_path,
                    self.debug_server_port,
                    pid_command)
            else:
                raise DevtoolError("Cannot use gdbserver attach mode for binary %s. No PID found." % self.binary.binary_path)
        elif server_mode == DebuggerServerModes.MULTI:
            hex_port = "%04X" % self.debug_server_port
            gdbserver_cmd_start = "grep -q :%s /proc/net/tcp /proc/net/tcp6 2>/dev/null && exit 0; " % hex_port
            gdbserver_cmd_start += "mkdir -p %s; " % self._gdbserver_tmp_dir(server_mode)
            gdbserver_cmd_start += "%s --multi :%s > %s 2>&1 & " % (
                self.debugger_cross.debug_server_path, self.debug_server_port, self._gdbserver_log_file(server_mode))
            gdbserver_cmd_start += "echo \\$! > %s; " % self._gdbserver_pid_file(server_mode)
            gdbserver_cmd_start += "_w=0; while ! grep -q :%s /proc/net/tcp /proc/net/tcp6 2>/dev/null; " % hex_port
            gdbserver_cmd_start += "do _w=\\$((_w+1)); [ \\$_w -lt 100 ] || exit 1; sleep 0.1; done;"
        else:
            raise DevtoolError("Unsupported gdbserver mode: %s" % server_mode)
        return "\"/bin/sh -c '" + gdbserver_cmd_start + "'\""

    def _target_kill_cmd(self):
        """SSH command to kill gdbserver on the target device."""
        return "\"kill \\$(pgrep -o -f 'gdbserver --attach :%s') 2>/dev/null || true\"" % self.debug_server_port


class LldbServerConfig(DebuggerCrossConfig):
    """Configure lldb-server (platform mode) on the target for CodeLLDB remote debugging.

    Unlike gdbserver, lldb-server platform mode is architecture-agnostic on the host
    side: a single lldb-native binary handles all target architectures via the
    LLDB platform protocol that CodeLLDB speaks natively.

    The ATTACH mode is not supported because lldb-server platform does not take a
    PID argument; attaching is done client-side via 'process attach'.
    """

    def __init__(self, image_recipe, modified_recipe, binary,
                 default_mode=DebuggerServerModes.MULTI):
        super().__init__(image_recipe, modified_recipe, binary,
                         default_mode)

    def _lldb_server_tmp_dir(self, mode):
        return os.path.join('/tmp', 'lldb_server_%s' % self.id_pretty_mode(mode))

    def _lldb_server_pid_file(self, mode):
        return os.path.join(self._lldb_server_tmp_dir(mode), 'lldb_server.pid')

    def _lldb_server_log_file(self, mode):
        return os.path.join(self._lldb_server_tmp_dir(mode), 'lldb_server.log')

    def _target_start_cmd(self, mode):
        """SSH command to start lldb-server in platform mode on the target."""
        lldb_server = self.debugger_cross.debug_server_path
        # Use '*:<port>' so lldb-server binds on all interfaces (0.0.0.0), not
        # just loopback.  The bare ':<port>' form only binds to 127.0.0.1 in
        # lldb-server 21.x and the remote lldb client connects from the host.
        # Start from /tmp because lldb-server creates temp files in its cwd and
        # the SSH default cwd (/home/root) may not exist on a minimal image.
        if mode == DebuggerServerModes.ONCE:
            cmd = "cd /tmp && %s platform --one-shot --server --listen *:%s" % (
                lldb_server, self.debug_server_port)
        elif mode == DebuggerServerModes.MULTI:
            hex_port = "%04X" % self.debug_server_port
            pid_file = self._lldb_server_pid_file(mode)
            tmp_dir = self._lldb_server_tmp_dir(mode)
            log_file = self._lldb_server_log_file(mode)
            cmd = "grep -q :%s /proc/net/tcp /proc/net/tcp6 2>/dev/null && exit 0; " % hex_port
            cmd += "mkdir -p %s; " % tmp_dir
            cmd += "cd %s; " % tmp_dir
            cmd += "%s platform --server --listen *:%s > %s 2>&1 & " % (
                lldb_server, self.debug_server_port, log_file)
            cmd += "echo \\$! > %s; " % pid_file
            cmd += "_w=0; while ! grep -q :%s /proc/net/tcp /proc/net/tcp6 2>/dev/null; " % hex_port
            cmd += "do _w=\\$((_w+1)); [ \\$_w -lt 100 ] || { echo lldb-server did not start on port %s >&2; exit 1; }; sleep 0.1; done;" % self.debug_server_port
        else:
            raise DevtoolError(
                "lldb-server does not support mode %s "
                "(ATTACH is handled client-side with 'process attach')" % mode)
        return "\"/bin/sh -c '" + cmd + "'\""

    def _target_kill_cmd(self):
        """SSH command to stop a MULTI-mode lldb-server on the target."""
        pid_file = self._lldb_server_pid_file(DebuggerServerModes.MULTI)
        tmp_dir = self._lldb_server_tmp_dir(DebuggerServerModes.MULTI)
        cmd = ("test -f %(pf)s && kill \\$(cat %(pf)s) 2>/dev/null; rm -rf %(td)s"
               % {'pf': pid_file, 'td': tmp_dir})
        return "\"/bin/sh -c '" + cmd + "'\""

    def server_script_file(self, mode):
        return 'lldb_server_' + self.id_pretty_mode(mode)

    def server_script(self, mode):
        return os.path.join(self.script_dir, self.server_script_file(mode))

    def server_modes(self):
        """ATTACH mode is not applicable for lldb-server platform."""
        return [self.default_mode]

class IdeBase:
    """Base class defining the interface for IDE plugins"""

    def __init__(self):
        self.ide_name = 'undefined'
        self.cross_debug_configs = []

    @classmethod
    def ide_plugin_priority(cls):
        """Used to find the default ide handler if --ide is not passed"""
        return 10

    def setup_shared_sysroots(self, shared_env):
        logger.warn("Shared sysroot mode is not supported for IDE %s" %
                    self.ide_name)

    def setup_modified_recipe(self, args, image_recipe, modified_recipe):
        logger.warn("Modified recipe mode is not supported for IDE %s" %
                    self.ide_name)

    def initialize_cross_debug_configs(self, image_recipe, modified_recipe, cross_debug_config_class=GdbCrossConfig):
        for _, exec_bin in modified_recipe.installed_binaries.items():
            cross_debug_config = cross_debug_config_class(
                image_recipe, modified_recipe, exec_bin)
            cross_debug_config.initialize()
            self.cross_debug_configs.append(cross_debug_config)

    @staticmethod
    def gen_oe_scripts_sym_link(modified_recipe):
        # create a sym-link from sources to the scripts directory
        if os.path.isdir(modified_recipe.ide_sdk_scripts_dir):
            IdeBase.symlink_force(modified_recipe.ide_sdk_scripts_dir,
                                  os.path.join(modified_recipe.real_srctree, 'oe-scripts'))

    @staticmethod
    def update_json_file(json_dir, json_file, update_dict):
        """Update a json file

        By default it uses the dict.update function. If this is not sutiable
        the update function might be passed via update_func parameter.
        """
        json_path = os.path.join(json_dir, json_file)
        logger.info("Updating IDE config file: %s (%s)" %
                    (json_file, json_path))
        if not os.path.exists(json_dir):
            os.makedirs(json_dir)
        try:
            with open(json_path) as f:
                orig_dict = json.load(f)
        except json.decoder.JSONDecodeError:
            logger.info(
                "Decoding %s failed. Probably because of comments in the json file" % json_path)
            orig_dict = {}
        except FileNotFoundError:
            orig_dict = {}
        orig_dict.update(update_dict)
        with open(json_path, 'w') as f:
            json.dump(orig_dict, f, indent=4)

    @staticmethod
    def symlink_force(tgt, dst):
        try:
            os.symlink(tgt, dst)
        except OSError as err:
            if err.errno == errno.EEXIST:
                if os.readlink(dst) != tgt:
                    os.remove(dst)
                    os.symlink(tgt, dst)
            else:
                raise err


def get_devtool_deploy_opts(args):
    """Filter args for devtool deploy-target args"""
    if not args.target:
        return None
    devtool_deploy_opts = [args.target]
    if args.no_host_check:
        devtool_deploy_opts += ["-c"]
    if args.show_status:
        devtool_deploy_opts += ["-s"]
    if args.no_preserve:
        devtool_deploy_opts += ["-p"]
    if args.no_check_space:
        devtool_deploy_opts += ["--no-check-space"]
    if args.ssh_exec:
        devtool_deploy_opts += ["-e", args.ssh.exec]
    if args.port:
        devtool_deploy_opts += ["-P", args.port]
    if args.key:
        devtool_deploy_opts += ["-I", args.key]
    if args.strip is False:
        devtool_deploy_opts += ["--no-strip"]
    return devtool_deploy_opts
