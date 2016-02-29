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
from recipetool.create import RecipeHandler

logger = logging.getLogger('recipetool')


class NpmRecipeHandler(RecipeHandler):
    def process(self, srctree, classes, lines_before, lines_after, handled, extravalues):
        if 'buildsystem' in handled:
            return False

        files = RecipeHandler.checkfiles(srctree, ['package.json'])
        if files:
            with open(files[0], 'r') as f:
                data = json.loads(f.read())
            if 'name' in data and 'version' in data:
                extravalues['PN'] = data['name']
                extravalues['PV'] = data['version']
                classes.append('npm')
                handled.append('buildsystem')
                if 'description' in data:
                    lines_before.append('SUMMARY = "%s"' % data['description'])
                if 'homepage' in data:
                    lines_before.append('HOMEPAGE = "%s"' % data['homepage'])
                return True

        return False

def register_recipe_handlers(handlers):
    handlers.append((NpmRecipeHandler(), 60))
