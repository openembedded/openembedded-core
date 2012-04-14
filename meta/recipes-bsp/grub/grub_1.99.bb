SUMMARY = "GRUB2 is the next-generation GRand Unified Bootloader"

DESCRIPTION = "GRUB2 is the next generaion of a GPLed bootloader \
intended to unify bootloading across x86 operating systems. In \
addition to loading the Linux kernel, it implements the Multiboot \
standard, which allows for flexible loading of multiple boot images."

HOMEPAGE = "http://www.gnu.org/software/grub/"
SECTION = "bootloaders"

LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS = "autogen-native"
RDEPENDS_${PN} = "diffutils freetype"
PR = "r7"

SRC_URI = "ftp://ftp.gnu.org/gnu/grub/grub-${PV}.tar.gz \
          file://grub-install.in.patch \
          file://grub-1.99_fix_for_automake_1.11.2.patch \
          file://grub-1.99-fpmath-sse-387-fix.patch \
          file://grub-1.99-gcc-4.7.0.patch \
          file://grub-1.99-gcc-4.7.0-uninitialized-var-errors.patch \
          file://grub-1.99-gcc-4.7.0-strict-aliasing-errors.patch \
          file://grub-1.99-fix-enable_execute_stack-check.patch \
          file://40_custom"

SRC_URI[md5sum] = "ca9f2a2d571b57fc5c53212d1d22e2b5"
SRC_URI[sha256sum] = "b91f420f2c51f6155e088e34ff99bea09cc1fb89585cf7c0179644e57abd28ff"

COMPATIBLE_HOST = '(x86_64.*|i.86.*)-(linux|freebsd.*)'

inherit autotools
inherit gettext

EXTRA_OECONF = "--with-platform=pc --disable-grub-mkfont --program-prefix="""

do_configure() {
    oe_runconf
}

do_install_append () {
    install -d ${D}${sysconfdir}/grub.d
    install -m 0755 ${WORKDIR}/40_custom ${D}${sysconfdir}/grub.d/40_custom
}

FILES_${PN}-doc = "${datadir}"
FILES_${PN} = "/usr /etc"
INSANE_SKIP_${PN} = "arch"
