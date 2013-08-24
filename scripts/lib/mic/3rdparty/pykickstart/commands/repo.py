#
# Chris Lumens <clumens@redhat.com>
#
# Copyright 2007, 2008, 2009 Red Hat, Inc.
#
# This copyrighted material is made available to anyone wishing to use, modify,
# copy, or redistribute it subject to the terms and conditions of the GNU
# General Public License v.2.  This program is distributed in the hope that it
# will be useful, but WITHOUT ANY WARRANTY expressed or implied, including the
# implied warranties of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
# See the GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License along with
# this program; if not, write to the Free Software Foundation, Inc., 51
# Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.  Any Red Hat
# trademarks that are incorporated in the source code or documentation are not
# subject to the GNU General Public License and may only be used or replicated
# with the express permission of Red Hat, Inc. 
#
from pykickstart.base import *
from pykickstart.constants import *
from pykickstart.errors import *
from pykickstart.options import *

import gettext
import warnings
_ = lambda x: gettext.ldgettext("pykickstart", x)

class FC6_RepoData(BaseData):
    removedKeywords = BaseData.removedKeywords
    removedAttrs = BaseData.removedAttrs

    def __init__(self, *args, **kwargs):
        BaseData.__init__(self, *args, **kwargs)
        self.baseurl = kwargs.get("baseurl", "")
        self.mirrorlist = kwargs.get("mirrorlist", None)
        self.name = kwargs.get("name", "")

    def __eq__(self, y):
        return self.name == y.name

    def _getArgsAsStr(self):
        retval = ""

        if self.baseurl:
            retval += "--baseurl=%s" % self.baseurl
        elif self.mirrorlist:
            retval += "--mirrorlist=%s" % self.mirrorlist

        return retval

    def __str__(self):
        retval = BaseData.__str__(self)
        retval += "repo --name=\"%s\" %s\n" % (self.name, self._getArgsAsStr())
        return retval

class F8_RepoData(FC6_RepoData):
    removedKeywords = FC6_RepoData.removedKeywords
    removedAttrs = FC6_RepoData.removedAttrs

    def __init__(self, *args, **kwargs):
        FC6_RepoData.__init__(self, *args, **kwargs)
        self.cost = kwargs.get("cost", None)
        self.includepkgs = kwargs.get("includepkgs", [])
        self.excludepkgs = kwargs.get("excludepkgs", [])

    def _getArgsAsStr(self):
        retval = FC6_RepoData._getArgsAsStr(self)

        if self.cost:
            retval += " --cost=%s" % self.cost
        if self.includepkgs:
            retval += " --includepkgs=\"%s\"" % ",".join(self.includepkgs)
        if self.excludepkgs:
            retval += " --excludepkgs=\"%s\"" % ",".join(self.excludepkgs)

        return retval

class F11_RepoData(F8_RepoData):
    removedKeywords = F8_RepoData.removedKeywords
    removedAttrs = F8_RepoData.removedAttrs

    def __init__(self, *args, **kwargs):
        F8_RepoData.__init__(self, *args, **kwargs)
        self.ignoregroups = kwargs.get("ignoregroups", None)

    def _getArgsAsStr(self):
        retval = F8_RepoData._getArgsAsStr(self)

        if self.ignoregroups:
            retval += " --ignoregroups=true"
        return retval

class F13_RepoData(F11_RepoData):
    removedKeywords = F11_RepoData.removedKeywords
    removedAttrs = F11_RepoData.removedAttrs

    def __init__(self, *args, **kwargs):
        F11_RepoData.__init__(self, *args, **kwargs)
        self.proxy = kwargs.get("proxy", "")

    def _getArgsAsStr(self):
        retval = F11_RepoData._getArgsAsStr(self)

        if self.proxy:
            retval += " --proxy=\"%s\"" % self.proxy

        return retval

class F14_RepoData(F13_RepoData):
    removedKeywords = F13_RepoData.removedKeywords
    removedAttrs = F13_RepoData.removedAttrs

    def __init__(self, *args, **kwargs):
        F13_RepoData.__init__(self, *args, **kwargs)
        self.noverifyssl = kwargs.get("noverifyssl", False)

    def _getArgsAsStr(self):
        retval = F13_RepoData._getArgsAsStr(self)

        if self.noverifyssl:
            retval += " --noverifyssl"

        return retval

RHEL6_RepoData = F14_RepoData

F15_RepoData = F14_RepoData

class FC6_Repo(KickstartCommand):
    removedKeywords = KickstartCommand.removedKeywords
    removedAttrs = KickstartCommand.removedAttrs

    urlRequired = True

    def __init__(self, writePriority=0, *args, **kwargs):
        KickstartCommand.__init__(self, writePriority, *args, **kwargs)
        self.op = self._getParser()

        self.repoList = kwargs.get("repoList", [])

    def __str__(self):
        retval = ""
        for repo in self.repoList:
            retval += repo.__str__()

        return retval

    def _getParser(self):
        op = KSOptionParser()
        op.add_option("--name", dest="name", required=1)
        op.add_option("--baseurl")
        op.add_option("--mirrorlist")
        return op

    def parse(self, args):
        (opts, extra) = self.op.parse_args(args=args, lineno=self.lineno)
        
        if len(extra) != 0:
            mapping = {"command": "repo", "options": extra}
            raise KickstartValueError, formatErrorMsg(self.lineno, msg=_("Unexpected arguments to %(command)s command: %(options)s") % mapping)

        # This is lame, but I can't think of a better way to make sure only
        # one of these two is specified.
        if opts.baseurl and opts.mirrorlist:
            raise KickstartValueError, formatErrorMsg(self.lineno, msg=_("Only one of --baseurl and --mirrorlist may be specified for repo command."))

        if self.urlRequired and not opts.baseurl and not opts.mirrorlist:
            raise KickstartValueError, formatErrorMsg(self.lineno, msg=_("One of --baseurl or --mirrorlist must be specified for repo command."))

        rd = self.handler.RepoData()
        self._setToObj(self.op, opts, rd)
        rd.lineno = self.lineno

        # Check for duplicates in the data list.
        if rd in self.dataList():
            warnings.warn(_("A repo with the name %s has already been defined.") % rd.name)

        return rd

    def dataList(self):
        return self.repoList

class F8_Repo(FC6_Repo):
    removedKeywords = FC6_Repo.removedKeywords
    removedAttrs = FC6_Repo.removedAttrs

    def __str__(self):
        retval = ""
        for repo in self.repoList:
            retval += repo.__str__()

        return retval

    def _getParser(self):
        def list_cb (option, opt_str, value, parser):
            for d in value.split(','):
                parser.values.ensure_value(option.dest, []).append(d)

        op = FC6_Repo._getParser(self)
        op.add_option("--cost", action="store", type="int")
        op.add_option("--excludepkgs", action="callback", callback=list_cb,
                      nargs=1, type="string")
        op.add_option("--includepkgs", action="callback", callback=list_cb,
                      nargs=1, type="string")
        return op

    def methodToRepo(self):
        if not self.handler.method.url:
            raise KickstartError, formatErrorMsg(self.handler.method.lineno, msg=_("Method must be a url to be added to the repo list."))
        reponame = "ks-method-url"
        repourl = self.handler.method.url
        rd = self.handler.RepoData(name=reponame, baseurl=repourl)
        return rd

class F11_Repo(F8_Repo):
    removedKeywords = F8_Repo.removedKeywords
    removedAttrs = F8_Repo.removedAttrs

    def _getParser(self):
        op = F8_Repo._getParser(self)
        op.add_option("--ignoregroups", action="store", type="ksboolean")
        return op

class F13_Repo(F11_Repo):
    removedKeywords = F11_Repo.removedKeywords
    removedAttrs = F11_Repo.removedAttrs

    def _getParser(self):
        op = F11_Repo._getParser(self)
        op.add_option("--proxy")
        return op

class F14_Repo(F13_Repo):
    removedKeywords = F13_Repo.removedKeywords
    removedAttrs = F13_Repo.removedAttrs

    def _getParser(self):
        op = F13_Repo._getParser(self)
        op.add_option("--noverifyssl", action="store_true", default=False)
        return op

RHEL6_Repo = F14_Repo

class F15_Repo(F14_Repo):
    removedKeywords = F14_Repo.removedKeywords
    removedAttrs = F14_Repo.removedAttrs

    urlRequired = False
