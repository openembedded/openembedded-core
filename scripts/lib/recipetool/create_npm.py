# Copyright (C) 2016 Intel Corporation
# Copyright (C) 2020 Savoir-Faire Linux
#
# SPDX-License-Identifier: GPL-2.0-only
#
"""Recipe creation tool - npm module support plugin"""

import json
import os
import re
import sys
import tempfile
import bb
from bb.fetch2.npm import NpmEnvironment
from recipetool.create import RecipeHandler

TINFOIL = None

def tinfoil_init(instance):
    """Initialize tinfoil"""
    global TINFOIL
    TINFOIL = instance

class NpmRecipeHandler(RecipeHandler):
    """Class to handle the npm recipe creation"""

    @staticmethod
    def _npm_name(name):
        """Generate a Yocto friendly npm name"""
        name = re.sub("/", "-", name)
        name = name.lower()
        name = re.sub(r"[^\-a-z0-9]", "", name)
        name = name.strip("-")
        return name

    @staticmethod
    def _get_registry(lines):
        """Get the registry value from the 'npm://registry' url"""
        registry = None

        def _handle_registry(varname, origvalue, op, newlines):
            nonlocal registry
            if origvalue.startswith("npm://"):
                registry = re.sub(r"^npm://", "http://", origvalue.split(";")[0])
            return origvalue, None, 0, True

        bb.utils.edit_metadata(lines, ["SRC_URI"], _handle_registry)

        return registry

    @staticmethod
    def _ensure_npm():
        """Check if the 'npm' command is available in the recipes"""
        if not TINFOIL.recipes_parsed:
            TINFOIL.parse_recipes()

        try:
            d = TINFOIL.parse_recipe("nodejs-native")
        except bb.providers.NoProvider:
            bb.error("Nothing provides 'nodejs-native' which is required for the build")
            bb.note("You will likely need to add a layer that provides nodejs")
            sys.exit(14)

        bindir = d.getVar("STAGING_BINDIR_NATIVE")
        npmpath = os.path.join(bindir, "npm")

        if not os.path.exists(npmpath):
            TINFOIL.build_targets("nodejs-native", "addto_recipe_sysroot")

            if not os.path.exists(npmpath):
                bb.error("Failed to add 'npm' to sysroot")
                sys.exit(14)

        return bindir

    @staticmethod
    def _npm_global_configs(dev):
        """Get the npm global configuration"""
        configs = []

        if dev:
            configs.append(("also", "development"))
        else:
            configs.append(("only", "production"))

        configs.append(("save", "false"))
        configs.append(("package-lock", "false"))
        configs.append(("shrinkwrap", "false"))
        return configs

    def _run_npm_install(self, d, srctree, registry, dev):
        """Run the 'npm install' command without building the addons"""
        configs = self._npm_global_configs(dev)
        configs.append(("ignore-scripts", "true"))

        if registry:
            configs.append(("registry", registry))

        bb.utils.remove(os.path.join(srctree, "node_modules"), recurse=True)

        env = NpmEnvironment(d, configs=configs)
        env.run("npm install", workdir=srctree)

    def _generate_shrinkwrap(self, d, srctree, dev):
        """Check and generate the 'npm-shrinkwrap.json' file if needed"""
        configs = self._npm_global_configs(dev)

        env = NpmEnvironment(d, configs=configs)
        env.run("npm shrinkwrap", workdir=srctree)

        return os.path.join(srctree, "npm-shrinkwrap.json")

    def process(self, srctree, classes, lines_before, lines_after, handled, extravalues):
        """Handle the npm recipe creation"""

        if "buildsystem" in handled:
            return False

        files = RecipeHandler.checkfiles(srctree, ["package.json"])

        if not files:
            return False

        with open(files[0], "r") as f:
            data = json.load(f)

        if "name" not in data or "version" not in data:
            return False

        extravalues["PN"] = self._npm_name(data["name"])
        extravalues["PV"] = data["version"]

        if "description" in data:
            extravalues["SUMMARY"] = data["description"]

        if "homepage" in data:
            extravalues["HOMEPAGE"] = data["homepage"]

        dev = bb.utils.to_boolean(str(extravalues.get("NPM_INSTALL_DEV", "0")), False)
        registry = self._get_registry(lines_before)

        bb.note("Checking if npm is available ...")
        # The native npm is used here (and not the host one) to ensure that the
        # npm version is high enough to ensure an efficient dependency tree
        # resolution and avoid issue with the shrinkwrap file format.
        # Moreover the native npm is mandatory for the build.
        bindir = self._ensure_npm()

        d = bb.data.createCopy(TINFOIL.config_data)
        d.prependVar("PATH", bindir + ":")
        d.setVar("S", srctree)

        bb.note("Generating shrinkwrap file ...")
        # To generate the shrinkwrap file the dependencies have to be installed
        # first. During the generation process some files may be updated /
        # deleted. By default devtool tracks the diffs in the srctree and raises
        # errors when finishing the recipe if some diffs are found.
        git_exclude_file = os.path.join(srctree, ".git", "info", "exclude")
        if os.path.exists(git_exclude_file):
            with open(git_exclude_file, "r+") as f:
                lines = f.readlines()
                for line in ["/node_modules/", "/npm-shrinkwrap.json"]:
                    if line not in lines:
                        f.write(line + "\n")

        lock_file = os.path.join(srctree, "package-lock.json")
        lock_copy = lock_file + ".copy"
        if os.path.exists(lock_file):
            bb.utils.copyfile(lock_file, lock_copy)

        self._run_npm_install(d, srctree, registry, dev)
        shrinkwrap_file = self._generate_shrinkwrap(d, srctree, dev)

        if os.path.exists(lock_copy):
            bb.utils.movefile(lock_copy, lock_file)

        # Add the shrinkwrap file as 'extrafiles'
        shrinkwrap_copy = shrinkwrap_file + ".copy"
        bb.utils.copyfile(shrinkwrap_file, shrinkwrap_copy)
        extravalues.setdefault("extrafiles", {})
        extravalues["extrafiles"]["npm-shrinkwrap.json"] = shrinkwrap_copy

        url_local = "npmsw://%s" % shrinkwrap_file
        url_recipe= "npmsw://${THISDIR}/${BPN}/npm-shrinkwrap.json"

        if dev:
            url_local += ";dev=1"
            url_recipe += ";dev=1"

        # Add the npmsw url in the SRC_URI of the generated recipe
        def _handle_srcuri(varname, origvalue, op, newlines):
            """Update the version value and add the 'npmsw://' url"""
            value = origvalue.replace("version=" + data["version"], "version=${PV}")
            value = value.replace("version=latest", "version=${PV}")
            values = [line.strip() for line in value.strip('\n').splitlines()]
            values.append(url_recipe)
            return values, None, 4, False

        (_, newlines) = bb.utils.edit_metadata(lines_before, ["SRC_URI"], _handle_srcuri)
        lines_before[:] = [line.rstrip('\n') for line in newlines]

        classes.append("npm")
        handled.append("buildsystem")

        return True

def register_recipe_handlers(handlers):
    """Register the npm handler"""
    handlers.append((NpmRecipeHandler(), 60))
