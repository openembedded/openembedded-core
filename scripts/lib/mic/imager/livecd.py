#!/usr/bin/python -tt
#
# Copyright (c) 2011 Intel, Inc.
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

import os, sys
import glob
import shutil

from mic import kickstart, msger
from mic.utils import fs_related, runner, misc
from mic.utils.errors import CreatorError
from mic.imager.loop import LoopImageCreator


class LiveImageCreatorBase(LoopImageCreator):
    """A base class for LiveCD image creators.

        This class serves as a base class for the architecture-specific LiveCD
        image creator subclass, LiveImageCreator.

        LiveImageCreator creates a bootable ISO containing the system image,
        bootloader, bootloader configuration, kernel and initramfs.
    """

    def __init__(self, creatoropts = None, pkgmgr = None):
        """Initialise a LiveImageCreator instance.

           This method takes the same arguments as ImageCreator.__init__().
        """
        LoopImageCreator.__init__(self, creatoropts, pkgmgr)

        #Controls whether to use squashfs to compress the image.
        self.skip_compression = False

        #Controls whether an image minimizing snapshot should be created.
        #
        #This snapshot can be used when copying the system image from the ISO in
        #order to minimize the amount of data that needs to be copied; simply,
        #it makes it possible to create a version of the image's filesystem with
        #no spare space.
        self.skip_minimize = False

        #A flag which indicates i act as a convertor default false
        self.actasconvertor = False

        #The bootloader timeout from kickstart.
        if self.ks:
            self._timeout = kickstart.get_timeout(self.ks, 10)
        else:
            self._timeout = 10

        #The default kernel type from kickstart.
        if self.ks:
            self._default_kernel = kickstart.get_default_kernel(self.ks,
                                                                "kernel")
        else:
            self._default_kernel = None

        if self.ks:
            parts = kickstart.get_partitions(self.ks)
            if len(parts) > 1:
                raise CreatorError("Can't support multi partitions in ks file "
                                   "for this image type")
            # FIXME: rename rootfs img to self.name,
            # else can't find files when create iso
            self._instloops[0]['name'] = self.name + ".img"

        self.__isodir = None

        self.__modules = ["=ata",
                          "sym53c8xx",
                          "aic7xxx",
                          "=usb",
                          "=firewire",
                          "=mmc",
                          "=pcmcia",
                          "mptsas"]
        if self.ks:
            self.__modules.extend(kickstart.get_modules(self.ks))

        self._dep_checks.extend(["isohybrid",
                                 "unsquashfs",
                                 "mksquashfs",
                                 "dd",
                                 "genisoimage"])

    #
    # Hooks for subclasses
    #
    def _configure_bootloader(self, isodir):
        """Create the architecture specific booloader configuration.

            This is the hook where subclasses must create the booloader
            configuration in order to allow a bootable ISO to be built.

            isodir -- the directory where the contents of the ISO are to
                      be staged
        """
        raise CreatorError("Bootloader configuration is arch-specific, "
                           "but not implemented for this arch!")
    def _get_menu_options(self):
        """Return a menu options string for syslinux configuration.
        """
        if self.ks is None:
            return "liveinst autoinst"
        r = kickstart.get_menu_args(self.ks)
        return r

    def _get_kernel_options(self):
        """Return a kernel options string for bootloader configuration.

            This is the hook where subclasses may specify a set of kernel
            options which should be included in the images bootloader
            configuration.

            A sensible default implementation is provided.
        """

        if self.ks is None:
            r = "ro rd.live.image"
        else:
            r = kickstart.get_kernel_args(self.ks)

        return r

    def _get_mkisofs_options(self, isodir):
        """Return the architecture specific mkisosfs options.

            This is the hook where subclasses may specify additional arguments
            to mkisofs, e.g. to enable a bootable ISO to be built.

            By default, an empty list is returned.
        """
        return []

    #
    # Helpers for subclasses
    #
    def _has_checkisomd5(self):
        """Check whether checkisomd5 is available in the install root."""
        def _exists(path):
            return os.path.exists(self._instroot + path)

        if _exists("/usr/bin/checkisomd5") and os.path.exists("/usr/bin/implantisomd5"):
            return True

        return False

    def __restore_file(self,path):
        try:
            os.unlink(path)
        except:
            pass
        if os.path.exists(path + '.rpmnew'):
            os.rename(path + '.rpmnew', path)

    def _mount_instroot(self, base_on = None):
        LoopImageCreator._mount_instroot(self, base_on)
        self.__write_initrd_conf(self._instroot + "/etc/sysconfig/mkinitrd")
        self.__write_dracut_conf(self._instroot + "/etc/dracut.conf.d/02livecd.conf")

    def _unmount_instroot(self):
        self.__restore_file(self._instroot + "/etc/sysconfig/mkinitrd")
        self.__restore_file(self._instroot + "/etc/dracut.conf.d/02livecd.conf")
        LoopImageCreator._unmount_instroot(self)

    def __ensure_isodir(self):
        if self.__isodir is None:
            self.__isodir = self._mkdtemp("iso-")
        return self.__isodir

    def _get_isodir(self):
        return self.__ensure_isodir()

    def _set_isodir(self, isodir = None):
        self.__isodir = isodir

    def _create_bootconfig(self):
        """Configure the image so that it's bootable."""
        self._configure_bootloader(self.__ensure_isodir())

    def _get_post_scripts_env(self, in_chroot):
        env = LoopImageCreator._get_post_scripts_env(self, in_chroot)

        if not in_chroot:
            env["LIVE_ROOT"] = self.__ensure_isodir()

        return env
    def __write_dracut_conf(self, path):
        if not os.path.exists(os.path.dirname(path)):
            fs_related.makedirs(os.path.dirname(path))
        f = open(path, "a")
        f.write('add_dracutmodules+=" dmsquash-live pollcdrom "')
        f.close()

    def __write_initrd_conf(self, path):
        content = ""
        if not os.path.exists(os.path.dirname(path)):
            fs_related.makedirs(os.path.dirname(path))
        f = open(path, "w")

        content += 'LIVEOS="yes"\n'
        content += 'PROBE="no"\n'
        content += 'MODULES+="squashfs ext3 ext2 vfat msdos "\n'
        content += 'MODULES+="sr_mod sd_mod ide-cd cdrom "\n'

        for module in self.__modules:
            if module == "=usb":
                content += 'MODULES+="ehci_hcd uhci_hcd ohci_hcd "\n'
                content += 'MODULES+="usb_storage usbhid "\n'
            elif module == "=firewire":
                content += 'MODULES+="firewire-sbp2 firewire-ohci "\n'
                content += 'MODULES+="sbp2 ohci1394 ieee1394 "\n'
            elif module == "=mmc":
                content += 'MODULES+="mmc_block sdhci sdhci-pci "\n'
            elif module == "=pcmcia":
                content += 'MODULES+="pata_pcmcia  "\n'
            else:
                content += 'MODULES+="' + module + ' "\n'
        f.write(content)
        f.close()

    def __create_iso(self, isodir):
        iso = self._outdir + "/" + self.name + ".iso"
        genisoimage = fs_related.find_binary_path("genisoimage")
        args = [genisoimage,
                "-J", "-r",
                "-hide-rr-moved", "-hide-joliet-trans-tbl",
                "-V", self.fslabel,
                "-o", iso]

        args.extend(self._get_mkisofs_options(isodir))

        args.append(isodir)

        if runner.show(args) != 0:
            raise CreatorError("ISO creation failed!")

        """ It should be ok still even if you haven't isohybrid """
        isohybrid = None
        try:
            isohybrid = fs_related.find_binary_path("isohybrid")
        except:
            pass

        if isohybrid:
            args = [isohybrid, "-partok", iso ]
            if runner.show(args) != 0:
                raise CreatorError("Hybrid ISO creation failed!")

        self.__implant_md5sum(iso)

    def __implant_md5sum(self, iso):
        """Implant an isomd5sum."""
        if os.path.exists("/usr/bin/implantisomd5"):
            implantisomd5 = "/usr/bin/implantisomd5"
        else:
            msger.warning("isomd5sum not installed; not setting up mediacheck")
            implantisomd5 = ""
            return

        runner.show([implantisomd5, iso])

    def _stage_final_image(self):
        try:
            fs_related.makedirs(self.__ensure_isodir() + "/LiveOS")

            minimal_size = self._resparse()

            if not self.skip_minimize:
                fs_related.create_image_minimizer(self.__isodir + \
                                                      "/LiveOS/osmin.img",
                                                  self._image,
                                                  minimal_size)

            if self.skip_compression:
                shutil.move(self._image, self.__isodir + "/LiveOS/ext3fs.img")
            else:
                fs_related.makedirs(os.path.join(
                                        os.path.dirname(self._image),
                                        "LiveOS"))
                shutil.move(self._image,
                            os.path.join(os.path.dirname(self._image),
                                         "LiveOS", "ext3fs.img"))
                fs_related.mksquashfs(os.path.dirname(self._image),
                           self.__isodir + "/LiveOS/squashfs.img")

            self.__create_iso(self.__isodir)

            if self.pack_to:
                isoimg = os.path.join(self._outdir, self.name + ".iso")
                packimg = os.path.join(self._outdir, self.pack_to)
                misc.packing(packimg, isoimg)
                os.unlink(isoimg)

        finally:
            shutil.rmtree(self.__isodir, ignore_errors = True)
            self.__isodir = None

class x86LiveImageCreator(LiveImageCreatorBase):
    """ImageCreator for x86 machines"""
    def _get_mkisofs_options(self, isodir):
        return [ "-b", "isolinux/isolinux.bin",
                 "-c", "isolinux/boot.cat",
                 "-no-emul-boot", "-boot-info-table",
                 "-boot-load-size", "4" ]

    def _get_required_packages(self):
        return ["syslinux", "syslinux-extlinux"] + \
               LiveImageCreatorBase._get_required_packages(self)

    def _get_isolinux_stanzas(self, isodir):
        return ""

    def __find_syslinux_menu(self):
        for menu in ["vesamenu.c32", "menu.c32"]:
            if os.path.isfile(self._instroot + "/usr/share/syslinux/" + menu):
                return menu

        raise CreatorError("syslinux not installed : "
                           "no suitable /usr/share/syslinux/*menu.c32 found")

    def __find_syslinux_mboot(self):
        #
        # We only need the mboot module if we have any xen hypervisors
        #
        if not glob.glob(self._instroot + "/boot/xen.gz*"):
            return None

        return "mboot.c32"

    def __copy_syslinux_files(self, isodir, menu, mboot = None):
        files = ["isolinux.bin", menu]
        if mboot:
            files += [mboot]

        for f in files:
            path = self._instroot + "/usr/share/syslinux/" + f

            if not os.path.isfile(path):
                raise CreatorError("syslinux not installed : "
                                   "%s not found" % path)

            shutil.copy(path, isodir + "/isolinux/")

    def __copy_syslinux_background(self, isodest):
        background_path = self._instroot + \
                          "/usr/share/branding/default/syslinux/syslinux-vesa-splash.jpg"

        if not os.path.exists(background_path):
            return False

        shutil.copyfile(background_path, isodest)

        return True

    def __copy_kernel_and_initramfs(self, isodir, version, index):
        bootdir = self._instroot + "/boot"
        isDracut = False

        if self._alt_initrd_name:
            src_initrd_path = os.path.join(bootdir, self._alt_initrd_name)
        else:
            if os.path.exists(bootdir + "/initramfs-" + version + ".img"):
                src_initrd_path = os.path.join(bootdir, "initramfs-" +version+ ".img")
                isDracut = True
            else:
                src_initrd_path = os.path.join(bootdir, "initrd-" +version+ ".img")

        try:
            msger.debug("copy %s to %s" % (bootdir + "/vmlinuz-" + version, isodir + "/isolinux/vmlinuz" + index))
            shutil.copyfile(bootdir + "/vmlinuz-" + version,
                    isodir + "/isolinux/vmlinuz" + index)

            msger.debug("copy %s to %s" % (src_initrd_path, isodir + "/isolinux/initrd" + index + ".img"))
            shutil.copyfile(src_initrd_path,
                            isodir + "/isolinux/initrd" + index + ".img")
        except:
            raise CreatorError("Unable to copy valid kernels or initrds, "
                               "please check the repo.")

        is_xen = False
        if os.path.exists(bootdir + "/xen.gz-" + version[:-3]):
            shutil.copyfile(bootdir + "/xen.gz-" + version[:-3],
                            isodir + "/isolinux/xen" + index + ".gz")
            is_xen = True

        return (is_xen,isDracut)

    def __is_default_kernel(self, kernel, kernels):
        if len(kernels) == 1:
            return True

        if kernel == self._default_kernel:
            return True

        if kernel.startswith("kernel-") and kernel[7:] == self._default_kernel:
            return True

        return False

    def __get_basic_syslinux_config(self, **args):
        return """
default %(menu)s
timeout %(timeout)d

%(background)s
menu title Welcome to %(distroname)s!
menu color border 0 #ffffffff #00000000
menu color sel 7 #ff000000 #ffffffff
menu color title 0 #ffffffff #00000000
menu color tabmsg 0 #ffffffff #00000000
menu color unsel 0 #ffffffff #00000000
menu color hotsel 0 #ff000000 #ffffffff
menu color hotkey 7 #ffffffff #ff000000
menu color timeout_msg 0 #ffffffff #00000000
menu color timeout 0 #ffffffff #00000000
menu color cmdline 0 #ffffffff #00000000
menu hidden
menu clear
""" % args

    def __get_image_stanza(self, is_xen, isDracut, **args):
        if isDracut:
            args["rootlabel"] = "live:CDLABEL=%(fslabel)s" % args
        else:
            args["rootlabel"] = "CDLABEL=%(fslabel)s" % args
        if not is_xen:
            template = """label %(short)s
  menu label %(long)s
  kernel vmlinuz%(index)s
  append initrd=initrd%(index)s.img root=%(rootlabel)s rootfstype=iso9660 %(liveargs)s %(extra)s
"""
        else:
            template = """label %(short)s
  menu label %(long)s
  kernel mboot.c32
  append xen%(index)s.gz --- vmlinuz%(index)s root=%(rootlabel)s rootfstype=iso9660 %(liveargs)s %(extra)s --- initrd%(index)s.img
"""
        return template % args

    def __get_image_stanzas(self, isodir):
        versions = []
        kernels = self._get_kernel_versions()
        for kernel in kernels:
            for version in kernels[kernel]:
                versions.append(version)

        if not versions:
            raise CreatorError("Unable to find valid kernels, "
                               "please check the repo")

        kernel_options = self._get_kernel_options()

        """ menu can be customized highly, the format is:

          short_name1:long_name1:extra_opts1;short_name2:long_name2:extra_opts2

        e.g.: autoinst:InstallationOnly:systemd.unit=installer-graphical.service
        but in order to keep compatible with old format, these are still ok:

              liveinst autoinst
              liveinst;autoinst
              liveinst::;autoinst::
        """
        oldmenus = {"basic": {
                        "short": "basic",
                        "long": "Installation Only (Text based)",
                        "extra": "basic nosplash 4"
                    },
                    "liveinst": {
                        "short": "liveinst",
                        "long": "Installation Only",
                        "extra": "liveinst nosplash 4"
                    },
                    "autoinst": {
                        "short": "autoinst",
                        "long": "Autoinstall (Deletes all existing content)",
                        "extra": "autoinst nosplash 4"
                    },
                    "netinst": {
                        "short": "netinst",
                        "long": "Network Installation",
                        "extra": "netinst 4"
                    },
                    "verify": {
                        "short": "check",
                        "long": "Verify and",
                        "extra": "check"
                    }
                   }
        menu_options = self._get_menu_options()
        menus = menu_options.split(";")
        for i in range(len(menus)):
            menus[i] = menus[i].split(":")
        if len(menus) == 1 and len(menus[0]) == 1:
            """ Keep compatible with the old usage way """
            menus = menu_options.split()
            for i in range(len(menus)):
                menus[i] = [menus[i]]

        cfg = ""

        default_version = None
        default_index = None
        index = "0"
        netinst = None
        for version in versions:
            (is_xen, isDracut) = self.__copy_kernel_and_initramfs(isodir, version, index)
            if index == "0":
                self._isDracut = isDracut

            default = self.__is_default_kernel(kernel, kernels)

            if default:
                long = "Boot %s" % self.distro_name
            elif kernel.startswith("kernel-"):
                long = "Boot %s(%s)" % (self.name, kernel[7:])
            else:
                long = "Boot %s(%s)" % (self.name, kernel)

            oldmenus["verify"]["long"] = "%s %s" % (oldmenus["verify"]["long"],
                                                    long)
            # tell dracut not to ask for LUKS passwords or activate mdraid sets
            if isDracut:
                kern_opts = kernel_options + " rd.luks=0 rd.md=0 rd.dm=0"
            else:
                kern_opts = kernel_options

            cfg += self.__get_image_stanza(is_xen, isDracut,
                                           fslabel = self.fslabel,
                                           liveargs = kern_opts,
                                           long = long,
                                           short = "linux" + index,
                                           extra = "",
                                           index = index)

            if default:
                cfg += "menu default\n"
                default_version = version
                default_index = index

            for menu in menus:
                if not menu[0]:
                    continue
                short = menu[0] + index

                if len(menu) >= 2:
                    long = menu[1]
                else:
                    if menu[0] in oldmenus.keys():
                        if menu[0] == "verify" and not self._has_checkisomd5():
                            continue
                        if menu[0] == "netinst":
                            netinst = oldmenus[menu[0]]
                            continue
                        long = oldmenus[menu[0]]["long"]
                        extra = oldmenus[menu[0]]["extra"]
                    else:
                        long = short.upper() + " X" + index
                        extra = ""

                if len(menu) >= 3:
                    extra = menu[2]

                cfg += self.__get_image_stanza(is_xen, isDracut,
                                               fslabel = self.fslabel,
                                               liveargs = kernel_options,
                                               long = long,
                                               short = short,
                                               extra = extra,
                                               index = index)

            index = str(int(index) + 1)

        if not default_version:
            default_version = versions[0]
        if not default_index:
            default_index = "0"

        if netinst:
            cfg += self.__get_image_stanza(is_xen, isDracut,
                                           fslabel = self.fslabel,
                                           liveargs = kernel_options,
                                           long = netinst["long"],
                                           short = netinst["short"],
                                           extra = netinst["extra"],
                                           index = default_index)

        return cfg

    def __get_memtest_stanza(self, isodir):
        memtest = glob.glob(self._instroot + "/boot/memtest86*")
        if not memtest:
            return ""

        shutil.copyfile(memtest[0], isodir + "/isolinux/memtest")

        return """label memtest
  menu label Memory Test
  kernel memtest
"""

    def __get_local_stanza(self, isodir):
        return """label local
  menu label Boot from local drive
  localboot 0xffff
"""

    def _configure_syslinux_bootloader(self, isodir):
        """configure the boot loader"""
        fs_related.makedirs(isodir + "/isolinux")

        menu = self.__find_syslinux_menu()

        self.__copy_syslinux_files(isodir, menu,
                                   self.__find_syslinux_mboot())

        background = ""
        if self.__copy_syslinux_background(isodir + "/isolinux/splash.jpg"):
            background = "menu background splash.jpg"

        cfg = self.__get_basic_syslinux_config(menu = menu,
                                               background = background,
                                               name = self.name,
                                               timeout = self._timeout * 10,
                                               distroname = self.distro_name)

        cfg += self.__get_image_stanzas(isodir)
        cfg += self.__get_memtest_stanza(isodir)
        cfg += self.__get_local_stanza(isodir)
        cfg += self._get_isolinux_stanzas(isodir)

        cfgf = open(isodir + "/isolinux/isolinux.cfg", "w")
        cfgf.write(cfg)
        cfgf.close()

    def __copy_efi_files(self, isodir):
        if not os.path.exists(self._instroot + "/boot/efi/EFI/redhat/grub.efi"):
            return False
        shutil.copy(self._instroot + "/boot/efi/EFI/redhat/grub.efi",
                    isodir + "/EFI/boot/grub.efi")
        shutil.copy(self._instroot + "/boot/grub/splash.xpm.gz",
                    isodir + "/EFI/boot/splash.xpm.gz")

        return True

    def __get_basic_efi_config(self, **args):
        return """
default=0
splashimage=/EFI/boot/splash.xpm.gz
timeout %(timeout)d
hiddenmenu

""" %args

    def __get_efi_image_stanza(self, **args):
        return """title %(long)s
  kernel /EFI/boot/vmlinuz%(index)s root=CDLABEL=%(fslabel)s rootfstype=iso9660 %(liveargs)s %(extra)s
  initrd /EFI/boot/initrd%(index)s.img
""" %args

    def __get_efi_image_stanzas(self, isodir, name):
        # FIXME: this only supports one kernel right now...

        kernel_options = self._get_kernel_options()
        checkisomd5 = self._has_checkisomd5()

        cfg = ""

        for index in range(0, 9):
            # we don't support xen kernels
            if os.path.exists("%s/EFI/boot/xen%d.gz" %(isodir, index)):
                continue
            cfg += self.__get_efi_image_stanza(fslabel = self.fslabel,
                                               liveargs = kernel_options,
                                               long = name,
                                               extra = "", index = index)
            if checkisomd5:
                cfg += self.__get_efi_image_stanza(
                                               fslabel = self.fslabel,
                                               liveargs = kernel_options,
                                               long = "Verify and Boot " + name,
                                               extra = "check",
                                               index = index)
            break

        return cfg

    def _configure_efi_bootloader(self, isodir):
        """Set up the configuration for an EFI bootloader"""
        fs_related.makedirs(isodir + "/EFI/boot")

        if not self.__copy_efi_files(isodir):
            shutil.rmtree(isodir + "/EFI")
            return

        for f in os.listdir(isodir + "/isolinux"):
            os.link("%s/isolinux/%s" %(isodir, f),
                    "%s/EFI/boot/%s" %(isodir, f))


        cfg = self.__get_basic_efi_config(name = self.name,
                                          timeout = self._timeout)
        cfg += self.__get_efi_image_stanzas(isodir, self.name)

        cfgf = open(isodir + "/EFI/boot/grub.conf", "w")
        cfgf.write(cfg)
        cfgf.close()

        # first gen mactel machines get the bootloader name wrong apparently
        if rpmmisc.getBaseArch() == "i386":
            os.link(isodir + "/EFI/boot/grub.efi",
                    isodir + "/EFI/boot/boot.efi")
            os.link(isodir + "/EFI/boot/grub.conf",
                    isodir + "/EFI/boot/boot.conf")

        # for most things, we want them named boot$efiarch
        efiarch = {"i386": "ia32", "x86_64": "x64"}
        efiname = efiarch[rpmmisc.getBaseArch()]
        os.rename(isodir + "/EFI/boot/grub.efi",
                  isodir + "/EFI/boot/boot%s.efi" %(efiname,))
        os.link(isodir + "/EFI/boot/grub.conf",
                isodir + "/EFI/boot/boot%s.conf" %(efiname,))


    def _configure_bootloader(self, isodir):
        self._configure_syslinux_bootloader(isodir)
        self._configure_efi_bootloader(isodir)

arch = "i386"
if arch in ("i386", "x86_64"):
    LiveCDImageCreator = x86LiveImageCreator
elif arch.startswith("arm"):
    LiveCDImageCreator = LiveImageCreatorBase
else:
    raise CreatorError("Architecture not supported!")
