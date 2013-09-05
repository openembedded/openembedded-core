DESCRIPTION = "Run postinstall scripts on device"
SECTION = "devel"
PR = "r9"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://run-postinsts"

INITSCRIPT_NAME = "run-postinsts"
INITSCRIPT_PARAMS = "start 99 S ."

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

	sed -i -e 's:#SYSCONFDIR#:${sysconfdir}:g' ${D}${sysconfdir}/init.d/run-postinsts
}
