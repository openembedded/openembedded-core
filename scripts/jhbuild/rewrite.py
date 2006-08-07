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
        dependlist = [child for child in element if child.tag == "dependencies"]
        deps = [self.packagename(dep.attrib.get('package')) for dep in dependlist[0] if child.tag == "dep"]

        # create the package
        d = bb.data.init()
        pn = self.packagename(element.attrib.get('id'))
        bb.data.setVar('PN', pn, d)
        bb.data.setVar('DEPENDS', ' '.join(deps), d)
        bb.data.setVar('_handler', 'metamodule', d)
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
        bb.data.setVar('PN', pn, d)
        if deps is not None:
            bb.data.setVar('DEPENDS', ' '.join(deps), d)

        if branch is not None:
            # <branch repo="git.freedesktop.org" module="xorg/xserver"/>
            repo = os.path.join(self.repositories[branch.attrib.get('repo')], branch.attrib.get('module'))
            bb.data.setVar('SRC_URI', repo, d)

            checkoutdir = branch.attrib.get('checkoutdir')
            if checkoutdir is not None:
                bb.data.setVar('S', os.path.join('${WORKDIR}', checkoutdir), d)

        # build class
        bb.data.setVar('INCLUDES', 'autotools', d)
        bb.data.setVarFlag('INCLUDES', 'operator', '+=', d)
        bb.data.setVar('_handler', 'autotools', d)
        self.packages.append(d)

class Emitter(object):
    """
    Class to take a Handlers object after processing and emit the
    bitbake files from the metadata.  It supports either emitting
    the data as is, using templates based on package name, and using
    templates based on the name of handler / xml element associated
    with the package itself.
    """

def _test():
    msfile = os.path.join(os.path.abspath(os.curdir), 'modulesets', moduleset)
    tree = ElementTree.ElementTree(file=msfile)
    elem = tree.getroot()

    handlers = Handlers(msfile)
    handlers.handle(elem, None)

    for package in handlers.packages:
        print(bb.data.getVar('PN', package))

if __name__ == "__main__":
    _test()
