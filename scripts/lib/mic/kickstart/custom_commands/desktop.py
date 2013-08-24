#!/usr/bin/python -tt
#
# Copyright (c) 2008, 2009, 2010 Intel, Inc.
#
# Yi Yang <yi.y.yang@intel.com>
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

from pykickstart.base import *
from pykickstart.errors import *
from pykickstart.options import *

class Mic_Desktop(KickstartCommand):
    def __init__(self, writePriority=0,
                       defaultdesktop=None,
                       defaultdm=None,
                       autologinuser=None,
                       session=None):

        KickstartCommand.__init__(self, writePriority)

        self.__new_version = False
        self.op = self._getParser()

        self.defaultdesktop = defaultdesktop
        self.autologinuser = autologinuser
        self.defaultdm = defaultdm
        self.session = session

    def __str__(self):
        retval = ""

        if self.defaultdesktop != None:
            retval += " --defaultdesktop=%s" % self.defaultdesktop
        if self.session != None:
            retval += " --session=\"%s\"" % self.session
        if self.autologinuser != None:
            retval += " --autologinuser=%s" % self.autologinuser
        if self.defaultdm != None:
            retval += " --defaultdm=%s" % self.defaultdm

        if retval != "":
            retval = "# Default Desktop Settings\ndesktop %s\n" % retval

        return retval

    def _getParser(self):
        try:
            op = KSOptionParser(lineno=self.lineno)
        except TypeError:
            # the latest version has not lineno argument
            op = KSOptionParser()
            self.__new_version = True

        op.add_option("--defaultdesktop", dest="defaultdesktop",
                                          action="store",
                                          type="string",
                                          nargs=1)
        op.add_option("--autologinuser", dest="autologinuser",
                                         action="store",
                                         type="string",
                                         nargs=1)
        op.add_option("--defaultdm", dest="defaultdm",
                                     action="store",
                                     type="string",
                                     nargs=1)
        op.add_option("--session", dest="session",
                                   action="store",
                                   type="string",
                                   nargs=1)
        return op

    def parse(self, args):
        if self.__new_version:
            (opts, extra) = self.op.parse_args(args=args, lineno=self.lineno)
        else:
            (opts, extra) = self.op.parse_args(args=args)

        if extra:
            m = _("Unexpected arguments to %(command)s command: %(options)s") \
                  % {"command": "desktop", "options": extra}
            raise KickstartValueError, formatErrorMsg(self.lineno, msg=m)

        self._setToSelf(self.op, opts)
