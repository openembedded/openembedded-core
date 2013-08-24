#!/usr/bin/python -tt
#
# Copyright (c) 2008, 2009, 2010, 2011 Intel, Inc.
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
import re
import rpm

from mic import msger
from mic.utils.errors import CreatorError
from mic.utils.proxy import get_proxy_for
from mic.utils import runner


class RPMInstallCallback:
    """ Command line callback class for callbacks from the RPM library.
    """

    def __init__(self, ts, output=1):
        self.output = output
        self.callbackfilehandles = {}
        self.total_actions = 0
        self.total_installed = 0
        self.installed_pkg_names = []
        self.total_removed = 0
        self.mark = "+"
        self.marks = 40
        self.lastmsg = None
        self.tsInfo = None # this needs to be set for anything else to work
        self.ts = ts
        self.filelog = False
        self.logString = []
        self.headmsg = "Installing"

    def _dopkgtup(self, hdr):
        tmpepoch = hdr['epoch']
        if tmpepoch is None: epoch = '0'
        else: epoch = str(tmpepoch)

        return (hdr['name'], hdr['arch'], epoch, hdr['version'], hdr['release'])

    def _makeHandle(self, hdr):
        handle = '%s:%s.%s-%s-%s' % (hdr['epoch'], hdr['name'], hdr['version'],
          hdr['release'], hdr['arch'])

        return handle

    def _localprint(self, msg):
        if self.output:
            msger.info(msg)

    def _makefmt(self, percent, progress = True):
        l = len(str(self.total_actions))
        size = "%s.%s" % (l, l)
        fmt_done = "[%" + size + "s/%" + size + "s]"
        done = fmt_done % (self.total_installed + self.total_removed,
                           self.total_actions)
        marks = self.marks - (2 * l)
        width = "%s.%s" % (marks, marks)
        fmt_bar = "%-" + width + "s"
        if progress:
            bar = fmt_bar % (self.mark * int(marks * (percent / 100.0)), )
            fmt = "\r  %-10.10s: %-20.20s " + bar + " " + done
        else:
            bar = fmt_bar % (self.mark * marks, )
            fmt = "  %-10.10s: %-20.20s "  + bar + " " + done
        return fmt

    def _logPkgString(self, hdr):
        """return nice representation of the package for the log"""
        (n,a,e,v,r) = self._dopkgtup(hdr)
        if e == '0':
            pkg = '%s.%s %s-%s' % (n, a, v, r)
        else:
            pkg = '%s.%s %s:%s-%s' % (n, a, e, v, r)

        return pkg

    def callback(self, what, bytes, total, h, user):
        if what == rpm.RPMCALLBACK_TRANS_START:
            if bytes == 6:
                self.total_actions = total

        elif what == rpm.RPMCALLBACK_TRANS_PROGRESS:
            pass

        elif what == rpm.RPMCALLBACK_TRANS_STOP:
            pass

        elif what == rpm.RPMCALLBACK_INST_OPEN_FILE:
            self.lastmsg = None
            hdr = None
            if h is not None:
                try:
                    hdr, rpmloc = h
                except:
                    rpmloc = h
                    hdr = readRpmHeader(self.ts, h)

                handle = self._makeHandle(hdr)
                fd = os.open(rpmloc, os.O_RDONLY)
                self.callbackfilehandles[handle]=fd
                if hdr['name'] not in self.installed_pkg_names:
                    self.installed_pkg_names.append(hdr['name'])
                    self.total_installed += 1
                return fd
            else:
                self._localprint("No header - huh?")

        elif what == rpm.RPMCALLBACK_INST_CLOSE_FILE:
            hdr = None
            if h is not None:
                try:
                    hdr, rpmloc = h
                except:
                    rpmloc = h
                    hdr = readRpmHeader(self.ts, h)

                handle = self._makeHandle(hdr)
                os.close(self.callbackfilehandles[handle])
                fd = 0

                # log stuff
                #pkgtup = self._dopkgtup(hdr)
                self.logString.append(self._logPkgString(hdr))

        elif what == rpm.RPMCALLBACK_INST_PROGRESS:
            if h is not None:
                percent = (self.total_installed*100L)/self.total_actions
                if total > 0:
                    try:
                        hdr, rpmloc = h
                    except:
                        rpmloc = h

                    m = re.match("(.*)-(\d+.*)-(\d+\.\d+)\.(.+)\.rpm", os.path.basename(rpmloc))
                    if m:
                        pkgname = m.group(1)
                    else:
                        pkgname = os.path.basename(rpmloc)
                if self.output:
                    fmt = self._makefmt(percent)
                    msg = fmt % (self.headmsg, pkgname)
                    if msg != self.lastmsg:
                        self.lastmsg = msg

                        msger.info(msg)

                        if self.total_installed == self.total_actions:
                            msger.raw('')
                            msger.verbose('\n'.join(self.logString))

        elif what == rpm.RPMCALLBACK_UNINST_START:
            pass

        elif what == rpm.RPMCALLBACK_UNINST_PROGRESS:
            pass

        elif what == rpm.RPMCALLBACK_UNINST_STOP:
            self.total_removed += 1

        elif what == rpm.RPMCALLBACK_REPACKAGE_START:
            pass

        elif what == rpm.RPMCALLBACK_REPACKAGE_STOP:
            pass

        elif what == rpm.RPMCALLBACK_REPACKAGE_PROGRESS:
            pass

def readRpmHeader(ts, filename):
    """ Read an rpm header. """

    fd = os.open(filename, os.O_RDONLY)
    h = ts.hdrFromFdno(fd)
    os.close(fd)
    return h

def splitFilename(filename):
    """ Pass in a standard style rpm fullname

        Return a name, version, release, epoch, arch, e.g.::
            foo-1.0-1.i386.rpm returns foo, 1.0, 1, i386
            1:bar-9-123a.ia64.rpm returns bar, 9, 123a, 1, ia64
    """

    if filename[-4:] == '.rpm':
        filename = filename[:-4]

    archIndex = filename.rfind('.')
    arch = filename[archIndex+1:]

    relIndex = filename[:archIndex].rfind('-')
    rel = filename[relIndex+1:archIndex]

    verIndex = filename[:relIndex].rfind('-')
    ver = filename[verIndex+1:relIndex]

    epochIndex = filename.find(':')
    if epochIndex == -1:
        epoch = ''
    else:
        epoch = filename[:epochIndex]

    name = filename[epochIndex + 1:verIndex]
    return name, ver, rel, epoch, arch

def getCanonX86Arch(arch):
    #
    if arch == "i586":
        f = open("/proc/cpuinfo", "r")
        lines = f.readlines()
        f.close()
        for line in lines:
            if line.startswith("model name") and line.find("Geode(TM)") != -1:
                return "geode"
        return arch
    # only athlon vs i686 isn't handled with uname currently
    if arch != "i686":
        return arch

    # if we're i686 and AuthenticAMD, then we should be an athlon
    f = open("/proc/cpuinfo", "r")
    lines = f.readlines()
    f.close()
    for line in lines:
        if line.startswith("vendor") and line.find("AuthenticAMD") != -1:
            return "athlon"
        # i686 doesn't guarantee cmov, but we depend on it
        elif line.startswith("flags") and line.find("cmov") == -1:
            return "i586"

    return arch

def getCanonX86_64Arch(arch):
    if arch != "x86_64":
        return arch

    vendor = None
    f = open("/proc/cpuinfo", "r")
    lines = f.readlines()
    f.close()
    for line in lines:
        if line.startswith("vendor_id"):
            vendor = line.split(':')[1]
            break
    if vendor is None:
        return arch

    if vendor.find("Authentic AMD") != -1 or vendor.find("AuthenticAMD") != -1:
        return "amd64"
    if vendor.find("GenuineIntel") != -1:
        return "ia32e"
    return arch

def getCanonArch():
    arch = os.uname()[4]

    if (len(arch) == 4 and arch[0] == "i" and arch[2:4] == "86"):
        return getCanonX86Arch(arch)

    if arch == "x86_64":
        return getCanonX86_64Arch(arch)

    return arch

# Copy from libsatsolver:poolarch.c, with cleanup
archPolicies = {
    "x86_64":       "x86_64:i686:i586:i486:i386",
    "i686":         "i686:i586:i486:i386",
    "i586":         "i586:i486:i386",
    "ia64":         "ia64:i686:i586:i486:i386",
    "armv7tnhl":    "armv7tnhl:armv7thl:armv7nhl:armv7hl",
    "armv7thl":     "armv7thl:armv7hl",
    "armv7nhl":     "armv7nhl:armv7hl",
    "armv7hl":      "armv7hl",
    "armv7l":       "armv7l:armv6l:armv5tejl:armv5tel:armv5l:armv4tl:armv4l:armv3l",
    "armv6l":       "armv6l:armv5tejl:armv5tel:armv5l:armv4tl:armv4l:armv3l",
    "armv5tejl":    "armv5tejl:armv5tel:armv5l:armv4tl:armv4l:armv3l",
    "armv5tel":     "armv5tel:armv5l:armv4tl:armv4l:armv3l",
    "armv5l":       "armv5l:armv4tl:armv4l:armv3l",
}

# dict mapping arch -> ( multicompat, best personality, biarch personality )
multilibArches = {
    "x86_64":  ( "athlon", "x86_64", "athlon" ),
}

# from yumUtils.py
arches = {
    # ia32
    "athlon": "i686",
    "i686": "i586",
    "geode": "i586",
    "i586": "i486",
    "i486": "i386",
    "i386": "noarch",

    # amd64
    "x86_64": "athlon",
    "amd64": "x86_64",
    "ia32e": "x86_64",

    # arm
    "armv7tnhl": "armv7nhl",
    "armv7nhl": "armv7hl",
    "armv7hl": "noarch",
    "armv7l": "armv6l",
    "armv6l": "armv5tejl",
    "armv5tejl": "armv5tel",
    "armv5tel": "noarch",

    #itanium
    "ia64": "noarch",
}

def isMultiLibArch(arch=None):
    """returns true if arch is a multilib arch, false if not"""
    if arch is None:
        arch = getCanonArch()

    if not arches.has_key(arch): # or we could check if it is noarch
        return False

    if multilibArches.has_key(arch):
        return True

    if multilibArches.has_key(arches[arch]):
        return True

    return False

def getBaseArch():
    myarch = getCanonArch()
    if not arches.has_key(myarch):
        return myarch

    if isMultiLibArch(arch=myarch):
        if multilibArches.has_key(myarch):
            return myarch
        else:
            return arches[myarch]

    if arches.has_key(myarch):
        basearch = myarch
        value = arches[basearch]
        while value != 'noarch':
            basearch = value
            value = arches[basearch]

        return basearch

def checkRpmIntegrity(bin_rpm, package):
    return runner.quiet([bin_rpm, "-K", "--nosignature", package])

def checkSig(ts, package):
    """ Takes a transaction set and a package, check it's sigs,
        return 0 if they are all fine
        return 1 if the gpg key can't be found
        return 2 if the header is in someway damaged
        return 3 if the key is not trusted
        return 4 if the pkg is not gpg or pgp signed
    """

    value = 0
    currentflags = ts.setVSFlags(0)
    fdno = os.open(package, os.O_RDONLY)
    try:
        hdr = ts.hdrFromFdno(fdno)

    except rpm.error, e:
        if str(e) == "public key not availaiable":
            value = 1
        if str(e) == "public key not available":
            value = 1
        if str(e) == "public key not trusted":
            value = 3
        if str(e) == "error reading package header":
            value = 2
    else:
        error, siginfo = getSigInfo(hdr)
        if error == 101:
            os.close(fdno)
            del hdr
            value = 4
        else:
            del hdr

    try:
        os.close(fdno)
    except OSError:
        pass

    ts.setVSFlags(currentflags) # put things back like they were before
    return value

def getSigInfo(hdr):
    """ checks signature from an hdr hand back signature information and/or
        an error code
    """

    import locale
    locale.setlocale(locale.LC_ALL, 'C')

    string = '%|DSAHEADER?{%{DSAHEADER:pgpsig}}:{%|RSAHEADER?{%{RSAHEADER:pgpsig}}:{%|SIGGPG?{%{SIGGPG:pgpsig}}:{%|SIGPGP?{%{SIGPGP:pgpsig}}:{(none)}|}|}|}|'
    siginfo = hdr.sprintf(string)
    if siginfo != '(none)':
        error = 0
        sigtype, sigdate, sigid = siginfo.split(',')
    else:
        error = 101
        sigtype = 'MD5'
        sigdate = 'None'
        sigid = 'None'

    infotuple = (sigtype, sigdate, sigid)
    return error, infotuple

def checkRepositoryEULA(name, repo):
    """ This function is to check the EULA file if provided.
        return True: no EULA or accepted
        return False: user declined the EULA
    """

    import tempfile
    import shutil
    import urlparse
    import urllib2 as u2
    import httplib
    from mic.utils.errors import CreatorError

    def _check_and_download_url(u2opener, url, savepath):
        try:
            if u2opener:
                f = u2opener.open(url)
            else:
                f = u2.urlopen(url)
        except u2.HTTPError, httperror:
            if httperror.code in (404, 503):
                return None
            else:
                raise CreatorError(httperror)
        except OSError, oserr:
            if oserr.errno == 2:
                return None
            else:
                raise CreatorError(oserr)
        except IOError, oserr:
            if hasattr(oserr, "reason") and oserr.reason.errno == 2:
                return None
            else:
                raise CreatorError(oserr)
        except u2.URLError, err:
            raise CreatorError(err)
        except httplib.HTTPException, e:
            raise CreatorError(e)

        # save to file
        licf = open(savepath, "w")
        licf.write(f.read())
        licf.close()
        f.close()

        return savepath

    def _pager_file(savepath):

        if os.path.splitext(savepath)[1].upper() in ('.HTM', '.HTML'):
            pagers = ('w3m', 'links', 'lynx', 'less', 'more')
        else:
            pagers = ('less', 'more')

        file_showed = False
        for pager in pagers:
            cmd = "%s %s" % (pager, savepath)
            try:
                os.system(cmd)
            except OSError:
                continue
            else:
                file_showed = True
                break

        if not file_showed:
            f = open(savepath)
            msger.raw(f.read())
            f.close()
            msger.pause()

    # when proxy needed, make urllib2 follow it
    proxy = repo.proxy
    proxy_username = repo.proxy_username
    proxy_password = repo.proxy_password

    if not proxy:
        proxy = get_proxy_for(repo.baseurl[0])

    handlers = []
    auth_handler = u2.HTTPBasicAuthHandler(u2.HTTPPasswordMgrWithDefaultRealm())
    u2opener = None
    if proxy:
        if proxy_username:
            proxy_netloc = urlparse.urlsplit(proxy).netloc
            if proxy_password:
                proxy_url = 'http://%s:%s@%s' % (proxy_username, proxy_password, proxy_netloc)
            else:
                proxy_url = 'http://%s@%s' % (proxy_username, proxy_netloc)
        else:
            proxy_url = proxy

        proxy_support = u2.ProxyHandler({'http': proxy_url,
                                         'https': proxy_url,
                                         'ftp': proxy_url})
        handlers.append(proxy_support)

    # download all remote files to one temp dir
    baseurl = None
    repo_lic_dir = tempfile.mkdtemp(prefix = 'repolic')

    for url in repo.baseurl:
        tmphandlers = handlers[:]

        (scheme, host, path, parm, query, frag) = urlparse.urlparse(url.rstrip('/') + '/')
        if scheme not in ("http", "https", "ftp", "ftps", "file"):
            raise CreatorError("Error: invalid url %s" % url)

        if '@' in host:
            try:
                user_pass, host = host.split('@', 1)
                if ':' in user_pass:
                    user, password = user_pass.split(':', 1)
            except ValueError, e:
                raise CreatorError('Bad URL: %s' % url)

            msger.verbose("adding HTTP auth: %s, XXXXXXXX" %(user))
            auth_handler.add_password(None, host, user, password)
            tmphandlers.append(auth_handler)
            url = scheme + "://" + host + path + parm + query + frag

        if tmphandlers:
            u2opener = u2.build_opener(*tmphandlers)

        # try to download
        repo_eula_url = urlparse.urljoin(url, "LICENSE.txt")
        repo_eula_path = _check_and_download_url(
                                u2opener,
                                repo_eula_url,
                                os.path.join(repo_lic_dir, repo.id + '_LICENSE.txt'))
        if repo_eula_path:
            # found
            baseurl = url
            break

    if not baseurl:
        shutil.rmtree(repo_lic_dir) #cleanup
        return True

    # show the license file
    msger.info('For the software packages in this yum repo:')
    msger.info('    %s: %s' % (name, baseurl))
    msger.info('There is an "End User License Agreement" file that need to be checked.')
    msger.info('Please read the terms and conditions outlined in it and answer the followed qustions.')
    msger.pause()

    _pager_file(repo_eula_path)

    # Asking for the "Accept/Decline"
    if not msger.ask('Would you agree to the terms and conditions outlined in the above End User License Agreement?'):
        msger.warning('Will not install pkgs from this repo.')
        shutil.rmtree(repo_lic_dir) #cleanup
        return False

    # try to find support_info.html for extra infomation
    repo_info_url = urlparse.urljoin(baseurl, "support_info.html")
    repo_info_path = _check_and_download_url(
                            u2opener,
                            repo_info_url,
                            os.path.join(repo_lic_dir, repo.id + '_support_info.html'))
    if repo_info_path:
        msger.info('There is one more file in the repo for additional support information, please read it')
        msger.pause()
        _pager_file(repo_info_path)

    #cleanup
    shutil.rmtree(repo_lic_dir)
    return True
