#!/usr/bin/python -tt
#
# Copyright (c) 2010, 2011 Intel Inc.
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
import time
import tempfile
import re
import shutil
import glob
import hashlib
import subprocess
import platform
import traceback


try:
    import sqlite3 as sqlite
except ImportError:
    import sqlite

try:
    from xml.etree import cElementTree
except ImportError:
    import cElementTree
xmlparse = cElementTree.parse

from mic import msger
from mic.utils.errors import CreatorError, SquashfsError
from mic.utils.fs_related import find_binary_path, makedirs
from mic.utils.proxy import get_proxy_for
from mic.utils import runner


RPM_RE  = re.compile("(.*)\.(.*) (.*)-(.*)")
RPM_FMT = "%(name)s.%(arch)s %(version)s-%(release)s"
SRPM_RE = re.compile("(.*)-(\d+.*)-(\d+\.\d+).src.rpm")


def build_name(kscfg, release=None, prefix = None, suffix = None):
    """Construct and return an image name string.

    This is a utility function to help create sensible name and fslabel
    strings. The name is constructed using the sans-prefix-and-extension
    kickstart filename and the supplied prefix and suffix.

    kscfg -- a path to a kickstart file
    release --  a replacement to suffix for image release
    prefix -- a prefix to prepend to the name; defaults to None, which causes
              no prefix to be used
    suffix -- a suffix to append to the name; defaults to None, which causes
              a YYYYMMDDHHMM suffix to be used

    Note, if maxlen is less then the len(suffix), you get to keep both pieces.

    """
    name = os.path.basename(kscfg)
    idx = name.rfind('.')
    if idx >= 0:
        name = name[:idx]

    if release is not None:
        suffix = ""
    if prefix is None:
        prefix = ""
    if suffix is None:
        suffix = time.strftime("%Y%m%d%H%M")

    if name.startswith(prefix):
        name = name[len(prefix):]

    prefix = "%s-" % prefix if prefix else ""
    suffix = "-%s" % suffix if suffix else ""

    ret = prefix + name + suffix
    return ret

def get_distro():
    """Detect linux distribution, support "meego"
    """

    support_dists = ('SuSE',
                     'debian',
                     'fedora',
                     'redhat',
                     'centos',
                     'meego',
                     'moblin',
                     'tizen')
    try:
        (dist, ver, id) = platform.linux_distribution( \
                              supported_dists = support_dists)
    except:
        (dist, ver, id) = platform.dist( \
                              supported_dists = support_dists)

    return (dist, ver, id)

def get_distro_str():
    """Get composited string for current linux distribution
    """
    (dist, ver, id) = get_distro()

    if not dist:
        return 'Unknown Linux Distro'
    else:
        distro_str = ' '.join(map(str.strip, (dist, ver, id)))
        return distro_str.strip()

_LOOP_RULE_PTH = None

def hide_loopdev_presentation():
    udev_rules = "80-prevent-loop-present.rules"
    udev_rules_dir = [
                       '/usr/lib/udev/rules.d/',
                       '/lib/udev/rules.d/',
                       '/etc/udev/rules.d/'
                     ]

    global _LOOP_RULE_PTH

    for rdir in udev_rules_dir:
        if os.path.exists(rdir):
            _LOOP_RULE_PTH = os.path.join(rdir, udev_rules)

    if not _LOOP_RULE_PTH:
        return

    try:
        with open(_LOOP_RULE_PTH, 'w') as wf:
            wf.write('KERNEL=="loop*", ENV{UDISKS_PRESENTATION_HIDE}="1"')

        runner.quiet('udevadm trigger')
    except:
        pass

def unhide_loopdev_presentation():
    global _LOOP_RULE_PTH

    if not _LOOP_RULE_PTH:
        return

    try:
        os.unlink(_LOOP_RULE_PTH)
        runner.quiet('udevadm trigger')
    except:
        pass

def extract_rpm(rpmfile, targetdir):
    rpm2cpio = find_binary_path("rpm2cpio")
    cpio = find_binary_path("cpio")

    olddir = os.getcwd()
    os.chdir(targetdir)

    msger.verbose("Extract rpm file with cpio: %s" % rpmfile)
    p1 = subprocess.Popen([rpm2cpio, rpmfile], stdout=subprocess.PIPE)
    p2 = subprocess.Popen([cpio, "-idv"], stdin=p1.stdout,
                          stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    (sout, serr) = p2.communicate()
    msger.verbose(sout or serr)

    os.chdir(olddir)

def compressing(fpath, method):
    comp_map = {
        "gz": "gzip",
        "bz2": "bzip2"
    }
    if method not in comp_map:
        raise CreatorError("Unsupport compress format: %s, valid values: %s"
                           % (method, ','.join(comp_map.keys())))
    cmd = find_binary_path(comp_map[method])
    rc = runner.show([cmd, "-f", fpath])
    if rc:
        raise CreatorError("Failed to %s file: %s" % (comp_map[method], fpath))

def taring(dstfile, target):
    import tarfile
    basen, ext = os.path.splitext(dstfile)
    comp = {".tar": None,
            ".gz": "gz", # for .tar.gz
            ".bz2": "bz2", # for .tar.bz2
            ".tgz": "gz",
            ".tbz": "bz2"}[ext]

    # specify tarball file path
    if not comp:
        tarpath = dstfile
    elif basen.endswith(".tar"):
        tarpath = basen
    else:
        tarpath = basen + ".tar"
    wf = tarfile.open(tarpath, 'w')

    if os.path.isdir(target):
        for item in os.listdir(target):
            wf.add(os.path.join(target, item), item)
    else:
        wf.add(target, os.path.basename(target))
    wf.close()

    if comp:
        compressing(tarpath, comp)
        # when dstfile ext is ".tgz" and ".tbz", should rename
        if not basen.endswith(".tar"):
            shutil.move("%s.%s" % (tarpath, comp), dstfile)

def ziping(dstfile, target):
    import zipfile
    wf = zipfile.ZipFile(dstfile, 'w', compression=zipfile.ZIP_DEFLATED)
    if os.path.isdir(target):
        for item in os.listdir(target):
            fpath = os.path.join(target, item)
            if not os.path.isfile(fpath):
                continue
            wf.write(fpath, item, zipfile.ZIP_DEFLATED)
    else:
        wf.write(target, os.path.basename(target), zipfile.ZIP_DEFLATED)
    wf.close()

pack_formats = {
    ".tar": taring,
    ".tar.gz": taring,
    ".tar.bz2": taring,
    ".tgz": taring,
    ".tbz": taring,
    ".zip": ziping,
}

def packing(dstfile, target):
    (base, ext) = os.path.splitext(dstfile)
    if ext in (".gz", ".bz2") and base.endswith(".tar"):
        ext = ".tar" + ext
    if ext not in pack_formats:
        raise CreatorError("Unsupport pack format: %s, valid values: %s"
                           % (ext, ','.join(pack_formats.keys())))
    func = pack_formats[ext]
    # func should be callable
    func(dstfile, target)

def human_size(size):
    """Return human readable string for Bytes size
    """

    if size <= 0:
        return "0M"
    import math
    measure = ['B', 'K', 'M', 'G', 'T', 'P', 'E', 'Z', 'Y']
    expo = int(math.log(size, 1024))
    mant = float(size/math.pow(1024, expo))
    return "{0:.1f}{1:s}".format(mant, measure[expo])

def get_block_size(file_obj):
    """ Returns block size for file object 'file_obj'. Errors are indicated by
    the 'IOError' exception. """

    from fcntl import ioctl
    import struct

    # Get the block size of the host file-system for the image file by calling
    # the FIGETBSZ ioctl (number 2).
    binary_data = ioctl(file_obj, 2, struct.pack('I', 0))
    return struct.unpack('I', binary_data)[0]

def check_space_pre_cp(src, dst):
    """Check whether disk space is enough before 'cp' like
    operations, else exception will be raised.
    """

    srcsize  = get_file_size(src) * 1024 * 1024
    freesize = get_filesystem_avail(dst)
    if srcsize > freesize:
        raise CreatorError("space on %s(%s) is not enough for about %s files"
                           % (dst, human_size(freesize), human_size(srcsize)))

def calc_hashes(file_path, hash_names, start = 0, end = None):
    """ Calculate hashes for a file. The 'file_path' argument is the file
    to calculate hash functions for, 'start' and 'end' are the starting and
    ending file offset to calculate the has functions for. The 'hash_names'
    argument is a list of hash names to calculate. Returns the the list
    of calculated hash values in the hexadecimal form in the same order
    as 'hash_names'.
    """
    if end == None:
        end = os.path.getsize(file_path)

    chunk_size = 65536
    to_read = end - start
    read = 0

    hashes = []
    for hash_name in hash_names:
        hashes.append(hashlib.new(hash_name))

    with open(file_path, "rb") as f:
        f.seek(start)

        while read < to_read:
            if read + chunk_size > to_read:
                chunk_size = to_read - read
            chunk = f.read(chunk_size)
            for hash_obj in hashes:
                hash_obj.update(chunk)
            read += chunk_size

    result = []
    for hash_obj in hashes:
        result.append(hash_obj.hexdigest())

    return result

def get_md5sum(fpath):
    return calc_hashes(fpath, ('md5', ))[0]


def normalize_ksfile(ksconf, release, arch):
    '''
    Return the name of a normalized ks file in which macro variables
    @BUILD_ID@ and @ARCH@ are replace with real values.

    The original ks file is returned if no special macro is used, otherwise
    a temp file is created and returned, which will be deleted when program
    exits normally.
    '''

    if not release:
        release = "latest"
    if not arch or re.match(r'i.86', arch):
        arch = "ia32"

    with open(ksconf) as f:
        ksc = f.read()

    if "@ARCH@" not in ksc and "@BUILD_ID@" not in ksc:
        return ksconf

    msger.info("Substitute macro variable @BUILD_ID@/@ARCH@ in ks: %s" % ksconf)
    ksc = ksc.replace("@ARCH@", arch)
    ksc = ksc.replace("@BUILD_ID@", release)

    fd, ksconf = tempfile.mkstemp(prefix=os.path.basename(ksconf))
    os.write(fd, ksc)
    os.close(fd)

    msger.debug('normalized ks file:%s' % ksconf)

    def remove_temp_ks():
        try:
            os.unlink(ksconf)
        except OSError, err:
            msger.warning('Failed to remove temp ks file:%s:%s' % (ksconf, err))

    import atexit
    atexit.register(remove_temp_ks)

    return ksconf


def _check_mic_chroot(rootdir):
    def _path(path):
        return rootdir.rstrip('/') + path

    release_files = map(_path, [ "/etc/moblin-release",
                                 "/etc/meego-release",
                                 "/etc/tizen-release"])

    if not any(map(os.path.exists, release_files)):
        msger.warning("Dir %s is not a MeeGo/Tizen chroot env" % rootdir)

    if not glob.glob(rootdir + "/boot/vmlinuz-*"):
        msger.warning("Failed to find kernel module under %s" % rootdir)

    return

def selinux_check(arch, fstypes):
    try:
        getenforce = find_binary_path('getenforce')
    except CreatorError:
        return

    selinux_status = runner.outs([getenforce])
    if arch and arch.startswith("arm") and selinux_status == "Enforcing":
        raise CreatorError("Can't create arm image if selinux is enabled, "
                           "please run 'setenforce 0' to disable selinux")

    use_btrfs = filter(lambda typ: typ == 'btrfs', fstypes)
    if use_btrfs and selinux_status == "Enforcing":
        raise CreatorError("Can't create btrfs image if selinux is enabled,"
                           " please run 'setenforce 0' to disable selinux")

def get_image_type(path):
    def _get_extension_name(path):
        match = re.search("(?<=\.)\w+$", path)
        if match:
            return match.group(0)
        else:
            return None

    if os.path.isdir(path):
        _check_mic_chroot(path)
        return "fs"

    maptab = {
              "tar": "loop",
              "raw":"raw",
              "vmdk":"vmdk",
              "vdi":"vdi",
              "iso":"livecd",
              "usbimg":"liveusb",
             }

    extension = _get_extension_name(path)
    if extension in maptab:
        return maptab[extension]

    fd = open(path, "rb")
    file_header = fd.read(1024)
    fd.close()
    vdi_flag = "<<< Sun VirtualBox Disk Image >>>"
    if file_header[0:len(vdi_flag)] == vdi_flag:
        return maptab["vdi"]

    output = runner.outs(['file', path])
    isoptn = re.compile(r".*ISO 9660 CD-ROM filesystem.*(bootable).*")
    usbimgptn = re.compile(r".*x86 boot sector.*active.*")
    rawptn = re.compile(r".*x86 boot sector.*")
    vmdkptn = re.compile(r".*VMware. disk image.*")
    ext3fsimgptn = re.compile(r".*Linux.*ext3 filesystem data.*")
    ext4fsimgptn = re.compile(r".*Linux.*ext4 filesystem data.*")
    btrfsimgptn = re.compile(r".*BTRFS.*")
    if isoptn.match(output):
        return maptab["iso"]
    elif usbimgptn.match(output):
        return maptab["usbimg"]
    elif rawptn.match(output):
        return maptab["raw"]
    elif vmdkptn.match(output):
        return maptab["vmdk"]
    elif ext3fsimgptn.match(output):
        return "ext3fsimg"
    elif ext4fsimgptn.match(output):
        return "ext4fsimg"
    elif btrfsimgptn.match(output):
        return "btrfsimg"
    else:
        raise CreatorError("Cannot detect the type of image: %s" % path)


def get_file_size(filename):
    """ Return size in MB unit """
    cmd = ['du', "-s", "-b", "-B", "1M", filename]
    rc, duOutput  = runner.runtool(cmd)
    if rc != 0:
        raise CreatorError("Failed to run: %s" % ' '.join(cmd))
    size1 = int(duOutput.split()[0])

    cmd = ['du', "-s", "-B", "1M", filename]
    rc, duOutput = runner.runtool(cmd)
    if rc != 0:
        raise CreatorError("Failed to run: %s" % ' '.join(cmd))

    size2 = int(duOutput.split()[0])
    return max(size1, size2)


def get_filesystem_avail(fs):
    vfstat = os.statvfs(fs)
    return vfstat.f_bavail * vfstat.f_bsize

def convert_image(srcimg, srcfmt, dstimg, dstfmt):
    #convert disk format
    if dstfmt != "raw":
        raise CreatorError("Invalid destination image format: %s" % dstfmt)
    msger.debug("converting %s image to %s" % (srcimg, dstimg))
    if srcfmt == "vmdk":
        path = find_binary_path("qemu-img")
        argv = [path, "convert", "-f", "vmdk", srcimg, "-O", dstfmt,  dstimg]
    elif srcfmt == "vdi":
        path = find_binary_path("VBoxManage")
        argv = [path, "internalcommands", "converttoraw", srcimg, dstimg]
    else:
        raise CreatorError("Invalid soure image format: %s" % srcfmt)

    rc = runner.show(argv)
    if rc == 0:
        msger.debug("convert successful")
    if rc != 0:
        raise CreatorError("Unable to convert disk to %s" % dstfmt)

def uncompress_squashfs(squashfsimg, outdir):
    """Uncompress file system from squshfs image"""
    unsquashfs = find_binary_path("unsquashfs")
    args = [ unsquashfs, "-d", outdir, squashfsimg ]
    rc = runner.show(args)
    if (rc != 0):
        raise SquashfsError("Failed to uncompress %s." % squashfsimg)

def mkdtemp(dir = "/var/tmp", prefix = "wic-tmp-"):
    """ FIXME: use the dir in wic.conf instead """

    makedirs(dir)
    return tempfile.mkdtemp(dir = dir, prefix = prefix)

def get_repostrs_from_ks(ks):
    def _get_temp_reponame(baseurl):
        md5obj = hashlib.md5(baseurl)
        tmpreponame = "%s" % md5obj.hexdigest()
        return tmpreponame

    kickstart_repos = []

    for repodata in ks.handler.repo.repoList:
        repo = {}
        for attr in ('name',
                     'baseurl',
                     'mirrorlist',
                     'includepkgs', # val is list
                     'excludepkgs', # val is list
                     'cost',    # int
                     'priority',# int
                     'save',
                     'proxy',
                     'proxyuser',
                     'proxypasswd',
                     'proxypasswd',
                     'debuginfo',
                     'source',
                     'gpgkey',
                     'ssl_verify'):
            if hasattr(repodata, attr) and getattr(repodata, attr):
                repo[attr] = getattr(repodata, attr)

        if 'name' not in repo:
            repo['name'] = _get_temp_reponame(repodata.baseurl)

        kickstart_repos.append(repo)

    return kickstart_repos

def _get_uncompressed_data_from_url(url, filename, proxies):
    filename = myurlgrab(url, filename, proxies)
    suffix = None
    if filename.endswith(".gz"):
        suffix = ".gz"
        runner.quiet(['gunzip', "-f", filename])
    elif filename.endswith(".bz2"):
        suffix = ".bz2"
        runner.quiet(['bunzip2', "-f", filename])
    if suffix:
        filename = filename.replace(suffix, "")
    return filename

def _get_metadata_from_repo(baseurl, proxies, cachedir, reponame, filename,
                            sumtype=None, checksum=None):
    url = os.path.join(baseurl, filename)
    filename_tmp = str("%s/%s/%s" % (cachedir, reponame, os.path.basename(filename)))
    if os.path.splitext(filename_tmp)[1] in (".gz", ".bz2"):
        filename = os.path.splitext(filename_tmp)[0]
    else:
        filename = filename_tmp
    if sumtype and checksum and os.path.exists(filename):
        try:
            sumcmd = find_binary_path("%ssum" % sumtype)
        except:
            file_checksum = None
        else:
            file_checksum = runner.outs([sumcmd, filename]).split()[0]

        if file_checksum and file_checksum == checksum:
            return filename

    return _get_uncompressed_data_from_url(url,filename_tmp,proxies)

def get_metadata_from_repos(repos, cachedir):
    my_repo_metadata = []
    for repo in repos:
        reponame = repo['name']
        baseurl  = repo['baseurl']


        if 'proxy' in repo:
            proxy = repo['proxy']
        else:
            proxy = get_proxy_for(baseurl)

        proxies = None
        if proxy:
            proxies = {str(baseurl.split(":")[0]):str(proxy)}

        makedirs(os.path.join(cachedir, reponame))
        url = os.path.join(baseurl, "repodata/repomd.xml")
        filename = os.path.join(cachedir, reponame, 'repomd.xml')
        repomd = myurlgrab(url, filename, proxies)
        try:
            root = xmlparse(repomd)
        except SyntaxError:
            raise CreatorError("repomd.xml syntax error.")

        ns = root.getroot().tag
        ns = ns[0:ns.rindex("}")+1]

        filepaths = {}
        checksums = {}
        sumtypes = {}

        for elm in root.getiterator("%sdata" % ns):
            if elm.attrib["type"] == "patterns":
                filepaths['patterns'] = elm.find("%slocation" % ns).attrib['href']
                checksums['patterns'] = elm.find("%sopen-checksum" % ns).text
                sumtypes['patterns'] = elm.find("%sopen-checksum" % ns).attrib['type']
                break

        for elm in root.getiterator("%sdata" % ns):
            if elm.attrib["type"] in ("group_gz", "group"):
                filepaths['comps'] = elm.find("%slocation" % ns).attrib['href']
                checksums['comps'] = elm.find("%sopen-checksum" % ns).text
                sumtypes['comps'] = elm.find("%sopen-checksum" % ns).attrib['type']
                break

        primary_type = None
        for elm in root.getiterator("%sdata" % ns):
            if elm.attrib["type"] in ("primary_db", "primary"):
                primary_type = elm.attrib["type"]
                filepaths['primary'] = elm.find("%slocation" % ns).attrib['href']
                checksums['primary'] = elm.find("%sopen-checksum" % ns).text
                sumtypes['primary'] = elm.find("%sopen-checksum" % ns).attrib['type']
                break

        if not primary_type:
            continue

        for item in ("primary", "patterns", "comps"):
            if item not in filepaths:
                filepaths[item] = None
                continue
            if not filepaths[item]:
                continue
            filepaths[item] = _get_metadata_from_repo(baseurl,
                                                      proxies,
                                                      cachedir,
                                                      reponame,
                                                      filepaths[item],
                                                      sumtypes[item],
                                                      checksums[item])

        """ Get repo key """
        try:
            repokey = _get_metadata_from_repo(baseurl,
                                              proxies,
                                              cachedir,
                                              reponame,
                                              "repodata/repomd.xml.key")
        except CreatorError:
            repokey = None
            msger.debug("\ncan't get %s/%s" % (baseurl, "repodata/repomd.xml.key"))

        my_repo_metadata.append({"name":reponame,
                                 "baseurl":baseurl,
                                 "repomd":repomd,
                                 "primary":filepaths['primary'],
                                 "cachedir":cachedir,
                                 "proxies":proxies,
                                 "patterns":filepaths['patterns'],
                                 "comps":filepaths['comps'],
                                 "repokey":repokey})

    return my_repo_metadata

def get_rpmver_in_repo(repometadata):
    for repo in repometadata:
        if repo["primary"].endswith(".xml"):
            root = xmlparse(repo["primary"])
            ns = root.getroot().tag
            ns = ns[0:ns.rindex("}")+1]

            versionlist = []
            for elm in root.getiterator("%spackage" % ns):
                if elm.find("%sname" % ns).text == 'rpm':
                    for node in elm.getchildren():
                        if node.tag == "%sversion" % ns:
                            versionlist.append(node.attrib['ver'])

            if versionlist:
                return reversed(
                         sorted(
                           versionlist,
                           key = lambda ver: map(int, ver.split('.')))).next()

        elif repo["primary"].endswith(".sqlite"):
            con = sqlite.connect(repo["primary"])
            for row in con.execute("select version from packages where "
                                   "name=\"rpm\" ORDER by version DESC"):
                con.close()
                return row[0]

    return None

def get_arch(repometadata):
    archlist = []
    for repo in repometadata:
        if repo["primary"].endswith(".xml"):
            root = xmlparse(repo["primary"])
            ns = root.getroot().tag
            ns = ns[0:ns.rindex("}")+1]
            for elm in root.getiterator("%spackage" % ns):
                if elm.find("%sarch" % ns).text not in ("noarch", "src"):
                    arch = elm.find("%sarch" % ns).text
                    if arch not in archlist:
                        archlist.append(arch)
        elif repo["primary"].endswith(".sqlite"):
            con = sqlite.connect(repo["primary"])
            for row in con.execute("select arch from packages where arch not in (\"src\", \"noarch\")"):
                if row[0] not in archlist:
                    archlist.append(row[0])

            con.close()

    uniq_arch = []
    for i in range(len(archlist)):
        if archlist[i] not in rpmmisc.archPolicies.keys():
            continue
        need_append = True
        j = 0
        while j < len(uniq_arch):
            if archlist[i] in rpmmisc.archPolicies[uniq_arch[j]].split(':'):
                need_append = False
                break
            if uniq_arch[j] in rpmmisc.archPolicies[archlist[i]].split(':'):
                if need_append:
                    uniq_arch[j] = archlist[i]
                    need_append = False
                else:
                    uniq_arch.remove(uniq_arch[j])
                    continue
            j += 1
        if need_append:
             uniq_arch.append(archlist[i])

    return uniq_arch, archlist

def get_package(pkg, repometadata, arch = None):
    ver = ""
    target_repo = None
    if not arch:
        arches = []
    elif arch not in rpmmisc.archPolicies:
        arches = [arch]
    else:
        arches = rpmmisc.archPolicies[arch].split(':')
        arches.append('noarch')

    for repo in repometadata:
        if repo["primary"].endswith(".xml"):
            root = xmlparse(repo["primary"])
            ns = root.getroot().tag
            ns = ns[0:ns.rindex("}")+1]
            for elm in root.getiterator("%spackage" % ns):
                if elm.find("%sname" % ns).text == pkg:
                    if elm.find("%sarch" % ns).text in arches:
                        version = elm.find("%sversion" % ns)
                        tmpver = "%s-%s" % (version.attrib['ver'], version.attrib['rel'])
                        if tmpver > ver:
                            ver = tmpver
                            location = elm.find("%slocation" % ns)
                            pkgpath = "%s" % location.attrib['href']
                            target_repo = repo
                        break
        if repo["primary"].endswith(".sqlite"):
            con = sqlite.connect(repo["primary"])
            if arch:
                sql = 'select version, release, location_href from packages ' \
                      'where name = "%s" and arch IN ("%s")' % \
                      (pkg, '","'.join(arches))
                for row in con.execute(sql):
                    tmpver = "%s-%s" % (row[0], row[1])
                    if tmpver > ver:
                        ver = tmpver
                        pkgpath = "%s" % row[2]
                        target_repo = repo
                    break
            else:
                sql = 'select version, release, location_href from packages ' \
                      'where name = "%s"' % pkg
                for row in con.execute(sql):
                    tmpver = "%s-%s" % (row[0], row[1])
                    if tmpver > ver:
                        ver = tmpver
                        pkgpath = "%s" % row[2]
                        target_repo = repo
                    break
            con.close()
    if target_repo:
        makedirs("%s/packages/%s" % (target_repo["cachedir"], target_repo["name"]))
        url = os.path.join(target_repo["baseurl"], pkgpath)
        filename = str("%s/packages/%s/%s" % (target_repo["cachedir"], target_repo["name"], os.path.basename(pkgpath)))
        if os.path.exists(filename):
            ret = rpmmisc.checkRpmIntegrity('rpm', filename)
            if ret == 0:
                return filename

            msger.warning("package %s is damaged: %s" %
                          (os.path.basename(filename), filename))
            os.unlink(filename)

        pkg = myurlgrab(str(url), filename, target_repo["proxies"])
        return pkg
    else:
        return None

def get_source_name(pkg, repometadata):

    def get_bin_name(pkg):
        m = RPM_RE.match(pkg)
        if m:
            return m.group(1)
        return None

    def get_src_name(srpm):
        m = SRPM_RE.match(srpm)
        if m:
            return m.group(1)
        return None

    ver = ""
    target_repo = None

    pkg_name = get_bin_name(pkg)
    if not pkg_name:
        return None

    for repo in repometadata:
        if repo["primary"].endswith(".xml"):
            root = xmlparse(repo["primary"])
            ns = root.getroot().tag
            ns = ns[0:ns.rindex("}")+1]
            for elm in root.getiterator("%spackage" % ns):
                if elm.find("%sname" % ns).text == pkg_name:
                    if elm.find("%sarch" % ns).text != "src":
                        version = elm.find("%sversion" % ns)
                        tmpver = "%s-%s" % (version.attrib['ver'], version.attrib['rel'])
                        if tmpver > ver:
                            ver = tmpver
                            fmt = elm.find("%sformat" % ns)
                            if fmt:
                                fns = fmt.getchildren()[0].tag
                                fns = fns[0:fns.rindex("}")+1]
                                pkgpath = fmt.find("%ssourcerpm" % fns).text
                                target_repo = repo
                        break

        if repo["primary"].endswith(".sqlite"):
            con = sqlite.connect(repo["primary"])
            for row in con.execute("select version, release, rpm_sourcerpm from packages where name = \"%s\" and arch != \"src\"" % pkg_name):
                tmpver = "%s-%s" % (row[0], row[1])
                if tmpver > ver:
                    pkgpath = "%s" % row[2]
                    target_repo = repo
                break
            con.close()
    if target_repo:
        return get_src_name(pkgpath)
    else:
        return None

def get_pkglist_in_patterns(group, patterns):
    found = False
    pkglist = []
    try:
        root = xmlparse(patterns)
    except SyntaxError:
        raise SyntaxError("%s syntax error." % patterns)

    for elm in list(root.getroot()):
        ns = elm.tag
        ns = ns[0:ns.rindex("}")+1]
        name = elm.find("%sname" % ns)
        summary = elm.find("%ssummary" % ns)
        if name.text == group or summary.text == group:
            found = True
            break

    if not found:
        return pkglist

    found = False
    for requires in list(elm):
        if requires.tag.endswith("requires"):
            found = True
            break

    if not found:
        return pkglist

    for pkg in list(requires):
        pkgname = pkg.attrib["name"]
        if pkgname not in pkglist:
            pkglist.append(pkgname)

    return pkglist

def get_pkglist_in_comps(group, comps):
    found = False
    pkglist = []
    try:
        root = xmlparse(comps)
    except SyntaxError:
        raise SyntaxError("%s syntax error." % comps)

    for elm in root.getiterator("group"):
        id = elm.find("id")
        name = elm.find("name")
        if id.text == group or name.text == group:
            packagelist = elm.find("packagelist")
            found = True
            break

    if not found:
        return pkglist

    for require in elm.getiterator("packagereq"):
        if require.tag.endswith("packagereq"):
            pkgname = require.text
        if pkgname not in pkglist:
            pkglist.append(pkgname)

    return pkglist

def is_statically_linked(binary):
    return ", statically linked, " in runner.outs(['file', binary])

def setup_qemu_emulator(rootdir, arch):
    # mount binfmt_misc if it doesn't exist
    if not os.path.exists("/proc/sys/fs/binfmt_misc"):
        modprobecmd = find_binary_path("modprobe")
        runner.show([modprobecmd, "binfmt_misc"])
    if not os.path.exists("/proc/sys/fs/binfmt_misc/register"):
        mountcmd = find_binary_path("mount")
        runner.show([mountcmd, "-t", "binfmt_misc", "none", "/proc/sys/fs/binfmt_misc"])

    # qemu_emulator is a special case, we can't use find_binary_path
    # qemu emulator should be a statically-linked executable file
    qemu_emulator = "/usr/bin/qemu-arm"
    if not os.path.exists(qemu_emulator) or not is_statically_linked(qemu_emulator):
        qemu_emulator = "/usr/bin/qemu-arm-static"
    if not os.path.exists(qemu_emulator):
        raise CreatorError("Please install a statically-linked qemu-arm")

    # qemu emulator version check
    armv7_list = [arch for arch in rpmmisc.archPolicies.keys() if arch.startswith('armv7')]
    if arch in armv7_list:  # need qemu (>=0.13.0)
        qemuout = runner.outs([qemu_emulator, "-h"])
        m = re.search("version\s*([.\d]+)", qemuout)
        if m:
            qemu_version = m.group(1)
            if qemu_version < "0.13":
                raise CreatorError("Requires %s version >=0.13 for %s" % (qemu_emulator, arch))
        else:
            msger.warning("Can't get version info of %s, please make sure it's higher than 0.13.0" % qemu_emulator)

    if not os.path.exists(rootdir + "/usr/bin"):
        makedirs(rootdir + "/usr/bin")
    shutil.copy(qemu_emulator, rootdir + "/usr/bin/qemu-arm-static")
    qemu_emulator = "/usr/bin/qemu-arm-static"

    # disable selinux, selinux will block qemu emulator to run
    if os.path.exists("/usr/sbin/setenforce"):
        msger.info('Try to disable selinux')
        runner.show(["/usr/sbin/setenforce", "0"])

    # unregister it if it has been registered and is a dynamically-linked executable
    node = "/proc/sys/fs/binfmt_misc/arm"
    if os.path.exists(node):
        qemu_unregister_string = "-1\n"
        fd = open("/proc/sys/fs/binfmt_misc/arm", "w")
        fd.write(qemu_unregister_string)
        fd.close()

    # register qemu emulator for interpreting other arch executable file
    if not os.path.exists(node):
        qemu_arm_string = ":arm:M::\\x7fELF\\x01\\x01\\x01\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x02\\x00\\x28\\x00:\\xff\\xff\\xff\\xff\\xff\\xff\\xff\\x00\\xff\\xff\\xff\\xff\\xff\\xff\\xff\\xff\\xfa\\xff\\xff\\xff:%s:\n" % qemu_emulator
        fd = open("/proc/sys/fs/binfmt_misc/register", "w")
        fd.write(qemu_arm_string)
        fd.close()

    return qemu_emulator

def SrcpkgsDownload(pkgs, repometadata, instroot, cachedir):
    def get_source_repometadata(repometadata):
        src_repometadata=[]
        for repo in repometadata:
            if repo["name"].endswith("-source"):
                src_repometadata.append(repo)
        if src_repometadata:
            return src_repometadata
        return None

    def get_src_name(srpm):
        m = SRPM_RE.match(srpm)
        if m:
            return m.group(1)
        return None

    src_repometadata = get_source_repometadata(repometadata)

    if not src_repometadata:
        msger.warning("No source repo found")
        return None

    src_pkgs = []
    lpkgs_dict = {}
    lpkgs_path = []
    for repo in src_repometadata:
        cachepath = "%s/%s/packages/*.src.rpm" %(cachedir, repo["name"])
        lpkgs_path += glob.glob(cachepath)

    for lpkg in lpkgs_path:
        lpkg_name = get_src_name(os.path.basename(lpkg))
        lpkgs_dict[lpkg_name] = lpkg
    localpkgs = lpkgs_dict.keys()

    cached_count = 0
    destdir = instroot+'/usr/src/SRPMS'
    if not os.path.exists(destdir):
        os.makedirs(destdir)

    srcpkgset = set()
    for _pkg in pkgs:
        srcpkg_name = get_source_name(_pkg, repometadata)
        if not srcpkg_name:
            continue
        srcpkgset.add(srcpkg_name)

    for pkg in list(srcpkgset):
        if pkg in localpkgs:
            cached_count += 1
            shutil.copy(lpkgs_dict[pkg], destdir)
            src_pkgs.append(os.path.basename(lpkgs_dict[pkg]))
        else:
            src_pkg = get_package(pkg, src_repometadata, 'src')
            if src_pkg:
                shutil.copy(src_pkg, destdir)
                src_pkgs.append(src_pkg)
    msger.info("%d source packages gotten from cache" % cached_count)

    return src_pkgs

def strip_end(text, suffix):
    if not text.endswith(suffix):
        return text
    return text[:-len(suffix)]
