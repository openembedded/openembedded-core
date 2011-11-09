#!/usr/bin/env python
# Available modulesets:
#
# bootstrap.modules
# freedesktop.modules
# gcj.modules
# gnome-2.10.modules
# gnome-2.12.modules
# gnome-2.14.modules
# gnome-2.16.modules
# gnutls.modules
# gtk28.modules
# gtk.modules
# xorg-7.0.modules
# xorg.modules

moduleset = 'xorg.modules'



import cElementTree as ElementTree
# import lxml.etree as ElementTree
import re, os, bb, bb.data

class Handlers(object):
    """
    Class to act as a store for handlers of jhbuild xml elements, and as a
    dispatcher of parsed Elements to those handlers.

    These handlers exist to take an xml element from the jhbuild files and
    either produce bitbake metadata in self.packages, or produce data which
    will be used by other element handlers to do so.

    Handlers(filename) -> new object to parse and process jhbuild file of
                          name 'filename'.
    """

    cvsrootpat = re.compile(r'''
        \s*                 # Skip leading whitespace
        :(?P<scheme>[^:]+): # scheme (i.e. pserver, ext)
        ((?P<user>\S+?)@)?  # username
        (?P<host>\S+?):     # non-greedy match of the remote host
        (?P<path>\S+)       # remote path
    ''', re.VERBOSE)


    def __init__(self, msfile):
        self.msfile = msfile
        self.msbasename = os.path.basename(msfile)
        self.msdirname = os.path.dirname(msfile)

        self.handled = {}

        self.cvsroots = {}
        self.repositories = {}
        self.packages = []

    def handle(self, element, parent):
        import sys
        """
        XML Element dispatch function.  Can be called both from outside the
        Handlers object to initiate handling, and from within individual XML
        element handlers to ensure that dependent elements have been handled.

        Does not handle a given XML Element more than once, as it retains
        information about the handling state of the Elements it encounters.
        """

        try:
            state = self.handled[element]
        except KeyError:
            pass
        except:
            return

        try:
            self.__class__.__dict__[element.tag](self, element, parent)
            self.handled[element] = True
        except KeyError:
            self.handled[element] = False
            sys.__stderr__.write('Unhandled element: %s\n' % element.tag)
        except Exception:
            sys.__stderr__.write('Error handling %s: %s:\n    %s\n' % (element.tag, sys.exc_type, sys.exc_value))
            self.handled[element] = False

        print('handle(%s, %s) -> %s' % (element, parent, self.handled[element]))
        return self.handled[element]

    def cvsroot(self, element, parent):
        # Rip apart the cvsroot style location to build a cvs:// url for
        # bitbake's usage in the cvsmodule handler.
        # root=":pserver:anoncvs@cvs.freedesktop.org:/cvs/fontconfig"
        print("cvsroot(%s, %s)" % (element, parent))

        root = element.attrib.get('root')
        rootmatch = re.match(Handlers.cvsrootpat, root)
        name = element.attrib.get('name')
        user = rootmatch.group('user') or ''
        if user != '':
            pw = element.attrib.get('password') or ''
            if pw != '':
                pw = ':' + pw + '@'
            else:
                user = user + '@'
        print('user: %s' % user)
        print('pw: %s' % pw)

        host = rootmatch.group('host')
        print('host: %s' % host)
        path = rootmatch.group('path') or '/'
        print('path: %s' % path)

        root = "cvs://%s%s%s%s" % (user, pw, host, path)
        print('root: %s' % root)
        self.cvsroots[name] = root

    def cvsmodule(self, element, parent):
        rootlist = [root for root in list(parent) if root.attrib.get('name') == element.attrib.get('cvsroot')]
        if len(rootlist) < 1:
            raise Exception("Error: cvsmodule '%s' requires cvsroot '%s'." % (element.attrib.get('module'), element.attrib.get('cvsroot')))

        cvsroot = rootlist[0]


    def include(self, element, parent):
        href = element.attrib.get('href')
        fullhref = os.path.join(self.msdirname, href)
        tree = ElementTree.ElementTree(file=fullhref)
        elem = tree.getroot()

        # Append the children of the newly included root element to the parent
        # element, and manually handle() them, as the currently running
        # iteration isn't going to hit them.
        for child in elem:
            self.handle(child, elem)
            parent.append(elem)

    def repository(self, element, parent):
        # TODO:
        # Convert the URL in the href attribute, if necessary, to the format
        # which bitbake expects to see in SRC_URI.
        name = element.attrib.get('name')
        self.repositories[name] = element.attrib.get('href')


    def moduleset(self, element, parent):
        for child in element:
            self.handle(child, element)

    def packagename(self, name):
        # mangle name into an appropriate bitbake package name
        return name.replace('/', '-') 

    def metamodule(self, element, parent):
        # grab the deps
        deps = None
        for child in element:
            if child.tag == 'dependencies':
                deps = [self.packagename(dep.attrib.get('package')) for dep in child if dep.tag == "dep"]

        # create the package
        d = bb.data.init()
        pn = self.packagename(element.attrib.get('id'))
        d.setVar('PN', pn)
        bb.data.setVar('DEPENDS', ' '.join(deps), d)
        d.setVar('_handler', 'metamodule')
        self.packages.append(d)

    def autotools(self, element, parent):
        deps = None
        branch = None
        for child in element:
            if child.tag == 'dependencies':
                deps = [self.packagename(dep.attrib.get('package')) for dep in child if dep.tag == "dep"]
            elif child.tag == 'branch':
                branch = child

        # create the package
        d = bb.data.init()
        id = element.attrib.get('id')
        if id is None:
            raise Exception('Error: autotools element has no id attribute.')
        pn = self.packagename(id)
        d.setVar('PN', pn)
        if deps is not None:
            bb.data.setVar('DEPENDS', ' '.join(deps), d)

        if branch is not None:
            # <branch repo="git.freedesktop.org" module="xorg/xserver"/>
            repo = os.path.join(self.repositories[branch.attrib.get('repo')], branch.attrib.get('module'))
            d.setVar('SRC_URI', repo)

            checkoutdir = branch.attrib.get('checkoutdir')
            if checkoutdir is not None:
                bb.data.setVar('S', os.path.join('${WORKDIR}', checkoutdir), d)

        # build class
        d.setVar('INHERITS', 'autotools')
        d.setVarFlag('INHERITS', 'operator', '+=')
        d.setVar('_handler', 'autotools')
        self.packages.append(d)

class Emitter(object):
    """
    Class which contains a single method for the emission of a bitbake
    package from the bitbake data produced by a Handlers object.
    """

    def __init__(self, filefunc = None, basedir = None):
        def _defaultfilefunc(package):
            # return a relative path to the bitbake .bb which will be written
            return package.getVar('PN', 1) + '.bb'

        self.filefunc = filefunc or _defaultfilefunc
        self.basedir = basedir or os.path.abspath(os.curdir)

    def write(self, package, template = None):
        # 1) Assemble new file contents in ram, either new from bitbake
        #    metadata, or a combination of the template and that metadata.
        # 2) Open the path returned by the filefunc + the basedir for writing.
        # 3) Write the new bitbake data file.
        fdata = ''
        if template:
            f = file(template, 'r')
            fdata = f.read()
            f.close()

            for key in bb.data.keys(package):
                fdata = fdata.replace('@@'+key+'@@', package.getVar(key))
        else:
            for key in bb.data.keys(package):
                if key == '_handler':
                    continue
                elif key == 'INHERITS':
                    fdata += 'inherit %s\n' % package.getVar('INHERITS')
                else:
                    oper = package.getVarFlag(key, 'operator') or '='
                    fdata += '%s %s "%s"\n' % (key, oper, package.getVar(key))

        if not os.path.exists(os.path.join(self.basedir, os.path.dirname(self.filefunc(package)))):
            os.makedirs(os.path.join(self.basedir, os.path.dirname(self.filefunc(package))))

        out = file(os.path.join(self.basedir, self.filefunc(package)), 'w')
        out.write(fdata)
        out.close()

def _test():
    msfile = os.path.join(os.path.abspath(os.curdir), 'modulesets', moduleset)
    tree = ElementTree.ElementTree(file=msfile)
    elem = tree.getroot()

    handlers = Handlers(msfile)
    handlers.handle(elem, None)

    def filefunc(package):
        # return a relative path to the bitbake .bb which will be written
        src_uri = package.getVar('SRC_URI', 1)
        filename = package.getVar('PN', 1) + '.bb'
        if not src_uri:
            return filename
        else:
            substr = src_uri[src_uri.find('xorg/'):]
            subdirlist = substr.split('/')[:2]
            subdir = '-'.join(subdirlist)
            return os.path.join(subdir, filename)

    emitter = Emitter(filefunc)
    for package in handlers.packages:
        template = emitter.filefunc(package) + '.in'
        if os.path.exists(template):
            print("%s exists, emitting based on template" % template)
            emitter.write(package, template)
        else:
            print("%s does not exist, emitting non-templated" % template)
            emitter.write(package)

if __name__ == "__main__":
    _test()
