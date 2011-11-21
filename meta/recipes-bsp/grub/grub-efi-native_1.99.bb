SUMMARY = "GRUB2 is the next-generation GRand Unified Bootloader"

DESCRIPTION = "GRUB2 is the next generaion of a GPLed bootloader \
intended to unify bootloading across x86 operating systems. In \
addition to loading the Linux kernel, it implements the Multiboot \
standard, which allows for flexible loading of multiple boot images. \
This recipe builds an EFI binary for the target. It does not install \
or package anything, it only deploys a target-arch GRUB EFI image."

HOMEPAGE = "http://www.gnu.org/software/grub/"
SECTION = "bootloaders"
PRIORITY = "optional"

LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

# FIXME: We should be able to optionally drop freetype as a dependency
DEPENDS = "help2man-native"
RDEPENDS_${PN} = "diffutils freetype"
PR = "r1"

# Native packages do not normally rebuild when the target changes.
# Ensure this is built once per HOST-TARGET pair.
PN := "grub-efi-${TRANSLATED_TARGET_ARCH}-native"

SRC_URI = "ftp://ftp.gnu.org/gnu/grub/grub-${PV}.tar.gz"

SRC_URI[md5sum] = "ca9f2a2d571b57fc5c53212d1d22e2b5"
SRC_URI[sha256sum] = "b91f420f2c51f6155e088e34ff99bea09cc1fb89585cf7c0179644e57abd28ff"

COMPATIBLE_HOST = '(x86_64.*|i.86.*)-(linux|freebsd.*)'

S = "${WORKDIR}/grub-${PV}"

# Determine the target arch for the grub modules before the native class
# clobbers TARGET_ARCH.
ORIG_TARGET_ARCH := ${TARGET_ARCH}
python __anonymous () {
    import re
    target = d.getVar('ORIG_TARGET_ARCH', True)
    if target == "x86_64":
        grubtarget = 'x86_64'
        grubimage = "bootx64.efi"
    elif re.match('i.86', target):
        grubtarget = 'i386'
        grubimage = "bootia32.efi"
    else:
        raise bb.parse.SkipPackage("grub-efi is incompatible with target %s" % target)
    d.setVar("GRUB_TARGET", grubtarget)
    d.setVar("GRUB_IMAGE", grubimage)
}

inherit autotools
inherit gettext
inherit native
inherit deploy

EXTRA_OECONF = "--with-platform=efi --disable-grub-mkfont \
                --target=${GRUB_TARGET} --enable-efiemu=no --program-prefix=''"

do_mkimage() {
	./grub-mkimage -p / -d ./grub-core/ \
		       -O ${GRUB_TARGET}-efi -o ./${GRUB_IMAGE} \
	               boot linux fat serial part_msdos normal
}
addtask mkimage after do_compile before do_install

do_deploy() {
	install -m 644 ${S}/${GRUB_IMAGE} ${DEPLOYDIR}
}
addtask deploy after do_install before do_build

do_install[noexec] = "1"
do_populate_sysroot[noexec] = "1"
