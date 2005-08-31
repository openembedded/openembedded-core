DESCRIPTION = "Encrypted shellscript for the Zaurus ROM update"
DEPENDS = "encdec-updater-native"
LICENSE = "zaurus-updater"
PR = "r2"

SRC_URI = "file://updater.sh"
S = "${WORKDIR}"

do_compile() {
	encdec-updater -e updater.sh
}

do_deploy() {
	install -d ${DEPLOY_DIR}/images/
	install -m 0755 updater.sh ${DEPLOY_DIR}/images/updater.sh.${MACHINE}
}

addtask deploy before do_build after do_compile
