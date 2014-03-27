SUMMARY = "Libraries for producing EFI binaries"
HOMEPAGE = "http://sourceforge.net/projects/gnu-efi/"
SECTION = "devel"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://debian/copyright;md5=5fb358a180f484b285b0d99acdc29666"

SRC_URI = "http://downloads.sourceforge.net/gnu-efi/gnu-efi_3.0u.orig.tar.gz \
           file://parallel-make.patch \
           file://parallel-make-archives.patch \
          "
SRC_URI[md5sum] = "d15d3c700e79a1e2938544d73edc572d"
SRC_URI[sha256sum] = "3c0d450d5829204ca05dcb3b2aae772e52c379b7c7e09146759c6315606f934e"

COMPATIBLE_HOST = "(x86_64.*|i.86.*)-linux"

S = "${WORKDIR}/gnu-efi-3.0"

def gnu_efi_arch(d):
    import re
    tarch = d.getVar("TARGET_ARCH", True)
    if re.match("i[3456789]86", tarch):
        return "ia32"
    return tarch

EXTRA_OEMAKE = "'ARCH=${@gnu_efi_arch(d)}' 'CC=${CC}' 'AS=${AS}' 'LD=${LD}' 'AR=${AR}' \
                'RANLIB=${RANLIB}' 'OBJCOPY=${OBJCOPY}' 'PREFIX=${prefix}' 'LIBDIR=${libdir}' \
                "

do_install() {
        oe_runmake install INSTALLROOT="${D}"
}

FILES_${PN} += "${libdir}/*.lds"
