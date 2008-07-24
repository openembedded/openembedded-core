PROVIDES = "\
    linux-libc-headers \
    virtual/${TARGET_PREFIX}gcc \
    virtual/${TARGET_PREFIX}gcc-intermediate \
    virtual/${TARGET_PREFIX}gcc-initial \
    virtual/${TARGET_PREFIX}binutils \
    virtual/${TARGET_PREFIX}libc-for-gcc \
    virtual/libc \
    virtual/libintl \
    virtual/libiconv \
    virtual/linux-libc-headers "
RPROVIDES = "glibc-utils libsegfault glibc-thread-db libgcc-dev libstdc++-dev libstdc++"
PACKAGES_DYNAMIC = "glibc-gconv-*"
INHIBIT_DEFAULT_DEPS = "1"
PR = "r1"

inherit sdk

do_stage() {
	if [ ! -e  ${prefix}/environment-setup ]; then
		echo "The Poky toolchain could not be found in ${prefix}!"
		exit 1
	fi

	install -d ${DEPLOY_DIR}/ipk/
	install -d ${STAGING_DIR}/pkgdata/
	install -d ${STAGING_DIR_TARGET}/shlibs/

	cp -ar ${prefix}/ipk/* ${DEPLOY_DIR}/ipk/
	cp -ar ${prefix}/pkgdata/* ${STAGING_DIR}/pkgdata/
	cp -ar ${prefix}/${TARGET_SYS}/shlibs/* ${STAGING_DIR_TARGET}/shlibs/
}

