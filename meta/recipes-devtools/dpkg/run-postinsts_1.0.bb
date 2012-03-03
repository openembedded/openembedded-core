DESCRIPTION = "Run postinstall scripts on device using awk"
SECTION = "devel"
PR = "r8"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://run-postinsts file://run-postinsts.awk"

INITSCRIPT_NAME = "run-postinsts"
INITSCRIPT_PARAMS = "start 98 S ."

inherit update-rc.d

do_configure() {
	:
}

do_compile () {
	:
}

do_install() {
	install -d ${D}${sysconfdir}/init.d/
	install -m 0755 ${WORKDIR}/run-postinsts ${D}${sysconfdir}/init.d/

	install -d ${D}${datadir}/${PN}/
	install -m 0644 ${WORKDIR}/run-postinsts.awk ${D}${datadir}/${PN}/
}
