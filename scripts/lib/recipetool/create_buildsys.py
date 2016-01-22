# Recipe creation tool - create command build system handlers
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

import re
import logging
from recipetool.create import RecipeHandler, validate_pv

logger = logging.getLogger('recipetool')

tinfoil = None

def tinfoil_init(instance):
    global tinfoil
    tinfoil = instance


class CmakeRecipeHandler(RecipeHandler):
    def process(self, srctree, classes, lines_before, lines_after, handled, extravalues):
        if 'buildsystem' in handled:
            return False

        if RecipeHandler.checkfiles(srctree, ['CMakeLists.txt']):
            classes.append('cmake')
            values = CmakeRecipeHandler.extract_cmake_deps(lines_before, srctree, extravalues)
            classes.extend(values.pop('inherit', '').split())
            for var, value in values.iteritems():
                lines_before.append('%s = "%s"' % (var, value))
            lines_after.append('# Specify any options you want to pass to cmake using EXTRA_OECMAKE:')
            lines_after.append('EXTRA_OECMAKE = ""')
            lines_after.append('')
            handled.append('buildsystem')
            return True
        return False

    @staticmethod
    def extract_cmake_deps(outlines, srctree, extravalues, cmakelistsfile=None):
        values = {}
        inherits = []

        if cmakelistsfile:
            srcfiles = [cmakelistsfile]
        else:
            srcfiles = RecipeHandler.checkfiles(srctree, ['CMakeLists.txt'])

        # Note that some of these are non-standard, but probably better to
        # be able to map them anyway if we see them
        cmake_pkgmap = {'alsa': 'alsa-lib',
                        'aspell': 'aspell',
                        'atk': 'atk',
                        'bison': 'bison-native',
                        'boost': 'boost',
                        'bzip2': 'bzip2',
                        'cairo': 'cairo',
                        'cups': 'cups',
                        'curl': 'curl',
                        'curses': 'ncurses',
                        'cvs': 'cvs',
                        'drm': 'libdrm',
                        'dbus': 'dbus',
                        'dbusglib': 'dbus-glib',
                        'egl': 'virtual/egl',
                        'expat': 'expat',
                        'flex': 'flex-native',
                        'fontconfig': 'fontconfig',
                        'freetype': 'freetype',
                        'gettext': '',
                        'git': '',
                        'gio': 'glib-2.0',
                        'giounix': 'glib-2.0',
                        'glew': 'glew',
                        'glib': 'glib-2.0',
                        'glib2': 'glib-2.0',
                        'glu': 'libglu',
                        'glut': 'freeglut',
                        'gobject': 'glib-2.0',
                        'gperf': 'gperf-native',
                        'gnutls': 'gnutls',
                        'gtk2': 'gtk+',
                        'gtk3': 'gtk+3',
                        'gtk': 'gtk+3',
                        'harfbuzz': 'harfbuzz',
                        'icu': 'icu',
                        'intl': 'virtual/libintl',
                        'jpeg': 'jpeg',
                        'libarchive': 'libarchive',
                        'libiconv': 'virtual/libiconv',
                        'liblzma': 'xz',
                        'libxml2': 'libxml2',
                        'libxslt': 'libxslt',
                        'opengl': 'virtual/libgl',
                        'openmp': '',
                        'openssl': 'openssl',
                        'pango': 'pango',
                        'perl': '',
                        'perllibs': '',
                        'pkgconfig': '',
                        'png': 'libpng',
                        'pthread': '',
                        'pythoninterp': '',
                        'pythonlibs': '',
                        'ruby': 'ruby-native',
                        'sdl': 'libsdl',
                        'sdl2': 'libsdl2',
                        'subversion': 'subversion-native',
                        'swig': 'swig-native',
                        'tcl': 'tcl-native',
                        'threads': '',
                        'tiff': 'tiff',
                        'wget': 'wget',
                        'x11': 'libx11',
                        'xcb': 'libxcb',
                        'xext': 'libxext',
                        'xfixes': 'libxfixes',
                        'zlib': 'zlib',
                        }

        pcdeps = []
        libdeps = []
        deps = []
        unmappedpkgs = []

        proj_re = re.compile('project\s*\(([^)]*)\)', re.IGNORECASE)
        pkgcm_re = re.compile('pkg_check_modules\s*\(\s*[a-zA-Z0-9-_]+\s*(REQUIRED)?\s+([^)\s]+)\s*\)', re.IGNORECASE)
        pkgsm_re = re.compile('pkg_search_module\s*\(\s*[a-zA-Z0-9-_]+\s*(REQUIRED)?((\s+[^)\s]+)+)\s*\)', re.IGNORECASE)
        findpackage_re = re.compile('find_package\s*\(\s*([a-zA-Z0-9-_]+)\s*.*', re.IGNORECASE)
        checklib_re = re.compile('check_library_exists\s*\(\s*([^\s)]+)\s*.*', re.IGNORECASE)
        include_re = re.compile('include\s*\(\s*([^)\s]*)\s*\)', re.IGNORECASE)
        subdir_re = re.compile('add_subdirectory\s*\(\s*([^)\s]*)\s*([^)\s]*)\s*\)', re.IGNORECASE)
        dep_re = re.compile('([^ ><=]+)( *[<>=]+ *[^ ><=]+)?')

        def interpret_value(value):
            return value.strip('"')

        def parse_cmake_file(fn, paths=None):
            searchpaths = (paths or []) + [os.path.dirname(fn)]
            logger.debug('Parsing file %s' % fn)
            with open(fn, 'r') as f:
                for line in f:
                    line = line.strip()
                    res = include_re.match(line)
                    if res:
                        includefn = bb.utils.which(':'.join(searchpaths), res.group(1))
                        if includefn:
                            parse_cmake_file(includefn, searchpaths)
                        else:
                            logger.debug('Unable to recurse into include file %s' % res.group(1))
                        continue
                    res = subdir_re.match(line)
                    if res:
                        subdirfn = os.path.join(os.path.dirname(fn), res.group(1), 'CMakeLists.txt')
                        if os.path.exists(subdirfn):
                            parse_cmake_file(subdirfn, searchpaths)
                        else:
                            logger.debug('Unable to recurse into subdirectory file %s' % subdirfn)
                        continue
                    res = proj_re.match(line)
                    if res:
                        extravalues['PN'] = interpret_value(res.group(1).split()[0])
                        continue
                    res = pkgcm_re.match(line)
                    if res:
                        res = dep_re.findall(res.group(2))
                        if res:
                            pcdeps.extend([interpret_value(x[0]) for x in res])
                        inherits.append('pkgconfig')
                        continue
                    res = pkgsm_re.match(line)
                    if res:
                        res = dep_re.findall(res.group(2))
                        if res:
                            # Note: appending a tuple here!
                            item = tuple((interpret_value(x[0]) for x in res))
                            if len(item) == 1:
                                item = item[0]
                            pcdeps.append(item)
                        inherits.append('pkgconfig')
                        continue
                    res = findpackage_re.match(line)
                    if res:
                        origpkg = res.group(1)
                        pkg = interpret_value(origpkg.lower())
                        if pkg == 'gettext':
                            inherits.append('gettext')
                        elif pkg == 'perl':
                            inherits.append('perlnative')
                        elif pkg == 'pkgconfig':
                            inherits.append('pkgconfig')
                        elif pkg == 'pythoninterp':
                            inherits.append('pythonnative')
                        elif pkg == 'pythonlibs':
                            inherits.append('python-dir')
                        else:
                            dep = cmake_pkgmap.get(pkg, None)
                            if dep:
                                deps.append(dep)
                            elif dep is None:
                                unmappedpkgs.append(origpkg)
                        continue
                    res = checklib_re.match(line)
                    if res:
                        lib = interpret_value(res.group(1))
                        if not lib.startswith('$'):
                            libdeps.append(lib)
                    if line.lower().startswith('useswig'):
                        deps.append('swig-native')
                        continue

        parse_cmake_file(srcfiles[0])

        if unmappedpkgs:
            outlines.append('# NOTE: unable to map the following CMake package dependencies: %s' % ' '.join(unmappedpkgs))

        RecipeHandler.handle_depends(libdeps, pcdeps, deps, outlines, values, tinfoil.config_data)

        if inherits:
            values['inherit'] = ' '.join(list(set(inherits)))

        return values

class SconsRecipeHandler(RecipeHandler):
    def process(self, srctree, classes, lines_before, lines_after, handled, extravalues):
        if 'buildsystem' in handled:
            return False

        if RecipeHandler.checkfiles(srctree, ['SConstruct', 'Sconstruct', 'sconstruct']):
            classes.append('scons')
            lines_after.append('# Specify any options you want to pass to scons using EXTRA_OESCONS:')
            lines_after.append('EXTRA_OESCONS = ""')
            lines_after.append('')
            handled.append('buildsystem')
            return True
        return False

class QmakeRecipeHandler(RecipeHandler):
    def process(self, srctree, classes, lines_before, lines_after, handled, extravalues):
        if 'buildsystem' in handled:
            return False

        if RecipeHandler.checkfiles(srctree, ['*.pro']):
            classes.append('qmake2')
            handled.append('buildsystem')
            return True
        return False

class AutotoolsRecipeHandler(RecipeHandler):
    def process(self, srctree, classes, lines_before, lines_after, handled, extravalues):
        if 'buildsystem' in handled:
            return False

        autoconf = False
        if RecipeHandler.checkfiles(srctree, ['configure.ac', 'configure.in']):
            autoconf = True
            values = AutotoolsRecipeHandler.extract_autotools_deps(lines_before, srctree, extravalues)
            classes.extend(values.pop('inherit', '').split())
            for var, value in values.iteritems():
                lines_before.append('%s = "%s"' % (var, value))
        else:
            conffile = RecipeHandler.checkfiles(srctree, ['configure'])
            if conffile:
                # Check if this is just a pre-generated autoconf configure script
                with open(conffile[0], 'r') as f:
                    for i in range(1, 10):
                        if 'Generated by GNU Autoconf' in f.readline():
                            autoconf = True
                            break

        if autoconf and not ('PV' in extravalues and 'PN' in extravalues):
            # Last resort
            conffile = RecipeHandler.checkfiles(srctree, ['configure'])
            if conffile:
                with open(conffile[0], 'r') as f:
                    for line in f:
                        line = line.strip()
                        if line.startswith('VERSION=') or line.startswith('PACKAGE_VERSION='):
                            pv = line.split('=')[1].strip('"\'')
                            if pv and not 'PV' in extravalues and validate_pv(pv):
                                extravalues['PV'] = pv
                        elif line.startswith('PACKAGE_NAME=') or line.startswith('PACKAGE='):
                            pn = line.split('=')[1].strip('"\'')
                            if pn and not 'PN' in extravalues:
                                extravalues['PN'] = pn

        if autoconf:
            lines_before.append('')
            lines_before.append('# NOTE: if this software is not capable of being built in a separate build directory')
            lines_before.append('# from the source, you should replace autotools with autotools-brokensep in the')
            lines_before.append('# inherit line')
            classes.append('autotools')
            lines_after.append('# Specify any options you want to pass to the configure script using EXTRA_OECONF:')
            lines_after.append('EXTRA_OECONF = ""')
            lines_after.append('')
            handled.append('buildsystem')
            return True

        return False

    @staticmethod
    def extract_autotools_deps(outlines, srctree, extravalues=None, acfile=None):
        import shlex

        values = {}
        inherits = []

        # FIXME this mapping is very thin
        progmap = {'flex': 'flex-native',
                'bison': 'bison-native',
                'm4': 'm4-native',
                'tar': 'tar-native',
                'ar': 'binutils-native'}
        progclassmap = {'gconftool-2': 'gconf',
                'pkg-config': 'pkgconfig'}

        pkg_re = re.compile('PKG_CHECK_MODULES\(\[?[a-zA-Z0-9_]*\]?, *\[?([^,\]]*)\]?[),].*')
        pkgce_re = re.compile('PKG_CHECK_EXISTS\(\[?([^,\]]*)\]?[),].*')
        lib_re = re.compile('AC_CHECK_LIB\(\[?([^,\]]*)\]?,.*')
        libx_re = re.compile('AX_CHECK_LIBRARY\(\[?[^,\]]*\]?, *\[?([^,\]]*)\]?, *\[?([a-zA-Z0-9-]*)\]?,.*')
        progs_re = re.compile('_PROGS?\(\[?[a-zA-Z0-9_]*\]?, \[?([^,\]]*)\]?[),].*')
        dep_re = re.compile('([^ ><=]+)( [<>=]+ [^ ><=]+)?')
        ac_init_re = re.compile('AC_INIT\(([^,]+), *([^,]+)[,)].*')
        am_init_re = re.compile('AM_INIT_AUTOMAKE\(([^,]+), *([^,]+)[,)].*')
        define_re = re.compile(' *(m4_)?define\(([^,]+), *([^,]+)\)')

        defines = {}
        def subst_defines(value):
            newvalue = value
            for define, defval in defines.iteritems():
                newvalue = newvalue.replace(define, defval)
            if newvalue != value:
                return subst_defines(newvalue)
            return value

        def process_value(value):
            value = value.replace('[', '').replace(']', '')
            if value.startswith('m4_esyscmd(') or value.startswith('m4_esyscmd_s('):
                cmd = subst_defines(value[value.index('(')+1:-1])
                try:
                    if '|' in cmd:
                        cmd = 'set -o pipefail; ' + cmd
                    stdout, _ = bb.process.run(cmd, cwd=srctree, shell=True)
                    ret = stdout.rstrip()
                except bb.process.ExecutionError as e:
                    ret = ''
            elif value.startswith('m4_'):
                return None
            ret = subst_defines(value)
            if ret:
                ret = ret.strip('"\'')
            return ret

        # Since a configure.ac file is essentially a program, this is only ever going to be
        # a hack unfortunately; but it ought to be enough of an approximation
        if acfile:
            srcfiles = [acfile]
        else:
            srcfiles = RecipeHandler.checkfiles(srctree, ['acinclude.m4', 'configure.ac', 'configure.in'])

        pcdeps = []
        libdeps = []
        deps = []
        unmapped = []

        def process_macro(keyword, value):
            if keyword == 'PKG_CHECK_MODULES':
                res = pkg_re.search(value)
                if res:
                    res = dep_re.findall(res.group(1))
                    if res:
                        pcdeps.extend([x[0] for x in res])
                inherits.append('pkgconfig')
            elif keyword == 'PKG_CHECK_EXISTS':
                res = pkgce_re.search(value)
                if res:
                    res = dep_re.findall(res.group(1))
                    if res:
                        pcdeps.extend([x[0] for x in res])
                inherits.append('pkgconfig')
            elif keyword in ('AM_GNU_GETTEXT', 'AM_GLIB_GNU_GETTEXT', 'GETTEXT_PACKAGE'):
                inherits.append('gettext')
            elif keyword in ('AC_PROG_INTLTOOL', 'IT_PROG_INTLTOOL'):
                deps.append('intltool-native')
            elif keyword == 'AM_PATH_GLIB_2_0':
                deps.append('glib-2.0')
            elif keyword in ('AC_CHECK_PROG', 'AC_PATH_PROG', 'AX_WITH_PROG'):
                res = progs_re.search(value)
                if res:
                    for prog in shlex.split(res.group(1)):
                        prog = prog.split()[0]
                        progclass = progclassmap.get(prog, None)
                        if progclass:
                            inherits.append(progclass)
                        else:
                            progdep = progmap.get(prog, None)
                            if progdep:
                                deps.append(progdep)
                            else:
                                if not prog.startswith('$'):
                                    unmapped.append(prog)
            elif keyword == 'AC_CHECK_LIB':
                res = lib_re.search(value)
                if res:
                    lib = res.group(1)
                    if not lib.startswith('$'):
                        libdeps.append(lib)
            elif keyword == 'AX_CHECK_LIBRARY':
                res = libx_re.search(value)
                if res:
                    lib = res.group(2)
                    if not lib.startswith('$'):
                        header = res.group(1)
                        libdeps.add((lib, header))
            elif keyword == 'AC_PATH_X':
                deps.append('libx11')
            elif keyword in ('AX_BOOST', 'BOOST_REQUIRE'):
                deps.append('boost')
            elif keyword in ('AC_PROG_LEX', 'AM_PROG_LEX', 'AX_PROG_FLEX'):
                deps.append('flex-native')
            elif keyword in ('AC_PROG_YACC', 'AX_PROG_BISON'):
                deps.append('bison-native')
            elif keyword == 'AX_CHECK_ZLIB':
                deps.append('zlib')
            elif keyword in ('AX_CHECK_OPENSSL', 'AX_LIB_CRYPTO'):
                deps.append('openssl')
            elif keyword == 'AX_LIB_CURL':
                deps.append('curl')
            elif keyword == 'AX_LIB_BEECRYPT':
                deps.append('beecrypt')
            elif keyword == 'AX_LIB_EXPAT':
                deps.append('expat')
            elif keyword == 'AX_LIB_GCRYPT':
                deps.append('libgcrypt')
            elif keyword == 'AX_LIB_NETTLE':
                deps.append('nettle')
            elif keyword == 'AX_LIB_READLINE':
                deps.append('readline')
            elif keyword == 'AX_LIB_SQLITE3':
                deps.append('sqlite3')
            elif keyword == 'AX_LIB_TAGLIB':
                deps.append('taglib')
            elif keyword == 'AX_PKG_SWIG':
                deps.append('swig')
            elif keyword == 'AX_PROG_XSLTPROC':
                deps.append('libxslt-native')
            elif keyword == 'AX_WITH_CURSES':
                deps.append('ncurses')
            elif keyword == 'AX_PATH_BDB':
                deps.append('db')
            elif keyword == 'AX_PATH_LIB_PCRE':
                deps.append('libpcre')
            elif keyword == 'AC_INIT':
                if extravalues is not None:
                    res = ac_init_re.match(value)
                    if res:
                        extravalues['PN'] = process_value(res.group(1))
                        pv = process_value(res.group(2))
                        if validate_pv(pv):
                            extravalues['PV'] = pv
            elif keyword == 'AM_INIT_AUTOMAKE':
                if extravalues is not None:
                    if 'PN' not in extravalues:
                        res = am_init_re.match(value)
                        if res:
                            if res.group(1) != 'AC_PACKAGE_NAME':
                                extravalues['PN'] = process_value(res.group(1))
                            pv = process_value(res.group(2))
                            if validate_pv(pv):
                                extravalues['PV'] = pv
            elif keyword == 'define(':
                res = define_re.match(value)
                if res:
                    key = res.group(2).strip('[]')
                    value = process_value(res.group(3))
                    if value is not None:
                        defines[key] = value

        keywords = ['PKG_CHECK_MODULES',
                    'PKG_CHECK_EXISTS',
                    'AM_GNU_GETTEXT',
                    'AM_GLIB_GNU_GETTEXT',
                    'GETTEXT_PACKAGE',
                    'AC_PROG_INTLTOOL',
                    'IT_PROG_INTLTOOL',
                    'AM_PATH_GLIB_2_0',
                    'AC_CHECK_PROG',
                    'AC_PATH_PROG',
                    'AX_WITH_PROG',
                    'AC_CHECK_LIB',
                    'AX_CHECK_LIBRARY',
                    'AC_PATH_X',
                    'AX_BOOST',
                    'BOOST_REQUIRE',
                    'AC_PROG_LEX',
                    'AM_PROG_LEX',
                    'AX_PROG_FLEX',
                    'AC_PROG_YACC',
                    'AX_PROG_BISON',
                    'AX_CHECK_ZLIB',
                    'AX_CHECK_OPENSSL',
                    'AX_LIB_CRYPTO',
                    'AX_LIB_CURL',
                    'AX_LIB_BEECRYPT',
                    'AX_LIB_EXPAT',
                    'AX_LIB_GCRYPT',
                    'AX_LIB_NETTLE',
                    'AX_LIB_READLINE'
                    'AX_LIB_SQLITE3',
                    'AX_LIB_TAGLIB',
                    'AX_PKG_SWIG',
                    'AX_PROG_XSLTPROC',
                    'AX_WITH_CURSES',
                    'AX_PATH_BDB',
                    'AX_PATH_LIB_PCRE',
                    'AC_INIT',
                    'AM_INIT_AUTOMAKE',
                    'define(',
                    ]
        for srcfile in srcfiles:
            nesting = 0
            in_keyword = ''
            partial = ''
            with open(srcfile, 'r') as f:
                for line in f:
                    if in_keyword:
                        partial += ' ' + line.strip()
                        if partial.endswith('\\'):
                            partial = partial[:-1]
                        nesting = nesting + line.count('(') - line.count(')')
                        if nesting == 0:
                            process_macro(in_keyword, partial)
                            partial = ''
                            in_keyword = ''
                    else:
                        for keyword in keywords:
                            if keyword in line:
                                nesting = line.count('(') - line.count(')')
                                if nesting > 0:
                                    partial = line.strip()
                                    if partial.endswith('\\'):
                                        partial = partial[:-1]
                                    in_keyword = keyword
                                else:
                                    process_macro(keyword, line.strip())
                                break

            if in_keyword:
                process_macro(in_keyword, partial)

        if extravalues:
            for k,v in extravalues.items():
                if v:
                    if v.startswith('$') or v.startswith('@') or v.startswith('%'):
                        del extravalues[k]
                    else:
                        extravalues[k] = v.strip('"\'').rstrip('()')

        if unmapped:
            outlines.append('# NOTE: the following prog dependencies are unknown, ignoring: %s' % ' '.join(list(set(unmapped))))

        RecipeHandler.handle_depends(libdeps, pcdeps, deps, outlines, values, tinfoil.config_data)

        if inherits:
            values['inherit'] = ' '.join(list(set(inherits)))

        return values


class MakefileRecipeHandler(RecipeHandler):
    def process(self, srctree, classes, lines_before, lines_after, handled, extravalues):
        if 'buildsystem' in handled:
            return False

        makefile = RecipeHandler.checkfiles(srctree, ['Makefile'])
        if makefile:
            lines_after.append('# NOTE: this is a Makefile-only piece of software, so we cannot generate much of the')
            lines_after.append('# recipe automatically - you will need to examine the Makefile yourself and ensure')
            lines_after.append('# that the appropriate arguments are passed in.')
            lines_after.append('')

            scanfile = os.path.join(srctree, 'configure.scan')
            skipscan = False
            try:
                stdout, stderr = bb.process.run('autoscan', cwd=srctree, shell=True)
            except bb.process.ExecutionError as e:
                skipscan = True
            if scanfile and os.path.exists(scanfile):
                values = AutotoolsRecipeHandler.extract_autotools_deps(lines_before, srctree, acfile=scanfile)
                classes.extend(values.pop('inherit', '').split())
                for var, value in values.iteritems():
                    if var == 'DEPENDS':
                        lines_before.append('# NOTE: some of these dependencies may be optional, check the Makefile and/or upstream documentation')
                    lines_before.append('%s = "%s"' % (var, value))
                lines_before.append('')
                for f in ['configure.scan', 'autoscan.log']:
                    fp = os.path.join(srctree, f)
                    if os.path.exists(fp):
                        os.remove(fp)

            self.genfunction(lines_after, 'do_configure', ['# Specify any needed configure commands here'])

            func = []
            func.append('# You will almost certainly need to add additional arguments here')
            func.append('oe_runmake')
            self.genfunction(lines_after, 'do_compile', func)

            installtarget = True
            try:
                stdout, stderr = bb.process.run('make -n install', cwd=srctree, shell=True)
            except bb.process.ExecutionError as e:
                if e.exitcode != 1:
                    installtarget = False
            func = []
            if installtarget:
                func.append('# This is a guess; additional arguments may be required')
                makeargs = ''
                with open(makefile[0], 'r') as f:
                    for i in range(1, 100):
                        if 'DESTDIR' in f.readline():
                            makeargs += " 'DESTDIR=${D}'"
                            break
                func.append('oe_runmake install%s' % makeargs)
            else:
                func.append('# NOTE: unable to determine what to put here - there is a Makefile but no')
                func.append('# target named "install", so you will need to define this yourself')
            self.genfunction(lines_after, 'do_install', func)

            handled.append('buildsystem')
        else:
            lines_after.append('# NOTE: no Makefile found, unable to determine what needs to be done')
            lines_after.append('')
            self.genfunction(lines_after, 'do_configure', ['# Specify any needed configure commands here'])
            self.genfunction(lines_after, 'do_compile', ['# Specify compilation commands here'])
            self.genfunction(lines_after, 'do_install', ['# Specify install commands here'])


class VersionFileRecipeHandler(RecipeHandler):
    def process(self, srctree, classes, lines_before, lines_after, handled, extravalues):
        if 'PV' not in extravalues:
            # Look for a VERSION or version file containing a single line consisting
            # only of a version number
            filelist = RecipeHandler.checkfiles(srctree, ['VERSION', 'version'])
            version = None
            for fileitem in filelist:
                linecount = 0
                with open(fileitem, 'r') as f:
                    for line in f:
                        line = line.rstrip().strip('"\'')
                        linecount += 1
                        if line:
                            if linecount > 1:
                                version = None
                                break
                            else:
                                if validate_pv(line):
                                    version = line
                if version:
                    extravalues['PV'] = version
                    break


class SpecFileRecipeHandler(RecipeHandler):
    def process(self, srctree, classes, lines_before, lines_after, handled, extravalues):
        if 'PV' in extravalues and 'PN' in extravalues:
            return
        filelist = RecipeHandler.checkfiles(srctree, ['*.spec'], recursive=True)
        pn = None
        pv = None
        for fileitem in filelist:
            linecount = 0
            with open(fileitem, 'r') as f:
                for line in f:
                    if line.startswith('Name:') and not pn:
                        pn = line.split(':')[1].strip()
                    if line.startswith('Version:') and not pv:
                        pv = line.split(':')[1].strip()
            if pv or pn:
                if pv and not 'PV' in extravalues and validate_pv(pv):
                    extravalues['PV'] = pv
                if pn and not 'PN' in extravalues:
                    extravalues['PN'] = pn
                break

def register_recipe_handlers(handlers):
    # These are in a specific order so that the right one is detected first
    handlers.append(CmakeRecipeHandler())
    handlers.append(AutotoolsRecipeHandler())
    handlers.append(SconsRecipeHandler())
    handlers.append(QmakeRecipeHandler())
    handlers.append(MakefileRecipeHandler())
    handlers.append((VersionFileRecipeHandler(), -1))
    handlers.append((SpecFileRecipeHandler(), -1))
