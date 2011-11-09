SUMMARY = "GRUB2 is the next-generation GRand Unified Bootloader"

DESCRIPTION = "GRUB2 is the next generaion of a GPLed bootloader \
intended to unify bootloading across x86 operating systems. In \
addition to loading the Linux kernel, it implements the Multiboot \
standard, which allows for flexible loading of multiple boot images."

HOMEPAGE = "http://www.gnu.org/software/grub/"
SECTION = "bootloaders"
PRIORITY = "optional"

LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

RDEPENDS_${PN} = "diffutils freetype"
PR = "r2"

SRC_URI = "ftp://ftp.gnu.org/gnu/grub/grub-${PV}.tar.gz \
          file://grub-install.in.patch;apply=yes \
          file://40_custom"

SRC_URI[md5sum] = "ca9f2a2d571b57fc5c53212d1d22e2b5"
SRC_URI[sha256sum] = "b91f420f2c51f6155e088e34ff99bea09cc1fb89585cf7c0179644e57abd28ff"

inherit autotools
inherit gettext

EXTRA_OECONF = "--with-platform=pc --disable-grub-mkfont --target=${TARGET_ARCH} --program-prefix="""

do_configure() {
    oe_runconf
}

python __anonymous () {
    import re
    host = d.getVar('HOST_SYS', 1)
    if not re.match('x86.64.*-linux', host) and not re.match('i.86.*-linux', host):
        raise bb.parse.SkipPackage("incompatible with host %s" % host)
}

do_install_append () {
    install -m 0755 ${WORKDIR}/40_custom ${D}${sysconfdir}/grub.d/40_custom
}

FILES_${PN}-doc = "${datadir}"
FILES_${PN} = "/usr /etc"
INSANE_SKIP_${PN} = "arch"
