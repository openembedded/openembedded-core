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
import sys
import logging

from wic import pluginbase, WicError
from wic.utils.misc import get_bitbake_var

PLUGIN_TYPES = ["imager", "source"]

PLUGIN_DIR = "/lib/wic/plugins" # relative to scripts
SCRIPTS_PLUGIN_DIR = "scripts" + PLUGIN_DIR

logger = logging.getLogger('wic')

class PluginMgr:
    plugin_dirs = {}
    wic_path = os.path.dirname(__file__)
    eos = wic_path.rfind('scripts') + len('scripts')
    scripts_path = wic_path[:eos]
    plugin_dir = scripts_path + PLUGIN_DIR
    layers_path = None

    @classmethod
    def _build_plugin_dir_list(cls, plugin_dir, ptype):
        if cls.layers_path is None:
            cls.layers_path = get_bitbake_var("BBLAYERS")
        layer_dirs = []

        if cls.layers_path is not None:
            for layer_path in cls.layers_path.split():
                path = os.path.join(layer_path, SCRIPTS_PLUGIN_DIR, ptype)
                layer_dirs.append(path)

        path = os.path.join(plugin_dir, ptype)
        layer_dirs.append(path)

        return layer_dirs

    @classmethod
    def append_dirs(cls, dirs):
        for path in dirs:
            cls._add_plugindir(path)

        # load all the plugins AGAIN
        cls._load_all()

    @classmethod
    def _add_plugindir(cls, path):
        path = os.path.abspath(os.path.expanduser(path))

        if not os.path.isdir(path):
            return

        if path not in cls.plugin_dirs:
            cls.plugin_dirs[path] = False
            # the value True/False means "loaded"

    @classmethod
    def _load_all(cls):
        for (pdir, loaded) in cls.plugin_dirs.items():
            if loaded:
                continue

            sys.path.insert(0, pdir)
            for mod in [x[:-3] for x in os.listdir(pdir) if x.endswith(".py")]:
                if mod and mod != '__init__':
                    if mod in sys.modules:
                        logger.warning("Module %s already exists, skip", mod)
                    else:
                        try:
                            pymod = __import__(mod)
                            cls.plugin_dirs[pdir] = True
                            logger.debug("Plugin module %s:%s imported",
                                         mod, pymod.__file__)
                        except ImportError as err:
                            logger.warning('Failed to load plugin %s/%s: %s',
                                           os.path.basename(pdir), mod, err)

            del sys.path[0]

    @classmethod
    def get_plugins(cls, ptype):
        """ the return value is dict of name:class pairs """

        if ptype not in PLUGIN_TYPES:
            raise WicError('%s is not valid plugin type' % ptype)

        plugins_dir = cls._build_plugin_dir_list(cls.plugin_dir, ptype)

        cls.append_dirs(plugins_dir)

        return pluginbase.get_plugins(ptype)

    @classmethod
    def get_source_plugins(cls):
        """
        Return list of available source plugins.
        """
        plugins_dir = cls._build_plugin_dir_list(cls.plugin_dir, 'source')

        cls.append_dirs(plugins_dir)

        return cls.get_plugins('source')


    @classmethod
    def get_source_plugin_methods(cls, source_name, methods):
        """
        The methods param is a dict with the method names to find.  On
        return, the dict values will be filled in with pointers to the
        corresponding methods.  If one or more methods are not found,
        None is returned.
        """
        return_methods = None
        for _source_name, klass in cls.get_plugins('source').items():
            if _source_name == source_name:
                for _method_name in methods:
                    if not hasattr(klass, _method_name):
                        logger.warning("Unimplemented %s source interface for: %s",
                                       _method_name, _source_name)
                        return None
                    func = getattr(klass, _method_name)
                    methods[_method_name] = func
                    return_methods = methods
        return return_methods
