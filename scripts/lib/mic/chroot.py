#!/usr/bin/python -tt
#
# Copyright (c) 2009, 2010, 2011 Intel, Inc.
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

from __future__ import with_statement
import os
import shutil
import subprocess

from mic import msger
from mic.conf import configmgr
from mic.utils import misc, errors, runner, fs_related

chroot_lockfd = -1
chroot_lock = ""
BIND_MOUNTS = (
                "/proc",
                "/proc/sys/fs/binfmt_misc",
                "/sys",
                "/dev",
                "/dev/pts",
                "/dev/shm",
                "/var/lib/dbus",
                "/var/run/dbus",
                "/var/lock",
              )

def cleanup_after_chroot(targettype,imgmount,tmpdir,tmpmnt):
    if imgmount and targettype == "img":
        imgmount.cleanup()

    if tmpdir:
        shutil.rmtree(tmpdir, ignore_errors = True)

    if tmpmnt:
        shutil.rmtree(tmpmnt, ignore_errors = True)

def check_bind_mounts(chrootdir, bindmounts):
    chrootmounts = []
    for mount in bindmounts.split(";"):
        if not mount:
            continue

        srcdst = mount.split(":")
        if len(srcdst) == 1:
            srcdst.append("none")

        if not os.path.isdir(srcdst[0]):
            return False

        if srcdst[1] == "" or srcdst[1] == "none":
            srcdst[1] = None

        if srcdst[0] in BIND_MOUNTS or srcdst[0] == '/':
            continue

        if chrootdir:
            if not srcdst[1]:
                srcdst[1] = os.path.abspath(os.path.expanduser(srcdst[0]))
            else:
                srcdst[1] = os.path.abspath(os.path.expanduser(srcdst[1]))

            tmpdir = chrootdir + "/" + srcdst[1]
            if os.path.isdir(tmpdir):
                msger.warning("Warning: dir %s has existed."  % tmpdir)

    return True

def cleanup_mounts(chrootdir):
    umountcmd = misc.find_binary_path("umount")
    abs_chrootdir = os.path.abspath(chrootdir)
    mounts = open('/proc/mounts').readlines()
    for line in reversed(mounts):
        if abs_chrootdir not in line:
            continue

        point = line.split()[1]

        # '/' to avoid common name prefix
        if abs_chrootdir == point or point.startswith(abs_chrootdir + '/'):
            args = [ umountcmd, "-l", point ]
            ret = runner.quiet(args)
            if ret != 0:
                msger.warning("failed to unmount %s" % point)

    return 0

def setup_chrootenv(chrootdir, bindmounts = None, mountparent = True):
    global chroot_lockfd, chroot_lock

    def get_bind_mounts(chrootdir, bindmounts, mountparent = True):
        chrootmounts = []
        if bindmounts in ("", None):
            bindmounts = ""

        for mount in bindmounts.split(";"):
            if not mount:
                continue

            srcdst = mount.split(":")
            srcdst[0] = os.path.abspath(os.path.expanduser(srcdst[0]))
            if len(srcdst) == 1:
                srcdst.append("none")

            # if some bindmount is not existed, but it's created inside
            # chroot, this is not expected
            if not os.path.exists(srcdst[0]):
                os.makedirs(srcdst[0])

            if not os.path.isdir(srcdst[0]):
                continue

            if srcdst[0] in BIND_MOUNTS or srcdst[0] == '/':
                msger.verbose("%s will be mounted by default." % srcdst[0])
                continue

            if srcdst[1] == "" or srcdst[1] == "none":
                srcdst[1] = None
            else:
                srcdst[1] = os.path.abspath(os.path.expanduser(srcdst[1]))
                if os.path.isdir(chrootdir + "/" + srcdst[1]):
                    msger.warning("%s has existed in %s , skip it."\
                                  % (srcdst[1], chrootdir))
                    continue

            chrootmounts.append(fs_related.BindChrootMount(srcdst[0],
                                                           chrootdir,
                                                           srcdst[1]))

        """Default bind mounts"""
        for pt in BIND_MOUNTS:
            if not os.path.exists(pt):
                continue
            chrootmounts.append(fs_related.BindChrootMount(pt,
                                                           chrootdir,
                                                           None))

        if mountparent:
            chrootmounts.append(fs_related.BindChrootMount("/",
                                                           chrootdir,
                                                           "/parentroot",
                                                           "ro"))

        for kernel in os.listdir("/lib/modules"):
            chrootmounts.append(fs_related.BindChrootMount(
                                                "/lib/modules/"+kernel,
                                                chrootdir,
                                                None,
                                                "ro"))

        return chrootmounts

    def bind_mount(chrootmounts):
        for b in chrootmounts:
            msger.verbose("bind_mount: %s -> %s" % (b.src, b.dest))
            b.mount()

    def setup_resolv(chrootdir):
        try:
            shutil.copyfile("/etc/resolv.conf", chrootdir + "/etc/resolv.conf")
        except:
            pass

    globalmounts = get_bind_mounts(chrootdir, bindmounts, mountparent)
    bind_mount(globalmounts)

    setup_resolv(chrootdir)

    mtab = "/etc/mtab"
    dstmtab = chrootdir + mtab
    if not os.path.islink(dstmtab):
        shutil.copyfile(mtab, dstmtab)

    chroot_lock = os.path.join(chrootdir, ".chroot.lock")
    chroot_lockfd = open(chroot_lock, "w")

    return globalmounts

def cleanup_chrootenv(chrootdir, bindmounts=None, globalmounts=()):
    global chroot_lockfd, chroot_lock

    def bind_unmount(chrootmounts):
        for b in reversed(chrootmounts):
            msger.verbose("bind_unmount: %s -> %s" % (b.src, b.dest))
            b.unmount()

    def cleanup_resolv(chrootdir):
        try:
            fd = open(chrootdir + "/etc/resolv.conf", "w")
            fd.truncate(0)
            fd.close()
        except:
            pass

    def kill_processes(chrootdir):
        import glob
        for fp in glob.glob("/proc/*/root"):
            try:
                if os.readlink(fp) == chrootdir:
                    pid = int(fp.split("/")[2])
                    os.kill(pid, 9)
            except:
                pass

    def cleanup_mountdir(chrootdir, bindmounts):
        if bindmounts == "" or bindmounts == None:
            return
        chrootmounts = []
        for mount in bindmounts.split(";"):
            if not mount:
                continue

            srcdst = mount.split(":")

            if len(srcdst) == 1:
                srcdst.append("none")

            if srcdst[0] == "/":
                continue

            if srcdst[1] == "" or srcdst[1] == "none":
                srcdst[1] = srcdst[0]

            srcdst[1] = os.path.abspath(os.path.expanduser(srcdst[1]))
            tmpdir = chrootdir + "/" + srcdst[1]
            if os.path.isdir(tmpdir):
                if len(os.listdir(tmpdir)) == 0:
                    shutil.rmtree(tmpdir, ignore_errors = True)
                else:
                    msger.warning("Warning: dir %s isn't empty." % tmpdir)

    chroot_lockfd.close()
    bind_unmount(globalmounts)

    if not fs_related.my_fuser(chroot_lock):
        tmpdir = chrootdir + "/parentroot"
        if os.path.exists(tmpdir) and len(os.listdir(tmpdir)) == 0:
            shutil.rmtree(tmpdir, ignore_errors = True)

        cleanup_resolv(chrootdir)

        if os.path.exists(chrootdir + "/etc/mtab"):
            os.unlink(chrootdir + "/etc/mtab")

        kill_processes(chrootdir)

    cleanup_mountdir(chrootdir, bindmounts)

def chroot(chrootdir, bindmounts = None, execute = "/bin/bash"):
    def mychroot():
        os.chroot(chrootdir)
        os.chdir("/")

    if configmgr.chroot['saveto']:
        savefs = True
        saveto = configmgr.chroot['saveto']
        wrnmsg = "Can't save chroot fs for dir %s exists" % saveto
        if saveto == chrootdir:
            savefs = False
            wrnmsg = "Dir %s is being used to chroot" % saveto
        elif os.path.exists(saveto):
            if msger.ask("Dir %s already exists, cleanup and continue?" %
                         saveto):
                shutil.rmtree(saveto, ignore_errors = True)
                savefs = True
            else:
                savefs = False

        if savefs:
            msger.info("Saving image to directory %s" % saveto)
            fs_related.makedirs(os.path.dirname(os.path.abspath(saveto)))
            runner.quiet("cp -af %s %s" % (chrootdir, saveto))
            devs = ['dev/fd',
                    'dev/stdin',
                    'dev/stdout',
                    'dev/stderr',
                    'etc/mtab']
            ignlst = [os.path.join(saveto, x) for x in devs]
            map(os.unlink, filter(os.path.exists, ignlst))
        else:
            msger.warning(wrnmsg)

    dev_null = os.open("/dev/null", os.O_WRONLY)
    files_to_check = ["/bin/bash", "/sbin/init"]

    architecture_found = False

    """ Register statically-linked qemu-arm if it is an ARM fs """
    qemu_emulator = None

    for ftc in files_to_check:
        ftc = "%s/%s" % (chrootdir,ftc)

        # Return code of 'file' is "almost always" 0 based on some man pages
        # so we need to check the file existance first.
        if not os.path.exists(ftc):
            continue

        for line in runner.outs(['file', ftc]).splitlines():
            if 'ARM' in line:
                qemu_emulator = misc.setup_qemu_emulator(chrootdir, "arm")
                architecture_found = True
                break

            if 'Intel' in line:
                architecture_found = True
                break

        if architecture_found:
            break

    os.close(dev_null)
    if not architecture_found:
        raise errors.CreatorError("Failed to get architecture from any of the "
                                  "following files %s from chroot." \
                                  % files_to_check)

    try:
        msger.info("Launching shell. Exit to continue.\n"
                   "----------------------------------")
        globalmounts = setup_chrootenv(chrootdir, bindmounts)
        subprocess.call(execute, preexec_fn = mychroot, shell=True)

    except OSError, err:
        raise errors.CreatorError("chroot err: %s" % str(err))

    finally:
        cleanup_chrootenv(chrootdir, bindmounts, globalmounts)
        if qemu_emulator:
            os.unlink(chrootdir + qemu_emulator)
