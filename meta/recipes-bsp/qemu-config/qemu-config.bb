DESCRIPTION = "Adds scripts to use distcc on the host system under qemu"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"


COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemumips|qemuppc)"

PR = "r25"

SRC_URI = "file://distcc.sh \
           file://exports \
           file://shutdown.desktop \
           file://qemu-autostart"

S = "${WORKDIR}"

do_install() {
    install -d ${D}${sysconfdir}/profile.d

    install -m 0755 distcc.sh ${D}${sysconfdir}/profile.d/
    install -m 0644 exports ${D}${sysconfdir}/
    
    install -d ${D}${datadir}/applications
    install -m 0644 shutdown.desktop ${D}${datadir}/applications/

    install -d ${D}${sysconfdir}/init.d
    install qemu-autostart ${D}${sysconfdir}/init.d/
}

pkg_postinst_${PN} () {
    grep -q qemuarm $D${sysconfdir}/hostname && \
        sed -i $D${datadir}/applications/shutdown.desktop -e 's/^Exec=halt/Exec=reboot/'
}

RDEPENDS_${PN} = "distcc packagegroup-core-nfs-server oprofileui-server bash"

inherit update-rc.d allarch

INITSCRIPT_NAME = "qemu-autostart"
INITSCRIPT_PARAMS = "start 999 5 2 . stop 20 0 1 6 ."
