DESCRIPTION = "Adds scripts to use distcc on the host system under qemu"
LICENSE = "GPL"
RDEPENDS = "distcc task-poky-nfs-server fakeroot"
PR = "r5"

SRC_URI = "file://distcc.sh \
           file://anjuta-remote-run \
           file://exports"

S = "${WORKDIR}"

COMPATIBLE_MACHINE = "(qemuarm|qemux86)"
PACKAGE_ARCH = "all"

do_install() {
    install -d ${D}${sysconfdir}/profile.d

    install -m 0755 distcc.sh ${D}${sysconfdir}/profile.d/
    install -m 0644 exports ${D}${sysconfdir}/
    
    install -d ${D}${bindir}
    install -m 0755 anjuta-remote-run ${D}${bindir}/
}    
