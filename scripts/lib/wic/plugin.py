#!/usr/bin/env python -tt
#
# Copyright (c) 2011 Intel, Inc.
#
# This program is free software; you can redistribute it and/or modify it
# under the terms of the GNU General Public License as published by the Free
# Software Foundation; version 2 of the License
#
# This program is distributed in the hope that it will be useful, but
# WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
# or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
# for more details.
#
# You should have received a copy of the GNU General Public License along
# with this program; if not, write to the Free Software Foundation, Inc., 59
# Temple Place - Suite 330, Boston, MA 02111-1307, USA.

import os
import logging

from importlib.machinery import SourceFileLoader

from wic import pluginbase, WicError
from wic.utils.misc import get_bitbake_var

PLUGIN_TYPES = ["imager", "source"]

SCRIPTS_PLUGIN_DIR = "scripts/lib/wic/plugins"

logger = logging.getLogger('wic')

class PluginMgr:
    _plugin_dirs = []
    _loaded = []

    @classmethod
    def get_plugins(cls, ptype):
        """Get dictionary of <plugin_name>:<class> pairs."""
        if ptype not in PLUGIN_TYPES:
            raise WicError('%s is not valid plugin type' % ptype)

        # collect plugin directories
        if not cls._plugin_dirs:
            cls._plugin_dirs = [os.path.join(os.path.dirname(__file__), 'plugins')]
            layers = get_bitbake_var("BBLAYERS") or ''
            for layer_path in layers.split():
                path = os.path.join(layer_path, SCRIPTS_PLUGIN_DIR)
                path = os.path.abspath(os.path.expanduser(path))
                if path not in cls._plugin_dirs and os.path.isdir(path):
                    cls._plugin_dirs.insert(0, path)

        # load plugins
        for pdir in cls._plugin_dirs:
            ppath = os.path.join(pdir, ptype)
            if ppath not in cls._loaded:
                if os.path.isdir(ppath):
                    for fname in os.listdir(ppath):
                        if fname.endswith('.py'):
                            mname = fname[:-3]
                            mpath = os.path.join(ppath, fname)
                            SourceFileLoader(mname, mpath).load_module()
                cls._loaded.append(ppath)

        return pluginbase.get_plugins(ptype)

    @classmethod
    def get_plugin_methods(cls, ptype, pname, methods):
        """
        The methods param is a dict with the method names to find.  On
        return, the dict values will be filled in with pointers to the
        corresponding methods.  If one or more methods are not found,
        None is returned.
        """
        result = {}
        plugin = cls.get_plugins(ptype).get(pname)
        for method in methods:
            if not hasattr(plugin, method):
                raise WicError("Unimplemented %s plugin interface for: %s" %
                               (method, pname))
            result[method] = getattr(plugin, method)
        return result
