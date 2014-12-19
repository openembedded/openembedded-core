# Script utility functions
#
# Copyright (C) 2014 Intel Corporation
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License version 2 as
# published by the Free Software Foundation.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License along
# with this program; if not, write to the Free Software Foundation, Inc.,
# 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

import sys
import os
import logging
import glob

def logger_create(name):
    logger = logging.getLogger(name)
    loggerhandler = logging.StreamHandler()
    loggerhandler.setFormatter(logging.Formatter("%(levelname)s: %(message)s"))
    logger.addHandler(loggerhandler)
    logger.setLevel(logging.INFO)
    return logger

def logger_setup_color(logger, color='auto'):
    from bb.msg import BBLogFormatter
    console = logging.StreamHandler(sys.stdout)
    formatter = BBLogFormatter("%(levelname)s: %(message)s")
    console.setFormatter(formatter)
    logger.handlers = [console]
    if color == 'always' or (color=='auto' and console.stream.isatty()):
        formatter.enable_color()


def load_plugins(logger, plugins, pluginpath):
    import imp

    def load_plugin(name):
        logger.debug('Loading plugin %s' % name)
        fp, pathname, description = imp.find_module(name, [pluginpath])
        try:
            return imp.load_module(name, fp, pathname, description)
        finally:
            if fp:
                fp.close()

    logger.debug('Loading plugins from %s...' % pluginpath)
    for fn in glob.glob(os.path.join(pluginpath, '*.py')):
        name = os.path.splitext(os.path.basename(fn))[0]
        if name != '__init__':
            plugin = load_plugin(name)
            if hasattr(plugin, 'plugin_init'):
                plugin.plugin_init(plugins)
                plugins.append(plugin)
