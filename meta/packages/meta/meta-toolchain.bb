DESCRIPTION = "Meta package for building a installable toolchain"
LICENSE = "MIT"
DEPENDS = "opkg-native opkg-utils-native fakeroot-native sed-native"

inherit meta

SDK_DIR = "${WORKDIR}/sdk"
SDK_OUTPUT = "${SDK_DIR}/image"
SDK_OUTPUT2 = "${SDK_DIR}/image-extras"
SDK_DEPLOY = "${TMPDIR}/deploy/sdk"

IPKG_HOST = "opkg-cl -f ${IPKGCONF_SDK} -o ${SDK_OUTPUT}"
IPKG_TARGET = "opkg-cl -f ${IPKGCONF_TARGET} -o ${SDK_OUTPUT}/${SDKPATH}/${TARGET_SYS}"

TOOLCHAIN_HOST_TASK ?= "task-sdk-host"
TOOLCHAIN_TARGET_TASK ?= "task-poky-standalone-sdk-target task-poky-standalone-sdk-target-dbg"
TOOLCHAIN_OUTPUTNAME ?= "${SDK_NAME}-toolchain-${DISTRO_VERSION}"

RDEPENDS = "${TOOLCHAIN_TARGET_TASK} ${TOOLCHAIN_HOST_TASK}"

EXCLUDE_FROM_WORLD = "1"

do_populate_sdk() {
	rm -rf ${SDK_OUTPUT}
	rm -rf ${SDK_OUTPUT2}
	mkdir -p ${SDK_OUTPUT}
	mkdir -p ${SDK_OUTPUT}${libdir}/opkg/
	mkdir -p ${SDK_OUTPUT}/${SDKPATH}/${TARGET_SYS}${libdir}/opkg/

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

	install -d ${SDK_OUTPUT}/${SDKPATH}/usr/lib/opkg
	mv ${SDK_OUTPUT}/usr/lib/opkg/* ${SDK_OUTPUT}/${SDKPATH}/usr/lib/opkg/
	rm -Rf ${SDK_OUTPUT}/usr/lib

	install -d ${SDK_OUTPUT}/${SDKPATH}/${TARGET_SYS}/${sysconfdir}
	install -m 0644 ${IPKGCONF_TARGET} ${IPKGCONF_SDK} ${SDK_OUTPUT}/${SDKPATH}/${TARGET_SYS}/${sysconfdir}/

	install -d ${SDK_OUTPUT}/${SDKPATH}/${sysconfdir}
	install -m 0644 ${IPKGCONF_SDK} ${SDK_OUTPUT}/${SDKPATH}/${sysconfdir}/

	# extract and store ipks, pkgdata and shlibs data
	target_pkgs=`cat ${SDK_OUTPUT}/${SDKPATH}/${TARGET_SYS}/usr/lib/opkg/status | grep Package: | cut -f 2 -d ' '`
	mkdir -p ${SDK_OUTPUT2}/${SDKPATH}/ipk/
	mkdir -p ${SDK_OUTPUT2}/${SDKPATH}/pkgdata/runtime/
	mkdir -p ${SDK_OUTPUT2}/${SDKPATH}/${TARGET_SYS}/shlibs/
	for pkg in $target_pkgs ; do
		for arch in $revipkgarchs; do
			pkgnames=${DEPLOY_DIR_IPK}/$arch/${pkg}_*_$arch.ipk
			if [ -e $pkgnames ]; then
				echo "Found $pkgnames"
				cp $pkgnames ${SDK_OUTPUT2}/${SDKPATH}/ipk/
				orig_pkg=`opkg-list-fields $pkgnames | grep OE: | cut -d ' ' -f2`
				pkg_subdir=$arch${TARGET_VENDOR}${@['-' + bb.data.getVar('TARGET_OS', d, 1), ''][bb.data.getVar('TARGET_OS', d, 1) == ('' or 'custom')]}
				mkdir -p ${SDK_OUTPUT2}/${SDKPATH}/pkgdata/$pkg_subdir/runtime
				cp ${TMPDIR}/pkgdata/$pkg_subdir/$orig_pkg ${SDK_OUTPUT2}/${SDKPATH}/pkgdata/$pkg_subdir/
				subpkgs=`cat ${TMPDIR}/pkgdata/$pkg_subdir/$orig_pkg | grep PACKAGES: | cut -b 10-`
				for subpkg in $subpkgs; do
					cp ${TMPDIR}/pkgdata/$pkg_subdir/runtime/$subpkg ${SDK_OUTPUT2}/${SDKPATH}/pkgdata/$pkg_subdir/runtime/
					if [ -e ${TMPDIR}/pkgdata/$pkg_subdir/runtime/$subpkg.packaged ];then
						cp ${TMPDIR}/pkgdata/$pkg_subdir/runtime/$subpkg.packaged ${SDK_OUTPUT2}/${SDKPATH}/pkgdata/$pkg_subdir/runtime/
					fi
					if [ -e ${STAGING_DIR_TARGET}/shlibs/$subpkg.list ]; then
						cp ${STAGING_DIR_TARGET}/shlibs/$subpkg.* ${SDK_OUTPUT2}/${SDKPATH}/${TARGET_SYS}/shlibs/
					fi
				done
				break
			fi
		done
	done

	# Fix or remove broken .la files
	for i in `find ${SDK_OUTPUT}/${SDKPATH}/${TARGET_SYS} -name \*.la`; do
		sed -i 	-e "/^dependency_libs=/s,\([[:space:]']\)${base_libdir},\1${SDKPATH}/${TARGET_SYS}${base_libdir},g" \
			-e "/^dependency_libs=/s,\([[:space:]']\)${libdir},\1${SDKPATH}/${TARGET_SYS}${libdir},g" \
			-e "/^dependency_libs=/s,\-\([LR]\)${base_libdir},-\1${SDKPATH}/${TARGET_SYS}${base_libdir},g" \
			-e "/^dependency_libs=/s,\-\([LR]\)${libdir},-\1${SDKPATH}/${TARGET_SYS}${libdir},g" \
			-e 's/^installed=yes$/installed=no/' $i
	done
	rm -f ${SDK_OUTPUT}/${SDKPATH}/lib/*.la

	# Setup site file for external use
	siteconfig=${SDK_OUTPUT}/${SDKPATH}/site-config
	touch $siteconfig
	for sitefile in ${CONFIG_SITE} ; do
		cat $sitefile >> $siteconfig
	done

	# Create environment setup script
	script=${SDK_OUTPUT}/${SDKPATH}/environment-setup
	touch $script
	echo 'export PATH=${SDKPATH}/bin:$PATH' >> $script
	echo 'export PKG_CONFIG_SYSROOT_DIR=${SDKPATH}/${TARGET_SYS}' >> $script
	echo 'export PKG_CONFIG_PATH=${SDKPATH}/${TARGET_SYS}${libdir}/pkgconfig' >> $script
	echo 'export CONFIG_SITE=${SDKPATH}/site-config' >> $script
	echo 'export CC=${TARGET_PREFIX}gcc' >> $script
	echo 'export CONFIGURE_FLAGS="--target=${TARGET_SYS} --host=${TARGET_SYS} --build=${BUILD_SYS}"' >> $script
	if [ "${TARGET_OS}" = "darwin8" ]; then
		echo 'export TARGET_CFLAGS="-I${SDKPATH}/${TARGET_SYS}${includedir}"' >> $script
		echo 'export TARGET_LDFLAGS="-L${SDKPATH}/${TARGET_SYS}${libdir}"' >> $script
		# Workaround darwin toolchain sysroot path problems
		cd ${SDK_OUTPUT}${SDKPATH}/${TARGET_SYS}/usr
		ln -s /usr/local local
	fi
	echo "alias opkg='LD_LIBRARY_PATH=${SDKPATH}/lib ${SDKPATH}/bin/opkg-cl -f ${SDKPATH}/${sysconfdir}/opkg-sdk.conf -o ${SDKPATH}'" >> $script
	echo "alias opkg-target='LD_LIBRARY_PATH=${SDKPATH}/lib ${SDKPATH}/bin/opkg-cl -f ${SDKPATH}/${TARGET_SYS}${sysconfdir}/opkg.conf -o ${SDKPATH}/${TARGET_SYS}'" >> $script

	# Add version information
	versionfile=${SDK_OUTPUT}/${SDKPATH}/version
	touch $versionfile
	echo 'Distro: ${DISTRO}' >> $versionfile
	echo 'Distro Version: ${DISTRO_VERSION}' >> $versionfile
	echo 'Metadata Revision: ${METADATA_REVISION}' >> $versionfile
	echo 'Timestamp: ${DATETIME}' >> $versionfile

	# Package it up
	mkdir -p ${SDK_DEPLOY}
	cd ${SDK_OUTPUT}
	fakeroot tar cfj ${SDK_DEPLOY}/${TOOLCHAIN_OUTPUTNAME}.tar.bz2 .
	cd ${SDK_OUTPUT2}
	fakeroot tar cfj ${SDK_DEPLOY}/${TOOLCHAIN_OUTPUTNAME}-extras.tar.bz2 .
}

do_populate_sdk[nostamp] = "1"
do_populate_sdk[recrdeptask] = "do_package_write"
addtask populate_sdk before do_build after do_install
