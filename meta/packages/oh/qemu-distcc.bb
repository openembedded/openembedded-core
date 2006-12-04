DESCRIPTION = "Adds scripts to use distcc on the host system under qemu"
LICENSE = "GPL"
RDEPENDS = "distcc"

SRC_URI = "file://distcc.sh"
S = "${WORKDIR}"

COMPATIBLE_MACHINE = "(qemuarm|qemux86)"
PACKAGE_ARCH = "all"

do_install() {
    install -d ${D}/etc
    install -d ${D}/etc/profile.d
    install distcc.sh ${D}/etc/profile.d/
}    
