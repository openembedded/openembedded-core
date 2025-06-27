# Recipe creation tool - go support plugin
#
# The code is based on golang internals. See the afftected
# methods for further reference and information.
#
# Copyright (C) 2023 Weidmueller GmbH & Co KG
# Author: Lukas Funke <lukas.funke@weidmueller.com>
#
# SPDX-License-Identifier: GPL-2.0-only
#


from recipetool.create import RecipeHandler, handle_license_vars
from recipetool.create import find_licenses

import bb.utils
import json
import logging
import os
import re
import sys
import tempfile
import urllib.parse
import urllib.request


logger = logging.getLogger('recipetool')

tinfoil = None


def tinfoil_init(instance):
    global tinfoil
    tinfoil = instance



class GoRecipeHandler(RecipeHandler):
    """Class to handle the go recipe creation"""

    @staticmethod
    def __ensure_go():
        """Check if the 'go' command is available in the recipes"""
        recipe = "go-native"
        if not tinfoil.recipes_parsed:
            tinfoil.parse_recipes()
        try:
            rd = tinfoil.parse_recipe(recipe)
        except bb.providers.NoProvider:
            bb.error(
                "Nothing provides '%s' which is required for the build" % (recipe))
            bb.note(
                "You will likely need to add a layer that provides '%s'" % (recipe))
            return None

        bindir = rd.getVar('STAGING_BINDIR_NATIVE')
        gopath = os.path.join(bindir, 'go')

        if not os.path.exists(gopath):
            tinfoil.build_targets(recipe, 'addto_recipe_sysroot')

            if not os.path.exists(gopath):
                logger.error(
                    '%s required to process specified source, but %s did not seem to populate it' % 'go', recipe)
                return None

        return bindir

    @staticmethod
    def __unescape_path(path):
        """Unescape capital letters using exclamation points."""
        return re.sub(r'!([a-z])', lambda m: m.group(1).upper(), path)

    @staticmethod
    def __fold_uri(uri):
        """Fold URI for sorting shorter module paths before longer."""
        return uri.replace(';', ' ').replace('/', '!')

    @staticmethod
    def __go_run_cmd(cmd, cwd, d):
        env = dict(os.environ, PATH=d.getVar('PATH'), GOMODCACHE=d.getVar('GOMODCACHE'))
        return bb.process.run(cmd, env=env, shell=True, cwd=cwd)

    def __go_mod(self, go_mod, srctree, localfilesdir, extravalues, d):
        moddir = d.getVar('GOMODCACHE')

        # List main packages and their dependencies with the go list command.
        stdout, _ = self.__go_run_cmd(f"go list -json=Dir,Module -deps {go_mod['Module']['Path']}/...", srctree, d)
        pkgs = json.loads('[' + stdout.replace('}\n{', '},\n{') + ']')

        # Collect licenses for the dependencies.
        licenses = set()
        lic_files_chksum = []
        lic_files = {}
        for pkg in pkgs:
            # TODO: If the package is in a subdirectory with its own license
            # files then report those istead of the license files found in the
            # module root directory.
            mod = pkg.get('Module', None)
            if not mod or mod.get('Main', False):
                continue
            path = os.path.relpath(mod['Dir'], moddir)
            for lic in find_licenses(mod['Dir'], d):
                lic_files[os.path.join(path, lic[1])] = (lic[0], lic[2])

        for lic_file in lic_files:
            licenses.add(lic_files[lic_file][0])
            lic_files_chksum.append(
                f'file://pkg/mod/{lic_file};md5={lic_files[lic_file][1]}')

        # Collect the module cache files downloaded by the go list command as
        # the go list command knows best what the go list command needs and it
        # needs more files in the module cache than the go install command as
        # it doesn't do the dependency pruning mentioned in the Go module
        # reference, https://go.dev/ref/mod, for go 1.17 or higher.
        src_uris = []
        downloaddir = os.path.join(moddir, 'cache', 'download')
        for dirpath, _, filenames in os.walk(downloaddir):
            path, base = os.path.split(os.path.relpath(dirpath, downloaddir))
            if base != '@v':
                continue
            path = self.__unescape_path(path)
            zipver = None
            for name in filenames:
                ver, ext = os.path.splitext(name)
                if ext == '.zip':
                    chksum = bb.utils.sha256_file(os.path.join(dirpath, name))
                    src_uris.append(f'gomod://{path};version={ver};sha256sum={chksum}')
                    zipver = ver
                    break
            for name in filenames:
                ver, ext = os.path.splitext(name)
                if ext == '.mod' and ver != zipver:
                    chksum = bb.utils.sha256_file(os.path.join(dirpath, name))
                    src_uris.append(f'gomod://{path};version={ver};mod=1;sha256sum={chksum}')

        self.__go_run_cmd("go clean -modcache", srctree, d)

        licenses_basename = "{pn}-licenses.inc"
        licenses_filename = os.path.join(localfilesdir, licenses_basename)
        with open(licenses_filename, "w") as f:
            f.write(f'GO_MOD_LICENSES = "{" & ".join(sorted(licenses))}"\n\n')
            f.write('LIC_FILES_CHKSUM += "\\\n')
            for lic in sorted(lic_files_chksum, key=self.__fold_uri):
                f.write('    ' + lic + ' \\\n')
            f.write('"\n')

        extravalues['extrafiles'][f"../{licenses_basename}"] = licenses_filename

        go_mods_basename = "{pn}-go-mods.inc"
        go_mods_filename = os.path.join(localfilesdir, go_mods_basename)
        with open(go_mods_filename, "w") as f:
            f.write('SRC_URI += "\\\n')
            for uri in sorted(src_uris, key=self.__fold_uri):
                f.write('    ' + uri + ' \\\n')
            f.write('"\n')

        extravalues['extrafiles'][f"../{go_mods_basename}"] = go_mods_filename

    def process(self, srctree, classes, lines_before,
                lines_after, handled, extravalues):

        if 'buildsystem' in handled:
            return False

        files = RecipeHandler.checkfiles(srctree, ['go.mod'])
        if not files:
            return False

        d = bb.data.createCopy(tinfoil.config_data)
        go_bindir = self.__ensure_go()
        if not go_bindir:
            sys.exit(14)

        d.prependVar('PATH', '%s:' % go_bindir)
        handled.append('buildsystem')
        classes.append("go-mod")

        tmp_mod_dir = tempfile.mkdtemp(prefix='go-mod-')
        d.setVar('GOMODCACHE', tmp_mod_dir)

        stdout, _ = self.__go_run_cmd("go mod edit -json", srctree, d)
        go_mod = json.loads(stdout)
        go_import = re.sub(r'/v([0-9]+)$', '', go_mod['Module']['Path'])

        localfilesdir = tempfile.mkdtemp(prefix='recipetool-go-')
        extravalues.setdefault('extrafiles', {})

        # Write the ${BPN}-licenses.inc and ${BPN}-go-mods.inc files
        self.__go_mod(go_mod, srctree, localfilesdir, extravalues, d)

        # Do generic license handling
        handle_license_vars(srctree, lines_before, handled, extravalues, d)
        self.__rewrite_lic_vars(lines_before)

        self.__rewrite_src_uri(lines_before)

        lines_before.append('require ${BPN}-licenses.inc')
        lines_before.append('require ${BPN}-go-mods.inc')
        lines_before.append(f'GO_IMPORT = "{go_import}"')

    def __update_lines_before(self, updated, newlines, lines_before):
        if updated:
            del lines_before[:]
            for line in newlines:
                # Hack to avoid newlines that edit_metadata inserts
                if line.endswith('\n'):
                    line = line[:-1]
                lines_before.append(line)
        return updated

    def __rewrite_lic_vars(self, lines_before):

        def varfunc(varname, origvalue, op, newlines):
            if varname == 'LICENSE':
                return ' & '.join((origvalue, '${GO_MOD_LICENSES}')), None, -1, True
            if varname == 'LIC_FILES_CHKSUM':
                new_licenses = []
                licenses = origvalue.split('\\')
                for license in licenses:
                    if not license:
                        logger.warning("No license file was detected for the main module!")
                        # the license list of the main recipe must be empty
                        # this can happen for example in case of CLOSED license
                        # Fall through to complete recipe generation
                        continue
                    license = license.strip()
                    uri, chksum = license.split(';', 1)
                    url = urllib.parse.urlparse(uri)
                    new_uri = os.path.join(
                        url.scheme + "://", "src", "${GO_IMPORT}", url.netloc + url.path) + ";" + chksum
                    new_licenses.append(new_uri)

                return new_licenses, None, -1, True
            return origvalue, None, 0, True

        updated, newlines = bb.utils.edit_metadata(
            lines_before, ['LICENSE', 'LIC_FILES_CHKSUM'], varfunc)
        return self.__update_lines_before(updated, newlines, lines_before)

    def __rewrite_src_uri(self, lines_before):

        def varfunc(varname, origvalue, op, newlines):
            if varname == 'SRC_URI':
                src_uri = ['git://${GO_IMPORT};protocol=https;nobranch=1;destsuffix=${GO_SRCURI_DESTSUFFIX}']
                return src_uri, None, -1, True
            return origvalue, None, 0, True

        updated, newlines = bb.utils.edit_metadata(lines_before, ['SRC_URI'], varfunc)
        return self.__update_lines_before(updated, newlines, lines_before)


def register_recipe_handlers(handlers):
    handlers.append((GoRecipeHandler(), 60))
