# Recipe creation tool - node.js NPM module support plugin
#
# Copyright (C) 2016 Intel Corporation
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

import logging
import json
from recipetool.create import RecipeHandler, split_pkg_licenses

logger = logging.getLogger('recipetool')


class NpmRecipeHandler(RecipeHandler):
    def _handle_license(self, data):
        '''
        Handle the license value from an npm package.json file
        '''
        license = None
        if 'license' in data:
            license = data['license']
            if isinstance(license, dict):
                license = license.get('type', None)
        return None

    def process(self, srctree, classes, lines_before, lines_after, handled, extravalues):
        import oe
        from collections import OrderedDict

        if 'buildsystem' in handled:
            return False

        def read_package_json(fn):
            with open(fn, 'r') as f:
                return json.loads(f.read())

        files = RecipeHandler.checkfiles(srctree, ['package.json'])
        if files:
            data = read_package_json(files[0])
            if 'name' in data and 'version' in data:
                extravalues['PN'] = data['name']
                extravalues['PV'] = data['version']
                classes.append('npm')
                handled.append('buildsystem')
                if 'description' in data:
                    lines_before.append('SUMMARY = "%s"' % data['description'])
                if 'homepage' in data:
                    lines_before.append('HOMEPAGE = "%s"' % data['homepage'])

                # Split each npm module out to is own package
                npmpackages = oe.package.npm_split_package_dirs(srctree)
                for item in handled:
                    if isinstance(item, tuple):
                        if item[0] == 'license':
                            licvalues = item[1]
                            break
                if licvalues:
                    # Augment the license list with information we have in the packages
                    licenses = {}
                    license = self._handle_license(data)
                    if license:
                        licenses['${PN}'] = license
                    for pkgname, pkgitem in npmpackages.iteritems():
                        _, pdata = pkgitem
                        license = self._handle_license(pdata)
                        if license:
                            licenses[pkgname] = license
                    # Now write out the package-specific license values
                    # We need to strip out the json data dicts for this since split_pkg_licenses
                    # isn't expecting it
                    packages = OrderedDict((x,y[0]) for x,y in npmpackages.iteritems())
                    packages['${PN}'] = ''
                    pkglicenses = split_pkg_licenses(licvalues, packages, lines_after, licenses)
                    all_licenses = list(set([item for pkglicense in pkglicenses.values() for item in pkglicense]))
                    # Go back and update the LICENSE value since we have a bit more
                    # information than when that was written out (and we know all apply
                    # vs. there being a choice, so we can join them with &)
                    for i, line in enumerate(lines_before):
                        if line.startswith('LICENSE = '):
                            lines_before[i] = 'LICENSE = "%s"' % ' & '.join(all_licenses)
                            break

                return True

        return False

def register_recipe_handlers(handlers):
    handlers.append((NpmRecipeHandler(), 60))
