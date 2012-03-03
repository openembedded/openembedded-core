SUMMARY = "GRUB is the GRand Unified Bootloader"
DESCRIPTION = "GRUB is a GPLed bootloader intended to unify bootloading across x86 \
operating systems. In addition to loading the Linux kernel, it implements the Multiboot \
standard, which allows for flexible loading of multiple boot images."
HOMEPAGE = "http://www.gnu.org/software/grub/"
SECTION = "bootloaders"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=c93c0550bd3173f4504b2cbd8991e50b \
                    file://grub/main.c;beginline=3;endline=9;md5=22a5f28d2130fff9f2a17ed54be90ed6"

RDEPENDS_${PN} = "diffutils"
PR = "r4"

SRC_URI = "ftp://alpha.gnu.org/gnu/grub/grub-${PV}.tar.gz; \
            file://no-reorder-functions.patch \
            file://autohell.patch "

SRC_URI[md5sum] = "cd3f3eb54446be6003156158d51f4884"
SRC_URI[sha256sum] = "4e1d15d12dbd3e9208111d6b806ad5a9857ca8850c47877d36575b904559260b"

inherit autotools

python __anonymous () {
    import re
    host = d.getVar('HOST_SYS', True)
    if not re.match('i.86.*-linux', host):
        raise bb.parse.SkipPackage("incompatible with host %s" % host)
}

do_install_append_vmware() {
	mkdir -p ${D}/boot/
	ln -sf ../usr/lib/grub/{$TARGET_ARCH}{$TARGET_VENDOR}/ ${D}/boot/grub
}

FILES_${PN}-doc = "${datadir}"
FILES_${PN} = "/boot /usr"
