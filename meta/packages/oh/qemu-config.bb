DESCRIPTION = "Adds scripts to use distcc on the host system under qemu"
LICENSE = "GPL"
RDEPENDS = "distcc task-oh-nfs-server"
PR = "r2"

SRC_URI = "file://distcc.sh \
           file://exports"

S = "${WORKDIR}"

COMPATIBLE_MACHINE = "(qemuarm|qemux86)"
PACKAGE_ARCH = "all"

do_install() {
    install -d ${D}${sysconfdir}/profile.d

    install -m 0755 distcc.sh ${D}${sysconfdir}/profile.d/
    install -m 0644 exports ${D}${sysconfdir}
}    
