DESCRIPTION = "Adds scripts to use distcc on the host system under qemu"
LICENSE = "GPL"
RDEPENDS = "distcc task-poky-nfs-server fakeroot"
PR = "r6"

SRC_URI = "file://distcc.sh \
           file://anjuta-remote-run \
           file://exports \
	   file://shutdown.desktop \
	   file://shutdown.png"

S = "${WORKDIR}"

COMPATIBLE_MACHINE = "(qemuarm|qemux86)"
PACKAGE_ARCH = "all"

do_install() {
    install -d ${D}${sysconfdir}/profile.d

    install -m 0755 distcc.sh ${D}${sysconfdir}/profile.d/
    install -m 0644 exports ${D}${sysconfdir}/
    
    install -d ${D}${bindir}
    install -m 0755 anjuta-remote-run ${D}${bindir}/
    
    install -d ${D}${datadir}/applications
    install -m 0644 shutdown.desktop ${D}${datadir}/applications/

    install -d ${D}${datadir}/pixmaps
    install -m 0644 shutdown.png ${D}${datadir}/pixmaps/
}    
