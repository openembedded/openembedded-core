DESCRIPTION = "Meta package for building a standalone python tarball"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

DEPENDS = "opkg-native opkg-utils-native virtual/fakeroot-native sed-native"

PR = "r1"

inherit meta

SDK_DIR = "${WORKDIR}/sdk"
SDK_OUTPUT = "${SDK_DIR}/image"
SDK_DEPLOY = "${TMPDIR}/deploy/sdk"

IPKG_HOST = "opkg-cl -f ${IPKGCONF_SDK} -o ${SDK_OUTPUT}"

TOOLCHAIN_HOST_TASK ?= "\
    nativesdk-python-core \
    nativesdk-python-textutils \
    nativesdk-python-sqlite3 \
    nativesdk-python-pickle \
    nativesdk-python-logging \
    nativesdk-python-elementtree \
    nativesdk-python-curses \
    nativesdk-python-compile \
    nativesdk-python-compiler \
    nativesdk-python-fcntl \
    nativesdk-python-shell \
    nativesdk-python-misc \
    nativesdk-python-multiprocessing \
    nativesdk-python-subprocess \
    nativesdk-python-xmlrpc \
    nativesdk-python-netclient \
    nativesdk-python-netserver \
    nativesdk-python-distutils \
    nativesdk-chrpath \
    "

TOOLCHAIN_OUTPUTNAME ?= "python-nativesdk-standalone-${SDKMACHINE}"

RDEPENDS = "${TOOLCHAIN_HOST_TASK}"

EXCLUDE_FROM_WORLD = "1"

do_populate_sdk() {
	rm -rf ${SDK_OUTPUT}
	mkdir -p ${SDK_OUTPUT}
	mkdir -p ${SDK_OUTPUT}${localstatedir}/lib/opkg/

	rm -f ${IPKGCONF_TARGET}
	touch ${IPKGCONF_TARGET}
	rm -f ${IPKGCONF_SDK}
	touch ${IPKGCONF_SDK}

	package_update_index_ipk
	package_generate_ipkg_conf

	for arch in ${PACKAGE_ARCHS}; do
		revipkgarchs="$arch $revipkgarchs"
	done

	${IPKG_HOST} update
	${IPKG_HOST} install ${TOOLCHAIN_HOST_TASK}

	install -d ${SDK_OUTPUT}/${SDKPATHNATIVE}${localstatedir_nativesdk}/lib/opkg
	mv ${SDK_OUTPUT}/var/lib/opkg/* ${SDK_OUTPUT}/${SDKPATHNATIVE}${localstatedir_nativesdk}/lib/opkg/
	rm -Rf ${SDK_OUTPUT}/var

	install -d ${SDK_OUTPUT}/${SDKPATHNATIVE}/${sysconfdir}
	install -m 0644 ${IPKGCONF_SDK} ${SDK_OUTPUT}/${SDKPATHNATIVE}/${sysconfdir}/

	rm -f ${SDK_OUTPUT}/${SDKPATHNATIVE}${libdir_nativesdk}/*.la

	# Link the ld.so.cache file into the hosts filesystem
	ln -s /etc/ld.so.cache ${SDK_OUTPUT}/${SDKPATHNATIVE}/etc/ld.so.cache

	# Add version information
	versionfile=${SDK_OUTPUT}/${SDKPATH}/version-${MULTIMACH_TARGET_SYS}
	touch $versionfile
	echo 'Distro: ${DISTRO}' >> $versionfile
	echo 'Distro Version: ${DISTRO_VERSION}' >> $versionfile
	echo 'Metadata Revision: ${METADATA_REVISION}' >> $versionfile
	echo 'Timestamp: ${DATETIME}' >> $versionfile

	# Package it up
	mkdir -p ${SDK_DEPLOY}
	cd ${SDK_OUTPUT}
	tar  --owner=root --group=root -cj --file=${SDK_DEPLOY}/${TOOLCHAIN_OUTPUTNAME}.tar.bz2 .
}

do_populate_sdk[nostamp] = "1"
do_populate_sdk[recrdeptask] = "do_package_write"
addtask populate_sdk before do_build after do_install

inherit blacklist

PNBLACKLIST[external-python-tarball] = "${@base_contains('PACKAGE_CLASSES', 'package_ipk', '', 'This recipe requires \'package_ipk\' support to be enabled in PACKAGE_CLASSES.', d)}"
