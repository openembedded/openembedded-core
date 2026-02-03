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
import stat
from enum import Enum, auto
from devtool import DevtoolError
from bb.utils import mkdirhier

logger = logging.getLogger('devtool')


class BuildTool(Enum):
    UNDEFINED = auto()
    CMAKE = auto()
    MESON = auto()

    @property
    def is_c_ccp(self):
        if self is BuildTool.CMAKE:
            return True
        if self is BuildTool.MESON:
            return True
        return False


class GdbServerModes(Enum):
    ONCE = auto()
    ATTACH = auto()
    MULTI = auto()


class GdbCrossConfig:
    """Base class defining the GDB configuration generator interface

    Generate a GDB configuration for a binary on the target device.
    """
    _gdbserver_port_next = 1234
    _gdb_cross_configs = {}

    def __init__(self, image_recipe, modified_recipe, binary, gdbserver_default_mode):
        self.image_recipe = image_recipe
        self.modified_recipe = modified_recipe
        self.gdb_cross = modified_recipe.gdb_cross
        self.binary = binary
        self.gdbserver_default_mode = gdbserver_default_mode
        self.binary_pretty = self.binary.binary_path.replace(os.sep, '-').lstrip('-')
        self.gdbserver_port = GdbCrossConfig._gdbserver_port_next
        GdbCrossConfig._gdbserver_port_next += 1
        self.id_pretty = "%d_%s" % (self.gdbserver_port, self.binary_pretty)

        # Track all generated gdbserver configs to avoid duplicates
        if self.id_pretty in GdbCrossConfig._gdb_cross_configs:
            raise DevtoolError(
                "gdbserver config for binary %s is already generated" % binary)
        GdbCrossConfig._gdb_cross_configs[self.id_pretty] = self

    def id_pretty_mode(self, gdbserver_mode):
        return "%s_%s" % (self.id_pretty, gdbserver_mode.name.lower())

    # GDB and gdbserver script on the host
    @property
    def script_dir(self):
        return self.modified_recipe.ide_sdk_scripts_dir

    @property
    def gdbinit_dir(self):
        return os.path.join(self.script_dir, 'gdbinit')

    def gdbserver_script_file(self, gdbserver_mode):
        return 'gdbserver_' + self.id_pretty_mode(gdbserver_mode)

    def gdbserver_script(self, gdbserver_mode):
        return os.path.join(self.script_dir, self.gdbserver_script_file(gdbserver_mode))

    @property
    def gdbinit(self):
        return os.path.join(
            self.gdbinit_dir, 'gdbinit_' + self.id_pretty)

    @property
    def gdb_script(self):
        return os.path.join(
            self.script_dir, 'gdb_' + self.id_pretty)

    # gdbserver files on the target
    def gdbserver_tmp_dir(self, gdbserver_mode):
        return os.path.join('/tmp', 'gdbserver_%s' % self.id_pretty_mode(gdbserver_mode))

    def gdbserver_pid_file(self, gdbserver_mode):
        return os.path.join(self.gdbserver_tmp_dir(gdbserver_mode), 'gdbserver.pid')

    def gdbserver_log_file(self, gdbserver_mode):
        return os.path.join(self.gdbserver_tmp_dir(gdbserver_mode), 'gdbserver.log')

    def _target_gdbserver_start_cmd(self, gdbserver_mode):
        """Get the ssh command to start gdbserver on the target device

        returns something like:
          "\"/bin/sh -c '/usr/bin/gdbserver --once :1234 /usr/bin/cmake-example'\""
        or for multi mode:
          "\"/bin/sh -c 'if [ \"$1\" = \"stop\" ]; then ... else ... fi'\""
        """
        if gdbserver_mode == GdbServerModes.ONCE:
            gdbserver_cmd_start = "%s --once :%s %s" % (
                self.gdb_cross.gdbserver_path, self.gdbserver_port, self.binary.binary_path)
        elif gdbserver_mode == GdbServerModes.ATTACH:
            pid_command = self.binary.pid_command
            if pid_command:
                gdbserver_cmd_start = "%s --attach :%s \\$(%s)" % (
                    self.gdb_cross.gdbserver_path,
                    self.gdbserver_port,
                    pid_command)
            else:
                raise DevtoolError("Cannot use gdbserver attach mode for binary %s. No PID found." % self.binary.binary_path)
        elif gdbserver_mode == GdbServerModes.MULTI:
            gdbserver_cmd_start = "test -f %s && exit 0; " % self.gdbserver_pid_file(gdbserver_mode)
            gdbserver_cmd_start += "mkdir -p %s; " % self.gdbserver_tmp_dir(gdbserver_mode)
            gdbserver_cmd_start += "%s --multi :%s > %s 2>&1 & " % (
                self.gdb_cross.gdbserver_path, self.gdbserver_port, self.gdbserver_log_file(gdbserver_mode))
            gdbserver_cmd_start += "echo \\$! > %s;" % self.gdbserver_pid_file(gdbserver_mode)
        else:
            raise DevtoolError("Unsupported gdbserver mode: %s" % gdbserver_mode)
        return "\"/bin/sh -c '" + gdbserver_cmd_start + "'\""

    def _target_gdbserver_stop_cmd(self, gdbserver_mode):
        """Kill a gdbserver process"""
        # This is the usual behavior: gdbserver is stopped on demand
        if gdbserver_mode == GdbServerModes.MULTI:
            gdbserver_cmd_stop = "test -f %s && kill \\$(cat %s);" % (
                self.gdbserver_pid_file(gdbserver_mode), self.gdbserver_pid_file(gdbserver_mode))
            gdbserver_cmd_stop += " rm -rf %s" % self.gdbserver_tmp_dir(gdbserver_mode)
        # This is unexpected since gdbserver should terminate after each debug session
        # Just kill all gdbserver instances to keep it simple
        else:
            gdbserver_cmd_stop = "killall gdbserver"
        return "\"/bin/sh -c '" + gdbserver_cmd_stop + "'\""

    def _target_gdbserver_kill_cmd(self):
        """Get the ssh command to kill gdbserver on the target device"""
        return "\"kill \\$(pgrep -o -f 'gdbserver --attach :%s') 2>/dev/null || true\"" % self.gdbserver_port

    def _target_ssh_gdbserver_args(self):
        ssh_args = []
        if self.gdb_cross.target_device.ssh_port:
            ssh_args += ["-p", self.gdb_cross.target_device.ssh_port]
        if self.gdb_cross.target_device.extraoptions:
            ssh_args.extend(self.gdb_cross.target_device.extraoptions)
        if self.gdb_cross.target_device.target:
            ssh_args.append(self.gdb_cross.target_device.target)
        return ssh_args

    def _gen_gdbserver_start_script(self, gdbserver_mode=None):
        """Generate a shell script starting the gdbserver on the remote device via ssh"""
        if gdbserver_mode is None:
            gdbserver_mode = self.gdbserver_default_mode
        gdbserver_cmd_start = self._target_gdbserver_start_cmd(gdbserver_mode)
        gdbserver_cmd_stop = self._target_gdbserver_stop_cmd(gdbserver_mode)
        remote_ssh = "%s %s" % (self.gdb_cross.target_device.ssh_sshexec,
                                " ".join(self._target_ssh_gdbserver_args()))
        gdbserver_cmd = ['#!/bin/sh']
        gdbserver_cmd.append('if [ "$1" = "stop" ]; then')
        gdbserver_cmd.append('  shift')
        gdbserver_cmd.append("  %s %s" % (remote_ssh, gdbserver_cmd_stop))
        gdbserver_cmd.append('else')
        gdbserver_cmd.append("  %s %s" % (remote_ssh, gdbserver_cmd_start))
        gdbserver_cmd.append('fi')
        GdbCrossConfig.write_file(self.gdbserver_script(gdbserver_mode), gdbserver_cmd, True)

    def _gen_gdbinit_config(self, gdbserver_mode=None):
        """Generate a gdbinit file for this binary and the corresponding gdbserver configuration"""
        if gdbserver_mode is None:
            gdbserver_mode = self.gdbserver_default_mode
        gdbinit_lines = ['# This file is generated by devtool ide-sdk']
        if gdbserver_mode == GdbServerModes.MULTI:
            target_help = '#   gdbserver --multi :%d' % self.gdbserver_port
            remote_cmd = 'target extended-remote'
        else:
            target_help = '#   gdbserver :%d %s' % (
                self.gdbserver_port, self.binary)
            remote_cmd = 'target remote'
        gdbinit_lines.append('# On the remote target:')
        gdbinit_lines.append(target_help)
        gdbinit_lines.append('# On the build machine:')
        gdbinit_lines.append('#   cd ' + self.modified_recipe.real_srctree)
        gdbinit_lines.append(
            '#   ' + self.gdb_cross.gdb + ' -ix ' + self.gdbinit)
        gdbinit_lines.append('set sysroot ' + self.modified_recipe.d)

        if self.image_recipe.rootfs_dbg:
            gdbinit_lines.append(
                'set solib-search-path "' + self.modified_recipe.solib_search_path_str(self.image_recipe) + '"')

        gdbinit_lines.append('set substitute-path "/usr/include" "' +
                             os.path.join(self.modified_recipe.recipe_sysroot, 'usr', 'include') + '"')
        if self.image_recipe.rootfs_dbg:
            # First: Search for sources of this recipe in the workspace folder
            if self.modified_recipe.pn in self.modified_recipe.target_dbgsrc_dir:
                gdbinit_lines.append('set substitute-path "%s" "%s"' %
                                     (self.modified_recipe.target_dbgsrc_dir, self.modified_recipe.real_srctree))
            else:
                logger.error(
                    "TARGET_DBGSRC_DIR must contain the recipe name PN.")
            # Second: Search for sources of other recipes in the rootfs-dbg
            if self.modified_recipe.target_dbgsrc_dir.startswith("/usr/src/debug"):
                gdbinit_lines.append('set substitute-path "/usr/src/debug" "%s"' % os.path.join(
                    self.image_recipe.rootfs_dbg, "usr", "src", "debug"))
            else:
                logger.error(
                    "TARGET_DBGSRC_DIR must start with /usr/src/debug.")
        else:
            logger.warning(
                "Cannot setup debug symbols configuration for GDB. IMAGE_GEN_DEBUGFS is not enabled.")
        # Disable debuginfod for now, the IDE configuration uses rootfs-dbg from the image workdir.
        gdbinit_lines.append('set debuginfod enabled off')
        gdbinit_lines.append(
            '%s %s:%d' % (remote_cmd, self.gdb_cross.host, self.gdbserver_port))
        gdbinit_lines.append('set remote exec-file ' + self.binary.binary_path)
        gdbinit_lines.append('run ' + self.binary.binary_path)

        GdbCrossConfig.write_file(self.gdbinit, gdbinit_lines)

    def _gen_gdb_start_script(self):
        """Generate a script starting GDB with the corresponding gdbinit configuration."""
        cmd_lines = ['#!/bin/sh']
        cmd_lines.append('cd ' + self.modified_recipe.real_srctree)
        cmd_lines.append(self.gdb_cross.gdb + ' -ix ' +
                         self.gdbinit + ' "$@"')
        GdbCrossConfig.write_file(self.gdb_script, cmd_lines, True)

    def initialize(self):
        self._gen_gdbserver_start_script()
        if self.binary.runs_as_service and self.gdbserver_default_mode != GdbServerModes.ATTACH:
            self._gen_gdbserver_start_script(GdbServerModes.ATTACH)
        self._gen_gdbinit_config()
        self._gen_gdb_start_script()

    def gdbserver_modes(self):
        """Get the list of gdbserver modes for which scripts are generated"""
        modes = [self.gdbserver_default_mode]
        if self.binary.runs_as_service and self.gdbserver_default_mode != GdbServerModes.ATTACH:
            modes.append(GdbServerModes.ATTACH)
        return modes

    @staticmethod
    def write_file(script_file, cmd_lines, executable=False):
        script_dir = os.path.dirname(script_file)
        mkdirhier(script_dir)
        with open(script_file, 'w') as script_f:
            script_f.write(os.linesep.join(cmd_lines))
            script_f.write(os.linesep)
        if executable:
            st = os.stat(script_file)
            os.chmod(script_file, st.st_mode | stat.S_IEXEC)
        logger.info("Created: %s" % script_file)


class IdeBase:
    """Base class defining the interface for IDE plugins"""

    def __init__(self):
        self.ide_name = 'undefined'
        self.gdb_cross_configs = []

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

    def initialize_gdb_cross_configs(self, image_recipe, modified_recipe, gdb_cross_config_class=GdbCrossConfig):
        for _, exec_bin in modified_recipe.installed_binaries.items():
            gdb_cross_config = gdb_cross_config_class(
                image_recipe, modified_recipe, exec_bin)
            gdb_cross_config.initialize()
            self.gdb_cross_configs.append(gdb_cross_config)

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
