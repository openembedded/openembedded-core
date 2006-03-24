DESCRIPTION = "Encrypted shellscript for the Zaurus ROM update"
DEPENDS = "encdec-updater-native"
LICENSE = "zaurus-updater"
PR = "r3"

PACKAGES = ""
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "file://updater.sh \
           file://gnu-tar.gz"
S = "${WORKDIR}"

do_compile() {
	encdec-updater -e updater.sh
}

do_deploy() {
	install -d ${DEPLOY_DIR}/images/
	install -m 0755 updater.sh ${DEPLOY_DIR_IMAGE}/updater.sh.${MACHINE}

	case ${MACHINE} in
		spitz | borzoi )
			install -m 0755 gnu-tar ${DEPLOY_DIR_IMAGE}/gnu-tar
			;;
        	*)
			;;
	esac
}

addtask deploy before do_build after do_compile
