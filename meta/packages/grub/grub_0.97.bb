DESCRIPTION = "GRUB is the GRand Unified Bootloader"
HOMEPAGE = "http://www.gnu.org/software/grub"
SECTION = "bootloaders"
PRIORITY = "optional"
RDEPENDS = "diffutils"
PR = "r2"

SRC_URI = "ftp://alpha.gnu.org/gnu/grub/grub-${PV}.tar.gz; \
	   file://autohell.patch;patch=1 "

inherit autotools

python __anonymous () {
    import re
    host = bb.data.getVar('HOST_SYS', d, 1)
    if not re.match('i.86.*-linux', host):
        raise bb.parse.SkipPackage("incompatible with host %s" % host)
}

do_install_append_vmware() {
	mkdir -p ${D}/boot/
	ln -sf ../usr/lib/grub/{$TARGET_ARCH}{$TARGET_VENDOR}/ ${D}/boot/grub
}

FILES_${PN}-doc = "${datadir}"
FILES_${PN} = "/boot /usr"
