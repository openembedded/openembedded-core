DESCRIPTION = "Meta package for building a standalone python tarball"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

DEPENDS = "opkg-native opkg-utils-native virtual/fakeroot-native sed-native"

inherit meta

SDK_DIR = "${WORKDIR}/sdk"
SDK_OUTPUT = "${SDK_DIR}/image"
SDK_DEPLOY = "${TMPDIR}/deploy/sdk"

IPKG_HOST = "opkg-cl -f ${IPKGCONF_SDK} -o ${SDK_OUTPUT}"

TOOLCHAIN_HOST_TASK ?= "\
    python-nativesdk-core \
    python-nativesdk-textutils \
    python-nativesdk-sqlite3 \
    python-nativesdk-pickle \
    python-nativesdk-logging \
    python-nativesdk-elementtree \
    python-nativesdk-curses \
    python-nativesdk-compile \
    python-nativesdk-compiler \
    python-nativesdk-fcntl \
    python-nativesdk-shell \
    python-nativesdk-misc \
    python-nativesdk-multiprocessing \
    python-nativesdk-subprocess \
    python-nativesdk-xmlrpc \
    python-nativesdk-netclient \
    python-nativesdk-netserver \
    python-nativesdk-distutils \
    chrpath-nativesdk \
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

