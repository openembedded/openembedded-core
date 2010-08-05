DESCRIPTION = "Encrypted shellscript for the Zaurus ROM update"
DEPENDS = "encdec-updater-native"
LICENSE = "zaurus-updater"
PR = "r20"

PACKAGES = ""
PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = '(poodle|c7x0|spitz|akita|tosa)'

SRC_URI = "file://updater.sh \
           file://gnu-tar.gz"
S = "${WORKDIR}"

inherit deploy

do_configure() {
	sed -i "s/ZAURUS_UPDATER_VERSION/${PR}/" "${S}/updater.sh"
}

do_compile() {
	encdec-updater -e updater.sh
}

do_deploy() {
	install -m 0755 updater.sh ${DEPLOYDIR}/updater.sh.${MACHINE}

	case ${MACHINE} in
		spitz )
			install -m 0755 gnu-tar ${DEPLOYDIR}/gnu-tar
			;;
        	*)
			;;
	esac
}

addtask deploy before do_populate_sysroot after do_compile
