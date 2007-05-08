DESCRIPTION = "Meta package for building a installable toolchain"
LICENSE = "MIT"
DEPENDS = "ipkg-native ipkg-utils-native fakeroot-native sed-native"
PR = "r0"

inherit sdk

PACKAGES = ""

BUILD_ALL_DEPS = "1"
do_build[recrdeptask] = "do_build"

SDK_DIR = "${WORKDIR}/sdk"
SDK_OUTPUT = "${SDK_DIR}/image"
SDK_DEPLOY = "${TMPDIR}/deploy/sdk"

IPKG_HOST = "ipkg-cl -f ${SDK_DIR}/ipkg-host.conf -o ${SDK_OUTPUT}"
IPKG_TARGET = "ipkg-cl -f ${SDK_DIR}/ipkg-target.conf -o ${SDK_OUTPUT}/${prefix}"

HOST_INSTALL = "\
    binutils-cross-sdk \
    gcc-cross-sdk \
    gdb-cross"
TARGET_INSTALL = "\
    task-sdk-bare \
    "

RDEPENDS = "${TARGET_INSTALL} ${HOST_INSTALL}"

do_populate_sdk() {
	touch ${DEPLOY_DIR_IPK}/Packages
	ipkg-make-index -r ${DEPLOY_DIR_IPK}/Packages -p ${DEPLOY_DIR_IPK}/Packages -l ${DEPLOY_DIR_IPK}/Packages.filelist -m ${DEPLOY_DIR_IPK}

	rm -rf ${SDK_OUTPUT}
	mkdir -p ${SDK_OUTPUT}

	cat <<EOF >${SDK_DIR}/ipkg-host.conf
src oe file:${DEPLOY_DIR_IPK}
arch ${BUILD_ARCH} 1
EOF
        cat <<EOF >${SDK_DIR}/ipkg-target.conf
src oe file:${DEPLOY_DIR_IPK}
EOF
	ipkgarchs="${PACKAGE_ARCHS}"
        priority=1
        for arch in $ipkgarchs; do
                echo "arch $arch $priority" >> ${SDK_DIR}/ipkg-target.conf
	        priority=$(expr $priority + 5)
        done

	rm -r ${SDK_OUTPUT}
	mkdir -p ${SDK_OUTPUT}

	${IPKG_HOST} update
	${IPKG_HOST} -nodeps install ${HOST_INSTALL}

	${IPKG_TARGET} update
	${IPKG_TARGET} install ${TARGET_INSTALL}

	mkdir -p ${SDK_OUTPUT}/${prefix}/${TARGET_SYS}
	cp -pPR ${SDK_OUTPUT}/${prefix}/usr/* ${SDK_OUTPUT}/${prefix}/${TARGET_SYS}
	rm -rf ${SDK_OUTPUT}/${prefix}/usr/

        cp -pPR ${SDK_OUTPUT}/${prefix}/lib/* ${SDK_OUTPUT}/${prefix}/${TARGET_SYS}/lib
        rm -rf ${SDK_OUTPUT}/${prefix}/lib/*

	mv ${SDK_OUTPUT}/${prefix}/${TARGET_SYS}/lib/gcc* ${SDK_OUTPUT}/${prefix}/lib

	cp -pPR ${TMPDIR}/cross/${TARGET_SYS}/include/linux/ ${SDK_OUTPUT}/${prefix}/${TARGET_SYS}/include/
        cp -pPR ${TMPDIR}/cross/${TARGET_SYS}/include/asm/ ${SDK_OUTPUT}/${prefix}/${TARGET_SYS}/include/
	chmod -R a+r ${SDK_OUTPUT}/${prefix}/${TARGET_SYS}/include/
	find ${SDK_OUTPUT}/${prefix}/${TARGET_SYS}/include/ -type d | xargs chmod +x

        echo 'GROUP ( libpthread.so.0 libpthread_nonshared.a )' > ${SDK_OUTPUT}/${prefix}/${TARGET_SYS}/lib/libpthread.so
        echo 'GROUP ( libc.so.6 libc_nonshared.a )' > ${SDK_OUTPUT}/${prefix}/${TARGET_SYS}/lib/libc.so
	# remove unwanted housekeeping files
	mv ${SDK_OUTPUT}${libdir}/../${TARGET_SYS}/lib/ipkg/status ${SDK_OUTPUT}/${prefix}/package-status
	rm -rf ${SDK_OUTPUT}${libdir}/ipkg

	# remove unwanted executables
	rm -rf ${SDK_OUTPUT}/${prefix}/sbin ${SDK_OUTPUT}/${prefix}/etc

	# remove broken .la files
	rm ${SDK_OUTPUT}/${prefix}/${TARGET_SYS}/lib/*.la

        mkdir -p ${SDK_DEPLOY}
	cd ${SDK_OUTPUT}
	fakeroot tar cfj ${SDK_DEPLOY}/${DISTRO}-${DISTRO_VERSION}-${TARGET_ARCH}-toolchain.tar.bz2 .
}

do_populate_sdk[nostamp] = "1"
do_populate_sdk[recrdeptask] = "do_package_write"
addtask populate_sdk before do_build after do_install
