DESCRIPTION = "Meta package for building a installable toolchain"
LICENSE = "MIT"
DEPENDS = "opkg-native opkg-utils-native fakeroot-native sed-native"

inherit meta

SDK_DIR = "${WORKDIR}/sdk"
SDK_OUTPUT = "${SDK_DIR}/image"
SDK_DEPLOY = "${TMPDIR}/deploy/sdk"

SDKTARGETSYSROOT = "${SDKPATH}/sysroots/${TARGET_SYS}"

IPKG_HOST = "opkg-cl -f ${IPKGCONF_SDK} -o ${SDK_OUTPUT}"
IPKG_TARGET = "opkg-cl -f ${IPKGCONF_TARGET} -o ${SDK_OUTPUT}/${SDKTARGETSYSROOT}"

TOOLCHAIN_HOST_TASK ?= "task-sdk-host"
TOOLCHAIN_TARGET_TASK ?= "task-poky-standalone-sdk-target task-poky-standalone-sdk-target-dbg"
TOOLCHAIN_OUTPUTNAME ?= "${SDK_NAME}-toolchain-${DISTRO_VERSION}"

RDEPENDS = "${TOOLCHAIN_TARGET_TASK} ${TOOLCHAIN_HOST_TASK}"

EXCLUDE_FROM_WORLD = "1"

do_populate_sdk() {
	rm -rf ${SDK_OUTPUT}
	mkdir -p ${SDK_OUTPUT}
	mkdir -p ${SDK_OUTPUT}${libdir}/opkg/
	mkdir -p ${SDK_OUTPUT}/${SDKTARGETSYSROOT}${libdir}/opkg/

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

	${IPKG_TARGET} update
	${IPKG_TARGET} install ${TOOLCHAIN_TARGET_TASK}

	install -d ${SDK_OUTPUT}/${SDKPATHNATIVE}${libdir_nativesdk}/opkg
	mv ${SDK_OUTPUT}/usr/lib/opkg/* ${SDK_OUTPUT}/${SDKPATHNATIVE}${libdir_nativesdk}/opkg/
	rm -Rf ${SDK_OUTPUT}/usr

	# Don't ship any libGL in the SDK
	rm -rf ${SDK_OUTPUT}/${SDKPATHNATIVE}${libdir_nativesdk}/libGL*

	install -d ${SDK_OUTPUT}/${SDKTARGETSYSROOT}/${sysconfdir}
	install -m 0644 ${IPKGCONF_TARGET} ${IPKGCONF_SDK} ${SDK_OUTPUT}/${SDKTARGETSYSROOT}/${sysconfdir}/

	install -d ${SDK_OUTPUT}/${SDKPATHNATIVE}/${sysconfdir}
	install -m 0644 ${IPKGCONF_SDK} ${SDK_OUTPUT}/${SDKPATHNATIVE}/${sysconfdir}/

	# Can copy pstage files here
	# target_pkgs=`cat ${SDK_OUTPUT}/${SDKTARGETSYSROOT}/usr/lib/opkg/status | grep Package: | cut -f 2 -d ' '`

	# Fix or remove broken .la files
	for i in `find ${SDK_OUTPUT}/${SDKTARGETSYSROOT} -name \*.la`; do
		sed -i 	-e "/^dependency_libs=/s,\([[:space:]']\)${base_libdir},\1${SDKTARGETSYSROOT}${base_libdir},g" \
			-e "/^dependency_libs=/s,\([[:space:]']\)${libdir},\1${SDKTARGETSYSROOT}${libdir},g" \
			-e "/^dependency_libs=/s,\-\([LR]\)${base_libdir},-\1${SDKTARGETSYSROOT}${base_libdir},g" \
			-e "/^dependency_libs=/s,\-\([LR]\)${libdir},-\1${SDKTARGETSYSROOT}${libdir},g" \
			-e 's/^installed=yes$/installed=no/' $i
	done
	#rm -f ${SDK_OUTPUT}/${SDKPATHNATIVE}/lib/*.la
	rm -f ${SDK_OUTPUT}/${SDKPATHNATIVE}${libdir_nativesdk}/*.la

	# Setup site file for external use
	siteconfig=${SDK_OUTPUT}/${SDKPATH}/site-config-${TARGET_SYS}
	touch $siteconfig
	for sitefile in ${CONFIG_SITE} ; do
		cat $sitefile >> $siteconfig
	done

	# Create environment setup script
	script=${SDK_OUTPUT}/${SDKPATH}/environment-setup-${TARGET_SYS}
	touch $script
	echo 'export PATH=${SDKPATHNATIVE}${bindir_nativesdk}:$PATH' >> $script
	echo 'export PKG_CONFIG_SYSROOT_DIR=${SDKTARGETSYSROOT}' >> $script
	echo 'export PKG_CONFIG_PATH=${SDKTARGETSYSROOT}${libdir}/pkgconfig' >> $script
	echo 'export CONFIG_SITE=${SDKPATH}/site-config-${TARGET_SYS}' >> $script
	echo 'export CC=${TARGET_PREFIX}gcc' >> $script
	echo 'export CONFIGURE_FLAGS="--target=${TARGET_SYS} --host=${TARGET_SYS} --build=${SDK_ARCH}-linux"' >> $script
	if [ "${TARGET_OS}" = "darwin8" ]; then
		echo 'export TARGET_CFLAGS="-I${SDKTARGETSYSROOT}${includedir}"' >> $script
		echo 'export TARGET_LDFLAGS="-L${SDKTARGETSYSROOT}${libdir}"' >> $script
		# Workaround darwin toolchain sysroot path problems
		cd ${SDK_OUTPUT}${SDKTARGETSYSROOT}/usr
		ln -s /usr/local local
	fi
	echo "alias opkg='LD_LIBRARY_PATH=${SDKPATHNATIVE}${libdir_nativesdk} ${SDKPATHNATIVE}${bindir_nativesdk}/opkg-cl -f ${SDKPATHNATIVE}/${sysconfdir}/opkg-sdk.conf -o ${SDKPATHNATIVE}'" >> $script
	echo "alias opkg-target='LD_LIBRARY_PATH=${SDKPATHNATIVE}${libdir_nativesdk} ${SDKPATHNATIVE}${bindir_nativesdk}/opkg-cl -f ${SDKTARGETSYSROOT}${sysconfdir}/opkg.conf -o ${SDKTARGETSYSROOT}'" >> $script

	# Add version information
	versionfile=${SDK_OUTPUT}/${SDKPATH}/version-${TARGET_SYS}
	touch $versionfile
	echo 'Distro: ${DISTRO}' >> $versionfile
	echo 'Distro Version: ${DISTRO_VERSION}' >> $versionfile
	echo 'Metadata Revision: ${METADATA_REVISION}' >> $versionfile
	echo 'Timestamp: ${DATETIME}' >> $versionfile

	# Package it up
	mkdir -p ${SDK_DEPLOY}
	cd ${SDK_OUTPUT}
	fakeroot tar cfj ${SDK_DEPLOY}/${TOOLCHAIN_OUTPUTNAME}.tar.bz2 .
}

do_populate_sdk[nostamp] = "1"
do_populate_sdk[recrdeptask] = "do_package_write"
addtask populate_sdk before do_build after do_install
