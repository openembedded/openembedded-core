DESCRIPTION = "Meta package for building a installable toolchain"
LICENSE = "MIT"
DEPENDS = "ipkg-native ipkg-utils-native fakeroot-native sed-native"
PR = "r3"

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
    g++ \
    cpp \
    libgcc \
    libgcc-dev \
    libstdc++ \
    libstdc++-dev \
    gdb-cross \
    "

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
EOF
        cat <<EOF >${SDK_DIR}/ipkg-target.conf
src oe file:${DEPLOY_DIR_IPK}
EOF
	ipkgarchs="${PACKAGE_ARCHS}"
        priority=1
        for arch in $ipkgarchs; do
                echo "arch $arch $priority" >> ${SDK_DIR}/ipkg-target.conf
		echo "arch ${BUILD_ARCH}-$arch-sdk $priority" >> ${SDK_DIR}/ipkg-host.conf
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

	for fn in `ls ${SDK_OUTPUT}/${prefix}/${TARGET_SYS}/lib/`; do
		if [ -h ${SDK_OUTPUT}/${prefix}/${TARGET_SYS}/lib/$fn ]; then
			link=`readlink ${SDK_OUTPUT}/${prefix}/${TARGET_SYS}/lib/$fn`
			bname=`basename $link`
			if [ ! -e $link -a -e ${SDK_OUTPUT}/${prefix}/${TARGET_SYS}/lib/$bame ]; then
				rm ${SDK_OUTPUT}/${prefix}/${TARGET_SYS}/lib/$fn
				ln -s $bname ${SDK_OUTPUT}/${prefix}/${TARGET_SYS}/lib/$fn
			fi
		fi
	done

	mv ${SDK_OUTPUT}/${prefix}/${TARGET_SYS}/lib/gcc* ${SDK_OUTPUT}/${prefix}/lib

	cp -pPR ${TMPDIR}/cross/${TARGET_SYS}/include/linux/ ${SDK_OUTPUT}/${prefix}/${TARGET_SYS}/include/
        cp -pPR ${TMPDIR}/cross/${TARGET_SYS}/include/asm/ ${SDK_OUTPUT}/${prefix}/${TARGET_SYS}/include/
	chmod -R a+r ${SDK_OUTPUT}/${prefix}/${TARGET_SYS}/include/
	find ${SDK_OUTPUT}/${prefix}/${TARGET_SYS}/include/ -type d | xargs chmod +x

        echo 'GROUP ( libpthread.so.0 libpthread_nonshared.a )' > ${SDK_OUTPUT}/${prefix}/${TARGET_SYS}/lib/libpthread.so
        echo 'GROUP ( libc.so.6 libc_nonshared.a )' > ${SDK_OUTPUT}/${prefix}/${TARGET_SYS}/lib/libc.so

	# remove unwanted housekeeping files
	mv ${SDK_OUTPUT}${libdir}/../${TARGET_SYS}/lib/ipkg/status ${SDK_OUTPUT}/${prefix}/package-status
	rm -Rf ${SDK_OUTPUT}${libdir}/ipkg
	mv ${SDK_OUTPUT}/usr/lib/ipkg/status ${SDK_OUTPUT}/${prefix}/package-status-host
	rm -Rf ${SDK_OUTPUT}/usr/lib

	# extract and store ipks, pkgdata, pkgmaps and shlibs data
	target_pkgs=`cat ${SDK_OUTPUT}/${prefix}/package-status | grep Package: | cut -f 2 -d ' '`
	mkdir -p ${SDK_OUTPUT}/${prefix}/ipk/
	mkdir -p ${SDK_OUTPUT}/${prefix}/pkgdata/runtime/
	mkdir -p ${SDK_OUTPUT}/${prefix}/pkgmaps/debian/
	mkdir -p ${SDK_OUTPUT}/${prefix}/${TARGET_SYS}/shlibs/
	for pkg in $target_pkgs ; do
    		for arch in $ipkgarchs; do
            		if [ -e ${DEPLOY_DIR_IPK}/${pkg}_*_$arch.ipk ]; then
				echo "Found ${DEPLOY_DIR_IPK}/${pkg}_$arch.ipk"
				cp ${DEPLOY_DIR_IPK}/${pkg}_*_$arch.ipk ${SDK_OUTPUT}/${prefix}/ipk/
				orig_pkg=`ipkg-list-fields ${DEPLOY_DIR_IPK}/${pkg}_*_$arch.ipk | grep OE: | cut -d ' ' -f2`
				cp ${STAGING_DIR}/pkgdata/$orig_pkg ${SDK_OUTPUT}/${prefix}/pkgdata/
				subpkgs=`cat ${STAGING_DIR}/pkgdata/$orig_pkg | grep PACKAGES: | cut -b 10-`
				for subpkg in $subpkgs; do
					cp ${STAGING_DIR}/pkgdata/runtime/$subpkg ${SDK_OUTPUT}/${prefix}/pkgdata/runtime/
					if [ -e ${STAGING_DIR}/pkgdata/runtime/$subpkg.packaged ];then
						cp ${STAGING_DIR}/pkgdata/runtime/$subpkg.packaged ${SDK_OUTPUT}/${prefix}/pkgdata/runtime/
					fi
					if [ -e ${STAGING_DIR}/pkgmaps/debian/$subpkg ]; then
						cp ${STAGING_DIR}/pkgmaps/debian/$subpkg ${SDK_OUTPUT}/${prefix}/pkgmaps/debian/
					fi
					if [ -e ${STAGING_DIR}/${TARGET_SYS}/shlibs/$subpkg.list ]; then
						cp ${STAGING_DIR}/${TARGET_SYS}/shlibs/$subpkg.* ${SDK_OUTPUT}/${prefix}/${TARGET_SYS}/shlibs/
					fi
				done
				break
			fi
		done
	done


	# remove unwanted executables
	rm -rf ${SDK_OUTPUT}/${prefix}/sbin ${SDK_OUTPUT}/${prefix}/etc

	# remove broken .la files
	rm -f ${SDK_OUTPUT}/${prefix}/${TARGET_SYS}/lib/*.la

        mkdir -p ${SDK_DEPLOY}
	cd ${SDK_OUTPUT}
	fakeroot tar cfj ${SDK_DEPLOY}/${DISTRO}-${DISTRO_VERSION}-${TARGET_ARCH}-toolchain.tar.bz2 .
}

do_populate_sdk[nostamp] = "1"
do_populate_sdk[recrdeptask] = "do_package_write"
addtask populate_sdk before do_build after do_install
