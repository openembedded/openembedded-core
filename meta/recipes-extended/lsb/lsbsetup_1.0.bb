DESCRIPTION = "auto-setup environment for lsb test"
SECTION = "console/utils"
LICENSE = "GPLv2"
PR = "r3"

LIC_FILES_CHKSUM = "file://LSB_Setup.sh;beginline=3;endline=16;md5=97451c7c0786ce5bbe9ac58042945583"

SRC_URI = "file://LSB_Setup.sh"

S = "${WORKDIR}"

do_install() {
    # Only install file if it has a contents
    install -d ${D}/usr/bin
    install -d ${D}/${sysconfdir}
    install -m 0755 ${S}/LSB_Setup.sh ${D}/usr/bin
    install -d  ${D}/${libdir}/lsb
    ln -sf ${base_sbindir}/chkconfig ${D}/${libdir}/lsb/install_initd
    ln -sf ${base_sbindir}/chkconfig ${D}/${libdir}/lsb/remove_initd
    ln -sf ${sbindir}/sendmail ${D}/${libdir}/sendmail
    ln -sf ${datadir}/zoneinfo/Asia/Hong_Kong ${D}${sysconfdir}/localtime
}

FILES_${PN} += "${libdir}/lsb \
               ${libdir}/* \
               "
